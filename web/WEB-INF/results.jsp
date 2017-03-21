<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css" integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/CSS/narrow-jumbotron.css" rel="stylesheet">
    <title>CalorieWatch</title>
</head>
<body>
<div class="container">
    <%@ include file="navbar.jsp" %>
</div>
<div class="container-fluid">
    <div class="row">
        <div class="col-12">
            <div class="jumbotron" style="background:transparent !important">
                <div class="row">
                    <div class="mx-auto" style="width: 800px">
                        <form action="/Search" method="POST">
                            <div class="input-group input-group-lg" style="vertical-align: middle;">
                                <input type="text" placeholder="Search for a category" class="form-control" name="query" id="query">
                                <span class="input-group-btn">
                                    <input type="submit" class="btn btn-info" id="searchButton" value="Search">
                                </span>
                                <!-- <input type="text" placeholder="Location" class="form-control" name="location" id="location" value="Irvine, CA"> -->
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="container">
    <div class="row">
        <div class="col-lg-12">
            <c:set var="count" value="-1" scope="page" />
            <c:if test="${restaurants.size() > 0}"><h1>Available healthy options near you</h1><br></c:if>
            <c:forEach items="${restaurants}" var="restaurant">
                <c:if test="${restaurant.isAvailable() == true}">
                    <c:set var="count" value="${count + 1}" scope="page"/>
                    <div class="card">
                        <h3 class="card-header"><c:out value="${restaurant.name}"/>, Health Score: <font color="#5bc0de"><c:out value="${restaurant.healthScore}" /></font></h3>
                        <div class="card-block">
                            <h4 style="display: inline">Yelp Rating: ${restaurant.getRating()}</h4><br><br>
                            <strong>Phone Number: </strong> ${restaurant.getPhoneNumber()}<br>
                            <strong>Address:</strong> <a href="http://maps.google.com/?q=${restaurant.getDisplayAddress()}">${restaurant.getDisplayAddress()}</a>
                            <br><br>
                            <h4 class="card-title">Menu: </h4>
                            <c:forEach items="${restaurant.getEntries()}" var="entry">
                                <table width="100%">
                                    <col style="width:50%">
                                    <col style="width:50%">
                                    <tbody>
                                        <tr>
                                            <td>${entry.getItem()}</td>
                                            <td>Calories: ${entry.getCalories()}</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </c:forEach>
                            <br>
                            <a href="${restaurant.getYelpUrl()}" class="btn btn-primary">View on Yelp</a>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
            <br>
            <c:if test="${count > -1}"><h1>Other options</h1><br></c:if>
            <c:forEach items="${restaurants}" var="restaurant">
                <c:if test="${restaurant.isAvailable() == false}">
                    <div class="card">
                        <h3 class="card-header"><c:out value="${restaurant.name}"/>, Health Score: <font color="#5bc0de"><c:out value="${restaurant.healthScore}" /></font></h3>
                        <div class="card-block">
                            <h4 class="card-title">Menu: </h4>
                            <c:forEach items="${restaurant.getEntries()}" var="entry">
                                <table width="100%">
                                    <col style="width:50%">
                                    <col style="width:50%">
                                    <tbody>
                                    <tr>
                                        <td>${entry.getItem()}</td>
                                        <td>Calories: ${entry.getCalories()}</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </div>
</div>


<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
<script type="text/javascript">



</script>
<script src="./JS/pageSearch.js"></script>


</body>
</html>
