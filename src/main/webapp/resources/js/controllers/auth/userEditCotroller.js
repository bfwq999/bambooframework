'use strict';

define([ 'js/app' ], function(app) {
	app.register.controller('UserEditController', [ '$scope', "Dialog",
			"Restangular","Util",
			function($scope, Dialog, Restangular,Util) {
				var edit = false;
				Util.navigate.receiveData($scope,"添加用户",function(data){
					$scope.user = data.user;
					$scope.orgName = data.orgName;
				});
				Util.navigate.receiveData($scope,"编辑用户",function(data){
					edit = true;
					$scope.user = data.user;
					$scope.orgName = data.user.orgName;
				});
				
				$scope.ok = function(){
					if(edit){
						$scope.user.put().then(saveSuccesss);
					}else{
						var $users = Restangular.all("users");
						$users.post($scope.user).then(saveSuccesss);
					}
					
					function saveSuccesss(user){
						if($scope.$modalInstance){
							$modalInstance.close();
						}else{
							Util.navigate.prePage($scope,"保存用户成功",{user:user});
						}
					}
				}
				$scope.cancel = function(){
					Util.navigate.prePage($scope,"保存用户成功",null);
				}
			} ])
})