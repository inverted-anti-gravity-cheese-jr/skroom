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

<!--
<div id="taskBoard" class="onerow">
	<c:forEach var="taskColumn" items="${taskStatuses}">
		<div id="column${taskColumn.id}" class="kanban-column dragDrop">
		<h3>
			${taskColumn.name}
		</h3>
		<c:if test="${not empty tasks}">
			<c:forEach var="task" items="${tasks}">
				<c:if test="${ task.status.id ==  taskColumn.id}">
					<%@ include file="/WEB-INF/views/model/taskView.jsp"%>
				</c:if>
			</c:forEach>
		</c:if>
	</div>
	</c:forEach>
</div>
-->

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
		<tr>
		    <td>
		        User Story
		    </td>
			<c:forEach var="taskColumn" items="${taskStatuses}">
			<td class="canDrop">
				<%@ include file="/WEB-INF/views/model/taskView.jsp"%>
				<%@ include file="/WEB-INF/views/model/taskView.jsp"%>
			</td>
			</c:forEach>
		</tr>
		<tr>
		    <td>
                User Story
            </td>
			<c:forEach var="taskColumn" items="${taskStatuses}">
			<td class="canDrop">
				<%@ include file="/WEB-INF/views/model/taskView.jsp"%>
			</td>
			</c:forEach>
		</tr>
	</tbody>
</table>

<script>
    var container = $("#taskBoard");
	makeTaskItemsDraggable(container);
	makeTaskColumnsDroppable(container);

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
                    console.log(item);
                    var dropped = $(ui.draggable);
                    var droppedTrParent = $(event.target);
                    console.log(droppedTrParent);
                    var index = $(this).index();
                    dropped.detach();
                    droppedTrParent.append(dropped);
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

	/*function handleTaskUpdate(item, newTaskStatus) {
		var taskId = item.find(".taskId").data('taskid');
		//var taskName = item.find(".taskName").val();
		//var taskAssignee = item.find(".taskAssignee").val();
		//var taskSP = item.find(".taskSP").val();
		var serializedItem = {
			TaskId : taskId,
		}
		updateTaskStatus(newTaskStatus, taskId);
	}

	function updateTaskStatus(newTaskStatus, taskId) {
		$.ajax({
			type : "POST",
			data : { 'taskId' : taskId, 'status' : newTaskStatus },
			url : "rest/task/update",
			complete : function(response) {
				location.reload();
			}

		});
		return false;
	}*/
</script>

</t:index>