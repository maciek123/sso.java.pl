<%@ page import="java.io.PrintWriter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="es" lang="es">
<head>
    <title>JOSSO error page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="resources/css/admconsole.css" rel="stylesheet" type="text/css"/>
    <link href="resources/css/tables.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tbody>
    <tr>
        <td class="header_main"><span>JOSSO error page</span></td>
        <td class="header_main" style="text-align: right;">

        </td>
    </tr>
    <tr>
        <td class="main_menu" style="text-align: right; font-style: normal; font-size: 10px;" colspan="2">
            <div class="span-24 last">
                &nbsp;&nbsp;
            </div>
            <div style="display: none;">
                <%@ page isErrorPage="true" %>
                <%
                    if (exception != null) {
                        exception.printStackTrace(new PrintWriter(out));
                    }
                %>
            </div>
        </td>

    </tr>
    <tr>
        <td class="td_content" colspan="2">

            <%
                if (exception != null) {%>
            <ul>
                <li><% new PrintWriter(out).println(exception.getMessage());%></li>
            </ul>
            <%
                }
            %>

            <form name="usernamePasswordLoginForm" method="post" action="/josso/signon/usernamePasswordLogin.do">
                <input type="hidden" name="josso_cmd" value="login">
                <table class="section-div">
                    <tbody>
                    <tr>
                        <td class="td_blue">Login</td>

                        <td class="td_blue2">
                            <input type="text" name="josso_username" value="" class="text">
                        </td>
                    </tr>
                    <tr>
                        <td class="td_blue">Password</td>
                        <td class="td_blue2">
                            <input type="password" name="josso_password" value="" class="text error">

                        </td>
                    </tr>
                    </tbody>
                </table>
                <input class="button" type="submit" value="Login"/>
            </form>
            <script type="text/javascript" language="JavaScript">
                <!--
                var focusControl = document.forms["usernamePasswordLoginForm"].elements["josso_username"];

                if (focusControl.type != "hidden") {
                    focusControl.focus();
                }
                // -->
            </script>

        </td>
    </tr>

    </tbody>
</table>
</body>
</html>