var app = angular.module('my-admin', []);
app.controller('sanphamAdmin-controller', function($scope, $http, $window, $sce) {
	$scope.sanpham = {};
	$scope.sanphams = {};
	$scope.newProduct = {};
	$scope.sanphamThoiGian = [];
	$scope.phieuhang = [];
	$scope.loaiSanPhams = [];
	$scope.donvitinhs = [];
	$scope.hoadon = [];
	$scope.hoadonct = [];
	$scope.hinh = [];
	$scope.nhacungcap = [];
	$scope.nhacungcap1 = {};
	$scope.showCard = true;

	$http.get('/rest/sanpham')
		.then(function(response) {
			$scope.sanphams = response.data;
			$scope.sanphamNoiBat = response.data.filter(item => item.noiBat === true);
		});

	$http.get('/rest/loaisanpham')
		.then(function(response) {
			$scope.loaiSanPhams = response.data;
		});

	$http.get('/rest/donvitinh')
		.then(function(response) {
			$scope.donvitinhs = response.data;
		});

	$http.get('/rest/phieuhang')
		.then(function(response) {
			$scope.phieuhang = response.data;
			console.log(response.data)
		});

	$http.get('/rest/hoadon/{maHoaDon}')
		.then(function(response) {
			$scope.hoadonct = response.data;
			console.log("hoadon" + response.data)
		});

	$http.get('/rest/hoadon')
		.then(function(response) {
			$scope.hoadon = response.data;
		});
	$http.get('/rest/nhacungcap')
		.then(function(response) {
			$scope.nhacungcap = response.data;
		});

	$scope.goToSinglePage = function(maSanPham) {
		window.location.href = `/admin/product_detail/${maSanPham}`;
	};

	$scope.goToSingOrder = function(maHoaDon) {
		window.location.href = `/admin/hoaDon/${maHoaDon}`;
	};

	$scope.goTodetailCoupon = function(maPhieuNhap) {
		window.location.href = `/admin/coupon_detail/${maPhieuNhap}`;
	};
     $scope.resetForm = function () {

        // Xóa dữ liệu trong model
        $scope.sanpham = {};
    };
	$scope.uploadNCC = function(ncc) {
		alert(ncc.maNhaCungCap);
		$http.get('/rest/nhacungcap/' + ncc.maNhaCungCap)
			.then(function(response) {
				$scope.nhacungcap1 = response.data;
				$scope.showCard = false;
				console.log(response.data);
			});

	};
	
		$scope.capnhat = function(ncc) {
		$http.get('/rest/sanpham')
		.then(function(response) {
			$scope.sanphams = response.data;
		});

	};

	$scope.loadOrder = function(maHoaDon) {
		$http.get('/rest/hoadon/' + maHoaDon)
			.then(function(response) {
				$scope.hoadonct = response.data;
			})
			.catch(function(error) {
				console.error('Lỗi Hóa Đơn:', error);
			});
	};
	$scope.loadChiTietSanPham = function(maSanPham) {
		$http.get('/rest/' + maSanPham).then(function(response) {
			// Kiểm tra nếu có dữ liệu trả về từ server
			if (response.data) {
				$scope.sanpham = response.data;
				CKEDITOR.instances.thongTin.setData($scope.sanpham.thongTin);
				CKEDITOR.instances.moTa.setData($scope.sanpham.moTa);
			} else {
				console.error('Không có dữ liệu trả về.');
			}
		})
			.catch(function(error) {
				console.error('Lỗi khi tải chi tiết sản phẩm:', error);
			});
		$http.get('/rest/hinh/' + maSanPham).then(function(response) {
			// Kiểm tra nếu có dữ liệu trả về từ server
			if (response.data) {
				// Lưu thông tin sản phẩm vào $scope.sanpham
				$scope.hinh = response.data;
			} else {
				console.error('Không có dữ liệu trả về.');
			}
		})
			.catch(function(error) {
				console.error('Lỗi khi tải hình:', error);
			});
	};
	$scope.updateProduct = async function(sanpham) {
		try {
			var newImageFile = document.querySelector('#fileInput').files[0];
			if (newImageFile) {
				// Tải ảnh lên Firebase nếu có ảnh mới
				await $scope.updateImages();
				// Sau khi tải lên Firebase, bạn có thể cập nhật thông tin thương hiệu ở đây
			}
			var maSanPham = window.location.pathname.split('/').pop();
			// Lấy giá trị từ CKEditor và gán vào newProduct.configuration
			var thongTin = CKEDITOR.instances.thongTin;
			$scope.sanpham.thongTin = thongTin.getData();

			// Lấy giá trị từ CKEditor cho trường description và gán vào newProduct.description
			var moTa = CKEDITOR.instances.moTa;
			$scope.sanpham.moTa = moTa.getData();
			// Thêm trường maNhaCungCap vào sanpham
			$scope.sanpham.maNhaCungCap = $scope.nhacungcap1.maNhaCungCap;
			$http.put('/rest/update/product/' + maSanPham, sanpham)
				.then(function(response) {
					alert("Cập nhật thành công")
					 
				})
				.catch(function(error) {
					// Xử lý lỗi nếu có
					console.error("Lỗi khi cập nhật sản phẩm:", error);

					alert("Lỗi rồi fen ơi")
				});
		} catch (error) {
			console.error("Lỗi khi cập nhật sản phẩm:", error);

		}
	};
	// Tải ảnh lên Firebase	bắt đầu
	$scope.createImages = async function() {
		try {
			// Lấy tệp ảnh đã chọn từ trường input với id là "photo"
			const imageFile = document.querySelector('#fileInput').files[0];

			if (!imageFile) {
				throw new Error("Chưa chọn ảnh để tải lên Firebase.");
			}

			// Tạo một thư mục trên Firebase Storage để lưu trữ ảnh
			const storageRef = firebase.storage().ref();
			//			const imageRef = storageRef.child('brands/' + imageFile.name);
			const imageRef = storageRef.child(imageFile.name);

			// Tải tệp ảnh lên Firebase Storage
			const snapshot = await imageRef.put(imageFile);

			// Lấy URL của ảnh sau khi tải lên Firebase
			const downloadURL = await snapshot.ref.getDownloadURL();

			// Lưu URL vào biến $scope.form.brandImage hoặc nơi bạn muốn lưu
			$scope.sanpham.hinhAnh = downloadURL;
		} catch (error) {
			// Xử lý lỗi khi tải ảnh lên Firebase
			throw error;
		}
	};
	
	
$scope.create = async function() {
    try {
        await $scope.createImages(); // cập nhật ảnh từ Firebase

        // Lấy giá trị từ CKEditor cho trường thongTin và gán vào sanphamItem.thongTin
        var thongTinEditor = CKEDITOR.instances.thongTin;
        $scope.sanpham.thongTin = thongTinEditor.getData();

        // Lấy giá trị từ CKEditor cho trường moTa và gán vào sanphamItem.moTa
        var moTaEditor = CKEDITOR.instances.moTa;
        $scope.sanpham.moTa = moTaEditor.getData();

        // Đảm bảo dữ liệu được gửi lên server là một đối tượng, không phải mảng
        $http.post('/add/product', $scope.sanpham).then(resp => {
            alert("Thành công")
            $scope.sanphams.push(resp.data);
            console.log(resp.data);
            window.location.href = `/admin/listProduct`;
        }).catch(error => {
            console.log("Lỗi1", error);
        });
    } catch (error) {
        console.log("Lỗi2", error);
    }
};

$scope.delete = async function(masanpham) {
        $http.delete('/delete/product/'+masanpham).then(resp => {
            alert("Thành công")
           $scope.capnhat();
        }).catch(error => {
            console.log("Lỗi1", error);
        });

};



	// Tải ảnh lên Firebase	Kết thúc
	//Cập nhật ảnh lên Firebase	bắt đầu
	$scope.updateImages = async function() {
		try {
			const imageFile = document.querySelector('#fileInput').files[0];

			if (!imageFile) {
				throw new Error("Chưa chọn ảnh để tải lên Firebase.");
			}

			// Tạo một thư mục trên Firebase Storage để lưu trữ ảnh (ví dụ: theo tên brand)
			const maSanPham = $scope.sanpham.maSanPham; // Lấy tên brand (sử dụng tên brand hoặc một giá trị duy nhất khác)
			const storageRef = firebase.storage().ref();

			// Lấy định dạng của tệp ảnh được tải lên
			const fileExtension = imageFile.name.split('.').pop();

			// Đặt tên ảnh theo tên brand và định dạng của tệp
			/*const imageRef = storageRef.child('brands/' + maSanPham + '.' + fileExtension);*/
			const imageRef = storageRef.child(maSanPham + '.' + fileExtension);

			// Tải tệp ảnh lên Firebase Storage, và sử dụng 'put' để thay thế ảnh cũ nếu đã tồn tại
			const snapshot = await imageRef.put(imageFile);

			// Lấy URL của ảnh sau khi tải lên Firebase
			const downloadURL = await snapshot.ref.getDownloadURL();

			// Lưu URL vào biến $scope.form.brandImage hoặc nơi bạn muốn lưu
			$scope.sanpham.hinhAnh = downloadURL;
			console.log($scope.sanpham.hinhAnh)
		} catch (error) {
			throw error;
		}
	}
	window.onload = function() {
		var maSanPham = window.location.pathname.split('/').pop();
		if (maSanPham) {
			$scope.loadChiTietSanPham(maSanPham);
			/*			$scope.loadOrder(maSanPham);*/

		}



	}

});