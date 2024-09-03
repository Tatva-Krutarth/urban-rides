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
    <link rel="stylesheet" href="<c:url value="/resources/css/riderWallet.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/toaster.css" />">
    <link rel="icon" href="<c:url value="/resources/images/Icon.png" />" type="image/icon type">
    <link href="<c:url value="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" />"
          rel="stylesheet">

</head>
<body>
<%@include file="../commonImports/loader.jsp" %>
<%@include file="../rider/riderNavbar.jsp" %>

<div class="container mt-3">
    <div class="wallet-heading mt-2 mb-3 px-1">
        Wallet
    </div>

    <div class="wallet-card">
        <div class="wallet-balance">Balance: ₹ ${walletAmount}</div>
        <div class="wallet-info">Wallet for managing your transactions easily</div>
        <div class="buttons-container">
            <button class="wallet-button" onclick="showInputField()">Deposit Money</button>

        </div>
        <div class="input-container" id="inputContainer">
            <input type="number" id="depositAmount" placeholder="Enter amount">
            <button class="add-money" id="deposit-money" onclick="addMoney()">Add Money</button>
        </div>
    </div>


    <div class="wallet-heading mt-4">
        Transactions
    </div>
    <hr>
    <div class="container">

        <div class="wallet-subheading">
            Paid By Cash
        </div>
        <div id="transaction-container1">
            <div class="noti-container mt-2 mb-2">
                <div class="noti-img-cont">
                    <img src="<c:url value='/resources/images/wallet-white.svg'/>">
                </div>
                <div class="noti-righ-cont">
                    <div class="noti-header">
                        Paid to Taxi Driver - Ramesh Kuntariya
                    </div>
                    <div class="noti-time">
                        2:23 23/1/24
                    </div>
                </div>
                <div class="amount-paid"> Rs.35</div>
            </div>
        </div>
        <div class="wallet-subheading">
            Paid By Wallet
        </div>
        <div id="transaction-container2">

            <div class="noti-container mt-2 mb-2">
                <div class="noti-img-cont">
                    <img src="<c:url value='/resources/images/wallet-white.svg'/>">
                </div>
                <div class="noti-righ-cont">
                    <div class="noti-header">
                        Paid to Taxi Driver - Ramesh Kuntariya
                    </div>
                    <div class="noti-time">
                        2:23 23/1/24
                    </div>
                </div>
                <div class="amount-paid"> Rs.35</div>
            </div>
        </div>

    </div>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"
        integrity="sha512-iKDtgDyTHjAitUDdLljGhenhPwrbBfqTKWO1mkhSFH3A7blITC9MhYon6SjnMhp4o0rADGw9yAC6EW4t5a4K3g=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.6.1/sockjs.min.js"
        integrity="sha512-1QvjE7BtotQjkq8PxLeF6P46gEpBRXuskzIVgjFpekzFVF4yjRgrQvTG1MTOJ3yQgvTteKAcO7DSZI92+u/yZw=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<div class="hs-toast-wrapper  hs-toast-fixed-top " id="example"></div>
<script src="<c:url value="/resources/js/toaster.js"/>"></script>
<script src="<c:url value="/resources/js/riderWallet.js"/>"></script>
<script src="<c:url value="/resources/js/rider-web-socket.js"/>"></script>
</body>
</html>
