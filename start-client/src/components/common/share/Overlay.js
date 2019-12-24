import PropTypes from 'prop-types'
import React, { useRef } from 'react'
import { CSSTransition, TransitionGroup } from 'react-transition-group'
import { Linear, Sine, TweenMax } from 'gsap'

import useTheme from '../../utils/Theme'
import useWindowsUtils from '../../utils/WindowsUtils'
import { noScroll } from '../../utils/NoScroll'

const SNOW_TOTAL = 40

function getRandom(min, max) {
  return min + Math.random() * (max - min)
}

function Overlay({ open }) {
  const theme = useTheme()
  let container = useRef(null)
  const windowsUtils = useWindowsUtils()
  const refs = []
  const items = []
  if (theme === 'dark') {
    for (let i = 0; i < SNOW_TOTAL; i += 1) {
      refs.push(null)
      items.push(
        <div
          ref={element => {
            refs[i] = element
          }}
          id={`snow-${i}`}
          className='dot'
          key={`snow-${i}`}
        />
      )
    }
  }

  const onEnter = () => {
    noScroll.on()
    if (theme === 'dark') {
      TweenMax.set(container, { perspective: 600 })
      for (let i = 0; i < SNOW_TOTAL; i += 1) {
        const item = refs[i]
        TweenMax.set(item, {
          x: getRandom(0, windowsUtils.width),
          y: getRandom(-200, -150),
          z: getRandom(-200, 200),
        })
        TweenMax.to(item, getRandom(3, 6.5), {
          y: windowsUtils.height + 100,
          ease: Linear.easeNone,
          repeat: -1,
          delay: -15,
        })
        TweenMax.to(item, getRandom(5, 7), {
          x: '+=100',
          repeat: -1,
          yoyo: true,
          ease: Sine.easeInOut,
        })
      }
    }
  }

  const onEnded = () => {
    noScroll.off()
  }

  return (
    <TransitionGroup component={null}>
      {open && (
        <CSSTransition
          onEnter={onEnter}
          onExit={onEnded}
          classNames='overlay'
          timeout={500}
        >
          <div
            className='popup-share-overlay'
            ref={element => {
              container = element
            }}
          >
            {items}
          </div>
        </CSSTransition>
      )}
    </TransitionGroup>
  )
}

Overlay.propTypes = {
  open: PropTypes.bool.isRequired,
}

export default Overlay
