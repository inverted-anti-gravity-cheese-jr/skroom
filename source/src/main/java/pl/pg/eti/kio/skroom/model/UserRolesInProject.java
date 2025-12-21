package pl.pg.eti.kio.skroom.model;

import pl.pg.eti.kio.skroom.model.dba.tables.records.UserRolesInProjectRecord;

/**
 * Created by Marek Czerniawski on 2016-11-21.
 */
public class UserRolesInProject {
    private int id;
    private String role;
    private String color;
    private boolean privileges;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isPrivileges() {
        return privileges;
    }

    public void setPrivileges(boolean privileges) {
        this.privileges = privileges;
    }

    @Override
    public String toString() {
        return "UserRolesInProject{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", color='" + color + '\'' +
                ", privileges=" + privileges +
                '}';
    }

    /** Method for converting database records into model classes.
     *
     * @param record Database record fetched by jOOQ
     * @return Converted userRoleInProject
     */
    public static UserRolesInProject fromDba(UserRolesInProjectRecord record) {
        UserRolesInProject result = new UserRolesInProject();
        result.setId(record.getId());
        result.setRole(record.getRole());
        result.setColor(record.getColor());
        result.setPrivileges(record.getPriviliges() == 1);
        return result;
    }
}
