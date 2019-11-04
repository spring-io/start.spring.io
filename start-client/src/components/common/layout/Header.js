import PropTypes from 'prop-types'
import React from 'react'
import { Link } from 'gatsby'

import Logo from './Logo'

const Header = ({ children }) => (
  <div className='header'>
    <h1 className='logo'>
      <Link to='/'>
        <Logo />
        <span className='title'>
          Spring <strong>Initializr</strong>
        </span>
        <span className='description'>Bootstrap your application</span>
      </Link>
    </h1>
    {children}
  </div>
)

Header.propTypes = {
  children: PropTypes.node,
}

export default Header
