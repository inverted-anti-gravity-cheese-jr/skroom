<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="pl.pg.eti.kio.skroom.settings.AccountManagementSettings" %>
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
	<%
		boolean canAnonymousCreateAccount = pl.pg.eti.kio.skroom.settings.AccountManagementSettings.canAnonymousCreateAccount();
	%>
	<body>
		<div class="login-form<%if (request.getAttribute("loginError") != null) { out.print(" withmessage"); }%>">
			<form action="login" method="post">
				<h3>Username or email</h3>
				<input type="text" name="name" />
				<h3>Password</h3>
				<input type="password" name="password" />
				<input class="btn btn-sm btn-accent<%if (!canAnonymousCreateAccount) { out.print(" noallow"); } %>" type="submit" />
			</form>

			<% if (canAnonymousCreateAccount) { %>
			<a href="signup"><button class="btn btn-sm btn-accent">Sign up</button></a>
			<% } %>
			<c:if test="${loginError != null}">
				<span class="login-error">${loginError}</span>
			</c:if>
		</div>
	</body>
</html>
