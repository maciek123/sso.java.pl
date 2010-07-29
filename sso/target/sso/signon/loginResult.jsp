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

<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>

<html:errors/>

<logic:notEmpty name="org.josso.gateway.user">

    <bean:define id="ssoUsername" name="org.josso.gateway.user" property="name" toScope="page"/>
    <bean:define id="ssoSessionId" name="org.josso.gateway.session" property="id" toScope="page"/>

    <div id="authentication-success">

        <div id="subwrapper">

            <div class="main">

                <h2><bean:message key="sso.title.login.success" arg0="<%=(String)ssoUsername%>"/></h2>

                <p><bean:message key="sso.text.login.success" arg0="<%=(String)ssoUsername%>"/></p>

            </div>
        </div>
    </div>
</logic:notEmpty>
