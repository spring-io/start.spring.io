import PropTypes from 'prop-types'
import React from 'react'

import Radio from './Radio'

class RadioGroup extends React.Component {
  onChange = value => {
    if (this.props.onChange) {
      this.props.onChange(value)
    }
  }

  render() {
    const { options, error } = this.props
    const allOptions = options.map((option, i) => {
      return (
        <Radio
          key={i}
          checked={!error && this.props.selected === option.key}
          text={option.text}
          value={option.key}
          disabled={this.props.disabled}
          handler={this.onChange}
        />
      )
    })
    if (error) {
      allOptions.push(
        <Radio
          key={allOptions.length + 1}
          checked={true}
          text={error}
          value={error}
          disabled={this.props.disabled}
          handler={this.onChange}
          error={true}
        />
      )
    }

    return <div className='group-radio'>{allOptions}</div>
  }
}

RadioGroup.defaultProps = {
  disabled: false,
  options: {
    error: '',
  },
}

RadioGroup.propTypes = {
  name: PropTypes.string.isRequired,
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
