package pl.pg.eti.kio.skroom.model;

import pl.pg.eti.kio.skroom.model.dba.tables.records.IssuesRecord;

/**
 * Created by Marek Czerniawski on 2017-01-05.
 */
public class Issue {
    private int id;
    private String name;
    private String description;
    private TaskStatus status;
    private User assignee;
    private int priority;
    private Task task;
    private Project project;

    public static Issue fromDba(IssuesRecord record, TaskStatus status, User assignee, Task task, Project project) {
        Issue issue = new Issue();
        issue.setId(record.getId());
        issue.setName(record.getName());
        issue.setDescription(record.getDescription());
        issue.setStatus(status);
        issue.setAssignee(assignee);
        issue.setPriority(record.getPriority());
        issue.setTask(task);
        issue.setProject(project);
        return issue;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}