import PropTypes from 'prop-types'
import React from 'react'

function RadioInput({ handler, value, disabled, error, checked, text }) {
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
        <span className='caret' tabIndex='-1' />
        <span className='radio-content' tabIndex='-1'>
          {text}
        </span>
      </span>
    )
  }
  return (
    <a
      href='/'
      className={`radio ${checked ? 'checked' : ''}`}
      onClick={onClick}
    >
      <span className='caret' tabIndex='-1' />
      <span className='radio-content' tabIndex='-1'>
        {text}
      </span>
    </a>
  )
}

RadioInput.defaultProps = {
  disabled: false,
  error: false,
}

RadioInput.propTypes = {
  checked: PropTypes.bool.isRequired,
  text: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  handler: PropTypes.func.isRequired,
  disabled: PropTypes.bool,
  error: PropTypes.bool,
}

function Radio({ onChange, options, error, selected, disabled }) {
  const onChangeHandler = value => {
    if (onChange) {
      onChange(value)
    }
  }
  const allOptions = options.map(option => {
    return (
      <RadioInput
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
      <RadioInput
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

Radio.defaultProps = {
  selected: '',
  error: null,
  onChange: null,
  disabled: false,
  options: {
    error: '',
  },
}

Radio.propTypes = {
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

export default Radio
