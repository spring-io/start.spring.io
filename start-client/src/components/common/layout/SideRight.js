import React, { useContext } from 'react'

import { AppContext } from '../../reducer/App'
import { IconMoon, IconSun } from '../icons'

function SideRight() {
  const { dispatch, theme } = useContext(AppContext)

  return (
    <div id='side-right'>
      <div className='side-container'>
        <div className='switch'>
          <a
            href='/'
            onClick={e => {
              e.preventDefault()
              dispatch({
                type: 'UPDATE',
                payload: {
                  theme: 'light',
                },
              })
            }}
            className={`button inverse top ${
              theme === 'light' ? 'active' : ''
            }`}
          >
            <span className='button-content' tabIndex='-1'>
              <IconSun />
            </span>
          </a>
          <a
            href='/'
            onClick={e => {
              e.preventDefault()
              dispatch({
                type: 'UPDATE',
                payload: {
                  theme: 'dark',
                },
              })
            }}
            className={`button inverse ${theme === 'dark' ? 'active' : ''}`}
          >
            <span className='button-content' tabIndex='-1'>
              <IconMoon />
            </span>
          </a>
        </div>
      </div>
    </div>
  )
}

export default SideRight
