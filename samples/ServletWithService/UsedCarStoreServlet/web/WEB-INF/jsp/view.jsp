<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Used Car Store</title>
</head>
<body>
<%@include file="/banner.jsp" %>
Store front

Welcome <%= request.getSession().getAttribute("user_name") %>

</body>
</html>