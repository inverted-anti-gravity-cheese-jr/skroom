<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<t:index>

<spring:url value="/resources/css/app/adminPanel.css" var="adminCss" />
<link href="${adminCss}" rel="stylesheet" />


<div class="admin-panel">
    <h1>Users</h1>

    <form method="get"><input name="un" /></form>
    <form method="get">
        Users per page
        <select id="adminPanelUppSelect" name="upp" onchange="this.form.submit()">
            <option value="5">5</option>
            <option value="10">10</option>
            <option value="15">15</option>
            <option value="25">25</option>
            <option value="50">50</option>
        </select>
    </form>
    <script type="text/javascript">
    function adminUppPick() {
        var i, upp, url = window.location.toString();
        upp = document.getElementById("adminPanelUppSelect");
        for(i = 0; i < upp.children.length; i++) {
            var option = upp.children[i];
            if(option.getAttribute("value") == "5") {
                continue;
            }
            if(url.search("upp="+option.getAttribute("value")) != -1) {
                option.setAttribute("selected", "true");  
            }
        }
    }
        
    adminUppPick();
    </script>
    
    <table class="table">
        <thead><tr><td>Name</td><td>Email</td><td>Global privilages</td></tr></thead>
        <tbody>
            <c:forEach var="user" items="${globalUsers}">
                <tr><td>${user.name}</td><td>${user.email}</td><td>${user.role}</td></tr>
            </c:forEach>
        </tbody>
    </table>

    <h1>Projects</h1>
</div>

</t:index>