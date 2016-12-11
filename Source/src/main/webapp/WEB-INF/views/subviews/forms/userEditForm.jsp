<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>
<form method="post" class="skroom-form">
    <h1>${editUser.name}</h1>

    <div class="management-bar">
        <input value="Save" type="submit" class="btn"/>
    </div>

    <select id="select-edit-user-role" name="editUserRole" value="${editUser.role}" class="form-control" >
        <c:forEach var="role" items="${availableUserRoles}">
            <option>${role.displayName}</option>
        </c:forEach>
    </select>

</form>

    <script type="text/javascript">
        var role = "${editUser.role.displayName}";
        reloadEditUserForm(role);
    </script>

</t:index>