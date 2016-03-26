angular.module("myApp.orgTreeDirective", [])
.directive("myOrgtree", ["Restangular",function(Restangular) {
	return {
		restrict: 'A',
		require: '?ngModel',
		replace:true,
		template:function(tElement,tAttrs){
			var treeId = "tree_"+new Date().getTime();
			var menuId = "menu_"+new Date().getTime();
			var menuHtml = '<div style="position: relative;">\
			<ul class="ztree" id="'+treeId+'"></ul>\
			<ul id="'+menuId+'" class="dropdown-menu menu" role="menu" ng-show="isPermitted(\'org:add\') || isPermitted(\'org:edit\') || isPermitted(\'org:delete\')">\
				<li role="presentation" act="add" ng-show="isPermitted(\'org:add\')"><a role="menuitem" tabindex="-1" href="#">添加子机构</a></li>\
				<li role="presentation" act="edit" ng-show="isPermitted(\'org:edit\') && org.parentId!=null"><a role="menuitem" tabindex="-1" href="#">编辑机构</a></li>\
				<li role="presentation" act="del" ng-show="isPermitted(\'org:delete\') && org.parentId!=null"><a role="menuitem" tabindex="-1" href="#">删除机构</a></li>'
			+'</ul></div>'; 
			return menuHtml;
		},
		compile:function(tElement,tAttrs,transclude){
			return function($scope, $element, $attrs, $ngModel) {
				if (!$ngModel) {
					return;
				}
				var $tree = $element.children("ul.ztree");
				var rMenu =  $element.children("ul.menu");
				var menuId = rMenu.attr("id");
				
				var setting = {
					data: {
						key: {
							title:"name",
							children:"children",
							name:'name'
						},
						simpleData: {
							enable: true,
							idKey:"id",
							pIdKey:"parentId"
						}
					},
					check: {
						enable: false
					},
					edit:{
						enable:true,
						showRemoveBtn:false,
						showRenameBtn:false
					},
					callback: {
						//beforeClick: beforeClick,
						onClick: $scope.treeClick,
						onRightClick: OnRightClick,
						beforeDrop: zTreeBeforeDrop,
						onDrop: zTreeOnDrop 
					}
				};
				var zTree;
				
				var _clone = $.fn.zTree._z.tools.clone;
				
				$scope.$watch($attrs['ngModel'], function(newValue){
					if(!newValue){
						return;
					}
					$.fn.zTree._z.tools.clone = function(val){
						//不能让它对这个值进行克隆,否则restangular对象的this会失效
						if(val == newValue){
							return val;
						}
						return _clone(val);
					}
	                $.fn.zTree.init($tree, setting, newValue);
	                zTree = $.fn.zTree.getZTreeObj($tree.attr("id"));
					zTree.expandAll(true);
	            });
				$scope.$watch("isPermitted",function(newValue){
					if(!newValue){
						return;
					}
					setting.edit.enable = $scope.isPermitted("org:edit");
					 $.fn.zTree.init($tree, setting);
				});

				if(rMenu){
					rMenu.on("click","li",function(){
						hideRMenu();
						var act = $(this).attr("act");
						var selectedNode = zTree.getSelectedNodes()[0];
						if(act == 'add'){
							if($scope.treeAddNodeClick){
								$scope.treeAddNodeClick(zTree,selectedNode);
							}
						}else if(act == 'edit'){
							if($scope.treeEditNodeClick){
								$scope.treeEditNodeClick(zTree,selectedNode);
							}
						}else if(act == 'del'){
							if($scope.treeDelNodeClick){
								$scope.treeDelNodeClick(zTree,selectedNode);
							}
						}
					});
				}
				  
				function OnRightClick(event, treeId, treeNode) {
					$scope.treeClick(event,treeId,treeNode);
					if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
						zTree.cancelSelectedNode();
						showRMenu("root", event.clientX-$tree.offset().left, event.clientY-$tree.offset().top);
					} else if (treeNode && !treeNode.noR) {
						zTree.selectNode(treeNode);
						showRMenu("node", event.clientX-$tree.offset().left, event.clientY-$tree.offset().top);
					}
				}

				function showRMenu(type, x, y) {
					rMenu.show();
					rMenu.css({"top":y+"px", "left":x+"px"});
					$("body").bind("mousedown", onBodyMouseDown);
				}
				function hideRMenu() {
					if (rMenu) rMenu.hide();
					$("body").unbind("mousedown", onBodyMouseDown);
				}
				function onBodyMouseDown(event){
					if (!(event.target.id == menuId || $(event.target).parents("#"+menuId).length>0)) {
						rMenu.hide();
					}
				}
				function zTreeBeforeDrag(treeId, treeNodes){
				}
				function zTreeBeforeDrop(treeId, treeNodes, targetNode, moveType) {
				   //return !(targetNode == null || (moveType != "inner" && !targetNode.parentTId));
					var sourceNode = treeNodes[0];
					return sourceNode.level == targetNode.level && moveType != "inner";
				};
				function zTreeOnDrop(event, treeId, treeNodes, targetNode, moveType) {
					if(!moveType){
						return;
					}
					var sourceNode = treeNodes[0];
					var m = 0;
					if(moveType == 'prev'){
						m = -1;
					}else if(moveType == 'next'){
						m = 1;
					}
					sourceNode.sort = targetNode.sort + m;
					if($scope.treeNodeMoved){
						$scope.treeNodeMoved(zTree,sourceNode);
					}
				};
				//$.fn.zTree.init($($element), setting, $ngModel.$modelValue);
			}
		}
	};
}]);