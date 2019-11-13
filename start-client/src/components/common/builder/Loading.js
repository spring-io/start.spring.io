import React from 'react'

import Control from './Control'
import { Footer } from '../layout'
import { Placeholder } from '../form'

export default function Loading() {
  return (
    <>
      <Control text='Project'>
        <Placeholder type='radios' count={2} width='133px' />
      </Control>
      <Control text='Language'>
        <Placeholder type='radios' count={3} width='73px' />
      </Control>
      <Control text='Spring Boot'>
        <Placeholder type='radios' count={5} width='105px' />
      </Control>
      <Control text='Project Metadata' special='md'>
        <div>
          <div className='control'>
            <Placeholder type='input' />
          </div>
          <div className='control'>
            <Placeholder type='input' />
          </div>
          <div className='control'>
            <Placeholder type='dropdown' />
          </div>
        </div>
      </Control>
      <Control text='Dependencies'>
        <Placeholder type='tabs' count={2} />
      </Control>
      <Footer>
        <Placeholder type='button' width='189px' />
        <Placeholder type='button' width='212px' />
        <Placeholder type='button' width='110px' />
      </Footer>
    </>
  )
}
