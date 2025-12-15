import PropTypes from 'prop-types'
import React from 'react'

function FieldInput({ id, text, value, onChange, disabled, inputRef, className }) {
  return (
    <div className={`control control-inline ${className || ''}`}>
      <label htmlFor={id}>{text}</label>
      <input
        type='text'
        id={id}
        className='input'
        disabled={disabled}
        value={value}
        onChange={onChange}
        ref={inputRef}
      />
    </div>
  )
}

FieldInput.defaultProps = {
  disabled: false,
  inputRef: null,
  className: null,
}

FieldInput.propTypes = {
  id: PropTypes.string.isRequired,
  text: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
  inputRef: PropTypes.oneOfType([
    PropTypes.func,
    PropTypes.shape({ current: PropTypes.instanceOf(Element) }),
  ]),
  className: PropTypes.string,
  disabled: PropTypes.bool,
}

export default FieldInput
