import PropTypes from 'prop-types'
import React from 'react'
import get from 'lodash.get'

import { IconCaretDown, IconFile, IconFolder } from '../icons'

class FileTree extends React.Component {
  constructor(props) {
    super(props)
    const tree = this.props.tree

    this.state = {
      tree,
      folders: this.treeToArray(tree.children),
    }
  }

  treeToArray = tree => {
    const recursive = (tree, acc) => {
      tree.forEach(item => {
        if (item.type === 'folder') {
          acc.push({
            filename: get(item, 'filename'),
            path: get(item, 'path'),
            hidden: get(item, 'hidden', false),
          })
          if (get(item, 'children')) {
            recursive(item.children, acc)
          }
        }
      })
      return acc
    }
    return recursive(tree, [])
  }

  toggleFolder = item => {
    const folders = get(this.state, 'folders')
    const folder = folders.find(folder => folder.path === item.path)
    folder.hidden = !get(folder, `hidden`, false)
    this.setState({ folders: folders })
  }

  isItemDisabled = item => {
    if (get(item, 'language') === null) {
      return true
    }
    return false
  }

  renderItem = (item, depth = 0) => {
    if (item.type === 'folder') {
      const f = get(this.state, 'folders').find(
        folder => folder.path === item.path
      )
      const isHidden = get(f, `hidden`, false)
      return (
        <li
          key={`li${item.path}`}
          className={`li-folder ${isHidden ? 'folder-hide' : ''}`}
        >
          <a
            href='/#'
            key={`s1${item.path}`}
            className={`folder level-${depth}`}
            onClick={e => {
              e.preventDefault()
              this.toggleFolder(item)
            }}
          >
            <span key={`s2${item.path}`} className='text'>
              {get(item, 'children.length', 0) > 0 && <IconCaretDown />}
              <span key={`s3${item.path}`} className='icon'>
                <IconFolder key={`s4${item.path}`} />
              </span>
              {item.filename}
            </span>
          </a>
          {get(item, 'children') && (
            <ul className='ul' key={`ul${item.path}`}>
              {item.children.map(it => this.renderItem(it, depth + 1))}
            </ul>
          )}
        </li>
      )
    } else {
      // File
      const isItemDisabled = this.isItemDisabled(item)
      const selected = get(this.props.selected, 'path') === get(item, 'path')
      return (
        <li key={`li${item.path}`} className='li-file'>
          <a
            href='/#'
            key={`s1${item.path}`}
            tabIndex={`${isItemDisabled ? -1 : ''}`}
            className={`file level-${depth} ${
              isItemDisabled ? 'disabled' : ''
            } ${selected ? 'selected' : ''}`}
            onClick={e => {
              e.preventDefault()
              this.clickItem(item, isItemDisabled)
            }}
          >
            <span key={`s2${item.path}`} className={`text`}>
              <span key={`s3${item.path}`} className='icon'>
                <IconFile key={`s4${item.path}`} />
              </span>
              {item.filename}
            </span>
          </a>
        </li>
      )
    }
  }

  clickItem = (item, disabled) => {
    if (disabled) {
      return
    }
    this.props.onClickItem(item)
  }

  render() {
    return (
      <ul className='explorer-ul'>
        {this.state.tree.children.map(item => this.renderItem(item, 0))}
      </ul>
    )
  }
}

FileTree.propTypes = {
  tree: PropTypes.object.isRequired,
  selected: PropTypes.object,
  onClickItem: PropTypes.func.isRequired,
}

export default FileTree
