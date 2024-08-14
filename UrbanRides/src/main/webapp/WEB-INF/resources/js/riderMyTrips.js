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
    $.ajax({
        url: 'rider-my-trip-details',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            if (!data || data.length === 0) {
                var accordionExample = $('#accordionExample');
                accordionExample.empty(); // Clear any existing content

                accordionExample.append(`
            <div>
                <h5>You have no trips yet.</h5>
                <p>It looks like you haven't completed any trips so far. Once you book and complete a ride, your trip details will appear here.</p>
            </div>
        `);
            } else {
                populateTransactionDetails(data);
            }
        },

        error: function (xhr, textStatus, errorThrown) {
            console.error('Error fetching trip details:', xhr, textStatus, errorThrown);
            showErrorMsg('There is no.');
        }
    });

    function populateTransactionDetails(data) {
        var accordionExample = $('#accordionExample');
        accordionExample.empty(); // Clear any existing content

        data.forEach(function (trip, index) {
            var serviceTypeText = getServiceTypeText(trip.serviceTypeId);
            var notiImgSrc = getNotificationImage(trip.serviceTypeId);
            var statusText, statusColor;

            // Determine status text and color
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
                case 4:
                    statusText = 'Expired';
                    statusColor = 'red';
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
                                ${trip.serviceTypeId === 2 ? `
                                    <div class="my-trip-accor-details-resp-cont">
                                        <span class="my-trip-accor-details-resp">Pickup/Dropoff Location : </span><span class="my-trip-pickup"> ${trip.pickUpLocation} - ${trip.dropOffLocation}</span>
                                    </div>
                                ` : `
                                    <div class="my-trip-accor-details-resp-cont">
                                        <span class="my-trip-accor-details-resp">Pick up location : </span><span class="my-trip-pickup"> ${trip.pickUpLocation}</span>
                                    </div>
                                    <div class="my-trip-accor-details-resp-cont">
                                        <span class="my-trip-accor-details-resp">Drop off location : </span><span class="my-trip-pickup"> ${trip.dropOffLocation}</span>
                                    </div>
                                `}
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
                                                <!-- Initialize star rating here based on your logic -->
                                                <!-- Example: <div id="rating-system${index}"></div> -->
                                            </div>
                                        </div>
                                    </div>
                                ` : ''}
                                ${trip.serviceTypeId == 1 ? `
                                    <div class="trip-details-bottom-cont">
                                        <div class="left-part">
                                            <div>
                                                <span>Distance: </span>
                                                <span>${trip.distance}</span>
                                            </div>
                                            <div>
                                                <span>Charges: </span>
                                                <span>${trip.charges} rs</span>
                                            </div>
                                        </div>
                                        <div class="right-part">
                                            ${trip.duration ? `
                                                <div>
                                                    <span>Duration: </span>
                                                    <span>${trip.duration}</span>
                                                </div>
                                            ` : ''}
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
                                                <span>Charges: </span>
                                                <span>${trip.charges}</span>
                                            </div>
                                            <div>
                                                <span>Pickup Time: </span>
                                                <span>${trip.pickupTime}</span>
                                            </div>
                                           
                                        </div>
                                        <div class="right-part">
                                            <div>
                                                <span>DropOff Date: </span>
                                                <span>${trip.dropOffDate}</span>
                                            </div>
                                            <div>
                                                <span>Number of Passengers: </span>
                                                <span>${trip.numberOfPassengers}</span>
                                            </div> 
                                            
                                            <div>
                                                <span>Number of Days: </span>
                                                <span>${trip.numberOfDays}</span>
                                            </div>
                                             ${trip.dailyPickUpDays ? `
                                                <div>
                                                    <span>Daily Pickup Days: </span>
                                                    <span>${trip.dailyPickUpDays}</span>
                                                </div>
                                            ` : ''}
                                        </div>
                                    </div>
                                ` : ''}
                                ${trip.serviceTypeId == 1 ? `
                                    ${trip.cancellationReason ? `
                                        <div class="cancelation-reason p-2">
                                            Cancellation Reason: ${trip.cancellationReason}
                                        </div>
                                    ` : ''}
                                ` : ''}
                                ${trip.serviceTypeId !== 1 ? `
                                    ${trip.specialInstruction ? `
                                        <div class="cancelation-reason p-2">
                                            Special Instruction: ${trip.specialInstruction}
                                        </div>
                                    ` : ''}
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
