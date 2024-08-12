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


    <!-- Add custom CSS file (replace 'landingPage.css' with your CSS file name) -->

    <link rel="stylesheet" href="<c:url value="/resources/css/landingPage.css" />">

    <!-- Icon -->
    <link rel="icon" href="<c:url value="/resources/images/Icon.png" />" type="image/icon type">

    <%--linking js file--%>
    <script src="<c:url value="../../resources/js/landingPage.js" />"></script>


    <!-- font  -->
</head>

<body>
<nav class="navbar navbar-expand-md navbar-light fixed-top position-absolute" id="navId">
    <div class="container">
        <a class="navbar-brand" href="/UrbanRides"> <img src="<c:url value='/resources/images/Icon.png'/>" alt=" ">
            <span class="urban-rides1">Urban Rides</span><span class="urban-rides2">UB</span>
        </a>


        <div class="offcanvas offcanvas-end" tabindex="-1" id="offcanvasNavbar" aria-labelledby="offcanvasNavbarLabel">
            <!-- <div class="offcanvas-header">
              <h5 class="offcanvas-title" id="offcanvasNavbarLabel">Urban Rides</h5>
              <button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas" aria-label="Close"></button>
            </div> -->
            <div class="offcanvas-body">
                <ul class="navbar-nav justify-content-center flex-grow-1 pe-3">
                    <li class="nav-item">
                        <a class="nav-link  nav-text " aria-current="page" href="#carouselExampleIndicators">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link nav-text" href="#land-services">Our Services</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link nav-text" href="#land-career">Career</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link nav-text" href="#land-partner">Partner</a>
                    </li>


                    <li class="nav-item">
                        <a class="nav-link nav-text adjust-in-pc" data-bs-toggle="modal" data-bs-target="#joinUsModal">Sign
                            Up</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link nav-text adjust-in-pc" href="${pageContext.request.contextPath}/user-login">Login</a>
                    </li>


                </ul>
            </div>
        </div>
        <div class="login-btns-setting">

            <button type="button" class=" common-btn-color7-theme adjust-in-mobile " data-bs-toggle="modal"
                    data-bs-target="#joinUsModal">Sign Up
            </button>
            <a href="${pageContext.request.contextPath}/user-login">
                <button type="button" class="common-btn-color7-theme adjust-in-mobile">Login</button>
            </a>


            <button class="navbar-toggler cross-shrik" type="button" data-bs-toggle="offcanvas"
                    data-bs-target="#offcanvasNavbar" aria-controls="offcanvasNavbar">
                <svg id="hamburger" class="Header__toggle-svg" viewbox="0 0 60 40">
                    <g stroke="#fff" stroke-width="4" stroke-linecap="round" stroke-linejoin="round">
                        <path id="top-line" d="M10,10 L50,10 Z"></path>
                        <path id="middle-line" d="M10,20 L50,20 Z"></path>
                        <path id="bottom-line" d="M10,30 L50,30 Z"></path>
                    </g>
                </svg>
            </button>
        </div>
    </div>
</nav>

<%---------------------------modal 1 Sign up ------------------%>
<div class="modal fade" id="joinUsModal" tabindex="-1" aria-labelledby="joinUsModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h5 class="modal-title " id="joinUsModalLabel">Join Urban Rides</h5>
                <%--                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>--%>
            </div>
            <div class="modal-body">
                <p class="land-sub-heading-t2">Select account to open</p>
                <div class="land-pop-login-btns-align  ">

                    <a type="button" class="btn common-btn-color3-theme"
                       href="${pageContext.request.contextPath}/user-registration/3">Sign
                        up as a Rider
                    </a>
                    <a type="button" class="btn common-btn-color8-theme"
                       href="${pageContext.request.contextPath}/user-registration/2">
                        Sign up as a Captain
                    </a>
                </div>

            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="riderModal" tabindex="-1" aria-labelledby="riderModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="riderModalLabel">Sign up as a Rider</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <!-- Rider sign-up form goes here -->
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="captainModal" tabindex="-1" aria-labelledby="captainModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="captainModalLabel">Sign up as a Captain</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <!-- Captain sign-up form goes here -->
            </div>
        </div>
    </div>
</div>


