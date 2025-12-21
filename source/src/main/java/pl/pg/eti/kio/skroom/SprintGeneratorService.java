package pl.pg.eti.kio.skroom;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.pg.eti.kio.skroom.model.Project;
import pl.pg.eti.kio.skroom.model.Sprint;
import pl.pg.eti.kio.skroom.model.dao.SprintDao;

@Service
public class SprintGeneratorService {
	
	@Autowired private SprintDao sprintDao;
	
	public boolean generateAndUploadMissingSprint(Connection dbConnection, Project project) {
		try{
			Sprint newSprint = generateSprint(dbConnection, project);
			LocalDate endDate = LocalDate.now().plusWeeks(project.getDefaultSprintLength());
			newSprint.setEndDate(Date.valueOf(endDate));
			return sprintDao.createSprint(dbConnection, newSprint);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Sprint generateSprint(Connection dbConnection, Project project) throws Exception {
		if(project == null) {
			throw new Exception();
		}
		
		try {
			Sprint lastSprint = sprintDao.getLastSprint(dbConnection, project);
			Sprint newSprint = new Sprint();

			LocalDate startDate = new java.sql.Date(lastSprint.getEndDate().getTime()).toLocalDate();
			LocalDate endDate = startDate.plusWeeks(project.getDefaultSprintLength());

			newSprint.setName("Sprint " + sprintDao.getSprintCountForProject(dbConnection, project));
			newSprint.setStartDate(Date.valueOf(startDate));
			newSprint.setEndDate(Date.valueOf(endDate));
			newSprint.setProject(project);
			
			return newSprint;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null; 
	}

}
