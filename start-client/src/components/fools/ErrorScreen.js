import '../../styles/fools.scss'

import PropTypes from 'prop-types'
import get from 'lodash.get'
import React, { useEffect, useRef, useState } from 'react'
import { clearAllBodyScrollLocks, disableBodyScroll } from 'body-scroll-lock'

import Qrcode from './Qrcode'

function ErrorScreen({ onClose, open }) {
  const [loading, setLoading] = useState(0)
  const [step, setStep] = useState(0)
  const timer = useRef()
  const maxTick = 14
  const wrapper = useRef(null)

  useEffect(() => {
    if (!open) {
      setStep(0)
      setLoading(0)
      clearAllBodyScrollLocks()
      if (timer.current) {
        clearInterval(timer.current)
      }
    }
  }, [open, loading, timer, wrapper])

  useEffect(() => {
    if (loading === 0 && open) {
      const update = count => {
        timer.current = setTimeout(() => {
          setLoading(Math.min(Math.max(Math.round(Math.random() * 100), 1), 99))
          if (count < maxTick && open) {
            if (count > maxTick - 7) {
              setStep(1)
            }
            if (count > maxTick - 5) {
              setStep(2)
            }
            if (count > maxTick - 3) {
              setStep(3)
            }
            update(count + 1)
          } else {
            onClose()
          }
        }, 1000)
      }
      if (get(wrapper, 'current')) {
        disableBodyScroll(get(wrapper, 'current'))
      }
      update(0)
    }
  }, [loading, onClose, open, timer, wrapper])

  const renderSwitch = () => {
    switch (step) {
      case 1:
        return `Save completed!`
      case 2:
        return `Restart the interwebs...`
      case 3:
        return `Restart completed!`
      default:
      case 0:
        return `${loading}% complete`
    }
  }

  const date = new Date()
  if (!(date.getDate() === 1 && date.getMonth() === 3)) {
    return <></>
  }

  if (open) {
    return (
      <div className='fools' ref={wrapper}>
        <div className='windows-screen'>
          <div className='wrap'>
            <h2>;(</h2>
            <p>
              The interwebs ran into a problem and need to restart. Please
              <br className='not-mobile' />
              hold while we&apos;re collecting some error info, and then
              we&apos;ll
              <br className='not-mobile' />
              reboot it for you.
            </p>
            <p>{renderSwitch()}</p>
            <div className='qrcode-block'>
              <div className='qrcode'>
                <Qrcode />
              </div>
              <p>
                For more information about the issue and possible fixes, visit
                {` `}
                <a href='https://www.spring.io/'>https://spring.io</a>.
                <br className='not-mobile' />
                <br />
                No need to open an issue, we have already been notified.
                <br />
                To close this message, yell <strong>“Stop!”</strong> at your
                computer
                <span className='small'> (or really, just press Escape).</span>
              </p>
            </div>

            <p className='is-mobile'>
              <button className='button' type='button' onClick={onClose}>
                <span>Close</span>
              </button>
            </p>
          </div>
        </div>
      </div>
    )
  }
  return <></>
}

ErrorScreen.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
}

export default ErrorScreen
