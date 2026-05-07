<!DOCTYPE html>
<html ng-app="exampleApp">
<head>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular-route.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/angular-animate/1.8.3/angular-animate.min.js" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/2.5.6/ui-bootstrap-tpls.min.js"></script>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<script src="js/app.js"></script>
<script src="js/app.controllers.js"></script>
<script src="js/app.services.js"></script>
<script src="js/app.routes.js"></script>

<title>Our Beauty Salon</title>
<style>
  body { background: #fff; color: #333; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Helvetica, Arial, sans-serif; padding-left: 20px; padding-right: 20px; }
  .has-navbar { padding-top: 70px; }
  .navbar { background: #fff !important; border-bottom: 1px solid #f0f0f0; box-shadow: none; border-radius: 0; }
  .navbar-default { border-color: #f0f0f0; }
  .navbar-brand { font-weight: bold; color: #222 !important; letter-spacing: 0; }
  .navbar-default .navbar-nav > li > a { color: #888; font-weight: 400; }
  .navbar-default .navbar-nav > li > a:hover { color: #222; background: transparent; }
  .navbar-default .navbar-nav > .active > a,
  .navbar-default .navbar-nav > .active > a:hover,
  .navbar-default .navbar-nav > .active > a:focus { color: #222; font-weight: bold; background: transparent; }
  .empty-state { text-align: center; padding: 60px 20px; color: #aaa; }
  .empty-state .dots { font-size: 48px; letter-spacing: 6px; display: block; margin-bottom: 10px; }
  .btn-salon { background: #4a4a4a; color: #fff !important; border: none; border-radius: 4px; padding: 8px 16px; }
  .btn-salon:hover, .btn-salon:focus { background: #333; color: #fff !important; text-decoration: none; }
  .btn-salon.btn-block { display: block; width: 100%; text-align: center; }
  .salon-divider { border: none; border-top: 1px solid #eee; margin: 0; }
  .service-img-placeholder { background: #e0e0e0; height: 180px; display: flex; align-items: center; justify-content: center; margin-bottom: 16px; border-radius: 4px; font-size: 42px; color: #bbb; }
  .service-card { margin-bottom: 32px; border: 1px solid #e0e0e0; border-radius: 12px; padding: 20px; }
  .service-card h4 { font-weight: bold; margin-bottom: 8px; }
  .service-card p { color: #555; min-height: 50px; }
  .service-card .service-footer { display: flex; align-items: center; justify-content: space-between; margin-top: 8px; color: #555; }
  .review-row { display: flex; align-items: baseline; padding: 14px 0; gap: 16px; }
  .review-row .review-star { color: #f0ad4e; font-size: 18px; flex-shrink: 0; }
  .review-row .review-rating { flex-shrink: 0; color: #555; }
  .review-row .review-service { flex-shrink: 0; font-weight: 500; min-width: 100px; }
  .review-row .review-comment { flex: 1; font-style: italic; color: #444; }
  .review-row .review-client { flex-shrink: 0; font-style: italic; color: #666; }
  .review-row .review-date { flex-shrink: 0; font-style: italic; color: #999; text-align: right; }
</style>
</head>
<body>
	<!-- Admin navbar — only shown on /admin/* routes -->
	<nav class="navbar navbar-default navbar-fixed-top" ng-if="isAdminRoute()">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="#/">Our Beauty Salon</a>
			</div>
			<div class="collapse navbar-collapse">
				<ul class="nav navbar-nav navbar-right">
					<li ng-class="{active: currentPath() === '/admin/appointments'}"><a href="#/admin/appointments">Appointments</a></li>
					<li ng-class="{active: currentPath() === '/admin/clients'}"><a href="#/admin/clients">Clients</a></li>
					<li ng-class="{active: currentPath() === '/admin/reviews'}"><a href="#/admin/reviews">Reviews</a></li>
					<li ng-class="{active: currentPath() === '/admin/treatments'}"><a href="#/admin/treatments">Treatments</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<!-- Minimal client header — shown on non-admin, non-landing routes -->
	<nav class="navbar navbar-default navbar-fixed-top" ng-if="!isAdminRoute() && currentPath() !== '/'">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="#/">Our Beauty Salon</a>
			</div>
			<div class="collapse navbar-collapse">
				<ul class="nav navbar-nav navbar-right">
					<li ng-class="{active: currentPath() === '/treatments'}"><a href="#/treatments">Treatments</a></li>
					<li ng-class="{active: currentPath() === '/myAppointments'}"><a href="#/myAppointments">My Appointments</a></li>
					<li ng-class="{active: currentPath() === '/leaveReview'}"><a href="#/leaveReview">Leave a Review</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div ng-class="{'has-navbar': currentPath() !== '/', 'container': currentPath() !== '/'}" ng-view></div>

</body>
</html>
