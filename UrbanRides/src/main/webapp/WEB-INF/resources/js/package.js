document.addEventListener("DOMContentLoaded", function () {
    const dropbtn = document.querySelector(".dropbtn-package");
    const dropdownContent = document.querySelector(".dropdown-content-package");
    const selectedOptionInput = document.getElementById("selectedOption");

    const options = [
        {id: "rentTaxi", text: "Rent a Taxi"},
        {id: "dailyPickup", text: "Daily Pickup"}
    ];

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
                    selectedOptionInput.value = option.text;
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
        const numberOfPassengers = document.getElementById("numPassengers").parentElement;
        const hideCheckBox = document.getElementById("hide-all-checkbox");
        const pickUpDropOffLabel = document.getElementById("pickup-dropoff-label-id");
        const pickUpDateLabel = document.getElementById("pick-up-date");
        const dropOffDateLabel = document.getElementById("drop-off-date");
        const pickUpToggle = document.getElementById("pick-up-toggle");
        const hideCheckBoxText = document.getElementById("hide-daily-pickup-text");
        const selectedDays = document.getElementById("selectedDays");
        const timeAndDistance = document.getElementById("package-distance-time-container");
        const numberOfDays = document.getElementById("numberOfDays");
        const specialInstructionsLabel = document.getElementById("special-instruciton");
        const serviceType = document.getElementById("selectedOption");
        const validLocationPackage = document.getElementById("valid-location-package");
        const time = document.getElementById("package-time");
        const dist = document.getElementById("package-dist");

        if (optionId === "rentTaxi") {
            $('#package-form')[0].reset();
            numberOfDays.classList.add("d-none");
            numberOfPassengers.classList.remove("d-none");
            hideCheckBox.classList.add("d-none");
            selectedDays.classList.add("d-none");
            dropoffLocation.classList.add("d-none");
            pickupLocation.value = "Pakwan Chokdi, Pakwan Flyover, Bodakdev, Ahmedabad, Gujarat, India";
            pickupLocation.disabled = true;
            hideCheckBoxText.classList.add("d-none");
            specialInstructionsLabel.innerHTML = "Special Instructions (Optional)";
            pickUpDateLabel.innerHTML = "Pick up Date";
            $("#pickup-dropoff-label-id").css("background-color", "transparent");
            dropOffDateLabel.innerHTML = "Drop off date";
            pickUpDropOffLabel.classList.add("d-none");
            pickUpToggle.classList.remove("d-none");
            timeAndDistance.classList.add("d-none");
            serviceType.value = "Rent a taxi";
            validLocationPackage.classList.add("d-none");
            pickUpDropOffLabel.innerHTML = "Pick up - Drop off location";
        } else if (optionId === "dailyPickup") {
            dropoffLocation.classList.remove("d-none");
            hideCheckBox.classList.remove("d-none");
            selectedDays.classList.remove("d-none");
            pickupLocation.value = "";
            pickupLocation.readOnly = false;
            numberOfDays.classList.remove("d-none");
            pickupLocation.disabled = false;
            serviceType.value = "Daily pick up";
            validLocationPackage.classList.remove("d-none");
            $("#pickupLocation, #dropoffLocation").prop("placeholder", "");
            $("#pickup-dropoff-label-id").css("background-color", "white");
            $('#numDays').val(0);
            $("#valid-location-package").val('');
            pickUpDropOffLabel.classList.remove("d-none");
            pickUpDropOffLabel.innerHTML = "Pick up location";
            pickUpDateLabel.innerHTML = "Start date";
            dropOffDateLabel.innerHTML = "End date";
            timeAndDistance.classList.remove("d-none");
            dist.value = "--";
            time.value = "--";
            hideCheckBoxText.classList.remove("d-none");
            specialInstructionsLabel.innerHTML = "Special Instructions (Optional)";
            pickUpToggle.classList.add("d-none");
            $('#package-form')[0].reset();
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

    updateDropdownContent("Rent a Taxi");
    handleOptionChange("rentTaxi");
});



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
        calculateCharges();
    }


});

