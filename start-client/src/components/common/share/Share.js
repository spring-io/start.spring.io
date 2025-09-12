import '../../../styles/share.scss'

import PropTypes from 'prop-types'
import React from 'react'

import Popover from './Popover'
import { Overlay } from '../form'

function Share({ shareUrl, open, onClose }) {
  return (
    <>
      <Popover open={open || false} shareUrl={shareUrl} onClose={onClose} />
      <Overlay open={open || false} />
    </>
  )
}

Share.propTypes = {
  shareUrl: PropTypes.string.isRequired,
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
}

export default Share
