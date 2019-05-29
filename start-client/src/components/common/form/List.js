import PropTypes from 'prop-types'
import React from 'react'

import Versions from './../../utils/versions'
import { IconCheck, IconTimes } from './../icons'

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
            ? Versions.isInRange(this.props.boot, dependency.versionRange)
            : true
          return (
            <a
              className='dependency-item checked'
              href='/'
              onClick={event => {
                this.onClick(dependency, event)
              }}
              key={dependency.id}
            >
              <div key={`d1${dependency.id}`}>
                <strong key={`d2${dependency.id}`}>
                  {dependency.name} <span>{dependency.group}</span>
                </strong>
                <br key={`d3${dependency.id}`} />
                <span key={`d4${dependency.id}`} className='description'>
                  {dependency.description}
                </span>
                {compatibility && (
                  <span key={`d5${dependency.id}`} className='icon'>
                    <IconTimes key={`d6${dependency.id}`} />
                    <IconCheck key={`d7${dependency.id}`} />
                  </span>
                )}
                {!compatibility && (
                  <span className='warning' key={`warning${dependency.id}`}>
                    Requires Spring Boot {dependency.versionRequirement}.
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
