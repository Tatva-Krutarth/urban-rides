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

<nav class="container-fluid nav-class d-flex justify-content-between align-items-center fixed-top">
    <!-- nav content -->
    <div class="mx-3 d-flex justify-content-between align-items-center"
         style="color: white; font-size: 24px; position: relative ">
        <div class="back-button" id="back-button">
            <img src="<c:url value='/resources/images/back-arrow.svg'/>" id="back-arrow">
        </div>
        <div>
            Urban Rides
        </div>
    </div>

    <div class="nav-right-side">
        <a href="rider-notifications" style="text-decoration: none" class="nati-cont">
            <img src="<c:url value='/resources/images/notification1.svg' />" alt="">
        </a>

        <a href="rider-my-trip" style="text-decoration: none" class="common-btn-color7-theme nav-my-trip-cont">
            <div class="nav-my-trip-img">
                <img src="<c:url value='/resources/images/location2.svg' />" alt="">
            </div>
            <div class="nav-my-trip">
                My Trips
            </div>
        </a>
        <div class="dropdown d-flex flex-row-reverse align-items-center">
            <button class="dropbtn ">
                <div class="profile-pic">
                    <img src="<c:url value='/resources/images/profile-logo.svg' />" alt="">
                </div>
                <div class="profile-pic-side-dropdown">
                    <img src="<c:url value='/resources/images/down-arrow.svg' />" alt="">
                </div>
            </button>

            <div class="dropdown-content">
                <a href="rider-notifications" class="dropdown-list show-mobile" style="text-decoration: none">
                    <img src="<c:url value='/resources/images/notification1.svg' />" alt="">    </span>
                    Notifications</a>
                <div>
                    <hr class="hr-setting">
                </div>
                <a href="rider-my-trip" class="dropdown-list show-mobile" style="text-decoration: none">
                    <img src="<c:url value='/resources/images/location2.svg' />" alt="">    </span>
                    My Trips</a>
                <div>
                    <hr class="hr-setting">
                </div>


                <a href="rider-manage-account" class="dropdown-list " style="text-decoration: none">
                    <img src="<c:url value='/resources/images/manage-account.svg' />" alt="">    </span>
                    Manage your account</a>
                <div>
                    <hr class="hr-setting">
                </div>
                <a href="rider-wallet" class="dropdown-list " style="text-decoration: none">
                    <img src="<c:url value='/resources/images/wallet.svg' />" alt="">    </span>
                    Wallet</a>
                <div>
                    <hr class="hr-setting">
                </div>
                <a href="rider-help" class="dropdown-list " style="text-decoration: none">
                    <img src="<c:url value='/resources/images/help.svg' />" alt="">    </span>
                    Help</a>
                <div>
                    <hr class="hr-setting">
                </div>
                <a href="user-login" class="dropdown-list ">
                    <img src="<c:url value='/resources/images/sign-out.svg' />" alt=""></span>
                    Sign out
                </a>
            </div>
        </div>
    </div>
</nav>
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
                    <input type="text" class="personal-details-data" name="firstName" value="" readonly>
                </div>
                <div class="personal-details-cont">
                    <div class="personal-details-title">Last Name</div>
                    <input type="text" class="personal-details-data" name="lastName" value="">
                </div>
                <div class="personal-details-cont">
                    <div class="personal-details-title">Phone</div>
                    <input type="text" class="personal-details-data" name="phone" value="">
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
                    <input type="password" class="personal-details-data login-details" id="current-password"
                           name="currentPassword">
                    <div style="float: right; position: relative;">
                        <img src="<c:url value='/resources/images/password-eye.svg'/>"
                             id="togglePassword1"
                             style="position: absolute; right: 17px; top: 8px; width: 21px;">
                    </div>
                </div>

                <div class="personal-details-cont hide-this d-none position-relative">
                    <div class="personal-details-title">New Password</div>
                    <input type="password" class="personal-details-data login-details" id="new-password"
                           name="newPassword">
                    <div style="float: right; position: relative;">
                        <img src="<c:url value='/resources/images/password-eye.svg'/>" id="togglePassword2"
                             style="position: absolute; right: 17px;top: 8px;  bottom: 15px; width: 21px; height: 21px;">
                    </div>
                </div>
                <div class="personal-details-cont hide-this d-none position-relative">
                    <div class="personal-details-title">Confirm New Password</div>
                    <input type="password" class="personal-details-data login-details" id="conf-new-password"
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


<%------------------------------------jquerry  validation--%>
<script src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.20.0/jquery.validate.min.js"/>"></script>

<script src="<c:url value="/resources/js/riderManageAccount.js"/>"></script>
</html>
