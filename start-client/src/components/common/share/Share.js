import '../../../styles/share.scss'

import PropTypes from 'prop-types'
import React from 'react'

import Overlay from './Overlay'
import Popover from './Popover'

function Share({ shareUrl, open, onClose, position }) {
  return (
    <>
      <Popover
        open={open || false}
        shareUrl={shareUrl}
        position={position}
        onClose={onClose}
      />
      <Overlay open={open || false} />
    </>
  )
}

Share.propTypes = {
  shareUrl: PropTypes.string.isRequired,
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  position: PropTypes.shape({
    x: PropTypes.number.isRequired,
    y: PropTypes.number.isRequired,
  }).isRequired,
}

export default Share
