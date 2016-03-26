define([],function(){
	var myAppModule = angular.module('myApp.httpInterceptor', []);
	myAppModule.factory('httpInterceptor', ["$rootScope","$q","$location","$cacheFactory",
	                                        function($rootScope,$q,$location,$cacheFactory) {
		var cache = $cacheFactory("htmlCache");
		var interceptor = {
			'request' : function(config) {
				// 成功的请求方法
				return config; // 或者 $q.when(config);
			},
			'response' : function(response) {
				if(!response.data 
						|| !response.headers("Content-Type")  
						|| response.headers("Content-Type").substr(0,9) != "text/html"){
					//不是html
					var data = response.data;
					if(data && data.queryResult){
		        		  //返回数据是结果集,包含分页
						var extractedData = data.rows;
						if(extractedData){
							extractedData.pagination = data.pagination;
						}
						response.data = extractedData;
		        	}
					return response;
				}
				//如果是html,则加载返回值中的js
				var scripts = [];
	          	response.data=response.data.replace(/<script[^>]*>[^<]*<\/script>/ig, function(word) {
				var src = word.match(/src=['"][^'"]+['"]/i);
					if (src) {
						scripts.push(src[0].substring(5, src[0].length - 1))
							//如果有值替换当前位置为空
						return "";
					}
					return word;
				});
	          	if(scripts.length>0){
	          		//如果内容含有外部js,则加载js
	          		var deferred = $q.defer();
	          		require(scripts, function() {
						deferred.resolve(response);
					});
	          		return deferred.promise;
	          	}
	          	return response;
			},
			'requestError' : function(rejection) {
				// 请求发生了错误，如果能从错误中恢复，可以返回一个新的请求或promise
				//return rejection; // 或新的promise
				// 或者，可以通过返回一个rejection来阻止下一步
				return $q.reject(rejection);
			},
			'responseError' : function(rejection) {
				// 请求发生了错误，如果能从错误中恢复，可以返回一个新的响应或promise
				if(rejection.status == '401' || rejection.status == '404'){
					//跳转到404
					$location.path('404.html');
				}
				//return rejection; // 或新的promise
				// 或者，可以通过返回一个rejection来阻止下一步
				 return $q.reject(rejection);
			}
		};
		return interceptor;
	}]);
});