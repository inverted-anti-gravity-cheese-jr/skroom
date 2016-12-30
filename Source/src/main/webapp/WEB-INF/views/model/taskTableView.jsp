<tr class="task-row" style="border-color: ${task.color};">
    <td><a href="viewTask/${task.id}">#${task.id}</a></td>
    <td><a href="viewTask/${task.id}">${task.name}</a></td>
    <td>
        <c:if test="${not empty task.assignee}"><span class="round-table-label">${task.assignee.name}</span></c:if>
        <c:if test="${empty task.assignee}"><span class="round-table-label" style="background-color: #ff9d00;">
            <a href="assignTaskToMe/${task.id}" style="color: black;">Unassigned</a>
            </span></c:if>
    </td>
    <td><span class="round-table-label" style="color: black; background-color: <s:interpolate-color from="#ffff07" to="#f44336" value="${task.estimatedTime}" min="1" max="24" />">${task.estimatedTime}</span></td>    
    <td>${task.status.name}</td>
    <td>
        <span class="round-table-label" style="background-color: ${task.userStory.storyPoints.color};">
            <a href="viewUserStory/${task.userStory.id}/" style="color: ${task.userStory.storyPoints.textColor};">${task.userStory.name}</a>
        </span>
    </td>
</tr>