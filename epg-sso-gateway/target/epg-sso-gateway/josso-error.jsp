<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>

<html:messages id="errMsg" header="errors.text.header" footer="errors.text.footer">
    <li><p><bean:write name="errMsg"/><p></li>
</html:messages>

<html:form action="/signon/usernamePasswordLogin" focus="josso_username">
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



