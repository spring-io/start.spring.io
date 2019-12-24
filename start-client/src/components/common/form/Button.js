import PropTypes from 'prop-types'
import React from 'react'

function Button({
  id,
  onClick,
  children,
  variant,
  hotkey,
  onMouseOut,
  onMouseMove,
  onBlur,
  onFocus,
}) {
  return (
    <button
      className={`button ${variant === 'primary' ? 'primary' : ''}`}
      type='button'
      id={id}
      onClick={event => {
        if (onClick) {
          onClick(event)
        }
      }}
      onMouseOut={event => {
        if (onMouseOut) {
          onMouseOut(event)
        }
      }}
      onMouseMove={event => {
        if (onMouseMove) {
          onMouseMove(event)
        }
      }}
      onBlur={event => {
        if (onBlur) {
          onBlur(event)
        }
      }}
      onFocus={event => {
        if (onFocus) {
          onFocus(event)
        }
      }}
    >
      {children}
      {hotkey && (
        <>
          {' '}
          <span className='desktop-only'>- {hotkey}</span>
        </>
      )}
    </button>
  )
}

Button.defaultProps = {
  onClick: null,
  onMouseOut: null,
  onMouseMove: null,
  onBlur: null,
  onFocus: null,
  children: null,
  variant: '',
  hotkey: '',
}

Button.propTypes = {
  id: PropTypes.string.isRequired,
  variant: PropTypes.string,
  hotkey: PropTypes.string,
  onClick: PropTypes.func,
  onMouseOut: PropTypes.func,
  onMouseMove: PropTypes.func,
  onBlur: PropTypes.func,
  onFocus: PropTypes.func,
  children: PropTypes.node,
}

export default Button
