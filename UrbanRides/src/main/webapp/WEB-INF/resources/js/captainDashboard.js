var map, geocoder;

// Function to initialize Google Map
function initMap(latitude, longitude) {
    var mapOptions = {
        center: new google.maps.LatLng(latitude, longitude),
        zoom: 15
    };
    map = new google.maps.Map(document.getElementById('map'), mapOptions);
    geocoder = new google.maps.Geocoder();

    // Add a marker at user's location
    var marker = new google.maps.Marker({
        position: {lat: latitude, lng: longitude},
        map: map,
        title: 'Your Location'
    });

    // Load trips data from backend
    loadTripsData();
}

function loadTripsData() {
    $.ajax({
        url: 'get-all-trips-data', // Replace with your actual backend endpoint
        method: 'GET',
        success: function (data) {
            data.forEach(function (trip) {
                // Geocode the pickup location address to get coordinates
                geocodeAddress(trip.pickUpLocation, function (results, status) {
                    if (status === 'OK') {
                        var pickupLocation = results[0].geometry.location;
                        var infoWindowContent = '<div>' +
                            '<p><strong>Passenger Name: </strong>' + trip.passengerName + '</p>' +
                            '<p><strong>Pick Up Location: </strong>' + trip.pickUpLocation + '</p>' +
                            '<p><strong>Drop Off Location: </strong>' + trip.dropLocation + '</p>' +
                            '<p><strong>Charges: </strong>' + trip.charges + '</p>' +
                            '<button class="acceptTripBtn" data-tripid="' + trip.tripId + '">Accept Trip</button>' +
                            '</div>';

                        var infoWindow = new google.maps.InfoWindow({
                            content: infoWindowContent,
                            position: pickupLocation
                        });

                        var tripMarker = new google.maps.Marker({
                            position: pickupLocation,
                            map: map,
                            title: 'Pick Up Location'
                        });

                        // Show info window on marker click
                        tripMarker.addListener('click', function () {
                            infoWindow.open(map, tripMarker);
                        });

                        // Add click event listener to the accept button
                        google.maps.event.addListener(infoWindow, 'domready', function () {
                            document.querySelectorAll('.acceptTripBtn').forEach(function (button) {
                                button.addEventListener('click', function () {
                                    var tripId = this.getAttribute('data-tripid');
                                    // Perform AJAX call to backend to accept the trip
                                    $.ajax({
                                        url: 'accept-ride', // Replace with your actual backend endpoint
                                        method: 'POST',
                                        data: {
                                            tripId: tripId
                                        },
                                        success: function (data) {
                                            $('#dash-first-ui').hide();
                                            $('#rider-location-text').removeClass('d-none');
                                            $('#captain-rider-info-cont').removeClass('d-none');
                                            $('#rider-info-details-charges').html(data.charges + ' Rs' );
                                            $('.rider-name').text(data.riderName);
                                            $('#rider-info-details-pickUp').html(data.riderPickupLocation);
                                            $('#riderPickup').val(data.riderPickupLocation);
                                            $('#rider-info-details-dropOff').text(data.riderDropOffLocation);
                                            $('#riderDropOff').val(data.riderDropOffLocation);
                                            $('.rider-phone').text(data.riderContact);
                                            $('#tripIdForOtp').val(data.tripId);
                                            var imgElement = document.querySelector('.rider-info-img img');
                                            if (imgElement) {
                                                imgElement.src = data.photoLocation;
                                            }
                                            setDataForRiderAway(data.captainLocation, data.riderPickupLocation)
                                            showSuccesstMsg('Trip accepted successfully.');
                                        },
                                        error: function (jqXHR, textStatus, errorThrown) {
                                            console.error('AJAX Error: ', textStatus, errorThrown);
                                            showErrorMsg('Error accepting trip.');
                                        }
                                    });
                                });
                            });
                        });
                    } else {
                        showErrorMsg('Geocode was not successful for the following reason: ' + status);
                    }
                });
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.error('AJAX Error: ', textStatus, errorThrown);
            showErrorMsg('Error fetching location information.');
        }
    });
}

//
// // Function to calculate distance and time between two locations
// function setDataForRiderAway(riderLocation, captainLocation) {
//     var service = new google.maps.DistanceMatrixService();
//     service.getDistanceMatrix({
//         origins: [captainLocation],
//         destinations: [riderLocation],
//         travelMode: 'DRIVING',
//         unitSystem: google.maps.UnitSystem.METRIC,
//         avoidHighways: false,
//         avoidTolls: false
//     }, function (response, status) {
//         if (status === 'OK') {
//             var distance = response.rows[0].elements[0].distance.text;
//             var duration = response.rows[0].elements[0].duration.text;
//
//             // Example: Update UI elements with distance and duration
//             // Replace '#distanceElementId' and '#durationElementId' with your actual HTML element IDs
//             $('#rider-distance-info').text(distance);
//             $('#rider-time-info').text(duration);
//
//             // Example: Update DTO or any other data structure with distance and duration
//             // Replace 'yourDto.distance' and 'yourDto.duration' with your actual data structure
//             // yourDto.distance = distance;
//             // yourDto.duration = duration;
//         } else {
//             console.error('Error calculating distance: ', status);
//             // Handle error case, such as displaying an error message or fallback value
//         }
//     });
// }


// Function to handle geolocation
function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            initMap(position.coords.latitude, position.coords.longitude);
        }, function (error) {
            console.error('Geolocation Error: ', error);
            document.getElementById('getLocationBtn').style.display = 'block';
        });
    } else {
        showErrorMsg('Error: Your browser doesn\'t support geolocation.');
    }
}

