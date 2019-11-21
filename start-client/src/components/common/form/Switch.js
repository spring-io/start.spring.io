import React from 'react'

const Switch = ({ isOn, onChange }) => {
  return (
    <span className='switch'>
      <input
        checked={isOn}
        onChange={onChange}
        className='switch-checkbox'
        id={`switch-new`}
        type='checkbox'
      />
      <label className='switch-label' htmlFor={`switch-new`}>
        <span className={`switch-button`} />
      </label>
    </span>
  )
}

export default Switch
