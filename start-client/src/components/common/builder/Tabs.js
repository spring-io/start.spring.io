import PropTypes from 'prop-types'
import get from 'lodash.get'
import React, { useContext, useEffect, useState } from 'react'

import { AppContext } from '../../reducer/App'
import { IconList, IconSearch } from '../icons'
import { InitializrContext } from '../../reducer/Initializr'

const Tabs = ({ changeTab }) => {
  const { dependencies: deps, dispatch, tab } = useContext(AppContext)
  const { values } = useContext(InitializrContext)
  const [count, setCount] = useState(0)

  useEffect(() => {
    setCount(
      values.dependencies.filter(d => {
        return get(deps, 'list', []).find(a => a.id === d).valid
      }).length
    )
  }, [deps, values.dependencies])

  const change = newTab => {
    dispatch({
      type: 'UPDATE',
      payload: { tab: newTab },
    })
    setTimeout(() => {
      changeTab(newTab)
    })
  }

  return (
    <div className='tab'>
      <div className='tab-container'>
        <a
          href='/'
          aria-label='Search'
          onClick={event => {
            event.preventDefault()
            change('quicksearch')
          }}
          className={`quick-search ${tab === 'quicksearch' ? 'active' : ''}`}
        >
          <IconSearch />
        </a>
        <a
          href='/'
          aria-label='List'
          onClick={event => {
            event.preventDefault()
            change('list')
          }}
          className={`list ${tab === 'list' ? 'active' : ''}`}
        >
          <IconList />
        </a>
        {count > 0 && (
          <>
            <strong>
              <span>{count}</span> selected
            </strong>
          </>
        )}
      </div>
    </div>
  )
}

Tabs.propTypes = {
  changeTab: PropTypes.func.isRequired,
}

export default Tabs
