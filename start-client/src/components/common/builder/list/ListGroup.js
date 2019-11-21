import PropTypes from 'prop-types'
import React from 'react'

import ListItem from './ListItem'
import { IconChevronRight } from '../../icons'

function ListGroup({
  group,
  items,
  itemsSelected,
  isClose,
  add,
  remove,
  toggle,
}) {
  const toggleGroup = event => {
    event.preventDefault()
    toggle(group)
  }
  const onKeyDown = event => {
    const keyPressed = event.key
    if (keyPressed === 'Enter' || keyPressed === ' ') {
      toggleGroup(event)
    }
  }
  const isItemSelected = item => {
    return !!itemsSelected.find(o => o === item.id)
  }
  return (
    <div className='group'>
      <div className='group-title'>
        <a
          href='/'
          onClick={toggleGroup}
          className={!isClose ? 'toggleGroupItems' : ''}
          tabIndex={0}
          onKeyDown={onKeyDown}
        >
          <IconChevronRight />
          {group}
        </a>
      </div>
      {!isClose && (
        <div className='group-items' key={`links${group}`}>
          {items.map(item => (
            <ListItem
              key={item.id}
              id={item.id}
              name={item.name}
              description={item.description}
              valid={item.valid}
              message={item.message}
              selected={isItemSelected(item)}
              onChange={value => {
                if (value) {
                  add(item.id)
                } else {
                  remove(item.id)
                }
              }}
            />
          ))}
        </div>
      )}
    </div>
  )
}

ListGroup.propTypes = {
  group: PropTypes.string.isRequired,
  add: PropTypes.func.isRequired,
  remove: PropTypes.func.isRequired,
  toggle: PropTypes.func.isRequired,
  itemsSelected: PropTypes.arrayOf(PropTypes.string).isRequired,
  isClose: PropTypes.bool.isRequired,
  items: PropTypes.arrayOf(
    PropTypes.shape({
      description: PropTypes.string.isRequired,
      group: PropTypes.string.isRequired,
      id: PropTypes.string.isRequired,
      keywords: PropTypes.string,
      name: PropTypes.string.isRequired,
      valid: PropTypes.bool.isRequired,
    })
  ).isRequired,
}

export default ListGroup
