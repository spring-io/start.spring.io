import PropTypes from 'prop-types'
import get from 'lodash/get'
import React, {useContext, useEffect, useMemo, useRef, useState} from 'react'
import {CSSTransition, TransitionGroup} from 'react-transition-group'
import {clearAllBodyScrollLocks, disableBodyScroll} from 'body-scroll-lock'
import queryString from 'query-string'
import {AppContext} from '../../reducer/App'
import {Transform} from './Utils'
import {IconCheck, IconEditSlim, IconFavorite, IconTimes} from '../icons'

function HistoryDate({ label, items, onClose, editingId, setEditingId }) {
  return (
    <>
      <div className='date'>{label}</div>
      <ul>
        {items.map(item => (
          <HistoryItem
            key={`${item.value}-${item.isoDate}`}
            time={item.time}
            value={item.value}
            name={item.name}
            isoDate={item.isoDate}
            onClose={onClose}
            editingId={editingId}
            setEditingId={setEditingId}
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
      name: PropTypes.string,
      isoDate: PropTypes.string,
    })
  ),
  onClose: PropTypes.func.isRequired,
  editingId: PropTypes.string,
  setEditingId: PropTypes.func.isRequired,
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

function HistoryItem({ time, value, name, isoDate, onClose, editingId, setEditingId }) {
  const { config, dispatch } = useContext(AppContext)
  const itemId = `${value}-${isoDate}`
  const isEditing = editingId === itemId
  const [newName, setNewName] = useState(name || '')
  const params = queryString.parse(value)
  const deps = get(params, 'dependencies', '')
    .split(',')
    .filter(dep => !!dep)

  const defaultLabel = `Project ${getLabelFromList(
    get(config, 'lists.project'),
    get(params, 'type')
  )}, Language ${getLabelFromList(
    get(config, 'lists.language'),
    get(params, 'language')
  )}, Spring Boot ${getLabelFromList(
    get(config, 'lists.boot'),
    get(params, 'platformVersion')
  )}`

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

  const onEdit = () => {
    setNewName(name || defaultLabel)
    setEditingId(itemId)
  }

  const onSave = () => {
    if (newName.trim()) {
      dispatch({
        type: 'UPDATE_HISTORY',
        payload: {
          date: isoDate,
          value,
          name: newName.trim(),
        },
      })
    }
    setEditingId(null)
  }

  const onCancel = () => {
    setNewName(name || defaultLabel)
    setEditingId(null)
  }

  if (isEditing) {
    return (
      <li>
        <div className='item editing'>
          <span className='time'>{time}</span>
          <input
            type='text'
            className='input'
            value={newName}
            onChange={e => setNewName(e.target.value)}
            autoFocus
            onClick={e => e.stopPropagation()}
            onKeyDown={e => {
              if (e.key === 'Enter') {
                e.preventDefault()
                onSave()
              }
              if (e.key === 'Escape') {
                e.preventDefault()
                onCancel()
              }
            }}
          />
          <button type='button' className='save' onClick={onSave} title='Save'>
            <IconCheck />
          </button>
          <button type='button' className='cancel' onClick={onCancel} title='Cancel'>
            <IconTimes />
          </button>
        </div>
      </li>
    )
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
          {name ? (
            <span className='main'>
              <strong>{name}</strong>
            </span>
          ) : (
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
          )}
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
      </a>
      <button type='button' className='favorite' onClick={onFavorite} title='Favorite'>
        <IconFavorite />
      </button>
      <button type='button' className='edit' onClick={onEdit} title='Rename'>
        <IconEditSlim />
      </button>
    </li>
  )
}

HistoryItem.propTypes = {
  time: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  onClose: PropTypes.func.isRequired,
  name: PropTypes.string,
  isoDate: PropTypes.string,
  editingId: PropTypes.string,
  setEditingId: PropTypes.func.isRequired,
}

function Modal({ open, onClose }) {
  const wrapper = useRef(null)
  const { histories, dispatch } = useContext(AppContext)
  const [editingId, setEditingId] = useState(null)

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
                      editingId={editingId}
                      setEditingId={setEditingId}
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
