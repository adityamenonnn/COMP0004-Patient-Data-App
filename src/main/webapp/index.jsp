<%@ page contentType="text/html;charset=UTF-8" %>
<%
  String current = (String) session.getAttribute("currentDataset");
  if (current == null) current = "patients100.csv";
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Patient Data App</title>
  <link rel="stylesheet" type="text/css" href="/styles.css"/>
</head>
<body>
<div class="main">
  <h2>Welcome</h2>
  <p style="margin-bottom: 16px; color: #555;">Pick a dataset to load, then use the links below to browse and manage patient records.</p>
  <form action="selectDataset" method="post">
    <label for="dataset">Dataset:</label>
    <select name="dataset" id="dataset">
      <option value="patients100.csv"    <%= current.equals("patients100.csv")    ? "selected" : "" %>>100 patients</option>
      <option value="patients10000.csv"  <%= current.equals("patients10000.csv")  ? "selected" : "" %>>10,000 patients</option>
      <option value="patients100000.csv" <%= current.equals("patients100000.csv") ? "selected" : "" %>>100,000 patients</option>
    </select>
    <button type="submit">Load</button>
  </form>
  <ul>
    <li><a href="patientList">View Patient List</a></li>
    <li><a href="search.html">Search</a></li>
    <li><a href="stats">Statistics</a></li>
    <li><a href="addPatient">Add New Patient</a></li>
    <li><a href="patientAction.jsp?mode=edit">Edit a Patient</a></li>
    <li><a href="patientAction.jsp?mode=delete">Delete a Patient</a></li>
    <li><a href="exportJson">Download as JSON</a></li>
  </ul>
</div>
</body>
</html>