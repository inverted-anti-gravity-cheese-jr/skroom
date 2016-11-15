<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<c:set var="newLine" value="\n" />
<t:index>

<div class="dashboard">
	<h2>Hello ${ loggedUser.name }.</h2>
	<h1>Welcome to ${userSettings.recentProject.name}.</h1>
	<p class="project-description">${userSettings.recentProject.description}</p>
	<h2>What do you want to do?</h2>
	<button class="btn btn-sm btn-accent">Create task</button>
	<button class="btn btn-sm btn-accent">Create issue</button>
	<button class="btn btn-sm btn-accent">View tasks</button>
	<h2>Tasks assigned to you</h2>
	<div class="dashboard-task-list">
		<ul>
			<c:if test="${not empty list}">
				<c:forEach var="task" items="${list}">
					<c:if test="${ (task.assignee.name eq loggedUser.name) and (task.status != 'COMPLETED')}">
						<li>${task.name}</li>
					</c:if>
				</c:forEach>
			</c:if>
		</ul>
	</div>
</div>

</t:index>