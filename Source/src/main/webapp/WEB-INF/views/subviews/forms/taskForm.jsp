<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<t:index>
	<form class="task-form">
		<input name="taskName" type="text" class="form-control" placeholder="Task name" value="${userStory.name}" />  
		<textarea name="taskDescription" class="form-control" placeholder="Description">${userStory.description}</textarea> 
		<input id="taskAssignee" name="taskAssignee" type="text" class="form-control" placeholder="Assignee" value="${userStory.name}" />
		<input name="taskColor" type="text" class="form-control" placeholder="Color" value="${userStory.name}" />
		<input name="taskEstimated" type="text" class="form-control" placeholder="Estimated time" vaxlue="${userStory.name}" />  
		<select id="story-points-select" name="storyPoints" class="form-control">
			<c:forEach var="taskStatus" items="${taskStatuses}">
				<option>${taskStatus.name}</option>
			</c:forEach>
		</select>
	</form>
	
	<script type="text/javascript">
		var users = [
			<c:forEach var="user" items="${users}" varStatus="loop">
			"${user.name} [${user.email}]"<c:if test="${loop.index < fn:length(users) - 1}">,</c:if>
			</c:forEach>
		];
		
		var taskAssigneeForm = $( "#taskAssignee" );
		taskAssigneeForm.autocomplete({
			source: users,
			appendTo: taskAssigneeForm.parent()
		}).autocomplete( "widget" ).addClass( "form-control" );
	</script>
</t:index>