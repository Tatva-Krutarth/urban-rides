const divElements = document.querySelectorAll('#ad-dash-support-types > div');

divElements.forEach(div => {
    div.addEventListener('click', function () {
        divElements.forEach(d => {
            d.classList.remove('user-active');
        });

        this.classList.add('user-active');
    });
});
document.addEventListener('DOMContentLoaded', () => {
    reattachAccordionEventListeners();
});
$(document).ready(function () {
    userManAll();
});

function reattachAccordionEventListeners() {
    const accordionBtns = document.querySelectorAll('.accordion-btn');
    accordionBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            const targetId = btn.getAttribute('data-bs-target');
            const accordionContent = document.querySelector(targetId);
            accordionContent.classList.toggle('show');
            accordionBtns.forEach(otherBtn => {
                if (otherBtn !== btn) {
                    const otherTargetId = otherBtn.getAttribute('data-bs-target');
                    const otherAccordionContent = document.querySelector(otherTargetId);

                    otherAccordionContent.classList.remove('show');
                    otherBtn.textContent = 'View';
                }
            });
        });
    });
}

document.addEventListener('DOMContentLoaded', () => {
    reattachAccordionEventListeners();
    attachUnblockEventListeners();
    attachBlockEventListeners();
});


function userManAll() {
    $(".loader").css("display", "flex");

    $.ajax({
        url: 'admin-user-all-data',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            console.log(data)
            $(".loader").hide();

            populateAllRequests(data);
        },
        error: function (xhr, textStatus, errorThrown) {
            $(".loader").hide();

            showErrorMsg("Error while fetching the query data");
        }
    });
}

function populateAllRequests(data) {
    const tbody = document.querySelector('.table tbody');
    tbody.innerHTML = ''; // Clear the existing rows
    if (data && data.length > 0) {

        data.forEach((user, index) => {
            const row = document.createElement('tr');

            row.innerHTML = `
            <td class="align-middle text-center">${getAccountType(user.accountType)}</td>
            <td class="align-middle text-center">${user.userName}</td>
            <td class="align-middle text-center">${user.email}</td>
            <td class="align-middle text-center">${user.phone}</td>
            <td class="align-middle text-center">${getStatusBadge(user.status, user.riderUserId)}</td>
            <td class="align-middle text-center">
                <button class="btn user-manage-view-mote-btn accordion-btn" data-bs-toggle="collapse" data-bs-target="#details${index}" aria-expanded="false" aria-controls="details${index}">View</button>
            </td>
        `;

            const detailsRow = document.createElement('tr');
            detailsRow.id = `details${index}`;
            detailsRow.classList.add('collapse', 'accordion-content');

            detailsRow.innerHTML = `
            <td colspan="6">
                <div class="accordion-inner">
                    <p>Total Rides: ${user.totalNumberofRides}</p>
                    <p>Success Rides: ${user.totalSuccestrip}</p>
                    <p>Failed Rides: ${user.totalFailedTrip}</p>
                    <p>Rider User ID: ${user.riderUserId}</p>
                </div>
            </td>
        `;

            tbody.appendChild(row);
            tbody.appendChild(detailsRow);
        });

        // Reattach event listeners for the accordion buttons and block buttons
        reattachAccordionEventListeners();
        attachBlockEventListeners();
    } else {
        const noDataRow = document.createElement('tr');
        noDataRow.innerHTML = `
            <td colspan="6" class="text-center">No data found</td>
        `;
        tbody.appendChild(noDataRow);
    }
}

function getAccountType(accountType) {
    switch (accountType) {
        case 1:
            return 'Admin';
        case 2:
            return 'Captain';
        case 3:
            return 'Rider';
        default:
            return 'Unknown';
    }
}

function getStatusBadge(status, riderUserId) {
    switch (status) {
        case 1:
            return '<div class="unblock-btn">Pending</div>';
        case 2:
            return '<div class="unblock-btn">Pending</div>';
        case 3:
            return '<div class="unverified-btn">Unverified</div>';
        case 4:
            return '<div class="verificatoin-failed">Vefication Failed</div>';
        case 5:
            return `<div class="block-btn" data-user-id="${riderUserId}">Block</div>`;
        case 6:
            return `<div class="unblock-btn" data-user-id="${riderUserId}">Unblock</div>`;
        case 7:
            return '<div class="unknow">Unknown</div>';
        default:
            return 'Unknown';
    }
}

