import get from 'lodash.get'

import MockClient from '../../../../dev/api.mock.json'
import { getDefaultValues, getLists } from '../../utils/ApiUtils'
import { reducer } from '../Initializr'

const defaultValues = getDefaultValues({ ...MockClient })

let state = {}
beforeEach(() => {
  state = {
    values: { ...defaultValues, meta: { ...defaultValues.meta } },
    share: '',
    errors: {},
    warnings: {},
  }
})

describe('COMPLETE action', () => {
  beforeEach(() => {
    state = {
      values: {
        project: '',
        language: '',
        boot: '',
        meta: {
          name: '',
          group: '',
          artifact: '',
          description: '',
          packaging: '',
          packageName: '',
          java: '',
        },
        dependencies: [],
      },
      share: '',
      errors: {},
      warnings: {},
    }
  })
  it('should reduce the state', () => {
    const result = reducer(state, {
      type: 'COMPLETE',
      payload: {
        defaultValues,
      },
    })
    expect(get(result, 'share')).toBe(
      'type=maven-project&language=java&platformVersion=2.2.0.RELEASE&packaging=jar&jvmVersion=1.8&groupId=com.example&artifactId=demo&name=demo&description=Demo%20project%20for%20Spring%20Boot&packageName=com.example.demo'
    )
    expect(get(result, 'values.project')).toBe('maven-project')
    expect(get(result, 'values.language')).toBe('java')
    expect(get(result, 'values.boot')).toBe('2.2.0.RELEASE')
    expect(get(result, 'values.meta.name')).toBe('demo')
    expect(get(result, 'values.meta.group')).toBe('com.example')
    expect(get(result, 'values.meta.artifact')).toBe('demo')
    expect(get(result, 'values.meta.description')).toBe(
      'Demo project for Spring Boot'
    )
    expect(get(result, 'values.meta.packaging')).toBe('jar')
    expect(get(result, 'values.meta.packageName')).toBe('com.example.demo')
    expect(get(result, 'values.meta.java')).toBe('1.8')
    expect(get(result, 'values.dependencies').length).toBe(0)
    expect(Object.keys(get(result, 'errors')).length).toBe(0)
    expect(Object.keys(get(result, 'warnings')).length).toBe(0)
  })
})

describe('UPDATE action', () => {
  it('should reduce the state (type)', () => {
    const result = reducer(state, {
      type: 'UPDATE',
      payload: {
        project: 'maven-project',
      },
    })
    expect(get(result, 'values.project')).toBe('maven-project')
  })
  it('should reduce the state (language)', () => {
    const result = reducer(state, {
      type: 'UPDATE',
      payload: {
        language: 'kotlin',
      },
    })
    expect(get(result, 'values.language')).toBe('kotlin')
  })
  it('should reduce the state (boot)', () => {
    const result = reducer(state, {
      type: 'UPDATE',
      payload: {
        boot: '2.1.10.BUILD-SNAPSHOT',
      },
    })
    expect(get(result, 'values.boot')).toBe('2.1.10.BUILD-SNAPSHOT')
  })
  it('should reduce the state (meta name)', () => {
    const result = reducer(state, {
      type: 'UPDATE',
      payload: {
        meta: {
          name: 'demo1',
        },
      },
    })
    expect(get(result, 'values.meta.name')).toBe('demo1')
  })
  it('should reduce the state (meta group)', () => {
    state.values.meta.artifact = 'demo3'
    const result = reducer(state, {
      type: 'UPDATE',
      payload: {
        meta: {
          group: 'com.example1',
        },
      },
    })
    expect(get(result, 'values.meta.group')).toBe('com.example1')
    expect(get(result, 'values.meta.packageName')).toBe('com.example1.demo3')
  })
  it('should reduce the state (meta artifact)', () => {
    state.values.meta.group = 'com.example3'
    const result = reducer(state, {
      type: 'UPDATE',
      payload: {
        meta: {
          artifact: 'demo2',
        },
      },
    })
    expect(get(result, 'values.meta.artifact')).toBe('demo2')
    expect(get(result, 'values.meta.packageName')).toBe('com.example3.demo2')
  })

  it('should reduce the state (meta artifact, empty value)', () => {
    state.values.meta.group = 'com.example3'
    let result = reducer(state, {
      type: 'UPDATE',
      payload: {
        meta: {
          artifact: '',
        },
      },
    })
    expect(get(result, 'values.meta.artifact')).toBe('')
    expect(get(result, 'values.meta.name')).toBe('')
    expect(get(result, 'values.meta.packageName')).toBe('com.example3.')
    result = reducer(result, {
      type: 'UPDATE',
      payload: {
        meta: {
          name: 'demo',
        },
      },
    })
    expect(get(result, 'values.meta.artifact')).toBe('')
    expect(get(result, 'values.meta.name')).toBe('demo')
    expect(get(result, 'values.meta.packageName')).toBe('com.example3.')
  })

  it('should reduce the state (meta description)', () => {
    const result = reducer(state, {
      type: 'UPDATE',
      payload: {
        meta: {
          description: 'desc',
        },
      },
    })
    expect(get(result, 'values.meta.description')).toBe('desc')
  })
  it('should reduce the state (meta packaging)', () => {
    const result = reducer(state, {
      type: 'UPDATE',
      payload: {
        meta: {
          packaging: 'war',
        },
      },
    })
    expect(get(result, 'values.meta.packaging')).toBe('war')
  })
  it('should reduce the state (meta packageName)', () => {
    const result = reducer(state, {
      type: 'UPDATE',
      payload: {
        meta: {
          packageName: 'com1.example2.demo3',
        },
      },
    })
    expect(get(result, 'values.meta.packageName')).toBe('com1.example2.demo3')
  })
  it('should reduce the state (meta java)', () => {
    const result = reducer(state, {
      type: 'UPDATE',
      payload: {
        meta: {
          java: '1.9',
        },
      },
    })
    expect(get(result, 'values.meta.java')).toBe('1.9')
  })
})

