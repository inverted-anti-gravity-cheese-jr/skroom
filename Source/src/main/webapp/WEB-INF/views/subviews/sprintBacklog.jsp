<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>

    <h1>Tasks</h1>
    <div class="management-bar">
        <span class="bar-text">Choose sprint
            <select>
            <c:forEach var="sprint" items="${sprints}">
                <option>${sprint.name}</option>    
            </c:forEach>
            </select>
        </span>
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
            
        </tbody>
    </table>

</t:index>