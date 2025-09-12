import PropTypes from 'prop-types'
import React from 'react'

import { Placeholder } from '../builder'

function Loading({ onClose }) {
  return (
    <>
      <a
        href='/#'
        onClick={e => {
          e.preventDefault()
          onClose()
        }}
      >
        {' '}
      </a>
      <div className='colset-explorer'>
        <div className='left'>
          <div className='head'>
            <Placeholder width='70px' type='text' />
          </div>
          <div className='explorer-content'>
            <ul className='explorer-ul-placeholder'>
              <li>
                <Placeholder type='text' width='66px' />
              </li>
              <li>
                <Placeholder type='text' width='60px' />
              </li>
              <li>
                <Placeholder type='text' width='45px' />
              </li>
              <li>
                <Placeholder type='text' width='87px' />
              </li>
              <li>
                <Placeholder type='text' width='80px' />
              </li>
              <li>
                <Placeholder type='text' width='94px' />
              </li>
              <li>
                <Placeholder type='text' width='86px' />
              </li>
            </ul>
          </div>
        </div>
        <div className='right'>
          <div className='head'>
            <div className='actions-file'>
              <Placeholder width='120px' type='button' />
              <Placeholder width='78px' type='button' />
            </div>
          </div>

          <div className='is-mobile explorer-select placeholder-explorer-select'>
            <div className='placeholder-select' />
          </div>
          <div className='explorer-content' />
        </div>

        <div className='explorer-actions'>
          <Placeholder className='placeholder-button-download' type='button' />
          <a
            href='/#'
            onClick={e => {
              e.preventDefault()
              onClose()
            }}
            className='button'
          >
            <span className='button-content'>
              <span>Close</span>
              <span className='secondary desktop-only'>ESC</span>
            </span>
          </a>
        </div>
      </div>
    </>
  )
}

Loading.propTypes = {
  onClose: PropTypes.func.isRequired,
}

export default Loading
