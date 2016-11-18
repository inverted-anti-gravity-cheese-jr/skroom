<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8"  />
		<!-- Google's Open Sans -->
		<link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">

		<s:bundle name="jquery" />
		<s:bundle name="jquery-ui" />
		<s:bundle name="bootstrap" />
		<s:bundle name="main" />

		<title>Test</title>
	</head>
	<body>
		<div class="login-form signup-form<%if (request.getAttribute("signupError") != null) { out.print(" withmessage"); }%>">
			<form action="signup" method="post">
				<%@include file="/WEB-INF/views/subviews/forms/signupForm.jsp" %>
				<input class="btn btn-sm btn-accent" type="submit" />
			</form>
			<a href="."><button class="btn btn-sm btn-accent">Cancel</button></a>
			<c:if test="${signupError != null}">
				<span class="login-error">${signupError}</span>
			</c:if>
		</div>
	</body>
</html>
