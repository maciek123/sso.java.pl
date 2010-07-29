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

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>


<html:errors/>


<html:form action="/signon/usernamePasswordLogin.do" focus="josso_username">
    <html:hidden property="josso_cmd" value="login"/>
    <table class="section-div">
        <tbody>
        <tr>
            <td class="td_blue"><bean:message key="sso.label.username"/></td>
            <td class="td_blue2">
                <html:text styleClass="text" property="josso_username" value=""/>
            </td>
        </tr>
        <tr>
            <td class="td_blue"><bean:message key="sso.label.password"/></td>
            <td class="td_blue2">
                <html:password styleClass="text error" property="josso_password" value=""/>
            </td>
        </tr>
        </tbody>
    </table>
    <input class="button" type="submit" value="Login"/>
</html:form>