// Function to geocode address
function geocodeAddress(address, callback) {
    geocoder.geocode({'address': address}, function (results, status) {
        callback(results, status);
    });
}

// Event listener for button click to get location
document.getElementById('getLocationBtn').addEventListener('click', getLocation);

// Ensure the map is initialized with default coordinates on load
function initDefaultMap() {
    var defaultLat = 23.0225; // Default latitude (Ahmedabad)
    var defaultLng = 72.5714; // Default longitude
    initMap(defaultLat, defaultLng);
}

// Wait until the Google Maps script is fully loaded
$(document).ready(function () {
    getLocation();
});


//calculate distance between two markers by using coordinates
function setDataForRiderAway(originAddress, destinationAddress) {

    const geocoder = new google.maps.Geocoder();
    const directionsService = new google.maps.DirectionsService();
    const directionsDisplay = new google.maps.DirectionsRenderer();
    const distanceService = new google.maps.DistanceMatrixService();
    const map = new google.maps.Map(document.getElementById('map'), {
        zoom: 12, center: {lat: 0, lng: 0}, // Set your map's initial center
    });
    directionsDisplay.setMap(map);
    const captainMarker = new google.maps.Marker({
        map: map, label: 'A', // Label for captain's location
    });
    const pickupMarker = new google.maps.Marker({
        map: map, label: 'B', // Label for pickup point
    });
    const dropoffMarker = new google.maps.Marker({
        map: map, label: 'c', // Label for dropoff point
    });

    geocoder.geocode({address: originAddress}, (originResults, originStatus) => {
        if (originStatus === 'OK') {
            const origin = originResults[0].geometry.location;

            geocoder.geocode({address: destinationAddress}, (destinationResults, destinationStatus) => {
                if (destinationStatus === 'OK') {
                    const destination = destinationResults[0].geometry.location;
                    const travelMode = google.maps.TravelMode.DRIVING;

                    distanceService.getDistanceMatrix({
                        origins: [origin], destinations: [destination], travelMode: travelMode,
                    }, (distanceResponse, distanceStatus) => {
                        if (distanceStatus === 'OK') {
                            const distance = distanceResponse.rows[0].elements[0].distance.text;
                            const time = distanceResponse.rows[0].elements[0].duration.text;

                            console.log(`Distance: ${distance}, Time: ${time}`);

                            $('#rider-distance-info').text(distance);
                            $('#rider-time-info').text(time);

                            const request = {
                                origin: origin, destination: destination, travelMode: travelMode,
                            };

                            let tripid = $("#general-trip-id").val();
                            let estimatedDistance = $(".cap-estimated-waiting-distance").html();
                            let estimatedTime = $(".cap-estimated-waiting-time").html();
                            console.log(estimatedTime + estimatedDistance + tripid)

                            let data = {
                                tripId: tripid, captainAway: estimatedDistance, captainEstimatedReachTime: estimatedTime
                            };


                            directionsService.route(request, (routeResponse, routeStatus) => {
                                if (routeStatus === 'OK') {
                                    directionsDisplay.setDirections(routeResponse);

                                    const route = routeResponse.routes[0];
                                    const steps = route.legs[0].steps;
                                    let stepIndex = 0;

                                    function moveNextStep() {
                                        if (stepIndex >= steps.length) {
                                            showSuccesstMsg('Confirm the riders otp.');
                                            $('#otp-form-id').removeClass('d-none');

//last step
                                            // saveRideStartInfo();
                                            // captainReached();
                                            return;
                                        }
                                        const step = steps[stepIndex];
                                        const nextSegment = step.path;
                                        for (let i = 0; i < nextSegment.length; i++) {
                                            captainMarker.setPosition(nextSegment[i]);
                                            map.panTo(nextSegment[i]);
                                            // You can update other markers (pickupMarker, dropoffMarker) similarly if needed
                                            // For example, pickupMarker.setPosition(pickupLocation);
                                        }
                                        console.log(`Moving to step ${stepIndex + 1}: ${step.instructions}`);
                                        stepIndex++;
                                        setTimeout(moveNextStep, 3000); // Simulate delay between steps
                                    }

                                    moveNextStep();

                                } else {
                                    console.error('Error calculating route:', routeStatus);
                                    showErrorMsg('Error calculating route:', routeStatus);
                                }
                            });
                        } else {
                            console.error('Error calculating distance:', distanceStatus);
                            showErrorMsg('Error calculating distance:', distanceStatus);
                        }
                    });
                } else {
                    console.error('Error geocoding destination address:', destinationStatus);
                    showErrorMsg('Error geocoding destination address:', destinationStatus);
                }
            });
        } else {
            console.error('Error geocoding origin address:', originStatus);
            showErrorMsg('Error geocoding origin address:', originStatus);
        }
    });
}

