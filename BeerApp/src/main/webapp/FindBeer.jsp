<%--
  Created by IntelliJ IDEA.
  User: pmtqe
  Date: 4/18/2023
  Time: 10:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Find a User</title>
</head>
<body>
<form action="findbeer" method="post">
    <h1>Search for a User by UserName</h1>
    <p>
        <label for="pattern">pattern</label>
        <input id="pattern" name="pattern" value="${fn:escapeXml(param.pattern)}">
        <input id="pagesize" name="pagesize" value="${fn:escapeXml(param.pagesize)}">
        <input id="pagenumber" name="pagenumber" value="${fn:escapeXml(param.pagenumber)}">
    </p>
    <p>
        <input type="submit">
        <br/><br/><br/>
    </p>
</form>
<h1>Matching beers</h1>
<table border="1">
    <tr>
        <th>beer name</th>
    </tr>
    <c:forEach items="${beers}" var="beer">
        <tr>
            <td><c:out value="${beer.getName()}" /></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>