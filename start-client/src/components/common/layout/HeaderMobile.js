import React, { useContext } from 'react'

import LogoMobile from './LogoMobile'
import { AppContext } from '../../reducer/App'
import { IconMoon, IconSun } from '../icons'

function HeaderMobile() {
  const { nav, theme, dispatch } = useContext(AppContext)
  return (
    <div className='is-mobile header-mobile'>
      <h1 className='logo logo-mobile'>
        <a href='/'>
          <span className='logo-content' tabIndex='-1'>
            <LogoMobile />
          </span>
        </a>
      </h1>
      <div className='switch-mobile'>
        <button
          type='button'
          aria-label='Switch theme'
          aria-controls='navigation'
          onClick={e => {
            e.preventDefault()
            dispatch({
              type: 'UPDATE',
              payload: {
                theme: theme === 'dark' ? 'light' : 'dark',
              },
            })
          }}
        >
          {theme === 'dark' ? <IconSun /> : <IconMoon />}
        </button>
      </div>
      <button
        className={`hamburger hamburger--spin ${nav ? 'is-active' : ''}`}
        type='button'
        aria-label='Menu'
        aria-controls='navigation'
        onClick={() => {
          dispatch({ type: 'UPDATE', payload: { nav: !nav } })
        }}
      >
        <span className='hamburger-box' tabIndex='-1'>
          <span className='hamburger-inner' />
        </span>
      </button>
    </div>
  )
}

export default HeaderMobile
