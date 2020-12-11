import get from 'lodash.get'

import MockClient from '../../../../dev/api.mock.json'
import {
  getDefaultValues,
  getLists,
  getProject,
  getShareUrl,
  parseParams,
} from '../ApiUtils'

/**
 * Function getDefaultValues
 */
describe('getDefaultValues', () => {
  it('parse correctly', () => {
    const json = { ...MockClient }
    const defaultValues = getDefaultValues(json)
    expect(get(defaultValues, 'project')).toBe(get(MockClient, 'type.default'))
    expect(get(defaultValues, 'boot')).toBe(
      get(MockClient, 'bootVersion.default')
    )
    expect(get(defaultValues, 'language')).toBe(
      get(MockClient, 'language.default')
    )
    expect(get(defaultValues, 'meta.name')).toBe(
      get(MockClient, 'name.default')
    )
    expect(get(defaultValues, 'meta.group')).toBe(
      get(MockClient, 'groupId.default')
    )
    expect(get(defaultValues, 'meta.artifact')).toBe(
      get(MockClient, 'artifactId.default')
    )
    expect(get(defaultValues, 'meta.description')).toBe(
      get(MockClient, 'description.default')
    )
    expect(get(defaultValues, 'meta.packaging')).toBe(
      get(MockClient, 'packaging.default')
    )
    expect(get(defaultValues, 'meta.packageName')).toBe(
      get(MockClient, 'packageName.default')
    )
    expect(get(defaultValues, 'meta.java')).toBe(
      get(MockClient, 'javaVersion.default')
    )
  })
})

/**
 * Function getListValues
 */
describe('getListValues', () => {
  it('parse correctly the list of projects', () => {
    const json = { ...MockClient }
    const listsValues = getLists(json)
    const listProjects = get(listsValues, 'project')
    const mockProjects = get(MockClient, 'type.values').filter(
      project => project.action === '/starter.zip'
    )
    expect(listProjects.length).toBe(mockProjects.length)
    for (let i = 0; i < mockProjects.length; i += 1) {
      expect(listProjects[i].key).toBe(mockProjects[i].id)
      expect(listProjects[i].text).toBe(mockProjects[i].name)
    }
  })

  it('parse correctly the list of languages', () => {
    const json = { ...MockClient }
    const listsValues = getLists(json)
    const listLanguages = get(listsValues, 'language')
    const mockLanguages = get(MockClient, 'language.values')
    expect(listLanguages.length).toBe(mockLanguages.length)
    for (let i = 0; i < mockLanguages.length; i += 1) {
      expect(listLanguages[i].key).toBe(mockLanguages[i].id)
      expect(listLanguages[i].text).toBe(mockLanguages[i].name)
    }
  })

  it('parse correctly the list of boot versions', () => {
    const json = { ...MockClient }
    const listsValues = getLists(json)
    const listBootVersions = get(listsValues, 'boot')
    const mockBootVersions = get(MockClient, 'bootVersion.values')
    expect(listBootVersions.length).toBe(mockBootVersions.length)
    for (let i = 0; i < mockBootVersions.length; i += 1) {
      expect(listBootVersions[i].key).toBe(mockBootVersions[i].id)
      expect(listBootVersions[i].text).toBe(mockBootVersions[i].name)
    }
  })

  it('parse correctly the list of java versions', () => {
    const json = { ...MockClient }
    const listsValues = getLists(json)
    const listJavaVersions = get(listsValues, 'meta.java')
    const mockJavaVersions = get(MockClient, 'javaVersion.values')
    expect(listJavaVersions.length).toBe(mockJavaVersions.length)
    for (let i = 0; i < mockJavaVersions.length; i += 1) {
      expect(listJavaVersions[i].key).toBe(mockJavaVersions[i].id)
      expect(listJavaVersions[i].text).toBe(mockJavaVersions[i].name)
    }
  })

  it('parse correctly the list of packagings', () => {
    const json = { ...MockClient }
    const listsValues = getLists(json)
    const listPackagings = get(listsValues, 'meta.packaging')
    const mockPackagings = get(MockClient, 'packaging.values')
    expect(listPackagings.length).toBe(mockPackagings.length)
    for (let i = 0; i < mockPackagings.length; i += 1) {
      expect(listPackagings[i].key).toBe(mockPackagings[i].id)
      expect(listPackagings[i].text).toBe(mockPackagings[i].name)
    }
  })

  it('parse correctly the list of dependencies', () => {
    const json = { ...MockClient }
    const listsValues = getLists(json)
    const listDependencies = get(listsValues, 'dependencies')
    const mockDependencies = get(MockClient, 'dependencies.values').reduce(
      (p, c) => {
        p.push(...get(c, 'values'))
        return p
      },
      []
    )
    expect(listDependencies.length).toBe(mockDependencies.length)
    for (let i = 0; i < mockDependencies.length; i += 1) {
      expect(listDependencies[i].id).toBe(mockDependencies[i].id)
      expect(listDependencies[i].name).toBe(mockDependencies[i].name)
      expect(listDependencies[i].description).toBe(
        mockDependencies[i].description
      )
    }
  })
})

