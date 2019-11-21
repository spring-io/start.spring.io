import React from 'react'

import { Header } from '../layout'

class ErrorPage extends React.Component {
  render = () => {
    return (
      <div className='error-page'>
        <Header />
        <div className='text'>
          <p>The service is temporarily unavailable.</p>
          <p>
            <a href='/'>Refresh the page</a>
          </p>
        </div>
      </div>
    )
  }
}

export default ErrorPage
