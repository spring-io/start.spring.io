import FileSaver from 'file-saver'
import React from 'react'
import get from 'lodash.get'
import queryString from 'query-string'
import querystring from 'querystring'
import set from 'lodash.set'
import { GlobalHotKeys } from 'react-hotkeys'
import { ToastContainer, toast } from 'react-toastify'
import { graphql } from 'gatsby'

import { CheckboxList } from '../components/common/checkbox-list'
import { Footer, Header, Layout, Loader } from '../components/common/layout'
import {
  IconChevronRight,
  IconList,
  IconSearch,
} from '../components/common/icons'
import { List, RadioGroup } from '../components/common/form'
import { Meta } from '../components/common/meta'
import { Typehead } from '../components/common/typehead'

class IndexPage extends React.Component {
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
      deps.push(
        ...group.values.map(item => ({
          id: `${get(item, 'id', '')}`,
          name: `${get(item, 'name', '')}`,
          group: `${group.name}`,
          weight: 0,
          description: `${get(item, 'description', '')}`,
          versionRange: `${get(item, 'versionRange', '')}`,
          versionRequirement: `${get(item, 'versionRange', '')}`,
        }))
      )
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
    this.keyMap = {
      SUBMIT: ['command+enter', 'ctrl+enter'],
    }
    const submit = this.onSubmit
    this.handlers = {
      SUBMIT: event => {
        submit(event)
      },
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
                set(this.state, key, value)
              }
              break
            default:
              set(this.state, key, value)
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
      ...values,
    })
  }

  componentDidMount() {
    fetch('http://localhost:8080/', {
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

  onSubmit = event => {
    event.preventDefault()
    const { project, language, boot, meta, dependencies } = this.state
    const apiUrl = this.props.data.site.edges[0].node.siteMetadata.apiUrl
    const url = `${apiUrl}starter.zip`
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
    const paramsDependencies = dependencies
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
    if (!get(this.state, 'complete')) {
      return (
        <div className='loading-page'>
          <Header />
          <div className='text'>
            <Loader />
            loading ...
          </div>
        </div>
      )
    }
    return (
      <Layout>
        <GlobalHotKeys keyMap={this.keyMap} handlers={this.handlers} />
        <Meta />
        <ToastContainer position='top-center' hideProgressBar />
        <form onSubmit={this.onSubmit} autocomplete='off'>
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
              <RadioGroup
                name='project'
                selected={this.state.project}
                options={this.lists.project}
                onChange={value => {
                  this.setState({ project: value })
                }}
              />
            </div>
          </div>
          <div className='colset'>
            <div className='left'>Language</div>
            <div className='right'>
              <RadioGroup
                name='language'
                selected={this.state.language}
                onChange={value => {
                  this.setState({ language: value })
                }}
                options={this.lists.language}
              />
            </div>
          </div>
          <div className='colset'>
            <div className='left'>Spring Boot</div>
            <div className='right'>
              <RadioGroup
                name='boot'
                selected={this.state.boot}
                options={this.lists.boot}
                onChange={value => {
                  this.setState({ boot: value })
                }}
              />
            </div>
          </div>
          <div className='colset'>
            <div className='left'>Project Metadata</div>
            <div className='right right-md'>
              <div className='control'>
                <label>Group</label>
                <input
                  type='text'
                  className='control-input'
                  value={this.state.meta.group}
                  onChange={event => {
                    this.updateMetaState('group', event.target.value)
                  }}
                />
              </div>
              <div className='control'>
                <label>Artifact</label>
                <input
                  type='text'
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

              <div className={`panel ${this.state.more ? 'panel-active' : ''}`}>
                <div className='panel-wrap'>
                  <div className='control'>
                    <label>Name</label>
                    <input
                      type='text'
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
                    <label>Description</label>
                    <input
                      type='text'
                      className='control-input'
                      disabled={!this.state.more}
                      value={this.state.meta.description}
                      onChange={event => {
                        this.updateMetaState('description', event.target.value)
                      }}
                    />
                  </div>
                  <div className='control'>
                    <label>Package Name</label>
                    <input
                      type='text'
                      className='control-input'
                      disabled={!this.state.more}
                      value={this.state.meta.packageName}
                      onChange={event => {
                        this.updateMetaState('packageName', event.target.value)
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
            </div>
          </div>

          <div className='colset'>
            <div className='left'>
              <div className='sticky-label'>Dependencies</div>
            </div>
            <div className='dependencies-box'>
              <div className='tab'>
                <div className='tab-container'>
                  <a
                    href='/'
                    onClick={event => {
                      event.preventDefault()
                      this.setTab('quick-search')
                    }}
                    className={`${
                      this.state.tab === 'quick-search' ? 'active' : ''
                    }`}
                  >
                    <IconSearch />
                  </a>
                  <a
                    href='/'
                    onClick={event => {
                      event.preventDefault()
                      this.setTab('list')
                    }}
                    className={`${this.state.tab === 'list' ? 'active' : ''}`}
                  >
                    <IconList />
                  </a>
                  {this.state.dependencies.length > 0 && (
                    <>
                      <strong>
                        <span>{this.state.dependencies.length}</span> selected
                      </strong>
                    </>
                  )}
                </div>
              </div>

              {this.state.tab === 'quick-search' ? (
                <div className='colset-2'>
                  <div className='column'>
                    <label className='search-label'>
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
                />
              )}
            </div>
          </div>

          <div className='sticky'>
            <div className='colset'>
              <div className='left nopadding'>
                <Footer />
              </div>
              <div className='right nopadding'>
                <div className='submit'>
                  <button
                    className='button primary'
                    type='submit'
                    id='generate-project'
                  >
                    Generate the project - ⌘ + ⏎
                  </button>
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
          }
        }
      }
    }
  }
`

export default IndexPage
