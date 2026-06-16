import get from 'lodash/get'
import React, {useCallback, useContext, useEffect, useMemo, useRef, useState} from 'react'
import {CSSTransition, TransitionGroup} from 'react-transition-group'
import {clearAllBodyScrollLocks, disableBodyScroll} from 'body-scroll-lock'
import CopyToClipboard from 'react-copy-to-clipboard'

import starters from './starters.json'
import {AppContext} from '../../reducer/App'
import {InitializrContext} from '../../reducer/Initializr'
import {IconSearch, IconTimes} from '../icons'

// ─── Snippet helpers ─────────────────────────────────────────────────────────

function buildMavenSnippet(groupId, artifactId) {
  return `<dependency>
  <groupId>${groupId}</groupId>
  <artifactId>${artifactId}</artifactId>
</dependency>`
}

function buildGradleSnippet(groupId, artifactId) {
  return `implementation '${groupId}:${artifactId}'`
}

// ─── All unique tags ──────────────────────────────────────────────────────────

const ALL_TAGS = ['All', ...Array.from(
  starters.reduce((acc, s) => {
    s.tags.forEach(t => acc.add(t))
    return acc
  }, new Set())
).sort()]

// ─── StarterCard ─────────────────────────────────────────────────────────────

function StarterCard({starter}) {
  const [tab, setTab] = useState('maven')
  const [copied, setCopied] = useState(false)

  const mavenSnippet = useMemo(
    () => buildMavenSnippet(starter.groupId, starter.artifactId),
    [starter.groupId, starter.artifactId]
  )
  const gradleSnippet = useMemo(
    () => buildGradleSnippet(starter.groupId, starter.artifactId),
    [starter.groupId, starter.artifactId]
  )
  const activeSnippet = tab === 'maven' ? mavenSnippet : gradleSnippet

  const onCopy = useCallback(() => {
    setCopied(true)
    setTimeout(() => setCopied(false), 1500)
  }, [])

  return (
    <div className='community-card'>
      <div className='community-card-name'>{starter.name}</div>
      <p className='community-card-description'>{starter.description}</p>
      <div className='community-card-tags'>
        {starter.tags.map(tag => (
          <span key={tag}>{tag}</span>
        ))}
      </div>
      <div className='community-card-snippet'>
        <div className='snippet-tabs'>
          <button
            type='button'
            className={`snippet-tab${tab === 'maven' ? ' active' : ''}`}
            onClick={() => setTab('maven')}
          >
            Maven
          </button>
          <button
            type='button'
            className={`snippet-tab${tab === 'gradle' ? ' active' : ''}`}
            onClick={() => setTab('gradle')}
          >
            Gradle
          </button>
        </div>
        <div className='snippet-content'>
          {activeSnippet}
          <CopyToClipboard text={activeSnippet} onCopy={onCopy}>
            <button type='button' className='snippet-copy'>
              {copied ? 'Copied!' : 'Copy'}
            </button>
          </CopyToClipboard>
        </div>
      </div>
      <div className='community-card-links'>
        <a
          href={starter.docUrl}
          target='_blank'
          rel='noopener noreferrer'
        >
          Documentation
        </a>
        <a
          href={starter.repoUrl}
          target='_blank'
          rel='noopener noreferrer'
        >
          Repository
        </a>
      </div>
    </div>
  )
}

// ─── Modal ────────────────────────────────────────────────────────────────────

