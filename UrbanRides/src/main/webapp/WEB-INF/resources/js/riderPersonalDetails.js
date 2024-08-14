// Custom validator to ensure two values are not the same

$("#myForm").validate({

    rules: {
        riderFirstName: {
            required: true,
            minlength: 1,
            maxlength: 10,
            lettersOnly: true ,// Assuming you have a custom method for letters only
            notSameValue: '#lName'

        },
        riderLastName: {
            required: true,
            minlength: 1,
            maxlength: 10,
            lettersOnly: true,
            notSameValue: '#fName'
        },
        phone: {
            required: true,
            digits: true,
            minlength: 10,
            maxlength: 10,
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
        // Error messages for each field
        riderFirstName: {
            required: "Please enter your first name",
            minlength: "Please enter at least one character",
            maxlength: "The length of the last name must be below 10",
            notSameValue: "The first name cannot be same as second name"


        },
        riderLastName: {
            required: "Please enter your last name",
            minlength: "Please enter at least one character",
            maxlength: "The length of the last name must be below 10",
            notSameValue: "The second name cannot be same as first name"

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
    // ignore: "#error-ignore",
    errorElement: "span",
    errorClass: "error",
    submitHandler: function (form) {
        var formData = $(form).serializeArray();
        var jsonData = {};

        $.each(formData, function (index, element) {
            jsonData[element.name] = element.value;
        });
        console.log(JSON.stringify(jsonData))
        $(".loader").css("display", "flex");

        $.ajax({
            url: form.action,
            method: form.method,
            data: JSON.stringify(jsonData),
            contentType: 'application/json',
            dataType: 'text',
            success: function (response) {
                // Handle successful response
                if (typeof response === 'string') {
                    if (response.startsWith("Login")) {
                        showSuccesstMsg(response); // trim to remove extra spaces
                        disableAllElements();

                        setTimeout(function () {
                            $(".loader").hide();
                            window.location.href = "/UrbanRides/rider/rider-dashboard";
                        }, 3000); // 3000ms = 3 seconds
                    } else {
                        showErrorMsg(response);
                    }
                    $(".loader").hide();

                } else {
                    showErrorMsg(response);
                }
                console.log("Form submitted successfully:", response);
                $(".loader").hide();

            },
            error: function (xhr, textStatus, errorThrown) {
                console.error("Error:", xhr, textStatus, errorThrown);
                $(".loader").hide();
                try {
                    const errorResponse = JSON.parse(xhr.responseText);
                    if (Array.isArray(errorResponse.errors)) {
                        // handle error response in the format {"errors":["Phone number must be between 10 and 13 characters"]}
                        const errorMessage = errorResponse.errors[0];
                        showErrorMsg(errorMessage);
                        console.log("Backend try:", errorMessage);
                    } else {
                        // handle non-array error response
                        showErrorMsg(errorResponse);
                        console.log("Backend try:", errorResponse);
                    }
                } catch (e) {
                    // Handle non-JSON response
                    if (typeof xhr.responseText === 'string') {
                        // handle string error response
                        showErrorMsg(xhr.responseText);
                        console.log("Backend catch:", xhr.responseText);
                    } else {
                        // handle non-string error response
                        showErrorMsg(xhr.responseText);
                        console.log("Backend catch:", xhr.responseText);
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
    history.go(-1); /* move back in history on click */
});
$.validator.addMethod("notSameValue", function (value, element, param) {
    var valOne = value.toLowerCase(); // Convert the first value to lowercase
    var valTwo = $(param).val().toLowerCase(); // Convert the second value to lowercase

    return this.optional(element) || valOne !== valTwo;
}, "The values must be different.");
function disableAllElements() {
    // Disable all buttons
    var buttons = document.querySelectorAll('button, input[type="button"], input[type="submit"]');
    buttons.forEach(function(button) {
        button.disabled = true;
    });

    // Disable all links
    var links = document.querySelectorAll('a');
    links.forEach(function(link) {
        link.style.pointerEvents = 'none'; // Prevents clicking
        link.style.color = 'gray'; // Optional: visually indicate that the link is disabled
        link.removeAttribute('href'); // Optionally remove the href attribute
    });
}