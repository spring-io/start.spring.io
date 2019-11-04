import FileSaver from 'file-saver'
import JSZip from 'jszip'
import React from 'react'
import get from 'lodash.get'
import queryString from 'query-string'
import set from 'lodash.set'
import { GlobalHotKeys } from 'react-hotkeys'
import { ToastContainer, toast } from 'react-toastify'
import { graphql } from 'gatsby'

import CloseToast from '../components/utils/CloseToast'
import HashChangeHandler from '../components/utils/HashChangeHandler'
import { BootError, ErrorPage, Warnings } from '../components/common/error'
import { CheckboxList } from '../components/common/checkbox-list'
import { ExploreModal } from '../components/common/explore'
import { Footer, Layout } from '../components/common/layout'
import {
  IconChevronRight,
  IconList,
  IconSearch,
} from '../components/common/icons'
import { List, Placeholder, RadioGroup } from '../components/common/form'
import { Meta } from '../components/common/meta'
import { ShareOverlay, SharePopup } from '../components/common/share'
import { Typehead } from '../components/common/typehead'
import { createTree, findRoot } from '../components/utils/Zip'
import {
  getDefaultValues,
  getInfo,
  getListsValues,
  getProject,
  getShareUrl,
  isValidParams,
  parseParams,
} from '../components/utils/api'
import { getValidDependencies } from '../components/utils/versions'
import { noScroll } from '../components/utils/no-scroll'

class IndexPage extends React.Component {
  /**
   * Constructor
   * Setup the state, request the server
   */
  constructor(props) {
    super(props)
    this.state = {
      complete: false,
      explore: false,
      more: false,
      error: false,
      share: false,
      errors: {},
      warnings: {},
      tab: 'quick-search',
      symb: 'Ctrl',
      groups: {},
      dependencies: [],
    }
    getInfo(this.props.data.site.edges[0].node.siteMetadata.apiUrl).then(
      data => {
        this.json = data
        this.onComplete(data)
      },
      err => {
        this.setState({ error: true })
      }
    )
  }

  /**
   * Component Did Mount
   */
  componentDidMount() {
    if (window.navigator.userAgent.toLowerCase().indexOf('mac') > -1) {
      this.setState({ symb: '⌘' })
    }
    this.setState({ path: window.location.origin })
  }

  /**
   * First loading complete
   * Parse parameters URL, Update the state
   */
  onComplete = json => {
    let values = getDefaultValues(json)
    const rootValues = getDefaultValues(json)
    const lists = getListsValues(json)
    this.lists = lists
    this.setState({
      rootValues,
      ...values,
    })
    this.hashChange()
    this.update({ complete: true })
  }

  /**
   * Update state
   */
  update = change => {
    this.setState(change)
    if (window.location.hash) {
      if (window.history.pushState) {
        window.history.pushState(null, null, window.location.pathname)
      } else {
        window.history.hash = ``
      }
    }
  }

  /**
   * Get Share URL
   */
  getShareUrl = (change, short) => {
    const values = Object.assign(
      {
        language: this.state.language,
        project: this.state.project,
        boot: this.state.boot,
        meta: this.state.meta,
        dependencies: this.state.dependencies,
      },
      change
    )
    return getShareUrl(this.state.path, values, this.state.rootValues, short)
  }

  /**
   * Add a dependency
   */
  dependencyAdd = dependency => {
    this.update({ dependencies: [...this.state.dependencies, dependency] })
  }

  /**
   * Remove a dependency
   */
  dependencyRemove = dependency => {
    this.update({
      dependencies: this.state.dependencies.filter(
        item => dependency.name !== item.name
      ),
    })
  }

  /**
   * Update meta data state
   */
  updateMeta = (prop, value) => {
    let meta = { ...this.state.meta }
    meta[prop] = value
    if (prop === 'artifact') {
      set(meta, 'name', get(meta, 'artifact'))
      set(meta, 'packageName', `${get(meta, 'group')}.${get(meta, 'artifact')}`)
    }
    if (prop === 'group') {
      set(meta, 'packageName', `${get(meta, 'group')}.${get(meta, 'artifact')}`)
    }
    this.update({ meta: meta })
  }

