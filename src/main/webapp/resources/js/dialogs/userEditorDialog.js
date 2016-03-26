define([], function() {
	/**
	 * 员工编辑对话框
	 */
	return function($modal){
		return function openEditUserDialog(title, orgName, user) {
			var promise = {};
			var okFn;
			var cancelFn;
			promise.ok = function(callback) {
				okFn = callback;
				return promise;
			}
			promise.cancel = function(callback) {
				cancelFn = callback;
				return promise;
			};
			var $modalInstance = $modal.open({
				animation : true,
				templateUrl : 'views/auth/user-edit.html',
				backdrop : 'static',
				controller : [ '$scope', dlgCtrl ]
			});
			function dlgCtrl($scope) {
				$scope.title = title;
				$scope.orgName = orgName;
				var newUser = angular.extend({sex:'male'}, user);
				$scope.user = newUser;
				$scope.ok = function() {
					// 替换传进来的数据
					angular.extend(user, newUser);
					if (okFn) {
						okFn($modalInstance, user);
					} else {
						$modalInstance.close();
					}
				}
				$scope.cancel = function() {
					if (cancelFn) {
						cancelFn($modalInstance, $scope.user);
					} else {
						$modalInstance.dismiss("cancel");
					}
				}
			}
			return promise;
		}
	}
});
