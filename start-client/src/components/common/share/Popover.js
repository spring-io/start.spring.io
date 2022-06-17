import PropTypes from 'prop-types'
import get from 'lodash.get'
import React, { useEffect, useRef, useState } from 'react'
import { CSSTransition, TransitionGroup } from 'react-transition-group'
import { CopyToClipboard } from 'react-copy-to-clipboard'
import { clearAllBodyScrollLocks, disableBodyScroll } from 'body-scroll-lock'

import useWindowsUtils from '../../utils/WindowsUtils'

function Popover({ shareUrl, open, onClose }) {
  const [button, setButton] = useState('Copy')
  const wrapper = useRef(null)
  const input = useRef(null)
  const link = useRef(null)
  const windowsUtils = useWindowsUtils()
  useEffect(() => {
    const clickOutside = event => {
      const children = get(wrapper, 'current')
      if (children && !children.contains(event.target)) {
        onClose()
      }
    }
    document.addEventListener('mousedown', clickOutside)
    if (get(input, 'current')) {
      get(input, 'current').focus()
    }
    return () => {
      document.removeEventListener('mousedown', clickOutside)
    }
  }, [onClose, input])

  const onEnter = () => {
    setButton('Copy')
  }

  useEffect(() => {
    if (get(wrapper, 'current') && open) {
      disableBodyScroll(get(wrapper, 'current'))
    }
    return () => {
      clearAllBodyScrollLocks()
    }
  }, [wrapper, open])

  const onCopy = () => {
    setButton('Copied!')
    input.current.focus()
    setTimeout(() => {
      onClose()
    }, 1000)
  }

  const urlToShare = `${windowsUtils.origin}/#!${shareUrl}`
  return (
    <TransitionGroup component={null}>
      {open && (
        <CSSTransition onEnter={onEnter} classNames='popup' timeout={300}>
          <div className='popup-share'>
            <div className='popop-share-container' ref={wrapper}>
              <div className='popup-header'>
                <h1>Share your configuration</h1>
              </div>
              <div className='popup-content'>
                {/* eslint-disable-next-line */}
                  <label htmlFor='input-share'>
                  Use this link to share the current configuration. Attributes
                  can be removed from the URL if you want to rely on our
                  defaults.
                </label>
                <div className='control'>
                  <input
                    onFocus={event => {
                      event.target.select()
                    }}
                    id='input-share'
                    className={`input ${
                      button === 'Copied!' ? 'padding-lg' : ''
                    }`}
                    onKeyDown={event => {
                      if (event.key === 'Escape') {
                        onClose()
                      }
                    }}
                    readOnly
                    value={urlToShare}
                    ref={input}
                  />
                  <CopyToClipboard onCopy={onCopy} text={urlToShare}>
                    <a
                      href='/#'
                      onClick={e => {
                        e.preventDefault()
                      }}
                      className='button'
                      ref={link}
                    >
                      <span className='button-content' tabIndex='-1'>
                        <span>{button}</span>
                      </span>
                    </a>
                  </CopyToClipboard>
                </div>
              </div>
              <div className='popup-action'>
                <a
                  href='/#'
                  onClick={e => {
                    e.preventDefault()
                    onClose()
                  }}
                  className='button'
                >
                  <span className='button-content' tabIndex='-1'>
                    <span>Close</span>
                    <span className='secondary desktop-only'>ESC</span>
                  </span>
                </a>
              </div>
            </div>
          </div>
        </CSSTransition>
      )}
    </TransitionGroup>
  )
}

Popover.propTypes = {
  shareUrl: PropTypes.string.isRequired,
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
}

export default Popover
