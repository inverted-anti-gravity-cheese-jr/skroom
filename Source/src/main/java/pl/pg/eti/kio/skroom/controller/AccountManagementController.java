package pl.pg.eti.kio.skroom.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.pg.eti.kio.skroom.exception.signup.UserAccountCreationErrorException;
import pl.pg.eti.kio.skroom.exception.signup.UserAlreadyExistsException;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.UserSecurity;
import pl.pg.eti.kio.skroom.model.dao.UserDao;
import pl.pg.eti.kio.skroom.model.enumeration.UserRole;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;

import static pl.pg.eti.kio.skroom.controller.Views.LOGIN_JSP_LOCATION;
import static pl.pg.eti.kio.skroom.controller.Views.SIGNUP_JSP_LOCATION;

/**
 * @author Wojciech Stanisławski
 * @since 22.08.16
 */
@Controller
@SessionAttributes({"loggedUser"})
public class AccountManagementController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountManagementController.class);

	// Messages
	private static final String WRONG_USERNAME_OR_PASSWORD_MESSAGE = "Wrong username or password";

	// Redirects
	private static final String REDIRECT_AFTER_SUCCESSFULL_LOGIN = "redirect:/dashboard";

	@Autowired private UserDao userDao;

	@ModelAttribute("loggedUser")
	public User defaultNullUser() {
		return new User();
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView tryLoggingIn(@ModelAttribute("loggedUser") User user, @RequestParam String name, @RequestParam String password) {
		if (!isUserNull(user)) {
			return new ModelAndView(REDIRECT_AFTER_SUCCESSFULL_LOGIN);
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		user = userDao.fetchByName(dbConnection, name);
		if(user == null) {
			return getLoginModel(WRONG_USERNAME_OR_PASSWORD_MESSAGE);
		}
		UserSecurity userSecurity = userDao.fetchUserSecurityData(dbConnection, user);
		if(userSecurity.getPassword().equals(password)) {
			ModelAndView model = new ModelAndView(REDIRECT_AFTER_SUCCESSFULL_LOGIN);
			model.addObject("loggedUser", user);
			return model;
		}
		else {
			return getLoginModel(WRONG_USERNAME_OR_PASSWORD_MESSAGE);
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout() {
		ModelAndView model = getLoginModel();
		model.addObject("loggedUser", new User());
		return model;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView loginScreen(@ModelAttribute("loggedUser") User user) {
		if (!isUserNull(user)) {
			return new ModelAndView(REDIRECT_AFTER_SUCCESSFULL_LOGIN);
		}
		return getLoginModel();
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public ModelAndView signUpScreen() {
		return new ModelAndView(SIGNUP_JSP_LOCATION);
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView createAccount(@RequestParam("name") String name, @RequestParam("password") String password,
									  @RequestParam("confirm_password") String confirmPassword, @RequestParam("email") String email,
									  @RequestParam("question") String question, @RequestParam("answer") String answer) {
		if(!confirmPassword.equals(password)) {
			LOGGER.debug("Passwords not match for user " + name + ".");
			return getSignupModelWithErrorMessage("Passwords not match");
		}

		if(!email.matches("(.*@.*[.].*)")) {
			LOGGER.debug("Wrong email format " + email + ".");
			return getSignupModelWithErrorMessage("Wrong email format.");
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		User user = new User();

		user.setName(name);
		user.setEmail(email);
		user.setRole(UserRole.PEASANT);

		UserSecurity userSecurity = new UserSecurity();

		userSecurity.setPassword(password);
		userSecurity.setSalt("SALT");
		userSecurity.setSecureQuestion(question);
		userSecurity.setSecureAnswer(answer);


		try {
			userDao.registerUser(dbConnection, user, userSecurity);
		} catch (UserAlreadyExistsException e) {
			return getSignupModelWithErrorMessage(e.getMessage());
		} catch (UserAccountCreationErrorException e) {
			return getSignupModelWithErrorMessage(e.getMessage());
		}

		return getLoginModel("Thank you, your account has been created successfully.");
	}

	private ModelAndView getSignupModelWithErrorMessage(String message) {
		ModelAndView defaultModel = new ModelAndView(SIGNUP_JSP_LOCATION);
		defaultModel.addObject("signupError", message);
		return defaultModel;
	}

	private ModelAndView getLoginModel() {
		return getLoginModel(null);
	}

	private ModelAndView getLoginModel(String error) {
		ModelAndView model = new ModelAndView(LOGIN_JSP_LOCATION);
		model.addObject("loginError", error);
		return model;
	}

	private boolean isUserNull(User user) {
		return user.getId() < 0;
	}
}
