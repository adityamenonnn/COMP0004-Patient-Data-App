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
  <h2>Patients</h2>

  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null)
    {
  %>
    <p style="color: red;"><%= errorMessage %></p>
  <%
    }
  %>

  <button onclick="document.getElementById('filters').style.display = document.getElementById('filters').style.display === 'none' ? 'block' : 'none'">
    Filter Patients
  </button>

  <div id="filters" style="display:none; margin-top: 10px;">
    <form method="GET" action="/filter">
      <label>City: <input type="text" name="city" placeholder="e.g. Boston"/></label>
      <label>Gender:
        <select name="gender">
          <option value="">Any</option>
          <option value="M">Male</option>
          <option value="F">Female</option>
        </select>
      </label>
      <label>State: <input type="text" name="state" placeholder="e.g. Massachusetts"/></label>
      <input type="submit" value="Filter"/>
    </form>
  </div>

  <%
    String mode = (String) request.getAttribute("mode");
    if ("edit".equals(mode))
    {
  %>
    <p>Select a patient to edit:</p>
  <%
    }
    else if ("delete".equals(mode))
    {
  %>
    <p>Select a patient to delete:</p>
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
          String href;
          if ("edit".equals(mode))
            href = "editPatient?row=" + i;
          else if ("delete".equals(mode))
            href = "viewPatient?row=" + i;
          else
            href = "viewPatient?row=" + i;
    %>
    <li><a href="<%= href %>"><%= patients.get(i) %></a></li>
    <%
        }
      }
    %>
  </ul>
</div>
</body>
</html>