import React from 'react'
import { Power2, TimelineMax, TweenMax } from 'gsap'

class Spider extends React.Component {
  constructor(props) {
    super(props)
    this.eSpider = null
    this.eLine = null
    this.eAll = null

    this.timeline = null
  }

  componentDidMount() {
    TweenMax.set(this.eLine, {
      transformOrigin: 'center top',
    })
    TweenMax.set(this.eSpider, {
      x: '80',
      y: '-400',
    })
    const y1 = 1700
    new TimelineMax().to(this.eSpider, 1.6, { y: y1, ease: Power2.easeOut })
    new TimelineMax().to(this.eLine, 1.6, {
      attr: { points: `360,0 360,${y1 + 100}   ` },
      ease: Power2.easeOut,
    })
    TweenMax.from(this.eAll, 0.2, {
      opacity: 0,
    })

    this.timeline = new TimelineMax({
      onComplete: this.restartPlay,
    })

    this.timeline
      .to(this.eAll, 0.5, { rotation: 1.5 })
      .to(this.eAll, 0.5, { rotation: 0 })

    this.timeline.play()
  }

  restartPlay = () => {
    this.timeline.play(0)
  }

  render = () => (
    <svg x='0px' y='0px' viewBox='0 0 700 4000' className='spider'>
      <g
        ref={elmt => {
          this.eAll = elmt
        }}
      >
        <polyline
          ref={elmt => {
            this.eLine = elmt
          }}
          points='360,0 360,71  '
          className='st0'
        />

        <g
          ref={elmt => {
            this.eSpider = elmt
          }}
        >
          <path
            className='st1'
            d='M359.3,157c0,16.7-14.8,50.5-23,87.5c-15.2,68.1,4,146.5-63,146.5s-47.8-78.5-63-146.5c-8.2-36.9-23-70.8-23-87.5
  	c0-47.5,38.5-86,86-86S359.3,109.5,359.3,157z'
          />
          <g>
            <polyline className='st0' points='155,21 112,99 189,167.5 	' />
            <polyline className='st0' points='19,290 19,256 92,194 212,242 	' />
            <polyline
              className='st0'
              points='114,451 88,397 112,313 216.9,275.6 	'
            />
            <polyline
              className='st0'
              points='183,453 168,428 168,391 168,363 220.7,318.1 	'
            />
          </g>
          <g>
            <polyline className='st0' points='391.7,21 434.7,99 357.7,167.5 	' />
            <polyline
              className='st0'
              points='527.7,290 527.7,256 454.7,194 334.7,242 	'
            />
            <polyline
              className='st0'
              points='432.7,451 458.7,397 434.7,313 329.8,275.6 	'
            />
            <polyline
              className='st0'
              points='363.7,453 378.7,428 378.7,391 378.7,363 326,318.1 	'
            />
          </g>
        </g>
      </g>
    </svg>
  )
}

export default Spider
