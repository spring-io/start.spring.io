import PropTypes from 'prop-types'
import React from 'react'

import { IconTimes } from '../icons'
import { getWarningText } from '../../utils/api'

class Warnings extends React.Component {
  render = () => {
    const warnings = getWarningText(
      this.props.values,
      this.props.defaultValues,
      this.props.lists
    )
      .sort((a, b) => (a.order > b.order ? 1 : -1))
      .map(item => item.text)

    if (warnings.length > 0) {
      return (
        <div className='colset'>
          <div className='left'></div>
          <div className='right'>
            <div className='warnings'>
              <a
                className='close'
                href='/#'
                onClick={event => {
                  event.preventDefault()
                  this.props.hide()
                }}
              >
                <IconTimes />
              </a>
              The following attributes could not be handled:
              <ul>
                {warnings.map((warning, index) => (
                  <li
                    key={index}
                    dangerouslySetInnerHTML={{ __html: warning }}
                  ></li>
                ))}
              </ul>
            </div>
          </div>
        </div>
      )
    }
    return <></>
  }
}

Warnings.propTypes = {
  values: PropTypes.object,
  lists: PropTypes.object,
  defaultValues: PropTypes.object,
  hide: PropTypes.func.isRequired,
}

export default Warnings
