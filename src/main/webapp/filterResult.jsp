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
  <h2>Filter Patients</h2>

  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null)
    {
  %>
    <p style="color: red;"><%= errorMessage %></p>
  <%
    }
    else
    {
      String filterType = (String) request.getAttribute("filterType");
      String filterValue = (String) request.getAttribute("filterValue");
      List<String[]> results = (List<String[]>) request.getAttribute("results");

      if (results == null)
      {
  %>
    <h3>Filter by City</h3>
    <form method="GET" action="/filter">
      <input type="hidden" name="type" value="city"/>
      <input type="text" name="value" placeholder="Enter city"/>
      <input type="submit" value="Search"/>
    </form>

    <h3>Filter by Gender</h3>
    <form method="GET" action="/filter">
      <input type="hidden" name="type" value="gender"/>
      <select name="value">
        <option value="M">Male</option>
        <option value="F">Female</option>
      </select>
      <input type="submit" value="Search"/>
    </form>

    <h3>Filter by State</h3>
    <form method="GET" action="/filter">
      <input type="hidden" name="type" value="state"/>
      <input type="text" name="value" placeholder="Enter state"/>
      <input type="submit" value="Search"/>
    </form>
  <%
      }
      else
      {
  %>
    <h3>Results for <%= filterType %>: "<%= filterValue %>"</h3>
    <%
      if (results.isEmpty())
      {
    %>
      <p>No patients found.</p>
    <%
      }
      else
      {
    %>
      <ul>
        <%
          for (String[] patient : results)
          {
        %>
        <li><a href="/viewPatient?row=<%= patient[1] %>"><%= patient[0] %></a></li>
        <%
          }
        %>
      </ul>
    <%
      }
    %>
    <br>
    <a href="/filter">Back to Filters</a>
  <%
      }
    }
  %>

  <br>
  <a href="/patientList">Back to Patient List</a>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>