<%--
  ~ JOSSO: Java Open Single Sign-On
  ~
  ~ Copyright 2004-2009, Atricore, Inc.
  ~
  ~ This is free software; you can redistribute it and/or modify it
  ~ under the terms of the GNU Lesser General Public License as
  ~ published by the Free Software Foundation; either version 2.1 of
  ~ the License, or (at your option) any later version.
  ~
  ~ This software is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  ~ Lesser General Public License for more details.
  ~
  ~ You should have received a copy of the GNU Lesser General Public
  ~ License along with this software; if not, write to the Free
  ~ Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
  ~ 02110-1301 USA, or see the FSF site: http://www.fsf.org.
  ~
  --%>

<%@page contentType="text/html; charset=UTF-8" language="java" session="true" %>
<!--
Redirects the user to the proper login page.  Configured as the login url the web.xml for this application.
-->
<%
response.sendRedirect("http://127.0.0.1:8080/sso/signon/usernamePasswordLogin.do?josso_back_to=" + session.getAttribute("SPRING_SECURITY_SAVED_REQUEST_KEY").requestURL);
    //response.sendRedirect(request.getContextPath() + "/josso_login/");
%>
