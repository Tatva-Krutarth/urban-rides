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
    <link rel="stylesheet" href="<c:url value="/resources/css/adminUserManagement.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/toaster.css" />">
    <link rel="icon" href="<c:url value="/resources/images/Icon.png" />" type="image/icon type">
    <link href="<c:url value="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" />"
          rel="stylesheet">
<body>
<%@include file="../admin/adminNavbar.jsp" %>
<%@include file="../commonImports/loader.jsp" %>

<div class="container mt-4">
    <div id="ad-dash-support-types">
        <div id="all" class="user-active" onclick="userManAll()">All</div>
        <div id="rider" onclick="userManRider()">Rider</div>
        <div id="captain" onclick="userManCaptain()">Captain</div>
        <div id="blocked" onclick="userManBlocked()">Blocked</div>
    </div>
</div>
<div class="container mt-5">
    <div class="table-responsive">
        <table class="table table-hover">
            <thead id="table-head-user-management">
            <tr>
                <th class="align-middle text-center">Account Type</th>
                <th class="align-middle text-center">Name</th>
                <th class="align-middle text-center">Email</th>
                <th class="align-middle text-center">Phone</th>
                <th class="align-middle text-center">Status</th>
                <th class="align-middle text-center">View ride history</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>Admin</td>
                <td>johndoe@example.com</td>
                <td>123-456-7890</td>
                <td>John Doe</td>
                <td class="align-middle text-center"><span class="unverified-btn">Unverifed</span></td>
                <td class="align-middle text-center">
                    <button class="btn user-manage-view-mote-btn accordion-btn" data-bs-toggle="collapse"
                            data-bs-target="#details1" aria-expanded="false" aria-controls="details1"> View
                    </button>
                </td>
            </tr>
            <tr id="details1" class="collapse accordion-content">
                <td colspan="6">
                    <div class="accordion-inner">
                        <!-- Additional details for John Doe -->
                        <p>no of rides done , success rides , failed tide , rider userID , on going ride</p>
                    </div>
                </td>
            </tr>
            <tr>
                <td>Captain</td>
                <td>johndoe@example.com</td>
                <td>123-456-7890</td>
                <td>John Doe</td>
                <td class="align-middle text-center"><div class=" block-btn">Block</div></td>
                <td class="align-middle text-center"> <!-- Added Bootstrap classes -->
                    <button class="btn user-manage-view-mote-btn accordion-btn"
                            data-bs-toggle="collapse"
                            data-bs-target="#details2"
                            aria-expanded="false"
                            aria-controls="details2">
                        View
                    </button>
                </td>
            </tr>
            <tr id="details2" class="collapse accordion-content">
                <td colspan="6">
                    <div class="accordion-inner">
                        <!-- Additional details for Jane Smith -->
                        <p>More details about Jane Smith...</p>
                    </div>
                </td>
            </tr>
            <tr>
                <td>Rider</td>
                <td>johndoe@example.com</td>
                <td>123-456-7890</td>
                <td>John Doe</td>
                <td class="align-middle text-center"><span class=" unblock-btn">Unblock</span></td>
                <td class="align-middle text-center">
                    <button class="btn user-manage-view-mote-btn accordion-btn" data-bs-toggle="collapse"
                            data-bs-target="#details3" aria-expanded="false" aria-controls="details3"> View
                    </button>
                </td>
            </tr>
            <tr id="details3" class="collapse accordion-content">
                <td colspan="6">
                    <div class="accordion-inner">
                        <p>More details about Michael Johnson...</p>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

<div class="hs-toast-wrapper  hs-toast-fixed-top " id="example"></div>
<script src="<c:url value="/resources/js/toaster.js"/>"></script>
</body>
<script src="<c:url value="/resources/js/adminUserManagement.js"/>"></script>

</html>
