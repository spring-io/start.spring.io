import React from 'react'
import renderer from 'react-test-renderer'

import Header from '../Header'
import Logo from '../Logo'

describe('Header', () => {
  it('has a className header', () => {
    const tree = renderer.create(<Header>Test</Header>).toJSON()
    expect(tree.props.className).toBe('header')
  })

  it('has a logo', () => {
    const tree = renderer.create(<Header>Test</Header>)
    expect(tree.root.findByType(Logo)).toBeTruthy()
  })

  it('displays a child', () => {
    const X = () => <div>Test</div>
    const tree = renderer.create(
      <Header>
        <X />
      </Header>
    )
    expect(tree.root.findByType(X)).toBeTruthy()
  })
})
