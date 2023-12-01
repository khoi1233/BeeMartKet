
app.controller('khuyenmaiadmin', function($scope, $http, $window, $sce) {
	$scope.khuyenmais = [];
alert("khôi")
	$http.get('/rest/voucher')
		.then(function(response) {
			$scope.khuyenmais = response.data;
		});

	

});
