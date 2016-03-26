'use strict';

define(['js/app'], function(app) {
	app.register.controller('AlertController', ['$rootScope','$scope',"$window",
		function($rootScope,$scope,$window) {
			$scope.close = function(){
				$rootScope.alertMessages = null; 
				$rootScope.errorMessages = null;
			}
		}
	]);
});