var map, geocoder;

function initMap(latitude, longitude) {
    var mapOptions = {
        center: new google.maps.LatLng(latitude, longitude),
        zoom: 15
    };
    map = new google.maps.Map(document.getElementById('map'), mapOptions);
    geocoder = new google.maps.Geocoder();

    var marker = new google.maps.Marker({
        position: {lat: latitude, lng: longitude},
        map: map,
        title: 'Your Location'
    });
    getAddressFromCoordinates(latitude, longitude);
}


function startRefreshingTripsData() {
    refreshInterval = setInterval(loadTripsData, 10000);
}

function stopRefreshingTripsData() {
    clearInterval(refreshInterval);
}


function clearMarkers() {

    if (markersArray.length === 0) {
        return;
    }

    markersArray.forEach(function (marker, index) {
        if (marker) {
            marker.setMap(null);
        }
    });

    markersArray = [];
}

function loadTripsData() {
    $.ajax({
        url: 'get-all-trips-data',
        method: 'GET',
        success: function (data) {
            clearMarkers();

            if (!data || data.length === 0) {
                return;
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

                        markersArray.push(tripMarker);

                        tripMarker.addListener('click', function () {
                            infoWindow.open(map, tripMarker);
                        });

                        google.maps.event.addListener(infoWindow, 'domready', function () {
                            document.querySelectorAll('.acceptTripBtn').forEach(function (button) {
                                button.addEventListener('click', function () {
                                    var tripId = this.getAttribute('data-tripid');
                                    $.ajax({
                                        url: 'accept-ride',
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
            showErrorMsg('Error fetching location information.');
        }
    });
}

let markersArray = [];
let refreshInterval;


// Function to geocode address
function geocodeAddress(address, callback) {
    geocoder.geocode({'address': address}, function (results, status) {
        callback(results, status);
    });
}

function disableNavbarLinks() {
    $('nav a').each(function () {
        $(this).addClass('disabled-link');
        $(this).attr('href', '#');
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
        });
    } else {
        showErrorMsg('Error: Your browser doesn\'t support geolocation.');
    }
}

function getAddressFromCoordinates(latitude, longitude) {
    var geocoder = new google.maps.Geocoder();
    var latlng = {lat: parseFloat(latitude), lng: parseFloat(longitude)};
    geocoder.geocode({'location': latlng}, function (results, status) {
        if (status === 'OK') {
            if (results[0]) {
                var address = results[0].formatted_address;
                saveAddressToBackend(address);
            } else {
                showErrorMsg('No results found');
            }
        } else {
            showErrorMsg('Geocoder failed due to: ' + status);
        }
    });
}

function saveAddressToBackend(address) {
    $.ajax({
        url: 'save-captain-location',
        method: 'POST',
        data: {
            address: address
        },
        success: function (response) {
            startRefreshingTripsData();
        },
        error: function (xhr, status, error) {
        }
    });
}

document.getElementById('getLocationBtn').addEventListener('click', getLocation);

function initDefaultMap() {
    var defaultLat = 23.0225;
    var defaultLng = 72.5714;
    initMap(defaultLat, defaultLng);
}

$(document).ready(function () {
    $("#getLocationBtn").hide();
    getLocation();
});

function setDataForRiderAway(originAddress, destinationAddress) {
    const geocoder = new google.maps.Geocoder();
    const directionsService = new google.maps.DirectionsService();
    const directionsDisplay = new google.maps.DirectionsRenderer();
    const distanceService = new google.maps.DistanceMatrixService();
    const map = new google.maps.Map(document.getElementById('map'), {
        zoom: 12, center: {lat: 0, lng: 0},
    });
    directionsDisplay.setMap(map);
    const captainMarker = new google.maps.Marker({
        map: map, label: 'C',
    });
    const pickupMarker = new google.maps.Marker({
        map: map, label: 'O',
    });
    const dropoffMarker = new google.maps.Marker({
        map: map, label: 'D',
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


                            $('#rider-distance-info').text(distance);
                            $('#rider-time-info').text(time);

                            const request = {
                                origin: origin, destination: destination, travelMode: travelMode,
                            };

                            let tripid = $("#general-trip-id").val();
                            let estimatedDistance = $(".cap-estimated-waiting-distance").html();
                            let estimatedTime = $(".cap-estimated-waiting-time").html();

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
                                            return;
                                        }
                                        const step = steps[stepIndex];
                                        const nextSegment = step.path;
                                        for (let i = 0; i < nextSegment.length; i++) {
                                            captainMarker.setPosition(nextSegment[i]);
                                            map.panTo(nextSegment[i]);
                                        }
                                        stepIndex++;
                                        setTimeout(moveNextStep, 3000);
                                    }

                                    moveNextStep();
                                } else {
                                    showErrorMsg('Error calculating route:', routeStatus);
                                }
                            });
                        } else {
                            showErrorMsg('Error calculating distance:', distanceStatus);
                        }
                    });
                } else {
                    showErrorMsg('Error geocoding destination address:', destinationStatus);
                }
            });
        } else {
            showErrorMsg('Error geocoding origin address:', originStatus);
        }
    });
}

$(document).ready(function () {
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
            let tripId = parseInt($('#tripIdForOtp').val(), 10);
            let otp = parseInt($('#opt').val(), 10);

            let payload = {
                tripId: tripId,
                otp: otp
            };

            $.ajax({
                url: $(form).attr('action'),
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(payload),
                success: function (response) {
                    $('#otp-form-id').addClass('d-none');
                    let origin = $('#riderPickup').val();
                    let destination = $('#riderDropOff').val();
                    theFinalRide(origin, destination);
                    showSuccesstMsg('OTP verified successfully.');
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    showErrorMsg(jqXHR.responseText || errorThrown);
                }
            });

            return false;
        }
    });
});

function theFinalRide(originAddress, destinationAddress) {

    const geocoder = new google.maps.Geocoder();
    const directionsService = new google.maps.DirectionsService();
    const directionsDisplay = new google.maps.DirectionsRenderer();
    const distanceService = new google.maps.DistanceMatrixService();
    const map = new google.maps.Map(document.getElementById('map'), {
        zoom: 12, center: {lat: 0, lng: 0},
    });
    directionsDisplay.setMap(map);
    const captainMarker = new google.maps.Marker({
        map: map, label: 'C',
    });
    const pickupMarker = new google.maps.Marker({
        map: map, label: 'O',
    });
    const dropoffMarker = new google.maps.Marker({
        map: map, label: 'D',
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
                                        }
                                        stepIndex++;
                                        setTimeout(moveNextStep, 3000);
                                    }

                                    moveNextStep();

                                } else {
                                    showErrorMsg('Error calculating route:', routeStatus);
                                }
                            });
                        } else {
                            showErrorMsg('Error calculating distance:', distanceStatus);
                        }
                    });
                } else {
                    showErrorMsg('Error geocoding destination address:', destinationStatus);
                }
            });
        } else {
            showErrorMsg('Error geocoding origin address:', originStatus);
        }
    });
}


$(document).ready(function () {
    $('#concludeRideBtn').on('click', function () {
        $('#completionModal').modal('hide');
        showSuccesstMsg('Ride completed successfully');
        setTimeout(function () {
            location.reload();
        }, 3000);
    });
});

