/* eslint-disable */

const path = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const WebpackPwaManifest = require('webpack-pwa-manifest')
const isDev = process.env.NODE_ENV === 'development'
const CopyPlugin = require('copy-webpack-plugin')

const CODE = `<script defer src="https://www.googletagmanager.com/gtag/js?id={{ID}}"></script><script>window.dataLayer=window.dataLayer || []; function gtag(){dataLayer.push(arguments);}gtag('js', new Date()); gtag('config', '{{ID}}');</script>`

class WebpackGoogleTagManager {
  constructor(id) {
    this.id = id
  }
  apply(compiler) {
    compiler.hooks.compilation.tap('gtag', compilation => {
      HtmlWebpackPlugin.getHooks(compilation).beforeEmit.tapAsync(
        'gtag',
        (htmlPlugin, callback) => {
          if (this.id) {
            htmlPlugin.html = htmlPlugin.html.replace(
              '</body>',
              CODE.replace(new RegExp('{{ID}}', 'g'), this.id) + '</body>'
            )
          }
          callback(null, htmlPlugin)
        }
      )
    })
  }
}

const config = {
  entry: {
    index: [path.resolve(__dirname,  'src/App.js')],
    errors: [path.resolve(__dirname,  'src/Errors.js')],
    fourxx: [path.resolve(__dirname,  'src/Fourxx.js')],
    fivexx: [path.resolve(__dirname,  'src/Fivexx.js')],
  },
  output: {
    path: path.resolve(__dirname, 'public'),
    publicPath: '/',
    filename: '[name].[id].[fullhash].js',
    chunkFilename: '[name].[id].[chunkhash].js',
  },
  module: {
    rules: [
      {
        test: /.(js|jsx)$/,
        exclude: [path.resolve(__dirname, 'node_modules')],
        loader: 'babel-loader',
      },
      {
        test: /\.s[ac]ss$/i,
        use: ['style-loader', 'css-loader', 'sass-loader'],
      },
      {
        test: /\.(woff(2)?|ttf|eot|svg)(\?v=\d+\.\d+\.\d+)?$/,
        use: [
          {
            loader: 'file-loader',
            options: {
              name: '[hash].[ext]',
              outputPath: 'fonts/',
            },
          },
        ],
      },
    ],
  },
  resolve: {
    fallback: { querystring: require.resolve('querystring-es3') },
  },
  plugins: [
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve(__dirname, 'static/images'),
          to: path.resolve(__dirname, 'public/images'),
        },
      ],
    }),
    new HtmlWebpackPlugin({
      minify: isDev
        ? false
        : {
            collapseWhitespace: true,
            removeComments: true,
            useShortDoctype: true,
            minifyCSS: true,
          },
      filename: 'index.html', 
      chunks: ["index"],
      template: './static/index.html',
      title: 'Spring Initializr',
      description: `Initializr generates spring boot project with just what you need to start quickly!`,
      url: 'https://start.spring.io',
      twitter: '@springboot',
      image: `https://start.spring.io/images/initializr-card.jpg`,
      theme: `#6db33f`,
    }),
    new HtmlWebpackPlugin({
      minify: isDev
        ? false
        : {
            collapseWhitespace: true,
            removeComments: true,
            useShortDoctype: true,
            minifyCSS: true,
          },
      filename: 'error.html',
      chunks: ["errors"],    
      template: './static/error.html',
      title: 'Error Pages',
      description: `Errors!`,
      url: 'https://start.spring.io',
      twitter: '@springboot',
      image: `https://start.spring.io/images/initializr-card.jpg`,
      theme: `#6db33f`,
    }),
    new HtmlWebpackPlugin({
      minify: isDev
        ? false
        : {
            collapseWhitespace: true,
            removeComments: true,
            useShortDoctype: true,
            minifyCSS: true,
          },
      filename: 'fourxx.html',
      chunks: ["fourxx"],    
      template: './static/fourxx.html',
      title: 'Client Error!',
      description: `Client error responses!`,
      url: 'https://start.spring.io',
      twitter: '@springboot',
      image: `https://start.spring.io/images/initializr-card.jpg`,
      theme: `#6db33f`,
    }),
    new HtmlWebpackPlugin({
      minify: isDev
        ? false
        : {
            collapseWhitespace: true,
            removeComments: true,
            useShortDoctype: true,
            minifyCSS: true,
          },
      filename: 'fivexx.html',
      chunks: ["fivexx"],    
      template: './static/fivexx.html',
      title: 'Server Error!',
      description: `Server error responses!`,
      url: 'https://start.spring.io',
      twitter: '@springboot',
      image: `https://start.spring.io/images/initializr-card.jpg`,
      theme: `#6db33f`,
    }),
    new WebpackGoogleTagManager(process.env.GOOGLE_TAGMANAGER_ID),
    new WebpackPwaManifest({
      name: 'spring-initializr',
      short_name: 'Start',
      description: `Initializr generates spring boot project with just what you need to start quickly!`,
      background_color: '#6db33f',
      inject: true,
      fingerprints: true,
      ios: true,
      start_url: '/',
      crossorigin: null,
      icons: [
        {
          src: path.resolve('src/images/initializr-icon.png'),
          sizes: [48, 72, 96, 144, 192, 256, 384, 512],
        },
      ],
    }),
  ],
}

module.exports = config
