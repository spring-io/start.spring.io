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
import { DependencyDialog } from './common/dependency'
import { Fields, Loading } from './common/builder'
import { Form } from './common/form'
import { Header, SideLeft, SideRight } from './common/layout'
import { InitializrContext } from './reducer/Initializr'
import { getConfig, getInfo, getProject } from './utils/ApiUtils'

const Explore = lazy(() => import('./common/explore/Explore.js'))
const Share = lazy(() => import('./common/share/Share.js'))
const HotKeys = lazy(() => import('./common/builder/HotKeys.js'))

export default function Application() {
  const {
    complete,
    dispatch,
    theme,
    share: shareOpen,
    explore: exploreOpen,
    dependencies,
  } = useContext(AppContext)
  const { values, share, dispatch: dispatchInitializr } = useContext(
    InitializrContext
  )

  const [blob, setBlob] = useState(null)

  const buttonExplore = useRef(null)
  const buttonDependency = useRef(null)
  const buttonSubmit = useRef(null)

  const windowsUtils = useWindowsUtils()
  useHash()

  useEffect(() => {
    if (windowsUtils.origin) {
      const url = `${windowsUtils.origin}/metadata/client`
      getInfo(url).then(jsonConfig => {
        const response = getConfig(jsonConfig)
        setTimeout(() => {
          dispatchInitializr({ type: 'COMPLETE', payload: { ...response } })
          dispatch({ type: 'COMPLETE', payload: response })
        }, 1000)
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
    dispatch({ type: 'UPDATE', payload: { explore: true } })
    const project = await getProject(
      url,
      values,
      get(dependencies, 'list')
    ).catch(() => {
      toast.error(`Could not connect to server. Please check your network.`)
    })
    setBlob(project)
  }

  const onShare = () => {
    dispatch({ type: 'UPDATE', payload: { share: true } })
  }

  const onEscape = () => {
    setBlob(null)
    dispatch({
      type: 'UPDATE',
      payload: { list: false, share: false, explore: false, nav: false },
    })
  }

  return (
    <>
      <BodyClassName className={theme} />
      <Suspense fallback=''>
        <HotKeys
          onSubmit={() => {
            if (get(buttonSubmit, 'current')) {
              buttonSubmit.current.click()
            }
          }}
          onExplore={() => {
            if (get(buttonExplore, 'current')) {
              buttonExplore.current.click()
            }
          }}
          onDependency={() => {
            if (get(buttonDependency, 'current')) {
              buttonDependency.current.click()
            }
          }}
          onEscape={onEscape}
        />
      </Suspense>
      <SideLeft />
      <div id='main'>
        <Header />
        <hr className='divider' />
        <Form onSubmit={onSubmit}>
          {!complete ? (
            <Loading />
          ) : (
            <>
              <Fields
                onSubmit={onSubmit}
                onShare={onShare}
                onExplore={onExplore}
                refExplore={buttonExplore}
                refSubmit={buttonSubmit}
                refDependency={buttonDependency}
              />
              <DependencyDialog onClose={onEscape} />
            </>
          )}
        </Form>
      </div>
      <SideRight />
      <Suspense fallback=''>
        <Share open={shareOpen || false} shareUrl={share} onClose={onEscape} />
        <Explore
          projectName={`${get(values, 'meta.artifact')}.zip`}
          blob={blob}
          open={exploreOpen || false}
          onClose={onEscape}
        />
      </Suspense>
    </>
  )
}
