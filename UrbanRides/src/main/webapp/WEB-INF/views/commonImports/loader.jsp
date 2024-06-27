<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Loader</title>

<%--    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"--%>
<%--          crossorigin="anonymous">--%>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"
            integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            crossorigin="anonymous"></script>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">







    <style>
        .loader {
            position: fixed;
            top: 0;
            right: 0;
            bottom: 0;
            left: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            overflow: hidden;
            background-color: rgba(0, 0, 0, 0.5); /* light black transparent */
            z-index: 2000;
            display: none;
        }
        .icon {
            animation: shake 0.2s ease-in-out infinite alternate;
            width: 100px;
            height: auto;
            position: relative;
            z-index: 1;
        }

        /* .loading-text {
          margin-top: 10px;
          font-size: 24px;
          color: #002742;
          animation: pulse 1s infinite;
        } */

        .speed-lines {
            position: absolute;
            top: 50%;
            left: 47%;
            display: flex;
            flex-direction: column;
            z-index: 0;
            pointer-events: none;
            transform: translateY(-50%);
        }

        .line {
            width: 50px;
            height: 2px;
            background: black;
            margin: 5px 0;
            animation: moveLine 0.5s linear infinite;
        }

        .line:nth-child(2) {
            animation-delay: 0.1s;
        }

        .line:nth-child(3) {
            animation-delay: 0.2s;
        }

        @keyframes shake {
            0% {
                transform: translateY(-1%);
            }
            100% {
                transform: translateY(3%);
            }
        }

        /* @keyframes pulse {
          0% {
            opacity: 1;
          }
          50% {
            opacity: 0.5;
          }
          100% {
            opacity: 1;
          }
        } */

        @keyframes moveLine {
            0% {
                opacity: 1;
                transform: translateX(0);
            }
            100% {
                opacity: 0;
                transform: translateX(-100px);
            }
        }
    </style>
</head>
<body>

<div class="loader">

    <img src="<c:url value='/resources/images/taxi-loader.png'/>" alt="Loading Icon" class="icon" >
    <div class="speed-lines">
        <div class="line"></div>
        <div class="line"></div>
        <div class="line"></div>
    </div>
    <!-- <div class="loading-text">Loading...</div> -->
</div>

</body>
</html>
