define([], function() {
	var myAppModule = angular.module('myApp.util', []);
	myAppModule.factory('Util', [ "$rootScope","$location",
			function($rootScope, $location) {
				var utils = {};

				// page
				var navigateUtils = utils.navigate = {};
				navigateUtils.forward = function($scope,key, url, data) {
					$scope.$emit("navigate.forward",key,url,data);
				}
				navigateUtils.receiveData = function($scope,key, callback) {
					$scope.$emit("navigate.receiveData",key,callback);
				}
				navigateUtils.prePage = function($scope,key, url, data) {
					$scope.$emit("navigate.prePage",key, url, data);
				}
				navigateUtils.onComplete = function($scope,key, callback) {
					$scope.$emit("navigate.onComplete",key, callback);
				}
				return utils;
			} ]);
})