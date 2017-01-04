<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>
    
    <script type="text/javascript">
        function switchViews(showEdit) {
            if(showEdit) {
                $(".user-story-body").slideUp(function () {
                    $(".user-story-form").slideDown();
                });

            }
            else {
                $(".user-story-form").slideUp(function () {
                    $(".user-story-body").slideDown();
                });
            }
        }

        <c:if test="${not empty userStory}">
            var userStory_storyPoints = "${userStory.storyPoints.displayName}";
            var userStory_status = "${userStory.status.name}"
        </c:if>
    </script>
    
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
    
    <form class="skroom-form user-story-form" action="../editUserStory/${userStory.id}" method="post">
        <div class="management-bar">
            <input type="submit" class="btn" value="Save changes" />
            <button type="button" class="btn" onclick="switchViews(false)">Cancel</button>
            <a href="../removeUserStory/${userStory.id}" class="btn red pull-right">Delete</a>
        </div>

        </c:when>
        <c:otherwise>
    <h1>New user story</h1>
    <form class="skroom-form user-story-form" method="post" style="display: initial;">
        <div class="management-bar">
            <input type="submit" class="btn" value="Save" />
        </div>
        </c:otherwise>
    </c:choose>
        <label>Name</label>
        <input name="userStoryName" type="text" class="form-control" placeholder="User story name" value="${userStory.name}" />
        <label>User story</label>
        <div class="input-group">
          <span class="input-group-addon">As A</span>
          <input name="as-a-story" type="text" class="form-control" placeholder="user" value="${userStory.asA}" />
          <span class="input-group-addon">I want to</span>
          <input name="i-want-to-story" type="text" class="form-control" placeholder="some goal" value="${userStory.iWantTo}" />
          <span class="input-group-addon">so that</span>
          <input name="so-that-story" type="text" class="form-control" placeholder="some reason" value="${userStory.soThat}" />
        </div>
        <label>Description</label>
        <textarea name="userStoryDescription" class="form-control" placeholder="Description">${userStory.description}</textarea>
        <label>Priority</label>
        <div class="input-group">
          <span class="input-group-addon" id="basic-addon1">P</span>
          <input name="priority" type="text" class="form-control" placeholder="Priority" value="${userStory.priority}" />
        </div>
        <label>Story points</label>
        <select id="story-points-select" name="userStoryPoints" class="form-control">
            <c:forEach var="sp" items="${availableStoryPoints}">
                <option>${sp}</option>
            </c:forEach>
        </select>
        <label>Status</label>
        <select id="user-story-status-select" name="userStoryStatus" class="form-control">
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