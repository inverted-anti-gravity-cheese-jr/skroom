package pl.pg.eti.kio.skroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.pg.eti.kio.skroom.model.Project;
import pl.pg.eti.kio.skroom.model.User;
import pl.pg.eti.kio.skroom.model.dao.ProjectDao;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;

/**
 * @author Wojciech Stanisławski
 * @since 09.11.16
 */
@Controller
@SessionAttributes({"loggedUser", "selectedProject"})
public class ProjectManagementController {

	@Autowired
	private ProjectDao projectDao;

	@RequestMapping(value = "/addProject", method = RequestMethod.GET)
	public ModelAndView addProjectForm(@ModelAttribute("loggedUser") User user) {
		ModelAndView modelAndView = new ModelAndView(Views.INDEX_JSP_LOCATION);

		modelAndView.addObject("siteName", "projectForm");

		return modelAndView;
	}

	@RequestMapping(value = "/addProject", method = RequestMethod.POST)
	public ModelAndView addProject(@ModelAttribute("loggedUser") User user, @RequestParam String name, @RequestParam String description) {
		Project project = new Project();

		project.setName(name);
		project.setDescription(description);

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();

		projectDao.addProject(dbConnection, project);

		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/editProject")
	public ModelAndView editProject(@ModelAttribute("loggedUser") User user) {
		return new ModelAndView(Views.INDEX_JSP_LOCATION);
	}

	@RequestMapping(value = "/removeProject")
	public ModelAndView removeProject(@ModelAttribute("loggedUser") User user) {
		return new ModelAndView(Views.INDEX_JSP_LOCATION);
	}
}
