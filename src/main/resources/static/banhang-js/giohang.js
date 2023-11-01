
app.controller('giohang-controller', function($scope, $http, $window) {
	$scope.cartItems = [];
	$scope.chiTietGioHang = {};

	$http.get('/rest/4/chitietgiohang').then(function(response) {
		$scope.cartItems = response.data;
	});


	$scope.addToCart = function(masp) {
		// Tìm sản phẩm có giá trị "maSanPham" tương ứng trong dữ liệu 
		var selectedItem = $scope.cartItems.find(function(item) {
			return item.sanPham.maSanPham === masp;
		});

		if (selectedItem) {
			//nếu có sản phẩm
			selectedItem.soLuong += 1;
			$scope.update(selectedItem);
<<<<<<< HEAD
			
			
=======

>>>>>>> a52fef4fd03b58834c1361801eadc0144fe5f842
		} else {
			// Xử lý trường hợp khi không tìm thấy sản phẩm với "maSanPham" cụ thể.
			$http.post('/add/4/' + masp).then(function(response) {
				// Xử lý phản hồi, ví dụ: cập nhật mảng cartItems
				var newCartItem = response.data;
				$scope.cartItems.push(newCartItem);
				$scope.updateCartItemCount();
			});

		}
		Swal.fire({
			position: 'top-end',
			icon: 'success',
			title: 'Sản phẩm đã được thêm vào giỏ',
			showConfirmButton: false,
			timer: 1200
		})
	};

	// Hàm cập nhật sản phẩm trong cơ sở dữ liệu 
<<<<<<< HEAD
    $scope.update = function(item) {
        var url = '/4/update/' + item.maChiTietGH; 
        
=======
	$scope.update = function(item) {
		var url = '/3/update/' + item.maChiTietGH;
>>>>>>> a52fef4fd03b58834c1361801eadc0144fe5f842


		$http.put(url, item)
			.then(function(response) {
				// Xử lý phản hồi từ máy chủ nếu cần
				console.log("Cập nhật sản phẩm thành công");
			})
			.catch(function(error) {
				// Xử lý lỗi nếu có
				console.error("Lỗi khi cập nhật sản phẩm:", error);
			});
	};

	$scope.remove = function(maChiTietGH, tenSanPham) {
		Swal.fire({
			title: 'Bạn muốn xóa sản phẩm này?',
			text: tenSanPham,
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#119744',
			cancelButtonColor: '#d33',
			confirmButtonText: 'Có!',
			cancelButtonText: 'Không!'
		}).then((result) => {
			if (result.isConfirmed) {
				// Xóa sản phẩm khỏi mảng cartItems
				for (var i = 0; i < $scope.cartItems.length; i++) {
					if ($scope.cartItems[i].maChiTietGH === maChiTietGH) {
						index = i;
						$scope.cartItems.splice(i, 1);
						break; // Dừng vòng lặp sau khi xóa sản phẩm
					}
				}

				// xóa khỏi database
				$http.delete('/delete/' + maChiTietGH).then(function(response) { });
			}
		})

	};

	// Hàm để đếm số lượng sản phẩm trong giỏ hàng
	$scope.countCartItems = function() {
		return $scope.cartItems.length;
	};

	// Rest API gọi hàm này để cập nhật số lượng sản phẩm trong giỏ hàng
	$scope.updateCartItemCount = function() {
		$scope.cartItemCount = $scope.countCartItems();
	};

	// Hàm để tính tổng số lượng sản phẩm trong giỏ hàng (bao gồm số lượng của từng sản phẩm)
	$scope.getTotalCartItemCount = function() {
		var totalItemCount = 0;
		for (var i = 0; i < $scope.cartItems.length; i++) {
			totalItemCount += $scope.cartItems[i].soLuong;
		}
		return totalItemCount;
	};

	// Rest API gọi hàm này để cập nhật tổng số lượng sản phẩm trong giỏ hàng
	$scope.updateTotalCartItemCount = function() {
		$scope.totalCartItemCount = $scope.getTotalCartItemCount();
	};

	// Hàm để tính tam tính giỏ hàng
	$scope.getTamTinhGioHang = function() {
		var totalCartPrice = 0;
		for (var i = 0; i < $scope.cartItems.length; i++) {
			var item = $scope.cartItems[i];
			totalCartPrice += item.sanPham.giaGoc * item.soLuong;
		}

		return parseInt(totalCartPrice);
	};

	// Hàm để tính tam tính khuyến mãi sản phẩm
	$scope.getTamTinhSanPhamKM = function() {
		var totalCartPrice = 0;
		for (var i = 0; i < $scope.cartItems.length; i++) {
			var item = $scope.cartItems[i];
			totalCartPrice += (item.sanPham.giaGoc * item.sanPham.chietKhauKH / 100);
		}

		return parseInt(totalCartPrice);
	};

	// Hàm để tính thành tiền trong giỏ hàng (dựa trên số lượng và giá của từng sản phẩm)
	$scope.getTotalCartPrice = function() {
		var totalCartPrice = 0;
		for (var i = 0; i < $scope.cartItems.length; i++) {
			var item = $scope.cartItems[i];
			totalCartPrice += (item.sanPham.giaGoc - (item.sanPham.giaGoc * item.sanPham.chietKhauKH / 100)) * item.soLuong;
		}

		return parseInt(totalCartPrice);
	};


	// Hàm để tăng số lượng sản phẩm
	$scope.increaseQuantity = function(item) {
		if (item.soLuong >= 0) {
			item.soLuong += 1;
			// Gọi hàm cập nhật tổng tiền sau khi tăng số lượng
		}
		$scope.update(item);
	};

	// Hàm để giảm số lượng sản phẩm
	$scope.decreaseQuantity = function(item) {
		if (item.soLuong > 1) {
			item.soLuong -= 1;

		} else if (item.soLuong == 1) {
			$scope.remove(item.maChiTietGH, item.sanPham.tenSanPham);
		}
		$scope.update(item);
	};

	// Hàm để cập nhật số lượng sản phẩm khi người dùng sửa số lượng trong ô nhập liệu
	$scope.updateQuantity = function(item) {

		if (item.soLuong < 0) {
			item.soLuong = 1;

		} else if (!item.soLuong || isNaN(item.soLuong)) {

			item.soLuong = 1;
		}
		$scope.update(item);
		// Gọi hàm cập nhật tổng tiền sau khi số lượng thay đổi
		$scope.updateTotalCartPrice();
	};

	// Hàm để chuyển đến trang thanh toán
	$scope.goToCheckout = function() {
		// Chuyển danh sách giỏ hàng thành một chuỗi JSON
		var cartItemsJson = JSON.stringify($scope.cartItems);

		// Sử dụng $window để điều hướng đến trang thanh toán và truyền danh sách giỏ hàng qua tham số URL
		$window.location.href = '/user/checkout/3';
	};
});