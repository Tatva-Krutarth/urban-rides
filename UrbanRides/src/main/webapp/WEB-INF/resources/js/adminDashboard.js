$('#ad-dash-support-types div').on('click', function () {
    if (!$(this).hasClass('querry-active')) {
        $(this).addClass('querry-active');
        $(this).siblings().removeClass('querry-active');
    }
});
$(document).ready(function () {
    $.ajax({
        url: 'admin-count-data',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            console.log(data);
            $('#counter1').attr('data-target', data.totalUserCount);
            $('#counter2').attr('data-target', data.generalBooking);
            $('#counter3').attr('data-target', data.serviceBooking);
            $('#counter4').attr('data-target', data.totalSuccessBooking);

            const counters = document.querySelectorAll('.counter');
            const duration = 2000; // Duration of animation in milliseconds
            const frameRate = 1000 / 60; // Roughly 60 frames per second

            counters.forEach(counter => {
                const target = +counter.getAttribute('data-target');
                const updateCounter = () => {
                    const current = +counter.innerText;
                    const increment = target / (duration / frameRate);

                    if (current < target) {
                        counter.innerText = Math.ceil(current + increment);
                        setTimeout(updateCounter, frameRate);
                    } else {
                        counter.innerText = target;
                    }
                };

                updateCounter();
            });
        },
        error: function (xhr, textStatus, errorThrown) {
            showErrorMsg("Error while fetching the count data");
        }
    });
});

$(document).ready(function () {
    // Fetch initial request data
    allRequestData(0, 10);
});


$('#ad-dash-support-types #all').on('click', function () {
    currentState = 'allRequests';
    allRequestData(0, 10); // Initial page and size parameters
});
let currentState = 'allRequests'; // Default state

function allRequestData(page, size) {
    $.ajax({
        url: 'query-data',
        method: 'GET',
        dataType: 'json',
        data: {
            page: page,
            size: size
        },
        success: function (data) {
            console.log(data);

            // Call populateAllRequests function with the retrieved data
            populateAllRequests(data.content);

            // Update the pagination controls
            updatePaginationControls(page, data.totalPages);
        },
        error: function (xhr, textStatus, errorThrown) {
            showErrorMsg("Error while fetching the query data");
        }
    });
}

function updatePaginationControls(currentPage, totalPages) {
    const paginationCont = $('#pagination-cont .pagination-class');
    paginationCont.empty();

    paginationCont.append('<button class="previous">Previous</button>');

    // Calculate the range of pages to display
    let startPage = Math.max(0, currentPage - 2);
    let endPage = Math.min(totalPages - 1, currentPage + 2);

    // Adjust the range if there are fewer than 5 pages
    if (endPage - startPage + 1 < 5) {
        if (startPage === 0) {
            endPage = Math.min(totalPages - 1, 4);
        } else {
            startPage = Math.max(0, totalPages - 5);
        }
    }

    for (let i = startPage; i <= endPage; i++) {
        const activeClass = i === currentPage ? ' active' : '';
        paginationCont.append(`<button class="number${activeClass}" data-page="${i}">${i + 1}</button>`);
    }

    paginationCont.append('<button class="next">Next</button>');
    console.log(currentState)
    // Add click event listeners to pagination buttons
    $('.pagination-class .number').on('click', function () {
        const page = parseInt($(this).data('page'));
        if (currentState === 'runningQueries') {
            runningQuerries(page, 10);
        } else if (currentState === 'completedQueries') {
            completedQuerries(page, 10);
        } else if (currentState === 'allRequests') {
            allRequestData(page, 10);
        }
    });

    $('.pagination-class .previous').on('click', function () {
        if (currentPage > 0) {
            if (currentState === 'runningQueries') {
                runningQuerries(currentPage - 1, 10);
            } else if (currentState === 'completedQueries') {
                completedQuerries(currentPage - 1, 10);
            } else if (currentState === 'allRequests') {
                allRequestData(currentPage - 1, 10);
            }
        }
    });

    $('.pagination-class .next').on('click', function () {
        if (currentPage < totalPages - 1) {
            if (currentState === 'runningQueries') {
                runningQuerries(currentPage + 1, 10);
            } else if (currentState === 'completedQueries') {
                completedQuerries(currentPage + 1, 10);
            } else if (currentState === 'allRequests') {
                allRequestData(currentPage + 1, 10);
            }
        }
    });
}


