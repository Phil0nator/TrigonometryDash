
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
    var mm = document.getElementById("MM");
    hide(mm);
    ingame=true;
    

}



function keyUp(event){
    if(event.keyCode==27){
        //esc

    }
}
document.addEventListener('keyup', keyUp, false);

