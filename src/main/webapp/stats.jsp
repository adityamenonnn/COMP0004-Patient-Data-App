<%@ page import="java.util.Map" %>
<%@ page import="uk.ac.ucl.model.PatientSnapshot" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Patient Data App</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="main">
  <h2>Statistics</h2>

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
      String view = request.getParameter("view");
      if (view == null)
      {
  %>
    <ul>
      <li><a href="stats?view=oldest">Oldest Living Patient</a></li>
      <li><a href="stats?view=youngest">Youngest Living Patient</a></li>
      <li><a href="stats?view=agedist">Age Distribution (Table)</a></li>
      <li><a href="stats?view=agechart">Age Distribution (Chart)</a></li>
      <li><a href="stats?view=genderchart">Gender Distribution (Chart)</a></li>
    </ul>
  <%
      }
      else if (view.equals("oldest"))
      {
        PatientSnapshot oldest = (PatientSnapshot) request.getAttribute("oldest");
  %>
    <h3>Oldest Living Patient</h3>
    <%
        if (oldest == null)
        {
    %>
      <p>No data available.</p>
    <%
        }
        else
        {
          String oldestName = oldest.data().get("FIRST") + " " + oldest.data().get("LAST");
          String oldestDob  = oldest.data().get("BIRTHDATE");
    %>
      <p><a href="/viewPatient?row=<%= oldest.rowIndex() %>"><%= oldestName %> (born <%= oldestDob %>)</a></p>
    <%
        }
    %>
  <%
      }
      else if (view.equals("youngest"))
      {
        PatientSnapshot youngest = (PatientSnapshot) request.getAttribute("youngest");
  %>
    <h3>Youngest Living Patient</h3>
    <%
        if (youngest == null)
        {
    %>
      <p>No data available.</p>
    <%
        }
        else
        {
          String youngestName = youngest.data().get("FIRST") + " " + youngest.data().get("LAST");
          String youngestDob  = youngest.data().get("BIRTHDATE");
    %>
      <p><a href="/viewPatient?row=<%= youngest.rowIndex() %>"><%= youngestName %> (born <%= youngestDob %>)</a></p>
    <%
        }
    %>
  <%
      }
      else if (view.equals("agedist"))
      {
        Map<String, Integer> ageDistribution = (Map<String, Integer>) request.getAttribute("ageDistribution");
  %>
    <h3>Age Distribution (Living Patients)</h3>
    <table>
      <thead>
        <tr><th>Age Range</th><th>Count</th></tr>
      </thead>
      <tbody>
        <%
          for (Map.Entry<String, Integer> entry : ageDistribution.entrySet())
          {
        %>
        <tr>
          <td><%= entry.getKey() %></td>
          <td><%= entry.getValue() %></td>
        </tr>
        <%
          }
        %>
      </tbody>
    </table>
  <%
      }
      else if (view.equals("agechart"))
      {
  %>
    <h3>Age Distribution (Living Patients)</h3>
    <%= request.getAttribute("chart") %>
  <%
      }
      else if (view.equals("genderchart"))
      {
  %>
    <h3>Gender Distribution</h3>
    <%= request.getAttribute("chart") %>
  <%
      }
    }
  %>

  <br>
  <br>
  <a href="/stats">Back to Stats Menu</a> &nbsp;|&nbsp; <a href="/patientList">Patient List</a>
</div>
</body>
</html>