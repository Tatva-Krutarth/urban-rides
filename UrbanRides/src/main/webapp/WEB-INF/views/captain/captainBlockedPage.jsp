<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Blocked</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
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
        .blocked-container {
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
        .blocked-container h1 {
            font-size: 2rem;
            margin-bottom: 20px;
        }
        .blocked-container p {
            font-size: 1.25rem;
            margin-bottom: 30px;
        }
        .blocked-animation {
            width: 100px;
            margin: 20px auto;
        }
        .blocked-animation svg {
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
<div class="blocked-container">
    <h1>Account Blocked</h1>
    <div class="blocked-animation">
        <!-- Replace with appropriate SVG for blocked status -->
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" width="200" height="200">
            <!-- Blocked symbol -->
            <circle cx="50" cy="50" r="40" stroke="#FF0000" stroke-width="8" fill="none"/>
            <line x1="35" y1="35" x2="65" y2="65" stroke="#FF0000" stroke-width="8"/>
            <line x1="65" y1="35" x2="35" y2="65" stroke="#FF0000" stroke-width="8"/>
        </svg>
    </div>
    <p>Your account has been blocked by the admin.</p>
    <p>Please contact admin for more details:</p>
    <p><strong>Phone:</strong> +91 8849430122</p>
    <p><strong>Email:</strong> admin@urbanrides.com</p>
    <a href="captain-logout" id="log-out-btn">Log out</a>

</div>
<script src="<c:url value="/resources/js/toaster.js"/>"></script>
</body>
</html>
