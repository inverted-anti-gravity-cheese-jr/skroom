<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<h2>Available User Story Statuses</h2>
<form id="userStoryStatusesForm">
    <div class="management-bar">
        <a href="javascript:getFormValues()" class="btn green" >Add new</a>
        <a href="javascript:sendUserStoryStatusesForm()" class="btn">Save changes</a>
    </div>
    <table class="table">
        <thead>
            <tr>
                <th>Name</th>
                <th>Color</th>
                <th>Archive only</th>
            </tr>
        </thead>
        <c:forEach var="usStatus" items="${usStatuses}">
            <tr>
                <input type="hidden" name="usIds[]" value="${usStatus.id}" />
                <td><input name="usNames[]" class="form-control auto-cont" value="${usStatus.name}" style="display: none;"><a class="usFormSwitchControl" class="link-notarget">${usStatus.name}</a></td>
                <td><input name="usColors[]" class="form-control auto-cont color-picker" value="${usStatus.color}" style="display: none;"><a class="usFormSwitchControl" style="color: ${usStatus.color};">${usStatus.color}</a></td>
                <td><input name="isArchive[]" value="${usStatus.id}" type="checkbox" <c:if test="${usStatus.archive}">checked="checked"</c:if> /></td>
            </tr>
        </c:forEach>
    </table>
    <script type="text/javascript">
        $(".usFormSwitchControl").click(function(e) {
            var tar = $(e.target);
            tar.fadeOut(300, function() {
                tar.parent().find(".form-control").fadeIn(300);
            });
        });
        
        $(".color-picker").colorpicker();
        
        function getFormValues() {
            console.log($("#userStoryStatusesForm").serialize());
        }
        
        function sendUserStoryStatusesForm() {
            var formStr = $("#userStoryStatusesForm").serialize();
            $.ajax({
                type: "POST",
                url: "rest/admin/changeUserStoryStatuses",
                data: formStr,
                success: function(data) {
                    alert('Data send');
                }
            });
        }
    </script>
</form>