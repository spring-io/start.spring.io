import './styles/app.scss'

import React from 'react'
import { ToastContainer } from 'react-toastify'
import { render } from 'react-dom'

import FiveXXBody from './components/FiveXXBody'
import Close from './components/common/form/Close'
import { AppProvider } from './components/reducer/App'
import { InitializrProvider } from './components/reducer/Initializr'

render(
  <AppProvider>
  <InitializrProvider>
    <ToastContainer
      closeButton={<Close />}
      position='top-center'
      hideProgressBar
    />
    <FiveXXBody />
  </InitializrProvider>
</AppProvider>,
  document.getElementById('5xx')
)
