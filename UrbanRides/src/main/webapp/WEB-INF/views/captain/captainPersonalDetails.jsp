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
    <link rel="stylesheet" href="<c:url value="/resources/css/riderPersonalDetails.css" />">
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
        <!-- <img src="../../images/Icon.png" alt="companyLogo" class="mx-2"> -->
    </div>
    <!-- other elements -->
</nav>
<div id="large-header" class="large-header">
    <canvas id="demo-canvas">
    </canvas>
</div>

<div class="container-fluid d-flex justify-content-center align-items-center  tt">
    <div id="form-setting" class="d-flex justify-content-center">
        <div class="back-button" id="back-button">
            <img src="<c:url value='/resources/images/back-arrow.svg'/>" id="back-arrow">
        </div>

        <div id="registraion-form-container" class="py-5 ">
            <form class=" d-flex justify-content-center flex-column" id="myForm" method="post"
                  action="captain-personal-details-submit">
                <div id="backend-error"></div>
                <div id="registation-lable" class="text-center mt-1 mb-4">
                    User Personal Details
                </div>
                <div class="floating-label-group mt-2">
                    <input type="text" id="fName" class="form-control taskName" autocomplete="off" autofocus
                           name="riderFirstName" required/>
                    <label class="floating-label place-holder">First Name</label>
                    <span id="fNameError" class="error"></span>
                </div>
                <div class="floating-label-group mt-2">
                    <input type="text" id="lName" class="form-control taskName" autocomplete="off" autofocus
                           name="riderLastName" required/>
                    <label class="floating-label place-holder">Last Name</label>
                    <span id="lNameError" class="error"></span>
                </div>
                <div class="floating-label-group mt-2">
                    <input type="text" id="phone" class="form-control taskName" autocomplete="off" autofocus
                           name="phone" required/>
                    <label class="floating-label place-holder">Phone </label>
                    <span id="phoneError" class="error"></span>
                </div>
                <div class="floating-label-group mt-2">
                    <input type="text" id="age" class="form-control taskName" autocomplete="off" autofocus
                           name="age" required/>
                    <label class="floating-label place-holder">Your Age </label>
                    <span id="ageError" class="error"></span>
                </div>
                <div class="floating-label-group mt-2 ">
                    <div class="d-flex col mb-2">
                        <input type="checkbox" name="termsConditons" required> &nbsp;
                        <div id="error-ignore">
                            I Accpet terms and conditions.
                        </div>
                    </div>
                    <span id="checkBoxerror" class="error"></span>
                </div>
                <div class="floating-label-group mt-2 d-flex justify-content-center ">
                    <button class="btn common-btn-color3-theme mt-2 w-100" id="submitBtn" type="submit"
                    >Submit
                    </button>
                </div>
                <p id="location"></p>

            </form>
        </div>
    </div>
</div>

<div class="hs-toast-wrapper  hs-toast-fixed-top " id="example"></div>
<script src="<c:url value="/resources/js/toaster.js"/>"></script>

<script src="<c:url value="https://code.jquery.com/jquery-3.6.0.min.js"/>"></script>
<script src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.20.0/jquery.validate.min.js"/>"></script>
<script src="<c:url value="/resources/js/captainPersonalDetails.js"/>"></script>

<script src="<c:url value="/resources/js/captain-web-socket.js"/>"></script>

<%--<!-- Include GSAP library -->--%>
<script src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/gsap/3.9.1/gsap.min.js'/>"></script>
<script src="<c:url value="/resources/js/userRegistrationBackground.js"/>"></script>


</body>


</html>
