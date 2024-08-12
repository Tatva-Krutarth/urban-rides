function toggleAccordion(event, collapseId) {
    event.stopPropagation(); // Prevent default button behavior
    const collapseElement = document.getElementById(collapseId);
    const bsCollapse = new bootstrap.Collapse(collapseElement, {
        toggle: true
    });
}

document.addEventListener('DOMContentLoaded', () => {
    // Attach event listener to filter inputs
    document.getElementById('trip-id').addEventListener('input', applyFilters);
    document.getElementById('service-type').addEventListener('change', applyFilters);
    document.getElementById('trip-status').addEventListener('change', applyFilters);
});

$(document).ready(function () {
    applyFilters();
});

function applyFilters() {
    const tripId = document.getElementById('trip-id').value;
    const serviceType = document.getElementById('service-type').value;
    const tripStatus = document.getElementById('trip-status').value;
    var payload = {}
    payload["tripCode"] = tripId
    payload["serviceType"] = serviceType
    payload["tripStatus"] = tripStatus
    // Send the filter values to the backend via AJAX
    $(".loader").css("display", "flex");

    $.ajax({
        url: 'admin-rides-filter-trips', // Update this with your actual backend endpoint
        method: 'POST', contentType: 'application/json', data: JSON.stringify(payload), success: function (response) {
            // Update the UI with the filtered data
            populateTrips(response);
            $(".loader").hide();

        }, error: function (xhr, textStatus, errorThrown) {
            showErrorMsg("Error while processing the filter data")
            $(".loader").hide();

        }
    });
}

function populateTrips(data) {
    const accordion = document.getElementById('accordionExample');
    accordion.innerHTML = ''; // Clear existing trip cards
    if (data && data.length > 0) {
        data.forEach((trip, index) => {
            const card = document.createElement('div');
            card.className = 'card p-0 mt-2 mb-2';

            const statusText = getStatusText(trip.status);
            const statusColor = getStatusColor(trip.status);
            const notiImgSrc = getNotificationImage(trip.serviceTypeId);
            const captainName = trip.captainName ? trip.captainName : '--';
            const distance = trip.captainName ? trip.distance : '--';
            const duration = trip.captainName ? trip.duration : '--';
            card.innerHTML = `
            <div class="card-header noti-container p-0" id="heading${index}">
                <div class="noti-img-cont">
                    <img src="${notiImgSrc}" id="noti-img">
                </div>
                <div class="my-trip-accor-details-cont mt-2 mb-3">
                    <div class="service-type-text">Service Type :- <span class="my-trip-serviceType">${getServiceTypeText(trip.serviceTypeId)}</span></div>
                    <hr class="hr-accor">
                    <div class="my-trip-accor-details">
                        <div class="my-trip-accor-details-resp-cont">
                            <span class="my-trip-accor-details-resp">Captain Name : &nbsp; </span><span class="my-trip-pickup">${captainName}</span>
                        </div>
                        <div class="my-trip-accor-details-resp-cont">
                            <span class="my-trip-accor-details-resp">Rider Name : &nbsp; </span><span class="my-trip-pickup">${trip.riderName}</span>
                        </div>
                    </div>
                </div>
                <div class="my-trip-time">${trip.tripDate}</div>
                <div class="my-trip-status">
                    Status : <span class="my-trip-status-value" style="color: ${statusColor};">${statusText}</span>
                </div>
                <button id="accordion-button-${index}" class="accordion-button-btn" onclick="toggleAccordion(event, 'collapse${index}')">
                    View More
                </button>
            </div>

            <div id="collapse${index}" class="collapse" aria-labelledby="heading${index}" data-parent="#accordionExample">
                <div class="card-body p-0">
                    <div class="mt-2">
                        <div class="pickup-dropp">
                            <span class="my-trip-accor-details-resp">Pick up location : &nbsp; </span><span class="my-trip-pickup">${trip.pickUpLocation}</span>
                        </div>
                        <div class="pickup-dropp">
                            <span class="my-trip-accor-details-resp">Drop off location : &nbsp;</span><span class="my-trip-pickup">${trip.dropOffLocation}</span>
                        </div>
                    </div>
                    <div class="trip-details-bottom-cont">
                        <div class="left-part">
                            <div>
                                <span>Distance : -</span>
                                <span>${distance} Km</span>
                            </div>
                            <div>
                                <span>Charges  :-</span>
                                <span>${trip.charges} rs</span>
                            </div>
                        </div>
                        <div class="right-part">
                            <div>
                                <span>Duration  :-</span>
                                <span>${duration} Mins</span>
                            </div>
                            <div>
                                <span>Trip Id  :-</span>
                                <span>${trip.tripId}</span>
                            </div>
                        </div>
                    </div>
                    <div class="cancelation-reason">Cancellation Reason :- ${trip.cancellationReason}</div>
                </div>
            </div>
        `;

            accordion.appendChild(card);
        });
    } else {
        accordion.innerHTML = '<div class="no-data-found" style="font-width: 500;">No Record found</div>';
    }
}

function getServiceTypeText(serviceType) {
    switch (serviceType) {
        case 1:
            return 'General booking';
        case 2:
            return 'Rent a taxi';
        case 3:
            return 'Daily pick up';
        default:
            return 'Unknown';
    }
}

function getStatusText(status) {
    switch (status) {
        case 1:
            return 'Pending';
        case 2:
            return 'Expired';
        case 3:
            return 'Cancelled';
        case 4:
            return 'Running';
        case 5:
            return 'Completed';
        default:
            return 'Unknown';
    }
}

function getStatusColor(status) {
    switch (status) {
        case 1:
            return 'gray';
        case 2:
            return 'red';
        case 3:
            return 'red';
        case 4:
            return 'gray';
        case 5:
            return 'green';
        default:
            return 'black';
    }
}

function getNotificationImage(serviceType) {
    switch (serviceType) {
        case 1:
            return '/UrbanRides/resources/images/taxi-general-booking.svg';
        case 2:
            return '/UrbanRides/resources/images/taxi-rent-car.png';
        case 3:
            return '/UrbanRides/resources/images/taxi-car-route.png';
        default:
            return '/UrbanRides/resources/images/taxi-rent-car.png';
    }
}
