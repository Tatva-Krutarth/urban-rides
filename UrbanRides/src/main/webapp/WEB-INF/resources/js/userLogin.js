$("#myForm").validate({
    rules: {
        email: {
            required: true,
            email: true, // Added email validation
            minlength: 1,
            maxlength: 30 // Increased maximum length
        },
        password: {
            required: true,
            minlength: 8, // Minimum length for a secure password
            maxlength: 16 // Increased maximum length
        },
    },
    messages: {
        // Error messages for each field
        email: {
            required: "Please enter your email address",
            email: "Please enter a valid email address",
            minlength: "Please enter at least one character",
            maxlength: "The length of the email address must be below 100"
        },
        password: {
            required: "Please enter your password",
            minlength: "Please enter at least 8 characters",
            maxlength: "The length of the password must be below 50"
        },
    },

    // ignore: "#error-ignore",
    errorElement: "span",
    errorClass: "error",
    submitHandler: function (form) {
        var formData = $(form).serializeArray();
        var jsonData = {};

        $.each(formData, function (index, element) {
            jsonData[element.name] = element.value;
        });
        console.log(JSON.stringify(jsonData))
        $(".loader").css("display", "flex");
        $.ajax({
            url: form.action,
            method: form.method,
            data: JSON.stringify(jsonData),
            contentType: 'application/json',
            dataType: 'text',
            success: function (response) {

                // Handle successful response
                if (typeof response === 'string') {
                    if (response.startsWith("Login")) {
                        let loginMessage = response.substring(0, response.indexOf("+")); // get the "Login successful" part
                        showSuccesstMsg(loginMessage.trim()); // trim to remove extra spaces
                        let accountType = response.substring(response.indexOf("+") + 1).trim(); // get the account type digit
                        setTimeout(function () {
                            $(".loader").hide();
                            if (accountType === "3") {
                                window.location.href = "/UrbanRides/rider/rider-dashboard";
                            } else {
                                window.location.href = "/UrbanRides/captain/captain-dashboard";
                            }
                        }, 3000); // 3000ms = 3 seconds
                    } else {
                        showErrorMsg(response);
                    }
                    $(".loader").hide();

                } else {
                    showErrorMsg(response);
                }
                console.log("Form submitted successfully:", response);
                $(".loader").hide();

            },
            error: function (xhr, textStatus, errorThrown) {
                console.error("Error:", xhr, textStatus, errorThrown);
                $(".loader").hide();
                try {
                    const errorResponse = JSON.parse(xhr.responseText);
                    if (Array.isArray(errorResponse.errors)) {
                        // handle error response in the format {"errors":["Phone number must be between 10 and 13 characters"]}
                        const errorMessage = errorResponse.errors[0];
                        showErrorMsg(errorMessage);
                        console.log("Backend try:", errorMessage);
                    } else {
                        // handle non-array error response
                        showErrorMsg(errorResponse);
                        console.log("Backend try:", errorResponse);
                    }
                } catch (e) {
                    // Handle non-JSON response
                    if (typeof xhr.responseText === 'string') {
                        // handle string error response
                        showErrorMsg(xhr.responseText);
                        console.log("Backend catch:", xhr.responseText);
                    } else {
                        // handle non-string error response
                        showErrorMsg(xhr.responseText);
                        console.log("Backend catch:", xhr.responseText);
                    }
                }
            }
        });
    }
});

jQuery.validator.addMethod("lettersOnly", function (value, element) {
    return this.optional(element) || /^[a-zA-Z]+$/.test(value);
}, "Please enter only letters");

const togglePassword = document.querySelector('#togglePassword');

const password = document.querySelector('#pass');

togglePassword.addEventListener('click', function (e) {

    const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
    password.setAttribute('type', type);

    if (togglePassword.src.match("/UrbanRides/resources/images/password-eye-slash.svg")) {
        togglePassword.src = "/UrbanRides/resources/images/password-eye.svg";
    } else {
        togglePassword.src = "/UrbanRides/resources/images/password-eye-slash.svg";
    }
});
document.getElementById('back-button').addEventListener('click', function () {
    history.go(-1); /* move back in history on click */
});
