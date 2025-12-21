package pl.pg.eti.kio.skroom.model;

import java.time.ZoneOffset;
import java.util.Date;
import pl.pg.eti.kio.skroom.model.tables.records.SprintsRecord;

/**
 * Model representation of a sprint.
 *
 * @author Wojciech Stanisławski
 * @since 05.12.16
 */
public class Sprint {

    private int id;
    private String name;
    private Date startDate;
    private Date endDate;
    private Project project;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public static Sprint fromDba(SprintsRecord record, Project project) {
        if (record == null) {
            return null;
        }

        Sprint sprint = new Sprint();

        sprint.setId(record.getId());
        sprint.setName(record.getName());
        sprint.setProject(project);
        sprint.setStartDate(Date.from(record.getStartDay().atStartOfDay(ZoneOffset.systemDefault()).toInstant()));
        sprint.setEndDate(Date.from(record.getEndDay().atStartOfDay(ZoneOffset.systemDefault()).toInstant()));

        return sprint;
    }

    @Override
    public String toString() {
        return (
            "Sprint [id=" +
            id +
            ", name=" +
            name +
            ", startDate=" +
            startDate +
            ", endDate=" +
            endDate +
            ", project=" +
            project +
            "]"
        );
    }
}
