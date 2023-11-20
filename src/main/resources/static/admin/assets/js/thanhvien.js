app.filter('unsafeHtml', ['$sce', function($sce) {
    return function(val) {
        return $sce.trustAsHtml(val);
    };
}]);

app.controller('ThanhVienRestController', function($scope, $http, $window, $sce) {
    $scope.thanhviens = [];

    $http.get('/rest/thanhviens')
        .then(function(response) {
            $scope.thanhviens = response.data;
        });

    $scope.goToSinglePage = function(maThanhVien) {
        window.location.href = `/admin/member_detail/${maThanhVien}`;
    };

    $scope.loadChiTietThanhVien = function(maThanhVien) {
        $http.get('/rest/thanhviens/' + maThanhVien)
            .then(function(response) {
                $scope.thanhviens = response.data;
            })
            .catch(function(error) {
                console.error("Error:", error);
            });
    };

    $window.onload = function() {
        var maThanhVien = $window.location.pathname.split('/').pop();
        console.log('maThanhVien:', maThanhVien);
        if (maThanhVien) {
            $scope.loadChiTietThanhVien(maThanhVien);
        }
    };
});
