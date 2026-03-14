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
      <li><a href="stats?view=agedist">Age Distribution</a></li>
    </ul>
  <%
      }
      else if (view.equals("oldest"))
      {
  %>
    <h3>Oldest Living Patient</h3>
    <p><%= request.getAttribute("oldest") %></p>
  <%
      }
      else if (view.equals("youngest"))
      {
  %>
    <h3>Youngest Living Patient</h3>
    <p><%= request.getAttribute("youngest") %></p>
  <%
      }
      else if (view.equals("agedist"))
      {
  %>
    <h3>Age Distribution (Living Patients)</h3>
    <table>
      <thead>
        <tr>
          <th>Age Range</th>
          <th>Count</th>
        </tr>
      </thead>
      <tbody>
        <%
          Map<String, Integer> ageDistribution = (Map<String, Integer>) request.getAttribute("ageDistribution");
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
    }
  %>

  <br>
  <a href="stats">Back to Statistics Menu</a> |
  <a href="/patientList">Back to Patient List</a>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>