import FileSaver from 'file-saver'
import Modal from 'react-responsive-modal'
import PropTypes from 'prop-types'
import React from 'react'
import get from 'lodash.get'
import { CopyToClipboard } from 'react-copy-to-clipboard'

import CodePrism from './CodePrism'
import FileTree from './FileTree'
import { IconFile, IconTimes } from './../icons'
import { Placeholder } from './../form'

class ExploreModal extends React.Component {
  constructor(props) {
    super(props)
    this.ref = React.createRef()
    this.state = {
      button: 'Copy',
    }
  }

  onCopy = () => {
    this.setState({ button: 'Copied!' })
    setTimeout(() => {
      this.setState({ button: 'Copy' })
    }, 3000)
  }

  download = file => {
    let blob = new Blob([file.content], { type: 'text/plain;charset=utf-8' })
    FileSaver.saveAs(blob, file.filename)
  }

  onClickItem = item => {
    if (get(this.ref, 'current')) {
      this.ref.current.scrollTop = 0
      this.ref.current.scrollLeft = 0
    }
    this.props.onSelected(item)
  }

  downloadZip = () => {
    const { blob, projectName } = this.props
    FileSaver.saveAs(blob, projectName)
  }

  render() {
    const { tree, selected } = this.props
    return (
      <div>
        <Modal
          open={this.props.open}
          onClose={this.props.onClose}
          showCloseIcon={false}
          classNames={{ modal: 'modal-explorer', overlay: 'overlay' }}
        >
          {tree ? (
            <div className='colset-explorer'>
              <div className='left'>
                <div className='head'>
                  <strong>{this.props.projectName}</strong>
                </div>
                <div className='content'>
                  <FileTree
                    selected={selected}
                    onClickItem={this.onClickItem}
                    tree={tree}
                  />
                </div>
                <div className='foot'>
                  <a
                    href='/#'
                    onClick={e => {
                      e.preventDefault()
                      this.downloadZip()
                    }}
                    className='action'
                  >
                    Download the ZIP
                  </a>
                </div>
              </div>
              <div className='right'>
                {selected && (
                  <>
                    <div className='head'>
                      <strong>
                        <IconFile />
                        {get(selected, 'filename')}
                      </strong>
                      <div className='actions'>
                        <span className='divider'>|</span>
                        <a
                          href='/#'
                          onClick={e => {
                            e.preventDefault()
                            this.download(selected)
                          }}
                          className='action'
                        >
                          Download
                        </a>
                        <span className='divider'>|</span>
                        <CopyToClipboard
                          onCopy={this.onCopy}
                          text={get(selected, 'content', '')}
                        >
                          <a
                            href='/#'
                            onClick={e => {
                              e.preventDefault()
                            }}
                            className='action'
                          >
                            {this.state.button}
                          </a>
                        </CopyToClipboard>
                        {get(selected, 'language') === 'markdown' && (
                          <>
                            <span className='divider'>|</span>
                            <a
                              href='/#'
                              onClick={e => {
                                e.preventDefault()
                                selected.force = !get(selected, 'force', false)
                                this.props.onSelected(selected)
                              }}
                              className='action'
                            >
                              {get(selected, 'force', false)
                                ? 'Preview'
                                : 'View source'}
                            </a>
                          </>
                        )}
                      </div>
                      <a
                        href='/#'
                        onClick={e => {
                          e.preventDefault()
                          this.props.onClose()
                        }}
                        className='close'
                      >
                        <IconTimes />
                      </a>
                    </div>
                    <div className='content' ref={this.ref}>
                      <CodePrism item={selected} onChange={this.onClickItem} />
                    </div>
                  </>
                )}
              </div>
            </div>
          ) : (
            <>
              <a href='/#'>{` `}</a>

              <div className='colset-explorer'>
                <div className='left'>
                  <div className='head'>
                    <Placeholder width='70px' type='text' />
                  </div>
                  <div className='content'>
                    <ul className='explorer-ul-placeholder'>
                      <li>
                        <Placeholder type='text' width='66px' />
                      </li>
                      <li>
                        <Placeholder type='text' width='60px' />
                      </li>
                      <li>
                        <Placeholder type='text' width='45px' />
                      </li>
                      <li>
                        <Placeholder type='text' width='87px' />
                      </li>
                      <li>
                        <Placeholder type='text' width='80px' />
                      </li>
                      <li>
                        <Placeholder type='text' width='94px' />
                      </li>
                      <li>
                        <Placeholder type='text' width='86px' />
                      </li>
                    </ul>
                  </div>
                  <div className='foot'>
                    <Placeholder type='text' width='120px' />
                  </div>
                </div>
                <div className='right'>
                  <>
                    <div className='head'>
                      <Placeholder width='86px' type='text' />
                      <div className='actions'>
                        <Placeholder width='74px' type='text' />
                        <Placeholder width='43px' type='text' />
                      </div>
                      <a
                        href='/#'
                        onClick={e => {
                          e.preventDefault()
                          this.props.onClose()
                        }}
                        className='close'
                      >
                        <IconTimes />
                      </a>
                    </div>
                    <div className='content' />
                  </>
                </div>
              </div>
            </>
          )}
        </Modal>
      </div>
    )
  }
}

ExploreModal.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  onSelected: PropTypes.func.isRequired,
  tree: PropTypes.object,
  selected: PropTypes.object,
  projectName: PropTypes.string,
  blob: PropTypes.any,
}

export default ExploreModal
