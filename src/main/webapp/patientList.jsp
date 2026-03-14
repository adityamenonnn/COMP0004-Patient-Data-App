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
  <h2>Patients:</h2>

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

  <a href="/addPatient">Add New Patient</a> |
  <a href="/exportJson">Download as JSON</a>

  <h3>All Patients</h3>
  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null)
    {
  %>
      <p style="color: red;"><%= errorMessage %></p>
  <%
    }
  %>
  <ul>
    <%
      List<String> patients = (List<String>) request.getAttribute("patientNames");
      if (patients != null)
      {
        for (int i = 0; i < patients.size(); i++)
        {
          String href = "viewPatient?row=" + i;
    %>
    <li><a href="<%=href%>"><%=patients.get(i)%></a>
    </li>
    <%  }
      }
    %>
  </ul>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
