const fileInput_3 = document.getElementById('adhar-card-file-uplaod');
const fileNameField_3 = document.getElementById('aadhar-card-name-id');

fileInput_3.addEventListener('change', (event) => {
    $(".aadhar-card-view").removeClass("d-none")
    const files = event.target.files;
    if (files.length > 0) {
        const fileName = files[0].name;
        fileNameField_3.value = fileName;
    } else {
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
    const files = event.target.files;
    if (files.length > 0) {
        const fileName = files[0].name;
        fileNameField_1.value = fileName;
    } else {
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
    const files = event.target.files;
    if (files.length > 0) {
        const fileName = files[0].name;
        fileNameField_2.value = fileName;
    } else {
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
    const files = event.target.files;
    if (files.length > 0) {
        const fileName = files[0].name;
        fileNameField_4.value = fileName;
    } else {
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

function isValidDateRange(date) {
    const currentDate = new Date();
    const futureDate = new Date(currentDate);
    futureDate.setMonth(currentDate.getMonth() + 6);
    const maxDate = new Date(currentDate);
    maxDate.setFullYear(currentDate.getFullYear() + 10);
    const inputDate = new Date(date);
    return inputDate >= futureDate && inputDate <= maxDate;
}


$(document).ready(function () {


    $.validator.addMethod("vehicleNumberPlate", function (value, element) {
        var pattern = /^[A-Z]{2}[ -]?[0-9]{2}[ -]?[A-Z]{1,2}[ -]?[0-9]{4}$/;
        return this.optional(element) || pattern.test(value);
    }, "Please enter a valid vehicle number plate (e.g., GJ 03 AY 1097)");


    $.validator.addMethod("futureDate", function (value, element) {
        return new Date(value) > new Date();
    }, "The date must be in the future.");

    $.validator.addMethod("sixMonthsFuture", function (value, element) {
        return isValidDateRange(value);
    }, "The date must be at least 6 months in the future and within the next 10 years.");


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
                futureDate: true,
                sixMonthsFuture: true
            },
            licenseExpiration: {
                required: true,
                date: true,
                futureDate: true,
                sixMonthsFuture: true
            },
            vehicleType: {
                required: true,
            },
            vehicleNumber: {
                required: true,
                vehicleNumberPlate: true
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
                futureDate: "Date must be in the future",
                sixMonthsFuture: "Date must be at least 6 months in the future and no more than 10 years ahead."
            },
            licenseExpiration: {
                required: "Please enter License Expiration Date",
                date: "Invalid date format",
                futureDate: "Date must be in the future",
                sixMonthsFuture: "Date must be at least 6 months in the future and no more than 10 years ahead."
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
                dataType: 'json',
                enctype: 'multipart/form-data',
                data: formData,
                success: function (response) {

                        setTimeout(function () {
                            $(".loader").hide();
                            window.location.href = "/UrbanRides/captain/captain-waiting-page";
                        }, 3000);
                        showSuccesstMsg("Document submitted successfully")
                        $(".loader").hide();


                },
                error: function (xhr, textStatus, errorThrown) {
                    $(".loader").hide();

                    try {
                        if (xhr.responseJSON && xhr.responseJSON.message) {
                            showErrorMsg(xhr.responseJSON.message);
                        } else if (xhr.responseText) {
                            showErrorMsg(xhr.responseText);
                        } else {
                            showErrorMsg("An unexpected error occurred.");
                        }
                    } catch (e) {
                        showErrorMsg("An unexpected error occurred.");
                    }
                }

            });
        }


    });
});
;document.getElementById('back-button').addEventListener('click', function () {
    history.go(-1);
});