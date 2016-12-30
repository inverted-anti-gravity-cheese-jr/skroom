<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>
    <script type="text/javascript">
        
        
        
    </script>
    
    <h1>Tasks</h1>
    <div class="management-bar">
        <span class="bar-text">Choose sprint
            <select id="sprintSelect" onchange="selectSprintInMenu(this);">
            <c:forEach var="sprint" items="${sprintsWithoutLast}">
                <option value="${sprint.id}">${sprint.name}</option>    
            </c:forEach>
                <option value="${lastSprint.id}" selected="selected">${lastSprint.name}</option>
            </select>
            <script type="text/javascript">
                reloadSprintForm("sprintSelect");
            </script>
        </span>
        <c:if test="${not empty showNewButton}">
        <a href="addTask" class="btn green">Add task</a>
        </c:if>
    </div>
    
    <table class="table">
        <thead>
            <tr>
                <th>No</th>
                <th>Task</th>
                <th>Assignee</th>
                <th>Estimated time [h]</th>
                <th>Status</th>
                <th>User story</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="task" items="${tasks}">
                <%@ include file="/WEB-INF/views/model/taskTableView.jsp"%>
            </c:forEach>
        </tbody>
    </table>
    <c:if test="${fn:length(tasks) == 0}">
        <h4>No existing tasks, <a href="addTask">add one!</a></h4>
    </c:if>

</t:index>