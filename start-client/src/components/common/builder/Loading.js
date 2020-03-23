import React from 'react'

import Actions from './Actions'
import Control from './Control'
import Placeholder from './Placeholder'

export default function Loading() {
  return (
    <>
      <div className='colset colset-main'>
        <div className='left'>
          <div className='colset'>
            <div className='left'>
              <Control text='Project'>
                <Placeholder type='radio' width='97px' />
                <Placeholder type='radio' width='98px' />
              </Control>
            </div>
            <div className='right'>
              <Control text='Language'>
                <Placeholder type='radio' width='30px' />
                <Placeholder type='radio' width='36px' />
                <Placeholder type='radio' width='40px' />
              </Control>
            </div>
          </div>
          <Control text='Spring Boot'>
            <Placeholder type='radio' width='100px' />
            <Placeholder type='radio' width='98px' />
            <Placeholder type='radio' width='98px' />
            <Placeholder type='radio' width='120px' />
            <Placeholder type='radio' width='140px' />
            <Placeholder type='radio' width='98px' />
          </Control>
          <Control text='Project Metadata'>
            <div>
              <div className='control control-inline control-placeholder'>
                <span className='placeholder-label'>Group</span>
                <Placeholder type='input' />
              </div>
              <div className='control control-inline control-placeholder'>
                <span className='placeholder-label'>Artifact</span>
                <Placeholder type='input' />
              </div>
              <div className='control control-inline control-placeholder'>
                <span className='placeholder-label'>Name</span>
                <Placeholder type='input' />
              </div>
              <div className='control control-inline control-placeholder'>
                <span className='placeholder-label'>Description</span>
                <Placeholder type='input' />
              </div>
              <div className='control control-inline control-placeholder'>
                <span className='placeholder-label'>Package name</span>
                <Placeholder type='input' />
              </div>
              <div
                className='control control-inline control-placeholder'
                style={{ height: 30 }}
              >
                <span className='placeholder-label' style={{ marginRight: 20 }}>
                  Packaging
                </span>
                <Placeholder type='radio' width='20px' />
                <Placeholder type='radio' width='20px' />
              </div>
              <div
                className='control control-inline control-placeholder'
                style={{ height: 30 }}
              >
                <span className='placeholder-label' style={{ marginRight: 20 }}>
                  Java
                </span>
                <Placeholder type='radio' width='12px' />
                <Placeholder type='radio' width='12px' />
                <Placeholder type='radio' width='12px' />
              </div>
            </div>
          </Control>
        </div>
        <div className='right'>
          <div className='control'>
            <div className='dependency-header'>
              <span className='label'>Dependencies</span>
              <Placeholder className='placeholder-button-dep' type='button' />
            </div>
          </div>
        </div>
      </div>
      <Actions>
        <Placeholder className='placeholder-button-submit' type='button' />
        <Placeholder className='placeholder-button-explore' type='button' />
        <Placeholder className='placeholder-button-share' type='button' />
      </Actions>
    </>
  )
}
