module.exports = {
    devServer: {
        port: 3005
    },
    configureWebpack: {
        resolve: {
            fallback: {
                "crypto": false,
                "util": false
            }
        }
    }
}