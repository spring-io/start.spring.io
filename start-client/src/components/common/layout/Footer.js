import React from 'react'

const Footer = () => (
  <footer className='footer'>
    <div className='footer-container'>
      © 2013-{new Date().getFullYear()} Pivotal Software
      <br />
      Powered by{` `}
      <span>
        <a
          tabIndex='-1'
          target='_blank'
          rel='noopener noreferrer'
          href='https://github.com/spring-io/initializr/'
        >
          Spring Initializr
        </a>
      </span>
      <span>
        {` `}and{` `}
      </span>
      <span>
        <a
          tabIndex='-1'
          target='_blank'
          rel='noopener noreferrer'
          href='https://run.pivotal.io/'
        >
          PWS
        </a>
      </span>
      <br />
      <a
        tabIndex='-1'
        target='_blank'
        rel='noopener noreferrer'
        href='https://github.com/spring-io/start.spring.io/tree/master/start-client/src/components/halloween/readme.md'
      >
        Halloween Theme Credits
      </a>
    </div>
  </footer>
)

export default Footer
