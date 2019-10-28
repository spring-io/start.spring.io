import React from 'react'
import { Linear, TweenMax } from 'gsap'

import Bat from './Bat'

class Bats extends React.Component {
  constructor(props) {
    super(props)
    this.bathCount = 40
    this.eBats = []
    this.eBats = Array.apply(null, { length: this.bathCount })
    this.tBats = Array.apply(null, { length: this.bathCount })
  }

  componentDidMount() {
    this.tBats = this.eBats.map((bat, i) => {
      const x = Math.random() * 4000 - 2000
      TweenMax.set(bat, { x, y: 8500, opacity: 1 })
      return new TweenMax(bat, 1.6, {
        bezier: {
          type: 'soft',
          values: [
            { x, y: 8500 },
            {
              x: Math.random() * 10000 - 4000,
              y: 4000 * Math.random() + 1000,
            },
          ],
        },
        rotation: Math.random() * 100 - 50,
        opacity: 0,
        ease: Linear.easeNone,
      })
    })
  }

  render() {
    const bats = this.eBats.map((o, i) => {
      return (
        <g
          id={`gbath-${i}`}
          key={`gbath-${i}`}
          ref={elmt => {
            this.eBats[i] = elmt
          }}
        >
          <Bat key={`bath-${i}`} />
        </g>
      )
    })

    return (
      <svg className='bats' viewBox='0 0 700 9000'>
        {bats}
      </svg>
    )
  }
}

export default Bats
