$(document).ready(function () {
    $('#ride, #package').click(function () {
        $('#ride, #package').removeClass('ride-type-active');
        $(this).addClass('ride-type-active');

        if ($(this).attr('id') === 'ride') {
            $('#rider-form').show();
            $('#package-form').hide();
            $('.package-submit').addClass('d-none'); // Hide the "Book Now" button
            initMap();

        } else {
            $('#rider-form').hide();
            $('#package-form').show();
            $('.package-submit').removeClass('d-none'); // Show the "Book Now" button
            rentAtaxiStaticLoc();
        }
    });
});


//jquerry validations of pickup , drop off and select
$(document).ready(function () {
    $("#rider-form").validate({
        rules: {
            pickup: {
                required: true, maxlength: 200
            }, dropoff: {
                required: true, maxlength: 200
            }, vehicleId: {
                required: true, digits: true, max: 4
            }, validLocation: {
                required: true
            }
        }, messages: {
            pickup: {
                required: "Please enter a pickup location", maxlength: "Cannot be more than 200 characters"
            }, dropoff: {
                required: "Please enter a drop off location", maxlength: "Cannot be more than 200 characters  "
            }, vehicleId: {
                required: "Please select a vehicle", digits: "Illigal vehicle type id", max: "Illigal vehicle type id"
            }, validLocation: {
                required: "Location doest not exist",
            }
        }, errorElement: "div", errorClass: "error",


        submitHandler: function (form) {
            var formData = $(form).serializeArray();
            var jsonData = {};

            let dropoff = $("#dropoff").val();
            let pickup = $("#pickup").val();
            let vehicleId = $("#vehicle-id").val();
            let distance = $(".dynamic-distance").text().trim();
            let estimatedTime = $(".dynamic-time").text().trim();
            let rs = $(".active").find(".vehicle-price-text").html();
            let numericValue = rs.replace(/rs\s*/i, ''); // Remove "rs " or "Rs " from the string, case insensitive
            console.log(numericValue.trim()); // Trim any leading or trailing whitespace


            console.log(JSON.stringify(jsonData))
            var jsonData = {
                dropoff: dropoff,
                pickup: pickup,
                vehicleId: vehicleId,
                distance: distance,
                estimatedTime: estimatedTime,
                charges: numericValue,
            };


            console.log(JSON.stringify(jsonData))
            $(".loader").css("display", "flex");

            $.ajax({
                url: form.action,
                method: form.method,
                data: JSON.stringify(jsonData),
                contentType: 'application/json',
                dataType: 'text',
                success: function (response) {
                    if (response.startsWith("Ride")) {
                        $('#waitTingModal').modal('show');
                        var id = response.split(" ").pop(); // Extract the last part which is the ID
                        $('#general-trip-id').val(id);
                        showSuccesstMsg("Ride Registered")
                        connectToBackend();
                    } else {
                        showErrorMsg(response);
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
    // Click handler for form submission
    $('#submitBtn').click(function () {
        $('#rider-form').submit(); // Trigger form submission
    });
});
$(document).ready(function () {
    $(".vehicle-row").on("click", function () {
        setActiveVehicle($(this));
    });

    $("#pickup").on("change", function () {
        setTimeout(setMapDetails, 1000);
    });
    $("#dropoff").on("change", function () {
        setTimeout(setMapDetails, 1000);
    });
});

function setActiveVehicle($target) {
    $(".active").removeClass("active");
    $target.addClass("active");
    // Set the value of the hidden input field
    $("#vehicle-id").val($target.data("vehicle-id"));
}

function setMapDetails() {
    var pickup = $("#pickup").val();
    var dropoff = $("#dropoff").val();
    var vehicleId = $("#vehicle-id").val();
    if (pickup && dropoff && pickup.trim() !== "" && dropoff.trim() !== "") {
        console.log(pickup, dropoff);
        calculateDistanceByAddress(pickup, dropoff);
    }
}

let map;
let userMarker;
let captainMarkers = [];
let distanceService;
let directionsDisplay;
const captains = [{id: 1, name: "Captain A", lat: 22.470701, lng: 70.057732}, {
    id: 2, name: "Captain B", lat: 22.3039, lng: 70.8022
}, {id: 3, name: "Captain C", lat: 22.8252, lng: 70.8491}];

window.onload = initMap;

function initMap() {

    //initializing the map and setting the center portion of the map
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 22.470701, lng: 70.057732}, zoom: 12
    });
    console.log("previsious click");
    distanceService = new google.maps.DistanceMatrixService();
    service = new google.maps.DistanceMatrixService();
    directionsDisplay = new google.maps.DirectionsRenderer({
        map: map,
    });
    console.log(navigator.geolocation)
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(position => {
            const pos = {
                lat: position.coords.latitude, lng: position.coords.longitude
            };

            userMarker = new google.maps.Marker({
                position: pos, map: map, title: 'Your Location'
            });

            //setting up the user marker (position)
            map.setCenter(pos);

            //captain market
            captains.forEach(captain => {
                const marker = new google.maps.Marker({
                    position: {lat: captain.lat, lng: captain.lng}, map: map, title: captain.name
                });

                marker.addListener('click', () => {
                    new google.maps.InfoWindow({
                        content: `<h2>${captain.name}</h2><p>Details about the captain...</p>`
                    }).open(map, marker);
                    calculateDistance(userMarker.getPosition(), marker.getPosition());
                });

                captainMarkers.push(marker);
            });


            // Watch for changes in the user's position
            navigator.geolocation.watchPosition(position => {
                const newPos = {
                    lat: position.coords.latitude, lng: position.coords.longitude
                };

                userMarker.setPosition(newPos);
                map.setCenter(newPos);
            }, error => {
                console.error('Error watching position:', error);
            });


        }, error => {
            console.error('Error getting current position:', error);
            elseCondition();

        });
    } else {

        console.log("Browser does not support geolocation");

        elseCondition();


    }
}

function getTimeWeightage() {
    const now = new Date();
    const currentHour = now.getHours();

    if (currentHour >= 19 || currentHour < 8) {
        return 3;
    } else if (currentHour >= 8 && currentHour < 14) {
        return 2;
    } else if (currentHour >= 14 && currentHour < 17) {
        return 2.5;
    } else if (currentHour >= 17 && currentHour < 19) {
        return 2;
    } else {
        return 1; // Default weightage
    }
}

function calculateDistanceByAddress(originAddress, destinationAddress) {
    const geocoder = new google.maps.Geocoder();

    geocoder.geocode({address: originAddress}, (results, status) => {
        if (status === 'OK' && results.length > 0) {
            const origin = results[0].geometry.location;
            $("#valid-location").val('');

            // Check if origin is outside Ahmedabad
            if (!isWithinAhmedabad(results[0].geometry.bounds || results[0].geometry.viewport)) {
                showErrorMsg('The origin address is outside Ahmedabad.');
                document.getElementById('submitBtn').disabled = true;
                return;
            }

            geocoder.geocode({address: destinationAddress}, (results, status) => {
                if (status === 'OK' && results.length > 0) {
                    const destination = results[0].geometry.location;

                    // Check if destination is outside Ahmedabad
                    if (!isWithinAhmedabad(results[0].geometry.bounds || results[0].geometry.viewport)) {
                        showErrorMsg('The destination address is outside Ahmedabad.');
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

                            if (distanceInKm < 0.1 || distanceInKm > 50) {
                                showErrorMsg('The distance should not be more than 50 km.');
                                document.getElementById('submitBtn').disabled = true;
                                return;
                            }

                            console.log(`Distance: ${distanceText}, Time: ${time}`);

                            // Set the distance and time to the elements
                            document.querySelectorAll('.dynamic-distance').forEach((element) => {
                                element.innerText = distanceText;
                            });

                            const distanceInNumber = Math.round(distanceInKm); // Round the distance

                            const multipliers = [5, 6, 8, 10];
                            const timeWeightage = getTimeWeightage(); // Get the time weightage

                            // Update price elements
                            document.querySelectorAll('.vehicle-price-text').forEach((element, index) => {
                                if (index < multipliers.length) {
                                    const price = Math.round(distanceInNumber * multipliers[index] * timeWeightage); // Calculate and round price
                                    element.innerHTML = `Rs ${price}`;
                                }
                            });

                            document.querySelectorAll('.dynamic-time').forEach((element) => {
                                element.innerText = time;
                            });

                            // Enable the submit button if everything is valid
                            document.getElementById('submitBtn').disabled = false;
                            $("#valid-location").val('Valid location');

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

function isWithinAhmedabad(bounds) {
    const ahmedabadBounds = new google.maps.LatLngBounds(new google.maps.LatLng(22.7924, 72.4575), // Southwest coordinates
        new google.maps.LatLng(23.2989, 72.7211)  // Northeast coordinates
    );
    return ahmedabadBounds.intersects(bounds);
}


// function initDirectionsDisplay() {
//     directionsDisplay = new google.maps.DirectionsRenderer({
//         map: map,
//     });
// }


//calculate distance between two markers by using coordinates
function calculateDistance(origin, destination) {
    distanceService.getDistanceMatrix({
        origins: [origin], destinations: [destination], travelMode: google.maps.TravelMode.DRIVING,
    }, (response, status) => {
        if (status === 'OK') {
            const distance = response.rows[0].elements[0].distance.text;
            document.getElementsByClassName('dynamic-distance').innerText = distance;
        } else {
            console.error('Error calculating distance:', status);
            showErrorMsg('Error calculating distance:', status);
        }
    });
}


//if the location is denied by the user then it will show this default location of mumbai
function elseCondition() {
    // If location permission is not given, use a default location
    const defaultLocation = {lat: 19.0760, lng: 72.8777};
    userMarker = new google.maps.Marker({
        position: defaultLocation, map: map, title: 'Default Location'
    });

    map.setCenter(defaultLocation);

    captains.forEach(captain => {
        const marker = new google.maps.Marker({
            position: {lat: captain.lat, lng: captain.lng}, map: map, title: captain.name
        });

        marker.addListener('click', () => {
            new google.maps.InfoWindow({
                content: `<h2>${captain.name}</h2><p>Details about the captain...</p>`
            }).open(map, marker);
            calculateDistance(userMarker.getPosition(), marker.getPosition());
        });

        captainMarkers.push(marker);
    });
}


// ----------auto suggestion ------------------------------

autocompletePickup = new google.maps.places.Autocomplete(document.getElementById("pickup"), {
    bounds: new google.maps.LatLngBounds(new google.maps.LatLng(20.5937, 78.9629), new google.maps.LatLng(35.5047, 92.4426)),
    componentRestrictions: {country: "in"},
    fields: ["address_components", "geometry", "icon", "name"],
    strictBounds: false,

});

autocompleteDropoff = new google.maps.places.Autocomplete(document.getElementById("dropoff"), {
    bounds: new google.maps.LatLngBounds(new google.maps.LatLng(20.5937, 78.9629), new google.maps.LatLng(35.5047, 92.4426)),
    componentRestrictions: {country: "in"},
    fields: ["address_components", "geometry", "icon", "name"],
    strictBounds: false,
});


// -------------setting up the live location ---------------------
function setLiveLocation() {

    // Check if the user has granted access to their location
    if (navigator.geolocation) {
        $(".pickup-placeholder").css("display", "none");
        $(".pickup-placeholder-img").css("display", "none");
        $(".search-loader").css("display", "initial");
        $("#pickup").val('');

        navigator.geolocation.getCurrentPosition(function (position) {
            // Get the latitude and longitude from the position object
            var lat = position.coords.latitude;
            var lng = position.coords.longitude;
            const defaultLocation = {lat, lng};

            userMarker = new google.maps.Marker({
                position: defaultLocation, map: map, title: 'Default Location'
            });

            map.setCenter(defaultLocation);


            // Use the Google Maps API to reverse geocode the coordinates
            var apiUrl = "https://maps.googleapis.com/maps/api/geocode/json";
            var apiKey = "AIzaSyDDCIb4xyEV8ok30VlxsidKGHw1NAlrfFM";
            var params = {
                latlng: lat + "," + lng, key: apiKey
            };


            $.ajax({
                url: apiUrl, data: params, dataType: "json", success: function (data) {
                    var address = data.results[0].formatted_address;
                    $("#pickup").val(address);
                    // Set the value of the input field with id "pickup"
                    $(".pickup-placeholder").css("display", "initial");
                    $(".search-loader").css("display", "none");
                    $(".pickup-placeholder-img").css("display", "initial");

                }
            });
        }, function (error) {
            elseConditionLiveLocation();
        });
    } else {
        elseConditionLiveLocation();
    }
}


function elseConditionLiveLocation() {
    showErrorMsg("Geolocation is not supported by this browser. Please allow access to your location or enter your location manually.");
    $(".pickup-placeholder").css("display", "initial");
    $(".search-loader").css("display", "none");
    $(".pickup-placeholder-img").css("display", "initial");
    $("#pickup").focus();
}


// ----------watiting modal progress bar -------------
const progressBar = document.querySelector('.progress-bar');
const loadingText = document.querySelector('.loading-text');
const loadingBarText = document.querySelector('.sr-only');

const timeRemainingElement = document.querySelector('#time-remaining');
let startTime;
let endTime;
let animationFrameId;

function updateProgress() {
    const currentTime = new Date().getTime();
    const remainingTime = Math.max(endTime - currentTime, 0);
    const minutes = Math.floor(remainingTime / 60000);
    const seconds = Math.floor((remainingTime % 60000) / 1000);
    timeRemainingElement.textContent = `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    const progress = 1 - (remainingTime / (endTime - startTime));
    progressBar.style.width = `${progress * 100}%`;
    if (remainingTime > 0) {
        animationFrameId = requestAnimationFrame(updateProgress);
    } else {
        loadingText.textContent = 'Estimated time is over .';
        loadingBarText.textContent = 'No captain has accepted your ride!';
    }
}

document.querySelector('#waitTingModal').addEventListener('show.bs.modal', () => {
    startTime = new Date().getTime();
    endTime = startTime + 300000; // 5 minutes in milliseconds
    animationFrameId = requestAnimationFrame(updateProgress);
});

document.querySelector('#waitTingModal').addEventListener('hide.bs.modal', () => {
    cancelAnimationFrame(animationFrameId);
    progressBar.style.width = '0%';
    timeRemainingElement.textContent = '05:00';
    loadingText.textContent = 'Loading...'; // Reset or change this text as needed
    loadingBarText.textContent = ''; // Reset or change this text as needed
});

// -------------------------------waiting modal submit---------------------------------------
$(document).ready(function () {
    $('#waiting-mod-cancel-btn').click(function (e) {
        e.preventDefault(); // Prevent default form submission

        var cancelReason = $('#cancel-reason-input').val();
        var tripId = $('#general-trip-id').val();
        // Check if cancelReason is empty
        if (cancelReason === '') {
            $('#cancel-reason-error').css('display', 'block'); // Show error message
            return;
        } else {
            $('#cancel-reason-error').css('display', 'none'); // Hide error message if shown
        }

        // Construct the URL relative to the context path
        var url = '/UrbanRides/rider/cancel-ride-submit'; // Adjust this URL as needed

        $.ajax({
            url: url, type: 'POST', data: {
                'cancelation-reason': cancelReason, 'trip-id': tripId
            }, success: function (response) {

                showSuccesstMsg("Ride Cancelled");
                console.log("Form submitted successfully:", response);
                $(".loader").hide();
                $('#waitTingModal').modal('hide');
                $('#rider-form')[0].reset(); // Reset form fields
                $(".dynamic-time").html('--')
                $(".dynamic-distance").html('--')
            }, error: function (xhr, status, error) {
                console.error("Error submitting form:", error);
                $(".loader").hide();
                showErrorMsg("Error occurred while cancelling the ride.");
            }
        });
    });
});

//calculate distance between two markers by using coordinates
function calculateDistanceByAddressForCaptainInfo(originAddress, destinationAddress) {

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

                            document.querySelectorAll('.cap-estimated-waiting-distance').forEach((element) => {
                                element.innerText = distance;
                            });

                            document.querySelectorAll('.cap-estimated-waiting-time').forEach((element) => {
                                element.innerText = time;
                            });

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

                            $.ajax({
                                type: "POST",
                                url: "/UrbanRides/rider/rider-reach-info",  // Adjust URL as per your actual endpoint
                                contentType: "application/json",
                                data: JSON.stringify(data),  // Convert data object to JSON string
                                success: function (response) {
                                    $("#general-tripdetails-id").val(response)
                                    console.log("AJAX request successful!");
                                }, error: function (xhr, status, error) {
                                    console.error("AJAX request failed with status:", status);
                                }
                            });


                            directionsService.route(request, (routeResponse, routeStatus) => {
                                if (routeStatus === 'OK') {
                                    directionsDisplay.setDirections(routeResponse);

                                    const route = routeResponse.routes[0];
                                    const steps = route.legs[0].steps;
                                    let stepIndex = 0;

                                    function moveNextStep() {
                                        if (stepIndex >= steps.length) {
                                            console.log('Reached destination.');
                                            //otp verification

                                            saveRideStartInfo();
                                            captainReached();
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

function richToDestination(originAddress, destinationAddress) {
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

                            document.querySelectorAll('.cap-estimated-waiting-distance').forEach((element) => {
                                element.innerText = distance;
                            });

                            document.querySelectorAll('.cap-estimated-waiting-time').forEach((element) => {
                                element.innerText = time;
                            });

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
                                            console.log('Reached destination.');
                                            //Ratting pop up
                                            setRattingModalDetails();
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
                                        setTimeout(moveNextStep, 1000); // Simulate delay between steps
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

function setRattingModalDetails() {
    let tripId = $("#general-tripdetails-id").val();


    $.ajax({
        type: "POST", url: "/UrbanRides/rider/ride-end-info",  // Adjust URL as per your actual endpoint
        data: {
            tripId: tripId
        }, success: function (response) {
            console.log(response);
            $('#rating-modal').modal('show');
            $(".captain-info-name").html('You have reached to your destination.');
            $(".pay-amount").text(response.charges + " Rs.");
            $(".available-balance-amount").text(response.balance);
            $("#amount").val(response.charges);
            $(".ratting-cap-name").html(response.captainName);
            var imgElement = document.querySelector('.captain-info-profile-conclude img');
            if (imgElement) {
                imgElement.src = response.profilePhoto;
            }
            console.log("Response from server:", response);
            // Handle success response here if needed
        }, error: function (xhr, status, error) {
            console.error("AJAX request failed with status:", status);
            // Handle error response here if needed
        }
    });
}

// --------------------------Socket code---------
var stompClient;

var marker;

function connectToBackend() {
    var socket = new SockJS('/UrbanRides/mywebsockets');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("Connected to Stomp broker: " + frame);

        stompClient.subscribe('/topic/rideConfirmed', function (message) {
            var parts = message.body.split(/:(.+)/);  // Split only at the first colon
            var confirmationMessage = parts[0];
            var captainInfoJson = parts[1];
            var captainInfo = JSON.parse(captainInfoJson);

            if (captainInfo) {
                showSuccesstMsg("Ride Confirmed, Captain is on the way");

                $('#waitTingModal').modal('hide');
                $(".ride-package").css('display', 'none');
                $("#captain-info").css('display', 'block');
                let pickup = $("#pickup").val();
                $(".registation-lable").text('Wait for Captain to arrive...')

                $(".captain-contact").html(captainInfo.captainContact);
                $(".vehicle-number").html(captainInfo.vehicleNumber);
                $(".captain-org-name").html(captainInfo.captainName);
                var imgElement = document.querySelector('.captain-info-profile img');
                if (imgElement) {
                    imgElement.src = captainInfo.photo;
                }
                window.updateRating(captainInfo.ratings);

                var otp = captainInfo.otp.toString().padStart(4, '0');
                var otpInputs = document.querySelectorAll('.otp-input');
                otpInputs.forEach((input, index) => {
                    input.value = otp.charAt(index);
                });
                calculateDistanceByAddressForCaptainInfo(captainInfo.riderAddress, pickup);

            } else {
                console.error("Received null or invalid captainInfo from backend.");
            }
        });
    }, function (error) {
        console.log("Error connecting to Stomp broker: " + error);
    });
}

function captainReached() {
    stompClient.subscribe('/topic/captainReached', function (message) {
        console.log('captain reached: ' + message.body);
        // showSuccessMsg("Captain reached, Enjoy the ride");
        try {

            $(".captain-info-name").html('Enjoy the ride');
            $(".estimated-waiting-time").html('Estimated Reach time :-');
            $(".away").html('Distance Away :-');
            $(".help-line").css('display', 'flex');
            $(".hide-cancel-ride").css('display', 'none');
            $("#captain-info-waiting-cancel").css('display', 'none');
            $(".captain-info-cancel-text").css('display', 'none');
            $(".hide-hr").css('display', 'none');


            let dropoff = $("#dropoff").val();
            let pickup = $("#pickup").val();
            richToDestination(pickup, dropoff);


        } catch (e) {
            console.error("Error parsing reached update message:", e);
        }
    });
}

function saveRideStartInfo() {
    let tripId = $("#general-tripdetails-id").val();

    // let data = {
    //     tripId: tripId,
    // };
    //
    // $.ajax({
    $.ajax({
        type: "POST", url: "/UrbanRides/rider/ride-start-info",  // Adjust URL as per your actual endpoint
        data: {
            tripId: tripId
        }, success: function (response) {
            showSuccesstMsg("Rider has reached, please confirm the OTP");

            console.log("AJAX request successful!");
            // Handle success response here if needed
        }, error: function (xhr, status, error) {
            console.error("AJAX request failed with status:", status);
            // Handle error response here if needed
        }
    });
}

// ---otp field in captin info ---------
document.querySelectorAll('.otp-input').forEach((element, index, array) => {
    element.addEventListener('input', function (event) {
        let inputValue = event.target.value;
        inputValue = inputValue.replace(/[^0-9]/g, '');
        inputValue = inputValue.slice(0, 1);
        event.target.value = inputValue;

        if (inputValue !== '') {
            // Move focus to the next input field
            if (index < array.length - 1) {
                array[index + 1].focus();
            }
        } else {
            // Move focus to the previous input field
            if (index > 0) {
                array[index - 1].focus();
            }
        }
    });

    // Add a blur event listener to handle cases where the user clicks or tabs away
    element.addEventListener('blur', function () {
        // If the input is empty, move focus to the previous input field
        if (element.value === '' && index > 0) {
            array[index - 1].focus();
        }
    });
});


// rating------------
document.addEventListener('DOMContentLoaded', function () {
    const stars = document.querySelectorAll('.star');
    const ratingText = document.getElementById('rating-text');

    function updateRating(rating) {
        stars.forEach((star, index) => {
            star.classList.remove('star-filled', 'star-half-filled');
            if (index < Math.floor(rating)) {
                star.classList.add('star-filled');
            } else if (index === Math.floor(rating) && rating % 1 !== 0) {
                star.classList.add('star-half-filled');
            }
        });
        // ratingText.textContent = rating + ' stars';
    }

    stars.forEach((star, index) => {
        star.addEventListener('click', () => {
            rating = (rating === index + 1) ? index + 0.5 : index + 1;
            updateRating(rating);
        });
    });

    // Export the updateRating function to be accessible globally
    window.updateRating = updateRating;
});


document.addEventListener('DOMContentLoaded', function () {
    const stars = document.querySelectorAll('.give-star');
    const ratingText = document.getElementById('rating-text-conclude');
    let rating = 5; // Example rating from backend

    function updateRating(rating) {
        stars.forEach((star, index) => {
            star.classList.remove('star-filled', 'star-half-filled');
            if (index < Math.floor(rating)) {
                star.classList.add('star-filled');
            } else if (index === Math.floor(rating) && rating % 1 !== 0) {
                star.classList.add('star-half-filled');
            }
        });
        ratingText.textContent = rating + ' stars';
    }

    updateRating(rating);

    stars.forEach((star, index) => {
        star.addEventListener('click', () => {
            rating = (rating === index + 1) ? index + 0.5 : index + 1;
            updateRating(rating);
        });
    });
});

/*
document.getElementById('ratting-modal-submit-btn').addEventListener('click', function () {
    window.location.reload();
});
*/


$(document).ready(function () {
    // Event listener for clicking on payment options
    $('.pay-option').click(function () {
        // Remove 'rating-active' class from all pay-options
        $('.pay-option').removeClass('rating-active');

        // Add 'rating-active' class to the clicked pay-option
        $(this).addClass('rating-active');

        // Show or hide the input field based on the selected payment option
        if ($(this).hasClass('pay-with-wallet')) {
            $('#payment-field').show();
        } else {
            $('#payment-field').hide();
        }
    });
});

$('#rating-modal-form-id').submit(function (event) {
    event.preventDefault(); // Prevent default form submission

    // Fetch form data
    let feedback = $('#feedback').val(); // Fetch the feedback value
    let payMethod = $('.pay-option.rating-active').text().trim(); // Fetch the payment method
    let ratting = $('.long-rattings').text().trim(); // Get the text "0 stars"
    let ratingValue = ratting.match(/[\d.]+/)[0]; // Extract the digit (can include decimals)
    let tripId = $("#general-tripdetails-id").val(); // Fetch Trip ID

    // Display fetched values for verification (optional)
    console.log('Feedback:', feedback);
    console.log('Payment Method:', payMethod);
    console.log('Ratting:', ratting);
    console.log('Rating Value:', ratingValue);
    console.log('Trip ID:', tripId);


    let data = {
        tripId: tripId,
        feedback: feedback,
        payMethod: payMethod,
        rattings: ratingValue
    };
    // Ajax call to submit data
    $.ajax({
        type: "POST",
        url: "/UrbanRides/rider/ride-ratting-submit",  // Adjust URL as per your actual endpoint
        contentType: "application/json",
        data: JSON.stringify(data),


        success: function (response) {
            console.log('Response:', response);
            $('#rating-modal').modal('hide');

            showSuccesstMsg('Ride Completed');

            // Reload the page after 2 seconds
            setTimeout(function () {
                location.reload();
            }, 3000); // 2000 milliseconds = 2 seconds
        },

        error: function (error) {
            // Handle any errors
            console.error('Error:', error);
            showErrorMsg('An error occurred while submitting your feedback.');
        }
    });
});
