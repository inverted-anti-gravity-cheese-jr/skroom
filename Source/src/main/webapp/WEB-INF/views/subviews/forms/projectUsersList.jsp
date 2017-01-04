<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<h1>Users</h1>

<div class="management-bar">
    <a href="/skroom/settings/${userSettings.recentProject.id}/" class="btn green">Add User</a>
</div>

<table class="table">
    <thead><tr><td>Name</td><td>Email</td><td>Roles</td></tr></thead>
    <tbody>
        <c:forEach items="${projectUsers}" var="user">
            <tr>
                <td><a href="/skroom/settings/${user.projectId}/${user.user.id}/">${user.user.name}</a></td>
                <td>${user.user.email}</td>
                <td>
                    <c:forEach items="${user.roles}" var="role">
                        <div style="color: ${role.color};">${role.role}</div>
                    </c:forEach>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>