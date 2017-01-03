<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>

<form method="post" class="skroom-form">
    <c:choose>
        <c:when test="${ createProject eq 'true' }">
            <h1>Create new project</h1>
        </c:when>
        <c:otherwise>
            <h1>Edit project: ${project.name}</h1>
        </c:otherwise>
    </c:choose>

    <div class="management-bar" style="margin-bottom: 5px;">
        <input type="submit" class="btn green" />   
        <c:if test="${ createProject eq 'false' }">
            <a href="/skroom/removeProject/${project.id}" class="btn red pull-right">Delete project</a>
        </c:if>
    </div>
    
    <input name="projectName" placeholder="Project name" value="${project.name}" class="form-control" />
    <textarea name="projectDescription" placeholder="Project description" class="form-control"><s:html2plain>${project.description}</s:html2plain></textarea>
    <div class="input-group">
        <span class="input-group-addon">Sprint length</span>
        <input name="sprint-length" placeholder="Sprint length" class="form-control" value="${project.defaultSprintLength}" />
        <span class="input-group-addon">[weeks]</span>
    </div>
    <c:if test="${ createProject eq 'true' }">
    <input name="first-sprint-name" placeholder="Name of a first sprint" value="Sprint 1" class="form-control" />
    </c:if>
    
    <h2>Available task statuses</h2>
    <c:if test="${createProject}">
    This statuses are the default statuses for every new project. If you wish to change them go to project settings or kanban board after project creation.
    </c:if>
    <table class="table">
        <thead>
            <tr>
                <th>Name</th>
                <th>Does it stay in sprint?</th>
                <th>Delete</th>
            </tr>
        </thead>
        <tbody id="tsTableBody">
            <c:forEach var="taskStatus" items="${taskStatuses}">
            <tr>
                <td><input type="hidden" name="tsNameKey[]" value="${taskStatus.id}" /><input name="tsName[]" type="text" style="display: none;" value="${taskStatus.name}" /><a <c:if test="${projectIsEditable}">class="tsNameEdit"</c:if> >${taskStatus.name}</a></td>
                <td><input name="tsStaysInSprint[]" value="${taskStatus.id}" type="checkbox" <c:if test="${taskStatus.staysInSprint}">checked</c:if> <c:if test="${createProject}">disabled</c:if> /></td>
                <td><a href="deleteTaskStatus/${taskStatus.id}"><i class="fa fa-trash" aria-hidden="true"></i></a></td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
    <a id="addNewTaskStatus">Add new task status</a>

    <script type="text/javascript">
        function removeNewTaskStatus(obj) {
            $(obj).closest("tr").remove();
        }
        
        $("#addNewTaskStatus").click(function() {
            var body = $("#tsTableBody");
            var tabSize = body.children("tr").length;
            var elem = $("<tr>" +
                         '<td><input type="hidden" name="tsNameKey[]" value="-' + tabSize.toString() + '" /><input name="tsName[]" type="text" value="" /></td>' +
                         '<td><input name="tsStaysInSprint[]" value="-' + tabSize.toString() + '" type="checkbox" /></td>' +
                         '<td><a onclick="removeNewTaskStatus(this);"><i class="fa fa-trash" aria-hidden="true"></i></a></td>' +
                         "</tr>");
            body.append(elem);
        });
        
        $(".tsNameEdit").click(function(e) {
            var textField = $(e.target).siblings("input[type='text']")[0];
            $(e.target).fadeToggle(200, function() { $(textField).fadeToggle(200) });
        });
    </script>
</form>