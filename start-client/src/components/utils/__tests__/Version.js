import get from 'lodash.get'

import {
  compare,
  getValidDependencies,
  isInRange,
  parseQualifier,
  parseReleases,
  parseVersion,
  rangeToText,
} from '../Version'

/**
 * Function parseQualifier
 */
describe('parseQualifier', () => {
  it('should return the default value (RELEASE)', () => {
    let release = parseQualifier('1.0.0')
    expect(release).toBe('RELEASE')
    release = parseQualifier('1.0.0.RELEASE')
    expect(release).toBe('RELEASE')
    release = parseQualifier('foo')
    expect(release).toBe('RELEASE')
  })
  it('should return a specific qualifier', () => {
    let release = parseQualifier('1.0.0.M2')
    expect(release).toBe('M')
    release = parseQualifier('1.0.0.RC1')
    expect(release).toBe('RC')
    release = parseQualifier('1.0.0.BUILD-SNAPSHOT')
    expect(release).toBe('BUILD-SNAPSHOT')
  })
})

/**
 * Function parseVersion
 */
describe('parseVersion', () => {
  it('should parse correctly the version', () => {
    const version = parseVersion('1.0.1.RELEASE')
    expect(get(version, 'version')).toBe('1.0.1.RELEASE')
    expect(get(version, 'short')).toBe('1.0.1')
    expect(get(version, 'major')).toBe('1.0.x')
    expect(get(version, 'qualify')).toBe(3)
    expect(get(version, 'minor')).toBe(1)
  })

  it('should not parse the version', () => {
    const version = parseVersion('foo')
    expect(get(version, 'version')).toBe('foo')
    expect(get(version, 'short', null)).toBeNull()
    expect(get(version, 'major', null)).toBeNull()
    expect(get(version, 'qualify', null)).toBeNull()
    expect(get(version, 'minor', null)).toBeNull()
  })
})

/**
 * Function compare
 */
describe('compare', () => {
  it('should compare 2 versions', () => {
    let result = compare('1.0.0.RELEASE', '1.0.1.RELEASE')
    expect(result < 0).toBe(true)
    result = compare('1.0.2.RELEASE', '1.0.1.RELEASE')
    expect(result > 0).toBe(true)
    result = compare('1.0.1.RELEASE', '1.0.1.M1')
    expect(result > 0).toBe(true)
    result = compare('1.0.1.RELEASE', '1.0.1.RC1')
    expect(result > 0).toBe(true)
    result = compare('1.0.1.RELEASE', '1.0.1.BUILD-SNAPSHOT')
    expect(result > 0).toBe(true)
    result = compare('1.0.1', '1.0.1.BUILD-SNAPSHOT')
    expect(result > 0).toBe(true)
    result = compare('1.0.1.RELEASE', '1.0.1')
    expect(result > 0).toBe(true)
    result = compare('1.0.1', '1.0.1.RELEASE')
    expect(result < 0).toBe(true)
  })
})

/**
 * Function parseReleases
 */
describe('parseReleases', () => {
  it('should parse the versions', () => {
    const result = parseReleases([{ key: '1.0.1.RELEASE' }])
    expect(result.length).toBe(1)
    expect(get(result[0], 'version')).toBe('1.0.1.RELEASE')
    expect(get(result[0], 'short')).toBe('1.0.1')
    expect(get(result[0], 'major')).toBe('1.0.x')
    expect(get(result[0], 'qualify')).toBe(3)
    expect(get(result[0], 'minor')).toBe(1)
  })
})

/**
 * Function isInRange
 */
describe('isInRange', () => {
  it('should return true', () => {
    let result = isInRange('2.1.1.RELEASE', '[2.0.0.RELEASE,2.2.0.M1)')
    expect(result).toBe(true)
    result = isInRange('2.0.0.RELEASE', '[2.0.0.RELEASE,2.2.0.M1)')
    expect(result).toBe(true)
    result = isInRange('2.0.0.RELEASE', '')
    expect(result).toBe(true)
  })
  it('should return false', () => {
    let result = isInRange('1.9.8.RELEASE', '[2.0.0.RELEASE,2.2.0.M1)')
    expect(result).toBe(false)
    result = isInRange('2.0.0.M1', '[2.0.0.RELEASE,2.2.0.M1)')
    expect(result).toBe(false)
  })
})

/**
 * Function rangeToText
 */
describe('rangeToText', () => {
  it('should return the correct string', () => {
    let result = rangeToText('[2.0.0.RELEASE,2.2.0.M1)')
    expect(result).toBe('>= 2.0.0.RELEASE and < 2.2.0.M1')
    result = rangeToText('[2.0.0.RELEASE,2.2.0.M1]')
    expect(result).toBe('>= 2.0.0.RELEASE and <= 2.2.0.M1')
    result = rangeToText('(2.0.0.RELEASE,2.2.0.M1]')
    expect(result).toBe('> 2.0.0.RELEASE and <= 2.2.0.M1')
    result = rangeToText('2.0.0.RELEASE')
    expect(result).toBe('>= 2.0.0.RELEASE')
  })
})

/**
 * Function getValidDependencies
 */
describe('getValidDependencies', () => {
  it('should return the valid dependencies', () => {
    const result = getValidDependencies('2.0.0.RELEASE', [
      {
        name: 'foo1',
        versionRange: '[2.0.0.RELEASE,2.2.0.M1)',
      },
      {
        name: 'foo2',
        versionRange: '(2.0.0.RELEASE,2.2.0.M1]',
      },
      {
        name: 'foo3',
        versionRange: '2.0.0.RELEASE',
      },
      {
        name: 'foo4',
        versionRange: '',
      },
      {
        name: 'foo5',
      },
    ])
    expect(result.length).toBe(4)
    expect(get(result[0], 'name')).toBe('foo1')
    expect(get(result[1], 'name')).toBe('foo3')
    expect(get(result[2], 'name')).toBe('foo4')
    expect(get(result[3], 'name')).toBe('foo5')
  })
})
