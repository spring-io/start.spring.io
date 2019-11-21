import Prism from 'prism-react-renderer/prism'
import PropTypes from 'prop-types'
import React from 'react'
import get from 'lodash.get'
import Highlight, { defaultProps } from 'prism-react-renderer'
const ReactMarkdown = require('react-markdown')

if (typeof global !== 'undefined') {
  global.Prism = Prism
  require('prismjs/components/prism-java')
  require('prismjs/components/prism-kotlin')
  require('prismjs/components/prism-properties')
  require('prismjs/components/prism-groovy')
  require('prismjs/components/prism-git')
}

class CodePrism extends React.Component {
  toggleSource = item => {
    item.force = !get(item, 'force', false)
    this.props.onChange(item)
  }

  render() {
    const item = get(this.props, 'item')
    const code = get(item, 'content', '').replace(/\t/g, '  ')
    const language = get(item, 'language')
    if (language === 'markdown' && !get(item, 'force', false)) {
      return (
        <div className='markdown'>
          <ReactMarkdown linkTarget={'_blank'} source={code} />
        </div>
      )
    }
    return (
      <Highlight {...defaultProps} code={code} language={language} theme={null}>
        {({ className, style, tokens, getLineProps, getTokenProps }) => {
          let groupLine = tokens.length > 9 ? '2' : '1'
          groupLine = tokens.length > 99 ? '3' : groupLine
          groupLine = tokens.length > 999 ? '4' : groupLine
          return (
            <pre
              ref={this.ref}
              className={`${className} line-${groupLine}`}
              style={style}
            >
              {tokens.map((line, i) => (
                <div {...getLineProps({ line, key: i })}>
                  <span data-value={i + 1} className='explorer-number' />
                  {line.map((token, key) => (
                    <span {...getTokenProps({ token, key })} />
                  ))}
                </div>
              ))}
            </pre>
          )
        }}
      </Highlight>
    )
  }
}

CodePrism.propTypes = {
  item: PropTypes.object.isRequired,
  onChange: PropTypes.func.isRequired,
}

export default CodePrism
