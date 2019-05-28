import PropTypes from 'prop-types'
import React from 'react'

class Radio extends React.Component {
  onClick = event => {
    event.preventDefault()
    this.props.handler(this.props.value)
  }

  render() {
    if (this.props.disabled) {
      return (
        <span className={`radio ${this.props.checked ? 'checked' : ''}`}>
          {this.props.text}
        </span>
      )
    }
    return (
      <a
        href='/'
        className={`radio ${this.props.checked ? 'checked' : ''}`}
        onClick={this.onClick}
      >
        {this.props.text}
      </a>
    )
  }
}

Radio.defaultProps = {
  disabled: false,
}

Radio.propTypes = {
  checked: PropTypes.bool.isRequired,
  text: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  handler: PropTypes.func.isRequired,
  disabled: PropTypes.bool,
}

export default Radio
