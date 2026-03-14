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
  <h2>Edit Patient</h2>

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
      Integer row = (Integer) request.getAttribute("row");
      if (patientData != null)
      {
  %>
  <form method="POST" action="/editPatient">
    <input type="hidden" name="row" value="<%= row %>"/>
    <%
      for (Map.Entry<String, String> entry : patientData.entrySet())
      {
    %>
    <label><strong><%= entry.getKey() %></strong></label><br/>
    <input type="text" name="<%= entry.getKey() %>" value="<%= entry.getValue() != null ? entry.getValue() : "" %>"/><br/><br/>
    <%
      }
    %>
    <input type="submit" value="Save Changes"/>
  </form>
  <%
      }
    }
  %>

  <br>
  <a href="/viewPatient?row=<%= request.getAttribute("row") %>">Cancel</a>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>