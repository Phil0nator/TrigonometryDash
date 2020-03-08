int gravity = 1;
final float jumpRotVel = 60;
final int WORLD_DIMENTION = 500;
final int BLOCK_DIMENTION = 50;
boolean inAir = false;
int velx = 10;
int vely = 0;
float rot = 0;
float rotvel = 0;



void drawChunk(int type, int x, int y){

  pushMatrix();
  translate(-world.x+x*BLOCK_DIMENTION,-world.y+y*BLOCK_DIMENTION);

  if(type==0){

  }else if (type == 1){
    rect(0,0,BLOCK_DIMENTION,BLOCK_DIMENTION);
  }

  popMatrix();

}

void drawPlayer(){



  pushMatrix();
  translate(width/2+12,height/2+37);
  rotate(radians(rot));
  rect(-12,-12,25,25);
  popMatrix();

}

class World{

  int[][] data = new int[WORLD_DIMENTION][WORLD_DIMENTION];
  int x = 0;
  int y = 0;


  World(String p){

    PImage dat = loadImage(p);

    for(int i = 0 ; i < WORLD_DIMENTION;i++){

      for(int j = 0 ; j < WORLD_DIMENTION; j++){

        if(dat.get(i,j)==color(0,0,0)){
          data[i][j] = 0;
        }else if (dat.get(i,j) == color(255,255,255)){

          data[i][j] = 1;

        }

      }

    }


  }

  void physics(){

    x+=velx;
    y+=vely;



    if(vely<0){
      vely++;
      inAir = true;
    }

    if(rotvel>0){

      rotvel-=5;

    }else{
      rotvel = 0;
    }

    int indX = (x+width/2)/BLOCK_DIMENTION;
    int indY = (y+height/2)/BLOCK_DIMENTION;

    if(data[indX][indY+1] == 1){
      vely=0;
      y = indY*BLOCK_DIMENTION - (height/2);
      inAir=false;
      rot = 0;
      rotvel = 0;
      if(velx!=0){
        moveParticles();
      }
    }else{
      vely++;
      inAir = true;
      rot += 10;
    }


    if(data[indX+1][indY] == 1 &&velx!=0){

      velx=0;
      deathParticles();

    }


  }

  void d(){
    physics();

    stroke(255,255,255);
    fill(255,255,255,50);
    drawPlayer();


    for(int i = 0 ; i < WORLD_DIMENTION;i++){

      for(int j = 0 ; j < WORLD_DIMENTION; j++){

        drawChunk(data[i][j],i,j);

      }
    }

  }


}
