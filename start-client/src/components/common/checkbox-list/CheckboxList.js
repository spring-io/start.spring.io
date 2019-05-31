import PropTypes from 'prop-types'
import React from 'react'
import get from 'lodash.get'

import DependencyGroup from './DependencyGroup'
import { isInRange } from './../../utils/versions'

class CheckboxList extends React.Component {
  groupByParent = list => {
    const map = []
    const getParent = (map, name) => {
      return map.find(item => item.group === name)
    }
    for (var i = 0; i < list.length; i++) {
      const dep = list[i]
      let parent = getParent(map, dep.group)
      if (!parent) {
        parent = {
          group: dep.group,
          children: [],
        }
        map.push(parent)
      }
      const valid = dep.versionRange
        ? isInRange(this.props.boot, dep.versionRange)
        : true
      parent.children.push({ ...dep, valid: valid })
    }
    return map
  }

  render() {
    const groups = this.groupByParent(this.props.list)
    const select = {}
    this.props.checked.forEach(item => {
      select[item.id] = true
    })
    return (
      <div className='groups'>
        {groups.map(group => {
          const isExpand = get(this.props.stateGroups, group.group, true)
          return (
            <DependencyGroup
              key={group.group}
              dependencyGroup={group}
              addDependency={this.props.add}
              removeDependency={this.props.remove}
              selectedDependencies={select}
              toggleGroup={this.props.toggleGroup}
              expand={isExpand}
            />
          )
        })}
      </div>
    )
  }
}

CheckboxList.propTypes = {
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
  checked: PropTypes.arrayOf(
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
  remove: PropTypes.func.isRequired,
  toggleGroup: PropTypes.func.isRequired,
  boot: PropTypes.string.isRequired,
  stateGroups: PropTypes.any.isRequired,
}

export default CheckboxList
