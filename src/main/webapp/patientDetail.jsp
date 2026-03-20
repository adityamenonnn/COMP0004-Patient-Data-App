<%@ page import="uk.ac.ucl.model.PatientSnapshot" %>
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
      PatientSnapshot patient = (PatientSnapshot) request.getAttribute("patient");
      if (patient != null)
      {
  %>
    <button onclick="
      var l = document.getElementById('listView');
      var t = document.getElementById('tableView');
      if (l.style.display === 'none') { l.style.display=''; t.style.display='none'; this.textContent='Switch to Table View'; }
      else { l.style.display='none'; t.style.display=''; this.textContent='Switch to List View'; }
    ">Switch to Table View</button>

    <%-- List view: field name next to value --%>
    <div id="listView">
      <table>
        <tbody>
          <%
            for (Map.Entry<String, String> entry : patient.data().entrySet())
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
    </div>

    <%-- Table view: column names as headers, values as a single row (like Excel) --%>
    <div id="tableView" style="display:none; overflow-x:auto;">
      <style>
        #tableView table { border-collapse: collapse; }
        #tableView th, #tableView td { border: 1px solid #ccc; padding: 6px 10px; }
        #tableView th { background-color: #ff0000; color: white; }
      </style>
      <table>
        <thead>
          <tr>
            <%
              for (String key : patient.data().keySet())
              {
            %>
            <th><%= key %></th>
            <%
              }
            %>
          </tr>
        </thead>
        <tbody>
          <tr>
            <%
              for (String val : patient.data().values())
              {
            %>
            <td><%= val != null ? val : "" %></td>
            <%
              }
            %>
          </tr>
        </tbody>
      </table>
    </div>

    <div>
      <a href="/editPatient?row=<%= patient.rowIndex() %>">Edit Patient</a>
      <form method="POST" action="/deletePatient">
        <input type="hidden" name="row" value="<%= patient.rowIndex() %>"/>
        <input type="submit" value="Delete Patient" onclick="return confirm('Are you sure you want to delete this patient?');"/>
      </form>
      <a href="/patientList">Back to Patient List</a>
    </div>
  <%
      }
    }
  %>
</div>
</body>
</html>