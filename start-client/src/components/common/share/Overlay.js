import PropTypes from 'prop-types'
import React from 'react'
import { CSSTransition, TransitionGroup } from 'react-transition-group'

import { noScroll } from '../../utils/NoScroll'

function Overlay({ open }) {
  const onEnter = () => {
    noScroll.on()
  }

  const onEnded = () => {
    noScroll.off()
  }
  return (
    <TransitionGroup component={null}>
      {open && (
        <CSSTransition
          onEnter={onEnter}
          onExit={onEnded}
          classNames='overlay'
          timeout={500}
        >
          <div className='popup-share-overlay' />
        </CSSTransition>
      )}
    </TransitionGroup>
  )
}

Overlay.propTypes = {
  open: PropTypes.bool.isRequired,
}

export default Overlay
