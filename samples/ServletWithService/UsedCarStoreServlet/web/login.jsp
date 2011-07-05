<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Used Car Store</title>
</head>
<body>
<%@include file="banner.jsp" %>
<%
	if (request.getSession().getAttribute("user_name") != null) {
		response.sendRedirect(request.getContextPath() + "/store/view");
	}

	String message = (String)request.getAttribute("message");
%>
<br />
<form action="<%= request.getContextPath() %>/store/login" method="post">
	<table align="center" border="0" cellpadding="2" cellspacing="2">
		<tr>
			<td>
				<span style="font-size: large; font-weight: bold;">
					Login
				</span>
				<br /><br />
			</td>
		</tr>
		<% if (message != null) { %>
		<tr>
			<td style="font-style: italic; color: red;">
				<%= message %>
			</td>
		</tr>
		<% } %>
		<tr>
			<td>
				<label>User</label>
				<br />
				<input name="user" type="text" size="15" />
			</td>
		</tr>
		<tr>
			<td>
				<label>Password</label>
				<br />
				<input name="password"  type="password" size="15" />
			</td>
		</tr>
		<tr>
			<td>
				<input type="submit" value="Login" />
			</td>
		</tr>
		<tr>
			<td>
				Any "unique" user-password combination will work.
				<br />
				If user does not exist, one will be created on the fly.
			</td>
		</tr>
	</table>
</form>
</body>
</html>