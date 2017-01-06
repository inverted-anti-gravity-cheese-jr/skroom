package pl.pg.eti.kio.skroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import pl.pg.eti.kio.skroom.model.*;
import pl.pg.eti.kio.skroom.model.dao.IssueDao;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marek Czerniawski on 2017-01-06.
 */
@Controller
@SessionAttributes({"loggedUser", "userSettings"})
public class IssuesManagmentController {

    private static final String ISSUE_NAME_CANNOT_BE_EMPTY = "Issue name cannot be empty";
    private static final String ISSUE_PRIORITY_MUST_BE_A_NUMBER = "Issue priority must be a valid number";
    private static final String ISSUE_STATUS_NOT_FOUND = "Wrong issue status";
    private static final String ISSUE_USER_NOT_FOUND = "Wrong issue assignee";
    private static final String ISSUE_TASK_NOT_FOUND = "Wrong issue task";

    @Autowired private DefaultTemplateDataInjector injector;
    @Autowired private WebRequest request;
    @Autowired private IssueDao issueDao;

    @PostConstruct
    public void initDatabase() {
        DatabaseSettings.initConnection("jdbc:sqlite:data.db");
    }

    @ModelAttribute("loggedUser")
    public User defaultNullUser() {
        return new User();
    }

    @ModelAttribute("userSettings")
    public UserSettings defaultNullUserSettings() {
        return new UserSettings();
    }

    @RequestMapping(value = "/issues", method = RequestMethod.GET)
    public ModelAndView showIssues(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings) {
        if(!checkPermission(user, userSettings)) {
            return new ModelAndView("redirect:/");
        }

        Connection dbConnection = DatabaseSettings.getDatabaseConnection();

        List<Issue> issues = issueDao.listAllIssues(dbConnection, userSettings.getRecentProject());

        ModelAndView model = injector.getIndexForSiteName(Views.PROJECT_ISSUES_FORM_JSP_LOCATION, "Issues", userSettings.getRecentProject(), user, request);
        model.addObject("projectIssues", issues);
        return model;
    }

    @RequestMapping(value = "/issue/{projectId}/", method = RequestMethod.GET)
    public ModelAndView addIssue(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings, @PathVariable int projectId) {
        if(!checkPermission(user, userSettings)) {
            return new ModelAndView("redirect:/");
        }

        Connection dbConnection = DatabaseSettings.getDatabaseConnection();

        return showIssueModel(user, userSettings, dbConnection, null, null, null);
    }

    @RequestMapping(value = "/issue/{projectId}/", method = RequestMethod.POST)
    public ModelAndView addIssue(@ModelAttribute("loggedUser") User user,
                                 @ModelAttribute("userSettings") UserSettings userSettings,
                                 @PathVariable int projectId,
                                 @RequestParam String issueName,
                                 @RequestParam String issueDescription,
                                 @RequestParam String issuePriority,
                                 @RequestParam String issueStatus,
                                 @RequestParam String issueAssignee,
                                 @RequestParam String issueTask) {
        return addOrEditIssue(user, userSettings, projectId, null, issueName, issueDescription, issuePriority, issueStatus, issueAssignee, issueTask);
    }

    @RequestMapping(value = "/issue/{projectId}/{issueId}/", method = RequestMethod.POST)
    public ModelAndView saveIssue(@ModelAttribute("loggedUser") User user,
                                  @ModelAttribute("userSettings") UserSettings userSettings,
                                  @PathVariable int projectId,
                                  @PathVariable int issueId,
                                  @RequestParam String issueName,
                                  @RequestParam String issueDescription,
                                  @RequestParam String issuePriority,
                                  @RequestParam String issueStatus,
                                  @RequestParam String issueAssignee,
                                  @RequestParam String issueTask) {
        return addOrEditIssue(user, userSettings, projectId, issueId, issueName, issueDescription, issuePriority, issueStatus, issueAssignee, issueTask);
    }

