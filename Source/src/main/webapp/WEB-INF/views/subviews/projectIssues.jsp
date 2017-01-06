<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>

    <h1>Issues</h1>

    <div class="management-bar">
        <a href="/skroom/issue/${userSettings.recentProject.id}/" class="btn green" >Add new</a>
    </div>

    <table class="table">
        <thead><tr>
            <td>Name</td>
            <td>Description</td>
            <td>Status</td>
            <td>Assignee</td>
            <td>Priority</td>
            <td>Task</td>
        </tr></thead>
        <tbody>
            <c:forEach items="${projectIssues}" var="issue" >
                <tr>
                    <td><a href="/skroom/issue/${userSettings.recentProject.id}/${issue.id}/">${issue.name}</a></td>
                    <td>${issue.description}</td>
                    <td>${issue.status.name}</td>
                    <td><c:if test="${not empty issue.assignee}">${issue.assignee.name} [${issue.assignee.email}]</c:if></td>
                    <td>${issue.priority}</td>
                    <td>${issue.task.name}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</t:index>