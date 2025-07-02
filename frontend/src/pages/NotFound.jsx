import React from 'react'
import LetterGlitch from '../components/LetterGlitch';

const NotFound = () => {
  return (
    <div>
  <h1 className='align-middle justify-center'>404 Page not found</h1>
  <LetterGlitch
    glitchSpeed={50}
    centerVignette={true}
    outerVignette={false}
    smooth={true}
  />
    </div>
  )
}

export default NotFound
