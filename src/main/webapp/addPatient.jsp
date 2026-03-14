<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Patient Data App</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="main">
  <h2>Add New Patient</h2>

  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null)
    {
  %>
    <p style="color: red;"><%= errorMessage %></p>
  <%
    }
  %>

  <form method="POST" action="/addPatient">
    <%
      List<String> columnNames = (List<String>) request.getAttribute("columnNames");
      if (columnNames != null)
      {
        for (String column : columnNames)
        {
    %>
    <label><strong><%= column %></strong></label><br/>
    <input type="text" name="<%= column %>" /><br/><br/>
    <%
        }
      }
    %>
    <input type="submit" value="Add Patient"/>
  </form>

  <br>
  <a href="/patientList">Cancel</a>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>