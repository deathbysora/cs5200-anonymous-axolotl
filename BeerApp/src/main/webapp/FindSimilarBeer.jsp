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
	<form action="findsimilarbeer" method="post">
		<h1>Find similar beer review by their average rating</h1>
		<p>
			<label for="beerId">BeerId</label>
			<input id="beerId" name="beerId" value="${fn:escapeXml(param.beerId)}">
		</p>
		<p>
			<input type="submit">
			<br/><br/><br/>
			<span id="successMessage"><b>${messages.success}</b></span>
		</p>
	</form>
	<h1>Result: Matching Review</h1>
        <table border="1">
            <tr>
                <th>BeerName</th>
            </tr>
            <c:forEach items="${beers}" var="beer" >
                <tr>
                    <td><c:out value="${beer.getName()}" /></td>
                </tr>
            </c:forEach>
       </table>
</body>
</html>