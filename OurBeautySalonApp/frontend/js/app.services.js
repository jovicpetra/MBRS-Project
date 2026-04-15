var exampleApp = angular.module('exampleApp.services', []);

exampleApp.service('clientService', function($http) {

	this.url = 'api/client';

	this.getAll = function() {
		return $http.get(this.url);
	};

	this.getOne = function(id) {
		return $http.get(this.url + '/' + id);
	};

	this.save = function(client) {
		if (client.id) {
			return $http.put(this.url + '/' + client.id, client);
		} else {
			return $http.post(this.url, client);
		}
	};

	this.remove = function(id) {
		return $http.delete(this.url + '/' + id);
	};

});
exampleApp.service('appointmentService', function($http) {

	this.url = 'api/appointment';

	this.getAll = function() {
		return $http.get(this.url);
	};

	this.getOne = function(id) {
		return $http.get(this.url + '/' + id);
	};

	this.save = function(appointment) {
		if (appointment.id) {
			return $http.put(this.url + '/' + appointment.id, appointment);
		} else {
			return $http.post(this.url, appointment);
		}
	};

	this.remove = function(id) {
		return $http.delete(this.url + '/' + id);
	};

});
exampleApp.service('treatmentService', function($http) {

	this.url = 'api/treatment';

	this.getAll = function() {
		return $http.get(this.url);
	};

	this.getOne = function(id) {
		return $http.get(this.url + '/' + id);
	};

	this.save = function(treatment) {
		if (treatment.id) {
			return $http.put(this.url + '/' + treatment.id, treatment);
		} else {
			return $http.post(this.url, treatment);
		}
	};

	this.remove = function(id) {
		return $http.delete(this.url + '/' + id);
	};

});
exampleApp.service('reviewService', function($http) {

	this.url = 'api/review';

	this.getAll = function() {
		return $http.get(this.url);
	};

	this.getOne = function(id) {
		return $http.get(this.url + '/' + id);
	};

	this.save = function(review) {
		if (review.id) {
			return $http.put(this.url + '/' + review.id, review);
		} else {
			return $http.post(this.url, review);
		}
	};

	this.remove = function(id) {
		return $http.delete(this.url + '/' + id);
	};

});
