angular.module("myApp.icheckDirective", []).directive("myIcheck", function() {
	return {
		restrict: 'A',
		require: '?ngModel',
		link: function($scope, $element, $attrs, $ngModel) {
			if (!$ngModel) {
				return;
			}
			
			$scope.$watch($attrs['ngModel'], function(newValue){
                $($element).iCheck('update');
            });

			//using iCheck
			$($element).iCheck({
				labelHover: true,
				cursor: true,
				checkboxClass: 'icheckbox_square-blue',
				radioClass: 'iradio_square-blue',
				increaseArea: '20%'
			}).on('ifClicked', function(event) {
				var val;
				if ($attrs.type == "checkbox") {
					//checkbox, $ViewValue = true/false/undefined
					val = !($ngModel.$modelValue == undefined ? false : $ngModel.$modelValue)
				} else {
					// radio, $ViewValue = $attrs.value	
					val = $attrs.value;
				}
				$scope.$apply(function() {
					$ngModel.$setViewValue(val);
				});
			});
		}
	};
});