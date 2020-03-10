boolean keys[] = new boolean[1024];
PImage bg;
World world;
int worldNumber = 2;


enum gameState{

  MAIN_MENU, GAME, PAUSE;

}

enum PlayerState{
 
  SQUARE, SAW, PLANE;
  
}


gameState State = gameState.MAIN_MENU;
PlayerState pstate = PlayerState.SQUARE;
void setup(){

  size(1920,1080,P2D);
  smooth(8);
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

  if(validKey){

    inputPlayer();
    
  }




}

void inputPlayer(){
  if(!inAir&&velx!=0){
    if(pstate == PlayerState.SQUARE){
    
      vely-=15*gravity;
  
      rotvel += jumpRotVel;
  
      
    }else if (pstate == PlayerState.SAW){
     
      gravity=-gravity;
      
    }
  }
   
  if(pstate == PlayerState.PLANE){
   
    vely-=gravity*3;
    rot-=2*gravity;
    
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
