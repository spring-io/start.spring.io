export const noScroll = {
  on: () => {
    document.documentElement.style.overflow = 'hidden'
  },
  off: () => {
    document.documentElement.style.overflow = ''
  },
}
