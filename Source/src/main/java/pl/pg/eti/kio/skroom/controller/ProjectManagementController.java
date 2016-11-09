package pl.pg.eti.kio.skroom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import pl.pg.eti.kio.skroom.model.User;

/**
 * @author Wojciech Stanisławski
 * @since 09.11.16
 */
@Controller
@SessionAttributes({"loggedUser", "selectedProject"})
public class ProjectManagementController {

	@RequestMapping(value = "/addProject")
	public ModelAndView addProject(@ModelAttribute("loggedUser") User user) {
		return new ModelAndView(Views.INDEX_JSP_LOCATION);
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
