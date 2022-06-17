import React from 'react'

import { IconGithub, IconTwitter } from '../icons'

function Social() {
  return (
    <div className='social'>
      <a
        rel='noreferrer noopener'
        target='_blank'
        href='https://github.com/spring-io/start.spring.io'
      >
        <IconGithub />
      </a>
      <a
        rel='noreferrer noopener'
        target='_blank'
        href='https://twitter.com/springboot'
      >
        <IconTwitter />
      </a>
    </div>
  )
}

export default Social
