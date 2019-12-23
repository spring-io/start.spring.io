import get from 'lodash.get'

import { reducer } from '../App'

let state = {}
beforeEach(() => {
  state = {
    more: false,
    complete: false,
    explore: false,
    share: false,
    tab: 'quicksearch',
    theme: 'light',
    config: {},
    groupsClosed: [],
    dependencies: {
      list: [],
      groups: [],
    },
  }
})

describe('UPDATE action', () => {
  it('should reduce the state', () => {
    let result = reducer(state, { type: 'UPDATE', payload: { more: true } })
    expect(get(result, 'more')).toBe(true)
    result = reducer(state, { type: 'UPDATE', payload: { theme: 'dark' } })
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

describe('SHARE_UPDATE/EXPLORE_UPDATE action', () => {
  it('should add/remove a group', () => {
    let result = reducer(state, {
      type: 'SHARE_UPDATE',
      payload: { open: true },
    })
    expect(get(result, 'share')).toBe(true)
    result = reducer(result, {
      type: 'SHARE_UPDATE',
      payload: { open: false },
    })
    expect(get(result, 'share')).toBe(false)
    result = reducer(result, {
      type: 'EXPLORE_UPDATE',
      payload: { open: true },
    })
    expect(get(result, 'explore')).toBe(true)
    result = reducer(result, {
      type: 'EXPLORE_UPDATE',
      payload: { open: false },
    })
    expect(get(result, 'explore')).toBe(false)
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