<div id="carouselExampleIndicators" class=" carousel-fullscreen carousel slide carousel-background"
     data-bs-ride="carousel">
    <div class="carousel-indicators">
        <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active"
                aria-current="true" aria-label="Slide 1"></button>
        <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1"
                aria-label="Slide 2"></button>
        <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2"
                aria-label="Slide 3"></button>
    </div>
    <div class="carousel-inner">
        <!-- Slide 1 -->
        <div class="carousel-item active">
            <img src="<c:url value='/resources/images/casole12.jpg'/>" class="d-block w-100 carousel-images"
                 alt="First slide">
            <div class="carousel-caption ">
                <h5 class="carousel-caption1">Book A Fastest Ride Now</h5>
                <p class="carousel-caption2">Get in gear and join the ride! Login now to revoutionize your cab
                    management experience.</p>

                <p><a class="btn btn-primary" href="user-registration/3" role="button">Join Now</a></p>
            </div>
        </div>
        <!-- Slide 2 -->
        <div class="carousel-item">
            <img src="<c:url value='/resources/images/casole22.jpg'/>" class="d-block w-100 carousel-images"
                 alt="Second slide">
            <div class="carousel-caption ">
                <h5 class="carousel-caption1">Reliable Taxi Service</h5>
                <p class="carousel-caption2">"Where Every Mils Counts. Join Now For A Seamless Rides!".</p>
            </div>
        </div>
        <!-- Slide 3 -->
        <div class="carousel-item">
            <img src="<c:url value='/resources/images/casole33.jpg'/>" class="d-block w-100 carousel-images"
                 alt="Third slide">
            <div class="carousel-caption ">
                <h5 class="carousel-caption1">Welcome</h5>
                <h2 class="carousel-caption2">
                    " Experience Reliability , Convinience , and comfort with our cab management system. Login Now to
                    begin your journey."
                </h2>
                <a href="#land-partner" class="btn common-btn-color4-theme mt-3">Our Partner</a></div>
        </div>
    </div>
    <button class="carousel-control-prev btn-disappear" type="button" data-bs-target="#carouselExampleIndicators"
            data-bs-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Previous</span>
    </button>
    <button class="carousel-control-next btn-disappear" type="button" data-bs-target="#carouselExampleIndicators"
            data-bs-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Next</span>
    </button>
</div>


<section class="our-services mt-5" id="land-services">
    <div class="container">
        <div class="land-sub-heading-t1">
            OUR SERVICES
        </div>
        <div class="land-sub-heading-t2 mt-2">
            We are Always There at Your Services
        </div>
        <div class="row mt-5">

            <div class="col-md-4 mb-3">
                <div class="card border-0">
                    <img src="<c:url value='/resources/images/taxi-rent1.png'/>"
                         class="card-img-top img-fluid mx-auto d-block "
                         style="width: 50%"
                         alt="...">
                    <div class="card-body text-center">
                        <h5 class="card-title">Rent a Taxi</h5>
                        <p class="card-text land-sub-heading-t2">Rent a Taxi for more than a day for longer Journey and
                            Trips.</p>
                        <a href="${pageContext.request.contextPath}/user-login" class="btn common-btn-color3-theme">Book
                            Ride Now</a>
                    </div>
                </div>
            </div>

            <div class="col-md-4 mb-3">
                <div class="card border-0">
                    <img src="<c:url value='/resources/images/taxi-aeroplane2.png'/>"
                         class="card-img-top img-fluid mx-auto d-block  "
                         style="width: 50%" alt="...">
                    <div class="card-body text-center">
                        <h5 class="card-title">Airport Transport </h5>
                        <p class="card-text land-sub-heading-t2">Comfortable transfer services from airport to any
                            chosen address.
                        </p>
                        <a href="${pageContext.request.contextPath}/user-login" class="btn common-btn-color3-theme">Book
                            Service Now</a>
                    </div>
                </div>
            </div>

            <div class="col-md-4 mb-3">
                <div class="card border-0">
                    <img src="<c:url value='/resources/images/daily-pickup1.png'/>"
                         class="card-img-top img-fluid mx-auto d-block "
                         style="width: 50%"
                         alt="...">
                    <div class="card-body text-center">
                        <h5 class="card-title">Daily Pickup</h5>
                        <p class="card-text land-sub-heading-t2">Daily Scheduled transfer service for a fixed route.</p>
                        <a href="${pageContext.request.contextPath}/user-login"
                           class="btn common-btn-color3-theme mt-3">Book Service Now</a>
                    </div>
                </div>
            </div>

        </div>
    </div>
</section>


