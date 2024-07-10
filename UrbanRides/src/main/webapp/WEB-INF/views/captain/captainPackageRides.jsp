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

    <link rel="stylesheet" href="<c:url value="/resources/css/captainPackageRides.css" />">
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
<%@include file="../captain/captainNavbar.jsp" %>

<div class="container">
    <div class="service-trip-text">
        Available services trips
    </div>


    <hr>
    <div class="service-trip-text-sub">
        Rent a taxi
    </div>
    <div class="accordion" id="rentATaxiAccor">
        <div class="card p-0 mt-2 mb-2">
            <div class="card-header noti-container p-0" id="headingOne">
                <div class="noti-img-cont">
                    <img src="<c:url value='/resources/images/wallet-white.svg'/>" id="noti-img">
                </div>
                <div class="my-trip-accor-details-cont mt-2 mb-3">
                    <%--                    <div class="service-type-text">Service Type :- <span class="my-trip-serviceType">Taxi Booking</span></div>--%>
                    <%--                    <hr class="hr-accor">--%>
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
                <%--                <div class="my-trip-status">--%>
                <%--                    Status : <span class="my-trip-status-value">Cancelled</span>--%>
                <%--                </div>--%>
                <button class="accordion-button" onclick="toggleAccordion(event, 'collapseOne')">
                    View More
                </button>
            </div>
            <div id="collapseOne" class="collapse" aria-labelledby="headingOne" data-parent="#rentATaxiAccor">
                <div class="card-body p-0">
                    <div class="captain-info-nav">
                        <div class="captain-info-profile">
                            <img src="<c:url value='/resources/images/profile-logo.svg' />" alt="">
                        </div>
                        <div class="captain-info-discription">
                            <div class="ratings-star">
                                <div class="captain-org-name">
                                    Ramesh Singh
                                </div>
                            </div>
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
                    <button type="submit" class="captain-accept-btn mt-2 mb-2">Accept Ride</button>
                </div>
            </div>
        </div>
    </div>




    <div class="service-trip-text-sub">
        Package service
    </div>
    <div class="accordion" id="packageServiceAccor">
        <div class="card p-0 mt-2 mb-2">
            <div class="card-header noti-container p-0" id="headingTwo">
                <div class="noti-img-cont">
                    <img src="<c:url value='/resources/images/wallet-white.svg'/>" id="noti-img">
                </div>
                <div class="my-trip-accor-details-cont mt-2 mb-3">
                    <%--                    <div class="service-type-text">Service Type :- <span class="my-trip-serviceType">Taxi Booking</span></div>--%>
                    <%--                    <hr class="hr-accor">--%>
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
                <%--                <div class="my-trip-status">--%>
                <%--                    Status : <span class="my-trip-status-value">Cancelled</span>--%>
                <%--                </div>--%>
                <button class="accordion-button" onclick="toggleAccordion(event, 'collapseTwo')">
                    View More
                </button>
            </div>

            <div id="collapseTwo" class="collapse" aria-labelledby="headingOne" data-parent="#packageServiceAccor">
                <div class="card-body p-0">
                    <div class="captain-info-nav">
                        <div class="captain-info-profile">
                            <img src="<c:url value='/resources/images/profile-logo.svg' />" alt="">
                        </div>
                        <div class="captain-info-discription">
                            <div class="ratings-star">
                                <div class="captain-org-name">
                                    Ramesh Singh
                                </div>
                            </div>
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
                    <button type="submit" class="captain-accept-btn mt-2 mb-2">Accept Ride</button>
                </div>
            </div>
        </div>
    </div>





<%--    <div class="service-trip-text-sub">--%>
<%--        Lugguage service--%>
<%--    </div>--%>
<%--    <div class="accordion" id="lugguageService">--%>
<%--        <div class="card p-0 mt-2 mb-2">--%>
<%--            <div class="card-header noti-container p-0" id="headingThree">--%>
<%--                <div class="noti-img-cont">--%>
<%--                    <img src="<c:url value='/resources/images/wallet-white.svg'/>" id="noti-img">--%>
<%--                </div>--%>
<%--                <div class="my-trip-accor-details-cont mt-2 mb-3">--%>
<%--                    &lt;%&ndash;                    <div class="service-type-text">Service Type :- <span class="my-trip-serviceType">Taxi Booking</span></div>&ndash;%&gt;--%>
<%--                    &lt;%&ndash;                    <hr class="hr-accor">&ndash;%&gt;--%>
<%--                    <div class="my-trip-accor-details">--%>
<%--                        <div class="my-trip-accor-details-resp-cont">--%>
<%--                            <span class="my-trip-accor-details-resp">Pick up location : &nbsp; </span><span--%>
<%--                                class="my-trip-pickup">Mota Mavva</span>--%>
<%--                        </div>--%>
<%--                        <div class="my-trip-accor-details-resp-cont">--%>
<%--                            <span class="my-trip-accor-details-resp">Drop off location : &nbsp;</span><span--%>
<%--                                class="my-trip-pickup">Mota MavvaMota MavvaMota MavvaMota MavvaMota MavvaMota MavvaMota MavvaMota MavvaMota MavvaMota Mavva</span>--%>
<%--                        </div>--%>

