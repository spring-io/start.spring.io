import * as JsSearch from 'js-search'
import PropTypes from 'prop-types'
import React from 'react'

import SearchResultsList from './SearchResultsList'
import { isInRange } from './../../utils/versions'

class Typehead extends React.Component {
  search = null

  constructor(props) {
    super(props)
    this.state = {
      search: '',
      selected: 0,
    }
    this.search = new JsSearch.Search('name')
    this.search.addIndex('name')
    this.search.addIndex('id')
    this.search.addIndex('description')
    this.search.addIndex('group')
    this.search.addDocuments(this.props.options)
  }

  onChange = event => {
    this.setState({
      search: event.target.value,
      selected: 0,
      focus: false,
    })
  }

  onKeyPress = (event, dependencies) => {
    switch (event.keyCode) {
      case 40: // Down
        event.preventDefault()
        this.setState({
          selected: Math.min(
            this.state.selected + 1,
            dependencies.length - 1,
            4
          ),
        })
        break
      case 38: // Up
        event.preventDefault()
        this.setState({ selected: Math.max(this.state.selected - 1, 0) })
        break
      case 13: // Enter
        event.preventDefault()
        if (dependencies.length > 0) {
          this.onAdded(dependencies[this.state.selected])
        } else {
          this.props.submit(event)
        }
        break
      case 27: // Escape
        event.preventDefault()
        this.setState({ search: '' })
        break
      case 39: // Right
      case 37: // Left
        break
      default:
        // Default
        this.setState({ selected: 0 })
    }
  }

  onAdded = item => {
    const valid = item.versionRange
      ? isInRange(this.props.boot, item.versionRange)
      : true
    if (valid) {
      this.props.add(item)
      this.setState({ search: '', selected: 0 })
      this.nameInput.focus()
    }
  }

  onFocus = () => {
    if (this.state.selected === null) {
      this.setState({
        focus: true,
        selected: 0,
      })
    } else {
      this.setState({
        focus: true,
      })
    }
  }

  onBlur = () => {
    this.setState({ selected: null, focus: false })
  }

  onSelectedChanged = index => {
    this.setState({ selected: index })
  }

  messageSearch = count => {
    if (count > 5) {
      return (
        <div className='search-more-warning'>
          <p>
            More than 5 results found.
            <br />
            Refine your search if necessary.
          </p>
        </div>
      )
    }
    if (count === 0 && this.state.search.trim() !== '') {
      return (
        <div className='search-no-result'>
          <p>No result.</p>
        </div>
      )
    }
  }

  isValid = dependency => {
    return dependency.versionRange
      ? isInRange(this.props.boot, dependency.versionRange)
      : true
  }

  sort = dependencies => {
    return dependencies.sort((a, b) => {
      if (this.isValid(a) && !this.isValid(b)) {
        return -1
      }
      if (!this.isValid(a) && this.isValid(b)) {
        return 1
      }
      return b.weight - a.weight
    })
  }

  render() {
    let dependencies = []
    let count = 0
    if (this.state.search) {
      dependencies = this.search
        .search(this.state.search)
        .filter(item => !this.props.exclude.find(o => o.name === item.name))

      count = dependencies.length
      if (dependencies && dependencies.length > 5) {
        dependencies = dependencies.slice(0, 5)
      }
      dependencies = this.sort(dependencies)
    }
    return (
      <>
        <input
          type='text'
          className='control-input'
          placeholder='Web, Security, JPA, Actuator, Devtools...'
          value={this.state.search}
          onFocus={this.onFocus}
          onBlur={this.onBlur}
          onChange={this.onChange}
          id='input-quicksearch'
          ref={input => {
            this.nameInput = input
          }}
          onKeyDown={e => {
            this.onKeyPress(e, dependencies)
          }}
        />
        <SearchResultsList
          boot={this.props.boot}
          dependencies={dependencies}
          onAdded={this.onAdded}
          selected={this.state.selected}
          onSelectedChanged={this.onSelectedChanged}
        />
        {this.messageSearch(count)}
      </>
    )
  }
}

Typehead.propTypes = {
  options: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired,
      group: PropTypes.string.isRequired,
      description: PropTypes.string.isRequired,
      versionRange: PropTypes.string,
      versionRequirement: PropTypes.string,
    })
  ),
  exclude: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired,
      group: PropTypes.string.isRequired,
      description: PropTypes.string.isRequired,
      versionRange: PropTypes.string,
      versionRequirement: PropTypes.string,
    })
  ),
  add: PropTypes.func.isRequired,
  submit: PropTypes.func.isRequired,
  boot: PropTypes.string.isRequired,
}

export default Typehead
