import get from 'lodash.get'
import React, { useContext, useEffect, useRef, useState } from 'react'
import { CSSTransition, TransitionGroup } from 'react-transition-group'

import { AppContext } from '../../reducer/App'
import { IconCaretDown, IconGithub, IconSpring, IconTwitter } from '../icons'
import { Switch } from '../form'

const QuickLinks = () => {
  const { theme, dispatch } = useContext(AppContext)
  const [help, setHelp] = useState(false)
  const wrapper = useRef(null)
  const toggleTheme = () => {
    const newTheme = theme === 'dark' ? 'light' : 'dark'
    dispatch({
      type: 'UPDATE',
      payload: {
        theme: newTheme,
      },
    })
  }
  useEffect(() => {
    const clickOutside = event => {
      const children = get(wrapper, 'current')
      if (children && !children.contains(event.target)) {
        setHelp(false)
      }
    }
    document.addEventListener('mousedown', clickOutside)
    return () => {
      document.removeEventListener('mousedown', clickOutside)
    }
  }, [setHelp])

  return (
    <ul className='quick-links'>
      <li>
        <span className='switch-mode'>
          <Switch isOn={theme === 'dark'} onChange={toggleTheme} />
          {theme === 'dark' ? 'Dark' : 'Light'} UI
        </span>
      </li>
      <li>
        <a
          href='https://github.com/spring-io/start.spring.io'
          rel='noreferrer noopener'
          target='_blank'
          tabIndex='-1'
        >
          <IconGithub />
          Github
        </a>
      </li>
      <li>
        <a
          href='https://twitter.com/springboot'
          rel='noreferrer noopener'
          target='_blank'
          tabIndex='-1'
        >
          <IconTwitter />
          Twitter
        </a>
      </li>
      <li>
        <a
          href='/'
          className='dropdown'
          tabIndex='-1'
          onClick={e => {
            e.preventDefault()
            setHelp(!help)
          }}
          ref={wrapper}
        >
          <IconSpring />
          Help
          <IconCaretDown className='caret' />
        </a>

        <TransitionGroup component={null}>
          {help && (
            <CSSTransition classNames='nav-anim' timeout={500}>
              <ul className='dropdown-menu'>
                <li>
                  <a
                    id='ql-help-projects'
                    target='_blank'
                    rel='noopener noreferrer'
                    href='https://spring.io/projects'
                    tabIndex='-1'
                  >
                    Spring Projects
                  </a>
                </li>
                <li>
                  <a
                    id='ql-help-guides'
                    target='_blank'
                    rel='noopener noreferrer'
                    tabIndex='-1'
                    href='https://spring.io/guides'
                  >
                    Spring Guides
                  </a>
                </li>
                <li>
                  <a
                    id='ql-help-spring-blog'
                    target='_blank'
                    rel='noopener noreferrer'
                    tabIndex='-1'
                    href='https://spring.io/blog'
                  >
                    What&apos;s New With Spring
                  </a>
                </li>
                <li>
                  <a
                    id='ql-help-migration'
                    target='_blank'
                    rel='noopener noreferrer'
                    tabIndex='-1'
                    href='https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.0-Migration-Guide'
                  >
                    Migrate from 1.5 =&gt; 2.0
                  </a>
                </li>
              </ul>
            </CSSTransition>
          )}
        </TransitionGroup>
      </li>
    </ul>
  )
}

export default QuickLinks
