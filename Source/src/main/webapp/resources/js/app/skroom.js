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