function attachBlockEventListeners() {
    const blockButtons = document.querySelectorAll('.block-btn');

    blockButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const riderUserId = btn.getAttribute('data-user-id');
            $(".loader").css("display", "flex");
            // Send AJAX request to block the user
            $.ajax({
                url: 'admin-block-user',
                method: 'POST',
                data: {riderUserId: riderUserId},
                success: function (response) {
                    // Remove the row from the table
                    const row = btn.closest('tr');
                    row.nextElementSibling.remove(); // Remove details row
                    row.remove(); // Remove main row
                    $(".loader").hide();

                    // Optionally, show a success message
                    showSuccesstMsg("User blocked successfully.");
                },
                error: function (xhr, textStatus, errorThrown) {
                    $(".loader").hide();

                    showErrorMsg("Error while blocking the user.");
                }
            });
        });
    });
}

function attachUnblockEventListeners() {
    const unblockButtons = document.querySelectorAll('.unblock-btn');

    unblockButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const riderUserId = btn.getAttribute('data-user-id');

            $(".loader").css("display", "flex");
            $.ajax({
                url: 'admin-unblock-user',
                method: 'POST',
                data: {riderUserId: riderUserId},
                success: function (response) {
                    const row = document.querySelector(`.unblock-btn[data-user-id="${riderUserId}"]`).closest('tr');
                    row.remove();
                    $(".loader").hide();
                },
                error: function (xhr, textStatus, errorThrown) {
                    $(".loader").hide();
                    showErrorMsg("Error occurred while processing the action.");
                }
            });
        });
    });
}

// ------------------------------------------------------------------------------------------------------
function userManRider() {
    $.ajax({
        url: 'admin-user-rider-data',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            populateRiderRequests(data);
        },
        error: function (xhr, textStatus, errorThrown) {
            showErrorMsg("Error while fetching the query data");
        }
    });
}

function populateRiderRequests(data) {
    const tbody = document.querySelector('.table tbody');
    tbody.innerHTML = '';
    if (data && data.length > 0) {
        data.forEach((user, index) => {
            const row = document.createElement('tr');
            row.innerHTML = `
            <td class="align-middle text-center">${getAccountType(user.accountType)}</td>
            <td class="align-middle text-center">${user.userName}</td>
            <td class="align-middle text-center">${user.email}</td>
            <td class="align-middle text-center">${user.phone}</td>
            <td class="align-middle text-center">${getStatusBadge(user.status, user.riderUserId)}</td>
            <td class="align-middle text-center">
                <button class="btn user-manage-view-mote-btn accordion-btn" data-bs-toggle="collapse" data-bs-target="#details${index}" aria-expanded="false" aria-controls="details${index}">View</button>
            </td>
        `;
            const detailsRow = document.createElement('tr');
            detailsRow.id = `details${index}`;
            detailsRow.classList.add('collapse', 'accordion-content');
            detailsRow.innerHTML = `
            <td colspan="6">
                <div class="accordion-inner">
                    <p>Total Rides: ${user.totalNumberofRides}</p>
                    <p>Success Rides: ${user.totalSuccestrip}</p>
                    <p>Failed Rides: ${user.totalFailedTrip}</p>
                    <p>Rider User ID: ${user.riderUserId}</p>
                </div>
            </td>
        `;

            tbody.appendChild(row);
            tbody.appendChild(detailsRow);
        });

        reattachAccordionEventListeners();
        attachBlockEventListeners();
    } else {
        const noDataRow = document.createElement('tr');
        noDataRow.innerHTML = `
            <td colspan="6" class="text-center">No data found</td>
        `;
        tbody.appendChild(noDataRow);
    }
}


