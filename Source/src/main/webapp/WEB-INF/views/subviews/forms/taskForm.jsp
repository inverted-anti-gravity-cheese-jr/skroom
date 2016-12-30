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
        var task_taskStatus = "${task.status.name}";
        
        function switchViews(showEdit) {
            if(showEdit) {
                $(".task-body").slideUp(function () {
                    $(".task-form").slideDown();
                });

            }
            else {
                $(".task-form").slideUp(function () {
                    $(".task-body").slideDown();
                });
            }
        }
    </script>
    <h1>${task.name}</h1>
    <div class="task-body">
        <div class="management-bar">
            <button type="button" class="btn" onclick="switchViews(true)">Edit</button>
        </div>

        <div class="panel panel-info">
            <div class="panel-heading">Details</div>
            <div class="panel-body">
                <b>Assignee</b> ${task.assignee.name} <br />
                <b>Status</b> ${task.status.name} <br />
                <b>Estimated time</b><span class="round-table-label" style="color: black; background-color: <s:interpolate-color from="#ffff07" to="#f44336" value="${task.estimatedTime}" min="1" max="24" />">${task.estimatedTime}</span><br />
                <b>Color</b> ${task.color} <i style="background-color: ${task.color};"></i> <br />
                <b>User Story</b> <span class="round-table-label" style="background-color: ${task.userStory.storyPoints.color};"> <a href="viewUserStory/${task.userStory.id}/" style="color: ${task.userStory.storyPoints.textColor};">${task.userStory.name}</a> </span> <br />
                <b>Sprint</b> ${task.sprint.name}
            </div>
        </div>
        <div class="panel panel-info">
            <div class="panel-heading">Description</div>
            <div class="panel-body">
                ${task.description}
            </div>
        </div>
    </div>
        
    <form class="task-form" action="../editTask/${task.id}" method="post" style="display: none;">
        <div class="management-bar">
            <input type="submit" class="btn" value="Save changes" />
            <button type="button" class="btn" onclick="switchViews(false)">Cancel</button>
            <a href="../removeTask/${task.id}" class="btn red pull-right">Delete</a>
        </div>
    </c:if>
    
    <c:if test="${empty task}">
    <h1>Create task</h1>
	<form class="task-form" method="post">
        <div class="management-bar">
            <input type="submit" class="btn" value="Save" />
        </div>
    </c:if>
        
        
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
        
        <select id="user-story-select" name="userStoryId" class="form-control">
			<c:forEach var="userStory" items="${userStories}">
				<option value="${userStory.id}">${userStory.name}</option>
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