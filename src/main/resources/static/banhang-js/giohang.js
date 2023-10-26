
app.controller('giohang-controller', function($scope, $http, $window) {
	$scope.cartItems = [];
	$scope.chiTietGioHang = {};

	$http.get('/rest/3/chitietgiohang').then(function(response) {
		$scope.cartItems = response.data;
	});


	$scope.addToCart = function(masp) {
		// Tìm sản phẩm có giá trị "maSanPham" tương ứng trong dữ liệu 
		var selectedItem = $scope.cartItems.find(function(item) {
			return item.sanPham.maSanPham === masp;
		});

		if (selectedItem) {
			// Thêm sản phẩm đã chọn vào giỏ hàng
			// $scope.cartItems.push(selectedItem);
			// Bạn có thể thêm logic bổ sung ở đây, ví dụ: cập nhật số lượng hoặc hiển thị thông báo.
			alert("sản phẩm đã tồn tại")
		} else {
			// Xử lý trường hợp khi không tìm thấy sản phẩm với "maSanPham" cụ thể.
			$http.post('/add/3/' + masp).then(function(response) {
				// Xử lý phản hồi, ví dụ: cập nhật mảng cartItems
				var newCartItem = response.data;
				$scope.cartItems.push(newCartItem);
			});
			alert(masp);
		}
	};

	$scope.remove = function(maChiTietGH) {
		$http.delete('/delete/' + maChiTietGH).then(function(response) {
			// Xóa sản phẩm khỏi mảng cartItems sau khi xóa thành công
			for (var i = 0; i < $scope.cartItems.length; i++) {
				if ($scope.cartItems[i].maChiTietGH === maChiTietGH) {
					$scope.cartItems.splice(i, 1);
					break; // Dừng vòng lặp sau khi xóa sản phẩm
				}
			}
		});

		alert(maChiTietGH);
	};



});