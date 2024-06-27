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

    <link rel="stylesheet" href="<c:url value="/resources/css/captainPersonalDetails.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/toaster.css" />">

    <!-- Icon -->
    <link rel="icon" href="<c:url value="/resources/images/Icon.png" />" type="image/icon type">


    <!-- font  -->
    <link href="<c:url value="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" />"
          rel="stylesheet">

</head>
<%@include file="../commonImports/loader.jsp" %>


<body>
<div id="large-header" class="large-header">
    <canvas id="demo-canvas">
    </canvas>
</div>

<div class="outer-cont">
    <div class="main-cont">
        <nav class="container-fluid nav-class d-flex justify-content-between align-items-center nav">
            <div class="mx-3 d-flex justify-content-between align-items-center" style="color: white; font-size: 24px; position: relative ">
                <div class="back-button" id="back-button">
                    <img src="<c:url value='/resources/images/back-arrow.svg'/>" id="back-arrow">
                </div>
               <div>
                   Urban Rides
               </div>
                <!-- <img src="../../images/Icon.png" alt="companyLogo" class="mx-2"> -->
            </div>


            <div class="dropdown d-flex flex-row-reverse align-items-center">
                <button class="dropbtn common-btn-color7-theme">Get help</button>
                <div class="dropdown-content">
                    <a href="captain-support" class="dropdown-list " style="text-decoration: none">
                        <img src="<c:url value='/resources/images/help.svg' />" alt="">    </span>
                        Get help with your account</a>
                    <div>
                        <hr class="hr-setting">
                    </div>
                    <a href="user-login" class="dropdown-list ">
                         <img src="<c:url value='/resources/images/sign-out.svg' />" alt=""></span>
                        Sign out
                    </a>
                </div>
            </div>

        </nav>
        <div class="land-sub-heading-t6 ">Welcome , Krutarth</div>
        <div class="container">

            <div class="captain-details-sub mb-2 mt-2">Here's what you need to do to set up your account</div>
            <form id="form-id" method="post" action="captain-personal-details-submit"
                  enctype="multipart/form-data" >



                <div class="upload-file-sub-heading mt-4 mb-2">Profile Photo</div>
                <div class="col-12 mt-1 upload-view-flex d-flex">
                    <div class="input-group">
                        <input id="profile-photo" type="text"
                               class="form-control p-2 verification-files custom-upload"
                               placeholder="Upload in Jpg or Png format" disabled>
                        <button class="file-upload-btn btn-color-for-upload" type="button" id="upload">
                            <img src="<c:url value='/resources/images/upload.svg' />" class="upload-image-file"
                                 alt="">
                            <span class="for-remove-upload ">Upload</span></button>
                        <input id="profile-photo-upload" name="profilePhoto" multiple
                               class="file-input-hover-effect" type="file"
                               style="position: absolute; right: 0px; padding: 8px; width: 75px; opacity: 0;">
                    </div>
                    <button class="verification-files main-container-view-btn profile-photo-view d-none">
                        <div class="view-btn-flex d-flex">
                            <img src="<c:url value='/resources/images/eye-fill_white.svg' />" class="upload-image-file"
                                 alt="">
                            <div class="for-remove-upload ">View</div>
                        </div>
                    </button>
                </div>
                <div class="upload-file-sub-heading mt-4 mb-2">Aadhar Card</div>
                <div class="col-12 mt-1 upload-view-flex d-flex">
                    <div class="input-group">
                        <input id="aadhar-card-name-id" type="text"
                               class="form-control p-2 verification-files custom-upload"
                               placeholder="Upload Front and Back in Pdf" disabled>
                        <button class="file-upload-btn btn-color-for-upload" type="button" id="upload">
                            <img src="<c:url value='/resources/images/upload.svg' />" class="upload-image-file"
                                 alt="">
                            <span class="for-remove-upload ">Upload</span></button>
                        <input id="adhar-card-file-uplaod" name="adharCard" multiple
                               class="file-input-hover-effect" type="file"
                               style="position: absolute; right: 0px; padding: 8px; width: 75px; opacity: 0;">
                    </div>
                    <button class="verification-files main-container-view-btn aadhar-card-view d-none">
                        <div class="view-btn-flex d-flex">
                            <img src="<c:url value='/resources/images/eye-fill_white.svg' />" class="upload-image-file"
                                 alt="">
                            <div class="for-remove-upload ">View</div>
                        </div>
                    </button>
                </div>
                <div class="upload-file-sub-heading mt-4 mb-2">
                    <span>Driving Licence </span>
                    <span class="i-class">
                <img src="<c:url value='/resources/images/info-circle.svg' />" alt=""></span>
                </div>
                <div class="col-12 mt-1 upload-view-flex d-flex">
                    <div class="input-group">
                        <input id="driving-license" type="text"
                               class="form-control p-2 verification-files custom-upload"
                               placeholder="Upload Front and Back" disabled>
                        <button class="file-upload-btn btn-color-for-upload" type="button" id="upload">
                            <img src="<c:url value='/resources/images/upload.svg' />" class="upload-image-file"
                                 alt="">
                            <span class="for-remove-upload ">Upload</span></button>
                        <input id="driving-license-file-upload" name="drivingLicense" multiple
                               class="file-input-hover-effect" type="file"
                               style="position: absolute; right: 0px; padding: 8px; width: 75px; opacity: 0;">
                    </div>
                    <button class="verification-files main-container-view-btn driving-licence-view d-none">
                        <div class="view-btn-flex d-flex">
                            <img src="<c:url value='/resources/images/eye-fill_white.svg' />" class="upload-image-file"
                                 alt="">
                            <div class="for-remove-upload ">View</div>
                        </div>
                    </button>
                </div>
                <div class="upload-file-sub-heading mt-4 ">Registration Certificate (RC)</div>
                <div class="col-12 mt-1 upload-view-flex d-flex">
                    <div class="input-group">
                        <input id="registration-certificate" type="text"
                               class="form-control p-2 verification-files custom-upload"
                               placeholder="Upload in Png Format(Only Front)" disabled>
                        <button class="file-upload-btn btn-color-for-upload" type="button" id="upload">
                            <img src="<c:url value='/resources/images/upload.svg' />" class="upload-image-file"
                                 alt="">
                            <span class="for-remove-upload ">Upload</span></button>
                        <input id="registration-certificate-upload" name="registrationCertificate" multiple
                               class="file-input-hover-effect" type="file"
                        >
                    </div>
                    <button class="verification-files main-container-view-btn registration-certificate-view d-none">
                        <div class="view-btn-flex d-flex">
                            <img src="<c:url value='/resources/images/eye-fill_white.svg' />" class="upload-image-file"
                                 alt="">
                            <div class="for-remove-upload ">View</div>
                        </div>
                    </button>
                </div>

                <div class="row mt-2">
                    <div class="floating-label-group col-12 col-sm-6">
                        <div class="upload-file-sub-heading mt-2 mb-1">RC Expiration Date</div>
                        <input type="date" id="rcExpiration" class="form-control taskName" autocomplete="off" autofocus
                               name="rcExpiration" required/>
                    </div>
                    <div class="floating-label-group col-12 col-sm-6">
                        <div class="upload-file-sub-heading mt-2 mb-1">License Expiration Date</div>
                        <input type="date" id="licenseExpiration" class="form-control taskName" autocomplete="off"
                               autofocus
                               name="licenseExpiration" required/>
                    </div>
                </div>


                <div class="mt-4 mb-1">
                    <hr style="box-shadow: 0px 0px 8px grey">
                </div>

                <button class="btn common-btn-color3-theme mb-3 w-100" id="submitBtn" type="submit">Submit
                </button>

            </form>
        </div>
        <div class="hs-toast-wrapper  hs-toast-fixed-top " id="example"></div>
        <script src="<c:url value="/resources/js/toaster.js"/>"></script>
    </div>
</div>
<script src="<c:url value="https://code.jquery.com/jquery-3.6.0.min.js"/>"></script>
<script src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.20.0/jquery.validate.min.js"/>"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.20.0/jquery.validate.min.js"></script>
<script src="<c:url value="/resources/js/captainPersonalDetails.js"/>"></script>


<%--<!-- Include GSAP library -->--%>
<script src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/gsap/3.9.1/gsap.min.js'/>"></script>
<script src="<c:url value="/resources/js/userRegistrationBackground.js"/>"></script>
</body>
</html>
