package pl.pg.eti.kio.skroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

import static pl.pg.eti.kio.skroom.PlainTextUtil.*;
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
		ModelAndView model = injector.getIndexForSiteName(USER_STORY_FORM_JSP_LOCATION, "Add new user story - Skroom", userSettings.getRecentProject(), user, webRequest);

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		ArrayList<String> availableStoryPoints = StoryPoints.getAvailableDisplayNames();
		ArrayList<UserStoryStatus> availableStoryStatuses = userStoryStatusDao.listAllStatuses(dbConnection);

		model.addObject("availableStoryPoints", availableStoryPoints);
		model.addObject("availableStoryStatuses", availableStoryStatuses);

		return model;
	}

	@RequestMapping(value = "viewUserStory/", method = RequestMethod.GET)
	public ModelAndView editUserStory() {
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "viewUserStory/{userStoryId}", method = RequestMethod.GET)
	public ModelAndView editUserStory(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings, @PathVariable Integer userStoryId) {
		ModelAndView model = injector.getIndexForSiteName(USER_STORY_FORM_JSP_LOCATION, "User story details - Skroom", userSettings.getRecentProject(), user, webRequest);

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
									  @RequestParam("userStoryName") String name, @RequestParam("userStoryDescription") String description, @RequestParam("priority") String priorityString,
									  @RequestParam("userStoryPoints") String storyPoints, @RequestParam("userStoryStatus") String status,
									 @RequestParam("as-a-story") String asA, @RequestParam("i-want-to-story") String iWantTo, @RequestParam("so-that-story") String soThat) {
		return addOrEditUserStory(user, userSettings, null, name, description, priorityString, storyPoints, status, asA, iWantTo, soThat);
	}

	@RequestMapping(value = "editUserStory/{userStoryId}", method = RequestMethod.POST)
	public ModelAndView editUserStory(@ModelAttribute("loggedUser") User user, @ModelAttribute("userSettings") UserSettings userSettings,
									  @PathVariable Integer userStoryId, @RequestParam("userStoryName") String name, @RequestParam("userStoryDescription") String description,
									  @RequestParam("priority") String priorityString, @RequestParam("userStoryPoints") String storyPoints, @RequestParam("userStoryStatus") String status,
									  @RequestParam("as-a-story") String asA, @RequestParam("i-want-to-story") String iWantTo, @RequestParam("so-that-story") String soThat) {
		return addOrEditUserStory(user, userSettings, userStoryId, name, description, priorityString, storyPoints, status, asA, iWantTo, soThat);
	}

	@RequestMapping(value = "removeUserStory/{userStoryId}", method = RequestMethod.GET)
	public ModelAndView removeUserStory(@ModelAttribute("loggedUser") User user, @PathVariable Integer userStoryId) {
		if(user == null || user.getId() < 0)
			return new ModelAndView("redirect:/");
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		userStoryDao.removeUserStoryWithId(dbConnection, userStoryId.intValue());
		return new ModelAndView("redirect:/productbacklog");
	}

	private ModelAndView addOrEditUserStory(User user, UserSettings userSettings, Integer userStoryId, String name, String description, String priorityString, String storyPoints, String status, String asA, String iWantTo, String soThat) {
		ModelAndView model = injector.getIndexForSiteName(USER_STORY_FORM_JSP_LOCATION, "Add new user story", userSettings.getRecentProject(), user, webRequest);
		if (userSettings.getRecentProject() == null) {
			return new ModelAndView("redirect:/");
		}
		String errorMessage = null;
		description = description.replace(WINDOWS_ENDLINE_STRING, UNIX_ENDLINE_STRING).replace(PLAIN_TEXT_ENDLINE_STRING, HTML_ENDLINE_STRING);

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		UserStory userStory = new UserStory();
		if(userStoryId != null) {
			userStory.setId(userStoryId.intValue());
		}
		userStory.setName(name);
		userStory.setAsA(asA);
		userStory.setiWantTo(iWantTo);
		userStory.setSoThat(soThat);
		userStory.setDescription(description);
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
			if(userStoryId == null) {
				userStoryDao.insertNewUserStory(dbConnection, userStory, userSettings.getRecentProject());
				return new ModelAndView("redirect:/productbacklog");
			}
			else {
				userStoryDao.updateUserStory(dbConnection, userStory);
				return new ModelAndView("redirect:/viewUserStory/" + userStoryId);
			}
			
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
