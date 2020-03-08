boolean keys[] = new boolean[1024];
PImage bg;
World world;
enum gameState{

  MAIN_MENU, GAME, PAUSE;

}

gameState State = gameState.MAIN_MENU;

void setup(){

  fullScreen();
  setupUI();
  configureUI();

  bg = loadImage("world\\bg.png");


  world = new World("world\\w1.png");
  world.y = (BLOCK_DIMENTION*WORLD_DIMENTION) - (20*BLOCK_DIMENTION);

}
void keyPressed(){

  keys[keyCode] = true;}

void keyReleased(){

  keys[keyCode] = false;}

void keyPress(){

  if(keys[87]&&!inAir&&velx!=0){

    vely-=15;
    rotvel += jumpRotVel;
  }




}

void draw(){


  keyPress();

  if(State==gameState.GAME){

    image(bg,-world.x/100,0);
    world.d();
    handleParticles();
    if(velx==0){

      if(particles.size()==0){
        world = new World("world\\w1.png");
        world.y = (BLOCK_DIMENTION*WORLD_DIMENTION) - (20*BLOCK_DIMENTION);
        velx = 10;
      }

    }

  }

  if(State!=gameState.GAME){
    background(0);
    updateUI();
    UIConds();
  }

}
