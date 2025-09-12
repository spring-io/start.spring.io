import PropTypes from 'prop-types'
import React from 'react'

function Actions({ children }) {
  return (
    <div className='actions'>
      <div className='actions-container'>{children}</div>
    </div>
  )
}

Actions.defaultProps = {
  children: null,
}

Actions.propTypes = {
  children: PropTypes.node,
}

export default Actions
