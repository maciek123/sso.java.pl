<%-- 
    Document   : index
    Created on : 2010-07-13, 17:02:22
    Author     : Max
--%>
<%@page import ="pl.java.sso.DefaultAdministratorDAO" %>
<%@page import ="org.josso.gateway.WebserviceGatewayServiceLocator" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>JSP Page</title>
	</head>
	<body>
		<h1>Hello World!!</h1>
		<%!
		DefaultAdministratorDAO dad = new DefaultAdministratorDAO();
		WebserviceGatewayServiceLocator wgsl = new WebserviceGatewayServiceLocator();


		%>
		<%= dad.sayHello() %>
		<%
		wgsl.setEndpoint("localhost:8080/epg-sso-gateway");
		try{
			out.println(wgsl.getSSOIdentityProvider().getClass());
			out.println(wgsl.getSSOIdentityProvider().assertIdentityWithSimpleAuthentication("jan", "asd", "123"));
		}
		catch(Exception e)
		{
			out.println(e.getMessage());
			for(StackTraceElement ste : e.getStackTrace())
				out.println(ste + "<BR/>");
		}
		%>
	</body>
</html>
