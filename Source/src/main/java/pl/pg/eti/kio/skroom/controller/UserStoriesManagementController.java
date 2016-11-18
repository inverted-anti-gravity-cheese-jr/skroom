package pl.pg.eti.kio.skroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.UserSettings;
import pl.pg.eti.kio.skroom.model.UserStory;
import pl.pg.eti.kio.skroom.model.UserStoryStatus;
import pl.pg.eti.kio.skroom.model.dao.UserStoryDao;
import pl.pg.eti.kio.skroom.model.dao.UserStoryStatusDao;
import pl.pg.eti.kio.skroom.model.enumeration.StoryPoints;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;
import java.util.ArrayList;

import static pl.pg.eti.kio.skroom.controller.Views.USER_STORY_FORM_JSP_LOCATION;

/**
 *
 *
 * @author Wojciech Stanisławski
 * @since 17.11.16
 */
@Controller
@SessionAttributes({"loggedUser", "userSettings"})
public class UserStoriesManagementController {

	// ERROR MESSAGES
	private static final String NAME_CANNOT_BE_EMPTY_ERROR = "User story name cannot be empty.";
	private static final String PRIORITY_IS_NOT_A_VALID_NUMBER_ERROR = "Priority is not a valid number.";

	@Autowired private DefaultTemplateDataInjector injector;
	@Autowired private UserStoryDao userStoryDao;
	@Autowired private UserStoryStatusDao userStoryStatusDao;
	@Autowired private WebRequest webRequest;

	@ModelAttribute("loggedUser")
	public User defaultNullUser() {
		return new User();
	}

	@ModelAttribute("userSettings")
	public UserSettings defaultNullUserSettings() {
		return new UserSettings();
	}

	@RequestMapping(value = "addUserStory", method = RequestMethod.GET)
	public ModelAndView addUserStory(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings) {
		ModelAndView model = injector.getIndexForSiteName(USER_STORY_FORM_JSP_LOCATION, "Add new user story", userSettings.getRecentProject(), user, webRequest);

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		ArrayList<String> availableStoryPoints = StoryPoints.getAvailableDisplayNames();
		ArrayList<UserStoryStatus> availableStoryStatuses = userStoryStatusDao.listAllStatuses(dbConnection);

		model.addObject("availableStoryPoints", availableStoryPoints);
		model.addObject("availableStoryStatuses", availableStoryStatuses);

		return model;
	}

	@RequestMapping(value = "editUserStory/", method = RequestMethod.GET)
	public ModelAndView editUserStory() {
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "editUserStory/{userStoryId}/", method = RequestMethod.GET)
	public ModelAndView editUserStory(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings, @PathVariable Integer userStoryId) {
		ModelAndView model = injector.getIndexForSiteName(USER_STORY_FORM_JSP_LOCATION, "Add new user story", userSettings.getRecentProject(), user, webRequest);

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		ArrayList<String> availableStoryPoints = StoryPoints.getAvailableDisplayNames();
		ArrayList<UserStoryStatus> availableStoryStatuses = userStoryStatusDao.listAllStatuses(dbConnection);
		UserStory userStory = userStoryDao.fetchUserStoryById(dbConnection, userStoryId.intValue());

		model.addObject("userStory", userStory);
		model.addObject("availableStoryPoints", availableStoryPoints);
		model.addObject("availableStoryStatuses", availableStoryStatuses);

		return model;
	}

	@RequestMapping(value = "addUserStory", method = RequestMethod.POST)
	public ModelAndView addUserStory(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings,
									  @RequestParam String name, @RequestParam("priority") String priorityString,
									  @RequestParam String storyPoints, @RequestParam String status) {
		return addOrEditUserStory(user, userSettings, null, name, priorityString, storyPoints, status);
	}

	@RequestMapping(value = "editUserStory/{userStoryId}/", method = RequestMethod.POST)
	public ModelAndView editUserStory(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings,
									  @PathVariable Integer userStoryId, @RequestParam String name, @RequestParam("priority") String priorityString,
									  @RequestParam String storyPoints, @RequestParam String status) {
		return addOrEditUserStory(user, userSettings, userStoryId, name, priorityString, storyPoints, status);
	}

	private ModelAndView addOrEditUserStory(User user, UserSettings userSettings, Integer userStoryId, String name, String priorityString, String storyPoints, String status) {
		ModelAndView model = injector.getIndexForSiteName(USER_STORY_FORM_JSP_LOCATION, "Add new user story", userSettings.getRecentProject(), user, webRequest);
		if (userSettings.getRecentProject() == null) {
			return new ModelAndView("redirect:/");
		}
		String errorMessage = null;

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		UserStory userStory = new UserStory();
		userStory.setId(userStoryId.intValue());
		userStory.setName(name);
		userStory.setStatus(userStoryStatusDao.getUserStoryStatusForName(dbConnection, status));
		userStory.setStoryPoints(StoryPoints.fromDisplayName(storyPoints));
		userStory.setPriority(1);


		if (name == null || name.isEmpty()) {
			errorMessage = NAME_CANNOT_BE_EMPTY_ERROR;
		}

		if (priorityString == null || priorityString.isEmpty() || !isInteger(priorityString)) {
			if(errorMessage != null) {
				errorMessage += PRIORITY_IS_NOT_A_VALID_NUMBER_ERROR;
			}
			else {
				errorMessage = PRIORITY_IS_NOT_A_VALID_NUMBER_ERROR;
			}
		}
		else {
			userStory.setPriority(Integer.parseInt(priorityString));
		}

		if(errorMessage != null) {
			model.addObject("errorMessage", errorMessage);
		}
		else {
			// TODO: add or update model and view
			return new ModelAndView("redirect:/productbacklog");
		}

		ArrayList<String> availableStoryPoints = StoryPoints.getAvailableDisplayNames();
		ArrayList<UserStoryStatus> availableStoryStatuses = userStoryStatusDao.listAllStatuses(dbConnection);

		model.addObject("userStory", userStory);
		model.addObject("availableStoryPoints", availableStoryPoints);
		model.addObject("availableStoryStatuses", availableStoryStatuses);
		return model;
	}

	private boolean isInteger(String str) {
		return str.matches("^-?\\d+$");
	}

}
