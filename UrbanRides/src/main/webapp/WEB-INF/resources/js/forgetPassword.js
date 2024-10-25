$(document).ready(function () {

    $("#emailForm").validate({
        rules: {
            email: {
                required: true,
                email: true,
                maxlength: 30
            }
        },
        messages: {
            email: {
                required: "Please enter your email address",
                email: "Please enter a valid email address",
                maxlength: "The length of the email address must be below 30"
            }
        },
        submitHandler: function (form) {
            $("#get-otp-btn").prop("disabled", false);
        }
    });

    $("#get-otp-btn").click(function (e) {
        e.preventDefault();
        var email = $("#email").val();
        if (email && email.length <= 30) {
            var formData = {
                email: email
            };
            $(".loader").css("display", "flex");

            $.ajax({
                type: "POST",
                url: "forget-pass-otp",
                data: formData,
                success: function (response) {
                    $(".loader").hide();
                    console.log(response)
                        showSuccesstMsg(response);
                        $("#otp").prop("disabled", false);
                        $("#email").prop("readonly", true);
                        $("#passwordForm").show();
                        $(".pass-fields").show();
                        $("#get-otp-btn").prop("disabled", true);


                        const fields = document.getElementsByClassName('pass-fields');
                        for (let i = 0; i < fields.length; i++) {
                            fields[i].style.display = 'initial';
                        }

                        setTimeout(function () {
                            document.getElementById('get-otp-btn').disabled = false;
                        }, 10000);
                        const placeHolderElements = document.getElementsByClassName('place-holderr');
                        for (let i = 0; i < placeHolderElements.length; i++) {
                            placeHolderElements[i].style.display = 'none';
                        }




                },
                error: function (xhr) {
                    showErrorMsg(xhr.responseText);
                    $(".loader").hide();
                }
            });
        }
    });
    $.validator.addMethod("strongPass", function (value, element) {
        return this.optional(element) || /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,16}$/.test(value);
    }, "Password must be between 8-16 characters and include at least one uppercase letter, one lowercase letter, one number, and one special character.");

    $("#passwordForm").validate({
        rules: {
            password: {
                required: true,
                minlength: 8,
                maxlength: 16,
                strongPass: true

            },
            confPass: {
                required: true,
                minlength: 8,
                maxlength: 16,
                equalTo: "#pass"

            },
            otp: {
                required: true,
                digits: true,
                minlength: 4,
                maxlength: 4
            }
        },
        messages: {

            password: {
                required: "Please enter your password",
                minlength: "Password must be at least 8 characters long",
                maxlength: "Password cannot exceed 16 characters",
                strongPass: "Password must be between 8-16 characters and include at least one uppercase letter, one lowercase letter, one number, and one special character."
            },
            confPass: {
                required: "Please confirm your password",
                equalTo: "Passwords do not match",
                minlength: "Password must be at least 8 characters long",
                maxlength: "Password cannot exceed 16 characters"
            },
            otp: {
                            required: "OTP is required",
                            digits: "OTP must only contain digits",
                            minlength: "OTP must be 4 digits long",
                            maxlength: "OTP must be 4 digits long",
            }
        },
        errorPlacement: function (error, element) {
            if (element.attr("name") == "password") {
                error.insertAfter("#passError");
            } else if (element.attr("name") == "confPass") {
                error.insertAfter("#confPasswordError");
            } else if (element.attr("name") == "otp") {
                error.insertAfter("#otpError");
            } else {
                error.insertAfter(element);
            }
        },
        submitHandler: function (form) {
            var formData = $("#emailForm, #passwordForm").serializeArray();
            var jsonData = {};
            $.each(formData, function (index, element) {
                jsonData[element.name] = element.value;
            });
            $(".loader").css("display", "flex");

            $.ajax({
                url: "forget-pass-submit",
                method: "POST",
                contentType: 'application/json',
                data: JSON.stringify(jsonData),
                dataType: 'text',
                success: function (response) {
                    if (response === "Password updated successfully") {
                        $(".loader").hide();
                        showSuccesstMsg(response);
                        disableAllElements();
                        setTimeout(function () {
                            $(".loader").hide();
                            window.location.href = "user-login";
                        }, 1000);
                    } else {
                        showErrorMsg(response);
                        $(".loader").hide();
                    }
                },
                error: function (xhr) {
                    var errorMsg = xhr.responseText;
                    try {
                        var errorResponse = JSON.parse(xhr.responseText);
                        if (Array.isArray(errorResponse.errors)) {
                            errorMsg = errorResponse.errors[0];
                        }
                    } catch (e) {
                    }
                    showErrorMsg(errorMsg);
                    $(".loader").hide();
                }
            });
        }
    });

    $("#pass, #confPass").on("input", function () {
        var pass = $("#pass").val();
        var confPass = $("#confPass").val();
        if (pass !== confPass) {
            $("#submitBtn").prop("disabled", true);
        } else {
            $("#confPasswordError").text("");
            $("#submitBtn").prop("disabled", false);
        }
    });

    const togglePassword = document.querySelector('#togglePassword');
    const password = document.querySelector('#confPass');
    togglePassword.addEventListener('click', function (e) {
        const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
        password.setAttribute('type', type);
        togglePassword.src = togglePassword.src.match("password-eye-slash.svg") ? "/UrbanRides/resources/images/password-eye.svg" : "/UrbanRides/resources/images/password-eye-slash.svg";
    });

    const togglePassword1 = document.querySelector('#togglePassword2');
    const password1 = document.querySelector('#pass');
    togglePassword1.addEventListener('click', function (e) {
        const type = password1.getAttribute('type') === 'password' ? 'text' : 'password';
        password1.setAttribute('type', type);
        togglePassword1.src = togglePassword1.src.match("password-eye-slash.svg") ? "/UrbanRides/resources/images/password-eye.svg" : "/UrbanRides/resources/images/password-eye-slash.svg";
    });
});
document.getElementById('back-button').addEventListener('click', function () {
    history.go(-1);
});

function disableAllElements() {
    // Disable all buttons
    var buttons = document.querySelectorAll('button, input[type="button"], input[type="submit"]');
    buttons.forEach(function(button) {
        button.disabled = true;
    });

    // Disable all links
    var links = document.querySelectorAll('a');
    links.forEach(function(link) {
        link.style.pointerEvents = 'none';
        link.style.color = 'gray';
        link.removeAttribute('href');
    });
}
