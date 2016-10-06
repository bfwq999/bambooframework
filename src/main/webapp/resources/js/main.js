require.config({
    baseUrl: 'resources',
    urlArgs: 'v=1.0'
});

require(
    [	
     	'js/app'
    ],
    function (app) {
    	app.controller("MainController",["$scope",function($scope){
    		
    	}]);
        angular.bootstrap(document, ['customersApp']);
    });
