
 /**************************************Đăng ký tài khoản****************************************** */
//Bắt lỗi đăng ký-------------------------------------------------------------------------------------


    document.addEventListener("DOMContentLoaded", function () {
        const form = document.querySelector(".user-form");
        const usernameInput = document.getElementById("usernameRT");
        const emailInput = document.getElementById("emailRT");
        const passwordInput = document.getElementById("passwordRT");
        const password2Input = document.getElementById("passwordRT2");
        const checkbox = document.getElementById("check");
   		const checkboxError = document.getElementById("checkboxError");
   		
		
		
        form.addEventListener("submit", async function (event) {
            event.preventDefault(); // Ngăn chặn gửi biểu mẫu mặc định

            const usernameRT = usernameInput.value.trim();
            const emailRT = emailInput.value.trim();
            const passwordRT = passwordInput.value;
            const passwordRT2 = password2Input.value;
            const firstName = document.getElementById("firstName").value.trim();
        	const lastName = document.getElementById("lastName").value.trim();
        	
				
			const usernameError = document.getElementById("username-error");
            const emailError = document.getElementById("email-error");
            const passwordError = document.getElementById("password-error");
            const password2Error = document.getElementById("password2-error");
            const firstNameError = document.getElementById("firstName-error");
        	const lastNameError = document.getElementById("lastName-error");
        	const checkboxError = document.getElementById("checkboxError");

            usernameError.textContent = ""; // Xóa thông báo lỗi cũ
            emailError.textContent = "";
            passwordError.textContent = "";
            password2Error.textContent = "";
            firstNameError.textContent = "";
        	lastNameError.textContent = "";
        	checkboxError.textContent ="";
//            checkboxError.textContent = "";
            
			
			if (firstName === "") {
				firstNameError.textContent = "Họ không được để trống!";
				
			}

			if (lastName === "") {
				lastNameError.textContent = "Tên không được để trống!";
				
			}
            if (usernameRT === "") {
                usernameError.textContent = "Tên đăng nhập không được để trống!";
                
            }
            
            else{
				if (!/[a-zA-Z]/.test(usernameRT)) {
					usernameError.textContent = "Tên đăng nhập phải chứa ít nhất một ký tự!";
				
				}
             	if (usernameRT.length < 6) {
                	usernameError.textContent = "Tên đăng nhập phải có ít nhất 6 ký tự!";
                
            	}
//				const isUsernameTaken = await checkUsernameAvailability(usernameRT);
//
//				if (isUsernameTaken) {
//					usernameError.textContent = "Tên đăng nhập này đã được sử dụng.";
//				}
				  
			}
			
            if (emailRT === "") {
                emailError.textContent = "Địa chỉ email không được để trống!";
                
            }
            else{
				if (!isValidEmail(emailRT)) {
                	emailError.textContent = "Định dạng email không hợp lệ (*****@gmail.com)!";
                
            }
			}
            
            if (passwordRT === "") {
                	passwordError.textContent = "Mật khẩu không được để trống!";
                
            }
            else{
				if (passwordRT.length < 6 || passwordRT.length > 20) {
					passwordError.textContent = "Mật khẩu phải từ 6 đến 20 ký tự!";
				
			} 
				if (!/^(?=.*[0-9])(?=.*[A-Z]).+$/.test(passwordRT)) {
					passwordError.textContent = "Mật khẩu phải chứa chữ số và chữ in hoa!";
				
				}
			}
			
            if (passwordRT2 === "") {
                password2Error.textContent = "Xác nhận mật khẩu không được để trống!";
                
            }
            else{
				if (passwordRT !== passwordRT2) {
                	password2Error.textContent = "Mật khẩu và xác nhận mật khẩu không khớp!";
                
            	}
			}
            if (!checkbox.checked){
          	// Ngăn chặn gửi biểu mẫu mặc định
            	checkboxError.textContent = "Bạn phải chấp nhận Điều khoản & Điều kiện!";
            	
        	}
            if( usernameError.textContent === ""&&emailError.textContent === ""&&
            	passwordError.textContent === ""&&password2Error.textContent === ""&&
            	firstNameError.textContent === ""&&lastNameError.textContent === ""&&
        		checkboxError.textContent===""&&checkbox.checked){
				
				const fullName = firstName + " " + lastName;
				const userInfo = {
					username: usernameRT,
					email: emailRT,
					password: passwordRT,
					fullName: fullName,
					// Add other user information here
				};

				// Convert the object to a JSON string
				const userInfoJSON = JSON.stringify(userInfo);
				
				// Save the user information in local storage
				localStorage.setItem("userInformation", userInfoJSON);
				
				// Show an alert to inform the user
				//            alert("Tài khoản của bạn đã được lưu!");
				const formData = new FormData();
				formData.append("email", emailRT);

				// Gửi yêu cầu POST đến controller
				fetch("/access/send-mail", {
					method: "POST",
					body: formData,
				})
					.catch(error => {
						console.error('Error:', error); // Log lỗi nếu có
					});
				window.location.href = "/access/send-mail";
			}
			

            try {
				const response = await fetch(`/api/check-user?usernameRT=${usernameRT}&emailRT=${emailRT}`);
				if (response.ok) {
					const userExists = await response.json();

					if (userExists.usernameExists) {
						usernameError.textContent = "Tên tài khoản đã tồn tại!";
					}

					if (userExists.emailExists) {
						emailError.textContent = "Địa chỉ email đã được đăng ký!";
					}
				} else {
					// Handle error
					console.error("Lỗi trong quá trình kiểm tra tài khoản và email.");
					return;
				}
            } catch (error) {
                console.error("Có lỗi xảy ra:", error);
                return;
            }
            function isValidEmail(email) {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return emailRegex.test(email);
        }
            // Create an object to store user information
            

     }); 
});
//async function checkUsernameAvailability(username) {
//	try {
//		const response = await fetch(`/api/check-username?usernameRT=${encodeURIComponent(username)}`);
//		if (response.ok) {
//			const data = await response.json();
//			return data;
//		} else {
//			// Handle errors if needed
//			console.error("Error checking username availability");
//			return false;
//		}
//	} catch (error) {
//		// Handle network errors
//		console.error("Network error while checking username availability", error);
//		return false;
//	}
//}

