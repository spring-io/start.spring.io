import Highlight from 'prism-react-renderer'
import Prism from 'prism-react-renderer/prism'
import PropTypes from 'prop-types'
import React from 'react'
import get from 'lodash.get'
import ReactMarkdown from 'react-markdown'

if (typeof global !== 'undefined') {
  global.Prism = Prism
  require('prismjs/components/prism-java') // eslint-disable-line
  require('prismjs/components/prism-kotlin') // eslint-disable-line
  require('prismjs/components/prism-properties') // eslint-disable-line
  require('prismjs/components/prism-groovy') // eslint-disable-line
  require('prismjs/components/prism-git') // eslint-disable-line
}

function Code({ item }) {
  const code = get(item, 'content', '').replace(/\t/g, '  ')
  const language = get(item, 'language')
  if (language === 'markdown' && !get(item, 'force', false)) {
    return (
      <div className='markdown'>
        <ReactMarkdown linkTarget='_blank'>{code}</ReactMarkdown>
      </div>
    )
  }
  return (
    <Highlight Prism={Prism} code={code} language={language} theme={null}>
      {({ className, style, tokens, getLineProps, getTokenProps }) => {
        let groupLine = tokens.length > 9 ? '2' : '1'
        groupLine = tokens.length > 99 ? '3' : groupLine
        groupLine = tokens.length > 999 ? '4' : groupLine
        return (
          <pre className={`${className} line-${groupLine}`} style={style}>
            {tokens.map((line, i) => {
              const props = getLineProps({ line, key: i })
              return (
                <div
                  key={get(props, 'key')}
                  className={get(props, 'className')}
                >
                  <span data-value={i + 1} className='explorer-number' />
                  {line.map((token, key) => {
                    const props2 = getTokenProps({ token, key })
                    return (
                      <span
                        className={get(props2, 'className')}
                        style={get(props2, 'style')}
                        key={get(props2, 'key')}
                      >
                        {get(props2, 'children')}
                      </span>
                    )
                  })}
                </div>
              )
            })}
          </pre>
        )
      }}
    </Highlight>
  )
}

Code.defaultProps = {
  item: {
    content: '',
    force: false,
    language: 'md',
  },
}

Code.propTypes = {
  item: PropTypes.shape({
    content: PropTypes.string,
    force: PropTypes.bool,
    language: PropTypes.string,
  }),
}

export default Code
