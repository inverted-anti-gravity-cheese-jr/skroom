<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>
    
    <script type="text/javascript">
        function reloadUpp() {
            var us = document.getElementById("productBacklogUserStoriesUpp");
            searchForOptionInSelectAndCheckIfEquals(us.children, ${userSettings.userStoriesPerPage});
        }

        function createCalendar() {
            var evs = [
            <c:forEach var="sprint" items="${sprints}">
                {
                    id: ${sprint.id},
                    title: "${sprint.name}",
                    start: "${sprint.startDate}",
                    end: "${sprint.endDate}"
                },
            </c:forEach>
                {}
            ];
            evs.pop();
            
            $("#sprints-calendar").fullCalendar({
                locale: "${pageContext.response.locale}",
                editable: false,
                eventLimit: true, // allow "more" link when too many events
                events: evs
            });
            
        }
        
        function showSprintNameForm() {
            $("#sprint-create-form").slideDown();
        }
    </script>
    
    <h1>Product backlog</h1>
    <div class="management-bar"> <!-- "management-bar" -->
        <a href="addUserStory" class="btn green">Add user story</a>
        <button class="btn">Show filters</button>
        <button class="btn">Order by <span class="caret"></span></button>
        <span class="bar-text">Users per page</span>
        <select id="productBacklogUserStoriesUpp" name="upp" onchange="saveUserStoriesPerPage(this.value)">
            <option value="5">5</option>
            <option value="10">10</option>
            <option value="15">15</option>
            <option value="25">25</option>
            <option value="50">50</option>
        </select>
    </div>
    
    <table class="table">
        <thead>
            <tr>
                <td>No</td>
                <td>User story</td>
                <td>Priority</td>
                <td>Story points</td>
                <td>Status</td>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="userStory" items="${userStories}">
                <%@ include file="/WEB-INF/views/model/userStoryTableView.jsp"%>
            </c:forEach>
        </tbody>
    </table>
    <c:if test="${fn:length(userStories) == 0}">
        <h4>No existing user stories, <a href="addUserStory">add one!</a></h4>
    </c:if>
    <c:if test="${pages > 0}">
    <div class="btn-group" role="group">
    <c:forEach var="page" begin="0" end="${pages}">
        <a href="?p=${page}" class="btn btn-default">${page + 1}</a>
    </c:forEach>
    </div>
    </c:if>

    <h1>Sprints</h1>

    
    <div class="management-bar">
        <button onclick="showSprintNameForm();" class="btn green">Create new sprint</a>
        <button onclick="closeCurrentSprint();" type="button" class="btn">Close current sprint</button>
    </div>
    <div id="sprint-create-form" style="display: none;">
        <input class="form-control" id="sprint-name" placeholder="Sprint name" value="Sprint ${sprintCount + 1}" />
        <button class="btn green" onclick="createNewSprint('sprint-name');">Submit</button>
    </div>
    <div id="sprints-calendar-container">
        <div id="sprints-calendar"></div>
    </div>


    <script type="text/javascript">
        reloadUpp();
        createCalendar();
    </script>
    
</t:index>