// -----------------------------------------------------------------------------------captain---------------------------------------------------
function userManCaptain() {
    $.ajax({
        url: 'admin-user-captain-data',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            populateCaptainRequests(data);
        },
        error: function (xhr, textStatus, errorThrown) {
            showErrorMsg("Error while fetching the query data");
        }
    });
}

function populateCaptainRequests(data) {
    const tbody = document.querySelector('.table tbody');
    tbody.innerHTML = ''; // Clear the existing rows
    if (data && data.length > 0) {
        data.forEach((user, index) => {
            const row = document.createElement('tr');
            row.innerHTML = `
            <td class="align-middle text-center">${getAccountType(user.accountType)}</td>
            <td class="align-middle text-center">${user.userName}</td>
            <td class="align-middle text-center">${user.email}</td>
            <td class="align-middle text-center">${user.phone}</td>
            <td class="align-middle text-center">${getStatusBadge(user.status, user.riderUserId)}</td>
            <td class="align-middle text-center">
                <button class="btn user-manage-view-mote-btn accordion-btn" data-bs-toggle="collapse" data-bs-target="#details${index}" aria-expanded="false" aria-controls="details${index}">View</button>
            </td>
        `;

            const detailsRow = document.createElement('tr');
            detailsRow.id = `details${index}`;
            detailsRow.classList.add('collapse', 'accordion-content');

            detailsRow.innerHTML = `
            <td colspan="6">
                <div class="accordion-inner">
                    <p>Total Rides: ${user.totalNumberofRides}</p>
                    <p>Success Rides: ${user.totalSuccestrip}</p>
                    <p>Failed Rides: ${user.totalFailedTrip}</p>
                    <p>Rider User ID: ${user.riderUserId}</p>
                </div>
            </td>
        `;

            tbody.appendChild(row);
            tbody.appendChild(detailsRow);
        });

        reattachAccordionEventListeners();
        attachBlockEventListeners();
    } else {
        const noDataRow = document.createElement('tr');
        noDataRow.innerHTML = `
            <td colspan="6" class="text-center">No data found</td>
        `;
        tbody.appendChild(noDataRow);
    }
}

function userManBlocked() {
    $.ajax({
        url: 'admin-user-block-data',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            populateBlockRequests(data);
        },
        error: function (xhr, textStatus, errorThrown) {
            showErrorMsg("Error while fetching the query data");
        }
    });
}

function populateBlockRequests(data) {
    const tbody = document.querySelector('.table tbody');
    tbody.innerHTML = '';
    if (data && data.length > 0) {

        data.forEach((user, index) => {
            const row = document.createElement('tr');
            row.innerHTML = `
            <td class="align-middle text-center">${getAccountType(user.accountType)}</td>
            <td class="align-middle text-center">${user.userName}</td>
            <td class="align-middle text-center">${user.email}</td>
            <td class="align-middle text-center">${user.phone}</td>
            <td class="align-middle text-center">${getStatusBadge(user.status, user.riderUserId)}</td>
            <td class="align-middle text-center">
                <button class="btn user-manage-view-mote-btn accordion-btn" data-bs-toggle="collapse" data-bs-target="#details${index}" aria-expanded="false" aria-controls="details${index}">View</button>
            </td>
        `;

            const detailsRow = document.createElement('tr');
            detailsRow.id = `details${index}`;
            detailsRow.classList.add('collapse', 'accordion-content');

            detailsRow.innerHTML = `
            <td colspan="6">
                <div class="accordion-inner">
                    <p>Total Rides: ${user.totalNumberofRides}</p>
                    <p>Success Rides: ${user.totalSuccestrip}</p>
                    <p>Failed Rides: ${user.totalFailedTrip}</p>
                    <p>Rider User ID: ${user.riderUserId}</p>
                </div>
            </td>
        `;
            tbody.appendChild(row);
            tbody.appendChild(detailsRow);
        });
        reattachAccordionEventListeners();
        attachBlockEventListeners();
        attachUnblockEventListeners();
    } else {
        const noDataRow = document.createElement('tr');
        noDataRow.innerHTML = `
            <td colspan="6" class="text-center">No data found</td>
        `;
        tbody.appendChild(noDataRow);
    }
}

