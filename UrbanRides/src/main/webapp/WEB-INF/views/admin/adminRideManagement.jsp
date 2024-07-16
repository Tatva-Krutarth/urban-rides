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

    <link rel="stylesheet" href="<c:url value="/resources/css/adminRideManagement.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/toaster.css" />">

    <!-- Icon -->
    <link rel="icon" href="<c:url value="/resources/images/Icon.png" />" type="image/icon type">


    <!-- font  -->
    <link href="<c:url value="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" />"
          rel="stylesheet">
<body>
<%@include file="../admin/adminNavbar.jsp" %>
<div class="container">
    <div id="filter-by-id">Filter by</div>


    <div id="ride-management-cont" class=" mb-3 row">
        <div class="input-container col-sm-4  col-12" id="service-type-filter">
            <input type="text" class="form-control taskName" autocomplete="off" id="search-id"
                   name="dropoff" required/>
            <label class="floating-label place-holder">Search by trip ID</label>
            <img src="<c:url value='/resources/images/search.svg'/>"
                 class="pickUpimage">
        </div>
        <div class="input-container   col-sm-4 col-12" style="position: relative">
            <div class="custom-select-wrapper">
                <select id="service-type" name="vehicleType" class="form-control" required>
                    <option value="1" class="vehicle-select-package" selected title="Bike">General booking</option>
                    <option value="2" class="vehicle-select-package" title="Bike">Rent a taxi</option>
                    <option value="3" class="vehicle-select-package" title="Bike">Daily pick up</option>
                </select>
                <label class="always-focus">Service type</label>

            </div>
        </div>
        <div class="input-container   col-sm-4 col-12" style="position: relative">
            <div class="custom-select-wrapper">
                <select id="status" name="vehicleType" class="form-control" required>
                    <option value="1" class="vehicle-select-package" selected title="Bike">Completed</option>
                    <option value="2" class="vehicle-select-package" title="Bike">Cancelled</option>
                    <option value="3" class="vehicle-select-package" title="Bike">Running</option>
                </select>
                <label class="always-focus">Status</label>

            </div>
        </div>
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
                            <span class="my-trip-accor-details-resp">Captain Name : &nbsp; </span><span
                                class="my-trip-pickup">Algatro el petrol</span>
                        </div>
                        <div class="my-trip-accor-details-resp-cont">
                            <span class="my-trip-accor-details-resp">Rider Name : &nbsp; </span><span
                                class="my-trip-pickup">Krutarth Gondaliya</span>
                        </div>
                    </div>

                </div>
                <div class="my-trip-time">
                    22:204 22/07/2023
                </div>
                <div class="my-trip-status">
                    Status : <span class="my-trip-status-value">Cancelled</span>
                </div>
                <button id="accordion-button" onclick="toggleAccordion(event, 'collapseOne')">
                    View More
                </button>
            </div>

            <div id="collapseOne" class="collapse" aria-labelledby="headingOne" data-parent="#accordionExample">
                <div class="card-body p-0">
                    <div class="mt-2">
                        <div class="pickup-dropp">
                            <span class="my-trip-accor-details-resp">Pick up location : &nbsp; </span><span
                                class="my-trip-pickup">Mota Mavva</span>
                        </div>
                        <div class="pickup-dropp">
                            <span class="my-trip-accor-details-resp">Drop off location : &nbsp;</span><span
                                class="my-trip-pickup">Mota  MavvaMota MavvaMota MavvaMota MavvaMota Mavva</span>
                        </div>
                    </div>
                    <div class="trip-details-bottom-cont">
                        <div class="left-part">
                            <div>
                                <span>Distance : -</span>
                                <span>25 Km</span>
                            </div>
                            <div>
                                <span>   Charges  :-     </span>
                                <span>   45 rs</span>
                            </div>
                        </div>

                        <div class="right-part">
                            <div>
                                <span>Duration  :-</span>
                                <span>25 Mins </span>
                            </div>
                            <div>
                                <span>Trip Id  :-</span>
                                <span>25</span>
                            </div>
                        </div>
                    </div>

                    <div class="cancelation-reason">Cancelation Reason :- The ride is cancelled due to no avability of
                        the captain..
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
<script src="<c:url value="/resources/js/adminRideManagement.js"/>"></script>

</html>
