import get from 'lodash/get'
import React, { useContext, useEffect, useRef, useState } from 'react'
import { CSSTransition, TransitionGroup } from 'react-transition-group'
import { clearAllBodyScrollLocks, disableBodyScroll } from 'body-scroll-lock'

import Header from './Header'
import { AppContext } from '../../reducer/App'
import { IconGithub, IconHistory } from '../icons'

function SideLeft() {
  const [isOpen, setIsOpen] = useState(false)
  const [lock, setLock] = useState(false)
  const wrapper = useRef(null)

  const { nav, histories, dispatch } = useContext(AppContext)

  useEffect(() => {
    if (get(wrapper, 'current') && nav) {
      disableBodyScroll(get(wrapper, 'current'))
    }
    return () => {
      clearAllBodyScrollLocks()
    }
  }, [wrapper, isOpen, nav])

  const onEnter = () => {
    setLock(true)
    setTimeout(() => {
      setIsOpen(true)
    }, 350)
  }
  const onEntered = () => {
    setLock(false)
  }

  const onEnded = () => {
    setLock(true)
    setIsOpen(false)
  }
  const onExited = () => {
    setLock(false)
  }
  return (
    <>
      <div id='side-left' className={isOpen ? 'is-open' : ''}>
        <div className='side-container'>
          <div className='navigation-action'>
            <button
              className={`hamburger hamburger--spin ${nav ? 'is-active' : ''}`}
              type='button'
              aria-label='Menu'
              aria-controls='navigation'
              onClick={() => {
                if (lock) {
                  return
                }
                dispatch({ type: 'UPDATE', payload: { nav: !nav } })
              }}
            >
              <span className='hamburger-box' tabIndex='-1'>
                <span className='hamburger-inner' />
              </span>
            </button>
            {!isOpen && !lock && histories.length > 0 && (
              <>
                <div className='navigation-divider' />
                <button
                  type='button'
                  aria-label='Menu'
                  aria-controls='navigation'
                  className='navigation-item'
                  onClick={() => {
                    dispatch({ type: 'UPDATE', payload: { history: true } })
                  }}
                >
                  <IconHistory />
                </button>
              </>
            )}
          </div>
          <div className='social'>
            <a
              rel='noreferrer noopener'
              target='_blank'
              href='https://github.com/spring-io/start.spring.io'
            >
              <span className='a-content' tabIndex='-1'>
                <IconGithub />
              </span>
            </a>
          </div>
        </div>
      </div>
      <TransitionGroup component={null}>
        {nav && (
          <CSSTransition
            onEnter={onEnter}
            onEntered={onEntered}
            onExit={onEnded}
            onExited={onExited}
            classNames='navigation'
            timeout={500}
          >
            <div className='navigation' ref={wrapper}>
              <div className='navigation-content'>
                <div className='navigation-content-wrap'>
                  <Header />
                  <div>
                    <ul>
                      <li>
                        <a
                          id='ql-help-projects'
                          target='_blank'
                          rel='noopener noreferrer'
                          href='https://spring.io/projects'
                        >
                          Discover all the Spring projects
                        </a>
                      </li>
                      <li>
                        <a
                          id='ql-help-guides'
                          target='_blank'
                          rel='noopener noreferrer'
                          href='https://spring.io/guides'
                        >
                          Discover all the Spring guides
                        </a>
                      </li>
                      <li>
                        <a
                          id='ql-help-spring-blog'
                          target='_blank'
                          rel='noopener noreferrer'
                          href='https://spring.io/blog'
                        >
                          What&apos;s new with Spring
                        </a>
                      </li>
                      <li>
                        <a
                          id='ql-help-migration'
                          target='_blank'
                          rel='noopener noreferrer'
                          href='https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide'
                        >
                          Migrate Spring Boot 2.7 to 3.0
                        </a>
                      </li>
                    </ul>
                  </div>
                  <div className='is-mobile'>
                    <ul>
                      <li>
                        <a
                          rel='noreferrer noopener'
                          target='_blank'
                          href='https://github.com/spring-io/start.spring.io'
                        >
                          <span className='a-content' tabIndex='-1'>
                            Github
                          </span>
                        </a>
                      </li>
                    </ul>
                  </div>
                  <div className='copyright'>
                    © 2005-{new Date().getFullYear()} Broadcom. All Rights
                    Reserved.
                    <br />
                    The term &quot;Broadcom&quot; refers to Broadcom Inc. and/or
                    its subsidiaries
                    <br />
                    start.spring.io is powered by{' '}
                    <span>
                      <a
                        target='_blank'
                        rel='noopener noreferrer'
                        href='https://github.com/spring-io/initializr/'
                      >
                        Spring Initializr
                      </a>
                    </span>
                    <span>,</span>{' '}
                    <span>
                      <a
                        target='_blank'
                        rel='noopener noreferrer'
                        href='https://buildpacks.io/'
                      >
                        Cloud Native Buildpacks
                      </a>
                    </span>{' '}
                    <span>and</span>{' '}
                    <span>
                      <a
                        target='_blank'
                        rel='noopener noreferrer'
                        href='https://azure.microsoft.com/en-us/products/spring-apps'
                      >
                        Azure Spring Apps
                      </a>
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </CSSTransition>
        )}
      </TransitionGroup>
    </>
  )
}

export default SideLeft