describe('LOAD action', () => {
  it('should reduce the state', () => {
    const json = { ...MockClient }
    const listsValues = getLists(json)
    const result = reducer(state, {
      type: 'LOAD',
      payload: {
        params: {
          artifactId: 'demo1',
          description: 'Demo1 project for Spring Boot',
          groupId: 'com.example1',
          jvmVersion: '1.8',
          language: 'java',
          name: 'demo1',
          packageName: 'com.example1.demo1',
          packaging: 'war',
          platformVersion: '2.2.0.RELEASE',
          type: 'gradle-project',
        },
        lists: listsValues,
      },
    })
    expect(get(result, 'values.project')).toBe('gradle-project')
    expect(get(result, 'values.language')).toBe('java')
    expect(get(result, 'values.boot')).toBe('2.2.0.RELEASE')
    expect(get(result, 'values.meta.name')).toBe('demo1')
    expect(get(result, 'values.meta.group')).toBe('com.example1')
    expect(get(result, 'values.meta.artifact')).toBe('demo1')
    expect(get(result, 'values.meta.description')).toBe(
      'Demo1 project for Spring Boot'
    )
    expect(get(result, 'values.meta.packaging')).toBe('war')
    expect(get(result, 'values.meta.packageName')).toBe('com.example1.demo1')
    expect(get(result, 'values.meta.java')).toBe('1.8')
    expect(get(result, 'values.dependencies').length).toBe(0)
    expect(Object.keys(get(result, 'errors')).length).toBe(0)
    expect(Object.keys(get(result, 'warnings')).length).toBe(0)
  })
})

describe('ADD_DEPENDENCY action', () => {
  it('should reduce the state', () => {
    let result = reducer(state, {
      type: 'ADD_DEPENDENCY',
      payload: {
        id: 'foo1',
      },
    })
    expect(get(result, 'values.dependencies').length).toBe(1)
    expect(get(result, 'values.dependencies[0]')).toBe('foo1')
    result = reducer(result, {
      type: 'ADD_DEPENDENCY',
      payload: {
        id: 'foo2',
      },
    })
    expect(get(result, 'values.dependencies').length).toBe(2)
    expect(get(result, 'values.dependencies[0]')).toBe('foo1')
    expect(get(result, 'values.dependencies[1]')).toBe('foo2')
  })
})

describe('REMOVE_DEPENDENCY action', () => {
  it('should reduce the state', () => {
    let result = reducer(state, {
      type: 'ADD_DEPENDENCY',
      payload: {
        id: 'foo1',
      },
    })
    result = reducer(result, {
      type: 'ADD_DEPENDENCY',
      payload: {
        id: 'foo2',
      },
    })
    expect(get(result, 'values.dependencies').length).toBe(2)
    expect(get(result, 'values.dependencies[0]')).toBe('foo1')
    expect(get(result, 'values.dependencies[1]')).toBe('foo2')
    result = reducer(result, {
      type: 'REMOVE_DEPENDENCY',
      payload: {
        id: 'foo1',
      },
    })
    expect(get(result, 'values.dependencies').length).toBe(1)
    expect(get(result, 'values.dependencies[0]')).toBe('foo2')
    result = reducer(result, {
      type: 'REMOVE_DEPENDENCY',
      payload: {
        id: 'foo2',
      },
    })
    expect(get(result, 'values.dependencies').length).toBe(0)
  })
})

describe('CLEAR_WARNINGS action', () => {
  it('should reduce the state', () => {
    state.warnings = { foo: 'bar' }
    const result = reducer(state, {
      type: 'CLEAR_WARNINGS',
    })
    expect(Object.keys(get(result, 'warnings')).length).toBe(0)
  })
})