function calculateCharges() {
    var numPassengers = parseInt($('#numPassengers').val(), 10);
    var vehicleType = $('#vehicleType').val();
    var serviceText = $('#selectedService').html().trim();
    var pickupDate = $('#pickupDate').val();
    var dropOffDate = $('#dropoffDate').val();
    var selectedDays = $('#selectedDays').val().split(',').map(Number);
    var selectedDaysValue = $('#selectedDays').val();
    var distance = parseFloat($('.dynamic-distance-package').text().replace(' Km', ''));
    if (!numPassengers || !vehicleType || !serviceText || !pickupDate || !dropOffDate) {
        $('#charges').val('');
        return;
    }

    var maxPassengers = 0;
    if (vehicleType === '1') {
        maxPassengers = 1;
    } else if (vehicleType === '2') {
        maxPassengers = 3;
    } else if (vehicleType === '3' || vehicleType === '4') {
        maxPassengers = 4;
    }
    if (numPassengers > maxPassengers) {
        $('#charges').val('');
        return;
    }

    var pickDate = new Date(pickupDate);
    var dropDate = new Date(dropOffDate);
    var today = new Date();
    var minPickupDate = new Date(today);
    minPickupDate.setDate(today.getDate() + 2);
    var maxPickupDate = new Date(today);
    maxPickupDate.setMonth(today.getMonth() + 6);

    if (pickDate < minPickupDate || pickDate > maxPickupDate || dropDate <= pickDate || dropDate > new Date(pickDate.getTime() + (30 * 24 * 60 * 60 * 1000))) {
        $('#charges').val('');
        return;
    }

    var charges = 0;
    if (serviceText === 'Rent a Taxi') {
        var numDays = (dropDate - pickDate) / (1000 * 60 * 60 * 24);
        var baseRate = 0;

        if (vehicleType === '1') {
            baseRate = 100;
        } else if (vehicleType === '2') {
            baseRate = 200;
        } else if (vehicleType === '3') {
            baseRate = 300;
        } else if (vehicleType === '4') {
            baseRate = 400;
        }

        charges = baseRate * numPassengers * numDays;
    } else if (serviceText === 'Daily Pickup') {
        if (selectedDays.length === 0 || !distance || selectedDaysValue === "") {
            $('#charges').val('');
            return;
        }

        var baseRatePerKm = 0;
        var ratePerPassenger = 0;

        if (vehicleType === '1') {
            baseRatePerKm = 5;
            ratePerPassenger = 2;
        } else if (vehicleType === '2') {
            baseRatePerKm = 10;
            ratePerPassenger = 5;
        } else if (vehicleType === '3') {
            baseRatePerKm = 15;
            ratePerPassenger = 10;
        } else if (vehicleType === '4') {
            baseRatePerKm = 20;
            ratePerPassenger = 15;
        }

        var numberOfDays = 0;
        var currentDate = new Date(pickupDate);

        while (currentDate <= dropDate) {
            var dayOfWeek = currentDate.getDay();
            if (currentDate.getDay() === 0) {
                dayOfWeek = 7;
            }
            if (selectedDaysValue.includes(dayOfWeek)) {
                numberOfDays++;
            }
            currentDate.setDate(currentDate.getDate() + 1);
        }

        $('#numberOfDays').val(numberOfDays);

        if (numberOfDays === 0) {
            $('#charges').val('');
            showErrorMsg("No days selected between pickup date and drop off date")
            return;
        }
        charges = (baseRatePerKm * distance + ratePerPassenger * numPassengers) * numberOfDays;
    }

    $('#charges').val(Math.round(charges));
}

