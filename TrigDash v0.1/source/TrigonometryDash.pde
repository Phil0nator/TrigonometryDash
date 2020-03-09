boolean keys[] = new boolean[1024];
PImage bg;
World world;
int worldNumber = 1;


enum gameState{

  MAIN_MENU, GAME, PAUSE;

}

gameState State = gameState.MAIN_MENU;

void setup(){

  fullScreen(P2D);
  setupUI();
  configureUI();
  bg = loadImage("world\\bg.png");
  world = new World("world\\w"+worldNumber+".png");
  world.y = (BLOCK_DIMENTION*WORLD_DIMENTION) - (20*BLOCK_DIMENTION);
  frameRate(45);


}
void keyPressed(){

  keys[keyCode] = true;}

void keyReleased(){

  keys[keyCode] = false;}

void keyPress(){


  boolean validKey = keys[87]||keys[32]||keys[38];

  if(validKey&&!inAir&&velx!=0){

    vely-=15*gravity;

    rotvel += jumpRotVel;

  }




}

void draw(){

  background(0);
  keyPress();

  if(State==gameState.GAME){

    image(bg,-world.x/100,0);
    world.d();
    handleParticles();
    if(velx==0){
      if(particles.size()==0){
        world = new World("world\\w"+worldNumber+".png");
        world.y = (BLOCK_DIMENTION*WORLD_DIMENTION) - (20*BLOCK_DIMENTION);
        velx = 10;
        gravity = 1;
      }
    }

  }

  if(State!=gameState.GAME){

    updateUI();
    UIConds();
  }

}
