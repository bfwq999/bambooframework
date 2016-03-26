'use strict';
define([ 'js/app' ], function(app) {
	app.register.controller('FuncController', [
			'$scope',
			"resthttp",
			"$q",
			"$routeParams",
			"$modal",
			function($scope, resthttp, $q, $routeParams, $modal) {
				loadMenuTree();
				/**
				 * 加载菜单树
				 */
				function loadMenuTree(){
					resthttp.query("func/all/tree").success(function(data) {
						//给菜单加一层根
						$scope.funcs = [{
							code:'root',
							name:'根菜单',
							children:data.rows
						}];
					});
				}
				

				$scope.permission = {
					queryParam : {
						type:'A'
					},
					pagination : {
						pageNo : 1,
						pageSize : 10
					}
				}

				//点击树节点时,加载菜单信息
				$scope.treeClick = function(func) {
					$scope.func = func
					$scope.$apply();
					$scope.permission.queryParam.parentId = $scope.func.id;
					reLoadPermisssionOfMenu();
				}
				
				/**
				 * 加载菜单下的权限信息
				 */
				function reLoadPermisssionOfMenu(){
					resthttp.query("func", $scope.permission).success(
							function(data) {
								$scope.permission.rows = data.rows;
								$scope.permission.pagination = data.pagination;
							});
				}
				
				
				/**
				 * 添加菜单
				 */
				$scope.addFunc = function(type) {
					var dlgTitle;
					if(type instanceof Object){
						//编辑
						dlgTitle = '修改权限'
					}else{
						dlgTitle = type=='M'?'添加菜单':'添加权限'
					}
					var modalInstance = $modal.open({
						animation : true,
						templateUrl : 'resources/templates/func-edit.html',
						backdrop : 'static',
						controller : ['$rootScope','$scope','$modalInstance',funcDlgCtrl]
					});
					 modalInstance.result.then(function () {
					      $scope.selected = selectedItem;
					  });
					 
					var $pscope = $scope;
					
					function funcDlgCtrl($rootScope,$scope, $modalInstance) {
						$scope.title = dlgTitle;
						$scope.parentFuncName = $pscope.func.name;
						if(type instanceof Object){
							$scope.func = type;
						}else{
							$scope.func = {
								parentId: $pscope.func.id,
								type:type
							}
						}
						
						
						$scope.ok = function() {
							resthttp.save("func",{
								func:$scope.func
							})
							.success(function(){
								$rootScope.alertMessages = ['保存成功!'];
								loadMenuTree();
								reLoadPermisssionOfMenu();
								$modalInstance.close();
							});
						}
						$scope.cancel = function() {
							$modalInstance.dismiss("cancel");
						}
					}
				}
			} ]);

});