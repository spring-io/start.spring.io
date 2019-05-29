import PropTypes from 'prop-types'
import React from 'react'

import { IconCheck, IconPlus, IconTimes } from './../icons'
import { rangeToText } from './../../utils/versions'

class DependencyItem extends React.Component {
  onClick = event => {
    const target = event.target
    if (target.checked) {
      this.props.addDependency(this.props.dep)
    } else {
      this.props.removeDependency(this.props.dep)
    }
  }

  render() {
    const dep = this.props.dep
    const selectedDependencies = this.props.selectedDependencies
    return (
      <a
        href='/'
        onClick={event => {
          event.preventDefault()
          if (!dep.valid) {
            return
          }
          this.onClick({
            target: {
              value: dep.id,
              checked: !selectedDependencies[dep.id] === true,
            },
          })
        }}
        tabIndex={!dep.valid ? -1 : ''}
        className={`${!dep.valid ? 'invalid' : ''} ${
          selectedDependencies[dep.id] === true ? 'checked' : ''
        }`}
        key={dep.id}
      >
        <div key={`d${dep.id}`}>
          <input
            type='checkbox'
            value={dep.id}
            key={`ck${dep.id}`}
            checked={selectedDependencies[dep.id] === true}
            disabled={!dep.valid}
            onChange={this.onClick}
          />
          <strong key={`ck1${dep.id}`}>{dep.name}</strong>
          <br key={`br${dep.id}`} />
          {dep.valid && <span key={`ck2${dep.id}`}>{dep.description}</span>}
          <span key={`ck3${dep.id}`} className='icon'>
            <IconPlus key={`ck4${dep.id}`} />
            <IconTimes key={`ck5${dep.id}`} />
            <IconCheck key={`ck6${dep.id}`} />
          </span>
          {!dep.valid && (
            <span className='warning' key={`warning${dep.id}`}>
              Requires Spring Boot {rangeToText(dep.versionRequirement)}.
            </span>
          )}
        </div>
      </a>
    )
  }
}

DependencyItem.propTypes = {
  dep: PropTypes.shape({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    description: PropTypes.string.isRequired,
    group: PropTypes.string.isRequired,
    valid: PropTypes.bool.isRequired,
    versionRange: PropTypes.string,
    versionRequirement: PropTypes.string,
    keywords: PropTypes.string,
    weight: PropTypes.number,
  }),
  addDependency: PropTypes.func.isRequired,
  removeDependency: PropTypes.func.isRequired,
  selectedDependencies: PropTypes.object,
}

export default DependencyItem
