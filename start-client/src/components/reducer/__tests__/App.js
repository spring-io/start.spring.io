import get from 'lodash.get'

import { reducer } from '../App'

let state = {}
beforeEach(() => {
  state = {
    complete: false,
    explore: false,
    share: false,
    nav: false,
    list: false,
    theme: 'light',
    config: {},
    groupsClosed: [],
    dependencies: {
      list: [],
      groups: [],
    },
  }
  const localStorageMock = {
    getItem: jest.fn(),
    setItem: jest.fn(),
    clear: jest.fn(),
  }
  global.localStorage = localStorageMock
})

describe('UPDATE action', () => {
  it('should reduce the state', () => {
    const result = reducer(state, {
      type: 'UPDATE',
      payload: { theme: 'dark' },
    })
    expect(get(result, 'theme')).toBe('dark')
  })
  it('should throw an error', () => {
    expect(() => {
      reducer(state, { type: 'UPDATE', payload: { foo: 'bar' } })
    }).toThrow(Error)
  })
})

describe('TOGGLE_GROUP action', () => {
  it('should add/remove a group', () => {
    let result = reducer(state, {
      type: 'TOGGLE_GROUP',
      payload: { id: 'foo1' },
    })
    expect(get(result, 'groupsClosed').length).toBe(1)
    expect(get(result, 'groupsClosed[0]')).toBe('foo1')

    result = reducer(result, {
      type: 'TOGGLE_GROUP',
      payload: { id: 'foo2' },
    })
    expect(get(result, 'groupsClosed').length).toBe(2)
    expect(get(result, 'groupsClosed[0]')).toBe('foo1')
    expect(get(result, 'groupsClosed[1]')).toBe('foo2')

    result = reducer(result, {
      type: 'TOGGLE_GROUP',
      payload: { id: 'foo1' },
    })
    expect(get(result, 'groupsClosed').length).toBe(1)
    expect(get(result, 'groupsClosed[0]')).toBe('foo2')
  })
})

describe('UPDATE_DEPENDENCIES action', () => {
  it('should update dependencies', () => {
    state.config = {
      lists: {
        dependencies: [
          {
            id: 'foo1',
            group: 'bar2',
            versionRange: '',
          },
        ],
      },
    }
    const result = reducer(state, {
      type: 'UPDATE_DEPENDENCIES',
      payload: {
        boot: '1.0.0.RELEASE',
      },
    })
    expect(get(result, 'dependencies.list').length).toBe(1)
    expect(get(result, 'dependencies.list[0].id')).toBe('foo1')
    expect(get(result, 'dependencies.list[0].valid')).toBe(true)
  })
})

describe('share/explore/nav/list action', () => {
  it('should add/remove a group', () => {
    let result = reducer(state, {
      type: 'UPDATE',
      payload: { share: true },
    })
    expect(get(result, 'share')).toBe(true)
    result = reducer(result, {
      type: 'UPDATE',
      payload: { share: false },
    })
    expect(get(result, 'share')).toBe(false)
    result = reducer(result, {
      type: 'UPDATE',
      payload: { explore: true },
    })
    expect(get(result, 'explore')).toBe(true)
    result = reducer(result, {
      type: 'UPDATE',
      payload: { explore: false },
    })
    expect(get(result, 'explore')).toBe(false)
    result = reducer(result, {
      type: 'UPDATE',
      payload: { explore: true },
    })
    expect(get(result, 'explore')).toBe(true)
    result = reducer(result, {
      type: 'UPDATE',
      payload: { explore: false },
    })
    expect(get(result, 'explore')).toBe(false)
    result = reducer(result, {
      type: 'UPDATE',
      payload: { nav: true },
    })
    expect(get(result, 'nav')).toBe(true)
    result = reducer(result, {
      type: 'UPDATE',
      payload: { nav: false },
    })
    expect(get(result, 'nav')).toBe(false)
    result = reducer(result, {
      type: 'UPDATE',
      payload: { list: true },
    })
    expect(get(result, 'list')).toBe(true)
    result = reducer(result, {
      type: 'UPDATE',
      payload: { list: false },
    })
    expect(get(result, 'list')).toBe(false)
  })
})

describe('COMPLETE action', () => {
  it('should add/remove a group', () => {
    const result = reducer(state, {
      type: 'COMPLETE',
      payload: {
        defaultValues: {
          boot: '1.0.0',
        },
        lists: {
          dependencies: [
            {
              id: 'foo1',
              group: 'bar2',
              versionRange: '',
            },
          ],
        },
      },
    })
    expect(get(result, 'complete')).toBe(true)
    expect(get(result, 'config.json')).not.toBe(null)
    expect(get(result, 'dependencies')).not.toBe(null)
    expect(get(result, 'dependencies.list').length).toBe(1)
  })
})
