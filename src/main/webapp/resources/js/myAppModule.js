var defines = [
  ['myApp.restHttpService','js/services/restHttpService'],             
  ['myApp.httpInterceptor','js/services/httpInterceptor'],             
  ['myApp.treeDirective','js/directives/treeDirective'],             
  ['myApp.ztreeDirective','js/directives/ztreeDirective'],            
  ['myApp.orgTreeDirective','js/directives/orgTreeDirective'],            
  ['myApp.navigateDirective','js/directives/navigateDirective'],            
  ['myApp.roleTreeDirective','js/directives/roleTreeDirective'],            
  ['myApp.dialogs','js/dialogs/dialogs'],            
  ['myApp.filters','js/filters/myFilters'],            
  ['myApp.util','js/services/util']            
];

var defAddrs = [];
var defNames = [];
for(var i=0; i<defines.length;  i++){
	defNames.push(defines[i][0]);
	defAddrs.push(defines[i][1]);
}
define(defAddrs,function() {return angular.module('myApp', defNames);});