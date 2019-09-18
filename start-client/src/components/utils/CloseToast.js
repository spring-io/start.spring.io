import React from 'react'

import { IconTimes } from './../common/icons'

const CloseToast = ({ closeToast }) => (
  <span className='toast-close' onClick={closeToast}>
    <IconTimes />
  </span>
)

export default CloseToast
