<%@ page import="uk.ac.ucl.model.PatientSnapshot" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
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
      List<PatientSnapshot> results = (List<PatientSnapshot>) request.getAttribute("results");
      Map<String, String> filters = (Map<String, String>) request.getAttribute("filters");

      if (results == null)
      {
  %>
    <p>Use the filter options on the <a href="/patientList">patient list</a>.</p>
  <%
      }
      else
      {
        StringBuilder summary = new StringBuilder();
        for (Map.Entry<String, String> e : filters.entrySet())
        {
          if (e.getValue() != null && !e.getValue().trim().isEmpty())
          {
            if (summary.length() > 0) summary.append(", ");
            summary.append(e.getKey()).append("=").append(e.getValue());
          }
        }
  %>
    <h3>Results for: <%= summary %></h3>
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
          for (PatientSnapshot patient : results)
          {
            String name = patient.data().get("FIRST") + " " + patient.data().get("LAST");
        %>
        <li><a href="/viewPatient?row=<%= patient.rowIndex() %>"><%= name %></a></li>
        <%
          }
        %>
      </ul>
    <%
      }
    %>
    <br>
    <a href="/patientList">Back to Patient List</a>
  <%
      }
    }
  %>
</div>
</body>
</html>