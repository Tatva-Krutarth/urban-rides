document.addEventListener("DOMContentLoaded", function () {
    const dropbtn = document.querySelector(".dropbtn-package");
    const dropdownContent = document.querySelector(".dropdown-content-package");
    const selectedOptionInput = document.getElementById("selectedOption");

    const options = [{id: "rentTaxi", text: "Rent a Taxi"}, {
        id: "packageService",
        text: "Package Service"
    }, {id: "dailyPickup", text: "Daily Pickup"}];

    function updateDropdownContent(selectedText) {
        dropdownContent.innerHTML = '';
        options.forEach(option => {
            if (option.text !== selectedText) {
                const a = document.createElement("a");
                a.href = "#";
                a.textContent = option.text;
                a.id = option.id;
                a.addEventListener("click", function (event) {
                    event.preventDefault();
                    dropbtn.textContent = option.text;
                    selectedOptionInput.value = option.text; // Store selected option in hidden input
                    updateDropdownContent(option.text);
                    dropdownContent.classList.remove("show");
                    handleOptionChange(option.id);
                });
                dropdownContent.appendChild(a);
            }
        });
    }

    function handleOptionChange(optionId) {
        const dropoffLocation = document.getElementById("dropoffLocation").parentElement;
        const pickupLocation = document.getElementById("pickupLocation");
        const numberOfDays = document.getElementById("numDays").parentElement;
        const drofOffTime = document.getElementById("drofOffTime").parentElement;
        const hideCheckBox = document.getElementById("hide-all-checkbox");
        const pickUpDropOffLabel = document.getElementById("pickup-dropoff-label-id");
        const pickUpToggle = document.getElementById("pick-up-toggle");
        const hideCheckBoxText = document.getElementById("hide-daily-pickup-text");
        const selectedDays = document.getElementById("selectedDays");
        const timeAndDistance = document.getElementById("package-distance-time-container");
        const hideCharges = document.getElementById("hide-chages-notes");
        const specialInstructionsLabel = document.getElementById("special-instruciton");
        const serviceType = document.getElementById("selectedOption");

        // const dailyPickupFields = document.querySelectorAll(".d-none");

        if (optionId === "rentTaxi") {
            numberOfDays.classList.remove("d-none");
            hideCheckBox.classList.add("d-none");
            selectedDays.classList.add("d-none");
            dropoffLocation.classList.add("d-none");
            pickupLocation.value = "Vastrapur";
            pickupLocation.readOnly = true;
            hideCheckBoxText.classList.add("d-none");
            specialInstructionsLabel.innerHTML = "Special Instructions (Optional)";
            pickUpDropOffLabel.classList.add("d-none");
            pickUpToggle.classList.remove("d-none");
            timeAndDistance.classList.add("d-none");
            hideCharges.classList.remove("d-none");
            serviceType.value = "Rent a taxi";

            pickUpDropOffLabel.innerHTML = "Pick up - Drop off location";
            // pickupPlaceholder.classList.add("d-none");

            // pickupPlaceholder.forEach(field => field.classList.add("d-none"));


        } else if (optionId === "packageService") {
            dropoffLocation.classList.remove("d-none");
            hideCheckBox.classList.add("d-none");
            pickupLocation.value = "";
            pickupLocation.readOnly = false;
            // pickupPlaceholder.classList.remove("d-none");
            serviceType.value = "Package service";

            // pickupPlaceholder.forEach(field => field.classList.remove("d-none"));
            selectedDays.classList.add("d-none");
            hideCheckBoxText.classList.add("d-none");
            specialInstructionsLabel.innerHTML = "Luggage information";
            pickUpDropOffLabel.classList.remove("d-none");
            timeAndDistance.classList.remove("d-none");
            hideCharges.classList.add("d-none");

            pickUpDropOffLabel.innerHTML = "Pick up location";
            numberOfDays.classList.add("d-none");
            pickUpToggle.classList.add("d-none");


        } else if (optionId === "dailyPickup") {
            dropoffLocation.classList.remove("d-none");
            hideCheckBox.classList.remove("d-none");
            selectedDays.classList.remove("d-none");
            pickupLocation.value = "";
            pickupLocation.readOnly = false;
            // pickupPlaceholder.classList.remove("d-none");
            hideCharges.classList.add("d-none");
            serviceType.value = "Daily pick up";

            // pickupPlaceholder.forEach(field => field.classList.remove("d-none"));
            numberOfDays.classList.remove("d-none");
            pickUpDropOffLabel.classList.remove("d-none");
            pickUpDropOffLabel.innerHTML = "Pick up location";
            timeAndDistance.classList.remove("d-none");

            hideCheckBoxText.classList.remove("d-none");
            specialInstructionsLabel.innerHTML = "Special Instructions (Optional)";
            pickUpToggle.classList.add("d-none");


        }
    }

    dropbtn.addEventListener("click", function (event) {
        dropdownContent.classList.toggle("show");
    });

    window.addEventListener("click", function (event) {
        if (!event.target.matches('.dropbtn-package')) {
            if (dropdownContent.classList.contains('show')) {
                dropdownContent.classList.remove('show');
            }
        }
    });

    // Initialize the dropdown content
    updateDropdownContent("Rent a Taxi");
    handleOptionChange("rentTaxi");
});


