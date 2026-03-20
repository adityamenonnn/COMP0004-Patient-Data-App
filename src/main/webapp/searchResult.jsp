<%@ page import="uk.ac.ucl.model.PatientSnapshot" %>
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
  <h2>Search Results</h2>
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
      String mode = (String) request.getAttribute("mode");
      if (results == null || results.isEmpty())
      {
  %>
    <p>Nothing found.</p>
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
          String href;
          if ("edit".equals(mode))
            href = "/editPatient?row=" + patient.rowIndex();
          else if ("delete".equals(mode))
            href = "/viewPatient?row=" + patient.rowIndex();
          else
            href = "/viewPatient?row=" + patient.rowIndex();
      %>
      <li><a href="<%= href %>"><%= name %></a></li>
      <%
        }
      %>
    </ul>
  <%
      }
    }
  %>
  <br>
  <a href="/search.html">Search again</a>
</div>
</body>
</html>