<div id="main-menu" class="col-sm-3 sidebar">
	<h1>Skroom</h1>
	<ul class="menu-list">
		<li>Test project</li>
	</ul>
	<hr>
	<!-- Entypo pictograms by Daniel Bruce — www.entypo.com -->
	<!-- https://creativecommons.org/licenses/by-sa/4.0/ -->
	<ul class="nav nav-sidebar">
		<li
			<%if (request.getAttribute("siteName").equals("dashboard")) {
				out.print("class=\"active\""); }%>>
			<a href="dashboard"><img src="<c:url value="/resources/svg/gauge.svg" />" />Dashboard</a>
		</li>
		<li
			<%if (request.getAttribute("siteName").equals("productBacklog")) {
				out.print("class=\"active\""); }%>>
			<a href="productbacklog"><img src="<c:url value="/resources/svg/list.svg" />" />Product backlog</a>
		</li>
		<li
			<%if (request.getAttribute("siteName").equals("sprintBacklog")) {
				out.print("class=\"active\""); }%>>
			<a href="sprintbacklog">Sprint backlog</a>
		</li>
		<li
			<%if (request.getAttribute("siteName").equals("kanban")) {
				out.print("class=\"active\""); }%>>
			<a href="kanban">Kanban board</a>
		</li>
		<li
			<%if (request.getAttribute("siteName").equals("issues")) {
				out.print("class=\"active\""); }%>>
			<a href="issues">Issues</a>
		</li>
		<li
			<%if (request.getAttribute("siteName").equals("settings")) {
				out.print("class=\"active\""); }%>>
			<a href="settings">Project settings</a>
		</li>
	</ul>
	<hr>
	<ul class="nav nav-sidebar">
		<li><a href="#">User settings</a></li>
		<li><a href="logout">Sign out</a></li>

		<!--
		<li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li><li><a href="logout">Sign out</a></li>
	-->
	</ul>
</div>
