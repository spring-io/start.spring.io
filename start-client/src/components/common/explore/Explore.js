import '../../../styles/explore.scss'

import FileSaver from 'file-saver'
import JSZip from 'jszip'
import PropTypes from 'prop-types'
import get from 'lodash.get'
import React, { useContext, useEffect, useRef, useState } from 'react'
import { CSSTransition, TransitionGroup } from 'react-transition-group'
import { CopyToClipboard } from 'react-copy-to-clipboard'
import { clearAllBodyScrollLocks, disableBodyScroll } from 'body-scroll-lock'
import { toast } from 'react-toastify'

import Code from './Code'
import Loading from './Loading'
import Select from './Select'
import Tree from './Tree'
import useWindowsUtils from '../../utils/WindowsUtils'
import { AppContext } from '../../reducer/App'
import { createTree, findRoot } from '../../utils/Zip'

function Explore({ open, onClose, projectName, blob }) {
  const [button, setButton] = useState('Copy')
  const [tree, setTree] = useState(null)
  const [selected, setSelected] = useState(null)
  const { dispatch, explore } = useContext(AppContext)
  const wrapper = useRef(null)

  const windowsUtils = useWindowsUtils()

  useEffect(() => {
    const load = async () => {
      try {
        const zipJs = new JSZip()
        const { files } = await zipJs.loadAsync(blob).catch(() => {
          throw Error(`Could not load the ZIP project.`)
        })
        const path = `${findRoot({ files })}/`
        const result = await createTree(files, path, path, zipJs).catch(() => {
          throw Error(`Could not read the ZIP project.`)
        })
        setSelected(result.selected)
        setTree(result.tree)
      } catch (e) {
        dispatch({ type: 'UPDATE', payload: { explore: false } })
        toast.error(e.message)
      }
    }
    if (explore && blob) {
      load()
    }
  }, [explore, blob, dispatch])

  useEffect(() => {
    if (get(wrapper, 'current') && open) {
      disableBodyScroll(get(wrapper, 'current'))
    }
    return () => {
      clearAllBodyScrollLocks()
    }
  }, [wrapper, open, selected, tree])

  const onCopy = () => {
    setButton('Copied!')
    setTimeout(() => {
      setButton('Copy!')
    }, 3000)
  }

  const download = file => {
    const blobFile = new Blob([file.content], {
      type: 'text/plain;charset=utf-8',
    })
    FileSaver.saveAs(blobFile, file.filename)
  }

  const downloadZip = () => {
    FileSaver.saveAs(blob, projectName)
  }

  const onEnded = () => {
    setTimeout(() => {
      setTree(null)
      setSelected(null)
      clearAllBodyScrollLocks()
    }, 350)
  }

  return (
    <TransitionGroup component={null}>
      {open && (
        <CSSTransition onExit={onEnded} classNames='explorer' timeout={500}>
          <div className='explorer'>
            {tree && selected ? (
              <div className='colset-explorer'>
                <div className='left'>
                  <div className='head'>
                    <strong>{projectName}</strong>
                  </div>
                  <div className='explorer-content'>
                    <Tree
                      selected={selected}
                      onClickItem={item => {
                        setSelected(item)
                      }}
                      tree={tree}
                    />
                  </div>
                </div>
                <div className='is-mobile explorer-select'>
                  <Select
                    selected={selected}
                    onClickItem={item => {
                      setSelected(item)
                    }}
                    tree={tree}
                  />
                </div>
                <div className='right'>
                  {selected && (
                    <>
                      <div className='head'>
                        <div className='actions-file'>
                          <a
                            href='/#'
                            onClick={e => {
                              e.preventDefault()
                              download(selected)
                            }}
                            className='button'
                          >
                            <span className='button-content' tabIndex='-1'>
                              <span>Download</span>
                            </span>
                          </a>
                          <CopyToClipboard
                            onCopy={onCopy}
                            text={get(selected, 'content', '')}
                          >
                            <a
                              href='/#'
                              onClick={e => {
                                e.preventDefault()
                              }}
                              className='button'
                            >
                              <span className='button-content' tabIndex='-1'>
                                <span>{button}</span>
                              </span>
                            </a>
                          </CopyToClipboard>
                          {get(selected, 'language') === 'markdown' && (
                            <a
                              href='/#'
                              onClick={e => {
                                e.preventDefault()
                                const newSelected = { ...selected }
                                newSelected.force = !get(
                                  selected,
                                  'force',
                                  false
                                )
                                setSelected(newSelected)
                              }}
                              className='button'
                            >
                              <span className='button-content' tabIndex='-1'>
                                <span>
                                  {get(selected, 'force', false)
                                    ? 'Preview'
                                    : 'View source'}
                                </span>
                              </span>
                            </a>
                          )}
                        </div>
                      </div>
                      <div className='explorer-content' ref={wrapper}>
                        <Code item={selected} onChange={() => {}} />
                      </div>
                    </>
                  )}
                </div>
                <div className='explorer-actions'>
                  <a
                    href='/#'
                    onClick={e => {
                      e.preventDefault()
                      downloadZip()
                    }}
                    className='button'
                  >
                    <span className='button-content' tabIndex='-1'>
                      <span>Download</span>
                      <span className='secondary desktop-only'>
                        {windowsUtils.symb} + ⏎
                      </span>
                    </span>
                  </a>
                  <a
                    href='/#'
                    onClick={e => {
                      e.preventDefault()
                      onClose()
                    }}
                    className='button'
                  >
                    <span className='button-content' tabIndex='-1'>
                      <span>Close</span>
                      <span className='secondary desktop-only'>ESC</span>
                    </span>
                  </a>
                </div>
              </div>
            ) : (
              <Loading onClose={onClose} />
            )}
          </div>
        </CSSTransition>
      )}
    </TransitionGroup>
  )
}

Explore.defaultProps = {
  projectName: '',
  blob: null,
}

Explore.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  projectName: PropTypes.string,
  blob: PropTypes.instanceOf(Blob),
}

export default Explore
