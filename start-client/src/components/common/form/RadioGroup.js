import PropTypes from 'prop-types'
import React from 'react'

import Radio from './Radio'

function RadioGroup({ onChange, options, error, selected, disabled }) {
  const onChangeHandler = value => {
    if (onChange) {
      onChange(value)
    }
  }
  const allOptions = options.map(option => {
    return (
      <Radio
        key={option.key}
        checked={!error && selected === option.key}
        text={option.text}
        value={option.key}
        disabled={disabled}
        handler={onChangeHandler}
      />
    )
  })
  if (error) {
    allOptions.push(
      <Radio
        key={allOptions.length + 1}
        checked
        text={error}
        value={error}
        disabled={disabled}
        handler={onChangeHandler}
        error
      />
    )
  }
  return <div className='group-radio'>{allOptions}</div>
}

RadioGroup.defaultProps = {
  selected: '',
  error: null,
  onChange: null,
  disabled: false,
  options: {
    error: '',
  },
}

RadioGroup.propTypes = {
  selected: PropTypes.string,
  error: PropTypes.string,
  options: PropTypes.arrayOf(
    PropTypes.shape({
      key: PropTypes.string.isRequired,
      text: PropTypes.string.isRequired,
    })
  ),
  onChange: PropTypes.func,
  disabled: PropTypes.bool,
}

export default RadioGroup
