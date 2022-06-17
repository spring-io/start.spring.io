import * as JsSearch from 'js-search'
import PropTypes from 'prop-types'
import get from 'lodash.get'
import React, { useContext, useEffect, useRef, useState } from 'react'
import { CSSTransition, TransitionGroup } from 'react-transition-group'
import { clearAllBodyScrollLocks, disableBodyScroll } from 'body-scroll-lock'

import Item from './Item'
import useWindowsUtils from '../../utils/WindowsUtils'
import { AppContext } from '../../reducer/App'
import { IconTimes } from '../icons'
import { InitializrContext } from '../../reducer/Initializr'
import { Overlay } from '../form'

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

function Dialog({ onClose }) {
  const { list, dependencies: depsContext } = useContext(AppContext)

  const windowsUtils = useWindowsUtils()
  const wrapper = useRef(null)
  const input = useRef(null)
  const dialog = useRef(null)

  const { values, dispatch } = useContext(InitializrContext)
  const [query, setQuery] = useState('')
  const [selected, setSelected] = useState(0)
  const [result, setResult] = useState([])
  const [search, setSearch] = useState(null)
  const [multiple, setMultiple] = useState(false)
  const [groups, setGroups] = useState([])

  const add = id => {
    dispatch({
      type: 'ADD_DEPENDENCY',
      payload: { id },
    })
  }

  useEffect(() => {
    const jsSearchUp = new JsSearch.Search('name')
    jsSearchUp.addIndex('name')
    jsSearchUp.addIndex('id')
    jsSearchUp.addIndex('description')
    jsSearchUp.addIndex('group')
    jsSearchUp.addDocuments(get(depsContext, 'list'))
    setSearch(jsSearchUp)

    setGroups(
      get(depsContext, 'groups', [])
        .map(group => {
          return {
            group: group.group,
            items: group.items.filter(
              itemGroup =>
                !get(values, 'dependencies', []).find(o => o === itemGroup.id)
            ),
          }
        })
        .filter(group => group.items.length > 0)
    )
  }, [values, depsContext, values.dependencies])

  useEffect(() => {
    if (get(wrapper, 'current') && list) {
      disableBodyScroll(get(wrapper, 'current'))
    }
    return () => {
      clearAllBodyScrollLocks()
    }
  }, [wrapper, list])

  useEffect(() => {
    const onSearch = () => {
      if (!search) {
        return
      }
      let vals = get(depsContext, 'list', [])
      if (query.trim()) {
        vals = sortResult(search.search(query))
      }
      vals = vals.filter(
        item => !get(values, 'dependencies', []).find(o => o === item.id)
      )
      setResult(vals)
    }
    onSearch()
  }, [values, query, search, setResult, depsContext])

  useEffect(() => {
    const clickOutside = event => {
      const children = get(wrapper, 'current')
      if (
        children &&
        !children.contains(event.target) &&
        event.target.id !== 'input-quicksearch'
      ) {
        onClose()
      }
    }
    document.addEventListener('mousedown', clickOutside)
    return () => {
      document.removeEventListener('mousedown', clickOutside)
    }
  }, [onClose, input])

  const textFocus = () => {
    if (get(input, 'current')) {
      get(input, 'current').focus()
      get(input, 'current').select()
    }
  }

  const onEnter = () => {
    setSelected(0)
    setQuery('')
    textFocus()
  }

  const onKeyUp = event => {
    if (event.keyCode === 91 || event.keyCode === 93 || event.keyCode === 17) {
      setMultiple(false)
    }
  }

  const updateScroll = () => {
    const wrapperElement = get(wrapper, 'current')
    const dialogElement = get(dialog, 'current')
    const selectedElement =
      wrapperElement.querySelector('a.selected').parentElement
    const position = selectedElement.offsetTop - wrapperElement.scrollTop
    if (position - 50 < 0 || position > dialogElement.clientHeight - 160) {
      const top = query.trim() === '' ? 50 : 10
      wrapperElement.scrollTop = selectedElement.offsetTop - top
    }
  }

  const onKeyDown = event => {
    if (event.keyCode === 75 && (event.ctrlKey || event.metaKey)) {
      onClose()
      return
    }
    switch (event.keyCode) {
      case 40: // Down
        event.preventDefault()
        setSelected(
          Math.min(selected + 1, result.length - 1, result.length - 1)
        )
        setTimeout(() => {
          updateScroll()
        })
        break
      case 38: // Up
        event.preventDefault()
        setSelected(Math.max(selected - 1, 0))
        setTimeout(() => {
          updateScroll()
        })
        break
      case 13: // Enter
        event.preventDefault()
        if (selected < result.length && result[selected].valid) {
          add(result[selected].id)
        }
        if (!multiple) {
          onClose()
        } else {
          textFocus()
        }
        break
      case 27: // Escape
        event.preventDefault()
        onClose()
        break
      case 39: // Right
      case 37: // Left
        break
      case 9: // Tab
        event.preventDefault()
        break
      case 91: // Command
      case 93: // Command
      case 224: // Command
      case 17: // Ctrl
        setMultiple(true)
        break
      default:
        // Default
        setSelected(0)
    }
  }

  const onEnded = () => {
    setMultiple(false)
    setTimeout(() => {}, 300)
  }

  let currentIndex = -1
  return (
    <>
      <TransitionGroup component={null}>
        {list && (
          <CSSTransition
            onEnter={onEnter}
            onExit={onEnded}
            classNames='dialog-dependencies'
            timeout={300}
          >
            <div className='dialog-dependencies' ref={dialog}>
              <div className='control-input'>
                <input
                  className='input'
                  placeholder='Web, Security, JPA, Actuator, Devtools...'
                  ref={input}
                  value={query}
                  onKeyUp={onKeyUp}
                  onChange={event => {
                    setQuery(event.target.value)
                    setSelected(0)
                  }}
                  autoComplete='off'
                  id='input-quicksearch'
                  onKeyDown={onKeyDown}
                />
                <div className='is-mobile'>
                  <a
                    href='/#'
                    onClick={e => {
                      e.preventDefault()
                      onClose()
                    }}
                    className='close'
                  >
                    <IconTimes />
                  </a>
                </div>
                <span className='help'>
                  Press {windowsUtils.symb} for multiple adds{' '}
                </span>
              </div>
              <ul ref={wrapper}>
                {query.trim()
                  ? result.map((item, index) => (
                      <Item
                        selected={selected === index}
                        item={item}
                        index={index}
                        key={item.id}
                        onAdd={() => {
                          textFocus()
                          if (item.valid && !multiple) {
                            onClose()
                          }
                        }}
                        onSelect={() => {
                          setSelected(index)
                        }}
                      />
                    ))
                  : groups.map(group => (
                      <>
                        <li key={group.group} className='group-title'>
                          <span>{group.group}</span>
                        </li>
                        {group.items.map(itemGroup => {
                          currentIndex = +`${currentIndex + 1}`
                          return (
                            <Item
                              selected={selected === currentIndex}
                              item={itemGroup}
                              key={itemGroup.id}
                              index={currentIndex}
                              group={false}
                              onAdd={() => {
                                textFocus()
                                if (itemGroup.valid && !multiple) {
                                  onClose()
                                }
                              }}
                              onSelect={i => {
                                setSelected(i)
                              }}
                            />
                          )
                        })}
                      </>
                    ))}
              </ul>
            </div>
          </CSSTransition>
        )}
      </TransitionGroup>

      <Overlay open={list || false} />
    </>
  )
}

Dialog.propTypes = {
  onClose: PropTypes.func.isRequired,
}

export default Dialog
