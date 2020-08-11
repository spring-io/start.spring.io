import PropTypes from 'prop-types'
import get from 'lodash.get'
import set from 'lodash.set'
import React, { useReducer } from 'react'

import useTheme from '../utils/Theme'
import { isValidDependency } from '../utils/ApiUtils'
import { rangeToText } from '../utils/Version'

export const defaultAppContext = {
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

export function reduceDependencies(boot, items) {
  const groups = []
  const list = []
  const getParent = (m, name) => {
    return m.find(item => item.group === name)
  }
  for (let i = 0; i < items.length; i += 1) {
    let message = ''
    const dep = items[i]
    let parent = getParent(groups, dep.group)
    if (!parent) {
      parent = {
        group: dep.group,
        items: [],
      }
      groups.push(parent)
    }
    const valid = isValidDependency(boot, dep)
    if (!valid) {
      message = `Requires Spring Boot ${rangeToText(
        get(dep, 'versionRequirement')
      )}.`
    }
    parent.items.push({ ...dep, valid, message })
    list.push({ ...dep, valid, message })
  }
  return {
    list,
    groups,
  }
}

export function reducer(state, action) {
  switch (action.type) {
    case 'UPDATE': {
      const newState = { ...state }
      const keysContext = Object.keys(defaultAppContext)
      const keys = Object.keys(get(action, 'payload', {}))
      keys.map(key => {
        if (keysContext.indexOf(key) === -1) {
          throw Error('Error AppProvider, invalid paylaod field action')
        }
        const value = get(action, `payload.${key}`)
        set(newState, key, value)
        if (key === 'theme') {
          localStorage.setItem('springtheme', value)
        }
        return key
      })
      return newState
    }
    case 'TOGGLE_GROUP': {
      const id = get(action, 'payload.id')
      let groupsClosed = [...state.groupsClosed]
      if (groupsClosed.indexOf(id) > -1) {
        groupsClosed = [...groupsClosed.filter(g => g !== id)]
      } else {
        groupsClosed = [...groupsClosed, id]
      }
      return { ...state, groupsClosed }
    }
    case 'UPDATE_DEPENDENCIES': {
      const dependencies = reduceDependencies(
        get(action, 'payload.boot'),
        get(state, 'config.lists.dependencies')
      )
      return { ...state, dependencies }
    }
    case 'COMPLETE': {
      const json = get(action, 'payload', {})
      const dependencies = reduceDependencies(
        get(json, 'defaultValues.boot'),
        get(json, 'lists.dependencies')
      )
      return { ...state, complete: true, config: json, dependencies }
    }
    default:
      return state
  }
}

export const AppContext = React.createContext({ ...defaultAppContext })

export function AppProvider({ children }) {
  const theme = useTheme()
  const [state, dispatch] = useReducer(reducer, { ...defaultAppContext, theme })
  return (
    <AppContext.Provider value={{ ...state, dispatch }}>
      {children}
    </AppContext.Provider>
  )
}

AppProvider.propTypes = {
  children: PropTypes.node.isRequired,
}
