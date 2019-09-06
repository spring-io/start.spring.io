import get from 'lodash.get'

import { MockClient } from '../../../../__mocks__/api'
import {
  getDefaultValues,
  getListsValues,
  parseParametersUrl,
  parseParams,
} from '../api'

/**
 * Function getDefaultValues
 */
describe('getDefaultValues', () => {
  it('parse correctly', () => {
    const json = Object.assign({}, MockClient)
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
    const json = Object.assign({}, MockClient)
    const listsValues = getListsValues(json)
    const listProjects = get(listsValues, 'project')
    const mockProjects = get(MockClient, 'type.values').filter(
      project => project.action === '/starter.zip'
    )
    expect(listProjects.length).toBe(mockProjects.length)
    for (let i = 0; i < mockProjects.length; i++) {
      expect(listProjects[i].key).toBe(mockProjects[i].id)
      expect(listProjects[i].text).toBe(mockProjects[i].name)
    }
  })

  it('parse correctly the list of languages', () => {
    const json = Object.assign({}, MockClient)
    const listsValues = getListsValues(json)
    const listLanguages = get(listsValues, 'language')
    const mockLanguages = get(MockClient, 'language.values')
    expect(listLanguages.length).toBe(mockLanguages.length)
    for (let i = 0; i < mockLanguages.length; i++) {
      expect(listLanguages[i].key).toBe(mockLanguages[i].id)
      expect(listLanguages[i].text).toBe(mockLanguages[i].name)
    }
  })

  it('parse correctly the list of boot versions', () => {
    const json = Object.assign({}, MockClient)
    const listsValues = getListsValues(json)
    const listBootVersions = get(listsValues, 'boot')
    const mockBootVersions = get(MockClient, 'bootVersion.values')
    expect(listBootVersions.length).toBe(mockBootVersions.length)
    for (let i = 0; i < mockBootVersions.length; i++) {
      expect(listBootVersions[i].key).toBe(mockBootVersions[i].id)
      expect(listBootVersions[i].text).toBe(mockBootVersions[i].name)
    }
  })

  it('parse correctly the list of java versions', () => {
    const json = Object.assign({}, MockClient)
    const listsValues = getListsValues(json)
    const listJavaVersions = get(listsValues, 'meta.java')
    const mockJavaVersions = get(MockClient, 'javaVersion.values')
    expect(listJavaVersions.length).toBe(mockJavaVersions.length)
    for (let i = 0; i < mockJavaVersions.length; i++) {
      expect(listJavaVersions[i].key).toBe(mockJavaVersions[i].id)
      expect(listJavaVersions[i].text).toBe(mockJavaVersions[i].name)
    }
  })

  it('parse correctly the list of packagings', () => {
    const json = Object.assign({}, MockClient)
    const listsValues = getListsValues(json)
    const listPackagings = get(listsValues, 'meta.packaging')
    const mockPackagings = get(MockClient, 'packaging.values')
    expect(listPackagings.length).toBe(mockPackagings.length)
    for (let i = 0; i < mockPackagings.length; i++) {
      expect(listPackagings[i].key).toBe(mockPackagings[i].id)
      expect(listPackagings[i].text).toBe(mockPackagings[i].name)
    }
  })

  it('parse correctly the list of dependencies', () => {
    const json = Object.assign({}, MockClient)
    const listsValues = getListsValues(json)
    const listDependencies = get(listsValues, 'dependencies')
    const mockDependencies = get(MockClient, 'dependencies.values').reduce(
      (p, c) => {
        p.push(...get(c, 'values'))
        return p
      },
      []
    )
    expect(listDependencies.length).toBe(mockDependencies.length)
    for (let i = 0; i < mockDependencies.length; i++) {
      expect(listDependencies[i].id).toBe(mockDependencies[i].id)
      expect(listDependencies[i].name).toBe(mockDependencies[i].name)
      expect(listDependencies[i].description).toBe(
        mockDependencies[i].description
      )
    }
  })
})

/**
 * Function parseParametersUrl
 */
describe('parseParametersUrl', () => {
  it('return parameters', () => {
    const json = Object.assign({}, MockClient)
    const listsValues = getListsValues(json)
    let result = parseParametersUrl(
      {
        type: 'gradle-project',
        language: 'kotlin',
        packaging: 'war',
        jvmVersion: '11',
        groupId: 'com.example2',
        artifactId: 'demo2',
        name: 'demo2',
        description: 'Demo project for Spring Boot2',
        packageName: 'com.example2.demo2',
      },
      listsValues
    )
    expect(get(result, 'values.project')).toBe('gradle-project')
    expect(get(result, 'values.language')).toBe('kotlin')
    expect(get(result, 'values.meta.packaging')).toBe('war')
    expect(get(result, 'values.meta.java')).toBe('11')
    expect(get(result, 'values.meta.group')).toBe('com.example2')
    expect(get(result, 'values.meta.artifact')).toBe('demo2')
    expect(get(result, 'values.meta.name')).toBe('demo2')
    expect(get(result, 'values.meta.description')).toBe(
      'demo project for spring boot2'
    )
    expect(get(result, 'values.meta.packageName')).toBe('com.example2.demo2')
  })
})

/**
 * Function parseParams
 */
describe('parseParams', () => {
  it('return parameters', () => {
    const json = Object.assign({}, MockClient)
    const defaultValues = getDefaultValues(json)
    const listsValues = getListsValues(json)

    let result = parseParams(
      defaultValues,
      {
        type: 'gradle-project',
        language: 'kotlin',
        packaging: 'war',
        jvmVersion: '11',
        groupId: 'com.example2',
        artifactId: 'demo2',
        name: 'demo2',
        description: 'Demo project for Spring Boot2',
        packageName: 'com.example2.demo2',
      },
      listsValues
    )
    expect(Object.keys(result.warnings).length).toBe(0)

    expect(get(result, 'values.project')).toBe('gradle-project')
    expect(get(result, 'values.language')).toBe('kotlin')
    expect(get(result, 'values.meta.packaging')).toBe('war')
    expect(get(result, 'values.meta.java')).toBe('11')
    expect(get(result, 'values.meta.group')).toBe('com.example2')
    expect(get(result, 'values.meta.artifact')).toBe('demo2')
    expect(get(result, 'values.meta.name')).toBe('demo2')
    expect(get(result, 'values.meta.description')).toBe(
      'demo project for spring boot2'
    )
    expect(get(result, 'values.meta.packageName')).toBe('com.example2.demo2')
    expect(Object.keys(result.dependencies).length).toBe(0)
  })
})
