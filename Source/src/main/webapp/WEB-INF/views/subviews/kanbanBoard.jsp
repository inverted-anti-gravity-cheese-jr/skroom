<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>

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

<table id="taskBoard">
	<thead>
		<tr>
			<c:forEach var="taskColumn" items="${taskStatuses}">
				<th id="column${taskColumn.id}">${taskColumn.name}</th>
			</c:forEach>
		</tr>
	</thead>
	<tbody>
		<tr>
			<c:forEach var="taskColumn" items="${taskStatuses}">
			<td class="canDrop">
				<%@ include file="/WEB-INF/views/model/taskView.jsp"%>
			</td>
			</c:forEach>
		</tr>
		
		<tr>
			<c:forEach var="taskColumn" items="${taskStatuses}">
			<td class="canDrop">
				<%@ include file="/WEB-INF/views/model/taskView.jsp"%>
			</td>
			</c:forEach>
		</tr>
	</tbody>
</table>

<script>
	function fitView(container) {
		var fullWidth = 0;
		$(".kanban-column").each(function() {
			fullWidth += $(this).outerWidth( true );
		});
		container.css('min-width', fullWidth + 5);
	}
	
	function makeTaskItemsDraggable(container) {
		

		$('.taskItem.canDrag').each( function() {
			$(this).draggable({
				helper : "clone",
				zIndex : 100,
				revert: "invalid",
				opacity: 0.5,
				containment : container
			});
		});
	}

	function makeTaskItemStatusDroppable(container) {
		container.find(".canDrop").each(function() {
			$(this).droppable({
				accept: ".taskItem",
				drop: function(event, ui) {
					var dropped = $(ui.draggable);
					var droppedTrParent = dropped.closest("tr");
					var index = $(this).index();
					dropped.detach();
					droppedTrParent.find("td:eq("+index+")").append(dropped);
				}
			});
		});
		
		/*
		
		container.find("td.dragDrop").each(function() {
			var acceptItem = $(".taskItem").not($(this).find(".taskItem"));
			$(this).droppable({
				accept : acceptItem,
				cursor : 'auto',
				drop : function(event, ui) {
					var item = ui.draggable;
					var newTaskStatus = $(this).attr("id");
					console.log("Update task");
					//handleTaskUpdate(item, newTaskStatus);
				}
			});
		});
		*/
	}

	/*
	function handleTaskUpdate(item, newTaskStatus) {
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
	}
	*/

	var container = $("#taskBoard");
	fitView(container);
	makeTaskItemsDraggable(container);
	makeTaskItemStatusDroppable(container);
</script>

</t:index>