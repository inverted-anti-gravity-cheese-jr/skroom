<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    
<t:index>
    <c:if test="${not empty task}">
    <script type="text/javascript">
        var task_taskStatus = ${task.status.name};
    </script>
    </c:if>
    
    <h1>Create task</h1>
	<form class="task-form" method="post">
        <div class="management-bar">
            <input type="submit" class="btn" value="Save" />
        </div>
        
        
		<input name="taskName" type="text" class="form-control" placeholder="Task name" value="${task.name}" />  
		<textarea name="taskDescription" class="form-control" placeholder="Description">${userStory.description}</textarea> 
		<input id="taskAssignee" name="taskAssignee" type="text" class="form-control" placeholder="Assignee" value="${task.assignee.name}" />
        <div id="taskColorPicker" class="input-group">
            <input name="taskColor" type="text" class="form-control" placeholder="Color" value="${taskColor}" />
            <span class="input-group-addon"><i></i></span>
        </div>
		<input name="taskEstimated" type="text" class="form-control" placeholder="Estimated time" vaxlue="${task.estimatedTime}" />  
		<select id="task-status-select" name="taskStatus" class="form-control">
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
        
        $("#taskColorPicker").colorpicker();
        reloadTaskForm(task_taskStatus);
	</script>
</t:index>