function populateAllRequests(data) {
    // Select the container where notifications will be appended
    var container = $('.parent-container');

    // Clear existing content in the container (if needed)
    container.empty();

    // Check if data is empty and display a message if necessary
    if (data.length === 0) {
        container.append('<p>No requests available.</p>');
        return;
    }

    // Iterate through each item in 'data' array
    $.each(data, function (index, item) {
        // Determine image source based on item.accountType
        var imgSrc = item.accountType === 'Captain' ? '/UrbanRides/resources/images/wallet-white.svg' : '/UrbanRides/resources/images/cash.svg';

        // Construct HTML for each notification item
        var notification = `
            <div class="noti-container mt-2 mb-2">
                <div class="noti-img-cont">
                    <img src="${imgSrc}">
                </div>
                <div class="noti-righ-cont">
                    <div class="noti-header">
                        Query raised by ${item.userName} 
                    </div>
                    <hr class="hr-in-admin-dash">
                    <div class="user-id">
                        <span>User id :-</span>
                        <span>${item.userID}</span>
                    </div>
                    <div class="contact-detail">
                        <span>Contact details :-</span>
                        <span>${item.contactDetails}</span>
                    </div>
                    <div class="querry-type">
                        <span>Support type :-</span>
                        <span>${item.sypportType}</span> <!-- Adjust key based on actual data -->
                    </div>
                    <div class="accoutn-type">
                        <span>Account type :-</span>
                        <span>${item.accountType}</span>
                    </div>
                    <div class="noti-msg">
                        <span>Message :-</span>
                        ${item.message}
                    </div>
                    <div class="noti-time mt-2">
                       ${item.createdDate}
                    </div>
                </div>
                <div class="amount-paid">
                    <button onclick="acceptRequest('${item.supportId}', this)">
                        Accept
                    </button>
                </div>
                </div>
            </div>
        `;

        // Append the constructed HTML to the container
        container.append(notification);
    });
}

function acceptRequest(requestId, button) {
    $(".loader").css("display", "flex");
    $.ajax({
        url: 'admin-accept-request',
        method: 'POST',
        data: {id: requestId},
        success: function (response) {
            console.log(response);
            // Show success message
            showSuccesstMsg("You have accepted the request");

            // Remove the accepted notification from the UI
            $(button).closest('.noti-container').remove();

            // Check if there are remaining notifications on the page
            if ($('.noti-container').length === 0) {
                // Navigate to the previous page or the first page if current page is 0
                const currentPage = $('.pagination-class .number.active').data('page');
                const previousPage = currentPage > 0 ? currentPage - 1 : 0;
                allRequestData(previousPage, 10);
            } else {
                // Refresh current page's data
                const currentPage = $('.pagination-class .number.active').data('page');
                allRequestData(currentPage, 10);
            }
            $(".loader").hide();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(jqXHR);
            var responseText = jqXHR.responseText;

            // Attempt to parse the response text as JSON
            try {
                responseText = JSON.parse(responseText);
            } catch (e) {
                // Handle cases where response is not valid JSON
                showErrorMsg(jqXHR.responseText);
                return;
            }

            // Check if the response is a map with keys and values
            if (typeof responseText === 'object' && !Array.isArray(responseText)) {
                var messages = Object.values(responseText).join(', ');
                showErrorMsg(messages);
            } else {
                // Handle generic or unexpected errors
                showErrorMsg("An unexpected error occurred: " + jqXHR.responseText);
            }
        }
    });
}


// Event handler for running queries button
$('#ad-dash-support-types #running').on('click', function () {
    runningQuerries(0, 10); // Initial page and size parameters
     currentState = 'runningQueries'; // Default state

});

function runningQuerries(page, size) {
    $.ajax({
        url: 'admin-running-querry-data',
        method: 'GET',
        dataType: 'json',
        data: {
            page: page,
            size: size
        },
        success: function (data) {
            console.log(data);

            // Call populateRunningRequests function with the retrieved data
            populateRunningRequests(data.content);

            // Update the pagination controls
            updatePaginationControls(page, data.totalPages);
        },
        error: function (xhr, textStatus, errorThrown) {
            showErrorMsg("Error while fetching the running queries data");
        }
    });
}
function populateRunningRequests(data) {
    var container = $('.parent-container');

    // Clear existing content in the container
    container.empty();

    // Check if data is empty and display a message if necessary
    if (data.length === 0) {
        container.append('<p>No running queries available.</p>');
        return;
    }

    // Iterate through each item in 'data' array
    $.each(data, function (index, item) {
        var imgSrc = item.accountType === 'Captain' ? '/UrbanRides/resources/images/wallet-white.svg' : '/UrbanRides/resources/images/cash.svg';

        // Determine if there's a document to view
        var viewDocumentLink = '';
        if (!item.fileAvailable) {
            viewDocumentLink = `
                <div class="view-document mt-2 mb-3">
                 <span class="doc-heading">Document :- </span>   <a href="${item.fileLocaton}" target="_blank" id="document-id">View Document</a>
                </div>
            `;
        }

        // Construct HTML for each notification item
        var notification = `
            <div class="noti-container mt-2 mb-2">
                <div class="noti-img-cont">
                    <img src="${imgSrc}">
                </div>
                <div class="noti-righ-cont">
                    <div class="noti-header">
                        Query raised by ${item.userName}
                    </div>
                    <hr class="hr-in-admin-dash">
                    <div class="user-id">
                        <span>User id :-</span>
                        <span>${item.userID}</span>
                    </div>
                    <div class="contact-detail">
                        <span>Contact details :-</span>
                        <span>${item.contactDetails}</span>
                    </div>
                    <div class="querry-type">
                        <span>Support type :-</span>
                        <span>${item.supportType}</span>
                    </div>
                    <div class="accoutn-type">
                        <span>Account type :-</span>
                        <span>${item.accountType}</span>
                    </div>
                    <div class="noti-msg">
                        <span>Message :-</span>
                        ${item.message}
                    </div>
                                        ${viewDocumentLink} <!-- Add view document link if available -->

                    <div class="noti-time mt-2">
                       ${item.createdDate}
                    </div>
                </div>
                <div class="amount-paid">
                    <button onclick="completeRequest('${item.supportId}', this)">
                        Conclude
                    </button>
                </div>
            </div>
        `;

        // Append the constructed HTML to the container
        container.append(notification);
    });
}



