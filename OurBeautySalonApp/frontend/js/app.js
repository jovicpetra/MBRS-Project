var exampleApp = angular.module('exampleApp', [
	'exampleApp.controllers',
	'exampleApp.services',
	'exampleApp.routes',
	'ngRoute',
	'ui.bootstrap'
]);

exampleApp.config(['$locationProvider', function($locationProvider) {
	$locationProvider.hashPrefix('');
}]);

exampleApp.run(['$rootScope', '$location', function($rootScope, $location) {
	$rootScope.isAdminRoute = function() {
		return $location.path().indexOf('/admin') === 0;
	};
	$rootScope.currentPath = function() {
		return $location.path();
	};
}]);