<%--                    </div>--%>

<%--                </div>--%>
<%--                <div class="my-trip-time">--%>
<%--                    22:204 22/07/2023--%>
<%--                </div>--%>
<%--                &lt;%&ndash;                <div class="my-trip-status">&ndash;%&gt;--%>
<%--                &lt;%&ndash;                    Status : <span class="my-trip-status-value">Cancelled</span>&ndash;%&gt;--%>
<%--                &lt;%&ndash;                </div>&ndash;%&gt;--%>
<%--                <button class="accordion-button" onclick="toggleAccordion(event, 'collapseThree')">--%>
<%--                    View More--%>
<%--                </button>--%>
<%--            </div>--%>

<%--            <div id="collapseThree" class="collapse" aria-labelledby="headingOne" data-parent="#lugguageService">--%>
<%--                <div class="card-body p-0">--%>
<%--                    <div class="captain-info-nav">--%>
<%--                        <div class="captain-info-profile">--%>
<%--                            <img src="<c:url value='/resources/images/profile-logo.svg' />" alt="">--%>
<%--                        </div>--%>
<%--                        <div class="captain-info-discription">--%>
<%--                            <div class="ratings-star">--%>
<%--                                <div class="captain-org-name">--%>
<%--                                    Ramesh Singh--%>
<%--                                </div>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>


<%--                    <div class="trip-details-bottom-cont">--%>
<%--                        <div class="left-part">--%>
<%--                            <div>--%>
<%--                                <span>Distance : -</span>--%>
<%--                                <span>25 Km</span>--%>
<%--                            </div>--%>
<%--                            <div>--%>
<%--                                <span>   Charges  :-     </span>--%>
<%--                                <span>   45 rs</span>--%>
<%--                            </div>--%>
<%--                        </div>--%>

<%--                        <div class="right-part">--%>
<%--                            <div>--%>
<%--                                <span>Duration  :-</span>--%>
<%--                                <span>25 Mins </span>--%>
<%--                            </div>--%>
<%--                            <div>--%>
<%--                                <span>Trip Id  :-</span>--%>
<%--                                <span>25</span>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                    <div class="cancelation-reason">Cancelation Reason :- The ride is cancelled due to no avability of--%>
<%--                        the captain..--%>
<%--                    </div>--%>
<%--                    <button type="submit" class="captain-accept-btn mt-2 mb-2">Accept Ride</button>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>





    <hr>
    <div class="service-trip-text">
        Current Running Service
    </div>
    <div class="accordion" id="currentRunning">
        <div class="card p-0 mt-2 mb-2">
            <div class="card-header noti-container p-0" id="headingFour">
                <div class="noti-img-cont">
                    <img src="<c:url value='/resources/images/wallet-white.svg'/>" id="noti-img">
                </div>
                <div class="my-trip-accor-details-cont mt-2 mb-3">
                    <%--                    <div class="service-type-text">Service Type :- <span class="my-trip-serviceType">Taxi Booking</span></div>--%>
                    <%--                    <hr class="hr-accor">--%>
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
                <%--                <div class="my-trip-status">--%>
                <%--                    Status : <span class="my-trip-status-value">Cancelled</span>--%>
                <%--                </div>--%>
                <button class="accordion-button" onclick="toggleAccordion(event, 'collapseFour')">
                    View More
                </button>
            </div>

            <div id="collapseFour" class="collapse" aria-labelledby="headingOne" data-parent="#currentRunning">
                <div class="card-body p-0">
                    <div class="captain-info-nav">
                        <div class="captain-info-profile">
                            <img src="<c:url value='/resources/images/profile-logo.svg' />" alt="">
                        </div>
                        <div class="captain-info-discription">
                            <div class="ratings-star">
                                <div class="captain-org-name">
                                    Ramesh Singh
                                </div>
                            </div>
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
                    <button type="submit" class="captain-accept-btn mt-2 mb-2">Accept Ride</button>
                </div>
            </div>
        </div>
    </div>



</div>


</body>
<div class="hs-toast-wrapper  hs-toast-fixed-top " id="example"></div>
<script src="<c:url value="/resources/js/toaster.js"/>"></script>
<script src="<c:url value="/resources/js/captainPackageRides.js"/>"></script>

</html>
