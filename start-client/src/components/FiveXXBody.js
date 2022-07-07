import BodyClassName from 'react-body-classname'
import { AppContext } from './reducer/App'
import React, {
  useContext,
} from 'react'
import { Header, SideLeft, SideRight } from './common/layout'

export default function FiveXXBody() {
  const {
    theme,
  } = useContext(AppContext)

  return (
    <>
      <BodyClassName className={theme} />
      <SideLeft />
      <div id='main'>
        <Header />
        <hr className='divider' />
     
        <span class="label">5xx: Server error responses</span>
   
      </div>
      <SideRight />
    </>
  )
}
