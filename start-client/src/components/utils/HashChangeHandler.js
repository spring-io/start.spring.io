import { PureComponent } from 'react'

class HashChangeHandler extends PureComponent {
  handleHashChange = () => {
    this.props.onChange()
  }

  componentDidMount = () => {
    window.addEventListener('hashchange', this.handleHashChange)
  }

  componentWillUnmount = () => {
    window.removeEventListener('hashchange', this.handleHashChange)
  }

  render = () => {
    return null
  }
}

export default HashChangeHandler
