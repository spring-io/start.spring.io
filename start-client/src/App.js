import './styles/app.scss'

import React from 'react'
import { ToastContainer } from 'react-toastify'
import { render } from 'react-dom'

import Application from './components/Application'
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
      <Application />
    </InitializrProvider>
  </AppProvider>,
  document.getElementById('app')
)
