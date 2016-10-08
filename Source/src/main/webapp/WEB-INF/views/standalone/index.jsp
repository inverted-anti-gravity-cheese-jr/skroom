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
		<div class="row">
			<%@include file="/WEB-INF/views/subviews/menu.jsp" %>
			<div id="page-content" class = "col-sm-9 col-sm-offset-3">
				<c:if test="${ siteName eq 'dashboard' }">
					<%@include file="/WEB-INF/views/subviews/dashboard.jsp" %>
				</c:if>
				<c:if test="${ siteName eq 'kanban' }">
					<%@include file="/WEB-INF/views/subviews/kanbanBoard.jsp" %>
				</c:if>
			</div>
		</div>
	</body>
</html>