$(document).ready(function () {
    // Validate OTP form using jQuery Validation
    $('#otpForm').validate({
        rules: {
            captainOtp: {
                required: true,
                digits: true,
                maxlength: 4
            }
        },
        messages: {
            captainOtp: {
                required: "Please enter OTP",
                digits: "Please enter only digits",
                maxlength: "OTP must be exactly 4 digits"
            }
        },
        submitHandler: function (form) {
            // Extract tripId and otp from form fields
            let tripId = parseInt($('#tripIdForOtp').val(), 10); // Convert tripId to integer
            let otp = parseInt($('#opt').val(), 10); // Convert OTP to integer

            // Prepare payload object
            let payload = {
                tripId: tripId,
                otp: otp
            };

            // AJAX request to submit OTP and tripId
            $.ajax({
                url: $(form).attr('action'), // Form action URL
                method: 'POST',
                contentType: 'application/json', // Set content type to JSON
                data: JSON.stringify(payload), // Convert payload to JSON string
                success: function (response) {
                    // Handle success response
                    let origin = $('#riderPickup').val();
                    let destination = $('#riderDropOff').val();
                    console.log(response);
                    theFinalRide(origin, destination); // Assuming this function handles next steps
                    showSuccesstMsg('OTP verified successfully.');
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    // Handle error
                    console.error('AJAX Error: ', textStatus, errorThrown);
                    showErrorMsg(errorThrown);
                }
            });

            return false; // Prevent normal form submission
        }
    });
});


