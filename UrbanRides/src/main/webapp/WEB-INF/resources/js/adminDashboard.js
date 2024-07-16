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
            console.log(data)
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
            showErrorMsg("Error white fetching the count data")
        }
    });
});


$(document).ready(function () {
    $.ajax({
        url: 'admin-querry-data',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            console.log(data)

            // Call populateNotifications function with the retrieved data
            populateNotifications(data);
        },
        error: function (xhr, textStatus, errorThrown) {
            showErrorMsg("Error while fetching the query data");
        }
    });
});

function populateNotifications(data) {
    // Select the container where notifications will be appended
    var container = $('.parent-container');

    // Clear existing content in the container (if needed)
    container.empty();

    // Iterate through each item in 'data' array
    $.each(data, function(index, item) {
        // Determine image source based on item.accountType
        var imgSrc = '';
        if (item.accountType === 'Captain') {
            imgSrc = '/resources/images/wallet-white.svg'; // Adjust path as needed
        } else {
            imgSrc = '/resources/images/cash.svg'; // Adjust path as needed
        }

        // Construct HTML for each notification item
        var notification = `
            <div class="noti-container mt-2 mb-2">
                <div class="noti-img-cont">
                    <img src="${imgSrc}">
                </div>
                <div class="noti-righ-cont">
                    <div class="noti-header">
                        Query raised by ${item.userID} parameter (Not from this planet)
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
                    <button>
                        Acquire
                    </button>
                </div>
            </div>
        `;

        // Append the constructed HTML to the container
        container.append(notification);
    });
}
