<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n" crossorigin="anonymous"></script>
<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/CSS/narrow-jumbotron.css" rel="stylesheet">
    <title>CalorieWatch</title>
</head>
<body>
<div class="container">
    <%@ include file="navbar.jsp" %>
    <div class="jumbotron">
        <h1 class="display-3">Registration</h1>
        <p class="lead">Please fill in all forms.</p>
        <div class="row">
            <div class="col-3"></div>
            <div class="col-6">
                <form action="/Register/Submit" method="POST">
                    <div class="form-group">
                        Username: <input type="text" required class="form-control" id="username" name="username">
                    </div>

                    <div class="form-group">
                        Password: <input type="password" required class="form-control" id="password" name="password">
                    </div>
                    <input type="hidden" name="lat" id="lat" value="">
                    <input type="hidden" name="lng" id="lng" value="">
                    Your location: <div id="userLocation"> Please allow sharing of browser location.</div>

                    <p class="lead">${message}</p>
                    <c:remove var="message" scope="session" />

                    <input type="submit" class="btn btn-info" value="Register">
                </form>


            </div>
        </div>
    </div>
</div>
<script>
    $( document ).ready(function() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                var lat = position.coords.latitude;
                var lng = position.coords.longitude;

                document.getElementById("lat").value = lat;
                document.getElementById("lng").value = lng;
                document.getElementById("userLocation").innerHTML = lat + "," + lng;
            }, function() {
                handleLocationError(true, infoWindow, map.getCenter());
            });
        } else {
            // Browser doesn't support Geolocation
            handleLocationError(false, infoWindow, map.getCenter());
        }
    });


function handleLocationError(browserHasGeolocation, infoWindow, pos) {
    alert(browserHasGeolocation ?
        'Error: The Geolocation service failed.' :
        'Error: Your browser doesn\'t support geolocation.');
}
</script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
</body>
</html>