    private ModelAndView addOrEditIssue(User user, UserSettings userSettings, int projectId, Integer issueId, String issueName, String issueDescription, String priorityString, String issueStatus, String issueAssignee, String issueTask) {
        if(!checkPermission(user, userSettings)) {
            return new ModelAndView("redirect:/");
        }

        Connection dbConnection = DatabaseSettings.getDatabaseConnection();

        TaskStatus status = issueDao.fetchStatusByName(dbConnection, issueStatus, userSettings.getRecentProject());
        User assignee = issueDao.fetchUserNyName(dbConnection, issueAssignee, userSettings.getRecentProject());
        Task task = issueDao.fetchTaskByName(dbConnection, issueTask, userSettings.getRecentProject());

        Issue issue = new Issue();
        issue.setName(issueName);
        issue.setDescription(issueDescription);
        issue.setStatus(status);
        issue.setAssignee(assignee);
        issue.setTask(task);
        issue.setProject(userSettings.getRecentProject());

        List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(issue.getName())) {
            errors.add(ISSUE_NAME_CANNOT_BE_EMPTY);
        }
        if (StringUtils.isEmpty(priorityString) || !isInteger(priorityString)) {
            errors.add(ISSUE_PRIORITY_MUST_BE_A_NUMBER);
        } else {
            issue.setPriority(Integer.valueOf(priorityString));
        }
        if (!StringUtils.isEmpty(issueStatus) && status == null) {
            errors.add(ISSUE_STATUS_NOT_FOUND);
        }
        if (!StringUtils.isEmpty(issueAssignee) && assignee == null) {
            errors.add(ISSUE_USER_NOT_FOUND);
        }
        if (!StringUtils.isEmpty(issueTask) && task == null) {
            errors.add(ISSUE_TASK_NOT_FOUND);
        }

        if (!errors.isEmpty()) {
            Issue view = null;
            if (issueId != null) {
                view = issueDao.fetchById(dbConnection, issueId, userSettings.getRecentProject());
            }
            return showIssueModel(user, userSettings, dbConnection, view, issue, errors);
        }

        if (issueId == null) {
            issueDao.add(dbConnection, issue);
            return new ModelAndView("redirect:/issues/");
        } else {
            issue.setId(issueId);
            issueDao.update(dbConnection, issue);
            return new ModelAndView(MessageFormat.format("redirect:/issue/{0}/{1}/", projectId, issueId));
        }
    }

    @RequestMapping(value = "/issue/{projectId}/{issueId}/", method = RequestMethod.GET)
    public ModelAndView showIssue(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings, @PathVariable int projectId, @PathVariable int issueId) {
        if(!checkPermission(user, userSettings)) {
            return new ModelAndView("redirect:/");
        }

        Connection dbConnection = DatabaseSettings.getDatabaseConnection();

        Issue issue = issueDao.fetchById(dbConnection, issueId, userSettings.getRecentProject());

        if (issue == null) {
            return new ModelAndView(MessageFormat.format("redirect:/issue/{0}/", projectId));
        }

        return showIssueModel(user, userSettings, dbConnection, issue, issue, null);
    }

    private ModelAndView showIssueModel(User user, UserSettings userSettings, Connection dbConnection, Issue view, Issue edit, List<String> errors) {
        ModelAndView model = injector.getIndexForSiteName(Views.PROJECT_ISSUE_FORM_JSP_LOCATION, "Issue", userSettings.getRecentProject(), user, request);
        model.addObject("projectIssue", view);
        model.addObject("projectIssueEdit", edit);
        model.addObject("availableIssueStatuses", issueDao.availableIssueStatuses(dbConnection, userSettings.getRecentProject()));
        model.addObject("availableIssueUsers", issueDao.availableIssueUsers(dbConnection, userSettings.getRecentProject()));
        model.addObject("availableIssueTasks", issueDao.availableIssueTasks(dbConnection, userSettings.getRecentProject()));
        model.addObject("issueErrors", errors);
        return model;
    }

    @RequestMapping(value = "/removeIssue/{projectId}/{issueId}/", method = RequestMethod.GET)
    public ModelAndView removeIssue(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings, @PathVariable int projectId, @PathVariable int issueId) {
        if(!checkPermission(user, userSettings)) {
            return new ModelAndView("redirect:/");
        }

        Connection dbConnection = DatabaseSettings.getDatabaseConnection();

        issueDao.remove(dbConnection, issueId, projectId);
        return new ModelAndView("redirect:/issues/");
    }

    private boolean checkPermission(User user, UserSettings userSettings) {
        if (user == null || user.getId() < 0) {
            return false;
        }
        if (userSettings.getRecentProject() == null) {
            return false;
        }
        return true;
    }

    private boolean isInteger(String str) {
        return str.matches("^-?\\d+$");
    }
}