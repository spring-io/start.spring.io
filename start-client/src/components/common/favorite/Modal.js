import PropTypes from 'prop-types'
import get from 'lodash/get'
import React, { useEffect, useRef, useContext } from 'react'
import { CSSTransition, TransitionGroup } from 'react-transition-group'
import { clearAllBodyScrollLocks, disableBodyScroll } from 'body-scroll-lock'
import queryString from 'query-string'
import { AppContext } from '../../reducer/App'
import { getLabelFromList, getLabelFromDepsList } from './Utils'
import { IconEdit, IconDelete } from '../icons'

function FavoriteItem({ name, value, onClose, onRemove, onUpdate }) {
  const { config } = useContext(AppContext)
  const params = queryString.parse(value)
  const deps = get(params, 'dependencies', '')
    .split(',')
    .filter(dep => !!dep)
  return (
    <li>
      <a
        className='item'
        href={`/#${value}`}
        onClick={() => {
          onClose()
        }}
      >
        <span className='name'>{name}</span>
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
                      getLabelFromDepsList(
                        get(config, 'lists.dependencies'),
                        dep
                      )
                    )
                    .join(', ')}
                </strong>
              </>
            )}
          </span>
        </span>
      </a>
      <button type='button' className='edit' onClick={onUpdate}>
        <IconEdit />
      </button>
      <button type='button' className='remove' onClick={onRemove}>
        <IconDelete />
      </button>
    </li>
  )
}

FavoriteItem.propTypes = {
  name: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  onClose: PropTypes.func.isRequired,
  onRemove: PropTypes.func.isRequired,
  onUpdate: PropTypes.func.isRequired,
}

function Modal({ open, onClose }) {
  const wrapper = useRef(null)
  const { favorites, dispatch } = useContext(AppContext)

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
    return () => {
      clearAllBodyScrollLocks()
    }
  }, [wrapper, open])

  const onRemove = favorite => {
    dispatch({ type: 'REMOVE_FAVORITE', payload: favorite })
  }

  const onUpdate = favorite => {
    dispatch({
      type: 'UPDATE',
      payload: {
        favorite: false,
        favoriteAdd: true,
        favoriteOptions: {
          title: 'Edit bookmark',
          button: 'Update',
          back: 'favorite',
          favorite,
        },
      },
    })
  }

  return (
    <TransitionGroup component={null}>
      {open && (
        <CSSTransition classNames='popup' timeout={300}>
          <div className='modal-favorite'>
            <div className='modal-favorite-container' ref={wrapper}>
              <div className='modal-header'>
                <h1>Bookmarks</h1>
              </div>
              <div className='modal-content'>
                <div className='list'>
                  {favorites.map(favorite => (
                    <FavoriteItem
                      name={favorite.name}
                      value={favorite.value}
                      onClose={onClose}
                      onRemove={() => {
                        onRemove(favorite)
                      }}
                      onUpdate={() => {
                        onUpdate(favorite)
                      }}
                    />
                  ))}
                </div>
              </div>
              <div className='modal-action'>
                <a
                  href='/#'
                  onClick={e => {
                    e.preventDefault()
                    onClose()
                  }}
                  className='button'
                >
                  <span className='button-content' tabIndex='-1'>
                    <span>Close</span>
                    <span className='secondary desktop-only'>ESC</span>
                  </span>
                </a>
              </div>
            </div>
          </div>
        </CSSTransition>
      )}
    </TransitionGroup>
  )
}

Modal.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
}

export default Modal