  /**
   * Checking if there is any error in the form
   * Notify the user
   */
  checkError = () => {
    const errors = []
    if (get(this.state, 'errors.boot.code')) {
      errors.push(`Please select a valid Spring Boot version.`)
    }
    if (errors.length > 0) {
      toast.error(
        <>
          {errors.map((err, i) => (
            <div key={i}>{err}</div>
          ))}
        </>
      )
      return true
    }
    return false
  }

  /**
   * Show/hide more inputs
   */
  toggleMore = event => {
    event.preventDefault()
    this.setState({ more: !this.state.more })
    if (!this.state.more) {
      setTimeout(() => {
        this.inputMetaName && this.inputMetaName.focus()
      }, 300)
    }
  }

  /**
   * Set the state of the tabulation component
   */
  updateTab = tab => {
    this.setState({ tab: tab })
    // Hack focus
    if (tab === 'quick-search') {
      try {
        setTimeout(() => {
          document.getElementById('input-quicksearch').focus()
        }, 100)
      } catch (e) {}
    }
  }

  /**
   * Hide/Show a group from the list dependencies
   */
  toggleGroup = group => {
    const val = get(this.state, 'groups')
    val[group] = !get(val, `${group}`, true)
    this.setState({ groups: val })
  }

  /**
   * Generate a project
   * This action request the server and serve the ZIP file to the user
   * async
   */
  onSubmit = async event => {
    event.preventDefault()
    if (this.checkError()) {
      return
    }
    const url = `${this.props.data.site.edges[0].node.siteMetadata.apiZip}`
    const { project, language, boot, meta } = this.state
    const deps = getValidDependencies(boot, this.state.dependencies)
    const blob = await getProject(
      url,
      project,
      language,
      boot,
      meta,
      deps
    ).catch(err => {
      toast.error(`Could not connect to server. Please check your network.`)
    })
    const fileName = `${meta.artifact}.zip`
    FileSaver.saveAs(blob, fileName)
  }

  /**
   * Explore a project
   * This action displays a modal, request the server, read the ZIP file
   * @async
   */
  onExplore = async () => {
    if (get(this.state, 'explore')) {
      return
    }
    if (this.checkError()) {
      return
    }
    this.setState({
      explore: true,
      tree: null,
      file: null,
      projectName: null,
      blob: null,
    })
    const url = `${this.props.data.site.edges[0].node.siteMetadata.apiZip}`
    const { project, language, boot, meta } = this.state
    const deps = getValidDependencies(boot, this.state.dependencies)

    const blob = await getProject(
      url,
      project,
      language,
      boot,
      meta,
      deps
    ).catch(err => {
      toast.error(`Could not connect to server. Please check your network.`)
      this.onExploreClose()
    })
    const zip = new JSZip()
    const { files } = await zip.loadAsync(blob)
    const path = `${findRoot(zip)}/`
    const { tree, selected } = await createTree(files, path, path, zip)
    if (!get(this.state, 'explore')) {
      return
    }
    this.setState({
      explore: true,
      tree: tree,
      file: selected,
      projectName: `${meta.artifact}.zip`,
      blob: blob,
    })
  }

  /**
   * Select a file
   * @param  {[type]} item [description]
   * @return {[type]}      [description]
   */
  onSelectedFile = item => {
    this.setState({ file: item })
  }

  /**
   * Close modal explore
   * Reset the state of the explore feature
   */
  onExploreClose = () => {
    this.setState({
      explore: false,
      tree: null,
      file: null,
      projectName: null,
      blob: null,
    })
  }

