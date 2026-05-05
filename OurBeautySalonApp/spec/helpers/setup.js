// Set up a browser-like DOM environment for AngularJS
const { JSDOM } = require('jsdom');

const dom = new JSDOM('<!DOCTYPE html><html><body></body></html>', {
  url: 'http://localhost'
});

global.window    = dom.window;
global.document  = dom.window.document;
global.navigator = dom.window.navigator;
global.Element   = dom.window.Element;

// Load the UMD bundle directly; it initializes window.angular
require('angular/angular');
global.angular = window.angular;
require('angular-route/angular-route');
// angular-mocks initializes helpers only when window.jasmine exists
window.jasmine = jasmine;
window.beforeEach = beforeEach;
window.afterEach = afterEach;
require('angular-mocks/angular-mocks');
global.inject = window.inject;

// Stub ui.bootstrap â€” the module declaration must exist but its
// directives/services are fully mocked in tests, so an empty module is enough
angular.module('ui.bootstrap', []);

// Load all application modules so specs can call module('exampleApp.controllers') etc.
require('../../frontend/js/app.js');
require('../../frontend/js/app.controllers.js');
require('../../frontend/js/app.services.js');
require('../../frontend/js/app.routes.js');
global.inject = angular.mock.inject;
