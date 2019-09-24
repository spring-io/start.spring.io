import PropTypes from 'prop-types'
import React from 'react'
import { CSSTransition, TransitionGroup } from 'react-transition-group'

class ShareOverlay extends React.Component {
  render = () => {
    return (
      <TransitionGroup component={null}>
        {this.props.open && (
          <CSSTransition classNames='overlay' timeout={500}>
            <div className='popup-share-overlay'></div>
          </CSSTransition>
        )}
      </TransitionGroup>
    )
  }
}

ShareOverlay.propTypes = {
  open: PropTypes.bool.isRequired,
}

export default ShareOverlay
