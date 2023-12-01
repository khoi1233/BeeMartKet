app.controller('chitietHoadon', function($scope, $http, $window, $sce) {
    	$scope.hoadonCT = [];
    	
    	
    $scope.loadchiTietHoaDon = function(maHoaDon) {
    $http.get('/rest/chitiet/' + maHoaDon).then(function(response) {
        if (response.data) {
            $scope.hoadonCT = response.data;
            console.log("áaaa" + $scope.hoadonCT);
            console.log(response.data);
        } else {
            console.error('Lỗi khi nhận dữ liệu hóa đơn từ server.');
        }
    }, function(error) {
        console.error('Lỗi khi gửi yêu cầu đến server:', error);
    });
};

	window.onload = function() {
		var ma = window.location.pathname.split('/').pop();
		if (ma) {
			$scope.loadchiTietHoaDon(ma);
		}



	}
});