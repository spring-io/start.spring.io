import React from 'react'

const Footer = () => (
  <footer className='footer'>
    <div className='footer-container'>
      © 2013-{new Date().getFullYear()} Pivotal Software
      <br />
      start.spring.io is powered by
      <br />
      <a
        tabIndex='-1'
        target='_blank'
        rel='noopener noreferrer'
        href='https://github.com/spring-io/initializr/'
      >
        Spring Initializr
      </a>
      {` `}and{` `}
      <a
        tabIndex='-1'
        target='_blank'
        rel='noopener noreferrer'
        href='https://run.pivotal.io/'
      >
        Pivotal Web Services
      </a>
    </div>
  </footer>
)

export default Footer
