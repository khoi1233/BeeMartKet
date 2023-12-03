document.addEventListener("DOMContentLoaded", function () {
    var form = document.querySelector('.user-form');
    var usernameError = document.getElementById('username-error');
    var emailError = document.getElementById('email-error');

    form.addEventListener('submit', function (event) {
        var username = document.getElementById('username').value;
        var email = document.getElementById('email').value;

        // Reset thông báo lỗi
        usernameError.innerHTML = '';
        emailError.innerHTML = '';

        if (username.trim() === '') {
            usernameError.innerHTML = 'Vui lòng nhập tên tài khoản!';
            event.preventDefault(); // Prevent form submission
        }

        if (email.trim() === '') {
            emailError.innerHTML = 'Vui lòng nhập địa chỉ email!';
            event.preventDefault(); // Prevent form submission
        } else if (!validateEmail(email)) {
            emailError.innerHTML = 'Địa chỉ email không hợp lệ';
            event.preventDefault(); // Prevent form submission
        } else {
            // Use AJAX to send the form data to the server
            var xhr = new XMLHttpRequest();
            xhr.open('POST', '/admin/access/reset-password', true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        // Successful response, display success message
                        localStorage.setItem('username', username);
                        localStorage.setItem('email', email);
                        var xhrEmail = new XMLHttpRequest();
						xhrEmail.open('POST', '/admin/access/send-mail-resetpassword', true);
						xhrEmail.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

						xhrEmail.onreadystatechange = function() {
							if (xhrEmail.readyState === 4 && xhrEmail.status === 200) {
								// Handle the email sent successfully
							}
						};
						var emailData = 'email=' + encodeURIComponent(email);
						xhrEmail.send(emailData);

                        window.location.href = '/admin/access/send-mail-resetpassword';
                    } else {
                        // Server returned an error, display the error message
                        var response = JSON.parse(xhr.responseText);
                        if (response) {
                            if (response.usernameError) {
                                usernameError.innerHTML = response.usernameError;
                            }
                            if (response.emailError) {
                                emailError.innerHTML = response.emailError;
                            }
                        }
                    }
                }
            };

            // Send the data as a URL-encoded string
            var data = 'username=' + encodeURIComponent(username) + '&email=' + encodeURIComponent(email);
            xhr.send(data);
            event.preventDefault(); // Prevent the default form submission
        }
        // Các logic kiểm tra khác nếu cần
    });

    function validateEmail(email) {
        // Use a simple regex for basic email validation
        var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }
});
