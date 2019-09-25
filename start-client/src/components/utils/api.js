import get from 'lodash.get'
import querystring from 'querystring'
import set from 'lodash.set'

import META_EXTEND from '../../data/meta-extend.json'
import { parseReleases, parseVersion } from './versions'

const WEIGHT_DEFAULT = 50

const PROPERTIES_MAPPING_URL = {
  type: 'project',
  language: 'language',
  platformVersion: 'boot',
  packaging: 'meta.packaging',
  jvmVersion: 'meta.java',
  groupId: 'meta.group',
  artifactId: 'meta.artifact',
  name: 'meta.name',
  description: 'meta.description',
  packageName: 'meta.packageName',
}

export const getDefaultValues = json => {
  return {
    project: get(json, 'type.default'),
    language: get(json, 'language.default'),
    boot: get(json, 'bootVersion.default'),
    meta: {
      name: get(json, 'name.default'),
      group: get(json, 'groupId.default'),
      artifact: get(json, 'artifactId.default'),
      description: get(json, 'description.default'),
      packaging: get(json, 'packaging.default'),
      packageName: get(json, 'packageName.default'),
      java: get(json, 'javaVersion.default'),
    },
  }
}

export const getListsValues = json => {
  const deps = []
  get(json, 'dependencies.values', []).forEach(group => {
    group.values.forEach(item => {
      const metaExtend = META_EXTEND.find(meta => get(meta, 'id') === item.id)
      const val = {
        id: `${get(item, 'id', '')}`,
        name: `${get(item, 'name', '')}`,
        group: `${group.name}`,
        weight: get(metaExtend, `weight`, WEIGHT_DEFAULT),
        description: `${get(item, 'description', '')}`,
        versionRange: `${get(item, 'versionRange', '')}`,
        versionRequirement: `${get(item, 'versionRange', '')}`,
      }
      deps.push(val)
    })
  })
  return {
    project: get(json, 'type.values', [])
      .filter(type => type.action === '/starter.zip')
      .map(type => ({
        key: `${type.id}`,
        text: `${type.name}`,
      })),
    language: get(json, 'language.values', []).map(language => ({
      key: `${language.id}`,
      text: `${language.name}`,
    })),
    boot: get(json, 'bootVersion.values', []).map(boot => ({
      key: `${boot.id}`,
      text: `${boot.name}`,
    })),
    meta: {
      java: get(json, 'javaVersion.values', []).map(java => ({
        key: `${java.id}`,
        text: `${java.name}`,
      })),
      packaging: get(json, 'packaging.values', []).map(packaging => ({
        key: `${packaging.id}`,
        text: `${packaging.name}`,
      })),
    },
    dependencies: deps,
  }
}

export const parseParams = (values, queryParams, lists) => {
  const vals = Object.assign({}, values)
  const meta = Object.assign({}, values.meta)
  const params = parseParametersUrl(queryParams, lists)
  const paramsDeps = parseParametersDepsUrl(queryParams, lists)
  Object.assign(meta, params.meta, get(params, 'values.meta', {}))
  set(params, 'values.meta', meta)
  Object.assign(vals, params.values)
  return {
    dependencies: paramsDeps.values,
    errors: params.errors,
    warnings: Object.assign({}, paramsDeps.warnings, params.warnings),
    values: vals,
  }
}

export const isValidParams = params => {
  return (
    Object.keys(params)
      .map(entry => {
        const key = get(PROPERTIES_MAPPING_URL, entry, null)
        return key !== null || entry === 'dependencies'
      })
      .filter(item => !!item).length > 0
  )
}

export const parseParametersUrl = (queryParams, lists) => {
  const values = {}
  const errors = {}
  const warnings = {}
  Object.keys(queryParams).forEach(entry => {
    const key = get(PROPERTIES_MAPPING_URL, entry)
    if (key) {
      const value = get(queryParams, entry, '').toLowerCase()
      switch (key) {
        case 'project':
        case 'language':
        case 'meta.packaging':
        case 'meta.java':
          const vals = get(lists, key, [])
          const res = vals.find(a => a.key.toLowerCase() === value)
          if (res) {
            set(values, key, res.key)
          } else {
            set(warnings, key, {
              value: get(queryParams, entry, ''),
            })
          }
          break
        case 'boot':
          const vals2 = get(lists, key, [])
          const res2 = vals2.find(a => a.key.toLowerCase() === value)
          if (res2) {
            set(values, key, res2.key)
          } else {
            let versionMajor = value
            if (versionMajor.indexOf('.x') === -1) {
              versionMajor = get(parseVersion(versionMajor), 'major', '')
            }
            if (versionMajor.indexOf('.x') > -1) {
              const releases = parseReleases(vals2).filter(release => {
                return (
                  release.major.toLowerCase() === versionMajor.toLowerCase()
                )
              })
              if (releases.length > 0) {
                const release = releases.reduce((p, c) => {
                  if (p.qualify > c.qualify) {
                    return p
                  }
                  if (p.qualify === c.qualify) {
                    if (p.minor > c.minor) {
                      return p
                    }
                  }
                  return c
                }, releases[0])

                if (release) {
                  set(values, key, release.version)
                  set(warnings, key, {
                    value: get(queryParams, entry, ''),
                  })
                }
              }
            }
          }
          if (!get(values, 'boot')) {
            set(errors, 'boot', {
              value: get(queryParams, entry, ''),
              code: 'invalid',
            })
          }
          break
        default:
          set(values, key, value)
      }
    }
  })
  return {
    values,
    errors,
    warnings,
  }
}

