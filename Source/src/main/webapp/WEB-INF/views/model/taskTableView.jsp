<tr class="task-row" style="border-color: ${task.color};">
    <td><a href="editTask/${task.id}/">#${task.id}</a></td>
    <td><a href="editTask/${task.id}/">${task.name}</a></td>
    <td>
        <c:if test="${not empty task.assignee}"><span class="user-story-round-text">${task.assignee.name}</span></c:if>
        <c:if test="${empty task.assignee}"><span class="user-story-round-text" style="background-color: #ff9d00;">
            <a href="assignTaskToMe/${task.id}">Unassigned</a>
            </span></c:if>
    </td>
    <td><span class="user-story-round-text" style="color: black; background-color: <s:interpolate-color from="#ffff07" to="#f44336" value="${task.estimatedTime}" min="1" max="24" />">${task.estimatedTime}</span></td>    
    <td>${task.status.name}</td>
    <td>TODO</td>
</tr>