$(document).ready(function () {

    $.validator.addMethod("futureDateAfterTwoDays", function (value, element) {
        var today = new Date();
        var minDate = new Date(today);
        minDate.setDate(today.getDate() + 2);
        var inputDate = new Date(value);
        return inputDate >= minDate;
    }, "Pickup date must be at least 2 days from today");

    $.validator.addMethod("withinSixMonths", function (value, element) {
        var today = new Date();
        var maxDate = new Date(today);
        maxDate.setMonth(today.getMonth() + 6);
        var inputDate = new Date(value);
        return inputDate <= maxDate;
    }, "Date must be within 6 months from today");

    $.validator.addMethod("afterPickupDate", function (value, element) {
        var pickupDate = $('#pickupDate').val();
        if (!pickupDate) return false;
        var inputDate = new Date(value);
        var pickDate = new Date(pickupDate);
        pickDate.setDate(pickDate.getDate() + 1);
        return inputDate >= pickDate;
    }, "Drop off date must be at least 1 day after pickup date");

    $.validator.addMethod("withinThirtyDays", function (value, element) {
        var pickupDate = $('#pickupDate').val();
        if (!pickupDate) return false;
        var inputDate = new Date(value);
        var pickDate = new Date(pickupDate);
        var maxDropDate = new Date(pickupDate);
        maxDropDate.setDate(pickDate.getDate() + 30);
        return inputDate <= maxDropDate;
    }, "Drop off date must be within 30 days of pickup date");

    $.validator.addMethod("validPassengerCount", function (value, element) {
        var numPassengers = parseInt(value, 10);
        var maxPassengers = 0;
        var bikeCapacity = 1;
        var rickshawCapacity = 3;
        var carCapacity = 4;
        var vehicleType = $('#vehicleType').val();
        if (vehicleType === '1') {
            maxPassengers = bikeCapacity;
        } else if (vehicleType === '2') {
            maxPassengers = rickshawCapacity;
        } else if (vehicleType === '3' || vehicleType === '4') {
            maxPassengers = carCapacity;
        }
        return numPassengers <= maxPassengers;
    }, "Number of passengers must not exceed the capacity based on vehicle type");

    $.validator.addMethod("exactValidLocation", function (value, element) {
        return value === "Valid Location";
    }, "Please select Valid Location");
    $('#package-form').validate({
        rules: {
            pickupLocation: {
                required: true,
                maxlength: 200
            },
            dropoffLocation: {
                required: true,
                maxlength: 200
            },
            numPassengers: {
                required: true,
                digits: true,
                min: 1,
                max: 4,
                validPassengerCount: true
            }, numberOfDays: {
                digits: true,
                min: 1,
                max: 31,
            },
            pickupDate: {
                required: true,
                date: true,
                futureDateAfterTwoDays: true,
                withinSixMonths: true
            },
            dropOffDate: {
                required: true,
                date: true,
                afterPickupDate: true,
                withinSixMonths: true,
                withinThirtyDays: true
            },
            pickupTime: {
                required: true,
            },

            charges: {
                required: true,
                number: true
            },
            emergencyContact: {
                required: true,
                digits: true,
                minlength: 10,
                maxlength: 10
            },
            selectedDays: {
                required: true
            },
            vehicleType: {
                required: true,
                min: 1
            },
            specialInstruction: {
                maxlength: 80
            }, validLocation: {
                required: true,
                exactValidLocation: true
            }
        },
        messages: {
            pickupLocation: {
                required: "Please enter a pickup location",
                maxlength: "Location cannot be more than 200 characters"
            },
            dropoffLocation: {
                required: "Please enter a drop off location",
                maxlength: "Location cannot be more than 200 characters"
            },
            numPassengers: {
                required: "Please enter the number of passengers",
                digits: "Only numeric values are allowed",
                min: "Number of passengers must be atleast 1",
                max: "Number of passengers must not exceed 4",
                validPassengerCount: "Number of passengers must not exceed the capacity based on vehicle type"
            }, numberOfDays: {
                digits: "Only numeric values are allowed",
                min: "Number of days must be atleast 1 between start and end date",
                max: "Number of days must not exceed 30 between start and end date",
            },
            pickupDate: {
                required: "Please enter a pickup date",
                date: "Please enter a valid date",
                futureDateAfterTwoDays: "Pickup date must be at least 2 days from today",
                withinSixMonths: "Date must be within 6 months from today"
            },
            dropOffDate: {
                required: "Please enter a drop off date",
                date: "Please enter a valid date",
                afterPickupDate: "Drop off date must be at least 1 day after pickup date",
                withinSixMonths: "Date must be within 6 months from today",
                withinThirtyDays: "Drop off date must be within 30 days of pickup date"
            },
            pickupTime: {
                required: "Please enter a pickup time"
            },


            charges: {
                required: "Please enter the charges",
                number: "Only numeric values are allowed"
            },
            emergencyContact: {
                required: "Please enter an emergency contact",
                digits: "Only digits are allowed",
                minlength: "Emergency contact must be 10 digits",
                maxlength: "Emergency contact must be 10 digits"
            },
            selectedDays: {
                required: "Please select the days for the Daily Pickup service"
            },
            vehicleType: {
                required: "Please select a vehicle type",
                min: "Please select a valid vehicle type"
            },
            specialInstruction: {
                maxlength: "Cannot be more than 80 characters"
            }, validLocation: {
                required: "Location is not valid",
                exactValidLocation: "Please select Valid Location"
            }
        },
        errorElement: "div",
        errorClass: "error",

        submitHandler: function (form) {
            let pickupLocation = $('#pickupLocation').val();
            let dropOff = $('#dropoffLocation').val();
            let pickupDate = $('#pickupDate').val();
            let dropoffDate = $('#dropoffDate').val();
            let pickupTime = $('#pickupTime').val();
            let numPassengers = $('#numPassengers').val();
            let vehicleType = $('#vehicleType').val();
            let selectedDays = $("#selectedDays").val();
            let selectedOption = $("#selectedOption").val();
            let charges = $('#charges').val();
            let chargesInt = Math.ceil(charges);
            let emergencyContact = $('#emergencyContact').val();
            let specialInstructions = $('#floatingTextarea').val();
            var distance = parseFloat($('.dynamic-distance-package').text().replace(' Km', ''));

            var payload = {
                pickup: "Pakwan Chokdi, Pakwan Flyover, Bodakdev, Ahmedabad, Gujarat, India",
                dropOff: dropOff,
                pickUpDate: pickupDate,
                dropOffDate: dropoffDate,
                pickUpTime: pickupTime,
                distance: distance,
                numberOfPassengers: numPassengers,
                vehicleId: vehicleType,
                dailyPickUp: selectedDays,
                charges: chargesInt,
                emergencyContact: emergencyContact,
                specialInstructions: specialInstructions,
                serviceType: selectedOption,
            };

            $(".loader").css("display", "flex");

            $.ajax({
                url: 'package-ride-submit',
                method: 'POST',
                data: JSON.stringify(payload),
                contentType: 'application/json',
                dataType: 'text',
                success: function (response) {
                    showSuccesstMsg("Request Done Successfully");
                    setTimeout(function () {
                        $(".loader").hide();
                        window.location.reload();
                    }, 3000);

                    $(".loader").hide();
                },
                error: function (xhr, textStatus, errorThrown) {
                    $(".loader").hide();

                    let errorMessage = "Unknown error occurred.";
                    if (xhr.responseText) {
                        try {
                            const errorResponse = JSON.parse(xhr.responseText);
                            if (errorResponse.errors) {
                                if (Array.isArray(errorResponse.errors)) {
                                    errorMessage = errorResponse.errors.join(", ");
                                } else {
                                    errorMessage = errorResponse.errors;
                                }
                            } else if (errorResponse.error) {
                                errorMessage = errorResponse.error;
                            } else {
                                errorMessage = xhr.responseText;
                            }
                        } catch (e) {
                            errorMessage = xhr.responseText;
                        }
                    }
                    showErrorMsg(errorMessage);
                }

            });
            return false;
        }
    });

    $('.package-submit').click(function () {
        $('#package-form').submit();
    });

    $('#vehicleType, #numPassengers').on('change', function () {
        $('#package-form').validate().element('#numPassengers');
    });
    $('#numPassengers, #vehicleType, #selectedService,#selectedDays, #pickupDate, #dropoffDate, #valid-location-package ,  .dynamic-distance-package, #selectedDays').on('change', calculateCharges);
});


