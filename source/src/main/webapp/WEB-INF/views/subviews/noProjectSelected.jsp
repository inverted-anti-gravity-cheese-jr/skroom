<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<c:set var="newLine" value="\n" />
<t:index>

<div class="dashboard">
	<h2>Hello ${ loggedUser.name }.</h2>
	<h1>Please select project to continue.</h1>
    <c:if test="${canCreateProjects}">Create a new project <a href="addProject">here</a><br/></c:if>
    Available projects:
    <c:if test="${menuAvailableProjectsSize < 1 and not canCreateProject}">You have no access to any project, please contact your administrator.</c:if>
    <ul>
    <c:forEach var="project" items="${menuAvailableProjects}">
        <li>
            <a href="javascript:selectProject(${project.id})">${project.name}</a>
        </li>
    </c:forEach>
    </ul>
</div>

</t:index>