export const parseParametersDepsUrl = (queryParams, lists) => {
  const depsWarning = []
  const params = {
    dependencies: 'dependencies',
  }
  const result = {
    values: [],
    warnings: {},
  }
  for (let entry in queryParams) {
    const key = get(params, entry)
    if (key === 'dependencies') {
      const value = get(queryParams, entry, '').toLowerCase()
      result.values = value
        .split(',')
        .map(item => {
          const dep = get(lists, 'dependencies').find(
            dep => dep.id === item.trim()
          )
          if (dep) {
            return dep
          } else {
            depsWarning.push({ value: item, code: 'invalid' })
          }
          return null
        })
        .filter(item => !!item)
    }
  }
  if (depsWarning.length > 0) {
    result.warnings = { dependencies: depsWarning }
  }
  return result
}

export const getShareUrl = (path, values, rootValues, short) => {
  const props = {}
  for (var key in PROPERTIES_MAPPING_URL) {
    const key2 = get(PROPERTIES_MAPPING_URL, key)
    const value = get(values, key2)
    set(props, key, value)
  }
  const propertiesDepToUrl = get(values, 'dependencies', [])
    .map(item => {
      return item.id
    })
    .join(',')

  let params = `${querystring.stringify(props)}${propertiesDepToUrl ? '&' : ''}`

  if (propertiesDepToUrl) {
    params = `${params}dependencies=${propertiesDepToUrl}`
  }

  if (!short) {
    return `${path}/#!${params}`
  }

  return params
}

export const getInfo = function getInfo(url) {
  return new Promise((resolve, reject) => {
    fetch(`${url}`, {
      method: 'GET',
      headers: {
        Accept: 'application/vnd.initializr.v2.1+json',
      },
    })
      .then(
        response => response.json(),
        error => {
          reject()
          return null
        }
      )
      .then(data => {
        if (data) {
          resolve(data)
        }
      })
  })
}

export const getProject = function getProject(
  url,
  project,
  language,
  boot,
  meta,
  dependencies
) {
  return new Promise((resolve, reject) => {
    const params = querystring.stringify({
      type: project,
      language: language,
      bootVersion: boot,
      baseDir: meta.artifact,
      groupId: meta.group,
      artifactId: meta.artifact,
      name: meta.name,
      description: meta.description,
      packageName: meta.packageName,
      packaging: meta.packaging,
      javaVersion: meta.java,
    })
    const paramsDependencies = (dependencies || [])
      .map(dep => `&dependencies=${dep.id}`)
      .join('')
    fetch(`${url}?${params}${paramsDependencies}`, {
      method: 'GET',
    }).then(
      response => {
        if (response.status === 200) {
          resolve(response.blob())
          return
        }
        reject()
      },
      error => {
        reject()
      }
    )
  })
}

export const getWarningText = function getWarningText(
  values,
  currentValues,
  lists
) {
  let warnings = []
  const keys = Object.keys(values || {})
  if (keys.length > 0) {
    for (let key of keys) {
      const value = get(values, key)
      let currentValue
      if (key === 'dependencies') {
        warnings.push({
          order: 5,
          text: `The following dependencies are not supported: <strong>${value.map(
            dep => dep.value
          )}</strong>.`,
        })
      }
      if (key === 'project' || key === 'language' || key === 'boot') {
        currentValue = get(lists, key).find(
          item => item.key === get(currentValues, key)
        ).text
      }
      if (key === 'project') {
        warnings.push({
          order: 1,
          text: `<strong>${value.value}</strong> is not a valid project type, <strong>${currentValue}</strong> has been selected.`,
        })
      }
      if (key === 'language') {
        warnings.push({
          order: 2,
          text: `<strong>${value.value}</strong> is not a valid language, <strong>${currentValue}</strong> has been selected.`,
        })
      }
      if (key === 'boot') {
        warnings.push({
          order: 3,
          text: `Spring Boot <strong>${value.value}</strong> is not available, <strong>${currentValue}</strong> has been selected.`,
        })
      }
      if (key === 'meta') {
        if (get(value, 'java')) {
          currentValue = get(lists, 'meta.java').find(
            item => item.key === get(currentValues, 'meta.java')
          ).text

          warnings.push({
            order: 4,
            text: `<strong>${get(
              value,
              'java.value'
            )}</strong> is not a valid Java version, <strong>${currentValue}</strong> has been selected.`,
          })
        }
        if (get(value, 'packaging')) {
          currentValue = get(lists, 'meta.packaging').find(
            item => item.key === get(currentValues, 'meta.packaging')
          ).text

          warnings.push({
            order: 5,
            text: `<strong>${get(
              value,
              'packaging.value'
            )}</strong> is not a valid packaging, <strong>${currentValue}</strong> has been selected.`,
          })
        }
      }
    }
  }
  return warnings
}
