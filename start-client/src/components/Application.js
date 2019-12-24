import BodyClassName from 'react-body-classname'
import FileSaver from 'file-saver'
import get from 'lodash.get'
import React, {
  Suspense,
  lazy,
  useContext,
  useEffect,
  useRef,
  useState,
} from 'react'
import { toast } from 'react-toastify'

import useHash from './utils/Hash'
import useWindowsUtils from './utils/WindowsUtils'
import { AppContext } from './reducer/App'
import { Button, Form, RadioGroup } from './common/form'
import {
  Control,
  FieldError,
  FieldInput,
  FieldRadio,
  List,
  Loading,
  PanelMore,
  QuickSearch,
  Tabs,
  Warnings,
} from './common/builder'
import { Footer, Layout } from './common/layout'
import { InitializrContext } from './reducer/Initializr'
import { getConfig, getInfo, getProject } from './utils/ApiUtils'

const Explore = lazy(() => import('./common/explore/Explore.js'))
const Share = lazy(() => import('./common/share/Share.js'))
const HotKeys = lazy(() => import('./common/builder/HotKeys.js'))
const Rudolph = lazy(() => import('./endofyear/Rudolph.js'))
const Bell = lazy(() => import('./endofyear/Bell.js'))

export default function Application() {
  const {
    complete,
    config,
    more,
    tab,
    dispatch,
    theme,
    share: shareOpen,
    explore: exploreOpen,
    dependencies,
  } = useContext(AppContext)
  const { values, share, dispatch: dispatchInitializr, errors } = useContext(
    InitializrContext
  )

  const [positionShare, setPositionShare] = useState({ x: 0, y: 0 })
  const [hover, setHover] = useState(false)

  const [blob, setBlob] = useState(null)

  const inputMore = useRef(null)
  const inputQuickSearch = useRef(null)

  const windowsUtils = useWindowsUtils()
  useHash()

  useEffect(() => {
    if (windowsUtils.origin) {
      const url = `${windowsUtils.origin}/metadata/client`
      getInfo(url).then(jsonConfig => {
        const response = getConfig(jsonConfig)
        dispatchInitializr({ type: 'COMPLETE', payload: { ...response } })
        dispatch({ type: 'COMPLETE', payload: response })
      })
    }
  }, [dispatch, dispatchInitializr, windowsUtils.origin])

  const onSubmit = async () => {
    const url = `${windowsUtils.origin}/starter.zip`
    const project = await getProject(
      url,
      values,
      get(dependencies, 'list')
    ).catch(() => {
      toast.error(`Could not connect to server. Please check your network.`)
    })
    FileSaver.saveAs(project, `${get(values, 'meta.artifact')}.zip`)
  }

  const onExplore = async () => {
    const url = `${windowsUtils.origin}/starter.zip`
    dispatch({ type: 'EXPLORE_UPDATE', payload: { open: true } })
    const project = await getProject(
      url,
      values,
      get(dependencies, 'list')
    ).catch(() => {
      toast.error(`Could not connect to server. Please check your network.`)
    })
    setBlob(project)
  }

  const onShare = event => {
    const { x, y } = get(event, 'target').getBoundingClientRect()
    setPositionShare({ x, y })
    dispatch({ type: 'SHARE_UPDATE', payload: { open: true } })
  }

  const update = args => {
    dispatchInitializr({ type: 'UPDATE', payload: args })
  }

  return (
    <Layout>
      <BodyClassName className={theme} />
      <Suspense fallback=''>
        <HotKeys onSubmit={onSubmit} onExplore={onExplore} />
      </Suspense>
      <Form onSubmit={onSubmit}>
        {!complete ? (
          <Loading />
        ) : (
          <>
            <Warnings />
            <Control text='Project'>
              <RadioGroup
                name='project'
                selected={get(values, 'project')}
                options={get(config, 'lists.project')}
                onChange={value => {
                  update({ project: value })
                }}
              />
            </Control>
            <Control text='Language'>
              <RadioGroup
                name='language'
                selected={get(values, 'language')}
                options={get(config, 'lists.language')}
                onChange={value => {
                  update({ language: value })
                }}
              />
            </Control>
            <Control text='Spring Boot'>
              <RadioGroup
                name='boot'
                selected={get(values, 'boot')}
                error={get(errors, 'boot.value', '')}
                options={get(config, 'lists.boot')}
                onChange={value => {
                  dispatchInitializr({
                    type: 'UPDATE',
                    payload: { boot: value },
                    config: get(dependencies, 'list'),
                  })
                  dispatch({
                    type: 'UPDATE_DEPENDENCIES',
                    payload: { boot: value },
                  })
                }}
              />
              {get(errors, 'boot') && (
                <FieldError>
                  Spring Boot {get(errors, 'boot.value')} is not supported.
                  Please select a valid version
                </FieldError>
              )}
            </Control>
            <Control text='Project Metadata' variant='md'>
              <FieldInput
                id='input-group'
                value={get(values, 'meta.group')}
                text='Group'
                onChange={event => {
                  update({ meta: { group: event.target.value } })
                }}
              />
              <FieldInput
                id='input-artifact'
                value={get(values, 'meta.artifact')}
                text='Artifact'
                onChange={event => {
                  update({ meta: { artifact: event.target.value } })
                }}
              />
              <PanelMore fieldFocusOnOpen={inputMore}>
                <FieldInput
                  id='input-name'
                  value={get(values, 'meta.name')}
                  text='Name'
                  disabled={!more}
                  inputRef={inputMore}
                  onChange={event => {
                    update({ meta: { name: event.target.value } })
                  }}
                />
                <FieldInput
                  id='input-description'
                  value={get(values, 'meta.description')}
                  text='Description'
                  disabled={!more}
                  onChange={event => {
                    update({ meta: { description: event.target.value } })
                  }}
                />
                <FieldInput
                  id='input-packageName'
                  value={get(values, 'meta.packageName')}
                  text='Package name'
                  disabled={!more}
                  onChange={event => {
                    update({ meta: { packageName: event.target.value } })
                  }}
                />
                <FieldRadio
                  id='input-packaging'
                  value={get(values, 'meta.packaging')}
                  text='Packaging'
                  disabled={!more}
                  options={get(config, 'lists.meta.packaging')}
                  onChange={value => {
                    update({ meta: { packaging: value } })
                  }}
                />
                <FieldRadio
                  id='input-java'
                  value={get(values, 'meta.java')}
                  text='Java'
                  disabled={!more}
                  options={get(config, 'lists.meta.java')}
                  onChange={value => {
                    update({ meta: { java: value } })
                  }}
                />
              </PanelMore>
            </Control>
            <Control
              text='Dependencies'
              variant={tab === 'quicksearch' ? 'xl' : 'xxl'}
            >
              <Suspense fallback=''>
                <Bell />
              </Suspense>
              <Tabs
                changeTab={newTab => {
                  if (
                    newTab === 'quicksearch' &&
                    get(inputQuickSearch, 'current')
                  ) {
                    get(inputQuickSearch, 'current').focus()
                  }
                }}
              />
              {tab === 'quicksearch' && (
                <QuickSearch submit={onSubmit} input={inputQuickSearch} />
              )}
              {tab === 'list' && <List />}
            </Control>
            <Footer>
              <div className={`rudolph-wrapper ${hover ? 'active' : ''}`}>
                <Suspense fallback=''>
                  <Rudolph />
                </Suspense>
              </div>
              <Button
                onMouseMove={() => {
                  setHover(true)
                }}
                onMouseOut={() => {
                  setHover(false)
                }}
                onBlur={() => {
                  setHover(false)
                }}
                onFocus={() => {
                  setHover(true)
                }}
                id='generate-project'
                variant='primary'
                onClick={onSubmit}
                hotkey={`${windowsUtils.symb} + ⏎`}
              >
                Generate
              </Button>
              <Button
                id='explore-project'
                onClick={onExplore}
                hotkey='Ctrl + Space'
              >
                Explore
              </Button>
              <Button id='share-project' onClick={onShare}>
                Share...
              </Button>
            </Footer>
          </>
        )}
      </Form>
      <Suspense fallback=''>
        <Share
          open={shareOpen || false}
          shareUrl={share}
          position={positionShare}
          onClose={() => {
            dispatch({
              type: 'SHARE_UPDATE',
              payload: { open: false },
            })
          }}
        />
        <Explore
          projectName={`${get(values, 'meta.artifact')}.zip`}
          blob={blob}
          open={exploreOpen || false}
          onClose={() => {
            dispatch({
              type: 'EXPLORE_UPDATE',
              payload: { open: false },
            })
            setBlob(null)
          }}
        />
      </Suspense>
    </Layout>
  )
}
