document.getElementById('back-button').addEventListener('click', function () {
    history.go(-1); /* move back in history on click */
});

function toggleAccordion(event, collapseId) {
    event.stopPropagation(); // Prevent default button behavior
    const collapseElement = document.getElementById(collapseId);
    const bsCollapse = new bootstrap.Collapse(collapseElement, {
        toggle: true
    });
}

$(document).ready(function () {
    loadTripDetails(); // Initial load

    // Event listener for modals based on service type
    $(document).on('show.bs.modal', '#concludeModalRentTaxi, #concludeModalDailyPickup', function (event) {
        const button = $(event.relatedTarget); // Button that triggered the modal
        const tripId = button.data('trip-id'); // Extract trip ID from data attribute
        const vehicleName = button.data('vehicle-name'); // Extract vehicle name from data attribute

        // Store trip ID and vehicle name in modal for use on form submission
        if ($(this).attr('id') === 'concludeModalRentTaxi') {
            $('#modalTripIdRentTaxi').val(tripId);
            $('#vehicleNameInModal').val(vehicleName);
        } else {
            $('#modalTripIdDailyPickup').val(tripId);
            $('#vehicleNameInModal').val(vehicleName);
        }
    });

    $("#concludeFormRentTaxi").validate({
        rules: {

            paymentMethodRentTaxi: {
                required: true
            },
            distanceRentTaxi: {
                required: true,
                number: true
            }
        },
        messages: {

            paymentMethodRentTaxi: {
                required: "Please select a payment method."
            },
            distanceRentTaxi: {
                required: "Please enter the distance.",
                number: "Please enter a valid number."
            }
        },
        submitHandler: function (form) {
            const tripId = $('#modalTripIdRentTaxi').val();
            const vehicleName = $('#vehicleNameInModal').val();
            console.log(vehicleName)
            const conclusionNote = $('#conclusionNoteRentTaxi').val();
            const charges = $('#chargesRentTaxi').val();
            const paymentMethod = $('#paymentMethodRentTaxi').val();
            const distance = $('#distanceRentTaxi').val();
            $(".loader").css("display", "flex");

            $.ajax({
                url: 'conclude-ride-rent-taxi',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    tripId: tripId,
                    conclusionNote: conclusionNote,
                    charges: charges,
                    paymentMethod: paymentMethod,
                    distance: distance
                }),
                success: function (response) {
                    $('#concludeModalRentTaxi').modal('hide');
                    $('.modal-backdrop').remove();
                    showSuccesstMsg("Rent a taxi ride concluded successfully")
                    form.reset();
                    $(".loader").hide();

                    loadTripDetails(); // Reload data after concluding
                },
                error: function (xhr, textStatus, errorThrown) {
                    console.log(xhr);
                    $(".loader").hide();

                    // Attempt to parse the response as JSON
                    try {
                        var responseText = xhr.responseJSON.errors; // Access the first error message
                        showErrorMsg(responseText);
                    } catch (e) {
                        // Handle cases where response is not valid JSON
                        showErrorMsg("Error while concluding the request");
                    }
                }

            });
        }
    });

    $("#concludeFormDailyPickup").validate({
        // rules: {
        //     conclusionNoteDailyPickup: {
        //         required: true
        //     }
        // },
        // messages: {
        //     conclusionNoteDailyPickup: {
        //         required: "Please enter a conclusion note."
        //     }
        // },
        submitHandler: function (form) {
            const tripId = $('#modalTripIdDailyPickup').val();
            const conclusionNote = $('#conclusionNoteDailyPickup').val();
            const paymentMethod = $('#paymentMethodDailyPickup').val();

            $.ajax({
                url: 'conclude-ride-daily-pickup',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    tripId: tripId,
                    conclusionNote: conclusionNote,
                    paymentMethod: paymentMethod
                }),
                success: function (response) {
                    $('#concludeModalDailyPickup').modal('hide');
                    $('.modal-backdrop').remove();
                    showSuccesstMsg("Daily pick up ride concluded successfully")
                    form.reset();
                    loadTripDetails(); // Reload data after concluding
                },
                error: function (xhr, textStatus, errorThrown) {
                    console.log(xhr);
                    $(".loader").hide();

                    // Attempt to parse the response as JSON
                    try {
                        var responseText = xhr.responseJSON.errors; // Access the first error message
                        showErrorMsg(responseText);
                    } catch (e) {
                        // Handle cases where response is not valid JSON
                        showErrorMsg("Error while concluding the request");
                    }
                }
            });
        }
    });
});

function loadTripDetails() {
    $.ajax({
        url: 'captain-package-trip-details',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            console.log(data);
            populateTheData(data);
        },
        error: function (xhr, textStatus, errorThrown) {
            console.error('Error fetching trip details:', xhr, textStatus, errorThrown);
            showErrorMsg('Failed to fetch trip details. Please try again later.');
        }
    });
}

function populateTheData(data) {
    // Clear existing content
    $('#rentATaxiAccor').empty();
    $('#packageServiceAccor').empty();
    $('#currentRunning').empty();

    let hasActiveService = false;

    data.forEach(trip => {
        const tripElement = createTripElement(trip);
        if (trip.isTripLive === 1) {
            $('#currentRunning').append(tripElement);
            hasActiveService = true;
        } else if (trip.serviceTypeId === 2) {
            $('#rentATaxiAccor').append(tripElement);
        } else {
            $('#packageServiceAccor').append(tripElement);
        }
    });

    if (!$('#rentATaxiAccor').children().length) {
        $('#rentATaxiAccor').append('<p>No active service request</p>');
    }

    if (!$('#packageServiceAccor').children().length) {
        $('#packageServiceAccor').append('<p>No active service request</p>');
    }

    if (!hasActiveService) {
        $('#currentRunning').append('<p>You have no active requests</p>');
    }
}

