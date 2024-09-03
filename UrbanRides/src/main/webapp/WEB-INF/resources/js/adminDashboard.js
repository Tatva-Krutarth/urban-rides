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
            $('#counter1').attr('data-target', data.totalUserCount);
            $('#counter2').attr('data-target', data.generalBooking);
            $('#counter3').attr('data-target', data.serviceBooking);
            $('#counter4').attr('data-target', data.totalSuccessBooking);

            const counters = document.querySelectorAll('.counter');
            const duration = 2000;
            const frameRate = 1000 / 60;

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
    allRequestData(0, 10);
});


$('#ad-dash-support-types #all').on('click', function () {
    currentState = 'allRequests';
    allRequestData(0, 10);
});
let currentState = 'allRequests';

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
            populateAllRequests(data.content);
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

    let startPage = Math.max(0, currentPage - 2);
    let endPage = Math.min(totalPages - 1, currentPage + 2);
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
    var container = $('.parent-container');
    container.empty();
    if (data.length === 0) {
        container.append('<p>No requests available.</p>');
        return;
    }
    $.each(data, function (index, item) {
        var imgSrc = item.accountType === 'Captain' ? '/UrbanRides/resources/images/wallet-white.svg' : '/UrbanRides/resources/images/cash.svg';
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
                        <span>${item.sypportType}</span> 
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
            showSuccesstMsg("You have accepted the request");
            $(button).closest('.noti-container').remove();
            if ($('.noti-container').length === 0) {
                const currentPage = $('.pagination-class .number.active').data('page');
                const previousPage = currentPage > 0 ? currentPage - 1 : 0;
                allRequestData(previousPage, 10);
            } else {
                const currentPage = $('.pagination-class .number.active').data('page');
                allRequestData(currentPage, 10);
            }
            $(".loader").hide();
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


// Event handler for running queries button
$('#ad-dash-support-types #running').on('click', function () {
    runningQuerries(0, 10);
    currentState = 'runningQueries';
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
            populateRunningRequests(data.content);
            updatePaginationControls(page, data.totalPages);
        },
        error: function (xhr, textStatus, errorThrown) {
            showErrorMsg("Error while fetching the running queries data");
        }
    });
}

function populateRunningRequests(data) {
    var container = $('.parent-container');
    container.empty();
    if (data.length === 0) {
        container.append('<p>No running queries available.</p>');
        return;
    }
    $.each(data, function (index, item) {
        var imgSrc = item.accountType === 'Captain' ? '/UrbanRides/resources/images/wallet-white.svg' : '/UrbanRides/resources/images/cash.svg';
        var viewDocumentLink = '';
        if (item.fileAvailable) {
            viewDocumentLink = `
                <div class="view-document mt-2 mb-3">
                 <span class="doc-heading">Document :- </span>   <a href="${item.fileLocaton}" target="_blank" id="document-id">View Document</a>
                </div>
            `;
        }
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
                        <span>${item.sypportType}</span>
                    </div>
                    <div class="accoutn-type">
                        <span>Account type :-</span>
                        <span>${item.accountType}</span>
                    </div>
                    <div class="noti-msg">
                        <span>Message :-</span>
                        ${item.message}
                    </div>
                                        ${viewDocumentLink} 
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
            showSuccesstMsg("Request Concluded Succesfully");
            $(button).closest('.noti-container').remove();
            $(".loader").hide();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $(".loader").hide();
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

$('#ad-dash-support-types #completed').on('click', function () {
    completedQuerries(0, 10);
    currentState = 'completedQueries';
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
            populateCompletedNotifications(data.content);
            updatePaginationControls(page, data.totalPages);
        },
        error: function (xhr, textStatus, errorThrown) {
            showErrorMsg("Error while fetching the completed queries data");
        }
    });
}

function populateCompletedNotifications(data) {
    var container = $('.parent-container');
    container.empty();
    if (data.length === 0) {
        container.append('<p>No completed queries available.</p>');
        return;
    }
    $.each(data, function (index, item) {
        var imgSrc = '';
        if (item.accountType === 'Captain') {
            imgSrc = '/UrbanRides/resources/images/wallet-white.svg';
        } else {
            imgSrc = '/UrbanRides/resources/images/cash.svg';
        }
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
                        <span>${item.sypportType}</span>
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
        container.append(notification);
    });
}
