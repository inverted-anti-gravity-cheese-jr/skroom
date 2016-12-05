package pl.pg.eti.kio.skroom.controller;

import org.springframework.web.bind.annotation.*;
import pl.pg.eti.kio.skroom.model.UserSettings;

/**
 * Class for managing sprints in selected project.
 *
 * @author Wojciech Stanisławski
 * @since 05.12.16
 */

@RestController
@RequestMapping("/rest/sprints")
@SessionAttributes({"userSettings"})
public class SprintManagementController {

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public void createSprint(@ModelAttribute("userSettings") UserSettings userSettings) {

	}

}
