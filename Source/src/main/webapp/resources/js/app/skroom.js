/**
 * Method for selecting element in <select> list by supplied value.
 * 
 * @author Wojciech Stanisławski
 * @since 05.12.16
 */
function searchForOptionInSelectAndCheckIfEquals(children, value) {
    var i;
    for(i = 0; i < children.length; i++) {
        var child = children[i];
        if(child.value == value) {
            child.setAttribute("selected", true);
            return;
        }
    }
}

/**
 * Selects current values in user story form <select> elements.
 * 
 * @author Wojciech Stanisławski
 * @since 05.12.16
 */
function reloadUserStoryForm(storyPoints, storyStatus) {
    if(storyPoints != undefined) {
        var select = document.getElementById("story-points-select");
        searchForOptionInSelectAndCheckIfEquals(select.children, storyPoints);
        
    }
    if(storyPoints != undefined) {
        var select = document.getElementById("user-story-status-select");
        searchForOptionInSelectAndCheckIfEquals(select.children, storyStatus);
    }
}

/**
 * Saves in database how many user stories user wants to display.
 * 
 * @author Wojciech Stanisławski
 * @since 05.12.16
 */
function saveUserStoriesPerPage(value) {
    $.ajax({
        type : "POST",
        data : { 'perPage': value },
        url : "rest/userSettings/userStoriesPerPage",
        complete : function(response) {
            location.reload();
        }
    });
}

/**
 * Saves in database which project is selected.
 * 
 * @author Wojciech Stanisławski
 * @since 05.12.16
 */
function selectProject(value) {
    $.ajax({
        type : "POST",
        url : "rest/userSettings/selectProject",
        data: {"projectId": value},
        complete : function(response) {
            location.reload();
        }
    });
}