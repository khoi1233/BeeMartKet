app = angular.module("admin-app", ["ngRoute"]);

app.config(function($routeProvider) {
	$routeProvider
		.when("/indexAdmin", {
			templateUrl: "/templates/admin/view/index.html",
			controller: "indexAdmin-ctrl"
		})
		
		.otherwise({
			templateUrl: "/templates/admins/index.html",
			controller: "indexAdmin-ctrl"
		});
})