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
        <input type="submit" value="Save" class="btn" />   
        <c:if test="${ createProject eq 'false' }">
            <a href="/skroom/removeProject/${project.id}" class="btn red pull-right">Delete project</a>
        </c:if>
    </div>
    
    <label>Project name</label>
    <input name="projectName" placeholder="Project name" value="${project.name}" class="form-control" />
    <label>Project description</label>
    <textarea name="projectDescription" placeholder="Project description" class="form-control"><s:html2plain>${project.description}</s:html2plain></textarea>
    <label>Sprint length in weeks</label>
    <div class="input-group">
        <input name="sprint-length" placeholder="Sprint length" class="form-control" value="${project.defaultSprintLength}" />
        <span class="input-group-addon">[weeks]</span>
    </div>
    <c:if test="${ createProject eq 'true' }">
    <label>Name of a first sprint</label>
    <input name="first-sprint-name" placeholder="Name of a first sprint" value="Sprint 1" class="form-control" />
    </c:if>
</form>