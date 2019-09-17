import PropTypes from 'prop-types'
import React from 'react'

class Boot extends React.Component {
  render = () => {
    return (
      <div className='control-error'>
        <p className='title'>
          <strong>
            Spring Boot {this.props.value} is not supported. Please select a
            valid version.
          </strong>
        </p>
      </div>
    )
  }
}

Boot.propTypes = {
  value: PropTypes.string.isRequired,
}

export default Boot
