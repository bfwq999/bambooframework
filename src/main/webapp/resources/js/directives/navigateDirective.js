angular.module("myApp.navigateDirective", []).directive("myNavigate", function() {
	return {
		restrict: 'A',
		controller:'NavigateController',
		scope:{
		},
		template: '<span class="navigate-main" ng-transclude></span>',
		transclude:true,
		link:function($scope,$element,iAttrs){
			$scope.$on('$routeChangeSuccess',function(){
				$element.children(".navigate-main").show();
				$element.children(".navigate-forward").remove();
				$scope.$$childTail && $scope.$$childTail.$isNavigateController && $scope.$$childTail.$destroy();
			});
			
		}
	};
}).controller("NavigateController",["$scope","$element","$compile",
   function($scope, $element,$compile) {
	$scope.$isNavigateController = true;
	var that = this;
	that.forward = {};
	that.back = {};
	that.complete = {}
	/**
	 * 跳转到新的页面
	 */
	$scope.$on("navigate.forward",function(event,key,url,data){
		event.stopPropagation();
		that.forward={
				key:key,
				data:data
		}
		$element.children(".navigate-main").hide();
		if($element.children(".navigate-forward").length==0){
			$element.append($compile('<div my-navigate class="navigate-forward"><div ng-include="forwardUrl"></div><div>')($scope));
		}
		$scope.forwardUrl = url;
	});
	/**
	 * 获取数据
	 */
	$scope.$on("navigate.receiveData",function(event,key,callback){
		event.stopPropagation();
		$scope.$parent.$emit("navigate.receive",key,callback);
	});
	$scope.$on("navigate.receive",function(event,key,callback){
		event.stopPropagation();
		if(that.forward.key == key ){
			callback(that.forward.data);
		}
	});
	
	/**
	 * 返回上一个页面
	 */
	$scope.$on("navigate.prePage",function(event,key,data){
		event.stopPropagation();
		//当前页面的$scope的上一个NavigateController的上一个NavigateController才是对的父NavigateController
		//所以需要触发父NavigateController的complete事件
		$scope.$parent.$emit("navigate.complete",key,data);
	});
	
	$scope.$on("navigate.complete",function(event,key,data){
		//不管有没有监听当前key的完成事件,都不再往上传递了
		event.stopPropagation();
		$element.children(".navigate-main").show();
		$element.children(".navigate-forward").remove();
		$scope.$$childTail && $scope.$$childTail.$isNavigateController && $scope.$$childTail.$destroy();
		$scope.forwardUrl = null; 
		if(that.complete.key == key){
			that.complete.callback(data);
		}
	});
	/**
	 * 返回到前面某个页面
	 */
	$scope.$on("navigate.onComplete",function(event,key,callback){
		event.stopPropagation();
		that.complete = {
				key:key,
				callback:callback
		}
	});
}]);