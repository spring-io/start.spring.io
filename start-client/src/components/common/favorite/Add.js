import PropTypes from 'prop-types'
import get from 'lodash/get'
import React, { useEffect, useRef, useContext, useState } from 'react'
import { CSSTransition, TransitionGroup } from 'react-transition-group'
import { clearAllBodyScrollLocks, disableBodyScroll } from 'body-scroll-lock'
import queryString from 'query-string'
import { toast } from 'react-toastify'
import FieldInput from '../builder/FieldInput'
import { Button } from '../form'
import { AppContext } from '../../reducer/App'
import { InitializrContext } from '../../reducer/Initializr'
import {
  getLabelFromList,
  getLabelFromDepsList,
  getBookmarkDefaultName,
} from './Utils'

function FavoriteItem({ value }) {
  const { config } = useContext(AppContext)
  const params = queryString.parse(value)
  const deps = get(params, 'dependencies', '')
    .split(',')
    .filter(dep => !!dep)
  return (
    <div className='favorite-desc'>
      <span className='desc'>
        <span className='main'>
          Project{' '}
          <strong>
            {getLabelFromList(
              get(config, 'lists.project'),
              get(params, 'type')
            )}
          </strong>
          {`, `}
          Language{' '}
          <strong>
            {getLabelFromList(
              get(config, 'lists.language'),
              get(params, 'language')
            )}
          </strong>
          {`, `}
          Spring Boot{' '}
          <strong>
            {getLabelFromList(
              get(config, 'lists.boot'),
              get(params, 'platformVersion')
            )}
          </strong>
        </span>
        <span className='deps'>
          {deps.length === 0 && 'No dependency'}
          {deps.length > 0 && (
            <>
              Dependencies:{' '}
              <strong>
                {deps
                  .map(dep =>
                    getLabelFromDepsList(get(config, 'lists.dependencies'), dep)
                  )
                  .join(', ')}
              </strong>
            </>
          )}
        </span>
      </span>
    </div>
  )
}

FavoriteItem.propTypes = {
  value: PropTypes.string.isRequired,
}

function Add({ open, onClose }) {
  const wrapper = useRef(null)
  const { share } = useContext(InitializrContext)
  const { dispatch, favoriteOptions } = useContext(AppContext)
  const [name, setName] = useState()
  const input = useRef(null)

  const title = get(favoriteOptions, 'title', '') || 'Bookmark'
  const button = get(favoriteOptions, 'button', '') || 'Save'
  const value = get(favoriteOptions, 'favorite.value', '') || share
  const nameFav = get(favoriteOptions, 'favorite.name', '')

  useEffect(() => {
    setName(nameFav || `${getBookmarkDefaultName()}`)
  }, [setName, open, nameFav])

  useEffect(() => {
    const clickOutside = event => {
      const children = get(wrapper, 'current')
      if (children && !children.contains(event.target)) {
        onClose()
      }
    }
    document.addEventListener('mousedown', clickOutside)
    return () => {
      document.removeEventListener('mousedown', clickOutside)
    }
  }, [onClose])

  useEffect(() => {
    if (get(wrapper, 'current') && open) {
      disableBodyScroll(get(wrapper, 'current'))
    }
    if (get(input, 'current')) {
      get(input, 'current').focus()
    }
    return () => {
      clearAllBodyScrollLocks()
    }
  }, [wrapper, open])

  const onSubmit = e => {
    e.preventDefault()
    if (!get(favoriteOptions, 'button', '')) {
      dispatch({ type: 'ADD_FAVORITE', payload: { values: value, name } })
      toast.success('Project bookmarked')
    } else {
      dispatch({
        type: 'UPDATE_FAVORITE',
        payload: { name, favorite: favoriteOptions.favorite },
      })
    }
    onClose()
  }

  return (
    <TransitionGroup component={null}>
      {open && (
        <CSSTransition classNames='popup' timeout={300}>
          <div className='modal-add-favorite'>
            <form className='form' onSubmit={onSubmit} autoComplete='off'>
              <div className='modal-add-favorite-container' ref={wrapper}>
                <div className='modal-header'>
                  <h1>{title}</h1>
                </div>
                <div className='modal-content'>
                  <FavoriteItem value={value} />
                  <FieldInput
                    id='input-name'
                    value={name}
                    text='Name'
                    inputRef={input}
                    onChange={event => {
                      setName(`${event.target.value}`)
                    }}
                  />
                </div>
                <div className='modal-action'>
                  <Button
                    id='add-favorite'
                    variant='primary'
                    onClick={onSubmit}
                  >
                    {button}
                  </Button>
                  <Button
                    hotkey='ESC'
                    onClick={e => {
                      e.preventDefault()
                      onClose()
                    }}
                  >
                    Close
                  </Button>
                </div>
              </div>
            </form>
          </div>
        </CSSTransition>
      )}
    </TransitionGroup>
  )
}

Add.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
}

export default Add
