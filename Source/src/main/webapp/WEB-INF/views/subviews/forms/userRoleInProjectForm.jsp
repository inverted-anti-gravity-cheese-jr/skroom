<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>

    <spring:url value="/resources/css/app/adminPanel.css" var="adminCss" />
    <link href="${adminCss}" rel="stylesheet" />

    <script type="text/javascript">
        function switchViews(showEdit) {
            if(showEdit) {
                $(".user-role-in-project-view").slideUp(function () {
                    $(".user-role-in-project-edit").slideDown();
                });
            }
            else {
                $(".user-role-in-project-edit").slideUp(function () {
                    $(".user-role-in-project-view").slideDown();
                });
            }
        }
    </script>

    <c:choose>
       <c:when test="${not empty userRoleInProject.id}">
           <h1>${userRoleInProject.role}</h1>
       </c:when>
       <c:otherwise>
           <h1>New user role in project</h1>
       </c:otherwise>
   </c:choose>

    <div class="user-role-in-project-view">
        <div class="management-bar">
            <button class="btn" type="button" onclick="switchViews(true)">Edit</button>
            <a href="../../userAdmin" class="btn" >Back</a>
            <c:if test="${not empty userRoleInProject.id}">
                <a href="../../removeUserRoleInProject/${userRoleInProject.id}/" class="btn red pull-right" >Delete</a>
            </c:if>
        </div>

        <div class="panel panel-info">
            <div class="panel-heading">Details</div>
            <div class="panel-body">
                <b>Role</b> <span>${userRoleInProject.role}</span> <br />
                <b>Color</b> <span style="color: ${userRoleInProject.color};">${userRoleInProject.color}</span> <br />
                <b>Privileges</b> <span>${userRoleInProject.privileges}</span> <br />
            </div>
        </div>
    </div>

    <form class="skroom-form user-role-in-project-edit" method="post">
        <div>
            <div class="management-bar">
                <input type="submit" class="btn" value="Save" />
                <button class="btn" type="button" onclick="switchViews(false)">Cancel</button>
            </div>
            <input name="projectRole" type="text" class="form-control" placeholder="Role" value="${userRoleInProjectEdit.role}" />
            <input name="color" type="text" class="form-control" placeholder="Color" value="${userRoleInProjectEdit.color}" />
            <input name="privileges" type="text" class="form-control" placeholder="Privileges" value="${userRoleInProjectEdit.privileges}" />
        </div>

        <c:if test="${not empty userRoleInProjectErrors}">
            <c:forEach items="${userRoleInProjectErrors}" var="error">
                <div style="color: red">${error}</div>
            </c:forEach>
        </c:if>
    </form>

    <script type="text/javascript">
        var errors = "${userRoleInProjectErrors}";
        var adding = "${empty userRoleInProject.id}";
        if (adding == "true" || errors != "")
        {
            $(".user-role-in-project-view").hide();
            $(".user-role-in-project-edit").show();
        }
    </script>

</t:index>