Button MM_P;

void configureUI(){

  MM_P = new Button(width/3,height/2,width/3,height/10);
  MM_P.string("Play");
  MM_P.setColor(color(20,20,20));
  MM_P.mouseOverColor(color(40,40,40));
  MM_P.mouseDownColor(color(60,60,60));
  MM_P.init();


}

void UIConds(){

  if(MM_P.clicked()){

    State = gameState.GAME;

  }


}