/**
 * Function parseParams
 */
describe('parseParams', () => {
  it('return parameters, no warning and no error', () => {
    const json = { ...MockClient }
    const defaultValues = getDefaultValues(json)
    const listsValues = getLists(json)

    const result = parseParams(
      defaultValues,
      {
        type: 'gradle-project',
        language: 'kotlin',
        platformVersion: get(defaultValues, 'boot'),
        packaging: 'war',
        jvmVersion: '11',
        groupId: 'com.example2',
        artifactId: 'demo2',
        name: 'demo2',
        description: 'Demo project for Spring Boot2',
        packageName: 'com.example2.demo2',
        dependencies: ' devtools , lombok ',
      },
      listsValues
    )
    expect(Object.keys(result.warnings).length).toBe(0)
    expect(Object.keys(result.errors).length).toBe(0)

    expect(get(result, 'values.project')).toBe('gradle-project')
    expect(get(result, 'values.language')).toBe('kotlin')
    expect(get(result, 'values.boot')).toBe(get(defaultValues, 'boot'))
    expect(get(result, 'values.meta.packaging')).toBe('war')
    expect(get(result, 'values.meta.java')).toBe('11')
    expect(get(result, 'values.meta.group')).toBe('com.example2')
    expect(get(result, 'values.meta.artifact')).toBe('demo2')
    expect(get(result, 'values.meta.name')).toBe('demo2')
    expect(get(result, 'values.meta.description')).toBe(
      'Demo project for Spring Boot2'
    )
    expect(get(result, 'values.meta.packageName')).toBe('com.example2.demo2')
    expect(get(result, 'values.dependencies').length).toBe(2)
  })

  it('return parameters, warnings and no error', () => {
    const json = { ...MockClient }
    const defaultValues = getDefaultValues(json)
    const listsValues = getLists(json)

    const result = parseParams(
      defaultValues,
      {
        type: 'ant-project',
        language: 'php',
        platformVersion: get(defaultValues, 'boot'),
        packaging: 'tar',
        jvmVersion: '1',
        groupId: 'com.example',
        artifactId: 'demo',
        name: 'demo',
        description: 'Demo project for Spring Boot',
        packageName: 'com.example.demo',
        dependencies: 'a,devtools,b,lombok',
      },
      listsValues
    )
    expect(get(result, 'warnings.project.value')).toBe('ant-project')
    expect(get(result, 'warnings.language.value')).toBe('php')
    expect(get(result, 'warnings.meta.packaging.value')).toBe('tar')
    expect(get(result, 'warnings.meta.java.value')).toBe('1')

    expect(get(result, 'values.meta.group')).toBe('com.example')
    expect(get(result, 'values.meta.artifact')).toBe('demo')
    expect(get(result, 'values.meta.name')).toBe('demo')
    expect(get(result, 'values.meta.description')).toBe(
      'Demo project for Spring Boot'
    )

    expect(get(result, 'values.dependencies').length).toBe(2)
    expect(get(result, 'warnings.dependencies.value')).toBe('a, b')

    expect(get(result, 'values.project')).toBe('maven-project')
    expect(get(result, 'values.language')).toBe('java')
    expect(get(result, 'values.boot')).toBe(get(defaultValues, 'boot'))
    expect(get(result, 'values.meta.packaging')).toBe('jar')
    expect(get(result, 'values.meta.java')).toBe('1.8')
  })

  it('return parameters, no warning and error', () => {
    const json = { ...MockClient }
    const defaultValues = getDefaultValues(json)
    const listsValues = getLists(json)

    const result = parseParams(
      defaultValues,
      {
        type: 'gradle-project',
        language: 'kotlin',
        platformVersion: '1.1.1',
        packaging: 'war',
        jvmVersion: '11',
        groupId: 'com.example2',
        artifactId: 'demo2',
        name: 'demo2',
        description: 'Demo project for Spring Boot2',
        packageName: 'com.example2.demo2',
        dependencies: ' devtools , lombok ',
      },
      listsValues
    )
    expect(Object.keys(result.warnings).length).toBe(0)
    expect(Object.keys(result.errors).length).toBe(1)

    expect(get(result, 'values.project')).toBe('gradle-project')
    expect(get(result, 'values.language')).toBe('kotlin')
    expect(get(result, 'values.boot')).toBe(get(defaultValues, 'boot'))
    expect(get(result, 'values.meta.packaging')).toBe('war')
    expect(get(result, 'values.meta.java')).toBe('11')
    expect(get(result, 'values.meta.group')).toBe('com.example2')
    expect(get(result, 'values.meta.artifact')).toBe('demo2')
    expect(get(result, 'values.meta.name')).toBe('demo2')
    expect(get(result, 'values.meta.description')).toBe(
      'Demo project for Spring Boot2'
    )
    expect(get(result, 'values.meta.packageName')).toBe('com.example2.demo2')
    expect(get(result, 'values.dependencies').length).toBe(2)
    expect(get(result, 'errors.boot.value')).toBe('1.1.1')
  })

  it('return parameters, warnings and error', () => {
    const json = { ...MockClient }
    const defaultValues = getDefaultValues(json)
    const listsValues = getLists(json)

    const result = parseParams(
      defaultValues,
      {
        type: 'ant-project',
        language: 'php',
        platformVersion: '1.1.1',
        packaging: 'tar',
        jvmVersion: '1',
        groupId: 'com.example',
        artifactId: 'demo',
        name: 'demo',
        description: 'Demo project for Spring Boot',
        packageName: 'com.example.demo',
        dependencies: 'a,devtools,b,lombok',
      },
      listsValues
    )
    expect(get(result, 'warnings.project.value')).toBe('ant-project')
    expect(get(result, 'warnings.language.value')).toBe('php')
    expect(get(result, 'warnings.meta.packaging.value')).toBe('tar')
    expect(get(result, 'warnings.meta.java.value')).toBe('1')

    expect(get(result, 'errors.boot.value')).toBe('1.1.1')

    expect(get(result, 'values.meta.group')).toBe('com.example')
    expect(get(result, 'values.meta.artifact')).toBe('demo')
    expect(get(result, 'values.meta.name')).toBe('demo')
    expect(get(result, 'values.meta.description')).toBe(
      'Demo project for Spring Boot'
    )

    expect(get(result, 'values.dependencies').length).toBe(2)
    expect(get(result, 'warnings.dependencies.value')).toBe('a, b')

    expect(get(result, 'values.project')).toBe('maven-project')
    expect(get(result, 'values.language')).toBe('java')
    expect(get(result, 'values.boot')).toBe(get(defaultValues, 'boot'))
    expect(get(result, 'values.meta.packaging')).toBe('jar')
    expect(get(result, 'values.meta.java')).toBe('1.8')
  })
})

