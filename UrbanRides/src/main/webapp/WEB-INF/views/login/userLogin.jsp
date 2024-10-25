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
    <link rel="stylesheet" href="<c:url value="/resources/css/userLogin.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/toaster.css" />">
    <link rel="icon" href="<c:url value="/resources/images/Icon.png" />" type="image/icon type">
    <link href="<c:url value="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" />"
          rel="stylesheet">

</head>
<%@include file="../commonImports/loader.jsp" %>


<body>
<nav id="common-nav-1"
     class="d-flex justify-content-start align-items-center container-fluid fixed-start position-fixed">
    <div class="mx-3 d-flex justify-content-between align-items-center"
         style="color: white; font-size: 24px; position: relative ">
        <div class="back-button" id="back-button">
            <img src="<c:url value='/resources/images/back-arrow.svg'/>" id="back-arrow">
        </div>
        <div>
            Urban Rides
        </div>
    </div>
</nav>
<div id="large-header" class="large-header">
    <canvas id="demo-canvas">
    </canvas>
</div>

<div class="container-fluid d-flex justify-content-center align-items-center  tt">
    <div id="form-setting" class="d-flex justify-content-center">

        <div id="registraion-form-container" class="py-5 ">

            <form class=" d-flex justify-content-center flex-column" method="post" id="myForm"
                  action="user-login-submit">
                <div id="backend-error"></div>
                <div id="registation-lable" class="text-center mt-2 mb-4">
                    Enter Your Email <span class="hide-this">

                </span>
                </div>
                <div class="floating-label-group mt-2">
                    <input type="text" id="email" class="form-control taskName" autocomplete="off" autofocus
                           name="email" required/>
                    <label class="floating-label place-holder">Email</label>
                    <span id="emailError" class="error"></span>

                </div>
                <div class="floating-label-group mt-2">
                    <input type="password" class="form-control taskName" autocomplete="off" id="pass" autofocus
                           name="password" required/>
                    <label class="floating-label place-holder">Password</label>
                    <div style="float: right; position: absolute;top: 49px; right: 0px">
                        <img src="<c:url value='/resources/images/password-eye.svg'/>"
                             id="togglePassword"
                             style="position: absolute; right: 17px; top: -35px; width: 21px; height: 21px;">
                    </div>
                    <span id="passError" class="error"></span>
                </div>


                <div class="floating-label-group mt-2 d-flex justify-content-center ">

                    <button class="btn common-btn-color3-theme mt-2 w-100" id="submitBtn" type="submit">Login
                    </button>
                </div>
            </form>
            <div id="forget-pass-text" class="d-flex flex-column justify-content-center align-item-center mt-3">
                <div class="text-center">
                    <a href="forget-password" class="forget-pass-link">
                        Forget Password ?
                    </a>
                </div>
                <div class="text-center mt-2">
                    <a class="forget-pass-link adjust-in-mobile" data-bs-toggle="modal"
                       data-bs-target="#joinUsModal" role="button">Not a member ? Sign Up!</a>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="joinUsModal" tabindex="-1" aria-labelledby="joinUsModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h5 class="modal-title " id="joinUsModalLabel">Join Urban Rides</h5>
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
<div class="hs-toast-wrapper  hs-toast-fixed-top " id="example"></div>
<script src="<c:url value="/resources/js/toaster.js"/>"></script>
<script src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/gsap/3.9.1/gsap.min.js'/>"></script>
<script src="<c:url value="/resources/js/userRegistrationBackground.js"/>"></script>
<script src="<c:url value="https://code.jquery.com/jquery-3.6.0.min.js"/>"></script>
<script src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.20.0/jquery.validate.min.js"/>"></script>
<script src="<c:url value="/resources/js/userLogin.js"/>"></script>

</body>


</html>