function createTripElement(trip) {
    const serviceType = trip.serviceTypeId === 2 ? 'Rent a Taxi' : 'Daily Pickup';
    const pickDropLocation = trip.serviceTypeId === 2
        ? `<div class="my-trip-accor-details-resp-cont">
                <span class="my-trip-accor-details-resp">Pick up - Drop off point: &nbsp;</span>
                <span class="my-trip-pickup">${trip.pickUpLocation}</span>
           </div>`
        : `<div class="my-trip-accor-details-resp-cont">
                <span class="my-trip-accor-details-resp">Pick up location: &nbsp;</span>
                <span class="my-trip-pickup">${trip.pickUpLocation}</span>
           </div>
           <div class="my-trip-accor-details-resp-cont">
                <span class="my-trip-accor-details-resp">Drop off location: &nbsp;</span>
                <span class="my-trip-pickup">${trip.dropOffLocation}</span>
           </div>`;

    const distanceSection = trip.distance ? `<div><span>Distance: -</span><span>${trip.distance} Km</span></div>` : '';
    const dailyPickupDaysSection = trip.dailyPickUpDays ? `<div><span>Daily pickup days: -</span><span>${trip.dailyPickUpDays}</span></div>` : '';

    const button = trip.isTripLive === 1
        ? `<button type="button" class="captain-conclude-btn mt-2 mb-2" 
        data-bs-toggle="modal" 
        data-bs-target="${trip.serviceTypeId === 2 ? '#concludeModalRentTaxi' : '#concludeModalDailyPickup'}" 
        data-trip-id="${trip.tripId}" 
        data-vehicle-name="${trip.vehicleName}">
    Conclude
</button>`
        : `<button type="button" class="captain-accept-btn mt-2 mb-2" onclick="acceptRide(${trip.tripId})">Accept Ride</button>`;

    return `
        <div class="card p-0 mt-2 mb-2" data-trip-id="${trip.tripId}">
            <div class="card-header noti-container p-0" id="heading${trip.tripId}">
                <div class="noti-img-cont">
                    <img src="/UrbanRides/resources/images/taxi-rent-car.png" id="noti-img">
                </div>
                <div class="my-trip-accor-details-cont mt-2 mb-3">
                    <div class="my-trip-accor-details">
                        <div class="my-trip-accor-details-resp-cont">
                            <span class="my-trip-accor-details-resp">Service Type: &nbsp;</span>
                            <span class="my-trip-pickup">${serviceType}</span>
                        </div>
                        ${pickDropLocation}
                    </div>
                </div>
                <div class="my-trip-time">
                    ${trip.tripDate}
                </div>
                <button class="accordion-button" onclick="toggleAccordion(event, 'collapse${trip.tripId}')">
                    View More
                </button>
            </div>
            <div id="collapse${trip.tripId}" class="collapse" aria-labelledby="heading${trip.tripId}" data-parent="#${trip.serviceTypeId === 2 ? 'rentATaxiAccor' : 'packageServiceAccor'}">
                <div class="card-body p-0">
                    <div class="trip-details-bottom-cont">
                        <div class="left-part">
                            ${distanceSection}
                            <div><span>Emergency Contact: -</span><span>${trip.emergencyContact}</span></div>
                            <div><span>Pickup Date: -</span><span>${trip.pickupDate}</span></div>
                            <div><span>Pickup Time: -</span><span>${trip.pickupTime}</span></div>
                            <div><span>Charges: -</span><span>${trip.charges} Rs</span></div>
                            <div><span>Number of passengers: -</span><span>${trip.numberOfPassengers}</span></div>
                        </div>
                        <div class="right-part">
                            <div><span>Trip Id: -</span><span>${trip.tripId}</span></div>
                            <div><span>Number of Days: -</span><span>${trip.numberOfDays}</span></div>
                            <div><span>Vehicle Name: -</span><span>${trip.vehicleName}</span></div>
                            <div><span>Special Instruction: -</span><span>${trip.specialInstruction}</span></div>
                            ${dailyPickupDaysSection}
                        </div>
                    </div>
                    ${button}
                </div>
            </div>
        </div>
    `;
}


function acceptRide(tripId) {
    $(".loader").css("display", "flex");

    $.ajax({
        url: 'accept-package-ride',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({tripId: tripId}),
        success: function (response) {
            showSuccesstMsg(response.message);
            $(".loader").hide();

            loadTripDetails(); // Refresh data after accepting ride
        },
        error: function (xhr, textStatus, errorThrown) {
            console.log(xhr);
            $(".loader").hide();

            $(".loader").hide();

            // Attempt to parse the response as JSON
            try {
                var responseText = xhr.responseJSON.errors; // Access the first error message
                showErrorMsg(responseText);
            } catch (e) {
                // Handle cases where response is not valid JSON
                showErrorMsg("Error while concluding the request");
            }
        }
    });
}

function calPriceByDistance() {
    const distance = parseFloat($('#distanceRentTaxi').val());
    const vehicleName = $('#vehicleNameInModal').val();
    let chargeFactor = 0;

    switch (vehicleName.toLowerCase()) {
        case 'bike':
            chargeFactor = 2;
            break;
        case 'rickshaw':
            chargeFactor = 3;
            break;
        case 'car':
            chargeFactor = 6;
            break;
        case 'luxury car':
            chargeFactor = 10;
            break;
        default:
            console.error('Unknown vehicle type:', vehicleName);
            chargeFactor = 0; // Default to 0 if the vehicle type is unknown
    }

    const charge = Math.round(distance * chargeFactor); // Round off the charge
    $('#chargesRentTaxi').val(charge);
}
