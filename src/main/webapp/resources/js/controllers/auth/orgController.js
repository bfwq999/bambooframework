'use strict';

define([ 'js/app' ], function(app) {

	// 加载机构树
	function loadOrgTree($scope,$orgs) {
		return $orgs.getList().then(function(orgs) {
//			$scope.orgs = makeTree(orgs, 'id', 'parentId');
			debugger;
			$scope.orgs = orgs;
		});
	}

	// 加载机构岗位
	function getPositionsOfOrg($org) {
		return $org.getList("positions", {
			pageSize : 10,
			pageNo : 1
		}).$object;
	}

	// 加载机构员工
	function getEmployeesOfOrg($org) {
		return $org.getList("users", {
			pageSize : 10,
			pageNo : 1
		}).$object;
	}
	
	app.register.controller('OrgController', [
			'$scope',
			"$timeout",
			"Dialog",
			'Restangular',
			function($scope,$timeout, Dialog, Restangular) {

				var $orgs = Restangular.all("orgs");
				var $positions = Restangular.all("positions");
				var $users = Restangular.all("users");
				Restangular.setParentless(true);
				
				loadOrgTree($scope,$orgs);
				//$scope.orgs = getOrgs(Restangular);

				//点击树节点事件
				$scope.treeClick = function(event,treeId, $org, clickFlag) {
					$scope.org = $org
					$scope.$apply();
					if($scope.selected == 'pos'){
						$scope.posResult = getPositionsOfOrg($org);	
					}else if($scope.selected == 'emp'){
						$scope.empResult = getEmployeesOfOrg($org);
					}
				}
				
				$scope.treeAddNodeClick = function(zTree,selectedNode){
					$scope.addOrg();
				}
				$scope.treeEditNodeClick = function(zTree,selectedNode){
					$scope.editOrg(selectedNode);
				}
				$scope.treeDelNodeClick = function(zTree,selectedNode){
					selectedNode.remove().then(function(){
						zTree.removeNode(selectedNode);
					});
				}
				$scope.treeNodeMoved = function(zTree,node){
					node.put().then(function(){
						loadOrgTree($scope,$orgs);
					});
				}
				
				$scope.selectPosTab = function(){
					$scope.selected = 'pos';
					if($scope.org){
						$scope.posResult = getPositionsOfOrg($scope.org);	
					}
					
				}
				
				$scope.selectEmpTab = function(){
					$scope.selected = 'emp';
					if($scope.org){
						$scope.empResult = getEmployeesOfOrg($scope.org);
					}
				}
				
				//
				var $pscope = $scope;
				
				/**
				 * 添加机构按钮
				 */
				$scope.addOrg = function() {
					var dlgTitle = "添加机构";
					Dialog.openEditOrgDialog(dlgTitle,$pscope.org.name,{
						parentId : $pscope.org.id
					}).ok(function($modalInstance,org){
						$orgs.post(org).then(function($org) {
							//loadOrgTree($pscope,$orgs);
							var treeId  = $("#treeDemo").children("ul.ztree").attr("id");
							var node = $.fn.zTree.getZTreeObj(treeId).addNodes($pscope.org,$org);
							$timeout(function() {
							   // angular.element("#"+node[0].tId+"_a").triggerHandler('click');
								$("#"+node[0].tId+"_a").trigger("click");
							}, 0);
							$modalInstance.close();
						});
					});
				}
				/**
				 * 编辑机构按钮
				 */
				$scope.editOrg = function(org) {
					var dlgTitle = "编辑机构";
					Dialog.openEditOrgDialog(dlgTitle,$pscope.org.name,org)
					.ok(function($modalInstance,org){
						org.put().then(function($org) {
							//loadOrgTree($pscope,$orgs);
							var treeId  = $("#treeDemo").children("ul.ztree").attr("id");
							$.fn.zTree.getZTreeObj(treeId).updateNode(org);
							$.fn.zTree.getZTreeObj(treeId).selectNode(org,false);
							$modalInstance.close();
						});
					});
				}
				/**
				 * 添加岗位按钮
				 */
				$scope.addPos = function() {
					var dlgTitle = "添加岗位";
					Dialog.openEditPositionDialog(dlgTitle,$pscope.org.name,{
						orgId : $pscope.org.id
					}).ok(function($modalInstance,position){
						$positions.post(position).then(function(position) {
							$scope.posResult = getPositionsOfOrg($pscope.org);
							$modalInstance.close();
						});
					});
				}
				/**
				 * 编辑岗位按钮
				 */
				$scope.editPos = function(position) {
					var dlgTitle = "编辑岗位";
					Dialog.openEditPositionDialog(dlgTitle,$pscope.org.name,position)
					.ok(function($modalInstance,position){
						position.put().then(function(position) {
							$modalInstance.close();
						});
					});
				}
				
				/**
				 * 删除岗位
				 */
				$scope.deletePos = function(pos){
					pos.remove().then(function(){
						$scope.posResult = getPositionsOfOrg($pscope.org);
					});
				}
				
				$scope.addEmp = function(){
					var dlgTitle = "添加员工";
					Dialog.openEditUserDialog(dlgTitle,$pscope.org.name,{
						orgId:$pscope.org.id
					}).ok(function($modalInstance,user){
						$users.post(user).then(function(user) {
							$scope.empResult = getEmployeesOfOrg($pscope.org);
							$modalInstance.close();
						});
					});
				}
				$scope.editEmp = function(user){
					var dlgTitle = "编辑员工";
					Dialog.openEditUserDialog(dlgTitle,$pscope.org.name,user)
					.ok(function($modalInstance,user){
						user.put().then(function(user) {
							$modalInstance.close();
						});
					});
				}
				
				$scope.viewEmpOfPos = function(pos){
					
				}
			} ]);
});