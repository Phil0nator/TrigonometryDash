int gravity = 1;
final float jumpRotVel = 60;
final int WORLD_DIMENTION = 500;
final int BLOCK_DIMENTION = 50;
final color PLAYER_COLOR = color(50,255,50,235);
boolean inAir = false;
int velx = 10;
int vely = 0;
float rot = 0;
float rotvel = 0;



void drawChunk(int type, int x, int y){

  pushMatrix();
  translate(-world.x+x*BLOCK_DIMENTION,-world.y+y*BLOCK_DIMENTION);
  stroke(255,255,255);
  fill(255,255,255,50);

  if(type==0){

  }else if (type == 1){
    rect(0,0,BLOCK_DIMENTION,BLOCK_DIMENTION);
  }else if (type == 2){

    triangle(0,BLOCK_DIMENTION,BLOCK_DIMENTION,BLOCK_DIMENTION,BLOCK_DIMENTION/2,0);

  }else if (type == 3){

    fill(255,0,255,200);
    ellipse(+BLOCK_DIMENTION/2,+BLOCK_DIMENTION/2,BLOCK_DIMENTION,BLOCK_DIMENTION);

  }else if (type == 4){
    fill(255,255,0);
    ellipse(+BLOCK_DIMENTION/2,+BLOCK_DIMENTION/2,BLOCK_DIMENTION,BLOCK_DIMENTION);

  }

  popMatrix();

}

void die(){

  velx=0;
  deathParticles();


}

void drawPlayer(){



  pushMatrix();
  translate(width/2+BLOCK_DIMENTION/2,height/2+BLOCK_DIMENTION/2);
  rotate(radians(rot));
  fill(PLAYER_COLOR);
  rect(-BLOCK_DIMENTION/2,-BLOCK_DIMENTION/2,BLOCK_DIMENTION,BLOCK_DIMENTION);
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

        /////////////////////////////
        // 0 = nothing
        // 1 = block normal
        // 2 = spike
        // 3 = reverse gravity pad
        // 4 = jump pad
        //////////////////////////////

        if(dat.get(i,j)==color(0,0,0)){
          data[i][j] = 0;
        }else if (dat.get(i,j) == color(255,255,255)){
          data[i][j] = 1;
        }else if (dat.get(i,j) == color(255,0,0)){
          data[i][j]=2;
        }else if (dat.get(i,j) == color(255,0,255)){
          data[i][j]=3;
        }else if (dat.get(i,j) == color(255,255,0)){
          data[i][j] = 4;
        }

      }

    }


  }

  void physics(){

    x+=velx;
    y+=vely;



    if(vely<0&&gravity==1){
      vely++;
      inAir = true;
    }else if(vely>0&&gravity==-1){
      vely--;
      inAir = true;
    }

    if(rotvel>0){

      rotvel-=5;

    }else{
      rotvel = 0;
    }

    int indX = (x+width/2)/BLOCK_DIMENTION;
    int indY = ((y+(25*(gravity+1)))+height/2)/BLOCK_DIMENTION;

    if ((indY>WORLD_DIMENTION-2)){
      if(velx!=0)die();
      return;
    }

    if(indX>WORLD_DIMENTION-2){
      if(velx!=0)worldNumber++;
      particles.clear();
      return;
    }

    if(data[indX][indY+gravity] == 1){
      vely=0;
      y = indY*BLOCK_DIMENTION - (height/2);
      inAir=false;
      rot = 0;
      rotvel = 0;
      if(velx!=0){
        moveParticles();
      }
    }else{
      vely+=gravity;
      inAir = true;
      rot += 10;
    }


    if(data[indX+1][indY] == 1 &&velx!=0){

      die();
    }

    if(data[indX][indY] == 2&&velx!=0){

      die();

    }

    if(data[indX][indY] == 3&&velx!=0){

      gravity = -gravity;
      y+=gravity*BLOCK_DIMENTION;

    }

    if(data[indX][indY] == 4){

      vely-=gravity*50;

    }


  }

  void d(){
    physics();
    if(velx!=0){
      drawPlayer();
    }

    int blocksPerScreen = width/BLOCK_DIMENTION;
    int blocksPerHeight = height/BLOCK_DIMENTION;

    int indX = (x)/BLOCK_DIMENTION + 1;
    int indY = (y)/BLOCK_DIMENTION + 1;



    for(int i = indX ; i < indX+blocksPerScreen-1;i++){

      for(int j = indY ; j < indY+blocksPerHeight-1; j++){

        if(i<WORLD_DIMENTION&&j<WORLD_DIMENTION)
        drawChunk(data[i][j],i,j);

      }
    }

  }


}