function Modal({open, onClose}) {
  const wrapper = useRef(null)
  const searchRef = useRef(null)
  const [query, setQuery] = useState('')
  const [activeTag, setActiveTag] = useState('All')
  const [whyOpen, setWhyOpen] = useState(false)

  const {dispatch} = useContext(AppContext)
  const {values} = useContext(InitializrContext)
  const bootVersion = get(values, 'boot', '')

  // Close on outside click
  useEffect(() => {
    const clickOutside = event => {
      const children = get(wrapper, 'current')
      if (children && !children.contains(event.target)) {
        onClose()
      }
    }
    document.addEventListener('mousedown', clickOutside)
    return () => {
      document.removeEventListener('mousedown', clickOutside)
    }
  }, [onClose])

  // Scroll lock
  useEffect(() => {
    if (get(wrapper, 'current') && open) {
      disableBodyScroll(get(wrapper, 'current'))
    }
    return () => {
      clearAllBodyScrollLocks()
    }
  }, [wrapper, open])

  // Focus search on open
  useEffect(() => {
    if (open && get(searchRef, 'current')) {
      searchRef.current.focus()
    }
    if (!open) {
      setQuery('')
      setActiveTag('All')
      setWhyOpen(false)
    }
  }, [open])

  const filteredStarters = useMemo(() => {
    const q = query.trim().toLowerCase()
    return starters.filter(s => {
      const matchesTag = activeTag === 'All' || s.tags.includes(activeTag)
      if (!q) return matchesTag
      const matchesQuery =
        s.name.toLowerCase().includes(q) ||
        s.description.toLowerCase().includes(q) ||
        s.groupId.toLowerCase().includes(q) ||
        s.artifactId.toLowerCase().includes(q) ||
        s.tags.some(t => t.toLowerCase().includes(q))
      return matchesTag && matchesQuery
    })
  }, [query, activeTag])

  return (
    <TransitionGroup component={null}>
      {open && (
        <CSSTransition classNames='community' timeout={300}>
          <div className='modal-community'>
            <div className='modal-community-container' ref={wrapper}>
              <div className='modal-header'>
                <h1>Community Starters</h1>
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
              <div className='modal-content'>
                {/* Version-aware notice — directly addresses maintainer concern */}
                <div className='community-notice'>
                  <div className='community-notice-boot'>
                    Spring Boot version
                    <span className='boot-badge'>{bootVersion}</span>
                  </div>
                  <p>
                    Additional community-maintained starters are not part of the curated Spring Initializr dependency catalog.
                    <br /><br />
                    Community starters are independently maintained by their respective
                    projects. Check each project&apos;s documentation to verify
                    compatibility with your selected Spring Boot version.
                  </p>
                </div>

                {/* Why section */}
                <div className='community-why'>
                  <button
                    type='button'
                    className='community-why-toggle'
                    onClick={() => setWhyOpen(prev => !prev)}
                  >
                    Why aren&apos;t these starters in Spring Initializr?
                  </button>
                  {whyOpen && (
                    <div className='community-why-content'>
                      <p>
                        Spring Initializr uses a curated dependency catalog. Dependencies
                        are only included after the Spring team has verified that they
                        integrate smoothly with Spring Boot, are actively maintained, and
                        meet the quality bar expected by the community.
                      </p>
                      <p>
                        Community starters listed here are well-known, widely used projects
                        that have not yet been incorporated into the curated catalog. They
                        are maintained by their own teams and may require additional
                        configuration steps not covered by Initializr.
                      </p>
                    </div>
                  )}
                </div>

                {/* Search */}
                <div className='community-search'>
                  <div className='community-search-input-wrap'>
                    <IconSearch />
                    <input
                      ref={searchRef}
                      id='community-search-input'
                      type='text'
                      placeholder='Search starters…'
                      value={query}
                      onChange={e => setQuery(e.target.value)}
                    />
                    {query && (
                      <button
                        type='button'
                        className='community-search-clear'
                        onClick={() => setQuery('')}
                        aria-label='Clear search'
                      >
                        <IconTimes />
                      </button>
                    )}
                  </div>
                </div>

                {/* Tags */}
                <div className='community-tags'>
                  {ALL_TAGS.map(tag => (
                    <button
                      key={tag}
                      type='button'
                      className={`community-tag${activeTag === tag ? ' active' : ''}`}
                      onClick={() => setActiveTag(tag)}
                    >
                      {tag}
                    </button>
                  ))}
                </div>

                {/* Results count */}
                <div className='community-results-count'>
                  {filteredStarters.length === starters.length
                    ? `${starters.length} community starters`
                    : `${filteredStarters.length} of ${starters.length} starters`}
                </div>

                {/* Grid */}
                {filteredStarters.length > 0 ? (
                  <div className='community-grid'>
                    {filteredStarters.map(starter => (
                      <StarterCard key={starter.id} starter={starter} />
                    ))}
                  </div>
                ) : (
                  <div className='community-empty'>
                    <IconSearch />
                    <p>No starters match your search.</p>
                  </div>
                )}
              </div>
              <div className='modal-action'>
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
          </div>
        </CSSTransition>
      )}
    </TransitionGroup>
  )
}

export default Modal
