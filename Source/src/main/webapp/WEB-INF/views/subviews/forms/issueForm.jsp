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
                $(".issue-view").slideUp(function () {
                    $(".issue-edit").slideDown();
                });
            }
            else {
                $(".issue-edit").slideUp(function () {
                    $(".issue-view").slideDown();
                });
            }
        }
    </script>

    <c:choose>
       <c:when test="${not empty projectIssue.id}">
           <h1>${projectIssue.name}</h1>
       </c:when>
       <c:otherwise>
           <h1>New issue</h1>
       </c:otherwise>
   </c:choose>

    <div class="issue-view">
        <div class="management-bar">
            <button class="btn" type="button" onclick="switchViews(true)">Edit</button>
            <a href="../../../issues" class="btn" >Back</a>
            <c:if test="${not empty projectIssue.id}">
                <a href="../../../removeIssue/${userSettings.recentProject.id}/${projectIssue.id}/" class="btn red pull-right" >Delete</a>
            </c:if>
        </div>

        <div class="panel panel-info">
            <div class="panel-heading">Details</div>
            <div class="panel-body">
                <b>Name</b> <span>${projectIssue.name}</span> <br />
                <b>Description</b> <span>${projectIssue.description}</span> <br />
                <b>Status</b> <span>${projectIssue.status.name}</span> <br />
                <b>Assignee</b> <c:if test="${not empty projectIssue.assignee}"> <span>${projectIssue.assignee.name} [${projectIssue.assignee.email}]</span> </c:if> <br />
                <b>Priority</b> <span>${projectIssue.priority}</span> <br />
                <b>Task</b> <span>${projectIssue.task.name}</span> <br />
            </div>
        </div>
    </div>

    <form class="skroom-form issue-edit" method="post">
        <div>
            <div class="management-bar">
                <input type="submit" class="btn" value="Save" />
                <c:choose>
                   <c:when test="${not empty projectIssue.id}">
                       <button class="btn" type="button" onclick="switchViews(false)">Cancel</button>
                   </c:when>
                   <c:otherwise>
                       <a href="../../issues" class="btn" >Back</a>
                   </c:otherwise>
               </c:choose>

            </div>
            <input name="issueName" type="text" class="form-control" placeholder="Name" value="${projectIssueEdit.name}" />
            <input name="issueDescription" type="text" class="form-control" placeholder="Description" value="${projectIssueEdit.description}" />
            <input name="issueStatus" type="text" class="form-control" placeholder="Status" value="${projectIssueEdit.status.name}" list="statuses" />
            <datalist id="statuses">
                <c:forEach items="${availableIssueStatuses}" var="status">
                    <option>${status.name}</option>
                </c:forEach>
            </datalist>
            <input name="issueAssignee" type="text" class="form-control" placeholder="Assignee" value="${projectIssueEdit.assignee.name}" list="project-users" />
            <datalist id="project-users">
                <c:forEach items="${availableIssueUsers}" var="user">
                    <option>${user.name}</option>
                </c:forEach>
            </datalist>
            <input name="issuePriority" type="text" class="form-control" placeholder="Priority" value="${projectIssueEdit.priority}" />
            <input name="issueTask" type="text" class="form-control" placeholder="Task" value="${projectIssueEdit.task.name}" list="project-tasks" />
            <datalist id="project-tasks">
                <c:forEach items="${availableIssueTasks}" var="task">
                    <option>${task.name}</option>
                </c:forEach>
            </datalist>
        </div>

        <c:if test="${not empty issueErrors}">
            <c:forEach items="${issueErrors}" var="error">
                <div style="color: red">${error}</div>
            </c:forEach>
        </c:if>
    </form>

    <script type="text/javascript">
        <c:choose>
            <c:when test="${empty projectIssue or not empty issueErrors}">
                $(".issue-view").hide();
            </c:when>
            <c:otherwise>
                $(".issue-edit").hide();
            </c:otherwise>
        </c:choose>
    </script>

</t:index>