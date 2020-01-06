import PropTypes from 'prop-types'
import React from 'react'

function Button({ id, onClick, children, variant, hotkey }) {
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
}

Button.propTypes = {
  id: PropTypes.string.isRequired,
  variant: PropTypes.string,
  hotkey: PropTypes.string,
  onClick: PropTypes.func,
  children: PropTypes.node,
}

export default Button
