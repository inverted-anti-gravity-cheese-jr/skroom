<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>
    <script type="text/javascript">
        function chooseSprint(select) {
            if(document.URL.search("spr=") < 0) {
                window.location.href = document.URL + "?spr=" + select.options[select.selectedIndex].value;
            }
            else {
                window.location.href = document.URL.replace(/spr=\d/, "spr=" + select.options[select.selectedIndex].value);
            }
        }
        
        function getSprint() {
            var loc = document.URL.search(/spr=\d/);
            if (loc > 0) {
                var shortUrl = document.URL.substr(loc);
                var endLoc = shortUrl.indexOf("&");
                if(endLoc < 0) {
                    return parseInt(shortUrl.substr(shortUrl.indexOf("=") + 1));
                }
                else {
                    return parseInt(shortUrl.substr(shortUrl.indexOf("=") + 1, shortUrl.indexOf("&") - shortUrl.indexOf("=")));
                }
            }
            else {
                return -1;
            }
        }
    </script>
    
    <h1>Tasks</h1>
    <div class="management-bar">
        <span class="bar-text">Choose sprint
            <select id="sprintSelect" onchange="chooseSprint(this);">
            <c:forEach var="sprint" items="${sprintsWithoutLast}">
                <option value="${sprint.id}">${sprint.name}</option>    
            </c:forEach>
                <option value="${lastSprint.id}" selected="selected">${lastSprint.name}</option>
            </select>
            <script type="text/javascript">
                function reloadSprintForm() {
                    if(getSprint() >= 0) {
                        searchForOptionInSelectAndCheckIfEquals(document.getElementById("sprintSelect").children, getSprint());
                    }
                }
                reloadSprintForm();
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