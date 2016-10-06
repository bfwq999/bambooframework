'use strict';
require.config({
    baseUrl: 'resources',
    urlArgs: 'v=1.0'
});
require([ 'js/myAppModule' ], function() {
	var app = angular.module('loginApp', [ 'ngMessages', 'datautil' ]);
	app.controller('DemoController', [
			'$scope',
			'$window',
			'DataUtil',
			'$location',
			function($scope, $window, DataUtil, $location) {
				$scope.T_ORG = DataUtil.get("T_ORG","'00001'").$object;
			} ]);
	
	angular.bootstrap(document, ['loginApp']);
});