// ---------------------vehiclle drop down ------------

document.addEventListener('DOMContentLoaded', function () {
    const selectBox = document.getElementById('vehicleType');

    selectBox.addEventListener('focus', function () {
        this.parentNode.classList.add('focused');
    });

    selectBox.addEventListener('blur', function () {
        this.parentNode.classList.remove('focused');
    });

    selectBox.addEventListener('change', function () {
        if (this.value !== '') {
            this.parentNode.classList.add('has-value');
        } else {
            this.parentNode.classList.remove('has-value');
        }
    });
});


// ----------------------------checkboxes-------------
document.addEventListener('DOMContentLoaded', function () {
    const checkboxes = document.querySelectorAll('input[type="checkbox"][id^="day"]');
    const hiddenField = document.getElementById('selectedDays');

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', updateHiddenField);
    });

    function updateHiddenField() {
        const selectedValues = Array.from(checkboxes)
            .filter(checkbox => checkbox.checked)
            .map(checkbox => checkbox.value);

        hiddenField.value = selectedValues.join(',');
    }
});
// -------------form submitting---------------------
$(document).ready(function () {

    // Custom method to validate future date
    $.validator.addMethod("futureDate", function (value, element) {
        var today = new Date();
        var inputDate = new Date(value);
        return inputDate > today; // Validation condition
    }, "Date must be in the future");
    $.validator.addMethod("time", function (value, element) {
        return this.optional(element) || /^(?:2[0-3]|[01][0-9]):(?:[0-5][0-9])$/.test(value);
    }, "Please enter a valid time format (HH:mm)");
    // Validation initialization
    $('#package-form').validate({
        rules: {
            // Validation rules for various fields
            pickupLocation: {
                required: true, maxlength: 200
            }, dropoffLocation: {
                required: true, maxlength: 200
            }, numPassengers: {
                required: true, digits: true, max: 30 // Updated max passengers limit
            }, numDays: {
                required: true, digits: true, max: 30 // Updated max passengers limit
            }, pickupDate: {
                required: true, date: true, futureDate: true // Custom rule for future date validation
            }, pickupTime: {
                required: true, time: true
            }, drofOffTime: {
                required: true, time: true
            }, charges: {
                required: true, number: true
            }, emergencyContact: {
                required: true, digits: true
            }, selectedDays: {
                required: true,
            }, vehicleType: {
                required: true, min: 1 // Ensure a value other than the default "Select" option is chosen
            },
        }, messages: {
            // Error messages for validation rules
            pickupLocation: {
                required: "Please enter a pickup location", maxlength: "Cannot be more than 200 characters"
            }, dropoffLocation: {
                required: "Please enter a drop off location", maxlength: "Cannot be more than 200 characters"
            }, numPassengers: {
                required: "Please enter the number of passengers",
                digits: "Only digits are allowed",
                max: "Maximum 30 passengers are allowed",
            }, numDays: {
                required: "Number of days are required",
                digits: "Only digits are allowed",
                max: "Maximum 30 days are allowed",
            }, pickupDate: {
                required: "Please enter a pickup date",
                date: "Please enter a valid date",
                futureDate: "Pickup date must be in the future"
            }, pickupTime: {
                required: "Please enter a pickup time", time: "Please enter a valid time"
            }, drofOffTime: {
                required: "Please enter a dropoff time", time: "Please enter a valid time"
            }, charges: {
                required: "Please enter charges", number: "Please enter a valid number"
            }, emergencyContact: {
                required: "Please enter emergency contact number", digits: "Please enter digits only"
            }, selectedDays: {
                required: "Please add Daily pickup days",
            }, vehicleType: {
                required: "Please select a vehicle type."
            }
        }, errorElement: "div", errorClass: "error", submitHandler: function (form) {

            // Fetching all input values for data processing
            let pickupLocation = $('#pickupLocation').val();
            let dropOff = $('#dropoffLocation').val();
            let pickupDate = $('#pickupDate').val();
            let pickupTime = $('#pickupTime').val();
            let drofOffTime = $('#drofOffTime').val();
            let numPassengers = $('#numPassengers').val();
            let vehicleType = $('#vehicleType').val();
            let selectedDays = $("#selectedDays").val();
            let selectedOption = $("#selectedOption").val();
            let numDays = $("#numDays").val();

            let charges = $('#charges').val();
            let emergencyContact = $('#emergencyContact').val();
            let specialInstructions = $('#floatingTextarea').val(); // Optional field

            // Constructing JSON data object for submission

            var payload = {}

            payload["pickup"] = pickupLocation
            payload["dropOff"] = dropOff
            payload["pickUpDate"] = pickupDate
            payload["pickUpTime"] = pickupTime
            payload["dropoffTime"] = drofOffTime
            payload["numberOfPassengers"] = numPassengers
            payload["vehicleId"] = vehicleType
            payload["dailyPickUp"] = selectedDays
            payload["charges"] = charges
            payload["numbOfDays"] = numDays
            payload["emergencyContact"] = emergencyContact
            payload["specialInstructions"] = specialInstructions
            payload["serviceType"] = selectedOption

            // Example showing loader during AJAX request
            $(".loader").css("display", "flex");
            console.log(JSON.stringify(payload));
            // AJAX request to submit form data
            $.ajax({
                url: 'package-ride-submit', // Replace with your endpoint
                method: 'POST',
                data: JSON.stringify(payload),
                contentType: 'application/json',
                dataType: 'text',

                success: function (response) {
                    // Handle successful response

                    showSuccesstMsg("Request Done Successfully");
                    setTimeout(function () {
                        $(".loader").hide();
                        window.location.reload();
                    }, 3000); // 3000ms = 3 seconds

                    console.log("Form submitted successfully:", response);
                    $(".loader").hide();


                },
                error: function (xhr, textStatus, errorThrown) {
                    console.error("Error:", xhr, textStatus, errorThrown);
                    $(".loader").hide();
                    try {
                        const errorResponse = JSON.parse(xhr.responseText);
                        if (Array.isArray(errorResponse.errors)) {
                            const errorMessage = errorResponse.errors[0];
                            showErrorMsg(errorMessage);
                            console.log("Backend try1:", errorMessage);
                        } else {
                            // handle non-array error response
                            showErrorMsg(errorResponse);
                            console.log("Backend try2:", errorResponse);
                        }
                    } catch (e) {
                        // Handle non-JSON response
                        if (typeof xhr.responseText === 'string') {
                            // handle string error response
                            showErrorMsg(xhr.responseText);
                            console.log("Backend catch1:", xhr.responseText);
                        } else {
                            // handle non-string error response
                            showErrorMsg(xhr.responseText);
                            console.log("Backend catch2:", xhr.responseText);
                        }
                    }
                }

            });
            return false;
        }
    });


    // Click handler for form submission
    $('.package-submit').click(function () {
        $('#package-form').submit(); // Trigger form submission
    });
});
