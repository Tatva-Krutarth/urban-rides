$(document).ready(function () {
    $('#ride, #package').click(function () {
        $('#ride, #package').removeClass('ride-type-active');
        $(this).addClass('ride-type-active');

        if ($(this).attr('id') === 'ride') {
            $('#rider-form').show();
            $('#rider-form')[0].reset();
            $('#package-form').hide();
            $('.package-submit').addClass('d-none');
            getCaptainsDetails();

        } else {
            $('#rider-form').hide();
            $('#package-form').show();
            $('.package-submit').removeClass('d-none');
            rentAtaxiStaticLoc();
        }
    });
});


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
            let numericValue = rs.replace(/rs\s*/i, '');
            var jsonData = {
                dropoff: dropoff,
                pickup: pickup,
                vehicleId: vehicleId,
                distance: distance,
                estimatedTime: estimatedTime,
                charges: numericValue,
            };
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
                        var id = response.split(" ").pop();
                        $('#general-trip-id').val(id);

                        captainMarkers.forEach((marker) => {
                            marker.setMap(null);
                        });
                        captainMarkers = [];

                        showSuccesstMsg("Ride Registered")
                        disableNavbarLinks();
                        connectToBackend();
                    } else {
                        showErrorMsg(response);
                    }
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
        }
    });
    $('#submitBtn').click(function () {
        $('#rider-form').submit();
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
        calculateDistanceByAddress(pickup, dropoff);
    }
}

let map;
let userMarker;
let captainMarkers = [];
let distanceService;
let directionsDisplay;

window.onload = getCaptainsDetails;

function getCaptainsDetails() {
    $.ajax({
        url: 'get-captain-details', method: 'GET', dataType: 'json', success: function (response) {
            initMap(response);
        }, error: function (xhr, textStatus, errorThrown) {
            showErrorMsg('Failed to fetch captain details. Please try again later.');
        }
    });
}

function initMap(captains) {
    // initializing the map and setting the center portion of the map
    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 22.470701, lng: 70.057732}, zoom: 12
    });

    distanceService = new google.maps.DistanceMatrixService();
    directionsDisplay = new google.maps.DirectionsRenderer({map: map});

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(position => {
            const pos = {
                lat: position.coords.latitude, lng: position.coords.longitude
            };

            userMarker = new google.maps.Marker({
                position: pos, map: map, title: 'Your Location'
            });

            map.setCenter(pos);

            if (captains && captains.length > 0) {
                captains.forEach(captain => {
                    const geocoder = new google.maps.Geocoder();
                    geocoder.geocode({address: captain.captainLocation}, (results, status) => {
                        if (status === 'OK') {
                            const lat = results[0].geometry.location.lat();
                            const lng = results[0].geometry.location.lng();

                            const marker = new google.maps.Marker({
                                position: {lat: lat, lng: lng}, map: map, title: captain.captainName
                            });

                            marker.addListener('click', () => {
                                new google.maps.InfoWindow({
                                    content: `<h2>${captain.captainName}</h2>`
                                }).open(map, marker);
                                calculateDistancee(userMarker.getPosition(), marker.getPosition());
                            });

                            captainMarkers.push(marker);
                        } else {
                            showErrorMsg('Error geocoding captain location:', status);
                        }
                    });
                });
            } else {
                showErrorMsg('No captain details available.');
            }

            // Watch for changes in the user's position
            navigator.geolocation.watchPosition(position => {
                const newPos = {
                    lat: position.coords.latitude, lng: position.coords.longitude
                };

                userMarker.setPosition(newPos);
                map.setCenter(newPos);
            }, error => {
                showErrorMsg('Error watching position:', error);
            });

        }, error => {
            showErrorMsg('Error getting current position:', error);
            elseCondition();
        });
    } else {
        showErrorMsg("Browser does not support geolocation");
        elseCondition();
    }
}

