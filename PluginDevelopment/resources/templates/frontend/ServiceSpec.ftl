describe('${entityName}Service', function() {
	'use strict';

	beforeEach(angular.mock.module('exampleApp.services'));

	var ${entityNameLower}Service;
	var $httpBackend;

	beforeEach(inject(function(_${entityNameLower}Service_, _$httpBackend_) {
		${entityNameLower}Service = _${entityNameLower}Service_;
		$httpBackend = _$httpBackend_;
	}));

	afterEach(function() {
		$httpBackend.verifyNoOutstandingExpectation();
		$httpBackend.verifyNoOutstandingRequest();
	});

	it('getAll_whenInvoked_callsGetEndpoint', function() {
		$httpBackend.expectGET('api/${entityNameLower}').respond(200, []);

		${entityNameLower}Service.getAll();
		$httpBackend.flush();
	});

	it('create_whenEntityHasNoId_callsPostEndpoint', function() {
		var payload = {};
		$httpBackend.expectPOST('api/${entityNameLower}', payload).respond(201, payload);

		${entityNameLower}Service.save(payload);
		$httpBackend.flush();
	});

	it('update_whenEntityHasId_callsPutEndpoint', function() {
		var payload = { id: 1 };
		$httpBackend.expectPUT('api/${entityNameLower}/1', payload).respond(200, payload);

		${entityNameLower}Service.save(payload);
		$httpBackend.flush();
	});

	it('delete_whenIdProvided_callsDeleteEndpoint', function() {
		$httpBackend.expectDELETE('api/${entityNameLower}/1').respond(204);

		${entityNameLower}Service.remove(1);
		$httpBackend.flush();
	});
});
