<%--
  Created by IntelliJ IDEA.
  User: pci221
  Date: 02-07-2024
  Time: 10:50
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
        <%--        <a href="captain-dashboard" style="text-decoration: none" class="common-btn-color7-theme nav-my-trip-cont">--%>
        <%--            <div class="nav-my-trip-img">--%>
        <%--                <img src="<c:url value='/resources/images/home.svg' />" alt="">--%>
        <%--            </div>--%>
        <%--            <div class="nav-my-trip">--%>
        <%--                Home--%>
        <%--            </div>--%>
        <%--        </a>--%>


        <a href="captain-dashboard" style="text-decoration: none" class="common-btn-color7-theme nav-my-trip-cont">
            <div class="nav-my-trip-img">
                <img src="<c:url value='/resources/images/home.svg' />" alt="">
            </div>
            <div class="nav-my-trip">
                Home
            </div>
        </a>
        <a href="captain-notifications" style="text-decoration: none" class="nati-cont">
            <img src="<c:url value='/resources/images/notification1.svg' />" alt="">
        </a>
        <div class="dropdown d-flex flex-row-reverse align-items-center">
            <button class="dropbtn ">
                <div class="profile-pic">
                    <img src="<c:url value='/resources/images/profile-logo.svg' />" alt="">
                </div>
                <div class="profile-pic-side-dropdown">
                    <img src="<c:url value='/resources/images/down-arrow.svg' />" alt="">
                </div>
            </button>

            <div class="dropdown-content">
                <a href="captain-dashboard" class="dropdown-list show-mobile" style="text-decoration: none">
                    <img src="<c:url value='/resources/images/home.svg' />" alt="">    </span>
                    Home</a>
                <div>
                    <hr class="hr-setting">
                </div>
                <a href="captain-notifications" class="dropdown-list show-mobile" style="text-decoration: none">
                    <img src="<c:url value='/resources/images/notification1.svg' />" alt="">    </span>
                    Notifications</a>
                <div>
                    <hr class="hr-setting">
                </div>



                <a href="captain-manage-account" class="dropdown-list " style="text-decoration: none">
                    <img src="<c:url value='/resources/images/manage-account.svg' />" alt="">    </span>
                    Manage your account</a>
                <div>
                    <hr class="hr-setting">
                </div>
                <a href="captain-my-trip" class="dropdown-list " style="text-decoration: none">
                    <img src="<c:url value='/resources/images/location2.svg' />" alt="">    </span>
                    My Trips</a>
                <div>
                    <hr class="hr-setting">
                </div>
                <a href="captain-earnings" class="dropdown-list " style="text-decoration: none">
                    <img src="<c:url value='/resources/images/wallet.svg' />" alt="">    </span>
                    My Earnings</a>
                <div>
                    <hr class="hr-setting">
                </div>
                <a href="captain-package-rides" class="dropdown-list " style="text-decoration: none">
                    <img src="<c:url value='/resources/images/package.svg' />" alt="">    </span>
                    Package Rides</a>
                <div>
                    <hr class="hr-setting">
                </div>
                <a href="captain-help" class="dropdown-list " style="text-decoration: none">
                    <img src="<c:url value='/resources/images/help.svg' />" alt="">    </span>
                    Help</a>
                <div>
                    <hr class="hr-setting">
                </div>
                <a href="captain-logout" class="dropdown-list ">
                    <img src="<c:url value='/resources/images/sign-out.svg' />" alt=""></span>
                    Sign out
                </a>
            </div>
        </div>
    </div>
</nav>
</body>
</html>
