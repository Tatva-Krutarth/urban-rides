$(document).ready(function () {
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
            formData.append('supportType', form.supportType.value);
            formData.append('description', form.description.value);
            var fileInput = form.uploadFile;
            if (fileInput.files.length > 0) {
                formData.append('uploadFile', fileInput.files[0]);
            }

            $(".loader").css("display", "flex");

            $.ajax({
                type: 'POST',
                url: 'captain-get-support',
                data: formData,
                processData: false,
                contentType: false,
                cache: false,
                dataType: 'json',
                success: function (data) {
                    showSuccesstMsg('Support request submitted successfully.');
                    $('#supportForm')[0].reset();
                    $(".loader").hide();

                },
                error: function (jqXHR, textStatus, errorThrown) {
                    $(".loader").hide();
                    var responseText = jqXHR.responseText;
                    try {
                        responseText = JSON.parse(responseText);
                    } catch (e) {
                        showErrorMsg(jqXHR.responseText);
                        return;
                    }

                    if (typeof responseText === 'object' && !Array.isArray(responseText)) {
                        var messages = Object.values(responseText).join(', ');
                        showErrorMsg(messages);
                    } else {
                        showErrorMsg("An unexpected error occurred: " + jqXHR.responseText);
                    }
                }

            });
            return false;
        }
    });

    $('#search-btn-help').click(function () {
        var queryId = $('#queryId').val().trim();
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
                showErrorMsg('No support request found with the given ID.');
            }
        });
    });
});

