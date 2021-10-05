<!DOCTYPE html>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!--%@taglib prefix="c" uri="http://www.java.sun.com/jsp/jstl/core"%-->
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>


<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>company</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
    <sf:form method="POST" commandName="company" action="/register">
        <table><tr>
            <td>Name of Company:</td><td><sf:input path="name" type="text" placeholder="Enter name" /></td>
        </tr><tr>
            <td>Kennitala:</td><td><sf:input path="kennitala" type="text" placeholder="123456789"/></td>
        </tr><tr>
            <td>Netfang:</td><td><sf:input path="email" type="text" placeholder="company@company.is"/></td>
        </tr></table>
        <input type="submit" value="Register Company!"/>
    </sf:form>
</body>
</html>