import '../../../styles/history.scss'

import PropTypes from 'prop-types'
import React from 'react'

import Modal from './Modal'
import { Overlay } from '../form'

function History({ open, onClose }) {
  return (
    <>
      <Modal open={open || false} onClose={onClose} />
      <Overlay open={open || false} />
    </>
  )
}

History.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
}

export default History
