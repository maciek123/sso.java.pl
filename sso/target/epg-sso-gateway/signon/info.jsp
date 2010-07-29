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
