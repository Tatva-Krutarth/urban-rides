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

    <link rel="stylesheet" href="<c:url value="/resources/css/riderNotifications.css" />">
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

<div class="container ">

    <div class="notificaiton-text">
        Notificaitons
    </div>

    <c:if test="${not empty noNotifications}">
        <div>${noNotifications}</div>
    </c:if>

    <c:forEach var="notification" items="${notificationDataDto}">
        <div class="noti-container mt-2 mb-2">
            <div class="noti-img-cont">
                <c:choose>
                    <c:when test="${notification.notificationType eq 'Deposit successful'}">
                        <img src="<c:url value='/resources/images/wallet-white.svg'/>" id="noti-img">
                    </c:when>
                    <c:otherwise>
                        <img src="<c:url value='/resources/images/default-icon.svg'/>" id="noti-img">
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="noti-righ-cont">
                <div class="noti-header">${notification.notificationType}</div>
                <div class="noti-desc">${notification.notificationMsg}</div>
            </div>
            <div class="noti-time">
                    ${notification.createdDate}
            </div>
        </div>
    </c:forEach>


</div>

</body>
<script src="<c:url value="/resources/js/riderNotifications.js"/>"></script>

</html>
