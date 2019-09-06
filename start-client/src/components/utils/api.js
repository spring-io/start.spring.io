import get from 'lodash.get'
import querystring from 'querystring'
import set from 'lodash.set'

import META_EXTEND from '../../data/meta-extend.json'

const WEIGHT_DEFAULT = 50

const PROPERTIES_MAPPING_URL = {
  type: 'project',
  language: 'language',
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

  Object.assign(meta, params.meta, get(params, 'values.meta', {}))
  set(params, 'values.meta', meta)
  Object.assign(vals, params.values)

  return {
    dependencies: [],
    warnings: Object.assign({}, params.warnings),
    values: vals,
  }
}

export const parseParametersUrl = (queryParams, lists) => {
  const values = {}
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
    warnings,
  }
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
