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

    <%--    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>--%>
    <%--    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>--%>
    <%--    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/js/bootstrapbootstrap.min.js"></script>--%>
    <%--//web socket------%>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"
            integrity="sha512-iKDtgDyTHjAitUDdLljGhenhPwrbBfqTKWO1mkhSFH3A7blITC9MhYon6SjnMhp4o0rADGw9yAC6EW4t5a4K3g=="
            crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.6.1/sockjs.min.js"
            integrity="sha512-1QvjE7BtotQjkq8PxLeF6P46gEpBRXuskzIVgjFpekzFVF4yjRgrQvTG1MTOJ3yQgvTteKAcO7DSZI92+u/yZw=="
            crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <!-- Add custom CSS file (replace 'landingPage.css' with your CSS file name) -->

    <link rel="stylesheet" href="<c:url value="/resources/css/riderManageAccount.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/toaster.css" />">

    <!-- Icon -->
    <link rel="icon" href="<c:url value="/resources/images/Icon.png" />" type="image/icon type">


    <!-- font  -->
    <link href="<c:url value="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" />"
          rel="stylesheet">

</head>
<body>
<%@include file="../commonImports/loader.jsp" %>
<%@include file="../captain/captainNavbar.jsp" %>

<div class="container">

    <div class="manage-account-cont container" id="captain-manage-account-cont">
        <div class="left-part">
            <div class="manage-account-profile-photo">
                <img src="<c:url value='/resources/images/profile-logo-black.svg' />" alt="Profile Photo">
            </div>
            <div class="profile-pic-text">Profile Pic</div>
            <div class="change-profile-photo-cont mt-4">
                <button class="change-profile-photo-button common-btn-color3-theme" type="button">
                    Change Profile Photo
                </button>
                <input type="file" id="profile-photo-input" accept="image/png, image/jpeg" style="display: none;">
            </div>
        </div>

        <div class="right-part">
            <form id="user-management-form">
                <hr>
                <div class="mang-acc-heading mt-4">
                    Personal details
                </div>
                <div class="personal-details-cont">
                    <div class="personal-details-title">First Name</div>
                    <input type="text" class="personal-details-data" name="firstName" id="first-Name" value="" readonly>
                </div>
                <div class="personal-details-cont">
                    <div class="personal-details-title">Last Name</div>
                    <input type="text" class="personal-details-data" name="lastName" id="last-Name" value="" readonly>
                </div>
                <div class="personal-details-cont">
                    <div class="personal-details-title">Phone</div>
                    <input type="text" class="personal-details-data" name="phone" value="" readonly>
                </div>
                <div class="edit-btn-cont mt-4">
                    <button class="edit-button first-button common-btn-color3-theme" type="button">
                        Edit
                    </button>
                    <button class="edit-button second-button common-btn-color3-theme d-none" type="button">
                        Save Changes
                    </button>

                </div>
            </form>
            <hr>
            <form id="user-management-login-details">
                <div class="mang-acc-heading mt-2">
                    Login details
                </div>

                <div class="personal-details-cont">
                    <div class="personal-details-title">Email</div>
                    <input type="text" class="personal-details-data-login" name="email" value="" readonly>
                </div>

                <div class="personal-details-cont hide-this d-none position-relative">
                    <div class="personal-details-title">Current Password</div>
                    <input type="password" class="personal-details-data-login login-details" id="current-password"
                           name="currentPassword">
                    <div style="float: right; position: relative;">
                        <img src="<c:url value='/resources/images/password-eye.svg'/>"
                             id="togglePassword1"
                             style="position: absolute; right: 17px; top: 8px; width: 21px;">
                    </div>
                </div>

                <div class="personal-details-cont hide-this d-none position-relative">
                    <div class="personal-details-title">New Password</div>
                    <input type="password" class="personal-details-data-login login-details" id="new-password"
                           name="newPassword">
                    <div style="float: right; position: relative;">
                        <img src="<c:url value='/resources/images/password-eye.svg'/>" id="togglePassword2"
                             style="position: absolute; right: 17px;top: 8px;  bottom: 15px; width: 21px; height: 21px;">
                    </div>
                </div>
                <div class="personal-details-cont hide-this d-none position-relative">
                    <div class="personal-details-title">Confirm New Password</div>
                    <input type="password" class="personal-details-data-login login-details" id="conf-new-password"
                           name="confNewPassword">
                    <div style="float: right; position: relative;">
                        <img src="<c:url value='/resources/images/password-eye.svg'/>" id="togglePassword3"
                             style="position: absolute; right: 17px;top: 8px;  bottom: 15px; width: 21px; height: 21px;">
                    </div>
                </div>

                <div class="edit-btn-cont mt-4">
                    <button id="hide-btn" class="edit-button-login-details login-first common-btn-color3-theme"
                            type="button">
                        Edit Login Credentials
                    </button>
                    <button id="unhide-btn"
                            class="edit-button-login-details login-second common-btn-color3-theme d-none" type="button">
                        Save Changes
                    </button>
                </div>
            </form>
            <hr>
            <div class="doc-cont">
   <span id="aprroved-img">
                      <svg width="23" height="23" viewBox="0 0 52 52" xmlns="http://www.w3.org/2000/svg">
        <circle cx="26" cy="26" r="25" fill="none" stroke="#5cb85c" stroke-width="4"/>
        <path class="tick-mark" fill="none" stroke="#5cb85c" stroke-width="4" d="M15 27 l7 7 l15 -15"/>
    </svg>
                    </span>
                <div class="mang-acc-heading mt-0">

                    Document Status
                </div>
                <div class="aprroved-status">Approved</div>
                <div class="mt-3">
                    We are pleased to inform you that your documents have been successfully verified. As a crucial part
                    of our service, ensuring that all captains have their documents approved helps us maintain the
                    highest standards of safety and reliability.
                </div>
            </div>
            <hr>

            <%--            <div class="mang-acc-heading mt-2">--%>
            <%--                <h3>Vehicle Details</h3>--%>
            <%--            </div>--%>
            <%--            <p>If you need to change your vehicle, please send a request to the admin for approval. Once you send the--%>
            <%--                request, you won't be able to log in again until your documents are verified.</p>--%>

            <%--            <div class="mt-3">--%>
            <%--                <button type="button" class="btn btn-warning" data-bs-toggle="modal"--%>
            <%--                        data-bs-target="#vehicleChangeRequestModal">Request Vehicle Change--%>
            <%--                </button>--%>
            <%--            </div>--%>

            <%--            <div class="alert alert-info mt-3" role="alert">--%>
            <%--                <strong>Note:</strong> If your request is approved, you will need to re-upload all the documents for the--%>
            <%--                new vehicle.--%>
            <%--            </div>--%>


            <div class="modal fade" id="vehicleChangeRequestModal" tabindex="-1"
                 aria-labelledby="vehicleChangeRequestModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="vehicleChangeRequestModalLabel">Request Vehicle Change</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <p>Please provide the details of the new vehicle you wish to register.</p>
                            <form id="vehicleChangeRequestForm">
                                <div class="mb-3">
                                    <label for="newVehicleDetails" class="form-label">New Vehicle Details</label>
                                    <textarea class="form-control" id="newVehicleDetails" name="newVehicleDetails"
                                              rows="3" required></textarea>
                                </div>
                                <button type="submit" class="btn btn-primary">Send Request</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

<div class="hs-toast-wrapper  hs-toast-fixed-top " id="example"></div>
<script src="<c:url value="/resources/js/toaster.js"/>"></script>


<%------------------------------------jquerry  validation--%>
<script src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.20.0/jquery.validate.min.js"/>"></script>
<script src="<c:url value="/resources/js/captain-web-socket.js"/>"></script>

<script src="<c:url value="/resources/js/captainManageAccount.js"/>"></script>
</html>
