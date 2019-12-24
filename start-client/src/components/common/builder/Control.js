import PropTypes from 'prop-types'
import React from 'react'

const Control = ({ text, children, variant }) => {
  if (variant === 'xl' || variant === 'xxl') {
    return (
      <div className='colset'>
        <div className='left'>
          <div className='sticky-label'>{text}</div>
        </div>
        <div
          className={`dependencies-box ${variant === 'xl' ? 'list' : 'large'}`}
        >
          {children}
        </div>
      </div>
    )
  }

  return (
    <div className='colset'>
      <div className='left'>
        <>{text}</>
      </div>
      <div className={`right ${variant === 'md' ? 'right-md' : ''}`}>
        {children}
      </div>
    </div>
  )
}

Control.defaultProps = {
  children: null,
  variant: '',
}

Control.propTypes = {
  children: PropTypes.node,
  variant: PropTypes.string,
  text: PropTypes.string.isRequired,
}

export default Control
