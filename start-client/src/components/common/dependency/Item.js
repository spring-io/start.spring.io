import PropTypes from 'prop-types'
import get from 'lodash.get'
import React, { useContext } from 'react'

import { IconEnter } from '../icons'
import { InitializrContext } from '../../reducer/Initializr'

function Item({ item, selected, onSelect, onAdd, group, index }) {
  const { dispatch } = useContext(InitializrContext)

  const onClick = event => {
    event.preventDefault()
    if (item.valid) {
      dispatch({
        type: 'ADD_DEPENDENCY',
        payload: { id: item.id },
      })
    }
    onAdd()
  }
  return (
    <li key={`li-${get(item, 'id')}`}>
      <a
        href='/'
        className={`dependency ${selected ? 'selected' : ''} ${
          !item.valid ? 'disabled' : ''
        }`}
        onClick={onClick}
        onMouseMove={() => {
          onSelect(index)
        }}
      >
        <strong>
          {get(item, 'name')}{' '}
          {group && <span className='group'>{get(item, 'group')}</span>}
        </strong>
        <span>{get(item, 'description')}</span>
        <IconEnter />
        {!item.valid && <span className='invalid'>{item.message}</span>}
      </a>
    </li>
  )
}

Item.defaultProps = {
  selected: false,
  group: true,
}

Item.propTypes = {
  selected: PropTypes.bool,
  group: PropTypes.bool,
  onSelect: PropTypes.func.isRequired,
  onAdd: PropTypes.func.isRequired,
  index: PropTypes.number.isRequired,
  item: PropTypes.shape({
    description: PropTypes.string.isRequired,
    group: PropTypes.string.isRequired,
    id: PropTypes.string.isRequired,
    keywords: PropTypes.string,
    message: PropTypes.string,
    name: PropTypes.string.isRequired,
    valid: PropTypes.bool.isRequired,
  }).isRequired,
}

export default Item
