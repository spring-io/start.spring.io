/* eslint-disable */

const { merge } = require('webpack-merge')
const common = require('./webpack.common.js')
const path = require('path')

const mock = require('./dev/api.mock.json')
const fs = require('fs')

const config = {
  mode: 'development',
  devtool: 'inline-source-map',
  devServer: {
    static: path.resolve(__dirname, 'public'),
    historyApiFallback: true,
    compress: true,
    open: false,
    onAfterSetupMiddleware: function (devServer) {
      if (!devServer) {
        throw new Error('webpack-dev-server is not defined')
      }
      devServer.app.get('/metadata/client', function (req, res) {
        setTimeout(() => {
          res.json(mock)
        }, 800)
      })
      devServer.app.get('/starter.zip', function (req, res) {
        // 500 errors
        // res.status(500).json({
        //   timestamp: '2022-09-05T14:39:37.471+00:00',
        //   status: 500,
        //   error: 'Internal Server Error',
        //   message:
        //     'Malformed input or input contains unmappable characters: MixérApplication.java',
        //   path: '/starter.zip',
        // })
        fs.readFile(path.resolve('./dev/starter.mock.zip'), (err, data) => {
          if (err) return sendError(err, res)
          setTimeout(() => {
            res.send(data)
          }, 800)
        })
      })
    },
  },
}

module.exports = merge(common, config)