function rentAtaxiStaticLoc() {
    const defaultLocation = {lat: 23.037737, lng: 72.527735};

    if (!userMarker) {
        userMarker = new google.maps.Marker({
            position: defaultLocation, map: map, title: 'Default Location'
        });
    } else {
        userMarker.setPosition(defaultLocation);
    }
}


let autocompletePickup = new google.maps.places.Autocomplete(document.getElementById("pickupLocation"), {
    bounds: new google.maps.LatLngBounds(new google.maps.LatLng(20.1, 68.1), new google.maps.LatLng(24.7, 74.5)),
    componentRestrictions: {country: "in"},
    fields: ["address_components", "geometry", "icon", "name"],
    strictBounds: false
});

autocompleteDropoff = new google.maps.places.Autocomplete(document.getElementById("dropoffLocation"), {
    bounds: new google.maps.LatLngBounds(new google.maps.LatLng(20.1, 68.1), new google.maps.LatLng(24.7, 74.5)),
    componentRestrictions: {country: "in"},
    fields: ["address_components", "geometry", "icon", "name"],
    strictBounds: false,
});


function rentAtaxiStaticLoc() {
    const defaultLocation = {lat: 23.037737, lng: 72.527735};

    if (!userMarker) {
        userMarker = new google.maps.Marker({
            position: defaultLocation, map: map, title: 'Default Location'
        });
    } else {
        userMarker.setPosition(defaultLocation);
    }
}

