<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>
<form method="post">

<input name="name" value="${ name }" />
<textarea name="description">${ description }</textarea>
<button type="submit" >${ submitButtonText }</button>
</form>
<c:if test="${ displayDeleteButton eq 'true' }">
    <%@include file="/WEB-INF/views/subviews/projectDeleteForm.jsp" %>
</c:if>
</t:index>