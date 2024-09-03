const emailInput = document.getElementById('email');
const passInput = document.getElementById('pass');
const confPassInput = document.getElementById('confPass');
const otpInput = document.querySelector('input[name="otp"]');
const emailError = document.getElementById('emailError');
const passError = document.getElementById('passError');
const confPassError = document.getElementById('confPassError');
const otpError = document.getElementById('otpError');
const submitBtn = document.getElementById('submitBtn');
const getOtpBtn = document.querySelector(".get-otp-btn");

submitBtn.disabled = true;
otpInput.disabled = true;
otpInput.parentElement.style.display = 'none';
getOtpBtn.disabled = true;

emailInput.addEventListener('input', validateEmail);
passInput.addEventListener('input', validatePass);
confPassInput.addEventListener('input', validateConfPass);
getOtpBtn.addEventListener('click', enableOtp);
otpInput.addEventListener('input', validateOtp);
confPassInput.addEventListener('input', updateConfPass);

var i = 0;
var j = 0;

function validateEmail() {
    const email = emailInput.value;
    const isValidEmail = email.length > 0 && validateEmailFormat(email);

    if (!isValidEmail) {
        emailError.style.display = 'initial';
        j = 0;
    } else {
        emailError.style.display = 'none';
        j = 1;
    }

    updateOtpButton();
}

function validatePass() {
    const pass = passInput.value;
    const isValidPassLength = pass.length >= 8 && pass.length <= 16;
    const isStrongPass = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,16}$/.test(pass);

    if (!isValidPassLength) {
        passError.innerHTML = 'Password must be between 8 and 16 characters.';
        passError.style.display = 'initial';
        i = 0;
    } else if (!isStrongPass) {
        passError.innerHTML = 'Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character.';
        passError.style.display = 'initial';
        i = 0;
    } else {
        passError.style.display = 'none';
        i = 1;
    }

    updateConfPass();
}

function validateConfPass() {
    const confPass = confPassInput.value;
    const pass = passInput.value;

    if (confPass !== pass) {
        confPassError.style.display = 'initial';
    } else {
        confPassError.style.display = 'none';
    }

    updateOtpButton();
}

function enableOtp() {

}

function validateOtp() {
    const otp = otpInput.value;
    const isValidOtp = /^\d{4}$/.test(otp);

    if (!isValidOtp) {
        otpError.style.display = 'initial';
    } else {
        otpError.style.display = 'none';
    }

    submitBtn.disabled = !isValidOtp;
}

function validateEmailFormat(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(String(email).toLowerCase());
}

function updateConfPass() {
    const pass = passInput.value;
    const confPass = confPassInput.value;

    if (pass !== confPass) {
        confPassError.style.display = 'initial';
    } else {
        confPassError.style.display = 'none';
    }
    updateOtpButton();
}

function updateOtpButton() {
    const isValidEmail = j == 1 || emailError.style.display === 'none';
    const isValidPassLength = i == 1;
    const isValidConfPass = confPassError.style.display === 'none';

    if (isValidEmail && isValidPassLength && isValidConfPass) {
        otpInput.parentElement.style.display = 'flex';
        getOtpBtn.disabled = false;
    } else {
        otpInput.parentElement.style.display = 'none';
        getOtpBtn.disabled = true;
    }
}