  /**
   * Hash Change Handler
   */
  hashChange = () => {
    let values = getDefaultValues(this.json)
    values.dependencies = []
    if (window.location.hash) {
      const queryParams = queryString.parse(
        `?${window.location.hash.substr(2)}`
      )
      if (isValidParams(queryParams)) {
        const params = parseParams(values, queryParams, this.lists)
        values = { ...params.values }
        values.dependencies = params.dependencies
        values.errors = params.errors
        values.warnings = params.warnings
        // if (
        //   Object.keys(values.warnings).length > 0 ||
        //   Object.keys(values.errors).length > 0
        // ) {
        //   if (
        //     get(values.warnings, 'meta.java') ||
        //     get(values.warnings, 'meta.packaging')
        //   ) {
        //     values.more = true
        //   }
        //   // toast.warning('Configuration loaded with warnings.', {
        //   //   autoClose: 2000,
        //   // })
        // } else {
        //   // toast.success('Configuration loaded.', { autoClose: 2000 })
        // }
        toast.success('Configuration loaded.', { autoClose: 2000 })
      }
    }
    this.update(values)
  }

  /**
   * Share
   * This action displays a popup and an overlay
   */
  onShare = () => {
    if (this.state.share) {
      return
    }
    if (this.checkError()) {
      return
    }
    noScroll.on()
    this.setState({
      share: true,
    })
  }

  /**
   * On close share popup
   */
  onShareClose = () => {
    if (!this.state.share) {
      return
    }
    this.setState({
      share: false,
    })
    noScroll.off()
  }

