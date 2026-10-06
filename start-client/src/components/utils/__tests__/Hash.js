import React from 'react'
import { act, create } from 'react-test-renderer'

import { AppContext } from '../../reducer/App'
import { InitializrContext } from '../../reducer/Initializr'
import useHash, { parseHash } from '../Hash'

function UseHash() {
  useHash()
  return null
}

describe('parseHash', () => {
  it('strips #! prefix', () => {
    expect(parseHash('#!type=gradle-project&language=kotlin')).toEqual({
      type: 'gradle-project',
      language: 'kotlin',
    })
  })

  it('strips # prefix', () => {
    expect(parseHash('#type=gradle-project&language=kotlin')).toEqual({
      type: 'gradle-project',
      language: 'kotlin',
    })
  })
})

describe('useHash', () => {
  it('replaces a share hash instead of pushing history', () => {
    const pushState = jest.fn()
    const replaceState = jest.fn()
    global.window = {
      location: { hash: '#!type=maven-project', pathname: '/' },
      history: { pushState, replaceState },
      addEventListener: jest.fn(),
      removeEventListener: jest.fn(),
    }

    let renderer
    act(() => {
      renderer = create(
        <AppContext.Provider
          value={{
            complete: true,
            config: { lists: {} },
            dispatch: jest.fn(),
          }}
        >
          <InitializrContext.Provider value={{ dispatch: jest.fn() }}>
            <UseHash />
          </InitializrContext.Provider>
        </AppContext.Provider>
      )
    })

    expect(replaceState).toHaveBeenCalledWith(
      null,
      null,
      window.location.pathname
    )
    expect(pushState).not.toHaveBeenCalled()

    act(() => {
      renderer.unmount()
    })
    delete global.window
  })
})
