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

    <link rel="stylesheet" href="<c:url value="/resources/css/riderDashboard.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/toaster.css" />">

    <!-- Icon -->
    <link rel="icon" href="<c:url value="/resources/images/Icon.png" />" type="image/icon type">


    <!-- font  -->
    <link href="<c:url value="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" />"
          rel="stylesheet">

</head>
<body>
<%@include file="../commonImports/loader.jsp" %>
<%@include file="../rider/riderNavbar.jsp" %>


<div class=" overflow-hidden h-100">
    <div class="main-layout">

        <div class="div1">
            <div class="registation-lable  text-center">
                Get a Ride!
            </div>
            <div class="container this-for-responsive">
                <div class="ride-package">


                    <div class="type-of-ride">
                        <div class="normal-ride p-2 mx-2 ride-type-active" id="ride">
                            <img src="<c:url value='/resources/images/car1.png' />" alt="">
                            Ride
                        </div>
                        <div class="package-ride mx-2 p-2" id="package">
                            <img src="<c:url value='/resources/images/package.svg' />" alt="">
                            Package
                        </div>
                    </div>
                    <hr class="container hr-for-divs">


                    <form id="package-form" style="display: none">
                        <div id="package-form-inner-cont">
                            <div class="land-sub-heading-t2 w-100 select-service mb-2">Select a service</div>
                            <div class="dropdown-package w-100">
                                <button type="button" class="dropbtn-package w-100" id="selectedService">Rent a Taxi</button>
                                <div class="dropdown-content-package w-100">
                                    <a href="#" id="dailyPickup">Daily Pickup</a>
                                </div>
                            </div>
                            <input type="hidden" value="Rent a Taxi" id="selectedOption" name="selectedOption">

                            <div class="input-container mt-3 mb-2">
                                <input type="text" class="form-control taskName" autocomplete="off" id="pickupLocation"
                                       name="pickupLocation" required>
                                <label class="floating-label pickup-placeholder d-none place-holder"
                                       for="pickupLocation"
                                       id="pickup-dropoff-label-id">Pick up - Drop off
                                    location</label>
                                <label class="always-focus" id="pick-up-toggle">Pick up - Drop off
                                    location</label>

                                <img src="<c:url value='/resources/images/pickup.svg'/>"
                                     class="pickUpimage pickup-placeholder-img">
                            </div>
                            <div class="input-container mt-3 mb-2 d-none">
                                <input type="text" class="form-control taskName " autocomplete="off"
                                       id="dropoffLocation"
                                       name="dropoffLocation" required>
                                <label class="floating-label place-holder" for="dropoffLocation">Drop Off
                                    location</label>
                                <img src="<c:url value='/resources/images/location-animation.svg'/>"
                                     class="pickUpimage">
                            </div>

                            <div class="input-container mt-3 mb-2">
                                <input type="date" class="form-control taskName" autocomplete="off" id="pickupDate"
                                       name="pickupDate" required>
                                <label class="always-focus">Pick up date</label>
                                <img src="<c:url value='/resources/images/pickup-date.svg'/>"
                                     class="pickUpimage pickup-placeholder-img">
                            </div>


                            <div class="input-container mt-3 mb-2">
                                <input type="time" class="form-control taskName" autocomplete="off" id="pickupTime"
                                       name="pickupTime" required>
                                <label class="always-focus" for="pickupTime">Pickup Time</label>
                                <img src="<c:url value='/resources/images/time.svg'/>"
                                     class="pickUpimage pickup-placeholder-img">
                            </div>
                            <div class="input-container mt-3 mb-2">
                                <input type="time" class="form-control taskName" autocomplete="off" id="drofOffTime"
                                       name="drofOffTime" required>
                                <label class="always-focus" for="pickupTime">DropOff Time</label>
                                <img src="<c:url value='/resources/images/time.svg'/>"
                                     class="pickUpimage pickup-placeholder-img">
                            </div>


                            <div class="input-container mt-3 mb-3">
                                <input type="number" class="form-control taskName" autocomplete="off" id="numPassengers"
                                       name="numPassengers" required>
                                <label class="floating-label pickup-placeholder place-holder" for="numPassengers">Number
                                    of Passengers</label>
                                <img src="<c:url value='/resources/images/number-of-passenger.svg'/>"
                                     class="pickUpimage pickup-placeholder-img">
                            </div>


                            <div class="land-sub-heading-t2 w-100 mt-2 mb-2">Select the type of vehicle</div>
                            <div class="input-container mt-1 mb-2">
                                <div class="custom-select-wrapper">
                                    <select id="vehicleType" name="vehicleType" class="form-control" required>
                                        <option value="1" class="vehicle-select-package" selected title="Bike">Bike</option>
                                        <option value="2" class="vehicle-select-package" title="Rickshaw">Rickshaw</option>
                                        <option value="3" class="vehicle-select-package" title="Car">Car</option>
                                        <option value="4" class="vehicle-select-package" title="Luxury Car">Luxury Car</option>
                                        <option value="5" class="vehicle-select-package" title="Bus">Bus</option>
                                    </select>
                                </div>
                            </div>


                            <div class="land-sub-heading-t2 w-100 mb-1 mt-3 d-none" id="hide-daily-pickup-text">Daily
                                Pickup
                            </div>
                            <div class="checkbox-container d-none" id="hide-all-checkbox">

                                <div>
                                    <input type="checkbox" id="day1" name="day1" value="1">
                                    <label for="day1">Monday </label>
                                </div>
                                <div>
                                    <input type="checkbox" id="day2" name="day2" value="2">
                                    <label for="day2">Tuesday</label>
                                </div>
                                <div>
                                    <input type="checkbox" id="day3" name="day3" value="3">
                                    <label for="day3">Wednesday</label>
                                </div>
                                <div>
                                    <input type="checkbox" id="day4" name="day4" value="4">
                                    <label for="day4">Thursday</label>
                                </div>
                                <div>
                                    <input type="checkbox" id="day5" name="day5" value="5">
                                    <label for="day5">Friday</label>
                                </div>
                                <div>
                                    <input type="checkbox" id="day6" name="day6" value="6">
                                    <label for="day6">Saturday</label>
                                </div>
                                <div>
                                    <input type="checkbox" id="day7" name="day7" value="7">
                                    <label for="day7">Sunday</label>
                                </div>
                            </div>

                            <div class="w-100 row">
                                <input type="text" id="selectedDays" class="d-none" required name="selectedDays"
                                       value="">
                            </div>
                            <div class="input-container mt-3 mb-3">
                                <input type="number" class="form-control taskName" autocomplete="off" id="numDays"
                                       name="numDays" required>
                                <label class="floating-label pickup-placeholder place-holder" for="numPassengers">Number
                                    of Days</label>
                                <img src="<c:url value='/resources/images/number-of-passenger.svg'/>"
                                     class="pickUpimage pickup-placeholder-img">
                            </div>
                            <div class="input-container mt-2 mb-2" id="charges-div-cont">
                                <input type="number" class="form-control taskName" autocomplete="off" id="charges" readonly
                                       name="charges" value="0">
                                <%--                                <label class="floating-label pickup-placeholder place-holder"--%>
                                <%--                                       for="numPassengers">Charges</label>--%>
                                <label class="always-focus" for="pickupTime">Charges</label>

                                <img src="<c:url value='/resources/images/charges.svg'/>"
                                     class="pickUpimage pickup-placeholder-img">
                            </div>
                            <div class="input-container mt-2 mb-2">
                                <input type="tel" class="form-control taskName" autocomplete="off" id="emergencyContact"
                                       name="emergencyContact" required>
                                <label class="floating-label pickup-placeholder place-holder" for="emergencyContact">Emergency
                                    Number</label>
                                <img src="<c:url value='/resources/images/emergency-contact.svg'/>"
                                     class="pickUpimage pickup-placeholder-img">
                            </div>

                            <div class="form-floating mt-2">
                            <textarea class="form-control" rows="1" placeholder="Leave a comment here"
                                      maxlength="500" id="floatingTextarea"></textarea>
                                <label for="floatingTextarea" class="textarea-package" id="special-instruciton">Special
                                    Instructions(Optional)</label>
                            </div>

                            <div id="package-distance-time-container" class="d-none mt-4">
                                <div id="package-distance">Distance :- <span id="package-dist" class="dynamic-distance-package">--</span></div>
                                <div id="package-ride-time">Estimated time :- <span id="package-time" class="dynamic-time-package">--</span></div>
                            </div>

                            <div class="otp-notes mt-4" id="hide-chages-notes">Charges:- The charges will be calculated
                                as vehicale selected by you (1000-1500 per day) , the Kilometers travelled during the trip per km 2.5 rs.
                            </div>
                            <input type="text" class="d-none" name="validLocation" id="valid-location-package" value="">

                        </div>
                    </form>

                    <form id="rider-form" action="rider-normal-ride-submit" method="post">
                        <div id="ride-form-inner-cont">

                            <div class="land-sub-heading-t2  ">
                                Request a ride , hop in , and go
                            </div>

                            <div class="input-container mt-3 ">
                                <input type="text" class="form-control taskName" autocomplete="off" id="pickup"
                                       name="pickup" required/>
                                <label class="floating-label pickup-placeholder place-holder">Pickup</label>
                                <img src="<c:url value='/resources/images/pickup.svg'/>"
                                     class="pickUpimage pickup-placeholder-img">

                                <div class="search-loader column">
                                    <div class="loader--ripple">
                                        <div></div>
                                        <div></div>
                                    </div>
                                    <span class="loader--title">Getting your live location....</span>
                                </div>
                            </div>

                            <div class="pick-up-go">
                                <img src="<c:url value='/resources/images/pickup-go.svg'/>">
                            </div>
                            <div onclick="setLiveLocation()" id="set-live-location"> Use Live Location</div>
                            <div class="input-container  responsive-img-setting">
                                <input type="text" class="form-control taskName" autocomplete="off" id="dropoff"
                                       name="dropoff" required/>
                                <label class="floating-label place-holder">Drop Off</label>
                                <img src="<c:url value='/resources/images/location-animation.svg'/>"
                                     class="pickUpimage">
                            </div>

                            <div class="land-sub-heading-t2 ">
                                Select a ride
                            </div>
                            <div class="vehicle-cont">
                                <div class="vehicle-row  first" data-vehicle-id="1">
                                    <div class="vehicle-image">
                                        <img src="<c:url value='/resources/images/bike.png'/>">
                                        <div class="vehicle-name-font">
                                            Bike
                                        </div>
                                    </div>
                                    <div class="vehicle-price-text"> Rs. -</div>
                                </div>
                                <div class="vehicle-row  " onclick="setMapDetails()" data-vehicle-id="2">
                                    <div class="vehicle-image">
                                        <img src="<c:url value='/resources/images/rickshaw.png'/>">
                                        <div class="vehicle-name-font">
                                            Rickshaw
                                        </div>
                                    </div>
                                    <div class="vehicle-price-text"> Rs. -</div>
                                </div>
                                <div class="vehicle-row " data-vehicle-id="3">
                                    <div class="vehicle-image">
                                        <img src="<c:url value='/resources/images/car3.png'/>">
                                        <div class="vehicle-name-font">
                                            Car
                                        </div>
                                    </div>
                                    <div class="vehicle-price-text "> Rs. -</div>
                                </div>
                                <div class="vehicle-row last " data-vehicle-id="4">
                                    <div class="vehicle-image ">
                                        <img src="<c:url value='/resources/images/luxury-car3.png'/>">
                                        <div class="vehicle-name-font">
                                            luxury car
                                        </div>
                                    </div>
                                    <div class="vehicle-price-text"> Rs. -</div>
                                </div>
                                <input type="text" name="vehicleId" id="vehicle-id" value="">
                                <input type="text" name="validLocation" id="valid-location" value="">
                            </div>
                            <div id="distance-time-container" class="mt-2">
                                <div id="distance">Distance :- <span class="dynamic-distance">--</span></div>
                                <div id="ride-time">Estimated time :- <span class="dynamic-time">--</span></div>
                            </div>
                        </div>
                    </form>
                    <hr style="margin-bottom: -16px">
                    <button class="btn common-btn-color3-theme mt-3 w-90 container" id="submitBtn" type="submit">Search
                        for a
                        ride
                    </button>


                    <button type="submit" class="btn common-btn-color3-theme package-submit d-none">Book Now
                    </button>

                </div>
                <form id="captain-info">


                    <div class="captain-info-nav">
                        <div class="captain-info-profile">
                            <img src="<c:url value='/resources/images/profile-logo.svg' />" alt="">
                        </div>
                        <div class="captain-info-discription">
                            <div class="captain-info-name">
                                Your Captain is on the way......
                            </div>
                            <div class="ratings-star">

                                <div class="captain-org-name">
                                    Ramesh Kumar
                                </div>


                                <div id="rating-system">
                                    <!-- Display Stars -->
                                    <div class="d-flex gap-1">
                                        <svg class="star" data-index="0" xmlns="http://www.w3.org/2000/svg"
                                             viewBox="0 0 24 24">
                                            <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/>
                                        </svg>
                                        <svg class="star" data-index="1" xmlns="http://www.w3.org/2000/svg"
                                             viewBox="0 0 24 24">
                                            <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/>
                                        </svg>
                                        <svg class="star" data-index="2" xmlns="http://www.w3.org/2000/svg"
                                             viewBox="0 0 24 24">
                                            <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/>
                                        </svg>
                                        <svg class="star" data-index="3" xmlns="http://www.w3.org/2000/svg"
                                             viewBox="0 0 24 24">
                                            <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/>
                                        </svg>
                                        <svg class="star" data-index="4" xmlns="http://www.w3.org/2000/svg"
                                             viewBox="0 0 24 24">
                                            <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/>
                                        </svg>
                                        <svg xmlns="http://www.w3.org/2000/svg" width="0" height="0">
                                            <defs>
                                                <linearGradient id="half-filled-gradient">
                                                    <stop offset="50%" stop-color="#f59e0b"/>
                                                    <stop offset="50%" stop-color="gray"/>
                                                </linearGradient>
                                            </defs>
                                        </svg>
                                    </div>


                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="captain-info-inner-cont" class="container">

                        <div class="captain-info-cont">

                            <div class="land-sub-heading-t2 ">
                                OTP
                            </div>
                            <div class="captain-info-otp-field w-full">
                                <div class="otp-wrapper mb-6 grid grid-cols-4 gap-4 w-full">
                                    <input type="text" class="otp-input text-[32px] text-center form-input w-full"
                                           placeholder="0" readonly>
                                    <input type="text" class="otp-input text-[32px] text-center form-input w-full"
                                           placeholder="0" readonly>
                                    <input type="text" class="otp-input text-[32px] text-center form-input w-full"
                                           placeholder="0" readonly>
                                    <input type="text" class="otp-input text-[32px] text-center form-input w-full"
                                           placeholder="0" readonly>
                                </div>
                            </div>
                        </div>
                        <div class="otp-notes">Note:- Please provide this OTP to the captain to confirm the ride.
                        </div>
                        <div class="captain-info-rout-info-cont">
                            <div class="vehicle-number-text">
                                Vehicle Number :-
                            </div>
                            <div class="vehicle-number">
                                AD23AD1234
                            </div>
                        </div> <div class="captain-info-rout-info-cont">
                            <div class="estimated-waiting-time">
                                Estimated Waiting Time :-
                            </div>
                            <div class="cap-estimated-waiting-time">
                                --
                            </div>
                        </div>
                        <div class="captain-info-rout-info-cont">
                            <div class="away">
                                Captain Away :-
                            </div>
                            <div class="cap-estimated-waiting-distance">--</div>
                        </div>
                        <div class="captain-info-rout-info-cont">
                            <div>
                                Contact Info :-
                            </div>
                            <div class="captain-contact">+91 8849430122</div>
                        </div>
                        <div class="help-line">
                            <div>
                                Help line Number :-
                            </div>
                            <div class="captain-contact">+91 8849430122</div>
                        </div>
                        <hr class="hide-hr">
                    </div>
                </form>
                <%--                <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#rating-modal">--%>
                <%--                    Open Rating Modal--%>
                <%--                </button>--%>
            </div>
        </div>

        <div class="div2" id="map">
            <iframe width="100%" height="100%" id="gmap_canvas" src="" frameborder="0" scrolling="no"
                    marginheight="0" marginwidth="0"></iframe>
        </div>


    </div>
