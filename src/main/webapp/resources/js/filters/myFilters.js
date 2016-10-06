define([], function() {
	angular.module('myApp.filters', []).filter('selModel', function() {
		return function(input,codeName) {
			// input是我们传入的字符串
			if (input) {
				var dotPos = codeName.indexOf(".");
				var pre = dotPos == -1?"tb":codeName.substring(0,dotPos);
				codeName = codeName.substring(dotPos+1);
				if(pre == "tb" ){
					//没有前缀或前缀为tb时
					var values = StaticCode[codeName];
					if(!values){
						return input;
					}
					for(var i=0; i<values.length; i++){
						if(values[i].value==input){
							return values[i].text;
						}
					}
				}
			}
			return input;
		}
	});
});