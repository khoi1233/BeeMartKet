
app.controller('giohang-controller', function($scope, $http, $window) {
	$scope.cartItems = [];
	$scope.maGioHang;
	$http.get('/cart/getUsername')
		.then(function(response) {
			$scope.maGioHang = response.data;

			// Gọi $http.get bên trong .then() để đảm bảo rằng $scope.maGioHang đã được cập nhật
			$http.get('/cart/rest/' + $scope.maGioHang + '/chitietgiohang')
				.then(function(response) {
					$scope.cartItems = response.data;
				});
		})
		.catch(function(error) {
			console.error('Lỗi khi lấy mã giỏ hàng:', error);
		});

	//hàm giới hạn từ ..................................
	$scope.shortString = function(input, maxLength) {
		if (input.length > maxLength) {
			return input.substring(0, maxLength) + '...';
		}
		return input;
	};


	$scope.addToCart = function(masp) {
		// Tìm sản phẩm có giá trị "maSanPham" tương ứng trong dữ liệu 
		var selectedItem = $scope.cartItems.find(function(item) {
			return item.sanPham.maSanPham === masp;
		});

		if (selectedItem) {
			//nếu có sản phẩm
			selectedItem.soLuong += 1;
			$scope.update(selectedItem);
		} else {
			// Xử lý trường hợp khi không tìm thấy sản phẩm với "maSanPham" cụ thể.
			$http.post('/cart/add/' + $scope.maGioHang + '/' + masp).then(function(response) {
				// Xử lý phản hồi, ví dụ: cập nhật mảng cartItems
				var newCartItem = response.data;
				$scope.cartItems.push(newCartItem);
				$scope.updateCartItemCount();
				$scope.updateTotalCartItemCount();
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
	$scope.update = function(item) {
		var url = '/cart/update/' + item.maChiTietGH;
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
				$http.delete('/cart/delete/' + maChiTietGH).then(function(response) { });
			}
		})

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


	//thanh toán =============================================


	// Hàm để chuyển đến trang thanh toán
	$scope.goToCheckout = function() {
		$http.get('/cart/getUsername')
			.then(function(response) {
				$scope.maGioHang = response.data;
			})
			.catch(function(error) {
				console.error('Lỗi khi lấy mã giỏ hàng:', error);
			});
		// Sử dụng $window để điều hướng đến trang thanh toán và truyền danh sách giỏ hàng qua tham số URL
		$window.location.href = '/checkout/' + $scope.maGioHang;
	};


	$scope.HoaDon;
	$scope.createHoaDon = function() {
		$http.post('/createcheckout/' + $scope.maGioHang).then(function(response) {
			$scope.HoaDon = response.data;
			$window.location.href = `/invoice/` + $scope.maGioHang;
		});
	}


	// Hàm để tính tam tính khuyến mãi sản phẩm
	$scope.getTamTinhSanPhamKM = function() {
		var totalCartPrice = 0;
		for (var i = 0; i < $scope.cartItems.length; i++) {
			var item = $scope.cartItems[i];
			totalCartPrice += (item.sanPham.giaGoc * item.sanPham.chietKhauKH / 100) * item.soLuong;
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

		// Kiểm tra tồn tại của applyCoupon và giaTriKhuyenMai
		if ($scope.voucher && $scope.voucher.giaTriKhuyenMai !== undefined) {
			// kiểm tra giá trị tối thiểu
			if (totalCartPrice > $scope.voucher.giaTriToiThieu) {
				// kiểm tra loại voucher
				if (!$scope.voucher.loai) {
					//tính giá trị khuyến mãi
					$scope.giaTriKM = totalCartPrice * $scope.voucher.chiecKhau / 100;
					if ($scope.giaTriKM > $scope.voucher.giaTriKhuyenMai) {
						$scope.giaTriKM = $scope.voucher.giaTriKhuyenMai
					}

					// Kiểm tra nếu giaTriKM không phải NaN
					if (!isNaN($scope.giaTriKM) && $scope.giaTriKM > 0) {
						$scope.giaTriKM = $scope.giaTriKM * -1;
						totalCartPrice += $scope.giaTriKM;
					}
				} else if ($scope.voucher.loai) {
					$scope.khuyenMaiVC;
				}
			} else {
				alert("Giá trị đơn hàng chưa đạt điều kiện >" + $scope.voucher.giaTriToiThieu);
				$scope.voucher = null;
			}
		} else {
			$scope.giaTriKM = 0;
		}
		return parseInt(totalCartPrice);
	};


	///////////////////////////////tính giảm giá hóa đơn
	$scope.voucher = null;
	$scope.voucherVC = null;
	//hàm tính giảm giá theo mã
	$scope.applyCoupon = function() {
		if ($scope.couponCode) {
			$http.post('/applyCoupon', $scope.couponCode)
				.then(function(response) {
					if (response.data && response.data.giaTriKhuyenMai !== undefined) {
						if (response.data.loai) {
							$scope.voucherVC = response.data;
							const Toast = Swal.mixin({
								toast: true,
								position: "top-end",
								showConfirmButton: false,
								timer: 2000,
								timerProgressBar: true,
								didOpen: (toast) => {
									toast.onmouseenter = Swal.stopTimer;
									toast.onmouseleave = Swal.resumeTimer;
								}
							});
							Toast.fire({
								icon: "success",
								title: "Đã áp dụng voucher!"
							});
							$scope.couponCode = '';
						} else if (!response.data.loai) {
							const Toast = Swal.mixin({
								toast: true,
								position: "top-end",
								showConfirmButton: false,
								timer: 2000,
								timerProgressBar: true,
								didOpen: (toast) => {
									toast.onmouseenter = Swal.stopTimer;
									toast.onmouseleave = Swal.resumeTimer;
								}
							});
							Toast.fire({
								icon: "success",
								title: "Đã áp dụng voucher!"
							});
							$scope.voucher = response.data;
						}
						$scope.couponCode = '';

					} else {
						Swal.fire({
							position: "top-end",
							icon: "error",
							title: "Mã voucher không chính xác!",
							showConfirmButton: false,
							timer: 2000
						});
					}
				})
				.catch(function(error) {
					console.error(error);
				});
		} else {
			Swal.fire({
				position: "top-end",
				icon: "warning",
				title: "Vui lòng nhập mã",
				showConfirmButton: false,
				timer: 2000
			});

		}
	};

	// tính voucher vận chuyển
	$scope.getGiamGiaVanChuyen = function() {
		var check = false;
		// Kiểm tra tồn tại của applyCoupon và giaTriKhuyenMai
		if ($scope.voucherVC && $scope.voucherVC.giaTriKhuyenMai !== undefined) {
			// kiểm tra giá trị tối thiểu
			if ($scope.getTotalCartPrice() > $scope.voucherVC.giaTriToiThieu) {
				// kiểm tra loại voucher
				if ($scope.voucherVC.loai) {
					check = true;
					//tính giá trị khuyến mãi vận chuyển
					$scope.khuyenMaiVC = $scope.voucherVC.giaTriKhuyenMai;
					/*if ($scope.giaTriKM > $scope.voucher.giaTriKhuyenMai) {
						$scope.giaTriKM = $scope.voucher.giaTriKhuyenMai
					}*/
				}
			} else {
				alert("Giá trị đơn hàng chưa đạt điều kiện >" + $scope.voucherVC.giaTriToiThieu);
				$scope.voucherVC = null;
				check = false;
			}
		} else {
			$scope.khuyenMaiVC = 0;
			check = false;
		}
		return check;
	};
	// clear voucher
	$scope.clearVoucher = function(voucher) {
		if (voucher != null) {
			Swal.fire({
				title: "Bạn có muốn gỡ voucher này?",
				showCancelButton: true,
				confirmButtonText: "Có",
				confirmButtonColor: '#119744',
				cancelButtonText: 'Không!'
			}).then((result) => {
				if (result.isConfirmed) {
					$scope.$apply(function() {
						$scope.voucher = null;
					});


				}
			});
		} else {
		}

	}

	// clear voucherVC
	$scope.clearVoucherVC = function(voucherVC) {
		if (voucherVC != null) {
			Swal.fire({
				title: "Bạn có muốn gỡ voucher này?",
				showCancelButton: true,
				confirmButtonText: "Có",
				confirmButtonColor: '#119744',
				cancelButtonText: 'Không!'
			}).then((result) => {
				if (result.isConfirmed) {
					$scope.$apply(function() {
						$scope.voucherVC = null;
					});


				}
			});
		} else {
		}
	}


	$scope.getHoaDon = function() {
		$http.get('invoice/').then(function(response) {
			$scope.HoaDon = response.data;
		});
	}
});
