const fileInput_3 = document.getElementById('adhar-card-file-uplaod');
const fileNameField_3 = document.getElementById('aadhar-card-name-id');

fileInput_3.addEventListener('change', (event) => {
    $(".aadhar-card-view").removeClass("d-none")
    // Get the selected file(s)
    const files = event.target.files;

    // If one or more files are selected
    if (files.length > 0) {
        // Get the name of the first file
        const fileName = files[0].name;

        // Update the value of the input field with the file name
        fileNameField_3.value = fileName;
    } else {
        // If no files are selected, clear the input field
        fileNameField_3.value = 'Upload Profile Pic';
    }
});
$(".aadhar-card-view").bind("click", (e) => {

    e.preventDefault();
    const file = fileInput_3.files[0];
    const objUrl = URL.createObjectURL(file);
    const newTab = window.open(objUrl, "_blank");
    newTab.focus();
});


const fileInput_1 = document.getElementById('driving-license-file-upload');
const fileNameField_1 = document.getElementById('driving-license');

fileInput_1.addEventListener('change', (event) => {
    $(".driving-licence-view").removeClass("d-none")
    // Get the selected file(s)
    const files = event.target.files;

    // If one or more files are selected
    if (files.length > 0) {
        // Get the name of the first file
        const fileName = files[0].name;

        // Update the value of the input field with the file name
        fileNameField_1.value = fileName;
    } else {
        // If no files are selected, clear the input field
        fileNameField_1.value = 'Upload Profile Pic';
    }
});
$(".driving-licence-view").bind("click", (e) => {

    e.preventDefault();
    const file = fileInput_1.files[0];
    const objUrl = URL.createObjectURL(file);
    const newTab = window.open(objUrl, "_blank");
    newTab.focus();
});


const fileInput_2 = document.getElementById('profile-photo-upload');
const fileNameField_2 = document.getElementById('profile-photo');

fileInput_2.addEventListener('change', (event) => {
    $(".profile-photo-view").removeClass("d-none")
    // Get the selected file(s)
    const files = event.target.files;

    // If one or more files are selected
    if (files.length > 0) {
        // Get the name of the first file
        const fileName = files[0].name;

        // Update the value of the input field with the file name
        fileNameField_2.value = fileName;
    } else {
        // If no files are selected, clear the input field
        fileNameField_2.value = 'Upload Profile Pic';
    }
});
$(".profile-photo-view").bind("click", (e) => {

    e.preventDefault();
    const file = fileInput_2.files[0];
    const objUrl = URL.createObjectURL(file);
    const newTab = window.open(objUrl, "_blank");
    newTab.focus();
});


const fileInput_4 = document.getElementById('registration-certificate-upload');
const fileNameField_4 = document.getElementById('registration-certificate');

fileInput_4.addEventListener('change', (event) => {
    $(".registration-certificate-view").removeClass("d-none")
    // Get the selected file(s)
    const files = event.target.files;

    // If one or more files are selected
    if (files.length > 0) {
        // Get the name of the first file
        const fileName = files[0].name;

        // Update the value of the input field with the file name
        fileNameField_4.value = fileName;
    } else {
        // If no files are selected, clear the input field
        fileNameField_4.value = 'Upload Profile Pic';
    }
});
$(".registration-certificate-view").bind("click", (e) => {

    e.preventDefault();
    const file = fileInput_4.files[0];
    const objUrl = URL.createObjectURL(file);
    const newTab = window.open(objUrl, "_blank");
    newTab.focus();
});


$(".navigation li").hover(function () {
    var isHovered = $(this).is(":hover");
    if (isHovered) {
        $(this).children("ul").stop().slideDown(300);
    } else {
        $(this).children("ul").stop().slideUp(300);
    }
});

