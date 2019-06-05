import PropTypes from 'prop-types'
import React from 'react'

import { IconCheck, IconTimes } from './../icons'
import { isInRange, rangeToText } from './../../utils/versions'

class List extends React.Component {
  onClick = (dependency, event) => {
    event.preventDefault()
    this.props.remove(dependency)
    return false
  }

  render() {
    let dependencies = this.props.list
    return (
      <div className='dependencies-list dependencies-list-checked'>
        {dependencies.map(dependency => {
          const compatibility = dependency.versionRange
            ? isInRange(this.props.boot, dependency.versionRange)
            : true
          return (
            <a
              className={`dependency-item checked ${
                !compatibility ? 'invalid' : ''
              }`}
              href='/'
              onClick={event => {
                this.onClick(dependency, event)
              }}
              key={dependency.id}
            >
              <div key={`d1${dependency.id}`}>
                <strong key={`d2${dependency.id}`}>{dependency.name}</strong>
                <br key={`d3${dependency.id}`} />

                {compatibility && (
                  <span key={`d4${dependency.id}`} className='description'>
                    {dependency.description}
                  </span>
                )}
                <span key={`d5${dependency.id}`} className='icon'>
                  <IconTimes key={`d6${dependency.id}`} />
                  <IconCheck key={`d7${dependency.id}`} />
                </span>
                {!compatibility && (
                  <span className='warning' key={`warning${dependency.id}`}>
                    Requires Spring Boot{' '}
                    {rangeToText(dependency.versionRequirement)}.
                  </span>
                )}
              </div>
            </a>
          )
        })}
      </div>
    )
  }
}

List.propTypes = {
  list: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired,
      group: PropTypes.string.isRequired,
      description: PropTypes.string.isRequired,
    })
  ),
  remove: PropTypes.func.isRequired,
  boot: PropTypes.string.isRequired,
}

export default List
