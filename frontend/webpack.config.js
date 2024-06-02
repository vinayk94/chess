const path = require('path');

module.exports = {
    mode: 'development',
    entry: './src/app.js',
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, 'dist')
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env']
                    }
                }
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            }
        ]
    },
    resolve: {
        alias: {
            jquery: "jquery/src/jquery",
            'chess.js': path.resolve(__dirname, 'node_modules/chess.js/dist/cjs/chess.js'),
            'chessboard-js': path.resolve(__dirname, 'node_modules/@chrisoakman/chessboardjs/dist/chessboard-1.0.0.js'),
            'chessboard-css': path.resolve(__dirname, 'node_modules/@chrisoakman/chessboardjs/dist/chessboard-1.0.0.css')
        },
        fallback: {
            "net": false,
            "fs": false,
            "tls": false,
            "child_process": false
        }
    }
};