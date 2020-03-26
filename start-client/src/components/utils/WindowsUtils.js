import { useEffect, useState } from 'react'

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
  const isClient = typeof window === 'object'
  const [symb] = useState(getProperties().symb)
  const [origin] = useState(getProperties().origin)
  const [height, setHeight] = useState(getProperties().height)
  const [width, setWidth] = useState(getProperties().width)

  useEffect(() => {
    if (!isClient) {
      return false
    }
    function handleResize() {
      setHeight(window.innerHeight)
      setWidth(window.innerWidth)
    }
    window.addEventListener('resize', handleResize)
    return () => window.removeEventListener('resize', handleResize)
  }, [isClient])

  return { symb, origin, height, width }
}
