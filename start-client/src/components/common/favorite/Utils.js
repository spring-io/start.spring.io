import { DateTime } from 'luxon'

export function getLabelFromList(list, key) {
  return list.find(item => item.key === key)?.text || key
}

export function getLabelFromDepsList(list, key) {
  return list.find(item => item.id === key)?.name || key
}

export function getBookmarkDefaultName() {
  const date = DateTime.now()
  return `Bookmark ${date.toLocaleString(
    DateTime.DATE_SHORT
  )} ${date.toLocaleString(DateTime.TIME_24_SIMPLE)}`
}
