<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>

<div id="taskBoard" class="onerow">
	<c:forEach var="taskColumn" items="${taskStatuses}">
		<div id="column${taskColumn.id}" class="col5thsr dragDrop">
		<h2>
			${taskColumn.name} <span class="kanban-group-badge"></span>
		</h2>
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

<script>
	function makeTaskItemsDraggable($container) {
		$container.css('overflow', 'hidden');

		$('.dragDrop .taskItem.data-canDrag').each(
				function() {
					var thisItemContainer = $(this).parent();
					var thisItem = $(this);
					(thisItem)
							.draggable({
								helper : "clone",
								cursor : "move",
								zIndex : 100,
								containment : $container,
								start : function(event, ui) {
									thisItem.css("opacity", "0.5");
									$container.find("div.dragDrop").not(
											thisItemContainer).addClass(
											"highlight");
								},
								stop : function(event, ui) {
									thisItem.css("opacity", "1");
									$container.find("div.dragDrop").not(
											thisItemContainer).removeClass(
											"highlight");
								}
							});
				});
	}

	function makeTaskItemStatusDroppable($container) {
		$container.find("div.dragDrop").each(function() {
			var acceptItem = $(".taskItem").not($(this).find(".taskItem"));
			$(this).droppable({
				accept : acceptItem,
				cursor : 'auto',
				drop : function(event, ui) {
					var item = ui.draggable;
					var newTaskStatus = $(this).attr("id");
					handleTaskUpdate(item, newTaskStatus);
				}
			});
		});
	}

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

	var $container = $("#taskBoard");
	makeTaskItemsDraggable($container);
	makeTaskItemStatusDroppable($container);
</script>

</t:index>