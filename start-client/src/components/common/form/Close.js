import PropTypes from 'prop-types'
import React from 'react'

import { IconTimes } from '../icons'

const Close = ({ onClose }) => (
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

Close.defaultProps = {
  onClose: null,
}

Close.propTypes = {
  onClose: PropTypes.func,
}

export default Close
