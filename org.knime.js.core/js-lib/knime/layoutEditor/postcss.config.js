// Vue CLI uses PostCSS internally.
module.exports = {
    plugins: {
        'postcss-import': {},
        'postcss-preset-env': {
            stage: 2,
            features: {
                'nesting-rules': true,
                'custom-properties': false // doesn't support pseudo elements and only :root, so we'll use postcss-css-variables
            }
        },
        'postcss-css-variables': {}
    }
};
