import PropTypes from 'prop-types'
import get from 'lodash/get'
import set from 'lodash/set'
import React, { useReducer } from 'react'
import useTheme from '../utils/Theme'
import { isValidDependency } from '../utils/ApiUtils'
import { rangeToText } from '../utils/Version'

const MAX_HISTORY = 100

export const defaultAppContext = {
  complete: false,
  explore: false,
  share: false,
  history: false,
  favorite: false,
  favoriteAdd: false,
  nav: false,
  list: false,
  theme: 'light',
  config: {},
  groupsClosed: [],
  dependencies: {
    list: [],
    groups: [],
  },
  favoriteOptions: {
    title: '',
    button: '',
    favorite: null,
    back: '',
  },
  histories: [],
  favorites: [],
}

const localStorage =
  typeof window !== 'undefined'
    ? window.localStorage
    : {
        getItem: () => {},
        setItem: () => {},
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
        if (key === 'favoriteAdd' && !value) {
          newState.favoriteOptions = {
            title: '',
            button: '',
            favorite: null,
            back: '',
          }
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
      const histories = localStorage.getItem('histories')
        ? JSON.parse(localStorage.getItem('histories'))
        : []

      const favorites = localStorage.getItem('favorites')
        ? JSON.parse(localStorage.getItem('favorites'))
        : []
      return {
        ...state,
        complete: true,
        config: json,
        dependencies,
        histories,
        favorites,
      }
    }
    case 'ADD_HISTORY': {
      const newHistory = get(action, 'payload')
      const histories = [
        {
          date: new Date().toISOString(),
          value: newHistory,
        },
        ...state.histories.slice(0, MAX_HISTORY - 1),
      ]
      localStorage.setItem('histories', JSON.stringify(histories))
      return { ...state, histories }
    }
    case 'CLEAR_HISTORY': {
      localStorage.setItem('histories', JSON.stringify([]))
      return { ...state, histories: [] }
    }
    case 'ADD_FAVORITE': {
      const favorites = [
        {
          date: new Date().toISOString(),
          name: get(action, 'payload.name'),
          value: get(action, 'payload.values'),
        },
        ...state.favorites,
      ]
      localStorage.setItem('favorites', JSON.stringify(favorites))
      return { ...state, favorites }
    }
    case 'UPDATE_FAVORITE': {
      const favoriteToUpdate = get(action, 'payload.favorite')
      const favorites = state.favorites.map(item => {
        if (
          item.name === favoriteToUpdate.name &&
          item.date === favoriteToUpdate.date &&
          item.value === favoriteToUpdate.value
        ) {
          return {
            ...item,
            name: get(action, 'payload.name'),
          }
        }
        return item
      })
      localStorage.setItem('favorites', JSON.stringify(favorites))
      return { ...state, favorites }
    }
    case 'REMOVE_FAVORITE': {
      const favoriteToRemove = get(action, 'payload')
      const favorites = state.favorites.filter(
        item =>
          !(
            item.name === favoriteToRemove.name &&
            item.date === favoriteToRemove.date &&
            item.value === favoriteToRemove.value
          )
      )
      localStorage.setItem('favorites', JSON.stringify(favorites))
      return { ...state, favorites }
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
