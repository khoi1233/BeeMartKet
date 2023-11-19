document.addEventListener("DOMContentLoaded", function (event) {
        // Your code here
    
        // Attach the form submission handler to the form
        document.querySelector('.user-form').addEventListener('submit', function(event) {
            var newPassword = document.getElementById("newPassword").value;
            var confirmPassword = document.getElementById("confirmPassword").value;

            // Clear previous error messages
            document.getElementById("newpassword-error").innerHTML = "";
            document.getElementById("newpassword-error2").innerHTML = "";

            // Check if the passwords are empty
            if (newPassword === "") {
                document.getElementById("newpassword-error").innerHTML = "Mật khẩu không được để trống!";
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
            }

            // Check if the passwords match
            if (newPassword !== confirmPassword) {
                document.getElementById("newpassword-error2").innerHTML = "Xác nhận mật khẩu không chính xác!";
                event.preventDefault();
            }
			else{
				var username = localStorage.getItem('username');
				var email = localStorage.getItem('email');
				

				// Validate the data before sending the request
				if (username && email && newPassword) {
					// Make an AJAX request to send data to the server
					$.ajax({
						type: "POST",
						url: "/access/change-password",
						data: {
							username: username,
							email: email,
							newpassword: newPassword
						},
						success: function(response) {
							console.log(response);
							// Handle the response from the server if needed
							if (response.startsWith("redirect:")) {
								// Redirect to the specified URL
								localStorage.removeItem('email');
								localStorage.removeItem('username');
								window.location.href = response.substr("redirect:".length);
							}
						},
						error: function(error) {
							console.error(error);
							// Handle the error if needed
						}
					});
				}
			}
            // If everything is okay, allow the form to be submitted
        });
    });