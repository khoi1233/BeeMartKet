app.controller('phieunhap', function($scope, $http, $window, $sce) {
	$scope.ctphieunhaps = [];
	$scope.nhacungcaps = [];
	$scope.nhacungcap = {};
	$scope.sanphamncc = [];
	$scope.sanpham = {};
	$scope.sanphamdachon = [];
	$scope.showFirstButton = true;
	$scope.showSecondButton = false;
	$scope.showWarning = true;



	$scope.resetForm = function(maPhieuNhap) {
		$scope.sanphamdachon = [];
		$scope.sanphamncc = [];
		$scope.showFirstButton = true;
		$scope.showSecondButton = false;
	};
	$scope.editGiaNhap = function(item) {
		var maSanPham = item.maSanPham;
		$http.get('/rest/' + maSanPham).then(function(response) {
			// Kiểm tra nếu có dữ liệu trả về từ server
			if (response.data) {
				$scope.sanpham = response.data;
			} else {
				console.error('Không có dữ liệu trả về.');
			}
		})
			.catch(function(error) {
				console.error('Lỗi khi tải chi tiết sản phẩm:', error);
			});
	};
	$scope.updateGiaNhap = function(sanpham) {
		var maSanPham = sanpham.maSanPham;
		// Gọi API để cập nhật giá nhập
		$http.put('/rest/update/gianhap/product/' + maSanPham, sanpham)
			.then(function(response) {

				// Cập nhật giá nhập trong đối tượng sanpham
				alert("Thành công")
				var index = $scope.sanphamdachon.findIndex(item => item.maSanPham === sanpham.maSanPham);

				if (index !== -1) {
					$scope.sanphamdachon[index].giaNhap = response.data.giaNhap;
				}


				var modalElement = angular.element('#exampleModalSmall');
				modalElement.modal('hide');


			})
			.catch(function(error) {
				alert("lỗi")
				console.error('Error updating giaNhap: ', error);
			});
	};



	$scope.loadCTPhieuNhap = function(maPhieuNhap) {
		$http.get('/rest/nhacungcap')
			.then(function(response) {
				$scope.nhacungcaps = response.data;
			});

		$http.get('/rest/phieuhang/' + maPhieuNhap).then(function(response) {
			if (response.data) {
				$scope.ctphieunhaps = response.data;
				console.log(response.data);
			} else {
				console.error('Lỗi khi nhận dữ liệu hóa đơn từ server.');
			}
		}, function(error) {
			console.error('Lỗi khi gửi yêu cầu đến server:', error);
		});
	};

	$scope.chonNCC = function(item) {

		$scope.resetForm();
		var maNhaCungCap = item.maNhaCungCap;
		$http.get('/rest/nhacungcap/' + maNhaCungCap).then(function(response) {
			if (response.data) {
				$scope.nhacungcap = response.data;
				var modalElement = angular.element('#nhacungcap');
				modalElement.modal('hide');
				$scope.showWarning = false;
			} else {
				console.error('Không có dữ liệu trả về.');
			}
		})
			.catch(function(error) {
				console.error('Lỗi khi tải hình:', error);
			});
	};
	$scope.chonSP = function(nhacungcap) {
		var maNhaCungCap = nhacungcap.maNhaCungCap;
		$http.get('/rest/sanpham/' + maNhaCungCap).then(function(response) {
			if (response.data) {
				$scope.sanphamncc = response.data;
				$scope.showFirstButton = false;
				$scope.showSecondButton = true;

			} else {
				console.error('Không có dữ liệu trả về.');
			}
		})
			.catch(function(error) {
				console.error('Lỗi khi tải hình:', error);
			});



	};

	$scope.chonSanPham = function(item) {
		
		// Chuyển đổi trạng thái của sản phẩm khi nút được click
		item.selected = !item.selected;

		// Kiểm tra trạng thái của sản phẩm và thực hiện thêm hoặc loại bỏ từ danh sách
		if (item.selected) {
			item.$index = $scope.sanphamdachon.length;
			$scope.sanphamdachon.push(item);

			// Loại bỏ sản phẩm khỏi mảng sanphamncc khi checkbox không được chọn
			var indexSanPhamNCC = $scope.sanphamncc.indexOf(item);
			if (indexSanPhamNCC !== -1) {
				$scope.sanphamncc.splice(indexSanPhamNCC, 1);
			}
		} else {
			var index = $scope.sanphamdachon.indexOf(item);
			if (index !== -1) {
				$scope.sanphamdachon.splice(index, 1);
			}
		}
	};




	$scope.xoaSanPhamDaChon = function(sanPhamDaChon) {
		// Tìm vị trí của sản phẩm trong mảng sanphamncc
		var index = $scope.sanphamncc.findIndex(function(sp) {
			return sp.maSanPham === sanPhamDaChon.maSanPham;
		});

		// Nếu tìm thấy, xóa sản phẩm khỏi mảng sanphamncc
		if (index !== -1) {
			$scope.sanphamncc.splice(index, 1);
		}

		// Tìm vị trí của sản phẩm trong mảng sanphamdachon
		var indexInSelected = $scope.sanphamdachon.indexOf(sanPhamDaChon);

		// Nếu tìm thấy, xóa sản phẩm khỏi mảng sanphamdachon
		if (indexInSelected !== -1) {
			$scope.sanphamdachon.splice(indexInSelected, 1);

			// Thêm sản phẩm vào mảng sanphamncc
			$scope.sanphamncc.push(sanPhamDaChon);
		}
	};


	$scope.createPhieuNhapHang = function(maNCC) {

		$http.post('/add/phieunhaphang/' + maNCC, $scope.sanphamdachon)
			.then(function(response) {
				$scope.phieunhphang = response.data;
				alert(maNCC);
				console.log(response.data);

			})
			.catch(function(error) {

				console.error('Error creating PhieuNhapHang: ', error);
			});
	};




	window.onload = function() {
		var ma = window.location.pathname.split('/').pop();
		if (ma) {
			$scope.loadCTPhieuNhap(ma);
		}



	}
});