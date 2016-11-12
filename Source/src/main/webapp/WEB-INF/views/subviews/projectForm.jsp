<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>

<form method="post">

<input name="name" value="${ name }" />
<textarea name="description">${ description }</textarea>
<button type="submit" >${ submitButtonText }</button>
</form>

</t:index>