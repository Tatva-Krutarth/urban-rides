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
        const numberOfPassengers = document.getElementById("numPassengers").parentElement;
        const chargesDivCont = document.getElementById("charges-div-cont");
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
        const validLocationPackage = document.getElementById("valid-location-package");
        const time = document.getElementById("package-time");
        const dist = document.getElementById("package-dist");
        const charges = document.getElementById("charge");
        const actualNoOfDays = document.getElementById("numDays");

        if (optionId === "rentTaxi") {
            numberOfDays.classList.remove("d-none");
            numberOfPassengers.classList.remove("d-none");
            hideCheckBox.classList.add("d-none");
            selectedDays.classList.add("d-none");
            dropoffLocation.classList.add("d-none");
            chargesDivCont.classList.add("d-none");
            pickupLocation.value = "Vastrapur";
            actualNoOfDays.value = 0;
            pickupLocation.readOnly = true;
            hideCheckBoxText.classList.add("d-none");
            specialInstructionsLabel.innerHTML = "Special Instructions (Optional)";
            pickUpDropOffLabel.classList.add("d-none");
            pickUpToggle.classList.remove("d-none");
            timeAndDistance.classList.add("d-none");
            hideCharges.classList.remove("d-none");
            serviceType.value = "Rent a taxi";
            validLocationPackage.classList.add("d-none");
            pickUpDropOffLabel.innerHTML = "Pick up - Drop off location";
        } else if (optionId === "dailyPickup") {
            dropoffLocation.classList.remove("d-none");
            hideCheckBox.classList.remove("d-none");
            selectedDays.classList.remove("d-none");
            chargesDivCont.classList.remove("d-none");

            pickupLocation.value = "";
            pickupLocation.readOnly = false;
            hideCharges.classList.add("d-none");
            serviceType.value = "Daily pick up";
            validLocationPackage.classList.remove("d-none");
            $("#pickupLocation, #dropoffLocation").prop("placeholder", "");
            $('#charges').val(0);
            $('#numDays').val(0);
            $("#valid-location-package").val('');
            numberOfDays.classList.remove("d-none");
            pickUpDropOffLabel.classList.remove("d-none");
            pickUpDropOffLabel.innerHTML = "Pick up location";
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
                required: true, digits: true, min: 1, max: 30 // Updated max passengers limit
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
            }, validLocation: {
                required: true
            }
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
                min: "Minimum 1 day is allowed",

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
            }, validLocation: {
                required: "Location doest not exist",
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

// ----------------------------------------map-----------------------------------
function rentAtaxiStaticLoc() {
    const defaultLocation = {lat: 23.037737, lng: 72.527735};

    if (!userMarker) {
        userMarker = new google.maps.Marker({
            position: defaultLocation, map: map, title: 'Default Location'
        });
    } else {
        userMarker.setPosition(defaultLocation);
    }

    // map.setCenter(defaultLocation);
    //
    // // Clear all previous captain markers from the map
    // captainMarkers.forEach(marker => {
    //     marker.setMap(null);
    // });
    // captainMarkers = [];
    //
    // captains.forEach(captain => {
    //     const marker = new google.maps.Marker({
    //         position: {lat: captain.lat, lng: captain.lng}, map: map, title: captain.name
    //     });
    //
    //     marker.addListener('click', () => {
    //         new google.maps.InfoWindow({
    //             content: `<h2>${captain.name}</h2><p>Details about the captain...</p>`
    //         }).open(map, marker);
    //         // calculateDistance(userMarker.getPosition(), marker.getPosition());
    //     });
    //
    //     captainMarkers.push(marker);
    // });
}


// ---------------------------------lugguage and daily pickup

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
        console.log(pickup, dropoff);
        calculateDistanceByAddressForPackage(pickup, dropoff);
    }
}


function calculateDistanceByAddressForPackage(originAddress, destinationAddress) {
    const geocoder = new google.maps.Geocoder();

    geocoder.geocode({address: originAddress}, (results, status) => {
        if (status === 'OK' && results.length > 0) {
            const origin = results[0].geometry.location;
            $("#valid-location-package").val('');

            // Check if origin is outside Gujarat
            if (!isWithinAhmedabad(results[0].geometry.bounds || results[0].geometry.viewport)) {
                showErrorMsg('The origin address is outside Ahmedabad.');
                document.getElementById('submitBtn').disabled = true;
                return;
            }

            geocoder.geocode({address: destinationAddress}, (results, status) => {
                if (status === 'OK' && results.length > 0) {
                    const destination = results[0].geometry.location;

                    // Check if destination is outside Gujarat
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

                            // Convert distance to km
                            const distanceInKm = distanceValue / 1000;
                            console.log(distanceInKm)
                            if (distanceInKm < 1 || distanceInKm > 100) {
                                showErrorMsg('The distance should be between 1 to 100 Km.');
                                document.getElementById('submitBtn').disabled = true;
                                return;
                            }

                            console.log(`Distance: ${distanceText}, Time: ${time}`);

                            // Set the distance and time to the elements
                            document.querySelectorAll('.dynamic-distance-package').forEach((element) => {
                                element.innerText = distanceText;
                            });

                            // const distanceInNumber = Math.round(distanceInKm); // Round the distance

                            // const multipliers = [5, 6, 8, 10];
                            // const timeWeightage = getTimeWeightage(); // Get the time weightage

                            // Update price elements
                            // document.querySelectorAll('.vehicle-price-text').forEach((element, index) => {
                            //     if (index < multipliers.length) {
                            //         const price = Math.round(distanceInNumber * multipliers[index] * timeWeightage); // Calculate and round price
                            //         element.innerHTML = `Rs ${price}`;
                            //     }
                            // });

                            document.querySelectorAll('.dynamic-time-package').forEach((element) => {
                                element.innerText = time;
                            });

                            // Enable the submit button if everything is valid
                            document.getElementById('submitBtn').disabled = false;
                            $("#valid-location-package").val('Valid location');
                            let vehicle = document.getElementById('vehicleType').value;
                            let numPassengers = parseInt(document.getElementById('numPassengers').value); // Parse numPassengers as integer
                            let locationValid = $("#valid-location-package").val();
                            let numDays = $("#numDays").val();

                            // Check conditions for validity
                            if (vehicle !== "" && locationValid === "Valid location" && numPassengers > 0 && numPassengers < 30 && numDays > 0 && numDays < 30) {
                                calculatePrice(); // Call calculatePrice function if conditions are met
                            }
                            // Calculate and display the route
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
                        } else {
                            console.error('Error calculating distance:', status);
                            showErrorMsg('Error calculating distance:', status);
                            document.getElementById('submitBtn').disabled = true;
                        }
                    });
                } else {
                    showErrorMsg('The destination address is not valid or not found on the map.');
                    document.getElementById('submitBtn').disabled = true;
                }
            });
        } else {
            showErrorMsg('The origin address is not valid or not found on the map.');
            document.getElementById('submitBtn').disabled = true;
        }
    });
}

