import React from 'react'
import { TimelineMax, TweenMax } from 'gsap'

class Bat extends React.Component {
  constructor(props) {
    super(props)
    this.eWingL = null
    this.eWingR = null
    this.tWingL = null
  }

  componentDidMount() {
    TweenMax.set(this.eWingL, {
      transformOrigin: 'right top',
    })

    TweenMax.set(this.eWingR, {
      transformOrigin: 'left top',
    })

    this.tWingL = new TimelineMax({ onComplete: this.complete })
      .to(this.eWingL, 0.2, {
        rotation: -25,
        y: 10,
      })
      .to(this.eWingL, 0.1, {
        rotation: 0,
        y: 0,
      })

    this.tWingR = new TimelineMax()
      .to(this.eWingR, 0.2, {
        rotation: 25,
        y: 10,
      })
      .to(this.eWingR, 0.1, {
        rotation: 0,
        y: 0,
      })
  }

  complete = () => {
    this.tWingL.play(0)
    this.tWingR.play(0)
  }

  render() {
    return (
      <g className='bat'>
        <path
          className='st0'
          d='M367.3,357.2c20.2,22.2,33.4,52.6,38.3,87.7l1.4,10.4c0.9,6.5,6.4,11.3,13,11.3c6.5,0,12.1-4.8,13-11.3
      		l1.4-10.4c4.9-35.1,18.1-65.5,38.3-87.7l9.8-213.1l0-52.6c0-5.3-3.2-10.1-8.1-12.1c-4.9-2-10.6-0.9-14.3,2.8l-21.5,21.5
      		c-10.3,10.3-27,10.3-37.3,0L380,82.2c-3.7-3.7-9.4-4.9-14.3-2.8c-5,2.1-8.1,6.8-8.1,12.1l0.1,52.6'
        />
        <path
          className='st0'
          ref={elmt => {
            this.eWingR = elmt
          }}
          d='M472.4,357.2c3.9-4.3,8-8.3,12.3-12.2c30.9-27.7,70.6-42.9,111.8-42.9c4.4,0,8.8,0.3,13.1,0.7
          		c5.1,0.4,9.9-2.1,12.4-6.5C659,232,725.1,191.4,798.9,187.5c4.2-0.2,8-2.5,10.3-6c2.3-3.5,2.7-7.9,1.2-11.9
          		c-9.8-25.4-24.6-48.1-44-67.4c-36.8-36.8-85.7-57-137.8-56.8c-2.4,0-4.8,0.7-6.9,2c-4.4,2.8-6.9,7.2-11,14.6
          		c-12.6,22.7-41.6,74.5-128.7,82.1L472.4,357.2z'
        />
        <path
          className='st0'
          ref={elmt => {
            this.eWingL = elmt
          }}
          d='M357.6,144.1c-87.1-7.6-116-59.4-128.7-82.1c-4.1-7.4-6.6-11.8-11-14.6c-2.1-1.3-4.5-2-6.9-2
      		c-52.1-0.2-101,20-137.8,56.8c-19.3,19.3-34.1,42-44,67.4c-1.5,3.9-1.1,8.3,1.2,11.9c2.3,3.5,6.1,5.7,10.3,6
      		c73.8,3.9,139.9,44.5,176.8,108.7c2.5,4.4,7.3,6.9,12.4,6.5c4.4-0.4,8.8-0.7,13.1-0.7c41.2,0,80.9,15.2,111.8,42.9
      		c4.3,3.9,8.4,8,12.3,12.2L357.6,144.1z'
        />
      </g>
    )
  }
}
export default Bat
