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
            <td>Status</td>
            <td>Assignee</td>
            <td>Priority</td>
        </tr></thead>
        <tbody>
            <c:forEach items="${projectIssues}" var="issue" >
                <tr>
                    <td><a href="/skroom/issue/${userSettings.recentProject.id}/${issue.id}/">${issue.name}</a></td>
                    <td>${issue.status.name}</td>
                    <td>
                        <c:if test="${not empty issue.assignee}"><span class="round-table-label"><s:shorten max="15">${issue.assignee.name}</s:shorten></span></c:if>
                        <c:if test="${empty issue.assignee}"><span class="round-table-label" style="background-color: #ff9d00;">Unassigned</span></c:if></td>
                    <td>P${issue.priority}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</t:index>