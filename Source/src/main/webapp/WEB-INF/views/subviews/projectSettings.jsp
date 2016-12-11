<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>
    <c:choose>
        <c:when test="${createProject}">
            <jsp:include page="forms/projectForm.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <c:if test="${projectIsEditable}">
                <jsp:include page="forms/projectForm.jsp"></jsp:include>
            </c:if>
        </c:otherwise>
    </c:choose>
</t:index>