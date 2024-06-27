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

    <link rel="stylesheet" href="<c:url value="/resources/css/riderMyTrips.css" />">
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
<div class="container my-trip-container">

    <div class="my-tips-text">
        My Trips
    </div>

    <div class="accordion" id="accordionExample">


        <div class="card p-0 mt-2 mb-2">
            <div class="card-header noti-container p-0" id="headingOne">
                <div class="noti-img-cont">
                    <img src="<c:url value='/resources/images/wallet-white.svg'/>" id="noti-img">
                </div>
                <div class="my-trip-accor-details-cont mt-2 mb-3">
                    <div class="service-type-text">Service Type :- <span class="my-trip-serviceType">Taxi Booking</span>
                    </div>
                    <hr class="hr-accor">
                    <div class="my-trip-accor-details">
                        <div class="my-trip-accor-details-resp-cont">
                            <span class="my-trip-accor-details-resp">Pick up location : &nbsp; </span><span
                                class="my-trip-pickup">Mota Mavva</span>
                        </div>
                        <div class="my-trip-accor-details-resp-cont">
                            <span class="my-trip-accor-details-resp">Drop off location : &nbsp;</span><span
                                class="my-trip-pickup">Mota MavvaMota MavvaMota MavvaMota MavvaMota MavvaMota MavvaMota MavvaMota MavvaMota MavvaMota Mavva</span>
                        </div>

                    </div>

                </div>
                <div class="my-trip-time">
                    22:204 22/07/2023
                </div>
                <button class="accordion-button" onclick="toggleAccordion(event, 'collapseOne')">
                    View More
                </button>
            </div>

            <div id="collapseOne" class="collapse" aria-labelledby="headingOne" data-parent="#accordionExample">
                <div class="card-body">
                    Some placeholder content for the first accordion panel. This panel is hidden by default.
                </div>
            </div>
        </div>


    </div>
</div>

<div class="hs-toast-wrapper  hs-toast-fixed-top " id="example"></div>
<script src="<c:url value="/resources/js/toaster.js"/>"></script>


<%------------------------------------jquerry  validation--%>

<script src="<c:url value="/resources/js/riderMyTrips.js"/>"></script>

</body>
</html>
