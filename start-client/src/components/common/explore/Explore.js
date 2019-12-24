import '../../../styles/explore.scss'

import FileSaver from 'file-saver'
import JSZip from 'jszip'
import Modal from 'react-responsive-modal'
import PropTypes from 'prop-types'
import get from 'lodash.get'
import React, { useContext, useEffect, useState } from 'react'
import { CopyToClipboard } from 'react-copy-to-clipboard'
import { toast } from 'react-toastify'

import Code from './Code'
import Loading from './Loading'
import Mistletoe from '../../endofyear/Mistletoe'
import Tree from './Tree'
import { AppContext } from '../../reducer/App'
import { IconFile, IconTimes } from '../icons'
import { createTree, findRoot } from '../../utils/Zip'

function Explore({ open, onClose, projectName, blob }) {
  const [button, setButton] = useState('Copy')
  const [tree, setTree] = useState(null)
  const [selected, setSelected] = useState(null)
  const { dispatch, explore } = useContext(AppContext)

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
        dispatch({ type: 'EXPLORE_UPDATE', payload: { open: false } })
        toast.error(e.message)
      }
    }
    if (explore && blob) {
      load()
    }
  }, [explore, blob, dispatch])

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

  return (
    <div>
      <Modal
        open={open}
        onClose={() => {
          setSelected(null)
          onClose()
        }}
        showCloseIcon={false}
        classNames={{ modal: 'modal-explorer', overlay: 'overlay' }}
      >
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
                    // onSelected(item)
                  }}
                  tree={tree}
                />
              </div>
              <div className='foot'>
                <a
                  href='/#'
                  onClick={e => {
                    e.preventDefault()
                    downloadZip()
                  }}
                  className='action'
                >
                  Download the ZIP
                </a>
              </div>
            </div>
            <div className='right'>
              <Mistletoe />
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
                          download(selected)
                        }}
                        className='action'
                      >
                        Download
                      </a>
                      <span className='divider'>|</span>
                      <CopyToClipboard
                        onCopy={onCopy}
                        text={get(selected, 'content', '')}
                      >
                        <a
                          href='/#'
                          onClick={e => {
                            e.preventDefault()
                          }}
                          className='action'
                        >
                          {button}
                        </a>
                      </CopyToClipboard>
                      {get(selected, 'language') === 'markdown' && (
                        <>
                          <span className='divider'>|</span>
                          <a
                            href='/#'
                            onClick={e => {
                              e.preventDefault()
                              const newSelected = { ...selected }
                              newSelected.force = !get(selected, 'force', false)
                              setSelected(newSelected)
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
                        onClose()
                      }}
                      className='close'
                    >
                      <IconTimes />
                    </a>
                  </div>
                  <div className='explorer-content'>
                    <Code item={selected} onChange={() => {}} />
                  </div>
                </>
              )}
            </div>
          </div>
        ) : (
          <Loading onClose={onClose} />
        )}
      </Modal>
    </div>
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
