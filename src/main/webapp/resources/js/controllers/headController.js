'use strict';

define(['js/app'], function (app) {
    app.register.controller('HeadController', ['$rootScope','$scope','Restangular','$window',
     function ($rootScope,$scope,Restangular,$window) {
    	
    	$scope.logout = function(){
    		var logout = Restangular.all("logout");
    		logout.head();
    		$window.location.href = "login.html";
    	}
    	var authentication = Restangular.one("authentication");
    	authentication.get().then(function(data){
    		$rootScope.isPermitted = function(permission){
     			for(var i=0; i<data.permisssions.length; i++){
     				if(permission == data.permisssions[i]){
     					return true;
     				}
     			}
     			return false;
     		}
     		$rootScope.user = data.user;
     		$rootScope.menus = data.menus;
    	});
    }]);
});