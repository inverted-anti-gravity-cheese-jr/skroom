<div id="main-menu" class="col-xs-12 col-sm-3 sidebar">
	<h1>Skroom</h1>
	<ul class="nav project-list-container">
		<li><a><i class="fa fa-ellipsis-h" aria-hidden="true"/></i>Project list</a></li>
		<ul class="nav project-list">
            <c:forEach var="project" items="${menuAvailableProjects}">
                <li>
                    <a class="project-link" href="javascript:selectProject(${project.id})"><i class="fa fa-sticky-note-o" aria-hidden="true"/></i>${project.name}</a>
                </li>
            </c:forEach>
            <c:if test="${canCreateProjects}">
            <li><a href="<c:out value='${pageContext.request.contextPath}'/>/addProject"><i class="fa fa-plus" aria-hidden="true"/></i>Add project</a></li>
            </c:if>
        </ul>
	</ul>
	<script>
        $('.project-list-container li a').on('click', function(e){
            $('.project-list-container').toggleClass('active');
        });
    </script>
	<hr>
	<ul class="nav nav-sidebar">
        
        <s:menuitem href="dashboard" fa="fa fa-tachometer" disabled="${not isProjectSelected}">Dashboard</s:menuitem>
        <s:menuitem href="productbacklog" fa="fa fa-list" disabled="${not isProjectSelected}">Product backlog</s:menuitem>
		<s:menuitem href="sprintbacklog" fa="fa fa-tasks" disabled="${not isProjectSelected}">Sprint backlog</s:menuitem>
        <s:menuitem href="kanban" fa="fa fa-sticky-note" disabled="${not isProjectSelected}">Kanban board</s:menuitem>
        <s:menuitem href="issues" fa="fa fa-fire" disabled="${not isProjectSelected}">Issues</s:menuitem>
        <s:menuitem href="settings" fa="fa fa-cogs" disabled="${not isProjectSelected}">Project settings</s:menuitem>
	</ul>
	<hr>
	<ul class="nav nav-sidebar">

		<s:menuitem href="userSettings" fa="fa fa-sliders">User settings</s:menuitem>
        <s:menuitem href="userAdmin" fa="fa fa-users" disabled="${not isAdmin}">User privilages settings</s:menuitem>
		<s:menuitem href="logout" fa="fa fa-sign-out">Sign out</s:menuitem>
	</ul>
</div>
