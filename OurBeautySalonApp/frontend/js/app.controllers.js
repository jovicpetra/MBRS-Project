var exampleApp = angular.module('exampleApp.controllers', ['ui.bootstrap']);

// Landing page controller
exampleApp.controller('LandingController', function($scope) {
	// no-op — landing page is purely navigational
});

exampleApp.controller('ClientController', function($scope, $location, $routeParams, $uibModal,
	appointmentService,
	reviewService,
	clientService) {

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
		clientService.getAll()
			.then(function(response) {
				$scope.clientList = response.data;
			}, function() {
				$scope.clientList = [];
			});
	};

	$scope.getOne = function() {
		$scope.client = {};
		if ($routeParams.id) {
			clientService.getOne($routeParams.id)
				.then(function(response) {
					$scope.client = response.data;
				}, function() {
					$scope.alerts.push({ msg: 'Client not found.', type: 'danger' });
				});
		}
	};

	$scope.remove = function(id) {
		clientService.remove(id)
			.then(function() {
				$scope.getAll();
				$scope.alerts.push({ msg: 'Client deleted successfully.', type: 'success' });
			}, function() {
				$scope.alerts.push({ msg: 'Error deleting Client.', type: 'danger' });
			});
	};


	$scope.loadClientDetails = function() {
		if (!$routeParams.id) return;
		clientService.getOne($routeParams.id).then(function(response) {
			$scope.client = response.data;
			appointmentService.getAll().then(function(res) {
				$scope.clientAppointments = res.data.filter(function(a) {
					return a.client && a.client.id === $scope.client.id;
				});
			});
			reviewService.getAll().then(function(res) {
				$scope.clientReviews = res.data.filter(function(r) {
					return r.client && r.client.id === $scope.client.id;
				});
			});
		});
	};



	$scope.open = function() {
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'myModalContent.html',
			controller: 'addEditClientConfirmationController',
			size: 'sm',
			resolve: {
				client: function() {
					return $scope.client;
				}
			}
		});

		modalInstance.result.then(function() {
			$location.path('/admin/clients');
		}, function() {
		});
	};

});

exampleApp.controller('addEditClientConfirmationController',
	function($scope, $uibModalInstance, clientService, client) {

	$scope.client = client;

	$scope.confirm = function() {
		$scope.save();
	};

	$scope.revert = function() {
		$uibModalInstance.dismiss();
	};

	$scope.save = function() {
		clientService.save($scope.client)
			.then(function() {
				$uibModalInstance.close();
			}, function() {
				$uibModalInstance.dismiss();
			});
	};

});
exampleApp.controller('AppointmentController', function($scope, $location, $routeParams, $uibModal,
	clientService,
	treatmentService,
	appointmentService) {

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
		appointmentService.getAll()
			.then(function(response) {
				$scope.appointmentList = response.data;
			}, function() {
				$scope.appointmentList = [];
			});
	};

	$scope.getOne = function() {
		$scope.appointment = {};
		clientService.getAll()
			.then(function(response) {
				$scope.clientList = response.data;
			});
		treatmentService.getAll()
			.then(function(response) {
				$scope.treatmentList = response.data;
				if ($routeParams.treatmentId) {
					var preselected = response.data.find(function(t) {
						return t.id === parseInt($routeParams.treatmentId);
					});
					if (preselected) {
						$scope.appointment.treatment = preselected;
					}
				}
			});
		if ($routeParams.id) {
			appointmentService.getOne($routeParams.id)
				.then(function(response) {
					$scope.appointment = response.data;
				}, function() {
					$scope.alerts.push({ msg: 'Appointment not found.', type: 'danger' });
				});
		}
	};

	$scope.remove = function(id) {
		appointmentService.remove(id)
			.then(function() {
				$scope.getAll();
				$scope.alerts.push({ msg: 'Appointment deleted successfully.', type: 'success' });
			}, function() {
				$scope.alerts.push({ msg: 'Error deleting Appointment.', type: 'danger' });
			});
	};



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
		var appointmentDate = new Date($scope.appointment.dateTime);
		if (!$scope.appointment.dateTime || appointmentDate <= new Date()) {
			$scope.alerts.push({ msg: 'Appointment must be scheduled in the future.', type: 'danger' });
			return;
		}

		var DURATION_MS = 60 * 60 * 1000; // 1 hour per appointment
		var newStart = appointmentDate.getTime();
		var newEnd = newStart + DURATION_MS;

		appointmentService.getAll().then(function(allRes) {
			var active = allRes.data.filter(function(a) {
				return a.status === 'WAITING' || a.status === 'CONFIRMED';
			});
			var overlap = active.some(function(a) {
				var existStart = new Date(a.dateTime).getTime();
				var existEnd = existStart + DURATION_MS;
				return newStart < existEnd && newEnd > existStart;
			});
			if (overlap) {
				$scope.alerts.push({ msg: 'This time slot overlaps with an existing appointment. Please choose a different time.', type: 'danger' });
				return;
			}

			$scope.proceedWithBooking();
		}, function() {
			$scope.alerts.push({ msg: 'Error checking appointment availability.', type: 'danger' });
		});
	};

	$scope.proceedWithBooking = function() {
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


	$scope.open = function() {
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'myModalContent.html',
			controller: 'addEditAppointmentConfirmationController',
			size: 'sm',
			resolve: {
				appointment: function() {
					return $scope.appointment;
				}
			}
		});

		modalInstance.result.then(function() {
			if ($location.path() === '/book') {
				$location.path('/myAppointments');
			} else {
				$location.path('/admin/appointments');
			}
		}, function() {
		});
	};

});