</div>

<!------------------------ waiting modal -------------------------------------------------------->
<div class="modal fade" id="waitTingModal" tabindex="-1" aria-labelledby="exampleModalCenterTitle"
     aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form class="cancel-ride-form" id="cancel-ride-form" action="/cancel-ride-submit" method="post">

                <div class="modal-header waiting-modal-header">
                    <h5 class="modal-title" id="exampleModalLongTitle">Please wait for the captain to accept your
                        ride.</h5>
                </div>
                <div class="modal-body">
                    <div class="progress">
                        <div class="progress-bar progress-bar-striped active" role="progressbar"
                             aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%">
                            <span class="sr-only">Waiting....</span>
                        </div>
                    </div>
                    <p class="loading-text">Estimated waiting time : <span id="time-remaining">5:00</span></p>
                    <div>
                        Cancel this ride?
                    </div>
                    <div class="input-container mt-3">
                        <input type="text" class="form-control taskName" autocomplete="off" id="cancel-reason-input"
                               required list="cancelRideList" name="cancelation-reason"/>
                        <div style="display: none; color: red; font-size: 14px;" id="cancel-reason-error">Cancellation
                            reason requred
                        </div>
                        <label class="floating-label pickup-placeholder place-holder">Reason for cancelation</label>
                        <datalist id="cancelRideList">
                            <option value="Ride is too costly">
                            <option value="Already got a ride">
                            <option value="Too much waiting time">
                            <option value="No captains available">
                            <option value="Change the vehicle">
                        </datalist>
                        <img src="<c:url value='/resources/images/cancel-reason.svg'/>"
                             class="pickUpimage pickup-placeholder-img">
                    </div>
                </div>
                <input type="hidden" name="trip-id" id="general-trip-id" value="">
                <div class="modal-footer">
                    <button data-bs-dismiss="modal" style="display: none">Cancel</button>
                    <button class="btn common-btn-color3-theme mt-1 w-100" type="button" id="waiting-mod-cancel-btn">
                        Cancel
                    </button>
                </div>
            </form>

        </div>
    </div>
