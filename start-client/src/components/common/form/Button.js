import PropTypes from 'prop-types'
import React from 'react'

function Button({ id, onClick, children, variant, hotkey, buttonRef }) {
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
      ref={buttonRef}
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
  children: null,
  variant: '',
  hotkey: '',
  buttonRef: null,
}

Button.propTypes = {
  id: PropTypes.string.isRequired,
  variant: PropTypes.string,
  hotkey: PropTypes.string,
  onClick: PropTypes.func,
  children: PropTypes.node,
  buttonRef: PropTypes.oneOfType([
    PropTypes.func,
    PropTypes.shape({ current: PropTypes.instanceOf(Element) }),
  ]),
}

export default Button
