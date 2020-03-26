import PropTypes from 'prop-types'
import get from 'lodash.get'
import React, { useEffect, useState } from 'react'

import { IconCaretDown, IconFile, IconFolder } from '../icons'

function Tree({ tree, selected, onClickItem }) {
  const [folders, setFolders] = useState([])

  useEffect(() => {
    const treeToArray = map => {
      const recursive = (mapRec, acc) => {
        mapRec.forEach(item => {
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
      return recursive(map, [])
    }
    setFolders(treeToArray(tree.children))
  }, [tree, setFolders])

  const renderItem = (item, depth = 0) => {
    if (item.type === 'folder') {
      const folder = folders.find(f => f.path === item.path)
      const isHidden = get(folder, `hidden`, true)
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
              const newFolders = [...folders]
              const newFolder = newFolders.find(f => f.path === item.path)
              if (newFolder) {
                newFolder.hidden = !get(newFolder, `hidden`, true)
                setFolders(newFolders)
              }
            }}
          >
            <span className='item-content' tabIndex='-1'>
              <span key={`s2${item.path}`} className='text'>
                {get(item, 'children.length', 0) > 0 && <IconCaretDown />}
                <span key={`s3${item.path}`} className='icon'>
                  <IconFolder key={`s4${item.path}`} />
                </span>
                {item.filename}
              </span>
            </span>
          </a>
          {get(item, 'children') && (
            <ul className='ul' key={`ul${item.path}`}>
              {item.children.map(it => renderItem(it, depth + 1))}
            </ul>
          )}
        </li>
      )
    }
    // File
    const isDisabled = get(item, 'language') === null
    const isSelected = get(selected, 'path') === get(item, 'path')
    return (
      <li key={`li${item.path}`} className='li-file'>
        <a
          href='/#'
          key={`s1${item.path}`}
          tabIndex={`${isDisabled ? -1 : ''}`}
          className={`file level-${depth} ${isDisabled ? 'disabled' : ''} ${
            isSelected ? 'selected' : ''
          }`}
          onClick={e => {
            e.preventDefault()
            if (!isDisabled) {
              onClickItem(item)
            }
          }}
        >
          <span className='item-content' tabIndex='-1'>
            <span key={`s2${item.path}`} className='text'>
              <span key={`s3${item.path}`} className='icon'>
                <IconFile key={`s4${item.path}`} />
              </span>
              {item.filename}
            </span>
          </span>
        </a>
      </li>
    )
  }

  return (
    <ul className='explorer-ul'>
      {tree.children.map(item => renderItem(item, 0))}
    </ul>
  )
}

Tree.propTypes = {
  tree: PropTypes.shape({
    children: PropTypes.arrayOf(
      PropTypes.shape({
        type: PropTypes.string,
      })
    ),
  }).isRequired,
  selected: PropTypes.shape({
    path: PropTypes.string.isRequired,
  }).isRequired,
  onClickItem: PropTypes.func.isRequired,
}

export default Tree
