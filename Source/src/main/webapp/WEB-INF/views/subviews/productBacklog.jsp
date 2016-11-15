<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>
    <h1>Product backlog</h1>
    <div class="management-bar">
        <button class="btn green">Add user story</button>
        <button class="btn">Show filters</button>
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
    
</t:index>