exampleApp.controller('addEditAppointmentConfirmationController',
	function($scope, $uibModalInstance, appointmentService, appointment) {

	$scope.appointment = appointment;

	$scope.confirm = function() {
		$scope.save();
	};

	$scope.revert = function() {
		$uibModalInstance.dismiss();
	};

	$scope.save = function() {
		appointmentService.save($scope.appointment)
			.then(function() {
				$uibModalInstance.close();
			}, function() {
				$uibModalInstance.dismiss();
			});
	};

});
exampleApp.controller('TreatmentController', function($scope, $location, $routeParams, $uibModal,
	reviewService,
	treatmentService) {

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
		treatmentService.getAll()
			.then(function(response) {
				$scope.treatmentList = response.data;
			}, function() {
				$scope.treatmentList = [];
			});
	};

	$scope.getOne = function() {
		$scope.treatment = {};
		if ($routeParams.id) {
			treatmentService.getOne($routeParams.id)
				.then(function(response) {
					$scope.treatment = response.data;
				}, function() {
					$scope.alerts.push({ msg: 'Treatment not found.', type: 'danger' });
				});
		}
	};

	$scope.remove = function(id) {
		treatmentService.remove(id)
			.then(function() {
				$scope.getAll();
				$scope.alerts.push({ msg: 'Treatment deleted successfully.', type: 'success' });
			}, function() {
				$scope.alerts.push({ msg: 'Error deleting Treatment.', type: 'danger' });
			});
	};

	$scope.getStars = function(rating) {
		var stars = '';
		for (var i = 0; i < 5; i++) {
			stars += i < rating ? '\u2605' : '\u2606';
		}
		return stars;
	};

	$scope.loadTreatmentDetail = function() {
		if (!$routeParams.id) return;
		treatmentService.getOne($routeParams.id)
			.then(function(response) {
				$scope.selectedTreatment = response.data;
				reviewService.getAll().then(function(res) {
					$scope.treatmentReviews = res.data.filter(function(r) {
						return r.treatment && r.treatment.id === $scope.selectedTreatment.id;
					});
					if ($scope.treatmentReviews.length > 0) {
						var sum = $scope.treatmentReviews.reduce(function(acc, r) { return acc + r.rating; }, 0);
						$scope.averageRating = (sum / $scope.treatmentReviews.length).toFixed(1);
					}
				});
			}, function() {
				$scope.alerts.push({ msg: 'Treatment not found.', type: 'danger' });
			});
	};




	$scope.open = function() {
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'myModalContent.html',
			controller: 'addEditTreatmentConfirmationController',
			size: 'sm',
			resolve: {
				treatment: function() {
					return $scope.treatment;
				}
			}
		});

		modalInstance.result.then(function() {
			$location.path('/admin/treatments');
		}, function() {
		});
	};

});

exampleApp.controller('addEditTreatmentConfirmationController',
	function($scope, $uibModalInstance, treatmentService, treatment) {

	$scope.treatment = treatment;

	$scope.confirm = function() {
		$scope.save();
	};

	$scope.revert = function() {
		$uibModalInstance.dismiss();
	};

	$scope.save = function() {
		treatmentService.save($scope.treatment)
			.then(function() {
				$uibModalInstance.close();
			}, function() {
				$uibModalInstance.dismiss();
			});
	};

});
exampleApp.controller('ReviewController', function($scope, $location, $routeParams, $uibModal,
	clientService,
	treatmentService,
	reviewService) {

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
		reviewService.getAll()
			.then(function(response) {
				$scope.reviewList = response.data;
			}, function() {
				$scope.reviewList = [];
			});
	};

	$scope.getOne = function() {
		$scope.review = {};
		clientService.getAll()
			.then(function(response) {
				$scope.clientList = response.data;
			});
		treatmentService.getAll()
			.then(function(response) {
				$scope.treatmentList = response.data;
			});
		if ($routeParams.id) {
			reviewService.getOne($routeParams.id)
				.then(function(response) {
					$scope.review = response.data;
				}, function() {
					$scope.alerts.push({ msg: 'Review not found.', type: 'danger' });
				});
		}
	};

	$scope.remove = function(id) {
		reviewService.remove(id)
			.then(function() {
				$scope.getAll();
				$scope.alerts.push({ msg: 'Review deleted successfully.', type: 'success' });
			}, function() {
				$scope.alerts.push({ msg: 'Error deleting Review.', type: 'danger' });
			});
	};




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

	$scope.open = function() {
		var modalInstance = $uibModal.open({
			animation: true,
			templateUrl: 'myModalContent.html',
			controller: 'addEditReviewConfirmationController',
			size: 'sm',
			resolve: {
				review: function() {
					return $scope.review;
				}
			}
		});

		modalInstance.result.then(function() {
			if ($location.path() === '/leaveReview') {
				$location.path('/treatments');
			} else {
				$location.path('/admin/reviews');
			}
		}, function() {
		});
	};

});

exampleApp.controller('addEditReviewConfirmationController',
	function($scope, $uibModalInstance, reviewService, review) {

	$scope.review = review;

	$scope.confirm = function() {
		$scope.save();
	};

	$scope.revert = function() {
		$uibModalInstance.dismiss();
	};

	$scope.save = function() {
		reviewService.save($scope.review)
			.then(function() {
				$uibModalInstance.close();
			}, function() {
				$uibModalInstance.dismiss();
			});
	};

});
