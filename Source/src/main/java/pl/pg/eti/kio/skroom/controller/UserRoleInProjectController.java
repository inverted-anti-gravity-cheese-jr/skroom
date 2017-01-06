package pl.pg.eti.kio.skroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.UserRolesInProject;
import pl.pg.eti.kio.skroom.model.UserSettings;
import pl.pg.eti.kio.skroom.model.dao.UserRolesInProjectDao;
import pl.pg.eti.kio.skroom.model.enumeration.UserRole;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pl.pg.eti.kio.skroom.controller.Views.USERROLEINPROJECT_FORM_JSP_LOCATION;

/**
 * Created by Marek Czerniawski on 2016-12-29.
 */
@Controller
@SessionAttributes({"loggedUser", "userSettings"})
public class UserRoleInProjectController {

    private static final String ROLE_CANNOT_BE_EMPTY = "Role cannot be empty";

    @Autowired
    private DefaultTemplateDataInjector injector;
    @Autowired private WebRequest request;
    @Autowired private UserRolesInProjectDao userRolesInProjectDao;

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

    @RequestMapping(value = "/userRoleInProject/", method = RequestMethod.GET)
    public ModelAndView userRoleInProject(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings) {
        if (!checkPermission(user))
            return new ModelAndView("redirect:/");
        ModelAndView model = injector.getIndexForSiteName(USERROLEINPROJECT_FORM_JSP_LOCATION, "Add userRoleInProject", userSettings.getRecentProject(), user, request);
        return model;
    }

    private boolean checkPermission(@ModelAttribute("loggedUser") User user) {
        if(user == null || user.getId() < 0) {
            boolean isAdmin = UserRole.ADMIN.equals(user.getRole());
            if (!isAdmin) {
                return false;
            }
        }
        return true;
    }

    @RequestMapping(value = "/userRoleInProject/{userRoleInProjectId}/", method = RequestMethod.GET)
    public ModelAndView userRoleInProject(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings, @PathVariable Integer userRoleInProjectId) {
        if (!checkPermission(user))
            return new ModelAndView("redirect:/");

        ModelAndView model = injector.getIndexForSiteName(USERROLEINPROJECT_FORM_JSP_LOCATION, "Edit userRoleInProject", userSettings.getRecentProject(), user, request);

        Connection dbConnection = DatabaseSettings.getDatabaseConnection();

        UserRolesInProject userRolesInProject = userRolesInProjectDao.fetchById(dbConnection, userRoleInProjectId);

        model.addObject("userRoleInProject", userRolesInProject);
        model.addObject("userRoleInProjectEdit", userRolesInProject);
        return model;
    }

    @RequestMapping(value = "/userRoleInProject/", method = RequestMethod.POST)
    public ModelAndView userRoleInProject(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings,
                                          String projectRole, String color, String privileges) {
        return addOrEditUserRoleInProject(user, userSettings, null, projectRole, color, Boolean.valueOf(privileges));
    }

    @RequestMapping(value = "/userRoleInProject/{userRoleInProjectId}/", method = RequestMethod.POST)
    public ModelAndView userRoleInProject(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings, @PathVariable Integer userRoleInProjectId,
                                          @RequestParam String projectRole, @RequestParam String color, @RequestParam String privileges) {
        return addOrEditUserRoleInProject(user, userSettings, userRoleInProjectId, projectRole, color, Boolean.valueOf(privileges));
    }

    private ModelAndView addOrEditUserRoleInProject(User user, UserSettings userSettings, Integer userRoleInProjectId, String role, String color, boolean privileges) {
        if (!checkPermission(user))
            return new ModelAndView("redirect:/");

        UserRolesInProject userRolesInProjectEdit = new UserRolesInProject();
        if (userRoleInProjectId != null) {
            userRolesInProjectEdit.setId(userRoleInProjectId);
        }
        userRolesInProjectEdit.setRole(role);
        userRolesInProjectEdit.setColor(color);
        userRolesInProjectEdit.setPrivileges(privileges);

        Connection dbConnection = DatabaseSettings.getDatabaseConnection();

        List<String> errors = validate(userRolesInProjectEdit);
        if (errors.isEmpty()) {
            if (userRoleInProjectId == null) {
                userRolesInProjectDao.insert(dbConnection, userRolesInProjectEdit);
            } else {
                userRolesInProjectDao.update(dbConnection, userRolesInProjectEdit);
            }
            return new ModelAndView("redirect:/userAdmin");
        } else {
            ModelAndView model = injector.getIndexForSiteName(USERROLEINPROJECT_FORM_JSP_LOCATION, "Edit userRoleInProject", userSettings.getRecentProject(), user, request);
            if (userRoleInProjectId != null) {
                model.addObject("userRoleInProject", userRolesInProjectDao.fetchById(dbConnection, userRoleInProjectId));
            }
            model.addObject("userRoleInProjectEdit", userRolesInProjectEdit);
            model.addObject("userRoleInProjectErrors", errors);
            return model;
        }
    }

    @RequestMapping(value = "/removeUserRoleInProject/{userRoleInProjectId}/", method = RequestMethod.GET)
    public ModelAndView removeUserRoleInProject(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings,  @PathVariable Integer userRoleInProjectId) {
        if (!checkPermission(user))
            return new ModelAndView("redirect:/");

        Connection dbConnection = DatabaseSettings.getDatabaseConnection();

        userRolesInProjectDao.remove(dbConnection, userRoleInProjectId);

        return new ModelAndView("redirect:/userAdmin");
    }

    private List<String> validate(UserRolesInProject userRolesInProject) {
        List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(userRolesInProject.getRole())) {
            errors.add(ROLE_CANNOT_BE_EMPTY);
        }
        return errors;
    }
}