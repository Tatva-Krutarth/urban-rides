document.getElementById('back-button').addEventListener('click', function () {
    history.go(-1);
});

function toggleAccordion(event, collapseId) {
    event.stopPropagation();
    const collapseElement = document.getElementById(collapseId);
    const bsCollapse = new bootstrap.Collapse(collapseElement, {
        toggle: true
    });
}

$(document).ready(function () {
    loadTripDetails();

    $(document).on('show.bs.modal', '#concludeModalRentTaxi, #concludeModalDailyPickup', function (event) {
        const button = $(event.relatedTarget);
        const tripId = button.data('trip-id');
        const vehicleName = button.data('vehicle-name');
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
                number: true,
                min: 5,
                max: 5000,
                digits: true
            }
        },
        messages: {
            paymentMethodRentTaxi: {
                required: "Please select a payment method."
            },
            distanceRentTaxi: {
                required: "Please enter the distance.",
                number: "Please enter a valid number.",
                min: "The distance must be at least 5 km.",
                max: "The distance cannot exceed 5000 km.",
                digits: "Please enter a valid whole number."
            }
        },

        submitHandler: function (form) {
            const tripId = $('#modalTripIdRentTaxi').val();
            const vehicleName = $('#vehicleNameInModal').val();
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

                    loadTripDetails();
                },
                error: function (xhr, textStatus, errorThrown) {
                    $(".loader").hide();

                    try {
                        var responseText = xhr.responseJSON.errors;
                        showErrorMsg(responseText);
                    } catch (e) {
                        showErrorMsg("Error while concluding the request");
                    }
                }

            });
        }
    });

    $("#concludeFormDailyPickup").validate({
        rules: {
            conclusionNoteDailyPickup: {
                required: false,
                maxlength: 80
            },
        },
        messages: {
            conclusionNoteDailyPickup: {
                maxlength: "Conclude notes cannot exceed 80 characters in length."
            },
        },


        submitHandler: function (form) {
            const tripId = $('#modalTripIdDailyPickup').val();
            const conclusionNote = $('#conclusionNoteDailyPickup').val();
            const paymentMethod = $('#paymentMethodDailyPickup').val();
            $(".loader").css("display", "flex");

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
                    $(".loader").hide();

                    loadTripDetails();
                },
                error: function (xhr, textStatus, errorThrown) {
                    $(".loader").hide();
                    try {
                        var responseText = xhr.responseJSON.errors;
                        showErrorMsg(responseText);
                    } catch (e) {
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
            populateTheData(data);
        },
        error: function (xhr, textStatus, errorThrown) {
            showErrorMsg('Failed to fetch trip details. Please try again later.');
        }
    });
}

function populateTheData(data) {
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

    const distanceSection = trip.distance ? `<div><span>Distance: -</span><span>${trip.distance}</span></div>` : '';
    const dailyPickupDaysSection = trip.dailyPickUpDays ? `<div><span>Daily pickup days: -</span><span>${trip.dailyPickUpDays}</span></div>` : '';
    const imageSrc = trip.serviceTypeId === 2 ? '/UrbanRides/resources/images/taxi-rent-car.png' : '/UrbanRides/resources/images/taxi-car-route.png';
    const specialInstructionSection = trip.specialInstruction
        ? `<div><span>Special Instruction: -</span><span>${trip.specialInstruction}</span></div>`
        : '';

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
                    <img src="${imageSrc}" id="noti-img">
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
                            <div><span>Number of passengers: -</span><span>${trip.numberOfPassengers}</span></div>
                            ${specialInstructionSection}
                        </div>
                        <div class="right-part">
                            <div><span>Trip Id: -</span><span>${trip.tripId}</span></div>
                            <div><span>Number of Days: -</span><span>${trip.numberOfDays}</span></div>
                            <div><span>Vehicle Name: -</span><span>${trip.vehicleName}</span></div>
                            <div><span>Charges: -</span><span>${trip.charges} Rs</span></div>
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
            loadTripDetails();
        },
        error: function (xhr, textStatus, errorThrown) {
            $(".loader").hide();
            console.log(xhr)
            try {
                var responseText = xhr.responseJSON.errors;
                showErrorMsg(responseText);
            } catch (e) {
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
            chargeFactor = 0;
    }

    const charge = Math.round(distance * chargeFactor);
    $('#chargesRentTaxi').val(charge);
}
