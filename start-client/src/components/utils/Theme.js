import { useState } from 'react'

function getTheme() {
  const isDarkConfig =
    window.matchMedia &&
    window.matchMedia('(prefers-color-scheme: dark)').matches

  const theme = localStorage.getItem('springtheme')
  if (!theme) {
    return isDarkConfig ? 'dark' : 'light'
  }
  return theme
}

export default function useTheme() {
  const [darkTheme] = useState(getTheme())
  return darkTheme
}