<section class="land-career mt-3 py-4" id="land-career">
    <div class="container ">
        <div class="land-sub-heading-t1 text-center"> JOIN OUR TEAM AND MAKE A DIFFERENCE TODAY</div>
        <div class="land-sub-heading-t4 mt-2 text-center"> In cooperation with our Transfer Service Network we can
            provide all shorts
            of Transfers. Call to our team on the phone +91 9418891282 and you will be
            get a call back.
        </div>
        <div class="row mt-5">
            <div class="col-md-4 mb-3">
                <div class="card"><img src="<c:url value='/resources/images/career-wallet.png'/>"
                                       class="card-img-top img-fluid mx-auto d-block my-4"
                                       style="width: 30%" alt="...">
                    <div class="card-body text-center">
                        <h5 class="card-title">Reliable Earning</h5>
                        <p class="card-text land-sub-heading-t2 mb-4 mt-2">Latest Model Vehicles for Groups, Families
                            and Limousines for our
                            VIP guests..</p>
                        <!-- <a href="#" class="btn common-btn-color3-theme">Book Ride Now</a> -->
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-3">
                <div class="card"><img src="<c:url value='/resources/images/career-flexibility.png'/>"
                                       class="card-img-top img-fluid mx-auto d-block my-4" style="width: 30%" alt="...">
                    <div class="card-body text-center">
                        <h5 class="card-title">Flexibility </h5>
                        <p class="card-text land-sub-heading-t2 mb-4 mt-2">Feel free to contact us and we will provide a
                            stress-free
                            Transfer to the location you want..
                            <!-- </p> <a href="#" class="btn common-btn-color3-theme">Book Service Now</a> -->
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-3">
                <div class="card"><img src="<c:url value='/resources/images/career-safety.png'/>"
                                       class="card-img-top img-fluid mx-auto d-block my-4"
                                       style="width:30%" alt="...">
                    <div class="card-body text-center">
                        <h5 class="card-title">Driving Safety</h5>
                        <p class="card-text land-sub-heading-t2 mb-4 mt-2">Over 15 years experience guarantee a perfect
                            service, top
                            conditions and best prices.</p>
                        <!-- <a href="#" class="btn common-btn-color3-theme mt-3">Book Service Now</a> -->
                    </div>
                </div>
            </div>

        </div>
        <div class="d-flex justify-content-center "><a href="${pageContext.request.contextPath}/user-registration/2"
                                                       class="btn common-btn-color4-theme mt-3">Become a
            driver</a>
        </div>
    </div>
</section>

<section class="land-partner mt-3 py-4" id="land-partner">
    <div class="container">
        <div class="land-sub-heading-t6 text-center"> OUR PARTNERS</div>
        <div class="row">
            <div class="partner-card col-6 col-md-3  col-sm-6">
                <img src="<c:url value='/resources/images/company1.svg'/>"
                     class="card-img-top img-fluid mx-auto d-block my-4 partners-img-class"
                     alt="Righ Choice">
                <div class="hover-caption text-center text-dark gray-bg land-sub-heading-t2">Righ Choice</div>
            </div>

            <div class="partner-card col-6 col-md-3  col-sm-6">
                <img src="<c:url value='/resources/images/company2.svg'/>"
                     class="card-img-top img-fluid mx-auto d-block my-4 partners-img-class"
                     alt="priter litre">
                <div class="hover-caption text-center text-dark gray-bg land-sub-heading-t2">Priter litre</div>
            </div>

            <div class="partner-card col-6 col-md-3  col-sm-6">
                <img src="<c:url value='/resources/images/company3.svg'/>"
                     class="card-img-top img-fluid mx-auto d-block my-4 partners-img-class"
                     alt="International">
                <div class="hover-caption text-center text-dark gray-bg land-sub-heading-t2">International</div>
            </div>

            <div class="partner-card col-6 col-md-3  col-sm-6">
                <img src="<c:url value='/resources/images/company4.svg'/>"
                     class="card-img-top img-fluid mx-auto d-block my-4 partners-img-class"
                     alt="FedEx">
                <div class="hover-caption text-center text-dark gray-bg land-sub-heading-t2">FedEx</div>
            </div>
        </div>
    </div>
</section>
<%--<hr class="footer-hr">--%>

<footer class="footer-container">

    <div class="footer-text col">
        <p class="urban-rides">Urban Rides</p>
        <p class="copyright-text">&copy; All Rights Reserved.</p>
    </div>
    <div class="social-links ">
        <a href="https://www.facebook.com/" target="_blank">Facebook</a>
        <a href="https://www.twitter.com/" target="_blank">Twitter</a>
        <a href="https://www.linkedin.com/" target="_blank">Linkdin</a>
    </div>
</footer>
</body>

</html>