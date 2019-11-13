import PropTypes from 'prop-types'
import React from 'react'

function FieldError({ children }) {
  return (
    <div className='control-error'>
      <p className='title'>
        <strong>{children}</strong>
      </p>
    </div>
  )
}

FieldError.propTypes = {
  children: PropTypes.string.isRequired,
}

export default FieldError
