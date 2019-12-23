import get from 'lodash.get'
import React, { useContext } from 'react'

import ListGroup from './ListGroup'
import { AppContext } from '../../../reducer/App'
import { InitializrContext } from '../../../reducer/Initializr'

const List = () => {
  const { dependencies, dispatch, groupsClosed } = useContext(AppContext)
  const { values, dispatch: dispatchInitializr } = useContext(InitializrContext)
  return (
    <div className='groups'>
      {get(dependencies, 'groups').map(group => {
        return (
          <ListGroup
            group={group.group}
            key={group.group}
            dependencyGroup={group}
            add={id => {
              dispatchInitializr({
                type: 'ADD_DEPENDENCY',
                payload: { id },
              })
            }}
            remove={id => {
              dispatchInitializr({
                type: 'REMOVE_DEPENDENCY',
                payload: { id },
              })
            }}
            toggle={id => {
              dispatch({
                type: 'TOGGLE_GROUP',
                payload: { id },
              })
            }}
            itemsSelected={get(values, 'dependencies', [])}
            isClose={groupsClosed.indexOf(group.group) > -1}
            items={group.items}
          />
        )
      })}
    </div>
  )
}

export default List
