import processing.sound.*;



public final int WORLD_COUNT = 10;

boolean keys[] = new boolean[1024];
PImage bg;
World world;
int worldNumber = 1;
boolean loaded = false;

SoundFile music[] = new SoundFile[WORLD_COUNT];
SoundFile death;


enum gameState{

  MAIN_MENU, GAME, PAUSE, LOADING;

}

enum PlayerState{

  SQUARE, SAW, PLANE;

}
void loadSongs(){

    death = new SoundFile(this, "sound\\d.mp3");
    music[1] = new SoundFile(this, "sound\\1.mp3");
    music[1].amp(.2);
    music[2] = new SoundFile(this, "sound\\1.mp3");
    music[2].amp(.2);
    music[3] = new SoundFile(this, "sound\\1.mp3");
    music[3].amp(.2);
    loaded=true;
}

gameState State = gameState.LOADING;
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

  thread("loadSongs");

}
void keyPressed(){

  keys[keyCode] = true;}

void keyReleased(){

  keys[keyCode] = false;}

void keyPress(){


  boolean validKey = keys[87]||keys[32]||keys[38];

  if(validKey||mousePressed){

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
        music[worldNumber].play();
        death.stop();
      }
    }

  }

  if(State!=gameState.GAME&&State!=gameState.LOADING){

    updateUI();
    UIConds();
  }else if (State == gameState.LOADING){
    fill(random(0,255),255,random(100,200));
    ellipse(width/2,height/2,random(0,50),random(0,50));
    if(loaded){
    State = gameState.MAIN_MENU;
    }
  }

}
