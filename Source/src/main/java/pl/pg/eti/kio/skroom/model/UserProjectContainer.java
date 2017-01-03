package pl.pg.eti.kio.skroom.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marek Czerniawski on 2017-01-03.
 */
public class UserProjectContainer {
    private int projectId;
    private User user;
    private List<UserRolesInProject> roles = new ArrayList<>();

    public UserProjectContainer(int projectId, User user) {
        this.projectId = projectId;
        this.user = user;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int id) {
        this.projectId = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<UserRolesInProject> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRolesInProject> roles) {
        this.roles = roles;
    }
}