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
    <link rel="stylesheet" href="<c:url value="/resources/css/captainDashboard.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/toaster.css" />">
    <link rel="icon" href="<c:url value="/resources/images/Icon.png" />" type="image/icon type">
    <link href="<c:url value="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" />"
          rel="stylesheet">

</head>
<body>
<%@include file="../commonImports/loader.jsp" %>
<%@include file="../captain/captainNavbar.jsp" %>

<div id="captain-dash-main-cont">
    <div class="captain-dash-upper" id="map">

    </div>
    <div id="captain-dash-lower">
        <input type="hidden" id="refresh-trip-id" value="no-data">
        <input type="hidden" id="refresh-again" value="no-active-ride">
        <div id="dash-first-ui">

            <div class="dashboard-label">Welcome, Captain!</div>

            <p>
                As a captain, your role is to provide safe and reliable rides to our passengers. Here's how you can get
                started:
            </p>
            <div class="dashboard-label">How to Search for Riders:</div>
            <ul>
                <li>Check the map above for available ride requests in your area.</li>
                <li>Each request will show the rider's location and destination.</li>
                <li>Click on a ride request to view more details.</li>
            </ul>
            <div class="dashboard-label">How to Accept a Ride:</div>
            <ul>
                <li>Review the details of the ride request, including the pickup and drop-off locations.</li>
                <li>If you're ready to accept the ride, click the "Accept" button.</li>
                <li>Once accepted, you'll receive the rider's contact information and detailed instructions.</li>
            </ul>
            <p>
                Remember to maintain a high standard of service and ensure the safety and comfort of your passengers.
                Thank
                you for being a part of our team!
            </p>
            <p>
                If you have any questions or need assistance, please contact support.
            </p>

            <button id="getLocationBtn">Give Live Location Permission</button>

        </div>


        <div class="dashboard-label text-center d-none" id="rider-location-text">Proceed to Rider's Location :</div>
        <div id="captain-rider-info-cont" class="d-none">
            <div id="rider-info-cont">
                <div class="rider-info-img">
                    <img src="<c:url value='/resources/images/profile-logo.svg' />" alt="">    </span>
                </div>
                <div class="rider-info-name ">
                    <div class="rider-name"> Ramewsh Pratap</div>
                    <div class="rider-contact">
                        +91 <span class="rider-phone">8849439122 </span>
                    </div>
                </div>

            </div>
            <div class="p-2">

                <div class="rider-info-details-cont">
                    <div class="rider-info-details-left">
                        Pick up location :-
                    </div>
                    <div class="rider-info-details-right" id="rider-info-details-pickUp">

                        rajkot
                    </div>
                </div>
                <div class="rider-info-details-cont">
                    <div class="rider-info-details-left">
                        Drop off location :-
                    </div>
                    <div class="rider-info-details-right" id="rider-info-details-dropOff">
                        rajkot
                    </div>
                </div>
                <div class="rider-info-details-cont">
                    <div class="rider-info-details-left">
                        Rider distance away :-
                    </div>
                    <div class="rider-info-details-right" id="rider-distance-info">
                        23 Km
                    </div>
                </div>
                <div class="rider-info-details-cont">
                    <div class="rider-info-details-left">
                        Estimated time to reach rider :-
                    </div>
                    <div class="rider-info-details-right" id="rider-time-info">
                        35 Min
                    </div>
                </div>
                <div class="rider-info-details-cont">
                    <div class="rider-info-details-left">
                        Charges :-
                    </div>
                    <div class="rider-info-details-right" id="rider-info-details-charges">
                        45 Rs
                    </div>
                </div>

                <hr>
                <input type="hidden" value="" id="tripIdForOtp">
                <input type="hidden" value="" id="riderPickup">
                <input type="hidden" value="" id="riderDropOff">
                <div id="otp-form-id" class="d-none">

                    <div class="dashboard-label-otp text-center">Verify Rider with OTP:</div>

                    <form id="otpForm" action="captain-otp-submit" method="post">
                        <div class="input-container">
                            <input type="text" class="form-control taskName mt-1 " autocomplete="off"
                                   id="opt"
                                   name="captainOtp" required>
                            <label class="floating-label place-holder" for="opt">otp
                            </label>
                        </div>
                        <p class="mt-3">
                            Once you reach the rider's location, please verify their identity by entering the OTP
                            (One-Time
                            Password) provided by the rider.
                        </p>
                        <button type="submit" class="captain-accept-btn mt-2 mb-2">Submit OTP</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="completionModal" tabindex="-1" aria-labelledby="completionModalLabel" aria-hidden="true"
         data-bs-backdrop="static" data-bs-keyboard="false">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header modal-header-bg">
                    <h5 class="modal-title" id="completionModalLabel">The Ride is Completed</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p>Rider has to pay you <span id="charges-in-pop-up"> 43 Rs</span> for the ride. Please ensure the
                        payment is collected before concluding
                        the ride.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-success" id="concludeRideBtn">Conclude Ride</button>
                </div>
            </div>
        </div>
    </div>


</div>
</div>
<div class="hs-toast-wrapper  hs-toast-fixed-top " id="example"></div>
<script src="<c:url value="/resources/js/toaster.js"/>"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"
        integrity="sha512-iKDtgDyTHjAitUDdLljGhenhPwrbBfqTKWO1mkhSFH3A7blITC9MhYon6SjnMhp4o0rADGw9yAC6EW4t5a4K3g=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.6.1/sockjs.min.js"
        integrity="sha512-1QvjE7BtotQjkq8PxLeF6P46gEpBRXuskzIVgjFpekzFVF4yjRgrQvTG1MTOJ3yQgvTteKAcO7DSZI92+u/yZw=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.20.0/jquery.validate.min.js"></script>
<script src="<c:url value="https://maps.googleapis.com/maps/api/js?key=AIzaSyDDCIb4xyEV8ok30VlxsidKGHw1NAlrfFM&libraries=places"/>"></script>
<script src="<c:url value="/resources/js/captain-web-socket.js"/>"></script>

<script src="<c:url value="/resources/js/captainDashboard.js"/>"></script>

</body>
</html>
