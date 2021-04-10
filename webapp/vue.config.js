const path = require("path");

module.exports = {
    devServer: {
        proxy: {
            '^/api': {
                target: 'http://localhost:3412',
                changeOrigin: true
            },
        }
    },
    outputDir: path.resolve(__dirname, "../src/main/resources/webapp")
}