$(document).ready(function () {
    $("#pickupLocation").on("change", function () {
        setTimeout(setMapDetailsForPackage, 1000);
    });
    $("#dropoffLocation").on("change", function () {
        setTimeout(setMapDetailsForPackage, 1000);
    });
});

function setMapDetailsForPackage() {
    var pickup = $("#pickupLocation").val();
    var dropoff = $("#dropoffLocation").val();
    if (pickup && dropoff && pickup.trim() !== "" && dropoff.trim() !== "") {
        calculateDistanceByAddressForPackage(pickup, dropoff);
    }
}


function calculateDistanceByAddressForPackage(originAddress, destinationAddress) {
    const geocoder = new google.maps.Geocoder();

    geocoder.geocode({address: originAddress}, (results, status) => {
        if (status === 'OK' && results.length > 0) {
            const origin = results[0].geometry.location;
            $("#valid-location-package").val('');

            if (!isWithinAhmedabad(results[0].geometry.bounds || results[0].geometry.viewport)) {
                showErrorMsg('The origin address is outside Ahmedabad.');
                document.getElementById('submitBtn').disabled = true;
                return;
            }

            geocoder.geocode({address: destinationAddress}, (results, status) => {
                if (status === 'OK' && results.length > 0) {
                    const destination = results[0].geometry.location;

                    if (!isWithinGujarat(results[0].geometry.bounds || results[0].geometry.viewport)) {
                        showErrorMsg('The destination address is outside Gujarat.');
                        document.getElementById('submitBtn').disabled = true;
                        return;
                    }

                    travelMode = google.maps.TravelMode.DRIVING;

                    const distanceService = new google.maps.DistanceMatrixService();
                    distanceService.getDistanceMatrix({
                        origins: [origin], destinations: [destination], travelMode: travelMode,
                    }, (response, status) => {
                        if (status === 'OK') {
                            const distanceText = response.rows[0].elements[0].distance.text;
                            const distanceValue = response.rows[0].elements[0].distance.value;
                            const time = response.rows[0].elements[0].duration.text;

                            const distanceInKm = distanceValue / 1000;
                            if (distanceInKm < 1 || distanceInKm > 100) {
                                showErrorMsg('The distance should be between 1 to 100 Km.');
                                document.getElementById('submitBtn').disabled = true;
                                return;
                            }


                            document.querySelectorAll('.dynamic-distance-package').forEach((element) => {
                                element.innerText = distanceText;
                            });


                            document.querySelectorAll('.dynamic-time-package').forEach((element) => {
                                element.innerText = time;
                            });

                            document.getElementById('submitBtn').disabled = false;

                            const directionsService = new google.maps.DirectionsService();
                            const request = {
                                origin: originAddress, destination: destinationAddress, travelMode: travelMode,
                            };
                            directionsService.route(request, (response, status) => {
                                if (status === 'OK') {
                                    const routes = response.routes;
                                    if (routes && routes.length > 0) {
                                        directionsDisplay.setDirections(response);
                                    } else {
                                        showErrorMsg('Unable to calculate route');
                                    }
                                } else {
                                    showErrorMsg('Error: ', status);
                                }
                            });
                            $("#valid-location-package").val("Valid Location")
                            calculateCharges();
                        } else {
                            showErrorMsg('Error calculating distance:', status);
                            document.getElementById('submitBtn').disabled = true;
                            $("#valid-location-package").val("")

                        }
                    });
                } else {
                    showErrorMsg('The destination address is not valid or not found on the map.');
                    document.getElementById('submitBtn').disabled = true;
                    $("#valid-location-package").val("")

                }
            });
        } else {
            showErrorMsg('The origin address is not valid or not found on the map.');
            document.getElementById('submitBtn').disabled = true;
            $("#valid-location-package").val("")

        }
    });
}

//check within the gujarat
function isWithinGujarat(bounds) {
    const gujaratBounds = new google.maps.LatLngBounds(
        new google.maps.LatLng(20.1400, 68.0700),
        new google.maps.LatLng(24.7000, 74.2000)
    );
    return gujaratBounds.intersects(bounds);
}

