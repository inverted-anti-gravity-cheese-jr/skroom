<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>
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
    <c:if test="${pages > 1}">
    <div class="btn-group" role="group">
    <c:forEach var="page" begin="0" end="${pages}">
        <a href="?p=${page}" class="btn btn-default">${page + 1}</a>
    </c:forEach>
    </div>
    </c:if>

    <h1>Sprints</h1>

    <div id="sprints-calendar-container">
        <div id="sprints-calendar"></div>
    </div>


    <script type="text/javascript">
        function reloadUpp() {
            var us = document.getElementById("productBacklogUserStoriesUpp");
            searchForOptionInSelectAndCheckIfEquals(us.children, ${userSettings.userStoriesPerPage});
        }

        reloadUpp();


        function createCalendar() {

            $("#sprints-calendar").fullCalendar({
                locale: "${pageContext.response.locale}",
                editable: true,
                eventLimit: true, // allow "more" link when too many events
                events: [
                    {
                        title: 'Sprint 1',
                        start: '2016-12-01',
                        end: "2016-12-07"
                    },
                    {
                        title: 'Sprint 2',
                        start: '2016-12-08',
                        end: '2016-12-14',
                        url: 'http://google.com/'
                    }
                ]
            });
            
        }

        createCalendar();

    </script>
    
</t:index>