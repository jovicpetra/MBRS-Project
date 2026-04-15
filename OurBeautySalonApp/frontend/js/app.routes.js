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
				.when('/admin/clients', {
			templateUrl: 'partial/clientTable.html',
			controller:  'ClientController'
		})
		.when('/admin/clients/add', {
			templateUrl: 'partial/ClientCreation.html',
			controller:  'ClientController'
		})
		.when('/admin/clients/edit/:id', {
			templateUrl: 'partial/ClientCreation.html',
			controller:  'ClientController'
		})
		.when('/admin/clients/:id', {
			templateUrl: 'partial/ClientView.html',
			controller:  'ClientController'
		})
		.when('/admin/appointments', {
			templateUrl: 'partial/appointmentTable.html',
			controller:  'AppointmentController'
		})
		.when('/admin/appointments/add', {
			templateUrl: 'partial/AppointmentCreation.html',
			controller:  'AppointmentController'
		})
		.when('/admin/appointments/edit/:id', {
			templateUrl: 'partial/AppointmentCreation.html',
			controller:  'AppointmentController'
		})
		.when('/admin/appointments/:id', {
			templateUrl: 'partial/AppointmentView.html',
			controller:  'AppointmentController'
		})
		.when('/admin/treatments', {
			templateUrl: 'partial/treatmentAdmin.html',
			controller:  'TreatmentController'
		})
		.when('/admin/treatments/add', {
			templateUrl: 'partial/TreatmentCreation.html',
			controller:  'TreatmentController'
		})
		.when('/admin/treatments/edit/:id', {
			templateUrl: 'partial/TreatmentCreation.html',
			controller:  'TreatmentController'
		})
		.when('/admin/treatments/:id', {
			templateUrl: 'partial/TreatmentView.html',
			controller:  'TreatmentController'
		})
		.when('/admin/reviews', {
			templateUrl: 'partial/reviewTable.html',
			controller:  'ReviewController'
		})
		.when('/admin/reviews/add', {
			templateUrl: 'partial/ReviewCreation.html',
			controller:  'ReviewController'
		})
		.when('/admin/reviews/edit/:id', {
			templateUrl: 'partial/ReviewCreation.html',
			controller:  'ReviewController'
		})
		.when('/admin/reviews/:id', {
			templateUrl: 'partial/ReviewView.html',
			controller:  'ReviewController'
		})

		.otherwise({
			redirectTo: '/'
		});
}]);
