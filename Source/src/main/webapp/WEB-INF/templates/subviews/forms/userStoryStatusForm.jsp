<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="s" uri="/WEB-INF/skroom-tags.tld"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<h1>Available User Story Statuses</h1>
<form id="userStoryStatusesForm">
    <div class="management-bar">
        <a href="javascript:addNewValue()" class="btn green" >Add new</a>
        <a href="javascript:sendUserStoryStatusesForm()" class="btn">Save changes</a>
    </div>
    <table class="table">
        <thead>
            <tr>
                <th>Name</th>
                <th>Color</th>
                <th>Archive only</th>
                <th>Delete</th>
            </tr>
        </thead>
        <tbody id="userStoriesStatusesTableBody">
        <c:forEach var="usStatus" items="${usStatuses}">
            <tr>
                <input type="hidden" name="usIds[]" value="${usStatus.id}" />
                <td><input name="usNames[]" class="form-control auto-cont" value="${usStatus.name}" style="display: none;"><a class="usFormSwitchControl" class="link-notarget">${usStatus.name}</a></td>
                <td><input name="usColors[]" class="form-control auto-cont color-picker" value="${usStatus.color}" style="display: none;"><a class="usFormSwitchControl" style="color: ${usStatus.color};">${usStatus.color}</a></td>
                <td><input name="isArchive[]" value="${usStatus.id}" type="checkbox" <c:if test="${usStatus.archive}">checked="checked"</c:if> /></td>
                <td><a onclick="deleteRealValue(this, ${usStatus.id});" class="link-notarget"><i class="fa fa-trash" aria-hidden="true"></i></a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div id="ussAlert" class="alert alert-danger" style="display: none;">Warning cośtam</div>
    <script type="text/javascript">
        $(".usFormSwitchControl").click(function(e) {
            var tar = $(e.target);
            tar.fadeOut(300, function() {
                tar.parent().find(".form-control").fadeIn(300);
            });
        });
        
        $(".color-picker").colorpicker();
        
        function addNewValue() {
            var body = $("#userStoriesStatusesTableBody");
            var tabSize = body.children("tr").length;
            var startColor = "#FFFF00";
            var newElem = $("<tr>" +
                            "<input type=\"hidden\" name=\"usIds[]\" value=\"-" + tabSize + "\" />" +
                            "<td><input name=\"usNames[]\" class=\"form-control auto-cont\" /></td>" +
                            "<td><input name=\"usColors[]\" class=\"form-control auto-cont color-picker\" value=\"" + startColor +"\" /></td>" +
                            "<td><input name=\"isArchive[]\" value=\"-" + tabSize + "\" type=\"checkbox\" /></td>" +
                            "<td><a onclick=\"deleteValue(this);\" class=\"link-notarget\"><i class=\"fa fa-trash\" aria-hidden=\"true\"></i></a></td>" +
                            "</tr>"
            );
            body.append(newElem);
            $(".color-picker").colorpicker();
        }

        function deleteValue(sel) {
            $(sel).parents("tr").remove();
        }
        
        function deleteRealValue(sel, value) {
            $.ajax({
                type: "POST",
                url: "rest/admin/deleteUserStoryStatus",
                data: {"ussId": value},
                success: function(data) {
                    if(data === "OK") {
                        deleteValue(sel);
                        $("#ussAlert").fadeOut(300);
                    }
                    if(data === "ERROR") {
                        $("#ussAlert").html("<b>Unexpected error ocurred during deletion.</b> Please try again later.").fadeIn(300);
                    }
                    if(data === "USER_STORIES_EXIST") {
                        $("#ussAlert").html("<b>User stories with this status still exists.</b> Please delete all user stories with this status or change their status.").fadeIn(300);
                    }
                    if(data === "NOADMIN") {
                        window.location.href = document.URL + "/../";
                    }
                }
            });
        }
        
        function sendUserStoryStatusesForm() {
            var formStr = $("#userStoryStatusesForm").serialize();
            $.ajax({
                type: "POST",
                url: "rest/admin/changeUserStoryStatuses",
                data: formStr,
                success: function(data) {
                    location.reload();
                }
            });
        }
    </script>
</form>