/**
 * Function getShareUrl
 */
describe('getShareUrl', () => {
  it('return the base URL with custom values', () => {
    const result = getShareUrl({
      project: 'foo1',
      language: 'foo2',
      boot: 'foo3',
      meta: {
        packaging: 'foo4',
        java: 'foo5',
        group: 'foo6',
        artifact: 'foo7',
        name: 'foo8',
        description: 'foo9',
        packageName: 'foo10',
      },
      dependencies: ['foo11', 'foo12'],
    })
    expect(result).toBe(
      'type=foo1&language=foo2&platformVersion=foo3&packaging=foo4&jvmVersion=foo5&groupId=foo6&artifactId=foo7&name=foo8&description=foo9&packageName=foo10&dependencies=foo11,foo12'
    )
  })
})

/**
 * Get Project
 */
describe('getProject', () => {
  beforeEach(() => {
    fetch.resetMocks()
    fetch.mockResponseOnce(JSON.stringify({}))
  })
  it('should call the right service with the right parameters', () => {
    const values = {
      project: 'foo1',
      language: 'foo2',
      boot: 'foo3',
      meta: {
        packaging: 'foo4',
        java: 'foo5',
        group: 'foo6',
        artifact: 'foo7',
        name: 'foo8',
        description: 'foo9',
        packageName: 'foo10',
      },
      dependencies: ['foo11', 'foo12'],
    }
    getProject('http://demo/starter.zip', values, [
      { id: 'foo11' },
      { id: 'foo12' },
    ])
    expect(fetch.mock.calls.length).toEqual(1)
    expect(fetch.mock.calls[0][0]).toEqual(
      'http://demo/starter.zip?type=foo1&language=foo2&bootVersion=foo3&baseDir=foo7&groupId=foo6&artifactId=foo7&name=foo8&description=foo9&packageName=foo10&packaging=foo4&javaVersion=foo5&dependencies=foo11,foo12'
    )
  })

  it('should call the right service with the right parameters (clean invalid dependencies)', () => {
    const values = {
      project: 'foo1',
      language: 'foo2',
      boot: 'foo3',
      meta: {
        packaging: 'foo4',
        java: 'foo5',
        group: 'foo6',
        artifact: 'foo7',
        name: 'foo8',
        description: 'foo9',
        packageName: 'foo10',
      },
      dependencies: ['foo11', 'foo12'],
    }
    getProject('http://demo/starter.zip', values, [{ id: 'foo11' }])
    expect(fetch.mock.calls.length).toEqual(1)
    expect(fetch.mock.calls[0][0]).toEqual(
      'http://demo/starter.zip?type=foo1&language=foo2&bootVersion=foo3&baseDir=foo7&groupId=foo6&artifactId=foo7&name=foo8&description=foo9&packageName=foo10&packaging=foo4&javaVersion=foo5&dependencies=foo11'
    )
  })

  it('should call the right service with the right parameters (no dependency)', () => {
    const values = {
      project: 'foo1',
      language: 'foo2',
      boot: 'foo3',
      meta: {
        packaging: 'foo4',
        java: 'foo5',
        group: 'foo6',
        artifact: 'foo7',
        name: 'foo8',
        description: 'foo9',
        packageName: 'foo10',
      },
    }
    getProject('http://demo/starter.zip', values, [])
    expect(fetch.mock.calls.length).toEqual(1)
    expect(fetch.mock.calls[0][0]).toEqual(
      'http://demo/starter.zip?type=foo1&language=foo2&bootVersion=foo3&baseDir=foo7&groupId=foo6&artifactId=foo7&name=foo8&description=foo9&packageName=foo10&packaging=foo4&javaVersion=foo5'
    )
  })
})
