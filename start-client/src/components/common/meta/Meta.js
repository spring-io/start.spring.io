import Helmet from 'react-helmet'
import React from 'react'
import { graphql, useStaticQuery } from 'gatsby'

import ImageMeta from './ImageMeta'

const Meta = () => {
  const { site } = useStaticQuery(
    graphql`
      query {
        site {
          siteMetadata {
            title
            description
            author
            canonical
            twitter
            image
          }
        }
      }
    `
  )
  const metaData = site.siteMetadata
  return (
    <>
      <Helmet>
        <html lang='en' />
        <title>{metaData.title}</title>

        <meta name='description' content={metaData.description} />
        <link rel='canonical' href={metaData.canonical} />

        <meta property='og:site_name' content={metaData.title} />
        <meta property='og:type' content='website' />
        <meta property='og:title' content={metaData.title} />
        <meta property='og:description' content={metaData.description} />
        <meta property='og:url' content={metaData.canonical} />

        <meta name='twitter:title' content={metaData.title} />
        <meta name='twitter:description' content={metaData.description} />
        <meta name='twitter:url' content={metaData.canonical} />
        <meta name='twitter:site' content={metaData.twitter} />
        <meta name='twitter:creator' content={metaData.twitter} />
      </Helmet>
      <ImageMeta image={metaData.image} />
    </>
  )
}

export default Meta
