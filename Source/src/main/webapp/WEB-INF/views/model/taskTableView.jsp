<tr class="task-row" style="border-color: ${task.color};">
    <td><a href="editTask/${task.id}/">#${task.id}</a></td>
    <td><a href="editTask/${task.id}/">${task.name}</a></td>
    <td><span class="user-story-round-text">${task.assignee.name}</span></td>
    <td><span class="user-story-round-text" style="background-color: <s:interpolate-color from="#ffc107" to="#f44336" value="${task.estimatedTime}" min="1" max="24" />">${task.estimatedTime}</span></td>    
    <td>${task.status.name}</td>
    <td>TODO</td>
</tr>