import PropTypes from 'prop-types'
import React from 'react'
import { GlobalHotKeys, configure } from 'react-hotkeys'

configure({
  ignoreTags: ['textarea'],
})

function HotKeys({ onSubmit, onExplore, onEscape, onDependency }) {
  return (
    <GlobalHotKeys
      keyMap={{
        SUBMIT: ['command+enter', 'ctrl+enter'],
        EXPLORE: ['ctrl+space'],
        DEPENDENCY: ['command+b', 'ctrl+b'],
        ESCAPE: ['esc'],
      }}
      handlers={{
        SUBMIT: onSubmit,
        EXPLORE: onExplore,
        ESCAPE: onEscape,
        DEPENDENCY: onDependency,
      }}
      global
    />
  )
}

HotKeys.propTypes = {
  onSubmit: PropTypes.func.isRequired,
  onExplore: PropTypes.func.isRequired,
  onEscape: PropTypes.func.isRequired,
  onDependency: PropTypes.func.isRequired,
}

export default HotKeys
