package pl.pg.eti.kio.skroom.controller;

/**
 * Container for all views location. Every location be imported using clause "import static ...".
 *
 * @author Wojciech Stanisławski, Krzysztof Świeczkowski
 * @since 22.08.16
 */
class Views {
	static final String LOGIN_JSP_LOCATION = "standalone/login";
	static final String SIGNUP_JSP_LOCATION = "standalone/signup";

	static final String DASHBOARD_JSP_LOCATION = "subviews/dashboard";
	static final String KANBAN_BOARD_FORM_JSP_LOCATION = "subviews/kanbanBoard";
	static final String USER_ADMIN_FORM_JSP_LOCATION = "subviews/userAdmin";
	static final String USER_SETTINGS_FORM_JSP_LOCATION = "subviews/userSettings";
	static final String PROJECT_SETTINGS_FORM_JSP_LOCATION = "subviews/projectSettings";
	static final String PROJECT_ISSUES_FORM_JSP_LOCATION = "subviews/projectIssues";
	static final String SPRINT_BACKLOG_FORM_JSP_LOCATION = "subviews/sprintBacklog";
	static final String PRODUCT_BACKLOG_FORM_JSP_LOCATION = "subviews/productBacklog";

	static final String USER_STORY_FORM_JSP_LOCATION = "subviews/forms/userStoryForm";
	static final String TASK_FORM_JSP_LOCATION = "subviews/forms/taskForm";
}