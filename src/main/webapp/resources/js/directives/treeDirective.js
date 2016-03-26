define([], function() {
	angular.module('myApp.treeDirective', []).directive('myTree',
			function() {
				return {
					restrict : 'A',
					scope:true,
					compile: function(tEle, tAttrs, transcludeFn) {
						tEle.addClass("tree");
						var dataName = tAttrs.myTree;
						var childName = tAttrs.treeChildren;
						var treeClickMethod = tAttrs.treeClick;
						var nodeText = tAttrs.nodeText;
						var selectedItem;
						
						return function ngRepeatLink($scope, $element, $attr, ctrl, $transclude) {
							$scope.$watch(dataName,function(newVal, oldVal){
								tEle.html("");
								if(newVal == null){
									return;
								}
								for(var i=0; i<newVal.length; i++){
									render(tEle,newVal[i]);
								}
							});
							
							function render($element,val){
								if(val[childName] && val[childName].length>0){
									//创建folder
									var $el = angular.element(
											'<div class="tree-folder" style="display: block;">\
												<div class="tree-folder-header">\
													<i class="glyphicon glyphicon-minus"></i>\
													<div class="tree-folder-name">'+val[nodeText]+'</div>\
												</div>\
												<div class="tree-folder-content"></div>\
											 </div>');
									$el.find(".tree-folder-header").on("click",function(){
										if($scope[treeClickMethod] && $scope[treeClickMethod](val) === false){
											return false;
										}
										var $folder = $el.find(".tree-folder-header");
										if(selectedItem){
											selectedItem.removeClass("tree-selected");
										}
										$folder.addClass("tree-selected");
										selectedItem = $folder;
										
									}).find(".glyphicon").on("click",function(){
										if($(this).hasClass("glyphicon-minus")){
											$(this).removeClass("glyphicon-minus");
											$(this).addClass("glyphicon-plus");
										}else{
											$(this).removeClass("glyphicon-plus");
											$(this).addClass("glyphicon-minus");
										}
										$el.find(".tree-folder-content").toggle();
										return false;
									});
									for(var i=0; i<val[childName].length; i++){
										render($el.find(".tree-folder-content"),val[childName][i]);
									}
									$element.append($el);
								}else{
									//创建叶子节点
									var $el = angular.element(
											'<div class="tree-item" style="display: block;">\
												<i class="glyphicon glyphicon-remove"></i>\
												<div class="tree-item-name">'+val[nodeText]+'</div>\
											</div>');
									$el.on("click",function(){
										if($scope[treeClickMethod] && $scope[treeClickMethod](val) === false){
											return false;
										}
										if(selectedItem){
											selectedItem.removeClass("tree-selected");
											selectedItem.find(".glyphicon").removeClass("glyphicon-ok").addClass("glyphicon-remove");
										}
										$el.addClass("tree-selected");
										$el.find(".glyphicon").removeClass("glyphicon-remove").addClass("glyphicon-ok");
										selectedItem = $el;
									});
									$element.append($el);
								}
							}
						}
					}
				};
			});
});