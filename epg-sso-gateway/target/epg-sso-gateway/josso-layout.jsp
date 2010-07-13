<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="es" lang="es">

<head>
    <title><bean:message key="sso.title"/> - <tiles:getAsString name="josso.page.title" ignore="true"/></title>

    <meta name="Robots" content="index,follow"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <link href="<%=request.getContextPath()%>/resources/css/admconsole.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/resources/css/tables.css" rel="stylesheet" type="text/css"/>

</head>
<body>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tbody>
    <tr>
        <td class="header_main"><span>Login to EPG</span></td>
        <td class="header_main" style="text-align: right;">

        </td>
    </tr>
    <tr>
        <td class="main_menu" style="text-align: right; font-style: normal; font-size: 10px;" colspan="2">
            <div class="span-24 last">
                &nbsp;&nbsp;
            </div>
        </td>
    </tr>
    <tr>
        <td class="td_content" colspan="2">
            <tiles:insert attribute="josso.body" flush="false"/>
        </td>
    </tr>
    </tbody>
</table>
</body>
</html>