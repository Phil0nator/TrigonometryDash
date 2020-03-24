
function hide (elements) {
    elements = elements.length ? elements : [elements];
    for (var index = 0; index < elements.length; index++) {
      elements[index].style.display = 'none';
    }
    
}

function show(elements){
    elements = elements.length ? elements : [elements];
    for (var index = 0; index < elements.length; index++) {
      elements[index].style.display = 'block';
    }
    
}

var _MMTP = function(){
    if(!fullyLoaded)return;
    var mm = document.getElementById("MM");
    hide(mm);
    document.getElementById("loading").style.display = "none";
    ingame=true;
}

var _MMTPLS = function(){
    window.location.href = "levelSelect.html";

}

var onLoad = function(event){
    fullyLoaded =true;
    var mm = document.getElementById("MM");
    show(mm);
    document.getElementById("loading").style.display = "none";    
    
    var info = document.getElementById("info");
    info.innerHTML = "<u><b> Welcome </b></u> </br >Current Level: "+ currentWorld + "</br >Levels Completed: "+worldsCompleted;
    
}


function keyUp(event){
    if(event.keyCode==27){
        //esc

    }
}
document.addEventListener('keyup', keyUp, false);
window.addEventListener('load', function () {
    onLoad(null);
})
