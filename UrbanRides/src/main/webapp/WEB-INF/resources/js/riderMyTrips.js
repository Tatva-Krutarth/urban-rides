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
            console.log(data);
            populateTransactionDetails(data);
        },
        error: function (xhr, textStatus, errorThrown) {
            console.error('Error fetching transaction details:', xhr, textStatus, errorThrown);
            showErrorMsg('Failed to fetch trip details. Please try again later.');
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
                case 5:
                    statusText = 'Completed';
                    statusColor = 'green';
                    break;
                default:
                    statusText = 'Running';
                    statusColor = 'yellow';
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
                                    <span class="my-trip-accor-details-resp">Pick up location: </span><span class="my-trip-pickup">${trip.pickUpLocation}</span>
                                </div>
                                <div class="my-trip-accor-details-resp-cont">
                                    <span class="my-trip-accor-details-resp">Drop off location: </span><span class="my-trip-pickup">${trip.dropOffLocation}</span>
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
                            ${trip.serviceTypeId === 2 && (trip.status === 2 || trip.status === 3) ? `
                                <div class="waiting-for-captain">
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
                                                <div id="rating-system${index}" class="d-flex gap-1 give-star">
                                                    ${generateStarRating(trip.captainRatting)}
                                                </div>
                                            </div>
                                        </div>
                                    </div>
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
                                <div class="cancelation-reason">
                                    Cancellation Reason: ${trip.cancellationReason ? trip.cancellationReason : 'N/A'}
                                </div>
                            `}
                        </div>
                    </div>
                </div>
            `;
            accordionExample.append(tripCard);

            // Initialize star ratings for each trip
            if (trip.isCaptainDetails !== 2 && !(trip.serviceTypeId === 2 && (trip.status === 2 || trip.status === 3))) {
                initializeStarRating(`rating-system${index}`, trip.captainRatting);
            }
        });
    }

    function initializeStarRating(containerId, rating) {
        const container = document.getElementById(containerId);
        if (container) {
            const fullStar = `<svg class="star" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/></svg>`;
            const halfStar = `<svg class="star" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z" fill="url(#half-filled-gradient)"/></svg>`;
            const emptyStar = `<svg class="star" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z" fill="gray"/></svg>`;

            let stars = '';
            for (let i = 1; i <= 5; i++) {
                if (i <= rating) {
                    stars += fullStar;
                } else if (i - rating === 0.5) {
                    stars += halfStar;
                } else {
                    stars += emptyStar;
                }
            }
            container.innerHTML = stars;
        }
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

    function generateStarRating(rating) {
        const fullStar = `<svg class="star" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/></svg>`;
        const halfStar = `<svg class="star" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z" fill="url(#half-filled-gradient)"/></svg>`;
        const emptyStar = `<svg class="star" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z" fill="gray"/></svg>`;

        let stars = '';
        for (let i = 1; i <= 5; i++) {
            if (i <= rating) {
                stars += fullStar;
            } else if (i - rating === 0.5) {
                stars += halfStar;
            } else {
                stars += emptyStar;
            }
        }
        return stars;
    }
});
