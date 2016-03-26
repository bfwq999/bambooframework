define([],function(){
	var myAppModule = angular.module('myApp.restHttpService', []);
	myAppModule.factory('resthttp', ["$q","$http",function($q,$http) {
		
		var r20 = /%20/g;
		var rbracket = /\[\]$/;
		
		/**
		 * 判断是否是Array
		 */
		function isArray(obj) {   
			  return Object.prototype.toString.call(obj) === '[object Array]';    
		}  
		
		/**
		 * angularjs默认把对象解析成json串,springmvc接收不到值,
		 * 所以需要将对象解析成name1=value1;name2=value2形式
		 */
		var formatParam = function(a) {
			if(a == null){
				return '';
			}
			var prefix,
				s = [],
				add = function(key, value) {
					// If value is a function, invoke it and return its value
					value = (typeof value == 'function') ? value() : (value == null ? "" : value);
					s[s.length] = encodeURIComponent(key) + "=" + encodeURIComponent(value);
				};
			// If an array was passed in, assume that it is an array of form
			// elements.
			if (isArray(a)) {
				// Serialize the form elements
				for (var i = 0; i < a.length; i++) {
					add(i, a[i]);
				}
			} else {
				// did it), otherwise encode params recursively.
				for (prefix in a) {
					buildParams(prefix, a[prefix], add);
				}
			}
			// Return the resulting serialization
			return s.join("&").replace(r20, "+");
		};
	
		function buildParams(prefix, obj, add) {
			var name;
			if (isArray(obj)) {
				// Serialize array item.
				for (var i = 0; i < obj.length; i++) {
					var v = obj[i];
					if (rbracket.test(prefix)) {
						// Treat each array item as a scalar.
						add(prefix, v);
					} else {
						// If array item is non-scalar (array or object), encode its
						// numeric index to resolve deserialization ambiguity
						// issues.
						// Note that rack (as of 1.0.0) can't currently deserialize
						// nested arrays properly, and attempting to do so may cause
						// a server error. Possible fixes are to modify rack's
						// deserialization algorithm or to provide an option or flag
						// to force array serialization to be shallow.
						buildParams(prefix + "[" + i + "]", v, add);
					}
				}
			} else if (typeof obj == "object") {
				// Serialize object item.
				for (name in obj) {
					buildParams(prefix + "." + name , obj[name], add);
				}
			} else {
				// Serialize scalar item.
				add(prefix, obj);
			}
		}
		
	    /** 将参数组装到url中 */
	    var formatUrl =  function(url,param){
	    	if(param!=null){
	    		var pre = "?";
	    		if(url.indexOf("?")>-1){
	    			pre = "&";
	    		}
	    		url += pre+formatParam(param);
	    	}
			return url;
	    }
	    
	    var headers = {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'};
		return {
			"post":function(url,param){
			    return $http.post(url,formatParam(param),{
			    	"headers": headers
			    });
			},
			"put":function(url,param){
				return $http.put(url,formatParam(param),{
					"headers": headers
				});
			},
			"get":function(modul,id){
				if(id!=null){
					return $http.get("/"+modul+"/"+id,{
				    	"headers": headers
				    });
				}
				return $http.get(modul,{
			    	"headers": headers
			    });
				
			},
			"delete":function(modul,id){
				return $http['delete']("/"+modul+"/"+id,{
			    	"headers": headers
			    });
			},
			"query":function(modul,param){
				return $http.get(formatUrl("/"+modul,param),{
			    	"headers": headers
			    });
			},
			"insert":function(modul,param){
				return this.put("/"+modul,param);
			},
			"update":function(modul,param){
				return this.post("/"+modul,param);
			},
			"save":function(modul,param){
				return this.post("/"+modul,param);
			}
		}
	}])
});