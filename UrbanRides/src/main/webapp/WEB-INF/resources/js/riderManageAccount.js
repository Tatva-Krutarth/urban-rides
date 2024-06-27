document.getElementById('back-button').addEventListener('click', function () {
    history.go(-1); /* move back in history on click */
});


$(document).ready(function () {

    // Function to fetch user management details and populate the UI
    function fetchUserManagementDetails() {
        $.ajax({
            url: 'rider-usermanagement-details',
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                populateUserManagementDetails(data);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.error('Error fetching user management details:', xhr, textStatus, errorThrown);
                showErrorMsg('Failed to fetch user management details. Please try again later.');
            }
        });
    }

    // Function to populate user management details in the UI
    function populateUserManagementDetails(data) {
        // Populate the profile photo if it exists
        if (data.profilePhotoPath) {
            $('.manage-account-profile-photo img').attr('src', data.profilePhotoPath);
        }

        // Populate the personal details
        $('.personal-details-data[name="firstName"]').val(data.firstName);
        $('.personal-details-data[name="lastName"]').val(data.lastName);
        $('.personal-details-data-login[name="email"]').val(data.email);
        $('.personal-details-data[name="phone"]').val(data.phone);
    }

    // Call the function to fetch and populate user management details on page load
    fetchUserManagementDetails();

    // Edit button click handler for user details form
    $('.edit-button').click(function () {
        if ($(this).hasClass('first-button')) {
            // Unlock the fields for editing
            $('.personal-details-data').prop('readonly', false);
            $(this).addClass('d-none');
            $(this).siblings('.second-button').removeClass('d-none');
        } else {
            // Trigger form validation and submit if valid
            $('#user-management-form').submit();
        }
    });

    // Initialize form validation for user details form
    $('#user-management-form').validate({
        rules: {
            firstName: {
                required: true,
                maxlength: 20
            },
            lastName: {
                required: true,
                maxlength: 20
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
                required: "First name is required",
                maxlength: "First name cannot exceed 20 characters"
            },
            lastName: {
                required: "Last name is required",
                maxlength: "Last name cannot exceed 20 characters"
            },
            phone: {
                required: "Phone number is required",
                digits: "Please enter only digits",
                minlength: "Phone number should be at least 10 digits",
                maxlength: "Phone number cannot exceed 10 digits"
            }
        },

        submitHandler: function (form) {
            // Save changes button click handler
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
                success: function (response) {
                    // Show success alert
                    showSuccesstMsg('User data updated successfully.');

                    // Lock the fields again
                    $('.personal-details-data').prop('readonly', true);
                    $('.edit-button.first-button').removeClass('d-none');
                    $('.edit-button.second-button').addClass('d-none');
                },
                error: function (xhr, textStatus, errorThrown) {
                    console.error('Error updating user data:', xhr, textStatus, errorThrown);
                    showErrorMsg('Failed to update user data. Please try again later.');
                }
            });
        }
    });

    // // Edit button click handler for login details form
    // $('.edit-button-login-details').click(function () {
    //     // Toggle visibility of password fields and buttons
    //     $('.personal-details-cont.d-none').toggleClass('d-none');
    //     $(this).toggleClass('d-none');
    //     $(this).siblings('.edit-button-login-details').toggleClass('d-none');
    // });


    $('.edit-button-login-details').click(function () {
        if ($(this).hasClass('login-first')) {
            // Unlock the fields for editing
            $('.personal-details-cont.d-none').toggleClass('d-none');
            $(this).toggleClass('d-none');
            $(this).siblings('.edit-button-login-details').toggleClass('d-none');
        } else {
            // Trigger form validation and submit if valid
            $('#user-management-login-details').submit();
        }
    });


    // Initialize form validation for login details form
    $('#user-management-login-details').validate({
        rules: {
            currentPassword: {
                required: true,
                minlength: 8
            }, newPassword: {
                required: true,
                minlength: 8
            },
            confNewPassword: {
                required: true,
                equalTo: "#new-password"
            }
        },
        messages: {
            currentPassword: {
                required: "Password is required",
                minlength: "Password must be at least 8 characters long"
            }, newPassword: {
                required: "New password is required",
                minlength: "Password must be at least 8 characters long"
            },
            confNewPassword: {
                required: "Please confirm your password",
                equalTo: "Passwords do not match"
            }
        },
        submitHandler: function (form) {
            // Save changes button click handler
            var loginDetails = {
                currentPassword: $('#current-password').val(),
                newPassword: $('#new-password').val(),
                confirmPassword: $('#conf-new-password').val()
            };

            $.ajax({
                url: 'update-login-details',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(loginDetails),
                success: function (response) {
                    // Show success message
                    showSuccesstMsg('Login details updated successfully.');
                    $('#current-password').val('');
                    $('#new-password').val('');
                    $('#conf-new-password').val('');
                    // Hide password fields again
                    $('.hide-this').addClass('d-none');
                    // $('.login-second').add('d-none');
                    $('#unhide-btn').addClass('d-none');
                    $('#hide-btn').removeClass('d-none');
                },
                error: function (xhr, textStatus, errorThrown) {
                    let errorMessage = xhr.responseText; // Assuming your backend sends the error message in the response body
                    showErrorMsg(errorMessage);
                }

            });
        }
    });


    // Change Profile Photo button click handler
    $('.change-profile-photo-button').click(function () {
        $('#profile-photo-input').click();
    });

    // Profile photo input change handler
    $('#profile-photo-input').change(function () {
        var profilePhoto = this.files[0];
        if (profilePhoto) {
            var fileType = profilePhoto['type'];
            var validImageTypes = ['image/jpeg', 'image/png'];
            if (!validImageTypes.includes(fileType)) {
                sho('Invalid file type. Please select a JPG or PNG image.');
                return;
            }

            var formData = new FormData();
            formData.append('profilePhoto', profilePhoto);

            $.ajax({
                url: 'update-profile-photo',
                method: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    // Show success message
                    showSuccesstMsg('Profile photo updated successfully.');
                    // Update the profile photo in the UI
                    var relativePath = response; // Assuming the response contains the relative path
                    $('.manage-account-profile-photo img').attr('src', relativePath);
                },
                error: function (xhr, textStatus, errorThrown) {
                    console.error('Error updating profile photo:', xhr, textStatus, errorThrown);
                    showErrorMsg('Failed to update profile photo. Please try again later.');
                }
            });
        }
    });
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