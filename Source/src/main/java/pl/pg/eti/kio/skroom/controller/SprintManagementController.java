package pl.pg.eti.kio.skroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.*;

import pl.pg.eti.kio.skroom.SprintGeneratorService;
import pl.pg.eti.kio.skroom.model.Sprint;
import pl.pg.eti.kio.skroom.model.UserSettings;
import pl.pg.eti.kio.skroom.model.dao.SprintDao;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

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

	private static final String NO_PROJECTS_STRING = "0";

	@Autowired private SprintDao sprintDao;
	@Autowired private SprintGeneratorService generatorService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public void createSprint(@ModelAttribute("userSettings") UserSettings userSettings, @RequestParam String name) {
		if(userSettings.getRecentProject() == null) {
			return;
		}

		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		try{
			Sprint newSprint = generatorService.generateSprint(dbConnection, userSettings.getRecentProject());
			newSprint.setName(name);
			sprintDao.createSprint(dbConnection, newSprint);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/close", method = RequestMethod.POST)
	public void closeSprint(@ModelAttribute("userSettings") UserSettings userSettings) {
		if(userSettings.getRecentProject() == null) {
			return;
		}
		Connection dbConnection = DatabaseSettings.getDatabaseConnection();
		try {
			Sprint currentSprint = sprintDao.findCurrentSprint(dbConnection, userSettings.getRecentProject());
			Sprint nextSprint = sprintDao.getNextSprint(dbConnection, currentSprint);
			LocalDate localDate = LocalDate.now().plusDays(1);
			currentSprint.setEndDate(Date.valueOf(localDate));
			sprintDao.updateSprint(dbConnection, currentSprint);
			if(nextSprint != null) {
				nextSprint.setStartDate(Date.valueOf(localDate));
				sprintDao.updateSprint(dbConnection, nextSprint);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