</div>

<!------------------------ Ratting  modal -------------------------------------------------------->
<div class="modal fade" id="rating-modal" tabindex="-1" aria-labelledby="exampleModalCenterTitle"
     aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content ratting-modal-content ">
            <form id="rating-modal-form-id">
                <div class="captain-info-nav modal-header ratting-modal-header">
                    <div class="captain-info-profile-conclude">
                        <img src="<c:url value='/resources/images/profile-logo.svg' />" alt="">
                    </div>
                    <div class="captain-info-discription">
                        <div class="captain-info-name" id="reached">
                        </div>
                        <div class="ratings-star">
                            <div class="ratting-cap-name">
                                Ramesh Kumar
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-body">
                    <div class="pay-to-captain">
                        Please pay <span class="pay-amount"> 35 Rs. </span> to your Captain.
                    </div>
                    <div class="paywith-cont mt-2">
                        <div class="pay-option mr-2 pay-with-cash rating-active">
                            Pay with cash
                        </div>
                        <div class="pay-option ml-2 pay-with-wallet">
                            Pay with wallet
                        </div>
                    </div>
                    <div class="mt-3" id="payment-field" style="display: none;">
                        <input type="text" class="form-control taskName " autocomplete="off" id="amount" name="amount"
                               readonly>
                        <label class="always-focus">Amount</label>
                        <img src="<c:url value='/resources/images/wallet.svg'/>"
                             class="pickUpimage pickup-placeholder-img">
                        <div class="available-balance mt-1">Available Balance :- Rs. &nbsp; <span
                                class="available-balance-amount"> 00.00 </span></div>
                    </div>
                    <hr>
                    <div class="mb-2">
                        Rating and Feedback
                    </div>
                    <div id="give-rating-system">

                        <div class="ratting-start-allign">
                            <svg class="give-star" data-index="0" xmlns="http://www.w3.org/2000/svg"
                                 viewBox="0 0 24 24">
                                <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/>
                            </svg>
                            <svg class="give-star" data-index="1" xmlns="http://www.w3.org/2000/svg"
                                 viewBox="0 0 24 24">
                                <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/>
                            </svg>
                            <svg class="give-star" data-index="2" xmlns="http://www.w3.org/2000/svg"
                                 viewBox="0 0 24 24">
                                <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/>
                            </svg>
                            <svg class="give-star" data-index="3" xmlns="http://www.w3.org/2000/svg"
                                 viewBox="0 0 24 24">
                                <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/>
                            </svg>
                            <svg class="give-star" data-index="4" xmlns="http://www.w3.org/2000/svg"
                                 viewBox="0 0 24 24">
                                <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/>
                            </svg>
                            <svg xmlns="http://www.w3.org/2000/svg" width="0" height="0">
                                <defs>
                                    <linearGradient id="half-filled-gradient">
                                        <stop offset="50%" stop-color="#f59e0b"/>
                                        <stop offset="50%" stop-color="gray"/>
                                    </linearGradient>
                                </defs>
                            </svg>
                        </div>

                        <div class="">
                            <span id="rating-text-conclude" class="text-muted text-no long-rattings">0 stars</span>
                        </div>
                    </div>

                    <div class="mt-3">Your feedback helps us improve our service.</div>
                    <div class="input-container mt-4 ">
                        <input type="text" class="form-control taskName" autocomplete="off" id="feedback"
                               name="feedback"/>
                        <label class="floating-label place-holder">Ride Feedback</label>
                        <img src="<c:url value='/resources/images/feedback.svg'/>"
                             class="pickUpimage">
                    </div>
                </div>
                <div class="thank-you text-center">Thank you for riding with us!</div>
                <div class="modal-footer">
                    <button class="btn common-btn-color3-theme mt-1 w-100" type="submit"
                            id="ratting-modal-submit-btn">Conclude Ride
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<input type="hidden" name="generalTripDetailsId" id="general-tripdetails-id" value="">


<div class="hs-toast-wrapper  hs-toast-fixed-top " id="example"></div>
<script src="<c:url value="/resources/js/toaster.js"/>"></script>


<script src="<c:url value="https://maps.googleapis.com/maps/api/js?key=AIzaSyDDCIb4xyEV8ok30VlxsidKGHw1NAlrfFM&libraries=places"/>"></script>

<script src="<c:url value="https://code.jquery.com/jquery-3.6.0.min.js"/>"></script>


<%--//web socket------%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"
        integrity="sha512-iKDtgDyTHjAitUDdLljGhenhPwrbBfqTKWO1mkhSFH3A7blITC9MhYon6SjnMhp4o0rADGw9yAC6EW4t5a4K3g=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.6.1/sockjs.min.js"
        integrity="sha512-1QvjE7BtotQjkq8PxLeF6P46gEpBRXuskzIVgjFpekzFVF4yjRgrQvTG1MTOJ3yQgvTteKAcO7DSZI92+u/yZw=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>


<%------------------------------------jquerry  validation--%>
<script src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.20.0/jquery.validate.min.js"/>"></script>

<script src="<c:url value="/resources/js/riderDashboard.js"/>"></script>
<script src="<c:url value="/resources/js/package.js"/>"></script>


</body>


</html>