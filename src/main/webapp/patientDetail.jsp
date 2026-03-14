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
  <h2>Patient Details</h2>
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
      Map<String, String> patientData = (Map<String, String>) request.getAttribute("patientData");
      if (patientData != null)
      {
  %>
    <table>
      <tbody>
        <%
          for (Map.Entry<String, String> entry : patientData.entrySet())
          {
        %>
        <tr>
          <td><strong><%= entry.getKey() %></strong></td>
          <td><%= entry.getValue() != null ? entry.getValue() : "" %></td>
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
  <a href="/editPatient?row=<%= request.getParameter("row") %>">Edit Patient</a> |
  <form method="POST" action="/deletePatient" style="display:inline;">
    <input type="hidden" name="row" value="<%= request.getParameter("row") %>"/>
    <input type="submit" value="Delete Patient" onclick="return confirm('Are you sure you want to delete this patient?');"/>
  </form> |
  <a href="/patientList">Back to Patient List</a>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
