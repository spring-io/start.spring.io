import PropTypes from 'prop-types'
import React from 'react'

function Form({ onSubmit, children }) {
  return (
    <form className='form' onSubmit={onSubmit} autoComplete='off'>
      <input
        style={{ display: 'none' }}
        type='text'
        name='fakeusernameremembered'
      />
      <input
        style={{ display: 'none' }}
        type='password'
        name='fakepasswordremembered'
      />
      {children}
    </form>
  )
}

Form.defaultProps = {
  children: null,
}

Form.propTypes = {
  onSubmit: PropTypes.func.isRequired,
  children: PropTypes.node,
}

export default Form
