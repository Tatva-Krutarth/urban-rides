$(document).ready(function () {
    function fetchUserManagementDetails() {
        $.ajax({
            url: 'rider-usermanagement-details',
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                populateUserManagementDetails(data);
            },
            error: function (xhr, textStatus, errorThrown) {
                showErrorMsg('Failed to fetch user management details. Please try again later.');
            }
        });
    }

    function populateUserManagementDetails(data) {
        if (data.profilePhotoPath) {
            $('.manage-account-profile-photo img').attr('src', data.profilePhotoPath);
        }
        $('.personal-details-data[name="firstName"]').val(data.firstName);
        $('.personal-details-data[name="lastName"]').val(data.lastName);
        $('.personal-details-data-login[name="email"]').val(data.email);
        $('.personal-details-data[name="phone"]').val(data.phone);
    }

    fetchUserManagementDetails();
    $('.edit-button').click(function () {
        if ($(this).hasClass('first-button')) {
            $('.personal-details-data').prop('readonly', false);
            $(this).addClass('d-none');
            $(this).siblings('.second-button').removeClass('d-none');
        } else {
            $('#user-management-form').submit();
        }
    });

    $('#user-management-form').validate({
        rules: {
            firstName: {
                required: true,
                minlength: 1,
                maxlength: 10,
                lettersOnly: true,
                notSameValue: '#last-Name'
            },
            lastName: {
                required: true,
                minlength: 1,
                maxlength: 10,
                lettersOnly: true,
                notSameValue: '#first-Name'
            },
            phone: {
                required: true,
                digits: true,
                minlength: 10,
                maxlength: 10
            }
        },
        messages: {
            firstName: {
                required: "Please enter your first name",
                minlength: "Please enter at least one character",
                maxlength: "The length of the last name must be below 10",
                notSameValue: "The values must be different"
            },
            lastName: {
                required: "Please enter your last name",
                minlength: "Please enter at least one character",
                maxlength: "The length of the last name must be below 10",
                notSameValue: "The values must be different"
            },
            phone: {
                required: "Phone number is required",
                digits: "Please enter only digits",
                minlength: "Phone number should be at least 10 digits",
                maxlength: "Phone number cannot exceed 10 digits"
            }
        },
        submitHandler: function (form) {
            var updatedData = {
                firstName: $('.personal-details-data[name="firstName"]').val(),
                lastName: $('.personal-details-data[name="lastName"]').val(),
                phone: $('.personal-details-data[name="phone"]').val()
            };

            $.ajax({
                url: 'update-personal-details',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(updatedData),
                dataType: 'json',
                success: function (response) {
                    showSuccesstMsg('User data updated successfully.');

                    function capitalizeFirstLetter(string) {
                        return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
                    }

                    var formattedFirstName = capitalizeFirstLetter(updatedData.firstName);
                    var formattedLastName = capitalizeFirstLetter(updatedData.lastName);
                    $('.personal-details-data[name="firstName"]').val(formattedFirstName);
                    $('.personal-details-data[name="lastName"]').val(formattedLastName);
                    $('.personal-details-data').prop('readonly', true);
                    $('.edit-button.first-button').removeClass('d-none');
                    $('.edit-button.second-button').addClass('d-none');
                },
                error: function (xhr, textStatus, errorThrown) {
                    console.error('Error updating personal details:', xhr, textStatus, errorThrown);
                    let errorMessage = "An error occurred while updating personal details.";

                    if (xhr.responseJSON && xhr.responseJSON.errors) {
                        errorMessage = xhr.responseJSON.errors;
                    } else if (xhr.responseText) {
                        try {
                            let jsonResponse = JSON.parse(xhr.responseText);
                            if (jsonResponse.message) {
                                errorMessage = jsonResponse.message;
                            }
                        } catch (e) {
                            errorMessage = xhr.responseText;
                        }
                    }

                    showErrorMsg(errorMessage);
                }

            });
        }
    });

    $('.edit-button-login-details').click(function () {
        if ($(this).hasClass('login-first')) {
            $('.personal-details-cont.d-none').toggleClass('d-none');
            $(this).toggleClass('d-none');
            $(this).siblings('.edit-button-login-details').toggleClass('d-none');
        } else {
            $('#user-management-login-details').submit();
        }
    });


    // Initialize form validation for login details form
    $('#user-management-login-details').validate({
        rules: {
            currentPassword: {
                required: true,
                minlength: 8,
                maxlength: 16,
                strongPass: true,
                notSameValue: '#new-password'


            }, newPassword: {
                required: true,
                minlength: 8,
                maxlength: 16,
                strongPass: true,
                notSameValue: '#current-password'


            },
            confNewPassword: {
                required: true,
                equalTo: "#new-password",
                minlength: 8,
                maxlength: 16
            }
        },
        messages: {
            currentPassword: {
                required: "Please enter your password",
                minlength: "Password must be at least 8 characters long",
                maxlength: "Password cannot exceed 16 characters",
                strongPass: "Password must be between 8-16 characters and include at least one uppercase letter, one lowercase letter, one number, and one special character.",
                notSameValue: "The old password cannot be same as new password"


            }, newPassword: {
                required: "Please enter your password",
                minlength: "Password must be at least 8 characters long",
                maxlength: "Password cannot exceed 16 characters",
                strongPass: "Password must be between 8-16 characters and include at least one uppercase letter, one lowercase letter, one number, and one special character.",
                notSameValue: "The new password cannot be same as old password"


            },
            confNewPassword: {
                required: "Please confirm your password",
                equalTo: "Passwords do not match",
                minlength: "Password must be at least 8 characters long",
                maxlength: "Password cannot exceed 16 characters"
            }
        },
        submitHandler: function (form) {
            var loginDetails = {
                currentPassword: $('#current-password').val(),
                newPassword: $('#new-password').val(),
                confirmPassword: $('#conf-new-password').val()
            };
            $(".loader").css("display", "flex");
            $.ajax({
                url: 'update-login-details',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(loginDetails),
                dataType: 'json',

                success: function (response) {
                    showSuccesstMsg('Login details updated successfully.');
                    $('#current-password').val('');
                    $('#new-password').val('');
                    $('#conf-new-password').val('');
                    $('.hide-this').addClass('d-none');
                    $('#unhide-btn').addClass('d-none');
                    $('#hide-btn').removeClass('d-none');
                    $(".loader").hide();
                },
                error: function (xhr, textStatus, errorThrown) {
                    let errorMessage = "An error occurred while updating login details.";
                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        errorMessage = xhr.responseJSON.message;
                    } else if (xhr.responseText) {
                        try {
                            let jsonResponse = JSON.parse(xhr.responseText);
                            if (jsonResponse.message) {
                                errorMessage = jsonResponse.message;
                            }
                        } catch (e) {
                            errorMessage = xhr.responseText;
                        }
                    }
                    $(".loader").hide();
                    showErrorMsg(errorMessage);
                }
            });
        }
    });


    $('.change-profile-photo-button').click(function () {
        $('#profile-photo-input').click();
    });

    $('#profile-photo-input').change(function () {
        var profilePhoto = this.files[0];

        if (profilePhoto) {
            var fileType = profilePhoto['type'];
            var validImageTypes = ['image/jpeg', 'image/png'];
            if (!validImageTypes.includes(fileType)) {
                showErrorMsg('Invalid file type. Please select a JPG or PNG image.');
                $('#profile-photo-input').val('');
                return;
            }

            var formData = new FormData();
            formData.append('profilePhoto', profilePhoto);
            $(".loader").css("display", "flex");

            $.ajax({
                url: 'update-profile-photo',
                method: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                dataType: 'json',
                success: function (response) {
                    showSuccesstMsg('Profile photo updated successfully.');
                    var relativePath = response; // Assuming the response contains the relative path
                    $('.manage-account-profile-photo img').attr('src', relativePath);
                    $('#profile-photo-input').val('');

                    setTimeout(function () {
                        $(".loader").hide();
                        window.location.reload();
                    }, 2000);
                },
                error: function (xhr, textStatus, errorThrown) {
                    $(".loader").hide();
                    $('#profile-photo-input').val('');

                    try {
                        let response = JSON.parse(xhr.responseText);
                        if (response.errors && response.errors.length > 0) {
                            let errorMessage = response.errors.join('<br>');
                            showErrorMsg(errorMessage);
                        } else {
                            showErrorMsg("Only JPG and PNG files are allowed of size less than 1 mb");
                        }
                    } catch (e) {
                        showErrorMsg("Only JPG and PNG files are allowed of size less than 1 mb");
                    }
                }

            });
        }
    })
    $.validator.addMethod("strongPass", function (value, element) {
        return this.optional(element) || /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,16}$/.test(value);
    }, "Password must be between 8-16 characters and include at least one uppercase letter, one lowercase letter, one number, and one special character.");

    jQuery.validator.addMethod("lettersOnly", function (value, element) {
        return this.optional(element) || /^[a-zA-Z]+$/.test(value);
    }, "Please enter only letters");

    $.validator.addMethod("notSameValue", function (value, element, param) {
        var valOne = value.toLowerCase();
        var valTwo = $(param).val().toLowerCase();
        return this.optional(element) || valOne !== valTwo;
    }, "The values must be different.");
    ;
});


