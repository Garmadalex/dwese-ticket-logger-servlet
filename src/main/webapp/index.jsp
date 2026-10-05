<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

 <%@ include file="header.jsp" %>

<body>
 <h2>Hello, World!</h2>
 <%
 int count = 10;
 for (int i = 0; i < count; i++) {
 out.println("Number: " + i + "<br>");
 }
 %>

 <h3>Current Time: <%= new java.util.Date() %></h3>
<%!
 private int counter = 0;

 public int getCounter() {
 return ++counter;
 }
%>
<p>Counter Value: <%= getCounter() %></p>
<p>Counter Value: <%= getCounter() %></p>
<p>Counter Value: <%= getCounter() %></p>

<%
 // Código Java embebido (Scriptlet)
 String userName = "Alejandro Madrigal";
 int age = 20;
%>

<p>User Name: <%= userName %></p> <!-- Expresión -->
<p>User Age: <%= age %></p> <!-- Expresión -->

<%
 String role = "admin";
 if ("admin".equals(role)) {
%>
 <p>Welcome, Administrator!</p>
<%
 } else {
%>
 <p>Welcome, User!</p>
<%
 }
%>

<ul>
<%
 int i = 1;
 while ( i <= 5 ) {
%>
 <li>Item <%= i %></li>
<%
 i++;
 }
%>


<%@ include file="footer.jsp" %>

</ul>
</body>
</html>