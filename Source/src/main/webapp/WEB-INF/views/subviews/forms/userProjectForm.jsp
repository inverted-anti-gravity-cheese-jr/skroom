<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<t:index>

    <script type="text/javascript">
        function switchAddRole(showEdit) {
            if(showEdit) {
                $(".user-project-view").slideUp(function () {
                    $(".user-project-edit").slideDown();
                });
            }
            else {
                $(".user-project-edit").slideUp(function () {
                    $(".user-project-view").slideDown();
                });
            }
        }
    </script>

    <c:choose>
       <c:when test="${not empty ProjectUser}">
           <h1>${ProjectUser.user.name}</h1>
       </c:when>
       <c:otherwise>
           <h1>New user</h1>
       </c:otherwise>
    </c:choose>

    <div class="user-project-view">
        <div class="management-bar">
            <c:if test="${not userHasAllRoles}">
                <button class="btn" type="button" onclick="switchAddRole(true)">Add role</button>
            </c:if>
            <a href="../../" class="btn" >Back</a>
        </div>

        <div class="panel panel-info">
            <div class="panel-heading">Details</div>
            <div class="panel-body">
                <b>Name</b> <span>${ProjectUser.user.name}</span> <br />
                <b>Email</b> <span>${ProjectUser.user.email}</span> <br />
            </div>
        </div>

        <div class="panel panel-info">
            <div class="panel-heading">Roles</div>
            <div class="panel-body">
                <c:forEach items="${ProjectUser.roles}" var="role">
                    <span style="color: ${role.color};">${role.role}</span> <br />
                </c:forEach>
            </div>
        </div>

    </div>

    <form method="post" class="skroom-form user-project-edit">
        <div class="management-bar">
            <c:choose>
               <c:when test="${not empty ProjectUser}">
                   <input type="submit" class="btn" value="Add" />
                   <button class="btn" type="button" onclick="switchAddRole(false)">Cancel</button>
               </c:when>
               <c:otherwise>
                   <input type="submit" class="btn" value="Save" />
                   <a href="../" class="btn" >Back</a>
               </c:otherwise>
           </c:choose>
        </div>

        <div class="user-project-user-edit">
            <input name="projectUserName" type="text" class="form-control" placeholder="Username" list="allUsers" value="${lastUserName}"/>
            <datalist id="allUsers">
                <c:forEach items="${availableProjectUsers}" var="user">
                    <option>${user.name}</option>
                </c:forEach>
            </datalist>
        </div>

        <div class="user-project-roles-edit">
            <select id="select-user-project-role" name="userRoleInProject" class="form-control" >
                <c:forEach var="role" items="${availableUserProjectRoles}">
                    <option>${role.role}</option>
                </c:forEach>
            </select>
        </div>

        <c:if test="${not empty UserProjectErrors}">
            <c:forEach items="${UserProjectErrors}" var="error">
                <div style="color: red">${error}</div>
            </c:forEach>
        </c:if>
    </form>

    <script type="text/javascript">
        <c:choose>
           <c:when test="${not empty ProjectUser}">
               $(".user-project-user-edit").hide();
               $(".user-project-edit").hide();
           </c:when>
           <c:otherwise>
               $(".user-project-view").hide();
           </c:otherwise>
       </c:choose>
    </script>

</t:index>