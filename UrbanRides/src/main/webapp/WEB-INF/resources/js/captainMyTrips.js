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
    $.ajax({
        url: 'captain-my-trip-details',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            populateTransactionDetails(data);
        },
        error: function (xhr, textStatus, errorThrown) {
            showErrorMsg('Failed to fetch trip details. Please try again later.');
        }
    });

    function populateTransactionDetails(data) {
        var accordionExample = $('#accordionExample');
        accordionExample.empty();

        if (data.length === 0) {
            accordionExample.append('<div class="no-records">No trip history found.</div>');
            return;
        }
        data.forEach(function (trip, index) {
            var serviceTypeText = getServiceTypeText(trip.serviceTypeId);
            var notiImgSrc = getNotificationImage(trip.serviceTypeId);
            var statusText, statusColor;
            switch (trip.status) {
                case 1:
                    statusText = 'Completed';
                    statusColor = 'green';
                    break;
                case 2:
                    statusText = 'Cancelled';
                    statusColor = 'red';
                    break;
                case 3:
                    statusText = 'Pending';
                    statusColor = 'gray';
                    break;
                case 5:
                    statusText = 'Completed';
                    statusColor = 'green';
                    break;
                default:
                    statusText = 'Running';
                    statusColor = 'gray';
                    break;
            }

            var tripCard = `
                <div class="card p-0 mt-2 mb-2">
                    <div class="card-header noti-container p-0" id="heading${index}">
                        <div class="noti-img-cont">
                            <img src="${notiImgSrc}" id="noti-img">
                        </div>
                        <div class="my-trip-accor-details-cont mt-2 mb-3">
                            <div class="service-type-text">Service Type: <span class="my-trip-serviceType">${serviceTypeText}</span></div>
                            <hr class="hr-accor">
                            <div class="my-trip-accor-details">
                                <div class="my-trip-accor-details-resp-cont">
                                    <span class="my-trip-accor-details-resp">Pick up location : </span><span class="my-trip-pickup"> ${trip.pickUpLocation}</span>
                                </div>
                                <div class="my-trip-accor-details-resp-cont">
                                    <span class="my-trip-accor-details-resp">Drop off location : </span><span class="my-trip-pickup"> ${trip.dropOffLocation}</span>
                                </div>
                            </div>
                        </div>
                        <div class="my-trip-time">
                            ${trip.tripDate}
                        </div>
                        <div class="my-trip-status">
                            Status: <span class="my-trip-status-value" style="color: ${statusColor};">${statusText}</span>
                        </div>
                        <button class="accordion-button" onclick="toggleAccordion(event, 'collapse${index}')">
                            View More
                        </button>
                    </div>

                    <div id="collapse${index}" class="collapse" aria-labelledby="heading${index}" data-parent="#accordionExample">
                        <div class="card-body p-0">
                            ${trip.serviceTypeId === 30 ? `
                                <div class="waiting-for-captain p-2">
                                    Waiting for the captain to accept the ride.
                                </div>
                            ` : `
                                ${trip.isCaptainDetails !== 2 ? `
                                    <div class="captain-info-nav">
                                        <div class="captain-info-profile">
                                            <img src="${trip.captainProfilePath}" alt="">
                                        </div>
                                        <div class="captain-info-discription">
                                            <div class="ratings-star">
                                                <div class="captain-org-name">
                                                    You have rated ${trip.captainName}
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                ` : ''}
                                 ${trip.serviceTypeId == 1 ? `
                                          <div class="trip-details-bottom-cont">
                                            <div class="left-part">
                                              ${trip.distance ? `
                                                <div>
                                                  <span>Distance: </span>
                                                  <span>${trip.distance}</span>
                                                </div>` : ''}
                                              <div>
                                                <span>Charges: </span>
                                                <span>${trip.charges} rs</span>
                                              </div>
                                            </div>
                                            <div class="right-part">
                                              <div>
                                                <span>Duration: </span>
                                                <span>${trip.duration}</span>
                                              </div>
                                              <div>
                                                <span>Trip Id: </span>
                                                <span>${trip.tripId}</span>
                                              </div>
                                            </div>
                                          </div>
                                        ` : ''}

                                    ${trip.serviceTypeId !== 1 || trip.serviceTypeId == 2 ? `
                                        <div class="trip-details-bottom-cont">
                                            <div class="left-part">
                                                <div>
                                                    <span>Pickup Date: </span>
                                                    <span>${trip.pickupDate}</span>
                                                </div>
                                                <div>
                                                    <span>Trip Id: </span>
                                                    <span>${trip.tripId}</span>
                                                </div>
                                                <div>
                                                    <span>Pickup Time: </span>
                                                    <span>${trip.pickupTime}</span>
                                                </div>
                                                ${trip.dailyPickUpDays ? `
                                                <div>
                                                    <span>Daily Pickup Days: </span>
                                                    <span>${trip.dailyPickUpDays}</span>
                                                </div>
                                                ` : ''}
                                            </div>
                                            <div class="right-part">
                                                <div>
                                                    <span>Number of Passengers: </span>
                                                    <span>${trip.numberOfPassengers}</span>
                                                </div>
                                                <div>
                                                    <span>Number of Days: </span>
                                                    <span>${trip.numberOfDays}</span>
                                                </div>
                                            </div>
                                        </div>
                                    ` : ''}
                                ${trip.serviceTypeId == 1 && trip.cancellationReason ? `
                                    <div class="cancelation-reason p-2">
                                        Cancellation Reason: ${trip.cancellationReason}
                                    </div>
                                ` : ''}
                                ${trip.serviceTypeId !== 1 ? `
                                    <div class="cancelation-reason p-2">
                                        Special Instruction: ${trip.specialInstruction ? trip.specialInstruction : 'N/A'}
                                    </div>
                                ` : ''}
                            `}
                        </div>
                    </div>
                </div>
            `;
            accordionExample.append(tripCard);
        });
    }

    function getServiceTypeText(serviceType) {
        switch (serviceType) {
            case 2:
                return 'Rent a Taxi';
            case 3:
                return 'Daily Pickup';
            default:
                return 'Taxi Booking';
        }
    }

    function getNotificationImage(serviceType) {
        switch (serviceType) {
            case 2:
                return '/UrbanRides/resources/images/taxi-rent-car.png';
            case 3:
                return '/UrbanRides/resources/images/taxi-car-route.png';
            default:
                return '/UrbanRides/resources/images/taxi-general-booking.svg';
        }
    }


});