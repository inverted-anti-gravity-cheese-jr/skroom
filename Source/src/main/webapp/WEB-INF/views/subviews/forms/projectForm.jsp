<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>
<form method="post" class="skroom-form">
    <c:choose>
        <c:when test="${ displayDeleteButton eq 'true' }">
            <h1>Edit project ${project.name}</h1>
        </c:when>
        <c:otherwise>
            <h1>Create new project</h1>
        </c:otherwise>
    </c:choose>

    <div class="management-bar" style="margin-bottom: 5px;">
        <input type="submit" class="btn green" />   
        <c:if test="${ displayDeleteButton eq 'true' }">
            <a href="/skroom/removeProject/${projectId}" class="btn red pull-right">Delete project</a>
        </c:if>
    </div>
    
    <input name="projectName" placeholder="Project name" value="${project.name}" class="form-control" />
    <textarea name="projectDescription" placeholder="Project description" class="form-control">${project.description}</textarea>
    <div class="input-group">
        <span class="input-group-addon">Sprint length</span>
        <input name="sprint-length" placeholder="Sprint length" class="form-control" value="${project.defaultSprintLength}" />
        <span class="input-group-addon">[weeks]</span>
    </div>
    <c:if test="${ not displayDeleteButton eq 'true' }">
    <input name="first-sprint-name" placeholder="Name of a first sprint" value="Sprint 1" class="form-control" />
    </c:if>

</form>

</t:index>