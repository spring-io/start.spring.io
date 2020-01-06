import PropTypes from 'prop-types'
import React from 'react'

import Logo from './Logo'

const Header = ({ children }) => (
  <div className='header'>
    <h1 className='logo'>
      <a href='/'>
        <Logo />
        <span className='title'>
          Spring <strong>Initializr</strong>
        </span>
        <span className='description'>Bootstrap your application</span>
      </a>
    </h1>
    {children}
  </div>
)

Header.defaultProps = {
  children: null,
}

Header.propTypes = {
  children: PropTypes.node,
}

export default Header
