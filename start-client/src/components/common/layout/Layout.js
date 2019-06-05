import '../../../styles/app.scss'

import PropTypes from 'prop-types'
import React from 'react'

import Header from './Header'
import QuickLinks from './QuickLinks'

const Layout = ({ children }) => (
  <>
    <Header>
      <QuickLinks />
    </Header>
    <main>{children}</main>
  </>
)

Layout.propTypes = {
  children: PropTypes.node.isRequired,
}

export default Layout
