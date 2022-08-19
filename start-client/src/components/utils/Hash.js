import queryString from 'query-string'
import { toast } from 'react-toastify'
import { useContext, useEffect, useState } from 'react'

import { AppContext } from '../reducer/App'
import { InitializrContext } from '../reducer/Initializr'
import { isValidParams } from './ApiUtils'

const getHash = () => {
  return window.location.hash
}

const clearHash = () => {
  if (window.location.hash) {
    if (window.history.pushState) {
      window.history.pushState(null, null, window.location.pathname)
    } else {
      window.history.hash = ``
    }
  }
}

export default function useHash() {
  const [hash, setHash] = useState(getHash())

  const { dispatch } = useContext(InitializrContext)
  const { config, complete, dispatch: dispatchApp } = useContext(AppContext)

  useEffect(() => {
    const handler = () => {
      setHash(getHash())
    }
    window.addEventListener('hashchange', handler)
    return () => {
      window.removeEventListener('hashchange', handler)
    }
  }, [])

  useEffect(() => {
    if (complete && hash) {
      const params = queryString.parse(`?${hash.substr(2)}`)
      dispatch({ type: 'LOAD', payload: { params, lists: config.lists } })
      if (params?.platformVersion) {
        dispatchApp({
          type: 'UPDATE_DEPENDENCIES',
          payload: { boot: params.platformVersion },
        })
      }
      clearHash()
      setHash('')
      if (isValidParams(params)) {
        toast.success(`Configuration loaded.`)
      }
    }
  }, [complete, hash, dispatch, config])

  return null
}
