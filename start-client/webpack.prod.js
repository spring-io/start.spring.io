/* eslint-disable */

const webpack = require('webpack')
const merge = require('webpack-merge')
const common = require('./webpack.common.js')
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer')
  .BundleAnalyzerPlugin
const WebpackBundleSizeAnalyzerPlugin = require('webpack-bundle-size-analyzer')
  .WebpackBundleSizeAnalyzerPlugin
const path = require('path')

const config = {
  mode: 'production',
  devtool: 'source-map',
  optimization: {
    runtimeChunk: true,
    splitChunks: {
      chunks: 'all',
    },
  },
  plugins: [
    new BundleAnalyzerPlugin({
      analyzerMode: 'static',
      openAnalyzer: false,
      generateStatsFile: true,
      statsFilename: '../analysis/stats.json',
      reportFilename: '../analysis/bundle-analyzer.html',
    }),
    new WebpackBundleSizeAnalyzerPlugin('../analysis/bundle-size-analyzer.log'),
  ],
}

module.exports = merge(common, config)
