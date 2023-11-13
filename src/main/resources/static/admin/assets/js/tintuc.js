

app.filter('unsafeHtml', ['$sce', function($sce) {
    return function(val) {
        return $sce.trustAsHtml(val);
    };
}]);

app.controller('TinTucRestController', function($scope, $http, $window, $sce) {
    $scope.tintuc = [];
    $scope.tintuc = {};

    $http.get('/rest/tintuc')
        .then(function(response) {
            $scope.tintuc = response.data;
        });

    $scope.goToSinglePage = function(maTinTuc) {
        window.location.href = `/admin/news_detail/${maTinTuc}`;
    };

    $scope.loadChiTietTinTuc = function(maTinTuc) {
        $http.get('/rest/tintuc/' + maTinTuc)
            .then(function(response) {
                $scope.tintuc = response.data;
            })
            .catch(function(error) {
                console.error("Error:", error);
            });
    };

    $window.onload = function() {
        var maTinTuc = $window.location.pathname.split('/').pop();
        console.log('maTinTuc:', maTinTuc);
        if (maTinTuc) {
            $scope.loadChiTietTinTuc(maTinTuc);
        }
    };
});
