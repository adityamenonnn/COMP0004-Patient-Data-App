<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Patient Data App</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="main">
  <%
    String mode = request.getParameter("mode");
    String actionLabel = "edit".equals(mode) ? "Edit" : "Delete";
  %>

  <h2><%= actionLabel %> a Patient</h2>

  <h3>Search by name or keyword</h3>
  <form method="GET" action="/runsearch">
    <input type="hidden" name="mode" value="<%= mode %>"/>
    <input type="text" name="searchstring" placeholder="Enter name or keyword"/>
    <input type="submit" value="Search"/>
  </form>

  <h3>Or browse all patients</h3>
  <a href="/patientList?mode=<%= mode %>">Show full patient list</a>
</div>
</body>
</html>