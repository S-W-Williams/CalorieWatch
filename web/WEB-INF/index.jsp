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
  <c:if test="${not empty message}">
    <div class="alert alert-info alert-dismissible fade show" role="alert">
      <button type="button" class="close" data-dismiss="alert" aria-label="Close">
        <span aria-hidden="true">&times;</span>
      </button>
        ${message}
    </div>
  </c:if>
  <div class="row">
    <div class="col-12">
      <div class="jumbotron" style="background:transparent !important">
        <h3 class="display-3">Welcome to CalorieWatch</h3>

        <h4>Hello, ${username}.</h4>
        <br>
        <br>

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


<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn" crossorigin="anonymous"></script>
<script type="text/javascript">



</script>
<script src="./JS/pageSearch.js"></script>


</body>
</html>
