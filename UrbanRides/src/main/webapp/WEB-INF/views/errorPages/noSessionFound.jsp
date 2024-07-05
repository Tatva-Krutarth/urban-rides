<!DOCTYPE html>
<html lang="en">
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
    <link rel="icon" href="<c:url value='/resources/images/Icon.png'/>" type="image/icon type">
    <style>
        body {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
            background-color: #f8f9fa;
            margin: 0;
            padding: 6px 0px 100px 0px;
            overflow: hidden;
        }

        nav {
            background-color: #083358;
            width: 100%;
            padding: 10px 0;
            position: fixed;
            top: 0;
            z-index: 1000;
        }

        .session-container {
            text-align: center;
            padding: 40px;
            border: 1px solid #dee2e6;
            border-radius: 12px;
            background-color: #ffffff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            position: relative;
            z-index: 2;
        }

        .session-container h1 {
            font-size: 2rem;
            margin-bottom: 20px;
        }

        .session-container p {
            font-size: 1.25rem;
            margin-bottom: 30px;
        }

        #login-btn {
            background-color: #0D63A5;
            color: white;
            font-weight: 500;
            height: 40px;
            transition: background-color 0.3s ease;

        }

        #login-btn:hover {
            background-color: #001F3F;
            color: #FFD717;
        }


        .car-animation {
            position: absolute;
            bottom: 20px;
            left: 50%;
            /*width: 100%;*/
            transform: translateX(-50%);
            z-index: 1;
        }

        .road {
            position: absolute;
            bottom: 0;
            left: 0;
            width: 100%;
            height: 100px;
            background-color: #001F3F;
            z-index: 1;
        }

        @keyframes drive {
            0% {
                transform: translateX(-100%);
            }
            100% {
                transform: translateX(100%);
            }
        }

        .car-animation img {
            width: 100px;
            animation: drive 5s linear infinite;
        }
    </style>
</head>
<body>
<nav id="common-nav-1" class="d-flex justify-content-start align-items-center container-fluid">
    <div class="mx-3 d-flex justify-content-between align-items-center" style="color: white; font-size: 24px;">
        <div>Urban Rides</div>
    </div>
</nav>
<div class="session-container">
    <h1>Session Not Found</h1>
    <p>Your session has expired or does not exist.</p>
    <a href="/UrbanRides/user-login" class="btn  " id="login-btn">Login Again</a>
</div>
<div class="road"></div>
<div class="car-animation">
    <img src="<c:url value='/resources/images/taxi-loader.png'/>" alt="Loading Icon" class="icon">
</div>
</body>
</html>
