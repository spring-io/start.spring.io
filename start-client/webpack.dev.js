/* eslint-disable */

const merge = require('webpack-merge')
const common = require('./webpack.common.js')
const path = require('path')

const mock = require('./dev/api.json')
const fs = require('fs')

const config = {
  mode: 'development',
  devtool: 'inline-source-map',
  devServer: {
    contentBase: path.resolve(__dirname, 'public'),
    historyApiFallback: true,
    compress: true,
    open: false,
    before: function(app, server, compiler) {
      app.get('/metadata/client', function(req, res) {
        res.json(mock)
      })
      app.get('/starter.zip', function(req, res) {
        fs.readFile(path.resolve('./dev/starter.zip'), (err, data) => {
          if (err) return sendError(err, res)
          res.send(data)
        })
      })
    },
  },
}

module.exports = merge(common, config)
