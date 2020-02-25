import PropTypes from 'prop-types'
import React from 'react'

function Placeholder({ type, count, width }) {
  if (type === 'radios') {
    const options = Array.from({ length: count }, (item, i) => {
      const id = `p${i}`
      return <span key={id} style={{ width }} className='placeholder-radio' />
    })
    return <span className='placeholder-radios'>{options}</span>
  }
  if (type === 'input') {
    return <span className='placeholder-input' />
  }
  if (type === 'dropdown') {
    return <span className='placeholder-dropdown' />
  }
  if (type === 'text') {
    return <span style={{ width }} className='placeholder-text' />
  }
  if (type === 'button') {
    return <span style={{ width }} className='placeholder-button' />
  }
  if (type === 'tabs') {
    const options = Array.from({ length: count }, (item, i) => {
      return <span key={`p${i}`} className='placeholder-tab' />
    })
    return (
      <span className='placeholder-tabs'>
        <span className='placeholder-header'>{options}</span>
      </span>
    )
  }
  return ''
}

Placeholder.defaultProps = {
  width: '150px',
  type: 'radios',
  count: 3,
}

Placeholder.propTypes = {
  type: PropTypes.string,
  count: PropTypes.number,
  width: PropTypes.string,
}

export default Placeholder