function calculateDistancee(userPosition, captainPosition) {
    const request = {
        origins: [userPosition], destinations: [captainPosition], travelMode: 'DRIVING'
    };

    distanceService.getDistanceMatrix(request, (response, status) => {
        if (status === 'OK') {
            const distance = response.rows[0].elements[0].distance.text;
        } else {
            showErrorMsg('Error calculating distance:', status);
        }
    });
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
            if (!isWithinAhmedabad(results[0].geometry.bounds || results[0].geometry.viewport)) {
                showErrorMsg('The origin address is outside Ahmedabad.');
                document.getElementById('submitBtn').disabled = true;
                return;
            }

            geocoder.geocode({address: destinationAddress}, (results, status) => {
                if (status === 'OK' && results.length > 0) {
                    const destination = results[0].geometry.location;
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
                            const distanceInKm = distanceValue / 1000;
                            const distanceTextTrimmed = parseFloat(distanceText); // Extracts the numeric part, e.g., 4.5

                            if (distanceInKm < 1 || distanceInKm > 50) {
                                showErrorMsg('The distance should be between 1 than 50 km.');
                                document.getElementById('submitBtn').disabled = true;
                                return;
                            }
                            document.querySelectorAll('.dynamic-distance').forEach((element) => {
                                element.innerText = distanceText;
                            });
                            const distanceInNumber = (distanceTextTrimmed % 1 === 0.5)
                                ? Math.ceil(distanceInKm)
                                : Math.round(distanceInKm);
                            const multipliers = [5, 6, 8, 10];
                            const timeWeightage = getTimeWeightage();
                            document.querySelectorAll('.vehicle-price-text').forEach((element, index) => {
                                if (index < multipliers.length) {
                                    const price = Math.round(distanceInNumber * multipliers[index] * timeWeightage);
                                    element.innerHTML = `Rs ${price}`;
                                }
                            });

                            document.querySelectorAll('.dynamic-time').forEach((element) => {
                                element.innerText = time;
                            });

                            document.getElementById('submitBtn').disabled = false;
                            $("#valid-location").val('Valid location');

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


function disableNavbarLinks() {
    $('nav a').each(function () {
        $(this).addClass('disabled-link');
        $(this).attr('href', '#');
    });
}


function calculateDistance(origin, destination) {
    distanceService.getDistanceMatrix({
        origins: [origin], destinations: [destination], travelMode: google.maps.TravelMode.DRIVING,
    }, (response, status) => {
        if (status === 'OK') {
            const distance = response.rows[0].elements[0].distance.text;
            document.getElementsByClassName('dynamic-distance').innerText = distance;
        } else {
            showErrorMsg('Error calculating distance:', status);
        }
    });
}


function elseCondition() {
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


function setLiveLocation() {
    $(".loader").css("display", "flex");
    if (navigator.geolocation) {
        $(".pickup-placeholder").css("display", "none");
        $(".pickup-placeholder-img").css("display", "none");
        $(".search-loader").css("display", "initial");
        $("#pickup").val('');
        $("#pickup").prop("readonly", true);
        const geolocationPromise = new Promise((resolve, reject) => {
            const timeout = setTimeout(() => {
                reject(new Error('Geolocation request timed out'));
            }, 5000);

            navigator.geolocation.getCurrentPosition(position => {
                clearTimeout(timeout);
                resolve(position);
            }, error => {
                clearTimeout(timeout);
                reject(error);
            });
        });
        geolocationPromise.then(position => {
            var lat = position.coords.latitude;
            var lng = position.coords.longitude;
            const defaultLocation = {lat, lng};
            userMarker = new google.maps.Marker({
                position: defaultLocation, map: map, title: 'Default Location'
            });
            var apiUrl = "https://maps.googleapis.com/maps/api/geocode/json";
            var apiKey = "AIzaSyDDCIb4xyEV8ok30VlxsidKGHw1NAlrfFM";
            var params = {
                latlng: lat + "," + lng, key: apiKey
            };
            $.ajax({
                url: apiUrl, data: params, dataType: "json", success: function (data) {
                    var address = data.results[0].formatted_address;
                    $("#pickup").val(address);
                    $(".pickup-placeholder").css("display", "initial");
                    $(".search-loader").css("display", "none");
                    $(".pickup-placeholder-img").css("display", "initial");
                    $("#pickup").prop("readonly", false);
                    $(".loader").hide();
                }, error: function () {
                    showErrorMsg("Error: Unable to retrieve address from coordinates.");
                    elseConditionLiveLocation();
                }
            });
        }).catch(error => {
            showErrorMsg("Getting live location timeout");
            elseConditionLiveLocation();
        });
    } else {
        showErrorMsg("Error: Geolocation is not supported by this browser.");
        elseConditionLiveLocation();
    }
}


function elseConditionLiveLocation() {
    $(".pickup-placeholder").css("display", "initial");
    $(".search-loader").css("display", "none");
    $(".pickup-placeholder-img").css("display", "initial");
    $("#pickup").focus();
    $("#pickup").prop("readonly", false);
    $(".loader").hide();

}


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
    loadingText.textContent = 'Loading...';
    loadingBarText.textContent = '';
});

$(document).ready(function () {
    $('#waiting-mod-cancel-btn').click(function (e) {
        e.preventDefault(); // Prevent default form submission

        var cancelReason = $('#cancel-reason-input').val();
        var tripId = $('#general-trip-id').val();
        if (cancelReason === '') {
            $('#cancel-reason-error').css('display', 'block');
            return;
        } else {
            $('#cancel-reason-error').css('display', 'none');
        }

        var url = '/UrbanRides/rider/cancel-ride-submit';

        $.ajax({
            url: url, type: 'POST', data: {
                'cancelation-reason': cancelReason, 'trip-id': tripId
            }, success: function (response) {

                showSuccesstMsg("Ride Cancelled");
                $(".loader").hide();
                $('#waitTingModal').modal('hide');
                $('#rider-form')[0].reset();
                $(".dynamic-time").html('--')
                $(".dynamic-distance").html('--')
                location.reload();
            }, error: function (xhr, status, error) {
                $(".loader").hide();
                showErrorMsg("The cancellation reason cannot be more than 200 character.");
            }
        });
    });
});

function calculateDistanceByAddressForCaptainInfo(originAddress, destinationAddress) {

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

                            let data = {
                                tripId: tripid, captainAway: estimatedDistance, captainEstimatedReachTime: estimatedTime
                            };

                            $.ajax({
                                type: "POST",
                                url: "/UrbanRides/rider/rider-reach-info",
                                contentType: "application/json",
                                data: JSON.stringify(data),
                                success: function (response) {
                                    $("#general-tripdetails-id").val(response)
                                },
                                error: function (xhr, status, error) {
                                    showErrorMsg("AJAX request failed with status:", status);
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
                                            saveRideStartInfo();
                                            captainReached();
                                            return;
                                        }
                                        const step = steps[stepIndex];
                                        const nextSegment = step.path;
                                        for (let i = 0; i < nextSegment.length; i++) {
                                            captainMarker.setPosition(nextSegment[i]);
                                            map.panTo(nextSegment[i]);
                                        }
                                        stepIndex++;
                                        setTimeout(moveNextStep, 3000); // Simulate delay between steps
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

function richToDestination(originAddress, destinationAddress) {
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
                                            setRattingModalDetails();
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

function setRattingModalDetails() {
    let tripId = $("#general-tripdetails-id").val();
    $.ajax({
        type: "POST", url: "/UrbanRides/rider/ride-end-info", data: {
            tripId: tripId
        }, success: function (response) {
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
        }, error: function (xhr, status, error) {
            showErrorMsg("Error while sending end info");
        }
    });
}

var stompClient;

var marker;

function connectToBackend() {
    var socket = new SockJS('/UrbanRides/rider-cabooking');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {

        stompClient.subscribe('/topic/rideConfirmed', function (message) {
            var parts = message.body.split(/:(.+)/);
            var confirmationMessage = parts[0];
            var captainInfoJson = parts[1];
            var captainInfo = JSON.parse(captainInfoJson);

            if (captainInfo) {

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
                showErrorMsg("Received null or invalid captainInfo from backend.");
            }
        });
    }, function (error) {
        showErrorMsg("Error connecting to Stomp broker: " + error);
    });
}

function captainReached() {
    stompClient.subscribe('/topic/captain-reached', function (message) {
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
            showErrorMsg("Error parsing reached update message:", e);
        }
    });
}

function saveRideStartInfo() {
    let tripId = $("#general-tripdetails-id").val();
    $.ajax({
        type: "POST", url: "/UrbanRides/rider/ride-start-info", data: {
            tripId: tripId
        }, success: function (response) {
            showSuccesstMsg("Rider has reached, please confirm the OTP");
        }, error: function (xhr, status, error) {
            showErrorMsg("Error while sending the ride start info", status);
        }
    });
}

document.querySelectorAll('.otp-input').forEach((element, index, array) => {
    element.addEventListener('input', function (event) {
        let inputValue = event.target.value;
        inputValue = inputValue.replace(/[^0-9]/g, '');
        inputValue = inputValue.slice(0, 1);
        event.target.value = inputValue;
        if (inputValue !== '') {
            if (index < array.length - 1) {
                array[index + 1].focus();
            }
        } else {
            if (index > 0) {
                array[index - 1].focus();
            }
        }
    });

    element.addEventListener('blur', function () {
        if (element.value === '' && index > 0) {
            array[index - 1].focus();
        }
    });
});


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
    }

    stars.forEach((star, index) => {
        star.addEventListener('click', () => {
            rating = (rating === index + 1) ? index + 0.5 : index + 1;
            updateRating(rating);
        });
    });

    window.updateRating = updateRating;
});


document.addEventListener('DOMContentLoaded', function () {
    const stars = document.querySelectorAll('.give-star');
    const ratingText = document.getElementById('rating-text-conclude');
    let rating = 5;

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


$(document).ready(function () {
    $('.pay-option').click(function () {
        $('.pay-option').removeClass('rating-active');
        $(this).addClass('rating-active');
        if ($(this).hasClass('pay-with-wallet')) {
            $('#payment-field').show();
        } else {
            $('#payment-field').hide();
        }
    });
});
$('#rating-modal-form-id').validate({
    rules: {
        feedback: {
            maxlength: 200
        }
    }, messages: {
        feedback: {
            maxlength: "Feedback cannot exceed 200 characters."
        }
    }, submitHandler: function (form) {
        // Fetch form data
        let feedback = $('#feedback').val();
        let payMethod = $('.pay-option.rating-active').text().trim();
        let ratting = $('.long-rattings').text().trim();
        let ratingValue = ratting.match(/[\d.]+/)[0];
        let tripId = $("#general-tripdetails-id").val();

        let data = {
            tripId: tripId, feedback: feedback, payMethod: payMethod, rattings: ratingValue
        };

        $.ajax({
            type: "POST",
            url: "/UrbanRides/rider/ride-ratting-submit",
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function (response) {
                $('#rating-modal').modal('hide');
                setTimeout(function () {
                    location.reload();
                }, 3000);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                var responseText = jqXHR.responseText;

                try {
                    responseText = JSON.parse(responseText);
                } catch (e) {
                    showErrorMsg(jqXHR.responseText);
                    return;
                }

                if (typeof responseText === 'object' && !Array.isArray(responseText)) {
                    var messages = Object.values(responseText).join(', ');
                    showErrorMsg(messages);
                } else {
                    showErrorMsg("An unexpected error occurred: " + jqXHR.responseText);
                }
            }
        });
    }
});
