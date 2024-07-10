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

    <link rel="stylesheet" href="<c:url value="/resources/css/adminDashboard.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/toaster.css" />">

    <!-- Icon -->
    <link rel="icon" href="<c:url value="/resources/images/Icon.png" />" type="image/icon type">


    <!-- font  -->
    <link href="<c:url value="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" />"
          rel="stylesheet">
<body>
<%@include file="../admin/adminNavbar.jsp" %>

<div id="ad-dash-upper" class="">
    <div class="ad-dash-upper-per-cont">
        <div class="counter-part-one">
            <div class="counter" id="counter1" data-target="410">0</div>

        </div>
        <div class="counter-part-two">Total Users</div>

    </div>
    <div class="ad-dash-upper-per-cont">
        <div class="counter-part-one">
            <div class="counter" id="counter2" data-target="410">0</div>

        </div>
        <div class="counter-part-two">General Booking</div>

    </div>
    <div class="ad-dash-upper-per-cont">
        <div class="counter-part-one">
            <div class="counter" id="counter3" data-target="410">0</div>

        </div>
        <div class="counter-part-two">Service Booking</div>

    </div>
    <div class="ad-dash-upper-per-cont">
        <div class="counter-part-one">
            <div class="counter" id="counter4" data-target="410">0</div>

        </div>
        <div class="counter-part-two">Total Success Trip</div>
    </div>

</div>
<hr>

<div id="ad-dash-lower" class="container">
    <div id="ad-dash-support-types">
        <div id="querry" class="querry-active">Querries</div>
        <div id="running">Running</div>
        <div id="completed">Completed</div>
    </div>
    <div id="ad-dash-support-requests-cont" class="mt-3">
        <div id="transaction-container1">
            <div class="noti-container mt-2 mb-2">
                <div class="noti-img-cont">
                    <img src="<c:url value='/resources/images/wallet-white.svg'/>">
                </div>
                <div class="noti-righ-cont">
                    <div class="noti-header">
                        Querry raised by ramesh parameter (Not form this planet)
                    </div>
                    <hr class="hr-in-admin-dash">

                    <div class="user-id">
                        <span>
                            User id :-
                        </span>
                        <span>45</span>
                    </div>
                    <div class="contact-detail">
                        <span>
                            Contact details :-
                        </span>
                        <span>+91 8849430122</span>
                    </div>
                    <div class="querry-type">
                        <span>
                            Support type :-
                        </span>
                        <span>Request</span>
                    </div>
                    <div class="accoutn-type">
                        <span>
                            Account type :-
                        </span>
                        <span>Captain</span>
                    </div>
                    <div class="noti-msg">
                        <span>
                            Message :-
                        </span>
                        I am tryping to do somthingin my dash board bu fsflk sfasfs fsadfsd fdaf afad sfdasfs df asff
                        sdf af afadf adfa fddaf daf d fs afa fafas fsad
                    </div>

                    <div class="noti-time mt-2  ">
                        2:23 23/1/24
                    </div>
                </div>
                <div class="amount-paid">
                    <button>
                        Aquire
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script src="<c:url value="/resources/js/adminDashboard.js"/>"></script>

</html>
