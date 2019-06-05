import PropTypes from 'prop-types'
import React from 'react'

import Radio from './Radio'

class RadioGroup extends React.Component {
  constructor(props) {
    super(props)
    this.state = { selected: props.selected, options: props.options }
  }

  onChange = value => {
    this.setState({
      selected: value,
    })
    if (this.props.onChange) {
      this.props.onChange(value)
    }
  }

  render() {
    const { options } = this.state

    const allOptions = options.map((option, i) => {
      return (
        <Radio
          key={i}
          checked={this.state.selected === option.key}
          text={option.text}
          value={option.key}
          disabled={this.props.disabled}
          handler={this.onChange}
        />
      )
    })

    return <div className='group-radio'>{allOptions}</div>
  }
}

RadioGroup.defaultProps = {
  disabled: false,
}

RadioGroup.propTypes = {
  name: PropTypes.string.isRequired,
  selected: PropTypes.string.isRequired,
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
