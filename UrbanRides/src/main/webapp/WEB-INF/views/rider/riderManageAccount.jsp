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
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"
            integrity="sha512-iKDtgDyTHjAitUDdLljGhenhPwrbBfqTKWO1mkhSFH3A7blITC9MhYon6SjnMhp4o0rADGw9yAC6EW4t5a4K3g=="
            crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.6.1/sockjs.min.js"
            integrity="sha512-1QvjE7BtotQjkq8PxLeF6P46gEpBRXuskzIVgjFpekzFVF4yjRgrQvTG1MTOJ3yQgvTteKAcO7DSZI92+u/yZw=="
            crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <link rel="stylesheet" href="<c:url value="/resources/css/riderManageAccount.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/toaster.css" />">
    <script src="<c:url value="/resources/js/rider-web-socket.js"/>"></script>
    <link rel="icon" href="<c:url value="/resources/images/Icon.png" />" type="image/icon type">
    <link href="<c:url value="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" />"
          rel="stylesheet">

</head>
<body>
<%@include file="../commonImports/loader.jsp" %>
<%@include file="../rider/riderNavbar.jsp" %>

<div class="container">

    <div class="manage-account-cont container">
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
                    <input type="text" class="personal-details-data" name="lastName" id="last-Name"  value="" readonly>
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

        </div>
    </div>
</div>
</body>

<div class="hs-toast-wrapper  hs-toast-fixed-top " id="example"></div>
<script src="<c:url value="/resources/js/toaster.js"/>"></script>
<script src="<c:url value="https://code.jquery.com/jquery-3.6.0.min.js"/>"></script>
<script src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.20.0/jquery.validate.min.js"/>"></script>
<script src="<c:url value="/resources/js/riderManageAccount.js"/>"></script>
</html>
