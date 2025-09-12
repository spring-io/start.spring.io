const strictRange = /\[(.*),(.*)\]/
const halfopenRightRange = /\[(.*),(.*)\)/
const halfopenLeftRange = /\((.*),(.*)\]/

export const parseQualifier = version => {
  const splitted = (version || '').split('.')
  if (splitted.length < 3) {
    return 'RELEASE'
  }
  if (splitted.length === 4) {
    return splitted[3]
  }
  const last = splitted[2]
  const dash = last.indexOf('-')
  if (dash !== -1) {
    return last.substring(dash + 1)
  }
  return 'RELEASE'
}

export const RELEASE_ORDER = 300000
const SNAPSHOT_ORDER = 200000
const RELEASE_CANDIDATE_ORDER = 100000
const MILESTONE_ORDER = 0

const getQualifierOrder = qualifier => {
  if (qualifier === 'RELEASE') {
    return RELEASE_ORDER
  }
  if (qualifier === 'SNAPSHOT' || qualifier === 'BUILD-SNAPSHOT') {
    return SNAPSHOT_ORDER
  }
  if (qualifier.startsWith('RC')) {
    return RELEASE_CANDIDATE_ORDER + parseInt(qualifier.substring(2), 10)
  }
  if (qualifier.startsWith('M')) {
    return MILESTONE_ORDER + parseInt(qualifier.substring(1), 10)
  }
  return RELEASE_ORDER
}

export const parseVersion = version => {
  const r = version.toString().split('.')
  if (r.length < 2) {
    return {
      version,
    }
  }
  return {
    version,
    short: `${r[0]}.${r[1]}.${r[2]}`,
    major: `${r[0]}.${r[1]}.x`,
    qualify: getQualifierOrder(parseQualifier(version)),
    minor: +r[2],
  }
}

export const compare = (a, b) => {
  let result
  const versionA = a.split('.')
  const versionB = b.split('.')
  if (versionA.length === 3) {
    versionA[3] = ''
  }
  if (versionB.length === 3) {
    versionB[3] = ''
  }
  for (let i = 0; i < 3; i += 1) {
    result = parseInt(versionA[i], 10) - parseInt(versionB[i], 10)
    if (result !== 0) {
      return result
    }
  }
  const qualify = version => getQualifierOrder(parseQualifier(version))
  result = qualify(a) - qualify(b)
  if (result !== 0) {
    return result
  }
  return versionA[3].localeCompare(versionB[3])
}

export const parseReleases = releases => {
  return releases.map(release => {
    return parseVersion(release.key)
  })
}

export const isInRange = (version, range) => {
  if (!range) {
    return true
  }
  const strictMatch = range.match(strictRange)
  if (strictMatch) {
    return (
      compare(strictMatch[1], version) <= 0 &&
      compare(strictMatch[2], version) >= 0
    )
  }
  const horMatch = range.match(halfopenRightRange)
  if (horMatch) {
    return (
      compare(horMatch[1], version) <= 0 && compare(horMatch[2], version) > 0
    )
  }
  const holMatch = range.match(halfopenLeftRange)
  if (holMatch) {
    return (
      compare(holMatch[1], version) < 0 && compare(holMatch[2], version) >= 0
    )
  }
  return compare(range, version) <= 0
}

export const rangeToText = range => {
  const strictMatch = range.match(strictRange)
  if (strictMatch) {
    return `>= ${strictMatch[1]} and <= ${strictMatch[2]}`
  }
  const horMatch = range.match(halfopenRightRange)
  if (horMatch) {
    return `>= ${horMatch[1]} and < ${horMatch[2]}`
  }
  const holMatch = range.match(halfopenLeftRange)
  if (holMatch) {
    return `> ${holMatch[1]} and <= ${holMatch[2]}`
  }
  return `>= ${range}`
}

export const getValidDependencies = (boot, dependencies) => {
  return dependencies
    .map(dep => {
      const compatibility = dep.versionRange
        ? isInRange(boot, dep.versionRange)
        : true
      if (!compatibility) {
        return null
      }
      return dep
    })
    .filter(d => !!d)
}
