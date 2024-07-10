$(document).ready(function () {
    $('#supportForm').on('submit', function (event) {
        event.preventDefault();

        var formData = new FormData(this);

        $.ajax({
            type: 'POST',
            enctype: 'multipart/form-data',
            url: 'captain-get-support',
            data: formData,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
            success: function (data) {
                // Handle success response
                showSuccesstMsg('Support request submitted successfully.');

                // Reset the form
                $('#supportForm')[0].reset();

                // Optionally redirect or show success message
            },
            error: function (e) {
                // Handle error response
                showErrorMsg('Error occurred while submitting the support request.');
            }
        });
    });
});
