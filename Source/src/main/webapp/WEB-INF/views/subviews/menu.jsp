<div id="main-menu" class="col-xs-12 col-sm-3 sidebar">
	<h1>Skroom</h1>
	<ul class="menu-list">
		<li><a><i class="fa fa-ellipsis-v" aria-hidden="true"></i>Select project</a></li>
	</ul>
    <ul class="project-list">
        <c:forEach var="projectContainer" items="${menuAvailableProjects}">
            <li>
                <a href="javascript:selectProject(${projectContainer.project.id})">${projectContainer.project.name}</a>
                <c:if test="${projectContainer.editable}">
                <a href="<c:out value='${pageContext.request.contextPath}'/>/editProject/${projectContainer.project.id}"> <i class="fa fa-pencil" aria-hidden="true"></i></a>
                </c:if>
            </li>
        </c:forEach>
        <c:if test="${canCreateProjects}">
        <li><a href="<c:out value='${pageContext.request.contextPath}'/>/addProject">Add project</a></li>
        </c:if>
    </ul>
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
        <%  %>
        
		<s:menuitem href="userSettings" fa="fa fa-sliders">User settings</s:menuitem>
        <s:menuitem href="userAdmin" fa="fa fa-users" disabled="${not isAdmin}">User privilages settings</s:menuitem>
		<s:menuitem href="logout" fa="fa fa-sign-out">Sign out</s:menuitem>
	</ul>
</div>
