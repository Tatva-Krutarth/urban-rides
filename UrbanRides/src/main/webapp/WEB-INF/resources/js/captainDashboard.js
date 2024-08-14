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
    getAddressFromCoordinates(latitude, longitude);
}


// Start the interval for refreshing the data every 10 seconds
function startRefreshingTripsData() {
    refreshInterval = setInterval(loadTripsData, 10000);
}

// Stop the interval when the ride is accepted
function stopRefreshingTripsData() {
    clearInterval(refreshInterval);
}


function clearMarkers() {
    console.log("clearMarkers function called"); // Debugging log

    if (markersArray.length === 0) {
        console.log("No markers to clear");
        return; // Exit if there are no markers
    }

    markersArray.forEach(function (marker, index) {
        console.log("Clearing marker", index, marker); // Debugging log
        if (marker) {
            marker.setMap(null);
        }
    });

    markersArray = []; // Reset the markers array
}

function loadTripsData() {
    $.ajax({
        url: 'get-all-trips-data', // Replace with your actual backend endpoint
        method: 'GET',
        success: function (data) {
            console.log("Data fetched:", data); // Debugging log
            clearMarkers(); // Ensure this is called

            if (!data || data.length === 0) {
                console.log("No trips available"); // Debugging log
                return; // Exit if no trips are found
            }


            data.forEach(function (trip) {
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

                        // Add marker to the array
                        markersArray.push(tripMarker);
                        console.log("Marker added:", tripMarker); // Debugging log

                        tripMarker.addListener('click', function () {
                            infoWindow.open(map, tripMarker);
                        });

                        google.maps.event.addListener(infoWindow, 'domready', function () {
                            document.querySelectorAll('.acceptTripBtn').forEach(function (button) {
                                button.addEventListener('click', function () {
                                    var tripId = this.getAttribute('data-tripid');
                                    $.ajax({
                                        url: 'accept-ride', // Replace with your actual backend endpoint
                                        method: 'POST',
                                        data: {tripId: tripId},
                                        dataType: 'json',
                                        success: function (data) {
                                            $("#refresh-trip-id").val("dont-refresh");
                                            stopRefreshingTripsData();
                                            $('#dash-first-ui').hide();
                                            $('#rider-location-text').removeClass('d-none');
                                            $('#captain-rider-info-cont').removeClass('d-none');
                                            $('#rider-info-details-charges').html(data.charges + ' Rs');
                                            $('.rider-name').text(data.riderName);
                                            $('#rider-info-details-pickUp').html(data.riderPickupLocation);
                                            $('#riderPickup').val(data.riderPickupLocation);
                                            $('#rider-info-details-dropOff').text(data.riderDropOffLocation);
                                            $('#riderDropOff').val(data.riderDropOffLocation);
                                            $('.rider-phone').text(data.riderContact);
                                            $('#tripIdForOtp').val(data.tripId);
                                            var imgElement = document.querySelector('.rider-info-img img');
                                            if (imgElement && data.photoLocation) {
                                                imgElement.src = data.photoLocation;
                                            }
                                            showSuccesstMsg('Trip accepted successfully.');
                                            disableNavbarLinks();
                                            setDataForRiderAway(data.captainLocation, data.riderPickupLocation);
                                        },
                                        error: function (jqXHR, textStatus, errorThrown) {
                                            console.error('AJAX Error:', textStatus, errorThrown);
                                            if (jqXHR.responseJSON && jqXHR.responseJSON.message) {
                                                showErrorMsg(jqXHR.responseJSON.message);
                                            } else {
                                                showErrorMsg("An unexpected error occurred.");
                                            }
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
            console.error('AJAX Error:', textStatus, errorThrown);
            showErrorMsg('Error fetching location information.');
        }
    });
}

let markersArray = []; // Ensure this is global and accessible
let refreshInterval;


// Function to geocode address
function geocodeAddress(address, callback) {
    geocoder.geocode({'address': address}, function (results, status) {
        callback(results, status);
    });
}

function disableNavbarLinks() {
    $('nav a').each(function () {
        console.log("all links disabled")
        $(this).addClass('disabled-link'); // Add a class to visually indicate disabled state
        $(this).attr('href', '#'); // Override href to prevent navigation
    });
}

// Function to handle geolocation
function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {

            initMap(position.coords.latitude, position.coords.longitude);

        }, function (error) {

            $("#getLocationBtn").css("display", "block");
            showErrorMsg("Please allow the live location")
            initDefaultMap()
            console.error('Geolocation Error: ', error);
        });
    } else {
        showErrorMsg('Error: Your browser doesn\'t support geolocation.');
    }
}


// Function to get address from coordinates
function getAddressFromCoordinates(latitude, longitude) {
    var geocoder = new google.maps.Geocoder();
    var latlng = {lat: parseFloat(latitude), lng: parseFloat(longitude)};
    geocoder.geocode({'location': latlng}, function (results, status) {
        if (status === 'OK') {
            if (results[0]) {
                var address = results[0].formatted_address;
                console.log('Address:', address);
                saveAddressToBackend(address);
            } else {
                showErrorMsg('No results found');
            }
        } else {
            showErrorMsg('Geocoder failed due to: ' + status);
        }
    });
}

// Function to save address to the backend
function saveAddressToBackend(address) {
    $.ajax({
        url: 'save-captain-location', // Replace with your actual backend endpoint
        method: 'POST',
        data: {
            address: address
        },
        success: function (response) {
            console.log('Address saved successfully:', response);

            // Load trips data from backend
            startRefreshingTripsData();
        },
        error: function (xhr, status, error) {
            console.error('Error saving address:', error);
        }
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
    $("#getLocationBtn").hide();

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
                    $('#otp-form-id').addClass('d-none');

                    let origin = $('#riderPickup').val();
                    let destination = $('#riderDropOff').val();
                    console.log(response);
                    theFinalRide(origin, destination); // Assuming this function handles next steps
                    showSuccesstMsg('OTP verified successfully.');
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    // Log full error response
                    console.error('AJAX Error: ', jqXHR, textStatus, errorThrown);
                    console.log('Response Text: ', jqXHR.responseText);
                    showErrorMsg(jqXHR.responseText || errorThrown);
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
                                            showSuccesstMsg('You have reached your destination.');
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
    // Event listener for the "Conclude Ride" button
    $('#concludeRideBtn').on('click', function () {
        // Close the modal
        $('#completionModal').modal('hide');

        // Show success message
        showSuccesstMsg('Ride completed successfully');

        // Reload the page after 3 seconds
        setTimeout(function () {
            location.reload();
        }, 3000); // 3000 milliseconds = 3 seconds
    });
});

