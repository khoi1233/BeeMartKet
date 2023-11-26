var app = angular.module('my-admin', []);
app.controller('sanphamAdmin-controller', function($scope, $http, $window, $sce ) {
	$scope.sanpham = {};
	$scope.sanphams = {};
	$scope.newProduct = {};
	$scope.sanphamThoiGian = [];
	$scope.phieuhang = [];
	$scope.loaiSanPhams = [];
	$scope.loaiSanPham = {};
	$scope.donvitinhs = [];
	$scope.hoadon = [];
	$scope.hoadon1 = {};
	$scope.hoadonCT = [];
	$scope.hinh = [];
	$scope.nhacungcap = [];
	$scope.nhacungcap1 = {};
	$scope.showCard = true;
	$scope.khuyenmais = [];
	$scope.khuyenmai = {};
	$scope.hoadonTrangThai = [];


	$http.get('/rest/voucher')
		.then(function(response) {
			$scope.khuyenmais = response.data;
		});

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

	$scope.goKhuyenMai = function(maKhuyenMai) {
		window.location.href = `/admin/promotion/${maKhuyenMai}`;
	};

	$scope.goNhaCungCap = function(maNhaCungCap) {
		window.location.href = `/admin/detail_supplier/${maNhaCungCap}`;
	};

	$scope.goLoaiSanPham = function(maLoaiSanPham) {
		window.location.href = `/admin/detail_Category/${maLoaiSanPham}`;
	};

	$scope.goToSingOrder = function(maHoaDon) {
		window.location.href = `/admin/hoaDon/${maHoaDon}`;
	};

	$scope.goTodetailCoupon = function(maPhieuNhap) {
		window.location.href = `/admin/coupon_detail/${maPhieuNhap}`;
	};

	$scope.resetForm = function() {

		// Xóa dữ liệu trong model
		$scope.sanpham = {};
	};

	//Tính thời gian chờ của hóa đơn
	$http.get('/rest/xacnhanhoadon')
		.then(function(response) {
			$scope.hoadonTrangThai = response.data;


			// Tạo mảng promises cho các cuộc gọi API để tính thời gian
			var promises = $scope.hoadonTrangThai.map(function(hoadon) {
				return $http.get('/rest/hoadon/thoigian/' + hoadon.maHoaDon)
					.then(function(thoigianResponse) {
						hoadon.thoiGianTuNgayMuaDenHienTai = thoigianResponse.data;
					});
			});
			Promise.all(promises).then(function() {
			});
		});

	$scope.formatTime = function(minutes) {
		if (minutes < 60) {
			return minutes + ' phút';
		} else if (minutes < 1440) {
			return Math.floor(minutes / 60) + ' giờ';
		} else if (minutes < 34560) {
			return Math.floor(minutes / 1440) + ' ngày';
		} else {
			return Math.floor(minutes / 34560) + ' tháng';
		}
	};

	$scope.capnhat = function() {
		$http.get('/rest/hoadon')
			.then(function(response) {
				$scope.hoadon = response.data;
			});
		$http.get('/rest/sanpham')
			.then(function(response) {
				$scope.sanphams = response.data;
			});
		$http.get('/rest/loaisanpham')
			.then(function(response) {
				$scope.loaiSanPhams = response.data;
			});
		$http.get('/rest/nhacungcap')
			.then(function(response) {
				$scope.nhacungcap = response.data;
			});
		$http.get('/rest/voucher')
			.then(function(response) {
				$scope.khuyenmais = response.data;
			});
		//Tính thời gian chờ của hóa đơn
		$http.get('/rest/xacnhanhoadon')
			.then(function(response) {
				$scope.hoadonTrangThai = response.data;

				// Tạo mảng promises cho các cuộc gọi API để tính thời gian
				var promises = $scope.hoadonTrangThai.map(function(hoadon) {
					return $http.get('/rest/hoadon/thoigian/' + hoadon.maHoaDon)
						.then(function(thoigianResponse) {
							hoadon.thoiGianTuNgayMuaDenHienTai = thoigianResponse.data;
						});
				});
				Promise.all(promises).then(function() {
				});
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

	$scope.loadChiTietLoaiSanPham = function(maLoaiSanPham) {
		$http.get('/rest/loaisanpham/' + maLoaiSanPham).then(function(response) {
			if (response.data) {
				$scope.loaiSanPham = response.data;
			} else {
				console.error('Không có dữ liệu trả về.');
			}
		})
			.catch(function(error) {
				console.error('Lỗi khi tải hình:', error);
			});
	};

	$scope.loadChiTietNhaCungCap = function(maNhaCungCap) {
		$http.get('/rest/nhacungcap/' + maNhaCungCap).then(function(response) {
			if (response.data) {
				$scope.nhacungcap1 = response.data;
			} else {
				console.error('Không có dữ liệu trả về.');
			}
		})
			.catch(function(error) {
				console.error('Lỗi khi tải hình:', error);
			});
	};

	$scope.loadChiTietKhuyenMai = function(maKhuyenMai) {
		$http.get('/voucher/' + maKhuyenMai).then(function(response) {
			if (response.data) {
				$scope.khuyenmai = response.data;
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
					$scope.capnhat();
					window.location.href = `/admin/listProduct`;

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

	$scope.updateCategoty = function(loaiSanPham) {
		var maLoaiSanPham = window.location.pathname.split('/').pop();

		$http.put('/rest/update/categoty/' + maLoaiSanPham, loaiSanPham)
			.then(function(response) {
				alert("Cập nhật thành công")
				$scope.capnhat();
				window.location.href = `/admin/listCategory`;

			})
			.catch(function(error) {
				// Xử lý lỗi nếu có
				console.error("Lỗi khi cập nhật loại sản phẩm:", error);
				alert("Lỗi rồi fen ơi")
			});

	};
	$scope.updateSupplier = async function(nhacungcap1) {
		try {
			var newImageFile = document.querySelector('#fileInput').files[0];
			if (newImageFile) {
				// Tải ảnh lên Firebase nếu có ảnh mới
				await $scope.updateImages();
				// Sau khi tải lên Firebase, bạn có thể cập nhật thông tin thương hiệu ở đây
			}

			var maNhaCungCap = window.location.pathname.split('/').pop();

			$http.put('/rest/update/nhacungcap/' + maNhaCungCap, nhacungcap1)
				.then(function(response) {
					alert("Cập nhật thành công")
					$scope.capnhat();
					window.location.href = `/admin/listSupplier`;

				})
				.catch(function(error) {
					// Xử lý lỗi nếu có
					console.error("Lỗi khi cập nhật nhacungcap:", error);
					alert("Lỗi rồi fen ơi")
				});
		} catch (error) {
			console.error("Lỗi khi cập nhật nhà cung cấp:", error);

		}
	};

	$scope.updatePromotion = async function(khuyenmai) {
		try {
			var newImageFile = document.querySelector('#fileInput').files[0];
			if (newImageFile) {
				// Tải ảnh lên Firebase nếu có ảnh mới
				await $scope.updateImages();
				// Sau khi tải lên Firebase, bạn có thể cập nhật thông tin thương hiệu ở đây
			}

			var maKhuyenMai = window.location.pathname.split('/').pop();

			$http.put('/rest/update/promotion/' + maKhuyenMai, khuyenmai)
				.then(function(response) {
					alert("Cập nhật thành công")
					$scope.capnhat();
					window.location.href = `/admin/listPromotion`;

				})
				.catch(function(error) {
					// Xử lý lỗi nếu có
					console.error("Lỗi khi cập nhật voucher:", error);
					alert("Lỗi rồi fen ơi")
				});
		} catch (error) {
			console.error("Lỗi khi cập nhật nhà cung cấp:", error);

		}
	};

	$scope.createLoaiSanPham = async function() {

		$http.post('/add/category', $scope.loaiSanPham).then(resp => {
			alert("Thành công")
			$scope.loaiSanPhams.push(resp.data);
			console.log(resp.data);
			window.location.href = `/admin/listCategory`;
		}).catch(error => {
			console.log("Lỗi1", error);
		});
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
			$scope.nhacungcap1.hinhAnh = downloadURL;
			$scope.khuyenmai.hinh = downloadURL;
		} catch (error) {
			// Xử lý lỗi khi tải ảnh lên Firebase
			throw error;
		}
	};
	$scope.createNhaCungCap = async function() {
		try {
			await $scope.createImages(); // cập nhật ảnh từ Firebase


			$http.post('/add/nhacungcap', $scope.nhacungcap1).then(resp => {
				alert("Thành công")
				$scope.nhacungcap.push(resp.data);
				console.log(resp.data);
				window.location.href = `/admin/listSupplier`;
			}).catch(error => {
				console.log("Lỗi1", error);
			});
		} catch (error) {
			console.log("Lỗi2", error);
		}
	};

	$scope.createKhuyenMai = async function() {
		try {
			await $scope.createImages(); // cập nhật ảnh từ Firebase


			$http.post('/add/promotion', $scope.khuyenmai).then(resp => {
				alert("Thành công")
				$scope.khuyenmais.push(resp.data);
				console.log(resp.data);
				window.location.href = `/admin/listPromotion`;
			}).catch(error => {
				console.log("Lỗi1", error);
			});
		} catch (error) {
			console.log("Lỗi2", error);
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
		$http.delete('/delete/product/' + masanpham).then(resp => {
			alert("Thành công")
			$scope.capnhat();
		}).catch(error => {
			console.log("Lỗi1", error);
		});

	};

	$scope.deleteLoai = async function(maLoaiSanPham) {
		$http.delete('/delete/category/' + maLoaiSanPham).then(resp => {
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
			$scope.nhacungcap1.hinhAnh = downloadURL;
			$scope.khuyenmai.hinh = downloadURL;
			console.log($scope.sanpham.hinhAnh)
		} catch (error) {
			throw error;
		}
	}

	$scope.handleItemClick = function(item) {
		var maHoaDon = item.maHoaDon;
		$scope.selectedMaHoaDon = maHoaDon;
		$http.get('/rest/chitiet/' + maHoaDon).then(function(response) {
			if (response.data) {
				$scope.hoadonCT = response.data;
			} else {
				console.error('Lỗi khi nhận dữ liệu hóa đơn từ server.');
			}
		});
		$http.get('/hoadon/' + maHoaDon).then(function(response) {
			if (response.data) {
				$scope.hoadon1 = response.data;
				console.log(response.data);
			} else {
				console.error('Lỗi khi nhận dữ liệu hóa đơn từ server.');
			}
		});
	};

	$scope.capNhatTrangThai = function(trangThaiHoaDon) {
		var maHoaDon = $scope.selectedMaHoaDon;
		console.log(maHoaDon);
		$http.put('/rest/' + maHoaDon + '/cap-nhat-trang-thai/' + trangThaiHoaDon).then(function(response) {
			alert("Cập nhật thành công")
			$scope.capnhat();
			// Tìm modal hiện tại và tắt nó
			var modalElement = angular.element('#exampleModalLarge');
			modalElement.modal('hide');
		}).catch(function(error) {
			// Xử lý lỗi nếu có
			console.error("Lỗi khi cập nhật hóa đơn:", error);
			alert("Lỗi rồi fen ơi")
		});
	};

	$scope.capNhatTrangThaiTable = function(maHoaDon, trangThaiHoaDon) {
		console.log("Mã hóa đơn:", maHoaDon);
		maHoaDon = parseInt(maHoaDon); // Đảm bảo là một số nguyên
		console.log("Mã hóa đơn (sau khi chuyển đổi):", maHoaDon);
		$http.put('/rest/' + maHoaDon + '/cap-nhat-trang-thai/' + trangThaiHoaDon).then(function(response) {
			alert("Cập nhật thành công")
			$scope.capnhat();
			// Tìm modal hiện tại và tắt nó
			var modalElement = angular.element('#exampleModalLarge');
			modalElement.modal('hide');
		}).catch(function(error) {
			// Xử lý lỗi nếu có
			console.error("Lỗi khi cập nhật hóa đơn:", error);
			alert("Lỗi rồi fen ơi")
		});
	};

	$scope.hienDanhSachChuaThanhToan = function() {
		$http.get('/rest/xacnhanhoadon')
			.then(function(response) {
				$scope.hoadon = response.data;
			});
	};

	$scope.daxacnhan = function() {
		$http.get('/rest/hoadonxacnhan')
			.then(function(response) {
				$scope.hoadon = response.data;
			});
	};

	$scope.dahuy = function() {
		$http.get('/rest/hoadondahuy')
			.then(function(response) {
				$scope.hoadon = response.data;
			});
	};

	$scope.all = function() {
		$scope.capnhat();
	};

$scope.xuatExcel = function() {
    // Tạo một đối tượng Workbook mới
    var workbook = XLSX.utils.book_new();

    // Tạo một đối tượng Worksheet mới và đổ dữ liệu từ bảng vào nó
    var sheet = XLSX.utils.table_to_sheet(document.getElementById('dataTable'));
    XLSX.utils.book_append_sheet(workbook, sheet, 'DanhSachHoaDon');

    // Xuất Excel
    XLSX.writeFile(workbook, 'DanhSachHoaDon.xlsx');
};


app.controller('YourController', ['$scope', function($scope) {
    $scope.xuatPDF = function() {
        // Tạo một đối tượng jsPDF
        var pdf = new jsPDF();

        // Thêm nội dung vào PDF
        pdf.text('Hello, this is a PDF!', 10, 10);

        // Lưu hoặc tải file PDF
        pdf.save('document.pdf');
    };
}]);







	window.onload = function() {
		var ma = window.location.pathname.split('/').pop();
		if (ma) {
			$scope.loadChiTietSanPham(ma);
			$scope.loadChiTietLoaiSanPham(ma);
			$scope.loadChiTietNhaCungCap(ma);
			$scope.loadChiTietKhuyenMai(ma);
		}



	}

});
