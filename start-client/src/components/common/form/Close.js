import PropTypes from 'prop-types'
import React from 'react'

import { IconTimes } from '../icons'

function Close({ onClose }) {
  return (
    <a
      href='/#'
      className='toast-close'
      onClick={event => {
        event.preventDefault()
        if (onClose) {
          onClose()
        }
      }}
    >
      <IconTimes />
    </a>
  )
}

Close.defaultProps = {
  onClose: null,
}

Close.propTypes = {
  onClose: PropTypes.func,
}

export default Close
