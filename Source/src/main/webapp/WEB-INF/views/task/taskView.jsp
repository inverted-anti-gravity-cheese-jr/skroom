
<c:set var="rand"><%= java.lang.Math.round(java.lang.Math.random() * 2) %></c:set>


<c:choose>
    <c:when test="${rand % 3 == 0}">
<ul class="taskItem data-canDrag" style="background-color: #d9ff00;">
    </c:when>
    <c:when test="${rand % 3 == 1}">
<ul class="taskItem data-canDrag" style="background-color: #008cff;">
    </c:when>
    <c:otherwise>
<ul class="taskItem data-canDrag" style="background-color: #ff2f00;">
    </c:otherwise>
</c:choose>

<!--
<ul class="taskItem data-canDrag">
 -->
    <li class="taskId" hidden="true" data-taskid ="${task.id}">${task.id}</li>
    <li class="taskName">${task.name}</li>
    <li class="taskAssignee">Assignee: ${task.assignee.name}</li>
    <li class="taskSP">Story Points: ${task.storyPoints}</li>
</ul>
