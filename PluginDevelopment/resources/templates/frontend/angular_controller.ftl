var exampleApp = angular.module('exampleApp.controllers', ['ui.bootstrap']);

// Landing page controller
exampleApp.controller('LandingController', function($scope) {
	// no-op — landing page is purely navigational
});

<#list classes as class>
exampleApp.controller('${class.name}Controller', function($scope, $location, $routeParams, $uibModal,
	<#if class.referencedProperties??>
		<#list class.referencedProperties as prop>
			<#if prop.connectionType?string == "MANY_TO_ONE">
	${prop.type?uncap_first}Service,
			</#if>
		</#list>
	</#if>
	${class.name?uncap_first}Service) {

	$scope.alerts = [];

	$scope.closeAlert = function(index) {
		$scope.alerts.splice(index, 1);
	};

	$scope.addEditHeading = '';

	$scope.initAddEditPage = function() {
		if ($routeParams.id) {
			$scope.addEditHeading = 'Edit';
		} else {
			$scope.addEditHeading = 'Add';
		}
	};

	$scope.getAll = function() {
		${class.name?uncap_first}Service.getAll()
			.then(function(response) {
				$scope.${class.name?uncap_first}List = response.data;
			}, function() {
				$scope.${class.name?uncap_first}List = [];
			});
	};

	$scope.getOne = function() {
		$scope.${class.name?uncap_first} = {};
		<#if class.referencedProperties??>
			<#list class.referencedProperties as prop>
				<#if prop.connectionType?string == "MANY_TO_ONE">
		${prop.type?uncap_first}Service.getAll()
			.then(function(response) {
				$scope.${prop.type?uncap_first}List = response.data;
			});
				</#if>
			</#list>
		</#if>
		if ($routeParams.id) {
			${class.name?uncap_first}Service.getOne($routeParams.id)
				.then(function(response) {
					$scope.${class.name?uncap_first} = response.data;
				}, function() {
					$scope.alerts.push({ msg: '${class.name} not found.', type: 'danger' });
				});
		}
	};

	$scope.remove = function(id) {
		${class.name?uncap_first}Service.remove(id)
			.then(function() {
				$scope.getAll();
				$scope.alerts.push({ msg: '${class.name} deleted successfully.', type: 'success' });
			}, function() {
				$scope.alerts.push({ msg: 'Error deleting ${class.name}.', type: 'danger' });
			});
	};

	<#if class.name == "Appointment">
	// Override getAll to auto-mark past appointments as DONE
	$scope.getAll = function() {
		appointmentService.getAll()
			.then(function(response) {
				var now = new Date();
				response.data.forEach(function(a) {
					if ((a.status === 'WAITING' || a.status === 'CONFIRMED') && new Date(a.dateTime) < now) {
						a.status = 'DONE';
						appointmentService.save(a);
					}
				});
				$scope.appointmentList = response.data;
			}, function() {
				$scope.appointmentList = [];
			});
	};

	$scope.clientData = {};

	$scope.bookAppointment = function() {
		$scope.appointment.status = 'WAITING';
		var emailLower = ($scope.clientData.email || '').toLowerCase().trim();
		clientService.getAll().then(function(res) {
			var existing = res.data.find(function(c) {
				return (c.email || '').toLowerCase().trim() === emailLower;
			});
			if (existing) {
				$scope.appointment.client = existing;
				appointmentService.save($scope.appointment)
					.then(function() {
						$scope.alerts.push({ msg: 'Appointment booked successfully!', type: 'success' });
						$location.path('/myAppointments');
					}, function() {
						$scope.alerts.push({ msg: 'Error booking appointment.', type: 'danger' });
					});
			} else {
				clientService.save($scope.clientData)
					.then(function(response) {
						$scope.appointment.client = response.data;
						appointmentService.save($scope.appointment)
							.then(function() {
								$scope.alerts.push({ msg: 'Appointment booked successfully!', type: 'success' });
								$location.path('/myAppointments');
							}, function() {
								$scope.alerts.push({ msg: 'Error booking appointment.', type: 'danger' });
							});
					}, function() {
						$scope.alerts.push({ msg: 'Error saving client information.', type: 'danger' });
					});
			}
		}, function() {
			$scope.alerts.push({ msg: 'Error checking client information.', type: 'danger' });
		});
	};

	$scope.setStatus = function(id, statusVal) {
		appointmentService.getOne(id)
			.then(function(response) {
				var data = response.data;
				data.status = statusVal;
				appointmentService.save(data)
					.then(function() {
						$scope.getAll();
						if ($scope.myAppointmentList) {
							$scope.getMyAppointments();
						}
						$scope.alerts.push({ msg: 'Appointment status changed to ' + statusVal + '.', type: 'success' });
					}, function() {
						$scope.alerts.push({ msg: 'Error updating appointment status.', type: 'danger' });
					});
			});
	};

	// Client-side: look up appointments by email
	$scope.emailInput = '';
	$scope.myAppointmentList = null;
	$scope.clientNotFound = false;

	$scope.getMyAppointments = function() {
		var emailLower = ($scope.emailInput || '').toLowerCase().trim();
		if (!emailLower) return;
		clientService.getAll().then(function(res) {
			var client = res.data.find(function(c) {
				return (c.email || '').toLowerCase().includes(emailLower);
			});
			if (!client) {
				$scope.clientNotFound = true;
				$scope.myAppointmentList = [];
				return;
			}
			$scope.clientNotFound = false;
			appointmentService.getAll().then(function(r) {
				var now = new Date();
				var mine = r.data.filter(function(a) {
					return a.client && a.client.id === client.id;
				});
				mine.forEach(function(a) {
					if ((a.status === 'WAITING' || a.status === 'CONFIRMED') && new Date(a.dateTime) < now) {
						a.status = 'DONE';
						appointmentService.save(a);
					}
				});
				$scope.myAppointmentList = mine;
			}, function() {
				$scope.myAppointmentList = [];
			});
		}, function() {
			$scope.clientNotFound = false;
			$scope.myAppointmentList = [];
		});
	};
	</#if>

	<#if class.name == "Review">
	$scope.getStars = function(rating) {
		var stars = '';
		for (var i = 0; i < 5; i++) {
			stars += i < rating ? '\u2605' : '\u2606';
		}
		return stars;
	};

	// Client-side: look up client by email before leaving a review
	$scope.search = { email: '' };
	$scope.clientFound = false;
	$scope.clientNotFound = false;

	$scope.loadTreatments = function() {
		treatmentService.getAll().then(function(response) {
			$scope.treatmentList = response.data;
		});
	};

	$scope.getClientByEmail = function() {
		var emailLower = ($scope.search.email || '').toLowerCase().trim();
		if (!emailLower) return;
		clientService.getAll().then(function(res) {
			var client = res.data.find(function(c) {
				return (c.email || '').toLowerCase().includes(emailLower);
			});
			if (!client) {
				$scope.clientNotFound = true;
				$scope.clientFound = false;
				return;
			}
			$scope.clientFound = true;
			$scope.clientNotFound = false;
			$scope.review = $scope.review || {};
			$scope.review.client = client;
		}, function() {
			$scope.clientNotFound = false;
			$scope.clientFound = false;
		});
	};
	</#if>

	$scope.open = function() {
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'myModalContent.html',
			controller: 'addEdit${class.name}ConfirmationController',
			size: 'sm',
			resolve: {
				${class.name?uncap_first}: function() {
					return $scope.${class.name?uncap_first};
				}
			}
		});

		modalInstance.result.then(function() {
			<#if class.name == "Review">
			if ($location.path() === '/leaveReview') {
				$location.path('/treatments');
			} else {
				$location.path('/admin/reviews');
			}
			<#elseif class.name == "Appointment">
			if ($location.path() === '/book') {
				$location.path('/myAppointments');
			} else {
				$location.path('/admin/appointments');
			}
			<#else>
			$location.path('/admin/${class.name?uncap_first}s');
			</#if>
		}, function() {
		});
	};

});

exampleApp.controller('addEdit${class.name}ConfirmationController',
	function($scope, $uibModalInstance, ${class.name?uncap_first}Service, ${class.name?uncap_first}) {

	$scope.${class.name?uncap_first} = ${class.name?uncap_first};

	$scope.confirm = function() {
		$scope.save();
	};

	$scope.revert = function() {
		$uibModalInstance.dismiss();
	};

	$scope.save = function() {
		${class.name?uncap_first}Service.save($scope.${class.name?uncap_first})
			.then(function() {
				$uibModalInstance.close();
			}, function() {
				$uibModalInstance.dismiss();
			});
	};

});
</#list>
