<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>

<div class="dashboard">
	<h2>Hello ${ loggedUser.name }.</h2>
	<h1>Welcome to TestProject.</h1>
	<p class="project-description">Lorem ipsum dolor sit amet,
		consectetur adipiscing elit. Aliquam at porttitor sem. Aliquam erat
		volutpat. Donec placerat nisl magna, et faucibus arcu condimentum sed.</p>
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