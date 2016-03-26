define([ 'js/dialogs/userEditorDialog',
         'js/dialogs/orgEditorDialog',
		'js/dialogs/positionEditorDialog',
		'js/dialogs/userListDialog'
		], function() {
	var args = arguments;
	return angular.module('myApp.dialogs', [ 'ui.bootstrap.modal' ]).factory(
			'Dialog', [ "$modal", function($modal) {
				var dialogs = {};
				for (var i = 0; i < args.length; i++) {
					var method = args[i]($modal);
					dialogs[method.name] = method;
				}
				dialogs.open = function(url,opts){
					var $scope
					var promise = {};
					var buttons = {};
					promise.button = function(fnName,callback){
						buttons[fnName] = function() {
							callback($modalInstance,$scope)
						}
						return promise;
					}
					
					promise.button("close",function($modalInstance,$scope){
						if(!opts.beforeClose || opts.beforeClose($modalInstance,$scope)){
							$modalInstance.close();
						}
					});
					
					opts = opts||{};
					var $modalInstance = $modal.open({
						animation : opts.animation||true,
						templateUrl : opts.templateUrl||"resources/js/dialogs/template/dialog.html",
						backdrop : opts.backdrop||'static',
						controller : [ '$scope', dlgCtrl ]
					});
					function dlgCtrl($dlgScope) {
						$scope = $dlgScope;
						$scope.$modalInstance = $modalInstance;
						$scope.bodyUrl = url;
						$scope.title = opts.title;
						angular.extend($scope, opts.data);
						angular.extend($scope, buttons);
					}
					return promise;
				};
				return dialogs;
			} ]);
});