document.addEventListener("DOMContentLoaded", function (event) {
        // Your code here
    
        // Attach the form submission handler to the form
        document.getElementById('changePasswordAdminNewBtn').addEventListener('click', function (event) {
			
            var newPassword = document.getElementById("newPassword").value;
            var confirmPassword = document.getElementById("confirmPassword").value;

            // Clear previous error messages
            document.getElementById("newpassword-error").innerHTML = "";
            document.getElementById("newpassword-error2").innerHTML = "";

            // Check if the passwords are empty

            if (newPassword === "") {
                document.getElementById("newpassword-error").innerHTML = "Mật khẩu mới không được để trống!";
                event.preventDefault();
            }
          
			 else{
				if (newPassword.length < 6 || newPassword.length > 20) {
					 document.getElementById("newpassword-error").innerHTML = "Mật khẩu phải từ 6 đến 20 ký tự!";
					event.preventDefault();
					return;
			} 
			  if (/\s/.test(newPassword)) {
                document.getElementById("newpassword-error").innerHTML = "Mật khẩu không được chứa khoảng trắng!";
                event.preventDefault();
                return;
            }
				if (!/^(?=.*[0-9])(?=.*[A-Z]).+$/.test(newPassword)) {
					 document.getElementById("newpassword-error").innerHTML = "Mật khẩu phải chứa chữ số và chữ in hoa!";
					event.preventDefault();
					return;
				}
				event.preventDefault();
			}
            if (confirmPassword === "") {
                document.getElementById("newpassword-error2").innerHTML = "Xác nhận mật khẩu không được để trống!";
                event.preventDefault();
                return;
            }

            // Check if the passwords match
            if (newPassword !== confirmPassword) {
                document.getElementById("newpassword-error2").innerHTML = "Xác nhận mật khẩu không chính xác!";
                event.preventDefault();
                return;
            }
			 else {
            // Send data to the server if all validations pass
            fetch('/admin/access/change-passwordnew', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'newpassword=' + encodeURIComponent(newPassword),
            })
                .then(response => response.text())
                .then(data => {
                    if (data === "") {
                        // Password change successful, you can redirect or show a success message
                        alert("Mật khẩu đã được thay đổi thành công, vui lòng đăng nhập lại mật khẩu mới!");
                        window.location.href = "/admin/access/login";
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });

            event.preventDefault();
        }
        });
    });
  