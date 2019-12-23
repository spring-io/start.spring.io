import PropTypes from 'prop-types'
import React from 'react'
import { GlobalHotKeys } from 'react-hotkeys'

function HotKeys({ onSubmit, onExplore }) {
  return (
    <GlobalHotKeys
      keyMap={{
        SUBMIT: ['command+enter', 'ctrl+enter'],
        EXPLORE: ['ctrl+space'],
      }}
      handlers={{
        SUBMIT: onSubmit,
        EXPLORE: onExplore,
      }}
      global
    />
  )
}

HotKeys.propTypes = {
  onSubmit: PropTypes.func.isRequired,
  onExplore: PropTypes.func.isRequired,
}

export default HotKeys
