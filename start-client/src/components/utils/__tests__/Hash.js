import queryString from 'query-string'
import { parseHash } from '../Hash'

jest.mock('query-string', () => ({
  parse: jest.fn(str => {
    const params = {}
    if (!str) return params
    new URLSearchParams(str).forEach((val, key) => {
      params[key] = val
    })
    return params
  }),
}))

describe('parseHash', () => {
  beforeEach(() => {
    jest.clearAllMocks()
  })

  it('handles empty or undefined hash', () => {
    expect(parseHash()).toEqual({})
    expect(parseHash('')).toEqual({})
    expect(parseHash(null)).toEqual({})
    expect(parseHash('#')).toEqual({})
    expect(parseHash('#!')).toEqual({})
    expect(parseHash('#!/')).toEqual({})
    expect(parseHash('#/')).toEqual({})
  })

  it('passes clean string to queryString.parse with #! prefix', () => {
    parseHash('#!type=gradle-project&language=kotlin')
    expect(queryString.parse).toHaveBeenCalledWith(
      'type=gradle-project&language=kotlin'
    )
  })

  it('passes clean string to queryString.parse with # prefix without truncating parameter name', () => {
    parseHash('#type=gradle-project&language=kotlin')
    expect(queryString.parse).toHaveBeenCalledWith(
      'type=gradle-project&language=kotlin'
    )
  })

  it('passes clean string to queryString.parse with #!/ prefix', () => {
    parseHash('#!/type=maven-project&language=java')
    expect(queryString.parse).toHaveBeenCalledWith(
      'type=maven-project&language=java'
    )
  })

  it('passes clean string to queryString.parse without leading #', () => {
    parseHash('type=gradle-project&language=groovy')
    expect(queryString.parse).toHaveBeenCalledWith(
      'type=gradle-project&language=groovy'
    )
  })

  it('parses multiple parameters correctly', () => {
    const parsed = parseHash(
      '#!type=gradle-project&language=kotlin&platformVersion=3.2.0&packaging=jar'
    )
    expect(parsed).toEqual({
      type: 'gradle-project',
      language: 'kotlin',
      platformVersion: '3.2.0',
      packaging: 'jar',
    })
  })
})
