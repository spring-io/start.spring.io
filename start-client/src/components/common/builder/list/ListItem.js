import PropTypes from 'prop-types'
import React from 'react'

import { IconCheck, IconPlus, IconTimes } from '../../icons'

function ListItem({
  id,
  name,
  description,
  valid,
  message,
  onChange,
  selected,
}) {
  const toggle = event => {
    event.preventDefault()
    if (valid) {
      onChange(!selected)
    }
  }
  const onKeyDown = event => {
    const keyPressed = event.key
    if (keyPressed === 'Enter' || keyPressed === ' ') {
      toggle(event)
    }
  }
  return (
    <a
      href='/'
      onClick={toggle}
      tabIndex={!valid ? -1 : ''}
      className={`${!valid ? 'invalid' : ''} ${selected ? 'checked' : ''}`}
      key={id}
      onKeyDown={onKeyDown}
    >
      <div key={`d${id}`}>
        <input
          type='checkbox'
          value={id}
          key={`ck${id}`}
          checked={selected}
          disabled={!valid}
          onChange={() => {}}
        />
        <strong key={`ck1${id}`}>{name}</strong>
        <br key={`br${id}`} />
        {valid && <span key={`ck2${id}`}>{description}</span>}
        <span key={`ck3${id}`} className='icon'>
          <IconPlus key={`ck4${id}`} />
          <IconTimes key={`ck5${id}`} />
          <IconCheck key={`ck6${id}`} />
        </span>
        {!valid && (
          <span className='warning' key={`warning${id}`}>
            {message}
          </span>
        )}
      </div>
    </a>
  )
}

ListItem.defaultProps = {
  message: '',
}

ListItem.propTypes = {
  id: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
  description: PropTypes.string.isRequired,
  valid: PropTypes.bool.isRequired,
  message: PropTypes.string,
  onChange: PropTypes.func.isRequired,
  selected: PropTypes.bool.isRequired,
}

export default ListItem
