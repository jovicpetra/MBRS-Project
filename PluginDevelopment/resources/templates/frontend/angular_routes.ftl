var exampleApp = angular.module('exampleApp.routes', ['ngRoute']);

exampleApp.config(['$routeProvider', function($routeProvider) {
	$routeProvider

		// ── Landing ───────────────────────────────────────────────────────────
		.when('/', {
			templateUrl: 'partial/landing.html',
			controller:  'LandingController'
		})

		// ── Client routes ─────────────────────────────────────────────────────
		.when('/treatments', {
			templateUrl: 'partial/treatmentTable.html',
			controller:  'TreatmentController'
		})
		.when('/treatments/:id', {
			templateUrl: 'partial/treatmentDetail.html',
			controller:  'TreatmentController'
		})
		.when('/book/:treatmentId?', {
			templateUrl: 'partial/appointmentCreation.html',
			controller:  'AppointmentController'
		})
		.when('/myAppointments', {
			templateUrl: 'partial/myAppointments.html',
			controller:  'AppointmentController'
		})
		.when('/leaveReview', {
			templateUrl: 'partial/reviewCreation.html',
			controller:  'ReviewController'
		})

		// ── Admin routes ──────────────────────────────────────────────────────
		.when('/admin', {
			redirectTo: '/admin/appointments'
		})
		<#list classes as class>
		<#assign canCreate = !(class.uiClass??) || (class.uiClass.create!true) />
		<#assign canUpdate = !(class.uiClass??) || (class.uiClass.update!true) />
		<#assign canView   = !(class.uiClass??) || (class.uiClass.view!true)   />
		.when('/admin/${class.name?uncap_first}s', {
			<#if class.name == "Treatment">
			templateUrl: 'partial/treatmentAdmin.html',
			<#else>
			templateUrl: 'partial/${class.name?uncap_first}Table.html',
			</#if>
			controller:  '${class.name}Controller'
		})
		<#if canCreate>
		.when('/admin/${class.name?uncap_first}s/add', {
			templateUrl: 'partial/${class.name}Creation.html',
			controller:  '${class.name}Controller'
		})
		</#if>
		<#if canUpdate>
		.when('/admin/${class.name?uncap_first}s/edit/:id', {
			templateUrl: 'partial/${class.name}Creation.html',
			controller:  '${class.name}Controller'
		})
		</#if>
		<#if canView>
		.when('/admin/${class.name?uncap_first}s/:id', {
			templateUrl: 'partial/${class.name}View.html',
			controller:  '${class.name}Controller'
		})
		</#if>
		</#list>

		.otherwise({
			redirectTo: '/'
		});
}]);
