<%--
  Created by IntelliJ IDEA.
  User: pci221
  Date: 02-07-2024
  Time: 10:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<nav class="container-fluid nav-class d-flex justify-content-between align-items-center fixed-top">
    <!-- nav content -->
    <div class="mx-3 d-flex justify-content-between align-items-center"
         style="color: white; font-size: 24px; position: relative ">
        <div class="back-button" id="back-button">
            <img src="<c:url value='/resources/images/back-arrow.svg'/>" id="back-arrow">
        </div>
        <div>
            Urban Rides
        </div>
    </div>

    <div class="nav-right-side">


        <a href="rider-dashboard" style="text-decoration: none" class="common-btn-color7-theme nav-my-trip-cont">
            <div class="nav-my-trip-img">
                <img src="<c:url value='/resources/images/home.svg' />" alt="">
            </div>
            <div class="nav-my-trip">
                Home
            </div>
        </a>
        <a href="rider-notifications" style="text-decoration: none" class="nati-cont">
            <img src="<c:url value='/resources/images/notification1.svg' />" alt="">
        </a>
        <div class="dropdown d-flex flex-row-reverse align-items-center">
            <button class="dropbtn ">
                <div class="profile-pic">
                    <c:choose>
                        <c:when test="${riderSessionObj.profilePhoto eq 1}">
                            <img src="<c:url value='${riderSessionObj.profileLoc}' />" alt="">
                        </c:when>
                        <c:otherwise>
                            <img src="<c:url value='/resources/images/profile-logo.svg' />" alt="">
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="profile-pic-side-dropdown">
                    <img src="<c:url value='/resources/images/down-arrow.svg' />" alt="">
                </div>
            </button>

            <div class="dropdown-content">

                <a href="rider-notifications" class="dropdown-list show-mobile" style="text-decoration: none">
                    <img src="<c:url value='/resources/images/notification1.svg' />" alt="">    </span>
                    Notifications</a>
                <div>
                    <hr class="hr-setting">
                </div>
                <a href="rider-dashboard" class="dropdown-list show-mobile" style="text-decoration: none">
                    <img src="<c:url value='/resources/images/notification1.svg' />" alt="">    </span>
                    Home</a>
                <div>
                    <hr class="hr-setting">
                </div>
                <a href="rider-my-trip" class="dropdown-list " style="text-decoration: none">
                    <img src="<c:url value='/resources/images/location2.svg' />" alt="">    </span>
                    My Trips</a>
                <div>
                    <hr class="hr-setting">
                </div>
                <a href="rider-manage-account" class="dropdown-list " style="text-decoration: none">
                    <img src="<c:url value='/resources/images/manage-account.svg' />" alt="">    </span>
                    Manage your account</a>
                <div>
                    <hr class="hr-setting">
                </div>
                <a href="rider-wallet" class="dropdown-list " style="text-decoration: none">
                    <img src="<c:url value='/resources/images/wallet.svg' />" alt="">    </span>
                    Wallet</a>
                <div>
                    <hr class="hr-setting">
                </div>
                <a href="rider-help" class="dropdown-list " style="text-decoration: none">
                    <img src="<c:url value='/resources/images/help.svg' />" alt="">    </span>
                    Help</a>
                <div>
                    <hr class="hr-setting">
                </div>
                <a href="rider-logout" class="dropdown-list ">
                    <img src="<c:url value='/resources/images/sign-out.svg' />" alt=""></span>
                    Sign out
                </a>
            </div>
        </div>
    </div>


</nav>

</body>
</html>