function completeRequest(requestId, button) {
    $(".loader").css("display", "flex");

    $.ajax({
        url: 'admin-complete-request',
        method: 'POST',
        data: {id: requestId},
        success: function (response) {
            console.log(response);
            // Remove the accepted notification from the UI
            showSuccesstMsg("Request Concluded Succesfully");

            $(button).closest('.noti-container').remove();
            $(".loader").hide();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(jqXHR);
            $(".loader").hide();
            var responseText = jqXHR.responseText;

            // Attempt to parse the response text as JSON
            try {
                responseText = JSON.parse(responseText);
            } catch (e) {
                // Handle cases where response is not valid JSON
                showErrorMsg(jqXHR.responseText);
                return;
            }

            // Check if the response is a map with keys and values
            if (typeof responseText === 'object' && !Array.isArray(responseText)) {
                var messages = Object.values(responseText).join(', ');
                showErrorMsg(messages);
            } else {
                // Handle generic or unexpected errors
                showErrorMsg("An unexpected error occurred: " + jqXHR.responseText);
            }
        }
    });
}

// Event handler for completed queries button
$('#ad-dash-support-types #completed').on('click', function () {
    completedQuerries(0, 10); // Initial page and size parameters
    currentState = 'completedQueries'; // Default state

});

function completedQuerries(page, size) {
    $.ajax({
        url: 'admin-completed-querry-data',
        method: 'GET',
        dataType: 'json',
        data: {
            page: page,
            size: size
        },
        success: function (data) {
            console.log(data);

            // Call populateCompletedNotifications function with the retrieved data
            populateCompletedNotifications(data.content);

            // Update the pagination controls
            updatePaginationControls(page, data.totalPages);
        },
        error: function (xhr, textStatus, errorThrown) {
            showErrorMsg("Error while fetching the completed queries data");
        }
    });
}

function populateCompletedNotifications(data) {
    // Select the container where notifications will be appended
    var container = $('.parent-container');

    // Clear existing content in the container (if needed)
    container.empty();

    // Check if data is empty and display a message if necessary
    if (data.length === 0) {
        container.append('<p>No completed queries available.</p>');
        return;
    }

    // Iterate through each item in 'data' array
    $.each(data, function (index, item) {
        // Determine image source based on item.accountType
        var imgSrc = '';
        if (item.accountType === 'Captain') {
            imgSrc = '/UrbanRides/resources/images/wallet-white.svg'; // Adjust path as needed
        } else {
            imgSrc = '/UrbanRides/resources/images/cash.svg'; // Adjust path as needed
        }

        // Construct HTML for each notification item
        var notification = `
            <div class="noti-container mt-2 mb-2">
                <div class="noti-img-cont">
                    <img src="${imgSrc}">
                </div>
                <div class="noti-righ-cont">
                    <div class="noti-header">
                        Query raised by ${item.userName} 
                    </div>
                    <hr class="hr-in-admin-dash">
                    <div class="user-id">
                        <span>User id :-</span>
                        <span>${item.userID}</span>
                    </div>
                    <div class="contact-detail">
                        <span>Contact details :-</span>
                        <span>${item.contactDetails}</span>
                    </div>
                    <div class="querry-type">
                        <span>Support type :-</span>
                        <span>${item.sypportType}</span> <!-- Adjust key based on actual data -->
                    </div>
                    <div class="accoutn-type">
                        <span>Account type :-</span>
                        <span>${item.accountType}</span>
                    </div>
                    <div class="noti-msg">
                        <span>Message :-</span>
                        ${item.message}
                    </div>
                    <div class="noti-msg">
                        <span>Completed by :-</span>
                        ${item.adminName}
                    </div>
                    <div class="noti-time mt-2" style="margin-right: 20px">
                       ${item.createdDate}
                    </div> 
                </div>
            </div>
        `;

        // Append the constructed HTML to the container
        container.append(notification);
    });
}
