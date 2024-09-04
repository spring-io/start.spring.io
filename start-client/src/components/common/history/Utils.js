import { DateTime } from 'luxon'

function isToday(date) {
  return date.hasSame(DateTime.now(), 'day')
}

function isYesterday(date) {
  return date.hasSame(DateTime.now().minus({ days: 1 }), 'day')
}

export function Transform(histories) {
  if (histories.length === 0) {
    return []
  }
  const parsed = histories.map(history => {
    const dateLuxon = DateTime.fromISO(history.date)
    let label = ''
    if (isToday(dateLuxon)) {
      label = 'Today, '
    } else if (isYesterday(dateLuxon)) {
      label = 'Yesterday, '
    }
    return {
      date: dateLuxon,
      time: `${dateLuxon.toFormat('HH:mm')}`,
      label: `${label}${dateLuxon.toFormat('cccc, d LLLL yyyy')}`,
      value: history.value,
    }
  })
  return parsed.reduce((acc, history) => {
    if (acc.length === 0) {
      acc.push({
        label: history.label,
        histories: [history],
      })
    } else if (acc[acc.length - 1].label === history.label) {
      acc[acc.length - 1].histories.push(history)
    } else {
      acc.push({
        label: history.label,
        histories: [history],
      })
    }
    return acc
  }, [])
}
