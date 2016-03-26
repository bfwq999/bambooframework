'use strict';
define(['js/myAppModule'], function() {
	var app = angular.module('customersApp', [ 'ngRoute', 'ngMessages',
					'myApp', 'ui.bootstrap', 'restangular', 'angucomplete' ]);
	app.constant('CONTEXT_PATH', ''); 
	app.config([
			'$routeProvider',
			'$controllerProvider',
			'$compileProvider',
			'$filterProvider',
			'$provide',
			'$locationProvider',
			'$httpProvider',
			'RestangularProvider',
			function($routeProvider,
					$controllerProvider, $compileProvider, $filterProvider,
					$provide,$locationProvider,$httpProvider,RestangularProvider) {
				
			    $httpProvider.defaults.headers.common['Access-From'] = 'angularjs';
			    $httpProvider.interceptors.push("httpInterceptor");
			    //$httpProvider.defaults.headers.common['Cache-Control'] = 'no-cache';
			    //$httpProvider.defaults.headers.common['Pragma'] = 'no-cache';
			    
			   // $httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded';
			   // $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
			    
		        var r20 = /%20/g;
				var rbracket = /\[\]$/;
				
				/**
				 * 判断是否是Array
				 */
				function isArray(obj) {   
					  return Object.prototype.toString.call(obj) === '[object Array]';    
				}  
				
				/**
				 * angularjs默认把对象解析成json串,springmvc接收不到值,
				 * 所以需要将对象解析成{name1:value1,name2:value2}形式
				 */
				var formatParam = function(a) {
					if(a == null){
						return '';
					}
					var prefix,
						s = {},
						add = function(key, value) {
							// If value is a function, invoke it and return its value
							value = (typeof value == 'function') ? value() : (value == null ? "" : value);
//							s[s.length] = encodeURIComponent(key) + ":" + encodeURIComponent(value);
							s[key] =  value;
						};
					// If an array was passed in, assume that it is an array of form
					// elements.
					if (isArray(a)) {
						// Serialize the form elements
						for (var i = 0; i < a.length; i++) {
							add(i, a[i]);
						}
					} else {
						// did it), otherwise encode params recursively.
						for (prefix in a) {
							buildParams(prefix, a[prefix], add);
						}
					}
					// Return the resulting serialization
					return s;
				};
			
				function buildParams(prefix, obj, add) {
					var name;
					if (isArray(obj)) {
						// Serialize array item.
						for (var i = 0; i < obj.length; i++) {
							var v = obj[i];
							if (rbracket.test(prefix)) {
								// Treat each array item as a scalar.
								add(prefix, v);
							} else {
								// If array item is non-scalar (array or object), encode its
								// numeric index to resolve deserialization ambiguity
								// issues.
								// Note that rack (as of 1.0.0) can't currently deserialize
								// nested arrays properly, and attempting to do so may cause
								// a server error. Possible fixes are to modify rack's
								// deserialization algorithm or to provide an option or flag
								// to force array serialization to be shallow.
								buildParams(prefix + "[" + i + "]", v, add);
							}
						}
					} else if (typeof obj == "object") {
						// Serialize object item.
						for (name in obj) {
							buildParams(prefix + "." + name , obj[name], add);
						}
					} else {
						// Serialize scalar item.
						add(prefix, obj);
					}
				}
				
		        RestangularProvider.setFullRequestInterceptor(function(element, operation, route, url, headers, params, httpConfig) {
		        	if(operation == 'get'||operation == 'getList'){
		        		params = formatParam(params);
		        	}
		        	return {
		              element: element,
		              params: params,
		              headers: headers,
		              httpConfig: httpConfig
		            };
		          });
		        
		        
		        var refreshAccesstoken = function() {
		            var deferred = $q.defer();

		            // Refresh access-token logic

		            return deferred.promise;
		        };
		        
		        RestangularProvider.setErrorInterceptor(function(response, deferred, responseHandler) {
		            if(response.status === 403) {
		                refreshAccesstoken().then(function() {
		                    // Repeat the request and then call the handlers the usual way.
		                    $http(response.config).then(responseHandler, deferred.reject);
		                    // Be aware that no request interceptors are called this way.
		                });

		                return false; // error handled
		            }

		            return true; // error not handled
		        });

			    
				app.register = {
					controller : $controllerProvider.register,
					directive : $compileProvider.directive,
					filter : $filterProvider.register,
					factory : $provide.factory,
					service : $provide.service
				};
				
				$locationProvider.html5Mode(true);
				$routeProvider
				.when('/', {
					redirectTo: function(route,path,param){
						return '/views/home.html';
					}
				 })
				 .when("/main.html",{
					 redirectTo: function(route,path,param){
							return param.url || '/views/home.html';
					 }
				 })
				 .when('/:path1',{
					 templateUrl: function(rq){
						 return rq.path1;
					 }
				 })
				.when('/views/:path1',{
		        	templateUrl: function(rq){
		        		return "views/"+rq.path1;
		        	}
			    })
				.when('/views/:path1/:path2',{
		        	templateUrl: function(rq){
		        		return "views/"+rq.path1+"/"+rq.path2;
		        	}
			    })
			    .when('/views/:path1/:path2/:id',{
			    	templateUrl: function(rq){
			    		return "views/"+rq.path1+"/"+rq.path2+".html";
			    	}
			    }).otherwise({
			    	redirectTo: function(route,path,param){
						return '/views/home.html';
					}
			    });

			} ]);

	return app;
});

