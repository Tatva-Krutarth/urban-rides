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
    <link rel="stylesheet" href="<c:url value="/resources/css/toaster.css" />">
    <!-- Icon -->
    <link rel="icon" href="<c:url value="/resources/images/Icon.png" />" type="image/icon type">
    <style>
        body {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
            background-color: #f8f9fa;
            margin: 0;
            padding-top: 60px; /* Adjusted for fixed navbar */
            overflow: hidden;
            font-family: 'Roboto', sans-serif;
        }

        nav {
            background-color: #001F3F;
            width: 100%;
            padding: 10px 0;
            position: fixed;
            top: 0;
            z-index: 1000;
        }

        .waiting-container {
            text-align: center;
            padding: 40px;
            border: 1px solid #dee2e6;
            border-radius: 12px;
            background-color: #ffffff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            position: relative;
            z-index: 2;
            max-width: 600px;
            margin: auto;
        }

        .waiting-container h1 {
            font-size: 2rem;
            margin-bottom: 20px;
        }

        .waiting-container p {
            font-size: 1.25rem;
            margin-bottom: 30px;
        }

        .waiting-animation {
            width: 100px;
            margin: 20px auto;
        }

        .waiting-animation svg {
            width: 100%;
            height: auto;
            max-width: 100px;
        }

        #log-out-btn {
            background-color: #0D63A5;
            color: white;
            font-weight: 500;
            height: 40px;
            transition: background-color 0.3s ease;
            padding: 10px 12px;
            text-decoration: none;
            border-radius: 12px;
        }

        #log-out-btn:hover {
            background-color: #001F3F;
            color: #FFD717;
        }
    </style>
</head>
<body>
<nav id="common-nav-1" class="d-flex justify-content-start align-items-center container-fluid">
    <div class="mx-3 d-flex justify-content-between align-items-center" style="color: white; font-size: 24px;">
        <div>Urban Rides</div>
    </div>
</nav>
<div class="waiting-container">
    <h1>Waiting for Document Approval</h1>
    <div class="waiting-animation">
        <!-- Replace with appropriate SVG or animated icon for waiting -->
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" width="200" height="200">
            <!-- Watch frame -->
            <circle cx="50" cy="50" r="40" stroke="#333" stroke-width="8" fill="none"/>
            <circle cx="50" cy="50" r="42" stroke="#666" stroke-width="4" fill="none"/>

            <!-- Hour markers -->
            <line x1="50" y1="10" x2="50" y2="15" stroke="#333" stroke-width="4"/>
            <line x1="85" y1="50" x2="80" y2="50" stroke="#333" stroke-width="4"/>
            <line x1="50" y1="90" x2="50" y2="85" stroke="#333" stroke-width="4"/>
            <line x1="15" y1="50" x2="20" y2="50" stroke="#333" stroke-width="4"/>

            <!-- Seconds hand -->
            <line id="secondsHand" x1="50" y1="50" x2="50" y2="20" stroke="#ff0000" stroke-width="4">
                <animateTransform attributeName="transform" attributeType="XML"
                                  type="rotate" from="0 50 50" to="360 50 50"
                                  dur="60s" repeatCount="indefinite"/>
            </line>

            <!-- Minutes hand -->
            <line x1="50" y1="50" x2="50" y2="30" stroke="#333" stroke-width="8"/>

            <!-- Hour hand -->
            <line x1="50" y1="50" x2="50" y2="40" stroke="#333" stroke-width="12"/>

            <!-- Center dot -->
            <circle cx="50" cy="50" r="2" fill="#333"/>
        </svg>

    </div>
    <p>Your documents are being reviewed. Please wait until they are approved.</p>
    <a href="captain-logout" id="log-out-btn">Log out</a>
</div>
<script src="<c:url value="/resources/js/captain-web-socket.js"/>"></script>

<script src="<c:url value="/resources/js/toaster.js"/>"></script>
</body>
</html>
