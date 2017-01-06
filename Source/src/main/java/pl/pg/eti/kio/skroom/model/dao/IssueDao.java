package pl.pg.eti.kio.skroom.model.dao;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import pl.pg.eti.kio.skroom.exception.NoSuchUserRoleException;
import pl.pg.eti.kio.skroom.model.*;
import pl.pg.eti.kio.skroom.model.dba.tables.records.IssuesRecord;
import pl.pg.eti.kio.skroom.model.dba.tables.records.TaskStatusesRecord;
import pl.pg.eti.kio.skroom.settings.DatabaseSettings;

import java.sql.Connection;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static pl.pg.eti.kio.skroom.model.dba.Tables.*;

/**
 * Created by Marek Czerniawski on 2017-01-05.
 */
@Service
public class IssueDao {

    public List<Issue> listAllIssues(Connection connection, Project project) {
        DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

        Map<Integer, Task> taskMap = taskMapForProject(connection, project);

        Result<Record> records = query.select().from(ISSUES)
                .leftJoin(TASK_STATUSES).onKey()
                .leftJoin(USERS).onKey()
                .leftJoin(TASKS).onKey()
                .where(ISSUES.PROJECT_ID.eq(project.getId()))
                .fetch();

        List<Issue> results = new ArrayList<>();
        try {
            for (Record record : records) {
                IssuesRecord issue = record.into(ISSUES);
                TaskStatus status = null;
                if (record.get(ISSUES.STATUS_ID) != null) {
                    status = TaskStatus.fromDba(record.into(TASK_STATUSES), project);
                }
                User assignee = null;
                if (record.get(ISSUES.ASSIGNEE_ID) != null) {
                    assignee = User.fromDba(record.into(USERS));
                }
                Task task = null;
                if (record.get(ISSUES.TASK_ID) != null) {
                    task = taskMap.get(record.into(TASKS).getId());
                }
                results.add(Issue.fromDba(issue, status, assignee, task, project));
            }
        }
        catch (NoSuchUserRoleException e) {
            results.clear();
        }
        return results;
    }

    private Map<Integer, Task> taskMapForProject(Connection connection, Project project) {
        List<UserStory> userStories = new UserStoryDao().fetchUserStoriesForProject(connection, project);
        List<TaskStatus> taskStatuses = new TaskStatusDao().fetchByProject(connection, project);
        List<Sprint> sprints = new SprintDao().fetchAvailableSprintsForProject(connection, project);
        List<Task> taskList = new TaskDao().fetchTasks(connection, project, taskStatuses, userStories, sprints);
        return taskList.stream().collect(Collectors.toMap(Task::getId, Function.identity()));
    }

    public Issue fetchById(Connection connection, int issueId, Project project) {
        //TODO change
        return listAllIssues(connection, project).stream().filter(x -> x.getId() == issueId).findFirst().orElse(null);
    }

    public boolean update(Connection connection, Issue issue) {
        DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

        int updatedRows = query.update(ISSUES)
                .set(ISSUES.NAME, issue.getName())
                .set(ISSUES.DESCRIPTION, issue.getDescription())
                .set(ISSUES.STATUS_ID, issue.getStatus() != null ? issue.getStatus().getId() : null)
                .set(ISSUES.ASSIGNEE_ID, issue.getAssignee() != null ? issue.getAssignee().getId() : null)
                .set(ISSUES.PRIORITY, issue.getPriority())
                .set(ISSUES.TASK_ID, issue.getTask() != null ? issue.getTask().getId() : null)
                .set(ISSUES.PROJECT_ID, issue.getProject() != null ? issue.getProject().getId() : null)
                .where(ISSUES.ID.eq(issue.getId()))
                .execute();

        return updatedRows > 0;
    }

    public boolean add(Connection connection, Issue issue) {
        DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

        int insertedRows = query.insertInto(ISSUES)
                .set(ISSUES.NAME, issue.getName())
                .set(ISSUES.DESCRIPTION, issue.getDescription())
                .set(ISSUES.STATUS_ID, issue.getStatus() != null ? issue.getStatus().getId() : null)
                .set(ISSUES.ASSIGNEE_ID, issue.getAssignee() != null ? issue.getAssignee().getId() : null)
                .set(ISSUES.PRIORITY, issue.getPriority())
                .set(ISSUES.TASK_ID, issue.getTask() != null ? issue.getTask().getId() : null)
                .set(ISSUES.PROJECT_ID, issue.getProject() != null ? issue.getProject().getId() : null)
                .execute();

        return insertedRows > 0;
    }

    public TaskStatus fetchStatusByName(Connection connection, String issueStatusName, Project project) {
        DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

        List<TaskStatusesRecord> records = query.selectFrom(TASK_STATUSES)
                .where(TASK_STATUSES.PROJECT_ID.eq(project.getId()))
                .and(TASK_STATUSES.NAME.eq(issueStatusName))
                .fetch();

        if (records.isEmpty()) {
            return null;
        }
        return TaskStatus.fromDba(records.get(0), project);
    }

    public List<TaskStatus> availableIssueStatuses(Connection connection, Project project) {
        DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

        Result<TaskStatusesRecord> records = query.selectFrom(TASK_STATUSES)
                .where(TASK_STATUSES.PROJECT_ID.eq(project.getId()))
                .fetch();

        return records.stream().map(x -> TaskStatus.fromDba(x, project)).collect(Collectors.toList());
    }

    public List<User> availableIssueUsers(Connection connection, Project project) {
        DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

        Result<Record> records = query.select()
                .from(USERS_PROJECTS)
                .join(USERS).onKey()
                .where(USERS_PROJECTS.PROJECT_ID.eq(project.getId()))
                .fetch();

        List<User> results = new ArrayList<>();
        try {
            for (Record record : records) {
                results.add(User.fromDba(record.into(USERS)));
            }
        }
        catch (NoSuchUserRoleException e) {
            results.clear();
        }
        return results;
    }

    public User fetchUserNyName(Connection connection, String userName, Project project) {
        return availableIssueUsers(connection, project).stream()
                .filter(x -> Objects.toString(userName, "").equals(x.getName()))
                .findFirst().orElse(null);
    }

    public List<Task> availableIssueTasks(Connection connection, Project project) {
        return new ArrayList<>(taskMapForProject(connection, project).values());
    }

    public Task fetchTaskByName(Connection connection, String taskName, Project project) {
        return availableIssueTasks(connection, project).stream()
                .filter(x -> Objects.toString(taskName, "").equals(x.getName()))
                .findFirst().orElse(null);
    }

    public boolean remove(Connection connection, int issueId, int projectId) {
        DSLContext query = DSL.using(connection, DatabaseSettings.getCurrentSqlDialect());

        int deletedRows = query.delete(ISSUES)
                .where(ISSUES.ID.eq(issueId))
                .and(ISSUES.PROJECT_ID.eq(projectId))
                .execute();

        return deletedRows > 0;
    }
}