//calculate distance between two markers by using coordinates
function theFinalRide(originAddress, destinationAddress) {

    const geocoder = new google.maps.Geocoder();
    const directionsService = new google.maps.DirectionsService();
    const directionsDisplay = new google.maps.DirectionsRenderer();
    const distanceService = new google.maps.DistanceMatrixService();
    const map = new google.maps.Map(document.getElementById('map'), {
        zoom: 12, center: {lat: 0, lng: 0}, // Set your map's initial center
    });
    directionsDisplay.setMap(map);
    const captainMarker = new google.maps.Marker({
        map: map, label: 'C', // Label for captain's location
    });
    const pickupMarker = new google.maps.Marker({
        map: map, label: 'O', // Label for pickup point
    });
    const dropoffMarker = new google.maps.Marker({
        map: map, label: 'D', // Label for dropoff point
    });

    geocoder.geocode({address: originAddress}, (originResults, originStatus) => {
        if (originStatus === 'OK') {
            const origin = originResults[0].geometry.location;

            geocoder.geocode({address: destinationAddress}, (destinationResults, destinationStatus) => {
                if (destinationStatus === 'OK') {
                    const destination = destinationResults[0].geometry.location;
                    const travelMode = google.maps.TravelMode.DRIVING;

                    distanceService.getDistanceMatrix({
                        origins: [origin], destinations: [destination], travelMode: travelMode,
                    }, (distanceResponse, distanceStatus) => {
                        if (distanceStatus === 'OK') {
                            const distance = distanceResponse.rows[0].elements[0].distance.text;
                            const time = distanceResponse.rows[0].elements[0].duration.text;

                            console.log(`Distance: ${distance}, Time: ${time}`);

                            $('#rider-distance-info').text(distance);
                            $('#rider-time-info').text(time);

                            const request = {
                                origin: origin, destination: destination, travelMode: travelMode,
                            };


                            directionsService.route(request, (routeResponse, routeStatus) => {
                                if (routeStatus === 'OK') {
                                    directionsDisplay.setDirections(routeResponse);

                                    const route = routeResponse.routes[0];
                                    const steps = route.legs[0].steps;
                                    let stepIndex = 0;

                                    function moveNextStep() {
                                        if (stepIndex >= steps.length) {
                                            showSuccesstMsg('Confirm the riders otp.');
                                            // $('#otp-form-id').removeClass('d-none');

                                            $('#completionModal').modal('show');
                                            let charges = $('#rider-info-details-charges').html();
                                            $('#charges-in-pop-up').text(charges);

                                            return;
                                        }
                                        const step = steps[stepIndex];
                                        const nextSegment = step.path;
                                        for (let i = 0; i < nextSegment.length; i++) {
                                            captainMarker.setPosition(nextSegment[i]);
                                            map.panTo(nextSegment[i]);
                                            // You can update other markers (pickupMarker, dropoffMarker) similarly if needed
                                            // For example, pickupMarker.setPosition(pickupLocation);
                                        }
                                        console.log(`Moving to step ${stepIndex + 1}: ${step.instructions}`);
                                        stepIndex++;
                                        setTimeout(moveNextStep, 2000); // Simulate delay between steps
                                    }

                                    moveNextStep();

                                } else {
                                    console.error('Error calculating route:', routeStatus);
                                    showErrorMsg('Error calculating route:', routeStatus);
                                }
                            });
                        } else {
                            console.error('Error calculating distance:', distanceStatus);
                            showErrorMsg('Error calculating distance:', distanceStatus);
                        }
                    });
                } else {
                    console.error('Error geocoding destination address:', destinationStatus);
                    showErrorMsg('Error geocoding destination address:', destinationStatus);
                }
            });
        } else {
            console.error('Error geocoding origin address:', originStatus);
            showErrorMsg('Error geocoding origin address:', originStatus);
        }
    });
}
$(document).ready(function () {
    // Event listener for the "Conclude Ride" button
    $('#concludeRideBtn').on('click', function () {
        // Close the modal
        $('#completionModal').modal('hide');
        showSuccesstMsg('Ride completed successfully');

        setTimeout(function () {
            location.reload();
        }, 2000);
    });
});
