/**************************************Xác nhận email****************************************** */
 
 //Confirm mail----------------------------------------------------------------------------------------------
document.addEventListener("DOMContentLoaded", function () {
    
    document.getElementById("confirm").addEventListener("click",  function(event) {
	 	
	event.preventDefault();
	
	});
  });
  

document.getElementById("confirm").addEventListener("click", function () {
    const confirmError = document.getElementById("confirm-error");
    confirmError.textContent = ""; // Clear any previous error message
    var codeValue = document.getElementById("result").value.trim();
    if (codeValue === "") {
        confirmError.textContent = "Vui lòng nhập mã xác nhận của bạn!";
        return;
    }

    // Create a FormData object to send data in the request body
    var formData = new FormData();
    formData.append("code", codeValue);

    fetch('/access/checkmail-change-password', {
        method: 'POST',
        body: formData
    })
        .then(response => response.text())
        .then(data => {
            if (data === "Xác minh thành công") {
                // Xác minh thành công, xóa dữ liệu từ Local Storage
                localStorage.removeItem("userInformation");
                window.location.href = "/access/change-password";
               
            } else {
                confirmError.textContent = "Mã xác nhận không chính xác!";
                // Xác minh không thành công
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
});
   




	


//Kiểm tra mã xác nhận email-----------------------------------------------------------------------------------------
document.getElementById("resendLink").addEventListener("click", function () {


  // Kiểm tra xem có dữ liệu trong localStorage không
    sendEmailToServer(storedEmail);
    startCountdown(60);
});


// Retrieve email from local storage
// Get the user information from local storage


  // Update the label with the email
  const emailLabel = document.getElementById('emailLabel');
 const email = localStorage.getItem('email');
  if (emailLabel) {
    emailLabel.textContent = email;
 	}


 // Hàm này được gọi khi di chuyển đến ô nhập tiếp theo (từ trái qua phải)
        function nextInput(current) {
            if (current < 6) {
                var nextInputId = "code" + (current + 1);
                document.getElementById(nextInputId).focus();
            } else {
                combineInputs();
            }
        }

        // Hàm này xử lý sự kiện Backspace
        function handleBackspace(current) {
            if (event.keyCode === 8) {
            	if (current === 1) {
                    // Di chuyển con trỏ từ ô đầu tiên (1) đến ô cuối cùng (6) khi ấn Backspace trong ô đầu tiên.
                    document.getElementById("code6").focus();
                } else if (current > 1) {
                    document.getElementById("code" + current).value = ''; // Xóa ký tự trong ô nhập hiện tại
                    document.getElementById("code" + (current - 1)).focus(); // Di chuyển tiêu điểm đến ô nhập trước đó
                }
            	
            }
        }

        // Hàm này được gọi để kết hợp tất cả các ô nhập thành kết quả cuối cùng
        function combineInputs() {
            var code1 = document.getElementById("code1").value;
            var code2 = document.getElementById("code2").value;
            var code3 = document.getElementById("code3").value;
            var code4 = document.getElementById("code4").value;
            var code5 = document.getElementById("code5").value;
            var code6 = document.getElementById("code6").value;

            var combinedCode = code1 + code2 + code3 + code4 + code5 + code6;

            document.getElementById("result").value = combinedCode;
        }
     // Thêm sự kiện "input" cho từng ô nhập để theo dõi sự thay đổi và cập nhật kết quả liên tục
        var codeInputs = document.querySelectorAll('.code-input input');
        codeInputs.forEach(function(input, index) {
            input.addEventListener('input', function() {
                combineInputs(); // Cập nhật kết quả khi có sự thay đổi
                if (index < 5) {
                    codeInputs[index + 1].focus(); // Di chuyển tiêu điểm đến ô nhập tiếp theo
                }
            });
        });
         codeInputs.forEach(function (input, index) {
            input.addEventListener('paste', function (e) {
                e.preventDefault(); // Ngăn chặn hành động dán mặc định

                var clipboardData = e.clipboardData || window.clipboardData;
                var pastedData = clipboardData.getData('text');

                if (pastedData.length <= 6 - index) {
                    for (var i = 0; i < pastedData.length; i++) {
                        codeInputs[index + i].value = pastedData[i];
                        codeInputs[index + i].dispatchEvent(new Event('input')); // Kích hoạt sự kiện input để cập nhật kết quả
                    }
                }
            });
        });
          function removeAccents(event, inputId) {
            var charCode = event.charCode || event.keyCode;
            var char = String.fromCharCode(charCode);
            var withoutAccents = diacritics.remove(char);
            if (char !== withoutAccents) {
                event.preventDefault();
                var currentInput = document.getElementById(inputId);
                currentInput.value = withoutAccents;
                currentInput.dispatchEvent(new Event('input'));
            }
        }
        
        

// Đếm ngược 60 hiển thị mã---------------------------------------------------------------------------------------   
        
        
        
        // Hàm bắt đầu đếm ngược
function startCountdown(seconds) {
  // Lấy tham chiếu đến phần tử hiển thị thời gian đếm ngược
  var countdownElement = document.getElementById("countdown");

  // Lấy tham chiếu đến liên kết "Gửi lại"
  var resendLink = document.getElementById("resendLink");

  // Đặt nội dung ban đầu của countdownElement thành giá trị ban đầu (60 giây)
  countdownElement.textContent = seconds;

  // Thiết lập một interval để cập nhật thời gian đếm ngược mỗi giây
  var countdownInterval = setInterval(function () {
    seconds--; // Giảm giá trị giây đi 1

    if (seconds <= 0) { // Kiểm tra nếu đã hết thời gian đếm ngược
      clearInterval(countdownInterval); // Dừng interval
      countdownElement.style.display = "none"; // Ẩn phần tử hiển thị thời gian đếm ngược
      resendLink.style.display = "inline"; // Hiển thị liên kết "Gửi lại"
    } else {
      countdownElement.textContent = seconds+ " giây"; // Cập nhật nội dung của countdownElement
    }
  }, 1000); // Interval chạy sau mỗi giây (1000 ms)

	resendLink.addEventListener("click", function(e) {
		
    e.preventDefault();
		startCountdown(60); // Bắt đầu đếm ngược lại từ thời gian ban đầu
    	resendLink.style.display = "none";
    	document.getElementById("countdown").style.display = "inline";
	});
	 
}

// Bắt đầu đếm ngược khi trang tải xong
// Bắt đầu đếm ngược khi trang tải
  window.addEventListener("load", function () {
    startCountdown(60);
  });

const storedEmail = localStorage.getItem("storedEmail");



//Gửi email về controller -----------------------------------------------------------------------------------------

function sendEmailToServer() {
  // Lấy giá trị email từ localStorage
  const email = localStorage.getItem('email');

  // Kiểm tra xem email có tồn tại không
  if (email) {
    // Tạo một FormData object để chứa địa chỉ email
    const formData = new FormData();
    formData.append("email", email);

    // Gửi yêu cầu POST đến controller
    fetch("/access/send-mail-resetpassword", {
      method: "POST",
      body: formData,
    })
      .then(response => response.text())
      .then(data => {
        console.log(data); // Log phản hồi từ controller lên console
      })
      .catch(error => {
        console.error('Error:', error); // Log lỗi nếu có
      });
  } else {
    console.error('No email found in localStorage.');
  }
}


//function sendEmailToServer(email) {
//  // Tạo một FormData object để chứa địa chỉ email
//  const formData = new FormData();
//  formData.append("email", email);
//
//  // Gửi yêu cầu POST đến controller
//  fetch("/access/send-mail-resetpassword", {
//    method: "POST",
//    body: formData,
//  })
//    .then(response => response.text())
//    .then(data => {
//      console.log(data); // Log phản hồi từ controller lên console
//    })
//    .catch(error => {
//      console.error('Error:', error); // Log lỗi nếu có
//    });
//}