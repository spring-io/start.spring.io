import PropTypes from 'prop-types'
import React from 'react'
import { CSSTransition, TransitionGroup } from 'react-transition-group'

function Overlay({ open }) {
  return (
    <TransitionGroup component={null}>
      {open && (
        <CSSTransition classNames='overlay' timeout={100}>
          <div className='overlay' />
        </CSSTransition>
      )}
    </TransitionGroup>
  )
}

Overlay.propTypes = {
  open: PropTypes.bool.isRequired,
}

export default Overlay
