import PropTypes from 'prop-types'
import React from 'react'
import get from 'lodash.get'
import { CSSTransition, TransitionGroup } from 'react-transition-group'
import { CopyToClipboard } from 'react-copy-to-clipboard'

import { IconTimes } from './../icons'
import { getShareUrl } from '../../utils/api'

class SharePopup extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      button: 'Copy',
    }

    this.setWrapperRef = this.setWrapperRef.bind(this)
    this.handleClickOutside = this.handleClickOutside.bind(this)
  }

  componentDidMount = () => {
    document.addEventListener('mousedown', this.handleClickOutside)
    this.setState({ path: window.location.origin })
  }

  componentWillUnmount = () => {
    document.removeEventListener('mousedown', this.handleClickOutside)
  }

  setWrapperRef = node => {
    this.wrapperRef = node
  }

  handleClickOutside = event => {
    if (this.wrapperRef && !this.wrapperRef.contains(event.target)) {
      this.setOnClose()
    }
  }

  setOnClose = () => {
    this.props.onClose()
  }

  getUrl = () => {
    const rootValues = this.props.rootValues
    const values = this.props.properties
    return getShareUrl(get(this.state, 'path', ''), values, rootValues)
  }

  onCopy = () => {
    this.setState({ button: 'Copied!' })
    this.inputShare && this.inputShare.focus()
    setTimeout(() => {
      this.props.onClose()
    }, 1000)
  }

  onFocus = event => event.target.select()

  onEnter = event => {
    this.setState({ button: 'Copy' })
    this.inputShare && this.inputShare.focus()
  }

  render = () => {
    const urlToShare = this.getUrl()
    return (
      <TransitionGroup component={null}>
        {this.props.open && (
          <CSSTransition
            onEnter={this.onEnter}
            classNames='popup'
            timeout={500}
          >
            <div className='popup-share'>
              <div ref={this.setWrapperRef}>
                <div className='popup-header'>
                  <h1>Share your configuration</h1>
                  <a
                    href='/#'
                    onClick={e => {
                      e.preventDefault()
                      this.setOnClose()
                    }}
                    className='close'
                  >
                    <IconTimes />
                  </a>
                </div>
                <div className='popup-content'>
                  <label htmlFor='input-share'>
                    Use this link to share the current configuration. Attributes
                    can be removed from the URL if you want to rely on our
                    defaults.
                  </label>
                  <div className='control'>
                    <input
                      onFocus={this.onFocus}
                      id='input-share'
                      className={`control-input ${
                        this.state.button === 'Copied!' ? 'padding-lg' : ''
                      }`}
                      readOnly
                      value={urlToShare}
                      ref={input => {
                        this.inputShare = input
                      }}
                    />
                    <CopyToClipboard onCopy={this.onCopy} text={urlToShare}>
                      <a
                        href='/#'
                        onClick={e => {
                          e.preventDefault()
                        }}
                        className='link'
                        ref={link => {
                          this.linkCopy = link
                        }}
                      >
                        {this.state.button}
                      </a>
                    </CopyToClipboard>
                  </div>
                </div>
              </div>
            </div>
          </CSSTransition>
        )}
      </TransitionGroup>
    )
  }
}

SharePopup.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  properties: PropTypes.object.isRequired,
  rootValues: PropTypes.object.isRequired,
}

export default SharePopup
