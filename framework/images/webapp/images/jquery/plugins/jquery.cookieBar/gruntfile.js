module.exports = function (grunt) {
	require('jit-grunt')(grunt);

	grunt.initConfig(
		{
			watch:       {
				sass: {
					files: ['jquery.cookieBar.scss'],
					tasks: ['sass', 'postcss', 'cssmin']
				},
				js: {
					files: ['jquery.cookieBar.js'],
					tasks: ['uglify']
				}
			},
			sass:        {
				dist: {
					options: {
						outputStyle:	'expanded'
					},
					files:   {
						'jquery.cookieBar.css': 'jquery.cookieBar.scss'
					}
				}
			},
			postcss:  {
				options: {
					processors: [
						require('autoprefixer')({browsers: ['last 3 versions', 'ios 6', 'ie 9']}),
						require('postcss-flexbugs-fixes')
					]
				},
				dist: {
					src: '*.css'
				}
			},
			cssmin: {
				target: {
					files: {
						'jquery.cookieBar.min.css': ['jquery.cookieBar.css']
					}
				}
			},
			uglify: {
				options: {
					output: {
						comments: 'some'
					}
				},
				my_target: {
					files: [{
						src:	['jquery.cookieBar.js'],
						dest:	'jquery.cookieBar.min.js'
					}]
				}
			}
		}
	);

	grunt.registerTask('default', ['watch']);
	grunt.registerTask('build', ['uglify', 'sass', 'postcss', 'cssmin']);
};
