import Fuse from 'fuse.js'
import PropTypes from 'prop-types'
import get from 'lodash.get'
import React, { useContext, useEffect, useState } from 'react'

import Dependencies from './Dependencies'
import Result from './Result'
import { AppContext } from '../../../reducer/App'
import { InitializrContext } from '../../../reducer/Initializr'

const FuseConfig = {
  shouldSort: true,
  threshold: 0,
  location: 0,
  distance: 100,
  maxPatternLength: 32,
  minMatchCharLength: 1,
  keys: [
    {
      name: 'name',
      weight: 1,
    },
    {
      name: 'id',
      weight: 1,
    },
    {
      name: 'description',
      weight: 0.5,
    },
    {
      name: 'group',
      weight: 0.3,
    },
  ],
}

const sortResult = dependencies => {
  return dependencies.sort((a, b) => {
    if (a.valid && !b.valid) {
      return -1
    }
    if (!a.valid && b.valid) {
      return 1
    }
    return b.weight - a.weight
  })
}

const QuickSearch = ({ submit, input }) => {
  const { values, dispatch } = useContext(InitializrContext)
  const { dependencies: dependenciesContext } = useContext(AppContext)

  const [query, setQuery] = useState('')
  const [selected, setSelected] = useState(0)
  const [dependencies, setDependencies] = useState([])

  // const [dep, setDeps] = useState(false)

  const [result, setResult] = useState([])
  const [count, setCount] = useState(0)
  const [search, setSearch] = useState(
    new Fuse(get(dependenciesContext, 'list'), FuseConfig)
  )

  const add = id => {
    dispatch({
      type: 'ADD_DEPENDENCY',
      payload: { id },
    })
  }

  useEffect(() => {
    const newDeps = get(values, 'dependencies', []).map(item => {
      return get(dependenciesContext, 'list', []).find(d => d.id === item)
    })
    setDependencies(newDeps)
    setSearch(new Fuse(get(dependenciesContext, 'list'), FuseConfig))
  }, [values, dependenciesContext, values.dependencies])

  const onFocus = () => {
    setSelected(0)
  }

  const onBlur = () => {
    setSelected(-1)
  }

  const onKeyDown = event => {
    switch (event.keyCode) {
      case 40: // Down
        event.preventDefault()
        setSelected(Math.min(selected + 1, result.length - 1, 4))
        break
      case 38: // Up
        event.preventDefault()
        setSelected(Math.max(selected - 1, 0))
        break
      case 13: // Enter
        event.preventDefault()
        if (result.length > 0) {
          add(result[selected].id)
          setQuery('')
        } else {
          submit()
        }
        break
      case 27: // Escape
        event.preventDefault()
        setQuery('')
        break
      case 39: // Right
      case 37: // Left
        break
      default:
        // Default
        setSelected(0)
    }
  }

  useEffect(() => {
    const onSearch = () => {
      let vals = search
        .search(query)
        .filter(
          item => !get(values, 'dependencies', []).find(o => o === item.id)
        )
      setCount(vals.length)
      if (vals.length > 5) {
        vals = vals.slice(0, 5)
      }
      vals = sortResult(vals)
      setResult(vals)
    }
    onSearch()
  }, [values, query, search, setResult])

  return (
    <div className='colset-2'>
      <div className='column'>
        <label className='search-label' htmlFor='input-quicksearch'>
          {/* eslint-disable-line */}
          Search dependencies to add
        </label>
        <input
          type='text'
          className='control-input'
          placeholder='Web, Security, JPA, Actuator, Devtools...'
          value={query}
          onBlur={onBlur}
          onFocus={onFocus}
          onChange={event => {
            setQuery(event.target.value)
            setSelected(0)
          }}
          id='input-quicksearch'
          ref={input}
          onKeyDown={onKeyDown}
        />
        <Result
          list={result}
          add={item => {
            add(item.id)
            setQuery('')
            if (input) {
              get(input, 'current').focus()
            }
          }}
          selected={selected}
          select={setSelected}
        />
        {count > 5 && (
          <div className='search-more-warning'>
            <p>
              More than 5 results found.
              <br />
              Refine your search if necessary.
            </p>
          </div>
        )}
      </div>
      <div className='column'>
        {/* eslint-disable-next-line */}
        <label>Selected dependencies</label>
        {get(values, 'dependencies', []).length === 0 ? (
          <div className='search-no-selected'>No dependency selected</div>
        ) : (
          <Dependencies
            list={dependencies}
            remove={item => {
              dispatch({
                type: 'REMOVE_DEPENDENCY',
                payload: { id: item.id },
              })
            }}
          />
        )}
      </div>
    </div>
  )
}

QuickSearch.defaultProps = {}

QuickSearch.propTypes = {
  submit: PropTypes.func.isRequired,
  input: PropTypes.oneOfType([
    PropTypes.func,
    PropTypes.shape({ current: PropTypes.instanceOf(Element) }),
  ]).isRequired,
}

export default QuickSearch
