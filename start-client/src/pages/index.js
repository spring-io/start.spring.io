import FileSaver from 'file-saver'
import React from 'react'
import get from 'lodash.get'
import queryString from 'query-string'
import querystring from 'querystring'
import set from 'lodash.set'
import { GlobalHotKeys } from 'react-hotkeys'
import { ToastContainer, toast } from 'react-toastify'
import { graphql } from 'gatsby'

import META_EXTEND from '../data/meta-extend.json'
import { CheckboxList } from '../components/common/checkbox-list'
import { Footer, Header, Layout, Loader } from '../components/common/layout'
import {
  IconChevronRight,
  IconList,
  IconSearch,
} from '../components/common/icons'
import { List, Placeholder, RadioGroup } from '../components/common/form'
import { Meta } from '../components/common/meta'
import { Typehead } from '../components/common/typehead'
import { isInRange } from '../components/utils/versions'

const WEIGHT_DEFAULT = 50

class IndexPage extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      complete: false,
      tab: 'quick-search',
      more: false,
      error: false,
      symb: 'alt',
      groups: {},
    }

    this.keyMap = {
      SUBMIT: ['command+enter', 'ctrl+enter'],
    }
    const submit = this.onSubmit
    this.handlers = {
      SUBMIT: event => {
        submit(event)
      },
    }
  }
  onComplete = json => {
    const values = {
      project: get(json, 'type.default'),
      language: get(json, 'language.default'),
      boot: get(json, 'bootVersion.default'),
      meta: {
        name: get(json, 'name.default'),
        group: get(json, 'groupId.default'),
        artifact: get(json, 'artifactId.default'),
        description: get(json, 'description.default'),
        packaging: get(json, 'packaging.default'),
        packageName: get(json, 'packageName.default'),
        java: get(json, 'javaVersion.default'),
      },
    }
    const deps = []
    get(json, 'dependencies.values', []).forEach(group => {
      group.values.forEach(item => {
        const metaExtend = META_EXTEND.find(meta => get(meta, 'id') === item.id)
        const val = {
          id: `${get(item, 'id', '')}`,
          name: `${get(item, 'name', '')}`,
          group: `${group.name}`,
          weight: get(metaExtend, `weight`, WEIGHT_DEFAULT),
          description: `${get(item, 'description', '')}`,
          versionRange: `${get(item, 'versionRange', '')}`,
          versionRequirement: `${get(item, 'versionRange', '')}`,
        }
        deps.push(val)
      })
    })
    this.lists = {
      project: get(json, 'type.values', [])
        .filter(type => type.action === '/starter.zip')
        .map(type => ({
          key: `${type.id}`,
          text: `${type.name}`,
        })),
      language: get(json, 'language.values', []).map(language => ({
        key: `${language.id}`,
        text: `${language.name}`,
      })),
      boot: get(json, 'bootVersion.values', []).map(boot => ({
        key: `${boot.id}`,
        text: `${boot.name}`,
      })),
      meta: {
        java: get(json, 'javaVersion.values', []).map(java => ({
          key: `${java.id}`,
          text: `${java.name}`,
        })),
        packaging: get(json, 'packaging.values', []).map(packaging => ({
          key: `${packaging.id}`,
          text: `${packaging.name}`,
        })),
      },
      dependencies: deps,
    }

    // Parsing parameters URL (search or hash)
    if (this.props.location.search || this.props.location.hash) {
      let queryParams = queryString.parse(this.props.location.search)
      if (this.props.location.hash) {
        let hash = this.props.location.hash.substr(2)
        queryParams = queryString.parse(`?${hash}`)
      }
      const params = {
        type: 'project',
        language: 'language',
        boot: 'boot',
        packaging: 'meta.packaging',
        javaVersion: 'meta.java',
        groupId: 'meta.group',
        artifactId: 'meta.artifact',
        name: 'meta.name',
        description: 'meta.description',
        packageName: 'meta.packageName',
      }

      Object.keys(queryParams).forEach(entry => {
        const key = get(params, entry)
        const value = get(queryParams, entry).toLowerCase()
        if (key) {
          switch (key) {
            case 'project':
            case 'language':
            case 'boot':
            case 'meta.packaging':
            case 'meta.java':
              const vals = get(this.lists, key, [])
              if (vals.find(a => a.key === value)) {
                set(values, key, value)
              }
              break
            default:
              set(values, key, value)
          }
        }
      })
    }

    this.setState({
      complete: true,
      dependencies: [],
      tab: 'quick-search',
      more: false,
      error: false,
      groups: {},
      ...values,
    })
  }

  componentDidMount() {
    const apiUrl = this.props.data.site.edges[0].node.siteMetadata.apiUrl
    if (window.navigator.userAgent.toLowerCase().indexOf('mac') > -1) {
      this.setState({ symb: '⌘' })
    }
    fetch(`${apiUrl}`, {
      method: 'GET',
      headers: {
        Accept: 'application/vnd.initializr.v2.1+json',
      },
    })
      .then(
        response => response.json(),
        error => {
          this.setState({ error: true })
          return null
        }
      )
      .then(data => {
        if (data) {
          this.onComplete(data)
        }
      })
  }

  dependencyAdd = dependency => {
    this.setState({ dependencies: [...this.state.dependencies, dependency] })
  }

  dependencyRemove = dependency => {
    this.setState({
      dependencies: this.state.dependencies.filter(
        item => dependency.name !== item.name
      ),
    })
  }

  toggle = event => {
    event.preventDefault()
    this.setState({ more: !this.state.more })

    if (!this.state.more) {
      setTimeout(() => {
        this.inputMetaName && this.inputMetaName.focus()
      }, 300)
    }
  }

  setTab = tab => {
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

  updateMetaState = (prop, value) => {
    let meta = { ...this.state.meta }
    meta[prop] = value
    if (prop === 'artifact') {
      set(meta, 'name', get(meta, 'artifact'))
      set(meta, 'packageName', `${get(meta, 'group')}.${get(meta, 'artifact')}`)
    }
    if (prop === 'group') {
      set(meta, 'packageName', `${get(meta, 'group')}.${get(meta, 'artifact')}`)
    }
    this.setState({ meta: meta })
  }

  getValidDependencies = () => {
    const boot = get(this.state, 'boot')
    const dependencies = get(this.state, 'dependencies', [])
    return dependencies
      .map(dep => {
        const compatibility = dep.versionRange
          ? isInRange(boot, dep.versionRange)
          : true
        if (!compatibility) {
          return null
        }
        return dep
      })
      .filter(d => !!d)
  }

  toggleGroup = group => {
    const val = get(this.state, 'groups')
    val[group] = !get(val, `${group}`, true)
    this.setState({ groups: val })
  }

  onSubmit = event => {
    event.preventDefault()
    const { project, language, boot, meta } = this.state
    const apiZip = this.props.data.site.edges[0].node.siteMetadata.apiZip
    const url = `${apiZip}`
    const params = querystring.stringify({
      type: project,
      language: language,
      bootVersion: boot,
      baseDir: meta.artifact,
      groupId: meta.group,
      artifactId: meta.artifact,
      name: meta.name,
      description: meta.description,
      packageName: meta.packageName,
      packaging: meta.packaging,
      javaVersion: meta.java,
    })
    const paramsDependencies = this.getValidDependencies()
      .map(dep => `&style=${dep.id}`)
      .join('')
    fetch(`${url}?${params}${paramsDependencies}`, { method: 'GET' })
      .then(
        function(response) {
          if (response.status === 200) {
            return response.blob()
          }
          return null
        },
        function(error) {
          return null
        }
      )
      .then(function(blob) {
        if (!blob) {
          toast.error('The server API is not available.')
          return
        }
        const fileName = `${meta.artifact}.zip`
        FileSaver.saveAs(blob, fileName)
        toast.success('Your project has been generated with success.')
      })
  }

  render() {
    if (get(this.state, 'error')) {
      return (
        <div className='error-page'>
          <Header />
          <div className='text'>
            <p>The service is temporarily unavailable.</p>
            <p>
              <a href='/'>Refresh the page</a>
            </p>
          </div>
        </div>
      )
    }
    const selected = get(this.getValidDependencies(), 'length', 0)
    return (
      <Layout>
        <GlobalHotKeys keyMap={this.keyMap} handlers={this.handlers} />
        <Meta />
        <ToastContainer position='top-center' hideProgressBar />
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
          <div className='colset'>
            <div className='left'>Project</div>
            <div className='right'>
              {get(this.state, 'complete') ? (
                <RadioGroup
                  name='project'
                  selected={this.state.project}
                  options={this.lists.project}
                  onChange={value => {
                    this.setState({ project: value })
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
                    this.setState({ language: value })
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
                <RadioGroup
                  name='boot'
                  selected={this.state.boot}
                  options={this.lists.boot}
                  onChange={value => {
                    this.setState({ boot: value })
                  }}
                />
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
                        this.updateMetaState('group', event.target.value)
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
                        this.updateMetaState('artifact', event.target.value)
                      }}
                    />
                  </div>

                  <div className='more'>
                    <div className='wrap'>
                      <a
                        href='/'
                        onClick={this.toggle}
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
                            this.updateMetaState('name', event.target.value)
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
                            this.updateMetaState(
                              'description',
                              event.target.value
                            )
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
                            this.updateMetaState(
                              'packageName',
                              event.target.value
                            )
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
                              this.updateMetaState('packaging', value)
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
                              this.updateMetaState('java', value)
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
                          this.setTab('quick-search')
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
                          this.setTab('list')
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
                    <button
                      className='button primary'
                      type='submit'
                      id='generate-project'
                    >
                      Generate the project - {this.state.symb} + ⏎
                    </button>
                  ) : (
                    <Placeholder type='button' width='267px' />
                  )}
                </div>
              </div>
            </div>
          </div>
        </form>
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
