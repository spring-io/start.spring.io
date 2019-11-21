import PropTypes from 'prop-types'
import React from 'react'
import { CSSTransition, TransitionGroup } from 'react-transition-group'

import { IconCheck, IconTimes } from '../../icons'

function Dependencies({ list, remove }) {
  return (
    <TransitionGroup
      component='ul'
      className='dependencies-list dependencies-list-checked'
    >
      {list.map(item => {
        return (
          <CSSTransition timeout={500} classNames='fade' key={`f${item.id}`}>
            <li>
              <span className='dependency-item-span'>
                <a
                  className={`dependency-item checked ${
                    !item.valid ? 'invalid' : ''
                  }`}
                  href='/'
                  onClick={event => {
                    event.preventDefault()
                    remove(item)
                  }}
                  key={item.id}
                >
                  <div key={`d1${item.id}`}>
                    <strong key={`d2${item.id}`}>{item.name}</strong>
                    <br key={`d3${item.id}`} />
                    {item.valid && (
                      <span key={`d4${item.id}`} className='description'>
                        {item.description}
                      </span>
                    )}
                    <span key={`d5${item.id}`} className='icon'>
                      <IconTimes key={`d6${item.id}`} />
                      <IconCheck key={`d7${item.id}`} />
                    </span>
                    {!item.valid && (
                      <span className='warning' key={`warning${item.id}`}>
                        {item.message}
                      </span>
                    )}
                  </div>
                </a>
              </span>
            </li>
          </CSSTransition>
        )
      })}
    </TransitionGroup>
  )
}

Dependencies.defaultProps = {
  list: [],
}

Dependencies.propTypes = {
  list: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired,
      group: PropTypes.string.isRequired,
      valid: PropTypes.bool.isRequired,
      description: PropTypes.string.isRequired,
      message: PropTypes.string,
    })
  ),
  remove: PropTypes.func.isRequired,
}

export default Dependencies
