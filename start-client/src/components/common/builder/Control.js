import PropTypes from 'prop-types'
import React from 'react'

function Control({ text, children, labelFor }) {
  return (
    <div className='control'>
      <label className='label' htmlFor={labelFor}>
        {text}
      </label>
      <div className='control-element'>{children}</div>
    </div>
  )
}

Control.defaultProps = {
  children: null,
  labelFor: '',
}

Control.propTypes = {
  children: PropTypes.node,
  labelFor: PropTypes.string,
  text: PropTypes.string.isRequired,
}

export default Control
