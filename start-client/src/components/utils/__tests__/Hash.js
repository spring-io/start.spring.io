import { parseHash } from '../Hash'

describe('parseHash', () => {
  it('strips #! prefix', () => {
    expect(parseHash('#!type=gradle-project&language=kotlin')).toEqual({
      type: 'gradle-project',
      language: 'kotlin',
    })
  })

  it('strips # prefix', () => {
    expect(parseHash('#type=gradle-project&language=kotlin')).toEqual({
      type: 'gradle-project',
      language: 'kotlin',
    })
  })
})
