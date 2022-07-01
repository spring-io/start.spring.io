import React from 'react'

import HeaderMobile from './HeaderMobile'
import Logo from './Logo'

function Header() {
  return (
    <header id='header'>
      <div className='not-mobile'>
        <h1 className='logo'>
          <a href='/'>
            <span className='logo-content' tabIndex='-1'>
              <Logo />
            </span>
          </a>
        </h1>
      </div>
      <HeaderMobile />
    </header>
  )
}

export default Header