  /**
   * Render page
   */
  render = () => {
    if (get(this.state, 'error')) {
      return <ErrorPage />
    }
    const selected = get(
      getValidDependencies(this.state.boot, this.state.dependencies),
      'length',
      0
    )
    return (
      <Layout>
        <GlobalHotKeys
          keyMap={{
            SUBMIT: ['command+enter', 'ctrl+enter'],
            EXPLORE: ['ctrl+space'],
          }}
          handlers={{
            SUBMIT: event => {
              this.submit(event)
            },
            EXPLORE: event => {
              this.onExplore()
            },
          }}
          global
        />
        <Meta />
        <ToastContainer
          closeButton={<CloseToast />}
          position='top-center'
          hideProgressBar
        />
        <HashChangeHandler onChange={this.hashChange} />
        <form onSubmit={this.onSubmit} autoComplete='off'>
          <input
            style={{ display: 'none' }}
            type='text'
            name='fakeusernameremembered'
          />
          <input
            style={{ display: 'none' }}
            type='password'
            name='fakepasswordremembered'
          />
          <Warnings
            values={this.state.warnings}
            defaultValues={{
              language: this.state.language,
              project: this.state.project,
              boot: this.state.boot,
              meta: this.state.meta,
            }}
            lists={this.lists}
            hide={() => {
              this.setState({ warnings: null })
            }}
          />
          <div className='colset'>
            <div className='left'>Project</div>
            <div className='right'>
              {get(this.state, 'complete') ? (
                <RadioGroup
                  name='project'
                  selected={this.state.project}
                  options={this.lists.project}
                  onChange={value => {
                    this.update({ project: value })
                  }}
                />
              ) : (
                <Placeholder type='radios' count={2} width='133px' />
              )}
            </div>
          </div>
          <div className='colset'>
            <div className='left'>Language</div>
            <div className='right'>
              {get(this.state, 'complete') ? (
                <RadioGroup
                  name='language'
                  selected={this.state.language}
                  onChange={value => {
                    this.update({ language: value })
                  }}
                  options={this.lists.language}
                />
              ) : (
                <Placeholder type='radios' count={3} width='73px' />
              )}
            </div>
          </div>
          <div className='colset'>
            <div className='left'>Spring Boot</div>
            <div className='right'>
              {get(this.state, 'complete') ? (
                <>
                  <RadioGroup
                    name='boot'
                    selected={this.state.boot}
                    options={this.lists.boot}
                    error={
                      get(this.state, 'errors.boot.code') === 'invalid'
                        ? get(this.state, 'errors.boot.value')
                        : ''
                    }
                    onChange={value => {
                      this.update({ boot: value, errors: { boot: null } })
                    }}
                  />
                  {get(this.state, 'errors.boot.code') === 'invalid' && (
                    <BootError value={get(this.state, 'errors.boot.value')} />
                  )}
                </>
              ) : (
                <Placeholder type='radios' count={5} width='105px' />
              )}
            </div>
          </div>
          <div className='colset'>
            <div className='left'>Project Metadata</div>
            <div className='right right-md'>
              {get(this.state, 'complete') ? (
                <>
                  <div className='control'>
                    <label htmlFor='input-group'>Group</label>
                    <input
                      type='text'
                      id='input-group'
                      className='control-input'
                      value={this.state.meta.group}
                      onChange={event => {
                        this.updateMeta('group', event.target.value)
                      }}
                    />
                  </div>
                  <div className='control'>
                    <label htmlFor='input-artifact'>Artifact</label>
                    <input
                      type='text'
                      id='input-artifact'
                      className='control-input'
                      value={this.state.meta.artifact}
                      onChange={event => {
                        this.updateMeta('artifact', event.target.value)
                      }}
                    />
                  </div>
                  <div className='more'>
                    <div className='wrap'>
                      <a
                        href='/'
                        onClick={this.toggleMore}
                        className={this.state.more ? 'toggle' : ''}
                      >
                        <IconChevronRight />
                        {!this.state.more ? 'Options' : 'Options'}
                      </a>
                    </div>
                  </div>
                  <div
                    className={`panel ${this.state.more ? 'panel-active' : ''}`}
                  >
                    <div className='panel-wrap'>
                      <div className='control'>
                        <label htmlFor='input-name'>Name</label>
                        <input
                          type='text'
                          id='input-name'
                          className='control-input'
                          value={this.state.meta.name}
                          disabled={!this.state.more}
                          ref={input => {
                            this.inputMetaName = input
                          }}
                          onChange={event => {
                            this.updateMeta('name', event.target.value)
                          }}
                        />
                      </div>
                      <div className='control'>
                        <label htmlFor='input-description'>Description</label>
                        <input
                          type='text'
                          id='input-description'
                          className='control-input'
                          disabled={!this.state.more}
                          value={this.state.meta.description}
                          onChange={event => {
                            this.updateMeta('description', event.target.value)
                          }}
                        />
                      </div>
                      <div className='control'>
                        <label htmlFor='input-packageName'>Package Name</label>
                        <input
                          type='text'
                          id='input-packageName'
                          className='control-input'
                          disabled={!this.state.more}
                          value={this.state.meta.packageName}
                          onChange={event => {
                            this.updateMeta('packageName', event.target.value)
                          }}
                        />
                      </div>
                      <div className='control'>
                        <label>Packaging</label>
                        <div>
                          <RadioGroup
                            name='packaging'
                            disabled={!this.state.more}
                            selected={this.state.meta.packaging}
                            options={this.lists.meta.packaging}
                            onChange={value => {
                              this.updateMeta('packaging', value)
                            }}
                          />
                        </div>
                      </div>
                      <div className='control'>
                        <label>Java</label>
                        <div>
                          <RadioGroup
                            name='java'
                            disabled={!this.state.more}
                            selected={this.state.meta.java}
                            options={this.lists.meta.java}
                            onChange={value => {
                              this.updateMeta('java', value)
                            }}
                          />
                        </div>
                      </div>
                    </div>
                  </div>
                </>
              ) : (
                <div>
                  <div className='control'>
                    <Placeholder type='input' />
                  </div>
                  <div className='control'>
                    <Placeholder type='input' />
                  </div>
                  <div className='control'>
                    <Placeholder type='dropdown' />
                  </div>
                </div>
              )}
            </div>
          </div>
          <div className='colset'>
            <div className='left'>
              <div className='sticky-label'>Dependencies</div>
            </div>
            <div
              className={`dependencies-box ${
                this.state.tab === 'list' ? 'large' : ''
              }`}
            >
              {get(this.state, 'complete') ? (
                <>
                  <div className='tab'>
                    <div className='tab-container'>
                      <a
                        href='/'
                        aria-label='Search'
                        onClick={event => {
                          event.preventDefault()
                          this.updateTab('quick-search')
                        }}
                        className={`quick-search ${
                          this.state.tab === 'quick-search' ? 'active' : ''
                        }`}
                      >
                        <IconSearch />
                      </a>
                      <a
                        href='/'
                        aria-label='List'
                        onClick={event => {
                          event.preventDefault()
                          this.updateTab('list')
                        }}
                        className={`list ${
                          this.state.tab === 'list' ? 'active' : ''
                        }`}
                      >
                        <IconList />
                      </a>
                      {selected > 0 && (
                        <>
                          <strong>
                            <span>{selected}</span> selected
                          </strong>
                        </>
                      )}
                    </div>
                  </div>
                  {this.state.tab === 'quick-search' ? (
                    <div className='colset-2'>
                      <div className='column'>
                        <label
                          className='search-label'
                          htmlFor='input-quicksearch'
                        >
                          Search dependencies to add
                        </label>
                        <Typehead
                          boot={this.state.boot}
                          add={this.dependencyAdd}
                          submit={this.onSubmit}
                          options={this.lists.dependencies}
                          exclude={this.state.dependencies}
                        />
                      </div>
                      <div className='column'>
                        <label>Selected dependencies</label>
                        {this.state.dependencies.length > 0 ? (
                          <>
                            <List
                              boot={this.state.boot}
                              remove={this.dependencyRemove}
                              list={this.state.dependencies}
                            />
                          </>
                        ) : (
                          <div className='search-no-selected'>
                            No dependency selected
                          </div>
                        )}
                      </div>
                    </div>
                  ) : (
                    <CheckboxList
                      boot={this.state.boot}
                      add={this.dependencyAdd}
                      remove={this.dependencyRemove}
                      list={this.lists.dependencies}
                      checked={this.state.dependencies}
                      stateGroups={this.state.groups}
                      toggleGroup={this.toggleGroup}
                    />
                  )}
                </>
              ) : (
                <Placeholder type='tabs' count={2} />
              )}
            </div>
          </div>

          <div className='sticky'>
            <div className='colset colset-submit'>
              <div className='left nopadding'>
                <Footer />
              </div>
              <div className='right nopadding'>
                <div className='submit'>
                  {get(this.state, 'complete') ? (
                    <>
                      <button
                        className='button primary'
                        type='submit'
                        id='generate-project'
                      >
                        Generate{' '}
                        <span className='desktop-only'>
                          - {this.state.symb} + ⏎
                        </span>
                      </button>
                      <button
                        className='button'
                        type='button'
                        onClick={this.onExplore}
                        id='explore-project'
                      >
                        Explore{' '}
                        <span className='desktop-only'>- Ctrl + Space</span>
                      </button>

                      <span className='share-area'>
                        <button
                          className={`button  ${
                            this.state.share ? 'primary' : ''
                          }`}
                          type='button'
                          onClick={this.onShare}
                          id='share-project'
                        >
                          Share...
                        </button>
                        <SharePopup
                          open={this.state.share}
                          onClose={this.onShareClose}
                          rootValues={this.state.rootValues}
                          properties={{
                            language: this.state.language,
                            project: this.state.project,
                            boot: this.state.boot,
                            meta: this.state.meta,
                            dependencies: this.state.dependencies,
                          }}
                        />
                        <ShareOverlay open={this.state.share} />
                      </span>
                    </>
                  ) : (
                    <>
                      <Placeholder type='button' width='189px' />
                      <Placeholder type='button' width='212px' />
                      <Placeholder type='button' width='110px' />
                    </>
                  )}
                </div>
              </div>
            </div>
          </div>
        </form>

        <ExploreModal
          open={this.state.explore}
          onClose={this.onExploreClose}
          onSelected={this.onSelectedFile}
          tree={get(this.state, 'tree')}
          selected={get(this.state, 'file')}
          projectName={get(this.state, 'projectName')}
          blob={get(this.state, 'blob')}
        />
      </Layout>
    )
  }
}

export const jsonObject = graphql`
  query {
    site: allSite {
      edges {
        node {
          id
          siteMetadata {
            title
            description
            twitter
            canonical
            author
            image
            apiUrl
            apiZip
          }
        }
      }
    }
  }
`

export default IndexPage
