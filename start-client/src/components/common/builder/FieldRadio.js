import PropTypes from 'prop-types'
import React from 'react'

import { Radio } from '../form'

function FieldRadio({ id, text, value, onChange, disabled, options }) {
  return (
    <div className='control control-inline'>
      <label htmlFor={id}>{text}</label>
      <Radio
        name='packaging'
        disabled={disabled}
        selected={value}
        options={options}
        onChange={onChange}
      />
    </div>
  )
}

FieldRadio.defaultProps = {
  disabled: false,
  options: [],
}

FieldRadio.propTypes = {
  id: PropTypes.string.isRequired,
  text: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
  disabled: PropTypes.bool,
  options: PropTypes.arrayOf(
    PropTypes.shape({
      key: PropTypes.string,
      text: PropTypes.string,
    })
  ),
}

export default FieldRadio
