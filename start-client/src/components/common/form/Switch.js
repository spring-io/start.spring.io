import PropTypes from 'prop-types'
import React from 'react'

const Switch = ({ isOn, onChange }) => {
  return (
    <span className='switch'>
      <input
        checked={isOn}
        onChange={onChange}
        className='switch-checkbox'
        id='switch-new'
        name='switch-new'
        type='checkbox'
      />
      {/* eslint-disable-next-line */}
      <label className='switch-label' htmlFor='switch-new'>
        <span className='switch-button' />
      </label>
    </span>
  )
}

Switch.defaultProps = {
  isOn: false,
  onChange: null,
}

Switch.propTypes = {
  isOn: PropTypes.bool,
  onChange: PropTypes.func,
}

export default Switch
