<%@ page import="pl.pg.eti.kio.skroom.model.UserStory" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script type="text/javascript">
    function switchViews(showEdit) {
        if(showEdit) {
            $(".user-story-body").hide();
            $(".user-story-form").show();
        }
        else {
            $(".user-story-form").hide();
            $(".user-story-body").show();
        }
    }

    <% if(request.getAttribute("userStory") != null) { out.print("var userStory_storyPoints = \"" + ((UserStory)request.getAttribute("userStory")).getStoryPoints().getDisplayName() +"\";"); } %>
    <% if(request.getAttribute("userStory") != null) { out.print("var userStory_status = \"" + ((UserStory)request.getAttribute("userStory")).getStatus().getName() +"\";"); } %>
</script>
    
<t:index>
    <c:choose>
        <c:when test="${ not empty userStory }">
    <h1>${userStory.name}</h1>

    <div class="user-story-body">
        <div class="management-bar">
            <button type="button" class="btn" onclick="switchViews(true)">Edit</button>
        </div>

        <div class="panel panel-info">
            <div class="panel-heading">Details</div>
            <div class="panel-body">
                <b>Priority</b> P${userStory.priority} <br />
                <b>Status</b> <span style="color: ${userStory.status.color};">${userStory.status.name}</span> <br />
                <b>Story points</b><span class="user-story-round-text" style="margin-left: 5px; background-color: ${userStory.storyPoints.color}; color: ${userStory.storyPoints.textColor};">${userStory.storyPoints.displayName}</span> <br />
            </div>
        </div>
        <div class="panel panel-info">
            <div class="panel-heading">Description</div>
            <div class="panel-body">
                <span class="big-text-no-margin">As a ${userStory.asA} I want to ${userStory.iWantTo} so that ${userStory.soThat}.</span><br />
                ${userStory.description}
            </div>
        </div>
        
    </div>
    
    <form class="user-story-form" method="post">
        <div class="management-bar">
            <input type="submit" class="btn" value="Save changes" />
            <button type="button" class="btn" onclick="switchViews(false)">Cancel</button>
            <a href="../../removeUserStory/${userStory.id}/" class="btn red pull-right">Delete</a>
        </div>

        </c:when>
        <c:otherwise>
    <h1>New user story</h1>
    <form class="user-story-form" method="post" style="display: initial;">
        <div class="management-bar">
            <input type="submit" class="btn" value="Save" />
        </div>
        </c:otherwise>
    </c:choose>

        <input name="name" type="text" class="form-control" placeholder="User story name" value="${userStory.name}" />  
        <div class="input-group">
          <span class="input-group-addon" id="basic-addon1">As A</span>
          <input name="as-a-story" type="text" class="form-control" placeholder="user" value="${userStory.asA}" />
          <span class="input-group-addon" id="basic-addon1">I want to</span>
          <input name="i-want-to-story" type="text" class="form-control" placeholder="some goal" value="${userStory.iWantTo}" />
          <span class="input-group-addon" id="basic-addon1">so that</span>
          <input name="so-that-story" type="text" class="form-control" placeholder="some reason" value="${userStory.soThat}" />
        </div>
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
    </form>
    
    <script type="text/javascript">
        reloadUserStoryForm(userStory_storyPoints, userStory_status);
    </script>

</t:index>