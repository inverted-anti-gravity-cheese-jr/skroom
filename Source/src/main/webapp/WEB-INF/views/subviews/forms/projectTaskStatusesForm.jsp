<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>

<h2>Available task statuses</h2>
<c:if test="${createProject}">
This statuses are the default statuses for every new project. If you wish to change them go to project settings or kanban board after project creation.
</c:if>
<c:if test="${projectIsEditable}">
    <div class="management-bar">
        <a id="addNewTaskStatus" class="btn green">New task status</a>
    </div>
</c:if>
<table class="table">
    <thead>
        <tr>
            <th>Name</th>
            <th>Does it stay in sprint?</th>
            <c:if test="${projectIsEditable}">
            <th>Delete</th>
            </c:if>
        </tr>
    </thead>
    <tbody id="tsTableBody">
        <c:forEach var="taskStatus" items="${taskStatuses}">
        <tr>
            <td><input type="hidden" name="tsNameKey[]" value="${taskStatus.id}" /><input name="tsName[]" type="text" style="display: none;" value="${taskStatus.name}" />
            <c:if test="${projectIsEditable}"><a class="link-notarget tsNameEdit" >${taskStatus.name}</a></c:if>
            <c:if test="${createProject}">${taskStatus.name}</c:if></td>
            <td><input name="tsStaysInSprint[]" value="${taskStatus.id}" type="checkbox" <c:if test="${taskStatus.staysInSprint}">checked</c:if> <c:if test="${createProject}">disabled</c:if> /></td>
            <c:if test="${projectIsEditable}">
            <td><a onclick="removeTaskStatus(${taskStatus.id}, this);" class="link-notarget"><i class="fa fa-trash" aria-hidden="true"></i></a></td>
            </c:if>
        </tr>
        </c:forEach>
    </tbody>
</table>
<div id="projectFormTaskStatusAlert" class="alert alert-danger" style="margin-top: 5px; display: none;"></div>

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