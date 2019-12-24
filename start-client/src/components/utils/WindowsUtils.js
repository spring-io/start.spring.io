import { useState } from 'react'

function getProperties() {
  return {
    symb:
      window.navigator.userAgent.toLowerCase().indexOf('mac') > -1
        ? '⌘'
        : 'Ctrl',
    origin: window.location.origin,
    height: window.innerHeight,
    width: window.innerWidth,
  }
}

export default function useWindowsUtils() {
  const [symb] = useState(getProperties().symb)
  const [origin] = useState(getProperties().origin)
  const [height] = useState(getProperties().height)
  const [width] = useState(getProperties().width)
  return { symb, origin, height, width }
}
