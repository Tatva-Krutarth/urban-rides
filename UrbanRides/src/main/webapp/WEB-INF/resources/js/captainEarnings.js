document.getElementById('back-button').addEventListener('click', function () {
    history.go(-1); /* move back in history on click */
});


function showInputField() {
    var inputContainer = document.getElementById('inputContainer');
    inputContainer.style.display = 'flex';
}

function withdrawMoney() {
    var depositAmount = parseFloat(document.getElementById('depositAmount').value);
    if (isNaN(depositAmount) || depositAmount < 10 || depositAmount > 50000) {
        showErrorMsg('Please enter a valid amount between 10 to 5000.');
        return; // Exit the function if the input is invalid
    }
    // Validations
    if (!isNaN(depositAmount) && depositAmount > 0 && depositAmount <= 5000) {
        // AJAX request to backend
        $.ajax({
            url: 'captain-update-amount', // Replace with your actual endpoint
            method: 'POST',
            data: {amount: depositAmount}, // Send amount as a simple object
            contentType: 'application/x-www-form-urlencoded', // Adjust content type as per backend expectation
            dataType: 'json', // Expect JSON response from backend

            success: function (response) {
                // Handle successful response
                console.log('Amount successfully sent to backend:', response);
                // Check if response is a number (indicating success)
                var walletBalance = document.querySelector('.wallet-balance');
                var currentBalance = parseFloat(walletBalance.textContent.replace('Balance: ₹ ', ''));
                var newBalance = currentBalance - depositAmount;
                walletBalance.textContent = 'Balance: ₹ ' + newBalance.toFixed(2);
                document.getElementById('depositAmount').value = '';
                showSuccesstMsg('Money withdrawn successfully.');
                var inputContainer = document.getElementById('inputContainer');
                inputContainer.style.display = 'none';
            },
            error: function (xhr, textStatus, errorThrown) {
                console.error('Error updating transaction details:', xhr, textStatus, errorThrown);
                let errorMessage = "An error occurred while updating transaction details.";

                if (xhr.responseJSON && xhr.responseJSON.error) {
                    errorMessage = xhr.responseJSON.error;
                } else if (xhr.responseText) {
                    try {
                        let jsonResponse = JSON.parse(xhr.responseText);
                        if (jsonResponse.message) {
                            errorMessage = jsonResponse.message;
                        }
                    } catch (e) {
                        errorMessage = xhr.responseText;
                    }
                }
                $(".loader").hide();
                showErrorMsg(errorMessage);
            }
        });
    } else {
        showErrorMsg('Please enter a valid amount up to 5000.');
    }
}


$(document).ready(function () {
    $.ajax({
        url: 'captain-transaction-details',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            populateTransactionDetails(data);
        },
        error: function (xhr, textStatus, errorThrown) {
            console.error('Error fetching transaction details:', xhr, textStatus, errorThrown);
            showErrorMsg('Failed to fetch transaction details. Please try again later.');
            var container1 = $('#transaction-container1'); // Adjust the selector to match your container
            var container2 = $('#transaction-container2'); // Adjust the selector to match your container
            container1.empty(); // Clear any existing content
            container2.empty(); // Clear any existing content
            container1.append('<div class="no-records">Failed to fetch transaction details Or there is no data</div>');
            container2.append('<div class="no-records">Failed to fetch transaction details Or there is no data</div>');


        }
    });
});


function populateTransactionDetails(data) {
    var container1 = $('#transaction-container1'); // Adjust the selector to match your container
    var container2 = $('#transaction-container2'); // Adjust the selector to match your container
    container1.empty(); // Clear any existing content
    container2.empty(); // Clear any existing content

    var hasMethod1 = false;
    var hasOtherMethods = false;

    data.forEach(function (transaction) {
        var imgSrc = transaction.paymentMethod == 1 ? getContextPath() + '/resources/images/cash.svg' : getContextPath() + '/resources/images/wallet-white.svg';

        var html = '<div class="noti-container mt-2 mb-2">' +
            '<div class="noti-img-cont">' +
            '<img src="' + imgSrc + '" />' +
            '</div>' +
            '<div class="noti-righ-cont">' +
            '<div class="noti-header">' + transaction.walletHeader + '</div>' +
            '<div class="noti-time">' + transaction.dateAndTime + '</div>' +
            '</div>' +
            '<div class="amount-paid"> Rs.' + transaction.paidAmount + '</div>' +
            '</div>';

        if (transaction.paymentMethod == 1) {
            container1.append(html); // Append to container1 for payment method 1
            hasMethod1 = true;
        } else {
            container2.append(html); // Append to container2 for other payment methods
            hasOtherMethods = true;
        }
    });

    if (!hasMethod1) {
        container1.append('<div class="no-records">You have no record paid by Cash.</div>');
    }
    if (!hasOtherMethods) {
        container2.append('<div class="no-records">You have no record paid by Wallet.</div>');
    }
}


function getContextPath() {
    return window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
}
