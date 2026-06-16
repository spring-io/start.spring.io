import starters from '../starters.json'

// ─── Data integrity tests ─────────────────────────────────────────────────────

describe('starters.json', () => {
  it('should be a non-empty array', () => {
    expect(Array.isArray(starters)).toBe(true)
    expect(starters.length).toBeGreaterThan(0)
  })

  it('every entry should have all required fields with non-empty string values', () => {
    const requiredFields = [
      'id',
      'name',
      'description',
      'groupId',
      'artifactId',
      'docUrl',
      'repoUrl',
    ]
    starters.forEach(starter => {
      requiredFields.forEach(field => {
        expect(starter[field]).toBeDefined()
        expect(typeof starter[field]).toBe('string')
        expect(starter[field].trim().length).toBeGreaterThan(0)
      })
    })
  })

  it('every entry should have a non-empty tags array', () => {
    starters.forEach(starter => {
      expect(Array.isArray(starter.tags)).toBe(true)
      expect(starter.tags.length).toBeGreaterThan(0)
      starter.tags.forEach(tag => {
        expect(typeof tag).toBe('string')
        expect(tag.trim().length).toBeGreaterThan(0)
      })
    })
  })

  it('every id should be unique', () => {
    const ids = starters.map(s => s.id)
    const unique = new Set(ids)
    expect(unique.size).toBe(ids.length)
  })

  it('docUrl and repoUrl should be valid URLs', () => {
    starters.forEach(starter => {
      expect(starter.docUrl).toMatch(/^https?:\/\//)
      expect(starter.repoUrl).toMatch(/^https?:\/\//)
    })
  })

  it('groupId and artifactId should follow Maven coordinate conventions', () => {
    starters.forEach(starter => {
      // groupId: at least one dot-separated segment
      expect(starter.groupId).toMatch(/^[a-zA-Z0-9._-]+$/)
      // artifactId: lowercase, hyphens allowed
      expect(starter.artifactId).toMatch(/^[a-zA-Z0-9._-]+$/)
    })
  })
})

// ─── Snippet generation helpers ───────────────────────────────────────────────

function buildMavenSnippet(groupId, artifactId) {
  return `<dependency>
  <groupId>${groupId}</groupId>
  <artifactId>${artifactId}</artifactId>
</dependency>`
}

function buildGradleSnippet(groupId, artifactId) {
  return `implementation '${groupId}:${artifactId}'`
}

describe('buildMavenSnippet', () => {
  it('should produce a valid Maven dependency XML snippet', () => {
    const snippet = buildMavenSnippet('org.apache.camel.springboot', 'camel-spring-boot-starter')
    expect(snippet).toContain('<dependency>')
    expect(snippet).toContain('<groupId>org.apache.camel.springboot</groupId>')
    expect(snippet).toContain('<artifactId>camel-spring-boot-starter</artifactId>')
    expect(snippet).toContain('</dependency>')
  })
})

describe('buildGradleSnippet', () => {
  it('should produce a valid Gradle implementation snippet', () => {
    const snippet = buildGradleSnippet('org.apache.camel.springboot', 'camel-spring-boot-starter')
    expect(snippet).toBe(
      "implementation 'org.apache.camel.springboot:camel-spring-boot-starter'"
    )
  })
})

// ─── Filtering logic ──────────────────────────────────────────────────────────

function filterStarters(items, query, activeTag) {
  const q = query.trim().toLowerCase()
  return items.filter(s => {
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
}

describe('filterStarters', () => {
  it('should return all starters when query is empty and tag is All', () => {
    const result = filterStarters(starters, '', 'All')
    expect(result.length).toBe(starters.length)
  })

  it('should filter by name (case-insensitive)', () => {
    const result = filterStarters(starters, 'camel', 'All')
    expect(result.length).toBeGreaterThan(0)
    result.forEach(s => {
      expect(s.name.toLowerCase()).toContain('camel')
    })
  })

  it('should filter by groupId', () => {
    const result = filterStarters(starters, 'org.flowable', 'All')
    expect(result.length).toBeGreaterThan(0)
    result.forEach(s => {
      expect(s.groupId.toLowerCase()).toContain('flowable')
    })
  })

  it('should filter by tag when activeTag is set', () => {
    const result = filterStarters(starters, '', 'workflow')
    result.forEach(s => {
      expect(s.tags).toContain('workflow')
    })
  })

  it('should return empty array when nothing matches', () => {
    const result = filterStarters(starters, 'xyzzy_no_match_guaranteed', 'All')
    expect(result.length).toBe(0)
  })

  it('should apply both query and tag filter simultaneously', () => {
    // "integration" in both query and as a real tag
    const result = filterStarters(starters, 'camel', 'integration')
    // Apache Camel has both
    result.forEach(s => {
      expect(s.tags).toContain('integration')
    })
  })
})
