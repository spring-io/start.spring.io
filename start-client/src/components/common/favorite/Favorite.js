import '../../../styles/favorite.scss'

import PropTypes from 'prop-types'
import React from 'react'

import Modal from './Modal'
import Add from './Add'
import { Overlay } from '../form'

function Favorite({ open, add, onClose }) {
  return (
    <>
      <Modal open={open || false} onClose={onClose} />
      <Add open={add || false} onClose={onClose} />
      <Overlay open={open || add} />
    </>
  )
}

Favorite.propTypes = {
  open: PropTypes.bool.isRequired,
  add: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
}

export default Favorite