//check within the gujarat
function isWithinGujarat(bounds) {
    const gujaratBounds = new google.maps.LatLngBounds(
        new google.maps.LatLng(20.1400, 68.0700), // Southwest coordinates
        new google.maps.LatLng(24.7000, 74.2000)  // Northeast coordinates
    );
    return gujaratBounds.intersects(bounds);
}


autocompletePickup = new google.maps.places.Autocomplete(document.getElementById("pickupLocation"), {
    bounds: new google.maps.LatLngBounds(new google.maps.LatLng(20.1, 68.1), new google.maps.LatLng(24.7, 74.5)),
    componentRestrictions: {country: "in"},
    fields: ["address_components", "geometry", "icon", "name"],
    strictBounds: false,
});

autocompleteDropoff = new google.maps.places.Autocomplete(document.getElementById("dropoffLocation"), {
    bounds: new google.maps.LatLngBounds(new google.maps.LatLng(20.1, 68.1), new google.maps.LatLng(24.7, 74.5)),
    componentRestrictions: {country: "in"},
    fields: ["address_components", "geometry", "icon", "name"],
    strictBounds: false,
});


// ------------------price calculation-----------------------
document.getElementById('vehicleType').addEventListener('change', checkAndCalculate);
document.getElementById('numPassengers').addEventListener('blur', checkAndCalculate);
document.getElementById('numDays').addEventListener('blur', checkAndCalculate);

function checkAndCalculate() {
    debugger
    let vehicle = document.getElementById('vehicleType').value;
    let numPassengers = parseInt(document.getElementById('numPassengers').value); // Parse numPassengers as integer
    let locationValid = $("#valid-location-package").val();
    let numDays = $("#numDays").val();

    // Check conditions for validity
    if (vehicle !== "" && locationValid === "Valid location" && numPassengers > 0 && numPassengers < 30 && numDays > 0 && numDays < 30) {
        calculatePrice(); // Call calculatePrice function if conditions are met
    }

}

function calculatePrice() {
    let estimatedTime = $("#package-time").text();
    estimatedTime = getTimeFromSpan(estimatedTime); // Convert time string to minutes
    console.log("THis is the time  " + estimatedTime)
    let distance = getDistanceFromSpan(document.getElementById('package-dist').innerHTML);
    let vehicle = $('#vehicleType').val();
    let numPassengers = parseInt(document.getElementById('numPassengers').value);
    let numDays = $("#numDays").val();

    let totalPrice = calculatePriceFromTimeAndVehicle(estimatedTime, vehicle, distance, numPassengers, numDays);
    const totalPriceFormatted = parseFloat(totalPrice.toFixed(2));
    const chargesValue = Math.floor(totalPriceFormatted);
    console.log(totalPrice)
    console.log(totalPriceFormatted)
    console.log(chargesValue)
    console.log(estimatedTime)
    console.log(vehicle)
    console.log(distance)
    console.log(numPassengers)
    console.log(numDays)
    // Update the charges field with the calculated price
    document.getElementById('charges').value = chargesValue;
}

function getTimeFromSpan(timeString) {
    let hours = 0;
    let minutes = 0;

    // Split the timeString into parts
    let timeParts = timeString.split(" ");

    // Loop through the time parts and extract hours and minutes
    for (let i = 0; i < timeParts.length; i++) {
        if (timeParts[i].includes("hr")) {
            hours = parseInt(timeParts[i - 1]);
        }
        if (timeParts[i].includes("min")) {
            minutes = parseInt(timeParts[i - 1]);
        }
    }

    // Convert everything to minutes
    return hours * 60 + minutes;
}

function getDistanceFromSpan(distanceString) {
    return parseFloat(distanceString.replace(" km", ""));
}

function calculatePriceFromTimeAndVehicle(estimatedTime, vehicle, distance, numPassengers, numDays) {
    let basePrice = 0;
    switch (vehicle) {
        case "1":
            basePrice = 5;
            break;
        case "2":
            basePrice = 6;
            break;
        case "3":
            basePrice = 8;
            break;
        case "4":
            basePrice = 10;
            break;
        case "5":
            basePrice = 20;
            break;
        default:
            basePrice = 0;
            break;
    }
    debugger
    let timePrice = estimatedTime * 6;
    let distancePrice = distance * 3;
    let numPassengersPrice = numPassengers * 30;
    let pricePerDays = numDays * 30;

    // Calculate total price
    let totalPrice = basePrice + timePrice + distancePrice + numPassengersPrice + pricePerDays;

    return totalPrice;
}
