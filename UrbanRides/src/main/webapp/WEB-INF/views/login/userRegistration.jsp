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
    <link rel="stylesheet" href="<c:url value="/resources/css/userRegistration.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/toaster.css" />">
    <link rel="icon" href="<c:url value="/resources/images/Icon.png" />" type="image/icon type">
    <link href="<c:url value="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" />"
          rel="stylesheet">
</head>
<body>
<%@include file="../commonImports/loader.jsp" %>
<nav id="common-nav-1"
     class="d-flex justify-content-start align-items-center container-fluid fixed-start position-fixed">
    <div class="mx-3" style="color: white; font-size: 24px;">
        Urban Rides
    </div>
</nav>
<div id="large-header" class="large-header">
    <canvas id="demo-canvas">
    </canvas>
</div>
<div class="container-fluid d-flex justify-content-center align-items-center  tt">
    <div id="form-setting" class="d-flex justify-content-center ">
        <div class="back-button" id="back-button">
            <img src="<c:url value='/resources/images/back-arrow.svg'/>" id="back-arrow">
        </div>
        <div id="registraion-form-container" class="py-5 ">

            <form class=" d-flex justify-content-center flex-column" id="myForm">
                <div id="backend-error"></div>
                <input type="hidden" name="accountType" id="accountType" value="${accountType}"/>
                <div id="registation-lable" class="text-center mt-4 mb-4">
                    What's your Email Id ?<span class="hide-this">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            </span>
                </div>
                <div class="floating-label-group mt-2">
                    <input type="text" id="email" class="form-control taskName" autocomplete="off" autofocus
                           name="email" required/>
                    <label class="floating-label place-holder">Email</label>
                    <span id="emailError" class="error">Please enter a valid Email.</span>
                </div>
                <div class="floating-label-group mt-2" style="position: relative">
                    <input type="password" class="form-control taskName" autocomplete="off" id="pass" autofocus
                           name="password" required/>
                    <label class="floating-label place-holder">Password</label>
                    <span id="passError" class="error">Password length should be between 8 and 16 characters.</span>
                    <div style="position: absolute; right: 0px ; top: 50px">
                        <img src="<c:url value='/resources/images/password-eye.svg'/>"
                             id="togglePassword2"
                             style="position: absolute; right: 17px; bottom: 15px; width: 21px; height: 21px;">
                    </div>
                </div>
                <div class="floating-label-group mt-2">
                    <input type="password" class="form-control taskName" autocomplete="off" id="confPass" autofocus
                           name="confPass" required/>
                    <div style="float: right; position: relative;">
                        <img src="<c:url value='/resources/images/password-eye.svg'/>"
                             id="togglePassword"
                             style="position: absolute; right: 17px; bottom: 15px; width: 21px; height: 21px;">
                    </div>
                    <label class="floating-label place-holder">Confirm Password</label>
                    <span id="confPassword" class="error">Password length should be between 8 and 16 characters.</span>
                    <span class="error" id="confPassError"> Password and Confirmed doesn't matched.</span>
                </div>
                <div class="floating-label-group mt-2">
                    <div class="input-group">
                        <input type="text" class="form-control taskName" disabled autocomplete="off" id="otp" name="otp"
                               required/>
                        <div class="input-group-append">
                            <button class="btn common-btn-color3-theme w-100 get-otp-btn d-flex justify-content-center align-items-center"
                                    onclick="getOtp()" id="get-otp-btnn" type="submit">Get Otp
                            </button>
                        </div>
                        <label class="floating-label">Otp</label>
                    </div>
                    <div id="otpError" class="error">Otp is 4 Digit.</div>
                </div>
                <div class="floating-label-group mt-2 d-flex justify-content-center ">
                    <button class="btn common-btn-color3-theme mt-2 w-100" id="submitBtn" type="submit">Register
                    </button>
                </div>
            </form>
            <div id="forget-pass-text" class="d-flex justify-content-center mt-3">
                <a href="${pageContext.request.contextPath}/user-login" class="forget-pass-link">
                    Already a member ? Log in
                </a>
            </div>
        </div>
    </div>
</div>
<div class="hs-toast-wrapper  hs-toast-fixed-top " id="example"></div>
<script src="<c:url value="/resources/js/toaster.js"/>"></script>
<script src="<c:url value="/resources/js/userRegistration.js"/>"></script>
<script src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/gsap/3.9.1/gsap.min.js'/>"></script>
<script src="<c:url value="/resources/js/userRegistrationBackground.js"/>"></script>
</body>
</html>
