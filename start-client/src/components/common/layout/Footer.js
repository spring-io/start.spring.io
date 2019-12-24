import PropTypes from 'prop-types'
import React from 'react'

const Footer = ({ children }) => (
  <div className='sticky'>
    <div className='colset colset-submit'>
      <div className='left nopadding'>
        <footer className='footer'>
          <div className='footer-container'>
            © 2013-{new Date().getFullYear()} Pivotal Software
            <br />
            Powered by&nbsp;
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
            <span>&nbsp;and&nbsp;</span>
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
              href='https://github.com/spring-io/start.spring.io/tree/master/start-client/src/components/endofyear/readme.md'
            >
              End of Year Theme Credits
            </a>
          </div>
        </footer>
      </div>
      {children && (
        <div className='right nopadding'>
          <div className='submit'>{children}</div>
        </div>
      )}
    </div>
  </div>
)

Footer.defaultProps = {
  children: null,
}

Footer.propTypes = {
  children: PropTypes.node,
}

export default Footer
