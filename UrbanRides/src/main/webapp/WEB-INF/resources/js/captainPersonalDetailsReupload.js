$(document).ready(function () {
    handleFileInputs();
    validateForm();

    document.getElementById('back-button').addEventListener('click', function () {
        history.go(-1);
    });
});

function handleFileInputs() {
    const fileInputs = [{
        inputId: 'adhar-card-file-upload',
        nameFieldId: 'aadhar-card-name-id',
        viewClass: 'aadhar-card-view'
    }, {inputId: 'driving-license-file-upload', nameFieldId: 'driving-license', viewClass: 'driving-licence-view'}, {
        inputId: 'registration-certificate-upload',
        nameFieldId: 'registration-certificate',
        viewClass: 'registration-certificate-view'
    }];

    fileInputs.forEach(fileInput => {
        const input = document.getElementById(fileInput.inputId);
        const fileNameField = document.getElementById(fileInput.nameFieldId);

        if (input) {
            input.addEventListener('change', (event) => {
                $(`.${fileInput.viewClass}`).removeClass("d-none");

                const files = event.target.files;
                if (files.length > 0) {
                    const fileName = files[0].name;
                    fileNameField.value = fileName;
                } else {
                    fileNameField.value = 'Upload Profile Pic';
                }
            });

            $(`.${fileInput.viewClass}`).bind("click", (e) => {
                e.preventDefault();
                const files = input.files;
                if (files.length > 0) {
                    const file = files[0];
                    const objUrl = URL.createObjectURL(file);
                    const newTab = window.open(objUrl, "_blank");
                    newTab.focus();
                }
            });

            // Initial visibility of view button based on file input value
            if (fileNameField.value !== 'Upload Profile Pic' && fileNameField.value !== '') {
                $(`.${fileInput.viewClass}`).removeClass("d-none");
            } else {
                $(`.${fileInput.viewClass}`).addClass("d-none");
            }
        }
    });
}
function isValidDateRange(date) {
    const currentDate = new Date();

    // Calculate the future date that is 6 months from today
    const futureDate = new Date(currentDate);
    futureDate.setMonth(currentDate.getMonth() + 6);

    // Calculate the maximum date that is 10 years from today
    const maxDate = new Date(currentDate);
    maxDate.setFullYear(currentDate.getFullYear() + 10);

    const inputDate = new Date(date);

    // Check if the date is at least 6 months in the future and not more than 10 years
    return inputDate >= futureDate && inputDate <= maxDate;
}
function validateForm() {
    $.validator.addMethod("vehicleNumberPlate", function (value, element) {
        var pattern = /^[A-Z]{2}[ -]?[0-9]{2}[ -]?[A-Z]{1,2}[ -]?[0-9]{4}$/;
        return this.optional(element) || pattern.test(value);
    }, "Please enter a valid vehicle number plate (e.g., GJ 03 AY 1097)");

    $.validator.addMethod("accept", function (value, element, param) {
        return value.match(new RegExp("." + param + "$"));
    });


// Add custom validation method for checking if the date is at least 6 months in the future
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
                required: true, accept: "pdf"
            }, adharCard: {
                required: true, accept: "pdf"
            }, registrationCertificate: {
                required: true, accept: "pdf"
            }, rcExpiration: {
                required: true,
                date: true,
                futureDate: true,  // Check if date is in the future
                sixMonthsFuture: true
            },
            licenseExpiration: {
                required: true,
                date: true,
                futureDate: true,  // Check if date is in the future
                sixMonthsFuture: true

            }, vehicleNumber: {
                required: true, vehicleNumberPlate: true
            }
        }, messages: {
            drivingLicense: {
                required: 'Please upload a file', accept: 'Only PDF files are allowed'
            }, adharCard: {
                required: 'Please upload a file', accept: 'Only PDF files are allowed'
            }, registrationCertificate: {
                required: 'Please upload a file', accept: 'Only PDF files are allowed'
            }, rcExpiration: {
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

            }, vehicleNumber: {
                required: "Please enter vehicle number plate.",
                vehicleNumberPlate: "Please enter a valid vehicle number plate (e.g., AB12CD3456)"
            }
        }, errorElement: "span", errorClass: "error", submitHandler: function (form) {
            var formData = new FormData();

            // Manually fetch data from the form
            var rcExpiration = $('#rcExpiration').val();
            var licenseExpiration = $('#licenseExpiration').val();
            var vehicleNumber = $('#numberPlate').val();

            // Append data to formData, set null if not visible or empty
            if ($('#adhar-card-file-upload').prop('files')[0]) {
                formData.append('adharCarde', $('#adhar-card-file-upload').prop('files')[0]);
            }
            if ($('#driving-license-file-upload').prop('files')[0]) {
                formData.append('drivingLicensee', $('#driving-license-file-upload').prop('files')[0]);
            }
            if ($('#registration-certificate-upload').prop('files')[0]) {
                formData.append('registrationCertificatee', $('#registration-certificate-upload').prop('files')[0]);
            }
            if ($('#rc-expiration-section').is(':visible') && rcExpiration) {
                formData.append('rcExpiratione', rcExpiration);
            }
            if ($('#license-expiration-section').is(':visible') && licenseExpiration) {
                formData.append('licenseExpiratione', licenseExpiration);
            }
            if (vehicleNumber) {
                formData.append('numberPlatee', vehicleNumber);
            }


            $(".loader").css("display", "flex");
            console.log(formData);
            $.ajax({
                url: "captain-reupload-document-details-submit", // Correct backend endpoint
                method: "POST",
                processData: false,
                contentType: false,
                dataType: 'json', // Expect JSON response
                enctype: 'multipart/form-data',
                data: formData,
                success: function (response) {
                    $(".loader").hide(); // Hide loader immediately
                    console.log("Success:", response);
                    if (response.status === "success") {
                        showSuccesstMsg(response.message); // Display success message
                        setTimeout(function () {
                            $(".loader").hide();
                            window.location.href = "/UrbanRides/captain/captain-document-details";
                        }, 3000); // 3000ms = 3 seconds
                    } else {
                        showErrorMsg(response.message); // Display error message
                    }
                },
                error: function (xhr) {
                    $(".loader").hide();
                    console.log("Error:", xhr);

                    try {
                        // Parse JSON response from error
                        const errorResponse = JSON.parse(xhr.responseText);
                        if (errorResponse.message) {
                            showErrorMsg(errorResponse.message);
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
}

$(document).ready(function () {
    $.ajax({
        url: 'get-captain-document-reupload-details', // Replace with your backend endpoint
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            // Assuming 'data' is the response from your backend with approval statuses
            handleDocumentApprovalData(data);
        },
        error: function (xhr, status, error) {
            console.error('Error fetching document approval data:', error);
            // Handle frontend error display if needed
            showErrorMsg("Error while upproving the document ")
        }
    });

    function handleDocumentApprovalData(data) {
        if (!data.adharApprovedApprove) {
            $('#aadhar-card-section').show();
        } else {
            $('#aadhar-card-section').hide();
        }

        if (!data.drivingLicenceApprove) {
            $('#driving-license-section').show();
        } else {
            $('#driving-license-section').hide();
        }

        if (!data.rccertificateApprove) {
            $('#registration-certificate-section').show();
        } else {
            $('#registration-certificate-section').hide();
        }

        if (!data.rcexpirationDateApprove) {
            $('#license-expiration-section').show();
        } else {
            $('#license-expiration-section').hide();
        }

        if (!data.drivingLicenceExpiryDateApprove) {
            $('#rc-expiration-section').show();
        } else {
            $('#rc-expiration-section').hide();
        }

        if (!data.numberPlateApprove) {
            $('#number-plate-section').show();
        } else {
            $('#number-plate-section').hide();
        }
    }
});

