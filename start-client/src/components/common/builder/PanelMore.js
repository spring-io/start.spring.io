import PropTypes from 'prop-types'
import get from 'lodash.get'
import React, { useContext, useEffect } from 'react'

import { AppContext } from '../../reducer/App'
import { IconChevronRight } from '../icons'

const PanelMore = ({ children, fieldFocusOnOpen }) => {
  const { more, dispatch } = useContext(AppContext)

  useEffect(() => {
    if (more && fieldFocusOnOpen) {
      setTimeout(() => {
        get(fieldFocusOnOpen, 'current').focus()
      }, 300)
    }
  }, [more, fieldFocusOnOpen])

  return (
    <div>
      <div className='more'>
        <div className='wrap'>
          <a
            href='/'
            onClick={event => {
              event.preventDefault()
              dispatch({
                type: 'UPDATE',
                payload: { more: !more },
              })
            }}
            className={more ? 'toggle' : ''}
          >
            <IconChevronRight />
            {!more ? 'Options' : 'Options'}
          </a>
        </div>
      </div>
      <div className={`panel ${more ? 'panel-active' : ''}`}>
        <div className='panel-wrap'>{children}</div>
      </div>
    </div>
  )
}

PanelMore.defaultProps = {
  children: null,
  fieldFocusOnOpen: null,
}

PanelMore.propTypes = {
  children: PropTypes.node,
  fieldFocusOnOpen: PropTypes.oneOfType([
    PropTypes.func,
    PropTypes.shape({ current: PropTypes.instanceOf(Element) }),
  ]),
}

export default PanelMore
