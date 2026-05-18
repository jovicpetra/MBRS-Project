describe('${entityName}Controller', function() {
	'use strict';

	beforeEach(angular.mock.module('exampleApp.controllers'));

	var $controller;
	var $rootScope;
	var $q;
	var deferred;
	var $scope;
	var $uibModal;
	var $location;
	var $routeParams;
	var ${entityNameLower}Service;
	<#if entityNameLower != "client">
	var clientService;
	</#if>
	<#if entityNameLower != "appointment">
	var appointmentService;
	</#if>
	<#if entityNameLower != "treatment">
	var treatmentService;
	</#if>
	<#if entityNameLower != "review">
	var reviewService;
	</#if>

	beforeEach(inject(function(_$controller_, _$rootScope_, _$q_) {
		$controller = _$controller_;
		$rootScope = _$rootScope_;
		$q = _$q_;
		$scope = $rootScope.$new();
		$location = { path: jasmine.createSpy('path') };
		$routeParams = {};
		$uibModal = { open: jasmine.createSpy('open').and.returnValue({ result: $q.resolve() }) };

		deferred = $q.defer();
		${entityNameLower}Service = {
			getAll: jasmine.createSpy('getAll').and.returnValue(deferred.promise),
			getOne: jasmine.createSpy('getOne').and.returnValue($q.resolve({ data: {} })),
			save: jasmine.createSpy('save').and.returnValue($q.resolve({ data: {} })),
			remove: jasmine.createSpy('remove').and.returnValue($q.resolve())
		};
		<#if entityNameLower != "client">
		clientService = {
			getAll: jasmine.createSpy('getAll').and.returnValue($q.resolve({ data: [] })),
			getOne: jasmine.createSpy('getOne').and.returnValue($q.resolve({ data: {} })),
			save: jasmine.createSpy('save').and.returnValue($q.resolve({ data: {} })),
			remove: jasmine.createSpy('remove').and.returnValue($q.resolve())
		};
		</#if>
		<#if entityNameLower != "appointment">
		appointmentService = {
			getAll: jasmine.createSpy('getAll').and.returnValue($q.resolve({ data: [] })),
			getOne: jasmine.createSpy('getOne').and.returnValue($q.resolve({ data: {} })),
			save: jasmine.createSpy('save').and.returnValue($q.resolve({ data: {} })),
			remove: jasmine.createSpy('remove').and.returnValue($q.resolve())
		};
		</#if>
		<#if entityNameLower != "treatment">
		treatmentService = {
			getAll: jasmine.createSpy('getAll').and.returnValue($q.resolve({ data: [] })),
			getOne: jasmine.createSpy('getOne').and.returnValue($q.resolve({ data: {} })),
			save: jasmine.createSpy('save').and.returnValue($q.resolve({ data: {} })),
			remove: jasmine.createSpy('remove').and.returnValue($q.resolve())
		};
		</#if>
		<#if entityNameLower != "review">
		reviewService = {
			getAll: jasmine.createSpy('getAll').and.returnValue($q.resolve({ data: [] })),
			getOne: jasmine.createSpy('getOne').and.returnValue($q.resolve({ data: {} })),
			save: jasmine.createSpy('save').and.returnValue($q.resolve({ data: {} })),
			remove: jasmine.createSpy('remove').and.returnValue($q.resolve())
		};
		</#if>

		$controller('${entityName}Controller', {
			$scope: $scope,
			$location: $location,
			$routeParams: $routeParams,
			$uibModal: $uibModal,
			<#if entityNameLower != "client">
			clientService: clientService,
			</#if>
			<#if entityNameLower != "appointment">
			appointmentService: appointmentService,
			</#if>
			<#if entityNameLower != "treatment">
			treatmentService: treatmentService,
			</#if>
			<#if entityNameLower != "review">
			reviewService: reviewService,
			</#if>
			${entityNameLower}Service: ${entityNameLower}Service
		});
	}));

	it('controllerDefined_whenInitialized_isDefined', function() {
		expect($scope).toBeDefined();
	});

	it('getAll_whenServiceReturnsData_populatesScope', function() {
		var payload = [{ id: 1 }];

		$scope.getAll();
		deferred.resolve({ data: payload });
		$scope.$digest();

		expect($scope.${entityNameLower}List).toEqual(payload);
	});

	it('open_whenInvoked_opensConfirmationModal', function() {
		$scope.${entityNameLower} = { id: null };
		$scope.open();

		expect($uibModal.open).toHaveBeenCalled();
	});
});