const togglePassword2 = document.querySelector('#togglePassword2');
const password2 = document.querySelector('#new-password');
togglePassword2.addEventListener('click', function (e) {
    const type = password2.getAttribute('type') === 'password' ? 'text' : 'password';
    password2.setAttribute('type', type);
    togglePassword2.src = togglePassword2.src.match("password-eye-slash.svg") ? "/UrbanRides/resources/images/password-eye.svg" : "/UrbanRides/resources/images/password-eye-slash.svg";
});
const togglePassword3 = document.querySelector('#togglePassword3');
const password3 = document.querySelector('#conf-new-password');
togglePassword3.addEventListener('click', function (e) {
    const type = password3.getAttribute('type') === 'password' ? 'text' : 'password';
    password3.setAttribute('type', type);
    togglePassword3.src = togglePassword3.src.match("password-eye-slash.svg") ? "/UrbanRides/resources/images/password-eye.svg" : "/UrbanRides/resources/images/password-eye-slash.svg";
});

const togglePassword1 = document.querySelector('#togglePassword1');
const password1 = document.querySelector('#current-password');
togglePassword1.addEventListener('click', function (e) {
    const type = password1.getAttribute('type') === 'password' ? 'text' : 'password';
    password1.setAttribute('type', type);
    togglePassword1.src = togglePassword1.src.match("password-eye-slash.svg") ? "/UrbanRides/resources/images/password-eye.svg" : "/UrbanRides/resources/images/password-eye-slash.svg";
});