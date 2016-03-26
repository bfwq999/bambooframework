'use strict';

define([ 'js/app' ], function(app) {
	var RoleController =  function($scope,$timeout, Dialog, Restangular) {
		var $roles = Restangular.all("roles");
		$roles.getList().then(function($roles){
			$scope.roles = $roles;
		});
		$scope.addRole = function(){
			Dialog.open("views/auth/role-edit.html",{
				title:'添加角色',
				data:{
					role:{}
				}
			}).button("ok",function($modalInstance,$scope){
				$roles.post($scope.role).then(function($role) {
					var treeId  = $("#treeDemo").children("ul.ztree").attr("id");
					var node = $.fn.zTree.getZTreeObj(treeId).addNodes(null,$role);
					$timeout(function() {
						$("#"+node[0].tId+"_a").trigger("click");
					}, 0);
					$modalInstance.close();
				});
			}).button("cancel",function($modalInstance){
				$modalInstance.close();
			});
		}
		$scope.treeClick = function(){
			
		}
		$scope.treeEditNodeClick = function(zTree,selectedNode){
			Dialog.open("views/auth/role-edit.html",{
				title:'编辑角色',
				data:{
					role:selectedNode
				}
			}).button("ok",function($modalInstance,$scope){
				$scope.role.put().then(function($role) {
					var treeId  = $("#treeDemo").children("ul.ztree").attr("id");
					$role = angular.extend($scope.role,$role);
					$.fn.zTree.getZTreeObj(treeId).updateNode($role);
					$.fn.zTree.getZTreeObj(treeId).selectNode($role,false);
					$modalInstance.close();
				});
			}).button("cancel",function($modalInstance){
				$modalInstance.close();
			});
		}
		$scope.treeDelNodeClick = function(zTree,selectedNode){
			selectedNode.remove().then(function(){
				zTree.removeNode(selectedNode);
			});
		}
	}
	app.register.controller('RoleController', [ '$scope', "$timeout", "Dialog",
	                                			'Restangular', RoleController ]);
});