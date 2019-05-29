const isDev = process.env.NODE_ENV === 'development'

const siteMetadata = {
  title: `Spring Initializr`,
  description: `Initializr generates spring boot project with just what you need to start quickly!`,
  twitter: `@springboot`,
  canonical: `https://start.spring.io`,
  author: `@springboot`,
  image: `https://cocky-roentgen-4351af.netlify.com/images/initializr-card.jpg`,
  apiUrl: isDev ? `http://localhost:8080/` : `/`,
}

const plugins = [
  {
    resolve: 'gatsby-plugin-webpack-bundle-analyzer',
    options: {
      analyzerPort: 3000,
      production: true,
      openAnalyzer: false,
    },
  },
  `gatsby-plugin-react-helmet`,
  {
    resolve: `gatsby-source-filesystem`,
    options: {
      name: `images`,
      path: `${__dirname}/src/images/`,
    },
  },
  {
    resolve: `gatsby-plugin-prefetch-google-fonts`,
    options: {
      fonts: [
        {
          family: `Karla`,
          variants: [`400`, `700`],
        },
      ],
    },
  },
  `gatsby-transformer-json`,
  `gatsby-plugin-sass`,
  `gatsby-transformer-sharp`,
  `gatsby-plugin-sharp`,
  {
    resolve: `gatsby-plugin-manifest`,
    options: {
      name: `initializr`,
      short_name: `start`,
      start_url: `/`,
      background_color: `#6db33f`,
      theme_color: `#6db33f`,
      display: `minimal-ui`,
      icon: `src/images/initializr-icon.png`,
    },
  },
]

if (process.env.GOOGLE_ANALYTICS_TRACKING_ID) {
  const analyticsTrackingId = process.env.GOOGLE_ANALYTICS_TRACKING_ID
  const optimizeTrackingId = process.env.GOOGLE_OPTIMIZE_TRACKING_ID
  const experimentId = process.env.GOOGLE_EXPERIMENT_ID
  const optimizeVariationId = process.env.YOUR_GOOGLE_OPTIMIZE_VARIATION_ID
  plugins.push({
    resolve: `gatsby-plugin-google-analytics`,
    options: {
      trackingId: analyticsTrackingId,
      head: false,
      respectDNT: true,
      exclude: ['/preview/**', '/do-not-track/me/too/'],
      optimizeId: optimizeTrackingId,
      experimentId: experimentId,
      variationId: optimizeVariationId,
      cookieDomain: 'spring.io',
    },
  })
}

module.exports = {
  siteMetadata,
  plugins,
}
