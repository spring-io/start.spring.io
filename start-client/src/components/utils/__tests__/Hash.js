import { parseHash } from '../Hash'

jest.mock('query-string', () => ({
  parse: value =>
    Object.fromEntries(
      value
        .slice(1)
        .split('&')
        .map(parameter => parameter.split('='))
    ),
}))

describe('parseHash', () => {
  it('parses a hash without a prefix', () => {
    expect(parseHash('#type=gradle-project&language=kotlin')).toEqual({
      type: 'gradle-project',
      language: 'kotlin',
    })
  })

  it('parses a shared hash with the bang prefix', () => {
    expect(parseHash('#!type=gradle-project&language=kotlin')).toEqual({
      type: 'gradle-project',
      language: 'kotlin',
    })
  })
})
