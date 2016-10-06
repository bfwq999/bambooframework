/**
 * 机构信息对话框
 */
define([], function() {
	return function($modal) {
		return function openEditOrgDialog(title, parentOrgName, org) {
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
				templateUrl : 'views/auth/org-edit.html',
				backdrop : 'static',
				controller : [ '$scope', orgDlgCtrl ]
			});
			function orgDlgCtrl($scope) {
				$scope.title = title;
				$scope.parentOrgName = parentOrgName;

				// 备份org
				var newOrg = angular.extend({}, org);

				$scope.org = newOrg;
				$scope.ok = function() {
					// 替换传进来的数据
					angular.extend(org, newOrg);
					if (okFn) {
						okFn($modalInstance, org);
					} else {
						$modalInstance.close();
					}
				}
				$scope.cancel = function() {
					if (cancelFn) {
						cancelFn($modalInstance, $scope.org);
					} else {
						$modalInstance.dismiss("cancel");
					}
				}
			}
			return promise;
		}
	}
});