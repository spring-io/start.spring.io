import PropTypes from 'prop-types'
import React from 'react'

class Placeholder extends React.Component {
  render() {
    const width = this.props.width ? this.props.width : '150px'
    if (this.props.type === 'radios') {
      const options = Array.apply(null, { length: this.props.count }).map(
        (item, i) => {
          return (
            <span
              key={`p${i}`}
              style={{ width: width }}
              className='placeholder-radio'
            />
          )
        }
      )
      return <span className='placeholder-radios'>{options}</span>
    }
    if (this.props.type === 'input') {
      return <span className='placeholder-input' />
    }
    if (this.props.type === 'dropdown') {
      return <span className='placeholder-dropdown' />
    }
    if (this.props.type === 'button') {
      return <span style={{ width: width }} className='placeholder-button' />
    }
    if (this.props.type === 'tabs') {
      const options = Array.apply(null, { length: this.props.count }).map(
        (item, i) => {
          return <span key={`p${i}`} className='placeholder-tab' />
        }
      )
      return (
        <span className='placeholder-tabs'>
          <span className='placeholder-header'>{options}</span>
        </span>
      )
    }
    return ''
  }
}

Placeholder.propTypes = {
  type: PropTypes.string,
  count: PropTypes.number,
  width: PropTypes.string,
}

export default Placeholder
