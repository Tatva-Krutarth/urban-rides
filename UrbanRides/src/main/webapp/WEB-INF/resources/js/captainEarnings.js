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
        return;
    }
    if (!isNaN(depositAmount) && depositAmount > 0 && depositAmount <= 5000) {
        $.ajax({
            url: 'captain-update-amount',
            method: 'POST',
            data: {amount: depositAmount},
            contentType: 'application/x-www-form-urlencoded',
            dataType: 'json',

            success: function (response) {
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
            showErrorMsg('Failed to fetch transaction details. Please try again later.');
            var container1 = $('#transaction-container1');
            var container2 = $('#transaction-container2');
            container1.empty();
            container2.empty();
            container1.append('<div class="no-records">Failed to fetch transaction details Or there is no data</div>');
            container2.append('<div class="no-records">Failed to fetch transaction details Or there is no data</div>');


        }
    });
});


function populateTransactionDetails(data) {

    var container1 = $('#transaction-container1');
    var container2 = $('#transaction-container2');
    container1.empty();
    container2.empty();
    if (data.length === 0) {
        container1.append('<div class="no-records">No record paid by Cash</div>');
        container2.append('<div class="no-records">No record paid by Wallet.</div>');
        return;
    }
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
            container1.append(html);
            hasMethod1 = true;
        } else {
            container2.append(html);
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
