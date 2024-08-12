$(document).ready(function () {
    // jQuery Validation for the form
    $.validator.addMethod("fileType", function (value, element) {
        var fileType = value.split('.').pop().toLowerCase();
        return this.optional(element) || $.inArray(fileType, ['pdf']) !== -1;
    }, "Only PDF formats are allowed.");

    $('#supportForm').validate({
        rules: {
            supportType: {
                required: true
            },
            description: {
                required: true
            },
            uploadFile: {
                fileType: true
            }
        },
        messages: {
            supportType: {
                required: "Please select a type of support."
            },
            description: {
                required: "Please provide a description."
            },
            uploadFile: {
                fileType: "Only PDF format is allowed."
            }
        },
        submitHandler: function (form) {
            var formData = new FormData();

            // Append other form fields
            formData.append('supportType', form.supportType.value);
            formData.append('description', form.description.value);

            // Handle file input
            var fileInput = form.uploadFile;
            if (fileInput.files.length > 0) {
                formData.append('uploadFile', fileInput.files[0]);
            }
            // else {
            //     formData.append('uploadFile', null); // or you can choose not to append anything
            // }
            $(".loader").css("display", "flex");

            $.ajax({
                type: 'POST',
                url: 'rider-get-support', // Adjust URL to your endpoint
                data: formData,
                processData: false,
                contentType: false,
                cache: false,
                dataType: 'json',
                success: function (data) {
                    // Handle success response
                    showSuccesstMsg('Support request submitted successfully.');

                    // Reset the form
                    $('#supportForm')[0].reset();
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
            return false;
        }
    });

    $('#search-btn-help').click(function () {
        var queryId = $('#queryId').val().trim();
        console.log(queryId)
        if (queryId === "") {
            showErrorMsg('Please enter a Support Request ID.');
            return;
        }

        $.ajax({
            type: 'GET',
            url: 'search-support-request',
            data: {id: queryId},
            success: function (data) {
                if (data && data.id) {
                    $('#requestId').text(data.id);
                    $('#requestType').text(data.type);
                    $('#requestDescription').text(data.description);
                    $('#requestStatus').text(data.status);
                    $('#supportRequestModal').modal('show');
                } else {
                    showErrorMsg('No support request found with the given ID.');
                }
            },
            error: function (e) {
                showErrorMsg('Error occurred while searching for the support request.');
            }
        });
    });
});

