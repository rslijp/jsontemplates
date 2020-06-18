var HtmlWebpackPlugin = require('html-webpack-plugin');
var CopyWebpackPlugin = require('copy-webpack-plugin');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');

var path = require('path');
module.exports = env => {
    console.log("*** "+env+" ***");
    var config =  {
        // Tell Webpack which file kicks off our app.
        entry: [path.resolve(__dirname, 'src/app.js')],
        // Tell Weback to output our bundle to ./dist/bundle.js
        output: {
            filename: 'bundle.js',
            path: path.resolve(__dirname, 'dist')
        },
        // devtool: 'inline-source-map',
        mode: env,
        module: {
            rules: [
                {
                    test: /\.css$/i,
                    use: ['style-loader', 'css-loader'],
                },
                {
                    // If you see a file that ends in .html, send it to these loaders.
                    // test: path.join(__dirname, '.'),
                    test: /\.js$/,
                    exclude: /(node_modules)/,
                    // Chained loaders run last to first. So it will run
                    // polymer-webpack-loader, and hand the output to
                    // babel-loader. This let's us transpile JS in our `<script>` elements.
                    use: [
                        {   loader: 'babel-loader',
                            options: {
                                presets: ["@babel/preset-env", "@babel/preset-react"]
                            }
                        }
                    ]
                }
            ]
        },
        // Enable the Webpack dev server which will build, serve, and reload our
        // project on changes.
        // devServer: {
        //     contentBase: path.join(__dirname, 'dist'),
        //     compress: true,
        //     port: 9000,
        //     proxy: {
        //         '/api': {
        //             target: 'http://localhost:8080',
        //         }
        //     }
        // },
        devServer: {
            inline: true,
            contentBase: path.join(__dirname, 'dist'),
            compress: true,
            port: 9000,
            proxy: {
                '/workbench-api': {
                    target: 'http://localhost:8080',
                }
            },
            watchOptions: {
                index: 'index.ejs.html',
                open: true,
                poll: true,
                watchContentBase: true
            }
        },
        plugins: [
            env === "development" ? new UglifyJsPlugin() : null,  // This plugin will generate an index.ejs file for us that can be used
            // by the Webpack dev server. We can give it a template file (written in EJS)
            // and it will handle injecting our bundle for us.
            new HtmlWebpackPlugin({
                template: path.resolve(__dirname, 'src/index.ejs'),
                inject: true
            }),
            new CopyWebpackPlugin([
                {
                    from: path.resolve(__dirname, 'content/**/*'),
                    to: '[path]/[name].[ext]'
                }])
        ]
    };
    config.plugins=config.plugins.filter(x => !!x);
    return config;
};