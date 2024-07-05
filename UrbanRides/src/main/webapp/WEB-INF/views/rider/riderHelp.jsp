<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Urban Rides</title>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"
            integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            crossorigin="anonymous"></script>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="<c:url value="/resources/css/getHelp.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/toaster.css" />">

    <!-- Icon -->
    <link rel="icon" href="<c:url value="/resources/images/Icon.png" />" type="image/icon type">

</head>
<body>
<%@include file="../commonImports/loader.jsp" %>
<%@include file="../rider/riderNavbar.jsp" %>


<!-- Get Support Section -->
<div class="container my-trip-container mt-5">
    <div class="my-tips-text">
        Get Support
    </div>
    <p>If you have any queries, requests, or complaints, please use the form below to get in touch with us. Our support
        team is here to assist you.</p>

    <form id="supportForm" action="support-request" method="post" enctype="multipart/form-data" class="mt-4">
        <div class="mb-3">
            <label for="supportType" class="form-label">Type of Support</label>
            <select class="form-select" id="supportType" name="supportType" required>
                <option value="" selected disabled>Select</option>
                <option value="1">Query</option>
                <option value="2">Request</option>
                <option value="3">Complaint</option>
                <option value="4">Ask for a Call</option>
            </select>
        </div>
        <div class="mb-3">
            <label for="description" class="form-label">Description</label>
            <textarea class="form-control" id="description" name="description" rows="5" required></textarea>
        </div>
        <div class="mb-3">
            <label for="uploadFile" class="form-label">Upload File (if any)</label>
            <input class="form-control" type="file" id="uploadFile" name="uploadFile">
        </div>
        <button type="submit" id="submitBtn" class="submit-btn">Submit</button>
    </form>

</div>


<div class="hs-toast-wrapper  hs-toast-fixed-top " id="example"></div>
<script src="<c:url value="/resources/js/toaster.js"/>"></script>


<%------------------------------------jquerry  validation--%>

<script src="<c:url value="/resources/js/riderHelp.js"/>"></script>

</body>
</html>
