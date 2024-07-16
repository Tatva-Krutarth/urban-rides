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

    <link rel="stylesheet" href="<c:url value="/resources/css/adminApproveCaptain.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/toaster.css" />">

    <!-- Icon -->
    <link rel="icon" href="<c:url value="/resources/images/Icon.png" />" type="image/icon type">


    <!-- font  -->
    <link href="<c:url value="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" />"
          rel="stylesheet">
<body>
<%@include file="../admin/adminNavbar.jsp" %>
<div class="container my-trip-container">

    <div class="my-tips-text">
        Approve Captains
    </div>

    <div class="accordion" id="accordionExample">


        <div class="card p-0 mt-2 mb-2">
            <div class="card-header noti-container p-0" id="headingOne">
                <div class="noti-img-cont">
                    <img src="<c:url value='/resources/images/captain2.png'/>" id="noti-img">
                </div>
                <div class="my-trip-accor-details-cont mt-2 mb-3">
                    <div class="service-type-text">Name :- <span class="my-trip-serviceType">Krutarht Gondlaiya</span>
                    </div>
                    <div class="service-type-text">Mobile No. :- <span class="my-trip-serviceType">+91 8849430122</span>
                    </div>
                    <div class="service-type-text">Email :- <span class="my-trip-serviceType">Krutarth123456798@gmail.com   </span>
                    </div>
                    <div class="service-type-text">Created Date :- <span
                            class="my-trip-serviceType">22:204 22/07/2023  </span>
                    </div>
                    <div class="service-type-text">Satus :- <span class="my-trip-serviceType">First Attempt  </span>
                    </div>
                </div>

                <button id="accordion-button" onclick="toggleAccordion(event, 'collapseOne')">
                    View Details
                </button>
            </div>


            <div id="collapseOne" class="collapse" aria-labelledby="headingOne" data-parent="#accordionExample">
                <div class="card-body p-0">
                    <div class="trip-details-bottom-cont">
                        <div class="left-part">
                            <div>
                                <span>Adhar Card : -</span>
                                <span>View</span>
                            </div>
                            <div>
                                <span>   Driving License  :-     </span>
                                <span>   View</span>
                            </div>
                            <div>
                                <span>Registration Certificate  :-</span>
                                <span>View </span>
                            </div>
                        </div>
                        <div class="right-part">
                            <div>
                                <span>RC Expirtation Date  :-</span>
                                <span>View</span>
                            </div>
                            <div>
                                <span>License Expiry date  :-</span>
                                <span>View</span>
                            </div>
                        </div>

                    </div>
                    <button id="captain-approve-btn" class="mb-3">
                        Save
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script src="<c:url value="/resources/js/adminApproveCaptain.js"/>"></script>
</html>
