import PropTypes from 'prop-types'
import React from 'react'

function Button({
  id,
  onClick,
  children,
  variant,
  className,
  hotkey,
  refButton,
  disabled,
}) {
  return (
    <button
      className={`button ${className} ${
        variant === 'primary' ? 'primary' : ''
      }`}
      type='button'
      ref={refButton}
      id={id}
      disabled={disabled}
      onClick={event => {
        if (onClick) {
          onClick(event)
        }
      }}
    >
      <span className='button-content' tabIndex='-1'>
        <span>{children}</span>
        {hotkey && <span className='secondary desktop-only'>{hotkey}</span>}
      </span>
    </button>
  )
}

Button.defaultProps = {
  onClick: null,
  children: null,
  variant: '',
  hotkey: '',
  className: '',
  refButton: null,
  disabled: false,
}

Button.propTypes = {
  id: PropTypes.string.isRequired,
  variant: PropTypes.string,
  className: PropTypes.string,
  hotkey: PropTypes.string,
  onClick: PropTypes.func,
  children: PropTypes.node,
  disabled: PropTypes.bool,
  refButton: PropTypes.oneOfType([
    PropTypes.func,
    PropTypes.shape({ current: PropTypes.instanceOf(Element) }),
  ]),
}

export default Button
