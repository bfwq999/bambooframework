/**
 * 岗位对话框
 */
define([], function() {
	return function($modal) {
		return function openEditPositionDialog(dlgTitle, orgName, position) {
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
				templateUrl : 'views/auth/position-edit.html',
				backdrop : 'static',
				controller : [ '$scope', dlgCtrl ]
			});
			function dlgCtrl($scope) {
				$scope.title = dlgTitle;
				$scope.orgName = orgName;
				var newPosition = angular.extend({}, position);
				$scope.position = newPosition;
				$scope.ok = function() {
					// 替换传进来的数据
					angular.extend(position, newPosition);
					if (okFn) {
						okFn($modalInstance, position);
					} else {
						$modalInstance.close();
					}
				}
				$scope.cancel = function() {
					if (cancelFn) {
						cancelFn($modalInstance, $scope.position);
					} else {
						$modalInstance.dismiss("cancel");
					}
				}
			}
			return promise;
		}
	}
});