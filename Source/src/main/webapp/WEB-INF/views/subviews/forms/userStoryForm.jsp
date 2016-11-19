<%@ page import="pl.pg.eti.kio.skroom.model.UserStory" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script type="text/javascript">
    <% if(request.getAttribute("userStory") != null) { out.print("var userStory_storyPoints = \"" + ((UserStory)request.getAttribute("userStory")).getStoryPoints().getDisplayName() +"\";"); } %>
    <% if(request.getAttribute("userStory") != null) { out.print("var userStory_status = \"" + ((UserStory)request.getAttribute("userStory")).getStatus().getName() +"\";"); } %>
</script>
    
<t:index>
    

    
    <c:choose>
        <c:when test="${ not empty userStory }">
            <h1>${userStory.name}</h1>
        </c:when>
        <c:otherwise>
            <h1>New user story</h1>
        </c:otherwise>
    </c:choose>
        
    <form class="user-story-form" method="post">

        <input name="name" type="text" class="form-control" placeholder="User story name" value="${userStory.name}" />  
        <textarea name="description" class="form-control" placeholder="Description">${userStory.description}</textarea> 
        <div class="input-group">
          <span class="input-group-addon" id="basic-addon1">P</span>
          <input name="priority" type="text" class="form-control" placeholder="Priority" value="${userStory.priority}" />
        </div>
        <select id="story-points-select" name="storyPoints" class="form-control">
            <c:forEach var="sp" items="${availableStoryPoints}">
                <option>${sp}</option>
            </c:forEach>
        </select>
        <select id="user-story-status-select" name="status" class="form-control">
            <c:forEach var="ss" items="${availableStoryStatuses}">
                <option>${ss.name}</option>
            </c:forEach>
        </select>
        
        <c:if test="${ not empty errorMessage }">
            <span style="color: red">${errorMessage}</span>
        </c:if>

        <input type="submit" class="btn" />
    </form>
    <c:if test="${ not empty userStory }">
        <a href="../../removeUserStory/${userStory.id}/"><button class="btn">Delete</button></a>
    </c:if>
    
    
    <script type="text/javascript">
        reloadUserStoryForm(userStory_storyPoints, userStory_status);
    </script>

</t:index>