var path = require('path');



module.exports = env => {
    console.log("*** "+env+" ***")
    // const baseDir =  env==='development'?path.join(__dirname,'target','classes'):__dirname;
    // const bundleFile =  env==='development'?'./static/built/bundle.js':'./src/main/resources/static/built/bundle.js'
    //
    // console.log("***",baseDir);
    var config = {
        entry: [path.resolve(__dirname, 'src/app.js')],
        // Tell Weback to output our bundle to ./dist/bundle.js
        output: {
            filename: 'bundle.js',
            path: path.resolve(__dirname, 'dist')
        },
        devtool: 'sourcemaps',
        cache: true,
        mode: 'development',
        module: {
            rules: [
                {
                    test: path.join(__dirname, '.'),
                    exclude: /(node_modules)/,
                    use: [{
                        loader: 'babel-loader',
                        options: {
                            presets: ["@babel/preset-env", "@babel/preset-react"]
                        }
                    }]
                }
            ]
        }
    }
    return config;
};