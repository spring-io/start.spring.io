const strict_range = /\[(.*),(.*)\]/
const halfopen_right_range = /\[(.*),(.*)\)/
const halfopen_left_range = /\((.*),(.*)\]/
const qualifiers = ['M', 'RC', 'BUILD-SNAPSHOT', 'RELEASE']

const parseQualifier = version => {
  const qual = (version || '')
    .replace(/\d+/g, '')
    .replace(/\./g, ' ')
    .replace(/\s/g, '')
  return qualifiers.indexOf(qual) > -1 ? qual : 'RELEASE'
}

export const parseVersion = version => {
  const r = version.split('.')
  return {
    version,
    short: `${r[0]}.${r[1]}.${r[2]}`,
    major: `${r[0]}.${r[1]}.x`,
    qualify: qualifiers.indexOf(parseQualifier(version)),
    minor: +r[2],
  }
}

export const compare = (a, b) => {
  let result
  const versionA = a.split('.')
  const versionB = b.split('.')

  for (let i = 0; i < 3; i++) {
    result = parseInt(versionA[i], 10) - parseInt(versionB[i], 10)
    if (result !== 0) {
      return result
    }
  }
  const qualify = version => qualifiers.indexOf(parseQualifier(version))
  result = qualify(versionA[3]) - qualify(versionB[3])
  if (result !== 0) {
    return result
  }
  return versionA[3].localeCompare(versionB[3])
}

export const parseReleases = releases => {
  return releases.map(release => {
    const version = parseVersion(release.key)
    return version
  })
}

export const isInRange = (version, range) => {
  const strict_match = range.match(strict_range)
  if (strict_match) {
    return (
      compare(strict_match[1], version) <= 0 &&
      compare(strict_match[2], version) >= 0
    )
  }
  const hor_match = range.match(halfopen_right_range)
  if (hor_match) {
    return (
      compare(hor_match[1], version) <= 0 && compare(hor_match[2], version) > 0
    )
  }
  const hol_match = range.match(halfopen_left_range)
  if (hol_match) {
    return (
      compare(hol_match[1], version) < 0 && compare(hol_match[2], version) >= 0
    )
  }
  return compare(range, version) <= 0
}

export const rangeToText = range => {
  const strict_match = range.match(strict_range)
  if (strict_match) {
    return '>= ' + strict_match[1] + ' and <= ' + strict_match[2]
  }
  const hor_match = range.match(halfopen_right_range)
  if (hor_match) {
    return '>= ' + hor_match[1] + ' and < ' + hor_match[2]
  }
  const hol_match = range.match(halfopen_left_range)
  if (hol_match) {
    return '> ' + hol_match[1] + ' and <= ' + hol_match[2]
  }
  return '>= ' + range
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
