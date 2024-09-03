    $("#myForm").validate({
    rules: {
        riderFirstName: {
            required: true,
            minlength: 1,
            maxlength: 10,
            lettersOnly: true
        },
        riderLastName: {
            required: true,
            minlength: 1,
            maxlength: 10,
            lettersOnly: true
        },
        phone: {
            required: true,
            digits: true,
            minlength: 10,
            maxlength: 10
        },
        age: {
            required: true,
            digits: true,
            min: 18,
            max: 130,
        },
        termsCheckbox: {
            required: true
        }
    },
    messages: {
        riderFirstName: {
            required: "Please enter your first name",
            minlength: "Please enter at least one character",
            maxlength: "The length of the last name must be below 10"

        },
        riderLastName: {
            required: "Please enter your last name",
            minlength: "Please enter at least one character",
            maxlength: "The length of the last name must be below 10"
        },
        phone: {
            required: "Please enter a phone number",
            digits: "Phone number must contain only digits",
            minlength: "Phone number must be at least 10 digits long",
            maxlength: "Phone number must be no longer than 10 digits"
        },
        age: {
            required: "Please enter your age",
            min: "Age must be 18 or above",
            digits: "Age cannot contain characters",
            max: "Age cannot be greater than 130",

        },
        termsCheckbox: {
            required: "Please accept the terms and conditions"
        }
    },
    errorElement: "span",
    errorClass: "error",
    submitHandler: function (form) {
        var formData = $(form).serializeArray();
        var jsonData = {};

        $.each(formData, function (index, element) {
            jsonData[element.name] = element.value;
        });
        $(".loader").css("display", "flex");

        $.ajax({
            url: form.action,
            method: form.method,
            data: JSON.stringify(jsonData),
            contentType: 'application/json',
            dataType: 'text',
            success: function (response) {
                if (typeof response === 'string') {
                    if (response.startsWith("Login")) {
                        showSuccesstMsg(response);
                        setTimeout(function () {
                            $(".loader").hide();
                            window.location.href = "/UrbanRides/admin/admin-dashboard";
                        }, 3000);
                    } else {
                        showErrorMsg(response);
                    }
                    $(".loader").hide();

                } else {
                    showErrorMsg(response);
                }
                $(".loader").hide();

            },
            error: function (xhr, textStatus, errorThrown) {
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
    }
});

jQuery.validator.addMethod("lettersOnly", function (value, element) {
    return this.optional(element) || /^[a-zA-Z]+$/.test(value);
}, "Please enter only letters");

document.getElementById('back-button').addEventListener('click', function () {
    history.go(-1);
});
