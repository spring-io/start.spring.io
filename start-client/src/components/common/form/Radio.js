import PropTypes from 'prop-types'
import React from 'react'

function Radio({ handler, value, disabled, error, checked, text }) {
  const onClick = event => {
    event.preventDefault()
    handler(value)
  }
  if (disabled || error) {
    return (
      <span
        className={`radio disabled ${checked ? 'checked' : ''} ${
          error ? 'err' : ''
        }`}
      >
        {text}
      </span>
    )
  }
  return (
    <a
      href='/'
      className={`radio ${checked ? 'checked' : ''}`}
      onClick={onClick}
    >
      {text}
    </a>
  )
}

Radio.defaultProps = {
  disabled: false,
  error: false,
}

Radio.propTypes = {
  checked: PropTypes.bool.isRequired,
  text: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  handler: PropTypes.func.isRequired,
  disabled: PropTypes.bool,
  error: PropTypes.bool,
}

export default Radio
