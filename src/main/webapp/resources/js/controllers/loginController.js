'use strict';
require.config({
    baseUrl: 'resources',
    urlArgs: 'v=1.0'
});
require([ 'js/myAppModule' ], function() {
	var app = angular.module('loginApp', [ 'ngMessages', 'restangular' ]);
	app.controller('LoginController', [
			'$scope',
			'$window',
			'Restangular',
			'$location',
			function($scope, $window, Restangular, $location) {
				$scope.user = {
					userName : 'admin',
					password : '888888',
					rememberMe : true
				}
				$scope.signupForm = function() {
					if ($scope.loginForm.$valid) {
						var login = Restangular.all("login");
						login.head($scope.user).then(function(){
							$window.location.href = "main.html";
						});
					} else {
						$scope.loginfail = false;
					}
				};
	} ]);
	
	angular.bootstrap(document, ['loginApp']);
});