function getOtp() {
    $(".loader").css("display", "flex");
    const pass = passInput.value;
    const confPasss = confPassInput.value;
    if (pass !== confPasss) {
        confPassError.style.display = 'initial';
    } else {
        confPassError.style.display = 'none';
    }
    const placeHolderElements = document.getElementsByClassName('place-holder');
    for (let i = 0; i < placeHolderElements.length; i++) {
        placeHolderElements[i].style.display = 'none';
    }

    event.preventDefault();

    var email = $("#email").val();
    var password = $("#pass").val();
    var confPass = $("#confPass").val();

    var formData = {
        email: email, password: password, confPass: confPass,
    };
    $.ajax({
        type: "POST",
        url: "../user-registration-otp",
        contentType: 'application/json',
        dataType: 'text',
        data: JSON.stringify(formData),
        success: function (response) {
            $(".loader").hide();
            if (typeof response === 'string') {
                if (response === "Email Send Successfully") {
                    showSuccesstMsg(response);
                    document.getElementById('get-otp-btnn').disabled = true;
                    otpInput.disabled = false;
                    emailInput.readOnly = true;
                    passInput.readOnly = true;
                    confPassInput.readOnly = true;

                } else {
                    showErrorMsg(response);
                }
            } else if (response.hasOwnProperty("errors")) {
                let errorMessages = response.errors.join('<br/>');
                showErrorMsg(errorMessages);
            }
        },
        error: function (xhr, textStatus, errorThrown) {
            $(".loader").hide();
            try {
                const errorResponse = JSON.parse(xhr.responseText);
                showErrorMsg(errorResponse);
            } catch (e) {
                showErrorMsg(xhr.responseText);
            }
        }
    });
    setTimeout(function () {
        document.getElementById('get-otp-btnn').disabled = false;
    }, 10000);
    const otpButton1 = document.getElementById('get-otp-btnn');
    otpButton1.addEventListener('click', function (event) {
        if (otpButton1.disabled) {
            event.preventDefault();
            alert('Please wait for 1 minute to get a new OTP.');
        }
    });

}

const togglePassword = document.querySelector('#togglePassword');

const password = document.querySelector('#confPass');

togglePassword.addEventListener('click', function (e) {

    const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
    password.setAttribute('type', type);

    if (togglePassword.src.match("../resources/images/password-eye-slash.svg")) {
        togglePassword.src = "../resources/images/password-eye.svg";
    } else {
        togglePassword.src = "../resources/images/password-eye-slash.svg";
    }
});
const togglePassword1 = document.querySelector('#togglePassword2');

const password1 = document.querySelector('#pass');

togglePassword1.addEventListener('click', function (e) {

    const type = password1.getAttribute('type') === 'password' ? 'text' : 'password';
    password1.setAttribute('type', type);

    if (togglePassword1.src.match("../resources/images/password-eye-slash.svg")) {
        togglePassword1.src = "../resources/images/password-eye.svg";
    } else {
        togglePassword1.src = "../resources/images/password-eye-slash.svg";
    }
});

$(document).ready(function () {
    $("#submitBtn").click(function (event) {
        event.preventDefault();
        var email = $("#email").val();
        var password = $("#pass").val();
        var otp = $("#otp").val();
        var confPass = $("#confPass").val();
        var accountTypeId = parseInt($("#accountType").val());
        var formData = {
            email: email, password: password, otp: otp, confPass: confPass, acccoutTypeId: accountTypeId,
        };

        $(".loader").css("display", "flex");

        $.ajax({
            type: "POST",
            url: "../user-registration-submit",
            data: JSON.stringify(formData),
            contentType: 'application/json',
            dataType: 'text',
            success: function (response) {
                if (typeof response === 'string') {
                    if (response === "Rider Registered" || response === "Captain Registered") {
                        $(".loader").hide();

                        showSuccesstMsg(response);
                        disableAllElements();
                        setTimeout(function () {
                            const redirectUrl = response === "Rider Registered" ? "../rider/rider-personal-details" : "../captain/captain-personal-details";
                            window.location.href = redirectUrl;
                        }, 3000);
                    } else {
                        showErrorMsg(response);
                        $(".loader").hide();
                    }
                } else {
                    showErrorMsg(response);
                    $(".loader").hide();
                }
            },
            error: function (error) {
                $(".loader").hide();
                try {
                    const errorResponse = JSON.parse(xhr.responseText);
                    if (Array.isArray(errorResponse.errors)) {
                        const errorMessage = errorResponse.errors[0];
                        showErrorMsg(errorMessage);
                    } else {
                        showErrorMsg(errorResponse);
                    }
                } catch (e) {
                    if (typeof xhr.responseText === 'string') {
                        showErrorMsg(xhr.responseText);
                    } else {
                        showErrorMsg(xhr.responseText);
                    }
                }
            }
        });
    });
});

document.getElementById('back-button').addEventListener('click', function () {
    history.go(-1);
});

function disableAllElements() {
    var buttons = document.querySelectorAll('button, input[type="button"], input[type="submit"]');
    buttons.forEach(function (button) {
        button.disabled = true;
    });

    var links = document.querySelectorAll('a');
    links.forEach(function (link) {
        link.style.pointerEvents = 'none';
        link.style.color = 'gray';
        link.removeAttribute('href');
    });
}
