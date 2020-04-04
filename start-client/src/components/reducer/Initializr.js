import PropTypes from 'prop-types'
import get from 'lodash.get'
import set from 'lodash.set'
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

export function reducer(state, action) {
  switch (action.type) {
    case 'COMPLETE': {
      const json = get(action, 'payload')
      const defaultValues = {
        ...get(json, 'defaultValues'),
        meta: get(json, 'defaultValues.meta'),
      }
      return {
        values: defaultValues,
        share: getShareUrl(defaultValues),
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
