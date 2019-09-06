import PropTypes from 'prop-types'
import React from 'react'
import { CSSTransition, TransitionGroup } from 'react-transition-group'

import { IconPlus } from '../icons'
import { isInRange, rangeToText } from '../../utils/versions'

class SearchResultsList extends React.Component {
  onClick = dependency => {
    this.props.onAdded(dependency)
  }

  onMouseEnter = index => {
    this.props.onSelectedChanged(index)
  }

  onMouseLeave = index => {
    this.props.onSelectedChanged(-1)
  }

  render() {
    let dependencies = this.props.dependencies
    return (
      <TransitionGroup component='ul' className='dependencies-list'>
        {dependencies.map((dependency, index) => {
          const valid = dependency.versionRange
            ? isInRange(this.props.boot, dependency.versionRange)
            : true

          return (
            <CSSTransition
              timeout={500}
              classNames='fade'
              key={`f${dependency.id}`}
            >
              <li>
                <span className='dependency-item-span'>
                  <a
                    href='/'
                    className={`dependency-item dependency-item-gray ${
                      !valid ? 'invalid' : ''
                    }  ${this.props.selected === index ? 'selected' : ''}`}
                    key={`item${dependency.id}`}
                    selected={this.props.selected === index}
                    disabled={!valid}
                    onClick={e => {
                      e.preventDefault()
                      if (valid) {
                        this.onClick(dependency)
                      }
                    }}
                    onMouseEnter={() => {
                      this.onMouseEnter(index)
                    }}
                    onMouseLeave={() => {
                      this.onMouseLeave(index)
                    }}
                  >
                    <div>
                      <strong className='title' key={`item${dependency.id}`}>
                        {dependency.name}
                      </strong>
                      <br />
                      {valid && (
                        <span key={`d1${dependency.id}`}>
                          <span
                            className='description'
                            key={`d2${dependency.id}`}
                          >
                            {dependency.description}
                          </span>
                          <span key={`d3${dependency.id}`} className='icon'>
                            <IconPlus key={`d4${dependency.id}`} />
                          </span>
                        </span>
                      )}
                      {!valid && (
                        <span
                          className='warning'
                          key={`warning${dependency.id}`}
                        >
                          Requires Spring Boot{' '}
                          {rangeToText(dependency.versionRequirement)}.
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
}

SearchResultsList.propTypes = {
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
  onAdded: PropTypes.func.isRequired,
  boot: PropTypes.string.isRequired,
  selected: PropTypes.number,
  onSelectedChanged: PropTypes.func.isRequired,
}

export default SearchResultsList
