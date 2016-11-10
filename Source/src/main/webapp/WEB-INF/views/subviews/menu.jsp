<div id="main-menu" class="col-sm-3 sidebar">
	<h1>Skroom</h1>
	<ul class="menu-list">
		<li><a>Select project</a></li>
	</ul>
    <c:if test="${canCreateProjects}">
    <ul class="project-list">
        <c:forEach var="project" items="${availableProjects}">
            <li><a>${project.name}</a></li>
        </c:forEach>
        <li><a href="addProject">Add project</a></li>
    </ul>
    </c:if>
	<hr>
	<!-- Entypo pictograms by Daniel Bruce — www.entypo.com -->
	<!-- https://creativecommons.org/licenses/by-sa/4.0/ -->
	<ul class="nav nav-sidebar">
        
        
        <s:menuitem href="dashboard" imgSrc="/resources/svg/gauge.svg" disabled="${not isProjectSelected}">Dashboard</s:menuitem>
        <s:menuitem href="productbacklog" siteName="productBacklog" imgSrc="/resources/svg/list.svg" disabled="${not isProjectSelected}">Product backlog</s:menuitem>
		<s:menuitem href="sprintbacklog" siteName="sprintBacklog" disabled="${not isProjectSelected}">Sprint backlog</s:menuitem>
        <s:menuitem href="kanban" disabled="${not isProjectSelected}">Kanban board</s:menuitem>
        <s:menuitem href="issues" disabled="${not isProjectSelected}">Issues</s:menuitem>
        <s:menuitem href="settings" disabled="${not isProjectSelected}">Project settings</s:menuitem>
	</ul>
	<hr>
	<ul class="nav nav-sidebar">
        <%  %>
        
		<s:menuitem href="userSettings">User settings</s:menuitem>
        <s:menuitem href="userAdmin" disabled="${not isAdmin}">User privilages settings</s:menuitem>
		<s:menuitem href="logout">Sign out</s:menuitem>
	</ul>
</div>
