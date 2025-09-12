import PropTypes from 'prop-types'
import get from 'lodash/get'
import React, { useEffect, useRef, useContext, useMemo } from 'react'
import { CSSTransition, TransitionGroup } from 'react-transition-group'
import { clearAllBodyScrollLocks, disableBodyScroll } from 'body-scroll-lock'
import queryString from 'query-string'
import { AppContext } from '../../reducer/App'
import { Transform } from './Utils'
import { IconFavorite } from '../icons'

function HistoryDate({ label, items, onClose }) {
  return (
    <>
      <div className='date'>{label}</div>
      <ul>
        {items.map(item => (
          <HistoryItem
            key={item.value}
            time={item.time}
            value={item.value}
            onClose={onClose}
          />
        ))}
      </ul>
    </>
  )
}

HistoryDate.propTypes = {
  label: PropTypes.string.isRequired,
  items: PropTypes.arrayOf(
    PropTypes.shape({
      time: PropTypes.string,
      value: PropTypes.string,
    })
  ),
  onClose: PropTypes.func.isRequired,
}

HistoryDate.defaultProps = {
  items: [],
}

function getLabelFromList(list, key) {
  return list.find(item => item.key === key)?.text || key
}

function getLabelFromDepsList(list, key) {
  return list.find(item => item.id === key)?.name || key
}

function HistoryItem({ time, value, onClose }) {
  const { config, dispatch } = useContext(AppContext)
  const params = queryString.parse(value)
  const deps = get(params, 'dependencies', '')
    .split(',')
    .filter(dep => !!dep)

  const onFavorite = () => {
    dispatch({
      type: 'UPDATE',
      payload: {
        history: false,
        favoriteAdd: true,
        favoriteOptions: {
          back: 'history',
          favorite: {
            value,
          },
        },
      },
    })
  }
  return (
    <li>
      <a
        className='item'
        href={`/#${value}`}
        onClick={() => {
          onClose()
        }}
      >
        <span className='time'>{time}</span>
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
      <button type='button' className='favorite' onClick={onFavorite}>
        <IconFavorite />
      </button>
    </li>
  )
}

HistoryItem.propTypes = {
  time: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  onClose: PropTypes.func.isRequired,
}

function Modal({ open, onClose }) {
  const wrapper = useRef(null)
  const { histories, dispatch } = useContext(AppContext)

  const historiesTransform = useMemo(() => Transform(histories), [histories])

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

  return (
    <TransitionGroup component={null}>
      {open && (
        <CSSTransition classNames='popup' timeout={300}>
          <div className='modal-share'>
            <div className='modal-history-container' ref={wrapper}>
              <div className='modal-header'>
                <h1>History</h1>
                <a
                  href='/#'
                  onClick={e => {
                    e.preventDefault()
                    dispatch({ type: 'CLEAR_HISTORY' })
                    onClose()
                  }}
                  className='button'
                >
                  <span className='button-content' tabIndex='-1'>
                    <span>Clear</span>
                  </span>
                </a>
              </div>
              <div className='modal-content'>
                <div className='list'>
                  {historiesTransform.map(history => (
                    <HistoryDate
                      key={history.label}
                      label={history.label}
                      items={history.histories}
                      onClose={onClose}
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
