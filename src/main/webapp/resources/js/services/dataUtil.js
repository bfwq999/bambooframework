(function() {
	var datautil = angular.module('datautil', []);
	datautil.provider('DataUtil', function() {
	  // Configuration
	  var Configurer = {};
	  Configurer.init = function(object, config) {
	    object.configuration = config;
	  };
	  
	  var Query = function(){
		  this.$http = $http;
		  this.$q = $q;
		  this.data = {
			  "before":[],
			   "after":[],
			   "operate":[]
		  };
		  this.before = this.data.before;
		  this.after = this.data.after;
		  this.operate = this.data.operate;
	  }
	  var Get = function($http,$q){
	  }
	  Get.prototype.before = function(operateId,tableName){
		  this.before[this.before.length] = operateId;
		  return this;
	  }
	  Get.prototype.add =  function(id,result,where){
		  this.operate[this.operate.length] = {
				 "id":id,
		    	"type":"get",
		    	"result":result,
		    	"WHERE":where
		  }
		  return this;
	  }
	  Get.prototype.submit = function(success,fail){
		  return this.$http.post("get",JSON.stringify(this.data));
	  }
	  
	  
	  this.$get = ['$http', '$q', function($http, $q) {
		  var service = {};
		  /**
		   * 1. tableName,id
		   * 2. tableName,[col1,col2],id
		   * 3. tableName,[col1,col2],{col1:''}
		   */
		  service.get = function(tableName,id){
			  var g = new Get($http,$q);
			  g.add("a",tableName+".*",tableName+".ID_="+id);
			  var promise = g.submit();
			  promise[tableName] = {};
			  var val = {};
			  promise.$object = val;
			  promise.success(function(data){
				  _.extend(val,data[tableName]);
			  })
			  return promise;
		  }
		  
		  service.newQuery = function(){
			  
		  }
		  return service;
	  }];
	});
})();