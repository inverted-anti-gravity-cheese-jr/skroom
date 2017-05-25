<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>

    <div class="management-bar" style="margin-top: 10px; margin-bottom: 10px;">
        <span class="bar-text">Choose sprint
            <select id="sprintSelect" onchange="selectSprintInMenu(this);">
            <c:forEach var="sprint" items="${sprintsWithoutLast}">
                <option value="${sprint.id}">${sprint.name}</option>    
            </c:forEach>
                <option value="${lastSprint.id}" selected="selected">${lastSprint.name}</option>
            </select>
            <script type="text/javascript">
                reloadSprintForm("sprintSelect");
            </script>
        </span>
    </div>
<c:choose>
    <c:when test="${fn:length(userStories) == 0}">
        <div class="errorInfo">No user stories present. <a href="productbacklog">Add a story</a></div>
    </c:when>
    <c:when test="${fn:length(tasks) == 0}">
        <div class="errorInfo">No tasks in selected sprint. Do you want to <a href="sprintbacklog">create a task</a>?</div>
    </c:when>
    <c:otherwise>
    <table id="taskBoard" class="striped">
    	<thead>
    		<tr>
    		    <th id="columnUserStories">User Stories</th>
    			<c:forEach var="taskColumn" items="${taskStatuses}">
    				<th id="column${taskColumn.id}">${taskColumn.name}</th>
    			</c:forEach>
    		</tr>
    	</thead>
    	<tbody>
            <c:forEach var="userStory" items="${userStories}">
                <tr class="userStory-${userStory.id}">
                    <td class="userStoryTile">
                        ${userStory.name}
                    </td>
                    <c:forEach var="taskColumn" items="${taskStatuses}">
                    <td class="canDrop taskColumn-${taskColumn.id}">
                    </td>
                    </c:forEach>
                </tr>
            </c:forEach>
    	</tbody>
    </table>
    </c:otherwise>
</c:choose>
<script>
<c:forEach var="task" items="${tasks}">
    $(".userStory-${task.userStory.id} .taskColumn-${task.status.id}").append(<%@ include file="/WEB-INF/views/model/taskView.jsp"%>);
</c:forEach>
    //hide empty userStories
    $('tbody tr').each(function() {
        if(!$(this).find(".taskItem").length > 0) {
            $(this).css('display', 'none');
        }
    });
</script>
<script>
    $("#page-content").css("width", "inherit");
    $("#page-content").css("min-width", "75%");

    <c:if test="${sprintId == lastSprint.id}">
        var container = $("#taskBoard");
        makeTaskItemsDraggable(container);
        makeTaskColumnsDroppable(container);
    </c:if>

    function makeTaskItemsDraggable(container) {
        container.find(".taskItem.data-canDrag").each( function() {
            $(this).draggable({
                helper : "clone",
                revert: "invalid",
                opacity: 0.5,
                cursor: "move",
                containment : $(this).parents('tr'),
            });
        });
    }

    function makeTaskColumnsDroppable(container) {
        container.find(".canDrop").each(function() {
            $(this).droppable({
                accept: ".taskItem",
                hoverClass: "ui-drop-hover",
                drop: function(event, ui) {
                    var item = ui.draggable;
                    var dropped = $(ui.draggable);
                    var droppedTrParent = $(event.target);
                    var index = $(this).index();
                    dropped.detach();
                    droppedTrParent.append(dropped);
                    var classes = droppedTrParent.attr('class').split(/\s/);
                    for (var i = 0, len = classes.length; i < len; i++) {
                        if (/^taskColumn-/.test(classes[i])) {
                            handleTaskUpdate(item, classes[i].substring(11));
                        }
                    }
                }
            });
        });
    }

	/*function fitView(container) {
		var fullWidth = 0;
		$(".kanban-column").each(function() {
			fullWidth += $(this).outerWidth( true );
		});
		container.css('min-width', fullWidth + 5);
	}
	*/

	function handleTaskUpdate(item, newTaskStatusId) {
		var taskId = item.find(".taskId").data('taskid');
		//var taskName = item.find(".taskName").val();
		//var taskAssignee = item.find(".taskAssignee").val();
		//var taskSP = item.find(".taskSP").val();
		var serializedItem = {
			TaskId : taskId,
		}
		updateTaskStatus(taskId, newTaskStatusId);
	}

	function updateTaskStatus(taskId, newTaskStatusId) {
		$.ajax({
			type : "POST",
			data : { 'taskId' : taskId, 'statusId' : newTaskStatusId },
			url : "rest/task/update",
			complete : function(response) {
				//location.reload();
			}

		});
	}
</script>

</t:index>