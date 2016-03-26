'use strict';

define(['js/app'], function(app) {
	app.register.controller('UserController', ['$scope', "Dialog", "Restangular","Util",
		function($scope,Dialog,Restangular,Util) {
			var $users = Restangular.all("users");
			Util.navigate.onComplete($scope,"保存用户成功",function(data){
				doQuery(); //初始查询
			});
			doQuery(); //初始查询
			function doQuery(){
				if($scope.users){
					var pagination = $scope.users.pagination;
				}
				if(!pagination){
					pagination ={
							pageNo:1,
							pageSize:10
					}
				}
				$scope.users = $users.getList(angular.extend({},{
					queryParam:$scope.queryParam
				},pagination)).$object;
			}
			
			$scope.queryUser = function(){
				doQuery();
			}
			
			/**
			 * 添加
			 */
			$scope.addUser = function(){
				Util.navigate.forward($scope,"添加用户","views/auth/user-edit.html",{user:{sex:'male'}});
//				var dlgTitle = "添加用户";
//				Dialog.openEditUserDialog(dlgTitle,null,{})
//				.ok(function($modalInstance,user){
//					debugger;
//					$users.post(user).then(function(user) {
//						$modalInstance.close();
//						doQuery();
//					});
//				});
//				Dialog.open("views/auth/user-edit.html",{
//					title:"添加用户",
//					data:{
//						user:{sex:'male'}
//					}
//				})
//				.button("ok",function($modalInstance,$dlScope){
//					var user = $dlScope.user;
//					$users.post(user).then(function(user) {
//						$modalInstance.close();
//						doQuery();
//					});
//				});
			}
			/**
			 * 编辑
			 */
			$scope.edit = function(user){
				Util.navigate.forward($scope,"编辑用户","views/auth/user-edit.html",{user:user});
//				var dlgTitle = "编辑用户";
//				Dialog.openEditUserDialog(dlgTitle,user.name,user)
//				.ok(function($modalInstance,user){
//					user.put().then(function(user) {
//						$modalInstance.close();
//					});
//				});
			}
			$scope['delete'] = function(user){
				user.remove().then(function(){
					doQuery();
				});
			}
			
		  $scope.pageChanged = function() {
			  doQuery();
		  };
		}
	]);
});