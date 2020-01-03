var path = require('path');



module.exports = env => {
    const baseDir =  env==='development'?path.join(__dirname,'target','classes'):__dirname;
    const bundleFile =  env==='development'?'./static/built/bundle.js':'./src/main/resources/static/built/bundle.js'

    console.log("***",baseDir);
    var config = {
        entry: './src/main/js/app.js',
        devtool: 'sourcemaps',
        cache: true,
        mode: 'development',
        output: {
            path: baseDir,
            filename: bundleFile
        },
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