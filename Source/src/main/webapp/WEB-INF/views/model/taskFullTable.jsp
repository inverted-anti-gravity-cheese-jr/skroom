<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<c:forEach var="task" items="${tasks}">
    <%@ include file="/WEB-INF/views/model/taskTableView.jsp"%>
</c:forEach>