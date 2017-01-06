<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>

    <script type="text/javascript">
        function switchViews(showEdit) {
            if(showEdit) {
                $(".user-view").slideUp(function () {
                    $(".user-edit").slideDown();
                });
            }
            else {
                $(".user-edit").slideUp(function () {
                    $(".user-view").slideDown();
                });
            }
        }
    </script>


    <h1>${editUser.user.name}</h1>

    <div class="user-view">
        <div class="management-bar">
            <button class="btn" type="button" onclick="switchViews(true)">Edit</button>
            <a href="../../admin" class="btn" >Back</a>
        </div>

        <div class="panel panel-info">
            <div class="panel-heading">Details</div>
            <div class="panel-body">
                <b>Name</b> <span>${editUser.user.name}</span> <br />
                <b>Email</b> <span>${editUser.user.email}</span> <br />
                <b>Global privileges</b> <span>${editUser.user.role.displayName}</span> <br />
                <b>Accepted</b> <span>${editUser.accepted ? 'Yes' : 'No'}</span> <br />
            </div>
        </div>
    </div>

    <div class="user-edit">
        <form method="post" class="skroom-form">
            <div class="management-bar">
                <input value="Save" type="submit" class="btn"/>
                <c:if test="${not editUser.accepted}">
                    <input value="Save and accept" name="user-accept-button" type="submit" class="btn"/>
                </c:if>
                <button class="btn" type="button" onclick="switchViews(false)">Cancel</button>
            </div>

            <select id="select-edit-user-role" name="editUserRole" value="${editUser.user.role}" class="form-control" >
                <c:forEach var="role" items="${availableUserRoles}">
                    <option>${role.displayName}</option>
                </c:forEach>
            </select>
        </form>
    </div>


    <script type="text/javascript">
        $(".user-edit").hide();
        var role = "${editUser.user.role.displayName}";
        reloadEditUserForm(role);
    </script>

</t:index>