$.validator.addMethod("vehicleNumberPlate", function(value, element) {
    // Define your regex pattern for number plate validation
    var pattern = /^[A-Z]{2}[ -]?[0-9]{2}[ -]?[A-Z]{1,2}[ -]?[0-9]{4}$/;
    return this.optional(element) || pattern.test(value);
}, "Please enter a valid vehicle number plate (e.g., GJ 03 AY 1097)");
$(document).ready(function () {
    $.validator.addMethod("accept", function (value, element, param) {
        return value.match(new RegExp("." + param + "$"));
    });

    $('#form-id').validate({
        rules: {
            drivingLicense: {
                required: true,
                accept: "pdf"
            },
            profilePhoto: {
                required: true,
                accept: "jpg|png"
            },
            adharCard: {
                required: true,
                accept: "pdf"
            },
            registrationCertificate: {
                required: true,
                accept: "pdf"
            },
            rcExpiration: {
                required: true,
                date: true,
                min: "2024-06-01",
                max: "2040-01-01"
            },
            licenseExpiration: {
                required: true,
                date: true,
                min: "2024-06-01",
                max: "2040-01-01"
            },
            vehicleType: {
                required: true,
            },
            vehicleNumber: {
                required: true,
                vehicleNumberPlate: true // Apply custom validation rule
            }
        },
        messages: {
            drivingLicense: {
                required: 'Please upload a file',
                accept: 'Only PDF files are allowed'
            },
            profilePhoto: {
                required: 'Please upload a file',
                accept: 'Upload jpg or png files'
            },
            adharCard: {
                required: 'Please upload a file',
                accept: 'Only PDF files are allowed'
            },
            registrationCertificate: {
                required: 'Please upload a file',
                accept: 'Only PDF files are allowed'
            },
            rcExpiration: {
                required: "Please enter RC Expiration Date",
                date: "Invalid date format",
                min: "Date must be after 2024-06-01",
                max: "Date must be before 2040-01-01"
            },
            licenseExpiration: {
                required: "Please enter License Expiration Date",
                date: "Invalid date format",
                min: "Date must be after 01-06-2024",
                max: "Date must be before 01-01-2040"
            },
            vehicleType: {
                required: "Please select a vehicle type.",
            },
            vehicleNumber: {
                required: "Please enter vehicle number plate.",
                vehicleNumberPlate: "Please enter a valid vehicle number plate (e.g., AB12CD3456)"
            }
        },
        errorElement: "span",
        errorClass: "error",
        submitHandler: function (form) {

            var drivingLicense = $("#driving-license-file-upload")
            var profilephoto = $("#profile-photo-upload")
            var adhar = $("#adhar-card-file-uplaod")
            var rc = $("#registration-certificate-upload")
            var rcExpiration = $("#rcExpiration").val();
            var licenseExpiration = $("#licenseExpiration").val();
            var vehicleType = $("#vehicleType").val();
            var vehicleNumber = $("#numberPlate").val();


            $(".loader").css("display", "flex");
            var formData = new FormData();
            formData.append("drivingLicense", drivingLicense[0].files[0])
            formData.append("profilePhoto", profilephoto[0].files[0])
            formData.append("adharCard", adhar[0].files[0])
            formData.append("registrationCertificate", rc[0].files[0])
            formData.append("rcExpiration", rcExpiration)
            formData.append("licenseExpiration", licenseExpiration)
            formData.append("vehicleType", vehicleType)
            formData.append("numberPlate", vehicleNumber)


            $.ajax({
                url: form.action,
                method: form.method,
                processData: false,
                contentType: false,
                dataType: 'text',
                enctype: 'multipart/form-data',
                data: formData,
                success: function (response) {
                    console.log(response)
                    // Handle successful response

                    if (response.startsWith("Data")) {
                        showSuccesstMsg(response);
                        console.log("fsadffdaf fthis dis fdsfsdfds")
                        setTimeout(function () {
                            $(".loader").hide();
                            window.location.href = "/UrbanRides/captain/captain-waiting-page";
                        }, 3000); // 3000ms = 3 seconds
                    } else {
                        showErrorMsg(response)
                        $(".loader").hide();
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
});
;document.getElementById('back-button').addEventListener('click', function () {
    history.go(-1); /* move back in history on click */
});
