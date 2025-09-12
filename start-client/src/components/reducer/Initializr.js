import PropTypes from 'prop-types'
import get from 'lodash/get'
import set from 'lodash/set'
import React, { useReducer } from 'react'

import { getShareUrl, parseParams } from '../utils/ApiUtils'

export const defaultInitializrContext = {
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

const localStorage =
  typeof window !== 'undefined'
    ? window.localStorage
    : {
        getItem: () => {},
        setItem: () => {},
      }

const getPersistedOrDefault = json => {
  const values = {
    project:
      localStorage.getItem('project') || get(json, 'defaultValues').project,
    language:
      localStorage.getItem('language') || get(json, 'defaultValues').language,
    boot: get(json, 'defaultValues').boot,
    meta: {
      name: get(json, 'defaultValues.meta').name,
      group: get(json, 'defaultValues.meta').group,
      artifact: get(json, 'defaultValues.meta').artifact,
      description: get(json, 'defaultValues.meta').description,
      packageName: get(json, 'defaultValues.meta').packageName,
      packaging:
        localStorage.getItem('packaging') ||
        get(json, 'defaultValues.meta').packaging,
      java:
        localStorage.getItem('java') || get(json, 'defaultValues.meta').java,
    },
    dependencies: [],
  }
  const checks = ['project', 'language', 'meta.java', 'meta.packaging']
  checks.forEach(key => {
    const item = get(json, `lists.${key}`)?.find(
      it => it.key === get(values, key)
    )
    if (!item) {
      set(values, key, get(json, `defaultValues.${key}`))
    }
  })
  return values
}

const persist = changes => {
  if (get(changes, 'project')) {
    localStorage.setItem('project', get(changes, 'project'))
  }
  if (get(changes, 'language')) {
    localStorage.setItem('language', get(changes, 'language'))
  }
  if (get(changes, 'meta.packaging')) {
    localStorage.setItem('packaging', get(changes, 'meta.packaging'))
  }
  if (get(changes, 'meta.java')) {
    localStorage.setItem('java', get(changes, 'meta.java'))
  }
}

export function reducer(state, action) {
  switch (action.type) {
    case 'COMPLETE': {
      const json = get(action, 'payload')
      const values = getPersistedOrDefault(json)
      return {
        values,
        share: getShareUrl(values),
        errors: {},
        warnings: {},
      }
    }
    case 'UPDATE': {
      const changes = get(action, 'payload')
      let errors = { ...state.errors }
      let meta = { ...get(state, 'values.meta') }
      if (get(changes, 'meta')) {
        meta = { ...meta, ...get(changes, 'meta') }
      }
      if (get(changes, 'boot')) {
        const { boot, ...err } = errors
        errors = err
      }
      if (get(changes, 'meta.group') !== undefined) {
        set(
          meta,
          'packageName',
          `${get(meta, 'group')}.${get(meta, 'artifact')}`
        )
      }
      if (get(changes, 'meta.artifact') !== undefined) {
        set(
          meta,
          'packageName',
          `${get(meta, 'group')}.${get(meta, 'artifact')}`
        )
        set(meta, 'name', `${get(meta, 'artifact')}`)
      }
      persist(changes)
      const values = {
        ...get(state, 'values'),
        ...changes,
        meta,
      }
      return { ...state, values, share: getShareUrl(values), errors }
    }
    case 'LOAD': {
      const params = get(action, 'payload.params')
      const lists = get(action, 'payload.lists')
      const { values, errors, warnings } = parseParams(
        state.values,
        params,
        lists
      )
      return { ...state, values, errors, warnings, share: getShareUrl(values) }
    }
    case 'ADD_DEPENDENCY': {
      const dependency = get(action, 'payload.id')
      const values = { ...get(state, 'values') }
      values.dependencies = [...get(values, 'dependencies'), dependency]
      return { ...state, values, share: getShareUrl(values) }
    }
    case 'REMOVE_DEPENDENCY': {
      const dependency = get(action, 'payload.id')
      const values = { ...get(state, 'values') }
      values.dependencies = [
        ...get(values, 'dependencies').filter(dep => dep !== dependency),
      ]
      return { ...state, values, share: getShareUrl(values) }
    }
    case 'CLEAR_WARNINGS': {
      return { ...state, warnings: {} }
    }
    default:
      return state
  }
}

export const InitializrContext = React.createContext({
  ...defaultInitializrContext,
})

export function InitializrProvider({ children }) {
  const [state, dispatch] = useReducer(reducer, { ...defaultInitializrContext })
  return (
    <InitializrContext.Provider value={{ ...state, dispatch }}>
      {children}
    </InitializrContext.Provider>
  )
}

InitializrProvider.defaultProps = {
  children: null,
}

InitializrProvider.propTypes = {
  children: PropTypes.node,
}
