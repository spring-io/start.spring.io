import get from 'lodash.get'
import React, { useContext } from 'react'
import { CSSTransition, TransitionGroup } from 'react-transition-group'

import { AppContext } from '../../reducer/App'
import { IconRemove } from '../icons'
import { InitializrContext } from '../../reducer/Initializr'

function List() {
  const { values, dispatch } = useContext(InitializrContext)
  const { dependencies } = useContext(AppContext)
  const list = get(values, 'dependencies', []).map(dep => {
    return dependencies.list.find(item => item.id === dep)
  })
  return (
    <TransitionGroup component='ul' className='dependencies-list'>
      {list.map(item => {
        return (
          <CSSTransition timeout={300} classNames='fade' key={`f${item.id}`}>
            <li key={`d0${item.id}`}>
              <div
                className={`dependency-item ${!item.valid ? 'disabled' : ''}`}
                key={`d1${item.id}`}
              >
                <strong key={`d2${item.id}`}>
                  {item.name}{' '}
                  <span className='group'>{get(item, 'group')}</span>
                </strong>
                {item.valid && (
                  <span key={`d4${item.id}`} className='description'>
                    {item.description}
                  </span>
                )}
                <a
                  href='/'
                  onClick={event => {
                    event.preventDefault()
                    dispatch({
                      type: 'REMOVE_DEPENDENCY',
                      payload: { id: item.id },
                    })
                  }}
                  key={item.id}
                  className='icon'
                >
                  <span className='a-content' tabIndex='-1'>
                    <IconRemove />
                  </span>
                </a>
                {!item.valid && (
                  <span className='invalid' key={`warning${item.id}`}>
                    {item.message}
                  </span>
                )}
              </div>
            </li>
          </CSSTransition>
        )
      })}
    </TransitionGroup>
  )
}

export default List
