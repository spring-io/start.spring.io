import PropTypes from 'prop-types'
import React from 'react'
import { CSSTransition, TransitionGroup } from 'react-transition-group'

import { IconPlus } from '../../icons'

function Result({ list, selected, select, add }) {
  return (
    <TransitionGroup component='ul' className='dependencies-list'>
      {list.map((item, index) => {
        return (
          <CSSTransition timeout={500} classNames='fade' key={`f${item.id}`}>
            <li>
              <span className='dependency-item-span'>
                <a
                  href='/'
                  className={`dependency-item dependency-item-gray ${
                    !item.valid ? 'invalid' : ''
                  }  ${selected === index ? 'selected' : ''}`}
                  key={`item${item.id}`}
                  selected={selected === index}
                  disabled={!item.valid}
                  onClick={e => {
                    e.preventDefault()
                    if (item.valid) {
                      add(item)
                    }
                  }}
                  onMouseEnter={() => {
                    if (item.valid) {
                      select(index)
                    }
                  }}
                  onMouseLeave={() => {
                    if (item.valid) {
                      select(-1)
                    }
                  }}
                >
                  <div>
                    <strong className='title' key={`item${item.id}`}>
                      {item.name}
                    </strong>
                    <br />
                    {item.valid && (
                      <span key={`d1${item.id}`}>
                        <span className='description' key={`d2${item.id}`}>
                          {item.description}
                        </span>
                        <span key={`d3${item.id}`} className='icon'>
                          <IconPlus key={`d4${item.id}`} />
                        </span>
                      </span>
                    )}
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
  // }
}

Result.defaultProps = {
  list: [],
  selected: null,
}

Result.propTypes = {
  list: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired,
      group: PropTypes.string.isRequired,
      description: PropTypes.string.isRequired,
      versionRange: PropTypes.string,
      versionRequirement: PropTypes.string,
    })
  ),
  add: PropTypes.func.isRequired,
  selected: PropTypes.number,
  select: PropTypes.func.isRequired,
}

export default Result
