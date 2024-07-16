$(document).ready(function () {
    // jQuery Validation for the form
    $.validator.addMethod("fileType", function (value, element) {
        var fileType = value.split('.').pop().toLowerCase();
        return this.optional(element) || $.inArray(fileType, ['pdf', 'jpg', 'jpeg', 'png']) !== -1;
    }, "Only PDF, JPG, and PNG formats are allowed.");

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
                fileType: "Only PDF, JPG, and PNG formats are allowed."
            }
        },
        submitHandler: function (form) {
            var formData = new FormData(form);

            $.ajax({
                type: 'POST',
                enctype: 'multipart/form-data',
                url: 'support-request', // Adjust URL to your endpoint
                data: formData,
                processData: false,
                contentType: false,
                cache: false,
                timeout: 600000,
                success: function (data) {
                    // Handle success response
                    alert('Support request submitted successfully.');

                    // Reset the form
                    $('#supportForm')[0].reset();
                },
                error: function (e) {
                    // Handle error response
                    alert('Error occurred while submitting the support request.');
                }
            });

            return false; // Prevent default form submission
        }
    });

    // Function to search support requests by ID
    $('#search-btn-help').click(function () {
        var queryId = $('#queryId').val().trim();
        console.log(queryId)
        if (queryId === "") {
            showErrorMsg('Please enter a Support Request ID.');
            return;
        }

        $.ajax({
            type: 'GET',
            url: 'search-support-request', // Ensure correct endpoint URL
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
