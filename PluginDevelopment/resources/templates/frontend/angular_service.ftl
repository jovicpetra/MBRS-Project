var exampleApp = angular.module('exampleApp.services', []);

<#list classes as class>
exampleApp.service('${class.name?uncap_first}Service', function($http) {

	this.url = 'api/${class.name?uncap_first}';

	this.getAll = function() {
		return $http.get(this.url);
	};

	this.getOne = function(id) {
		return $http.get(this.url + '/' + id);
	};

	this.save = function(${class.name?uncap_first}) {
		if (${class.name?uncap_first}.id) {
			return $http.put(this.url + '/' + ${class.name?uncap_first}.id, ${class.name?uncap_first});
		} else {
			return $http.post(this.url, ${class.name?uncap_first});
		}
	};

	this.remove = function(id) {
		return $http.delete(this.url + '/' + id);
	};

});
</#list>
