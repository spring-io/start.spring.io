import PropTypes from 'prop-types'
import get from 'lodash.get'
import React, { useEffect, useState } from 'react'

function Select({ tree, selected, onClickItem }) {
  const [array, setArray] = useState([])

  useEffect(() => {
    const treeToArray = map => {
      const recursive = (mapRec, acc) => {
        mapRec.forEach(item => {
          if (item.type !== 'folder') {
            acc.push(item)
          } else if (get(item, 'children')) {
            recursive(item.children, acc)
          }
        })
        return acc
      }
      return recursive(map, [])
    }
    setArray(treeToArray(tree.children))
  }, [tree, setArray])

  const getPlaceholderPath = depth => {
    return depth > 0
      ? `${[...Array(depth).keys()].map(() => '..').join('/')}/`
      : ''
  }
  const renderItem = (item, depth = 0) => {
    if (item.type === 'folder') {
      return (
        <>
          <option value={item.path} key={item.path} disabled>
            {getPlaceholderPath(depth)}
            {item.filename}
          </option>
          {get(item, 'children') && (
            <>{item.children.map(it => renderItem(it, depth + 1))}</>
          )}
        </>
      )
    }
    const isDisabled = get(item, 'language') === null
    return (
      <option disabled={isDisabled} key={item.path} value={item.path}>
        {getPlaceholderPath(depth)}
        {item.filename}
      </option>
    )
  }
  return (
    <select
      value={selected.path}
      onChange={event => {
        const item = array.find(it => it.path === event.target.value)
        if (item) {
          onClickItem(item)
        }
      }}
    >
      {tree.children.map(item => renderItem(item, 0))}
    </select>
  )
}

Select.propTypes = {
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

export default Select
