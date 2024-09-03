document.getElementById('back-button').addEventListener('click', function () {
    history.go(-1);
});


function showInputField() {
    var inputContainer = document.getElementById('inputContainer');
    inputContainer.style.display = 'flex';
}

function addMoney() {
    var depositAmount = parseFloat(document.getElementById('depositAmount').value);
    if (isNaN(depositAmount) || depositAmount <= 10 || depositAmount > 50000) {
        showErrorMsg('Please enter a valid amount between 10 to 5000.');
        return;
    }
    $.ajax({
        url: 'rider-update-amount',
        method: 'POST',
        data: {amount: depositAmount},
        contentType: 'application/x-www-form-urlencoded',
        dataType: 'json',
        success: function (response) {
            var walletBalance = document.querySelector('.wallet-balance');
            var currentBalance = parseFloat(walletBalance.textContent.replace('Balance: ₹ ', ''));
            var newBalance = currentBalance + depositAmount;
            walletBalance.textContent = 'Balance: ₹ ' + newBalance.toFixed(2);
            document.getElementById('depositAmount').value = '';
            var inputContainer = document.getElementById('inputContainer');
            inputContainer.style.display = 'none';
        },
        error: function (xhr, textStatus, errorThrown) {
            console.error('Error sending amount to backend:', xhr, textStatus, errorThrown);
            var errorMessage = 'Failed to add money. Please try again later.';
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            showErrorMsg(errorMessage);
        }
    });
}


$(document).ready(function () {
    $.ajax({
        url: 'rider-transaction-details',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            populateTransactionDetails(data);
        },
        error: function (xhr, textStatus, errorThrown) {
            console.error('Error fetching transaction details: possible there is no data', xhr, textStatus, errorThrown);
            var container1 = $('#transaction-container1');
            var container2 = $('#transaction-container2');
            container1.empty();
            container2.empty();
            container1.append('<div class="no-records">You have no record paid by Cash</div>');
            container2.append('<div class="no-records">You have no record paid by Wallet</div>');


        }
    });
});


function populateTransactionDetails(data) {
    var container1 = $('#transaction-container1');
    var container2 = $('#transaction-container2');
    container1.empty();
    container2.empty();

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
        container2.append('<div class="no-records">You have no record paid by wallet.</div>');
    }
}


function getContextPath() {
    return window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
}
