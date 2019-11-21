import PropTypes from 'prop-types'
import React from 'react'

import { IconTimes } from '../icons'
import { Placeholder } from '../form'

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
          <div className='content'>
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
          <div className='foot'>
            <Placeholder type='text' width='120px' />
          </div>
        </div>
        <div className='right'>
          <>
            <div className='head'>
              <Placeholder width='86px' type='text' />
              <div className='actions'>
                <Placeholder width='74px' type='text' />
                <Placeholder width='43px' type='text' />
              </div>
              <a
                href='/#'
                onClick={e => {
                  e.preventDefault()
                  onClose()
                }}
                className='close'
              >
                <IconTimes />
              </a>
            </div>
            <div className='content' />
          </>
        </div>
      </div>
    </>
  )
}

Loading.propTypes = {
  onClose: PropTypes.func.isRequired,
}

export default Loading
