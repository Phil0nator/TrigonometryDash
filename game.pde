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
float bounceFactor = .5;


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

  }else if (type == 5){

    triangle(0,0,BLOCK_DIMENTION,0,BLOCK_DIMENTION/2,BLOCK_DIMENTION);

  }else if (type == 6){

    line(BLOCK_DIMENTION/3,BLOCK_DIMENTION,BLOCK_DIMENTION/3+1,(BLOCK_DIMENTION/3)*(float)random(1,3));
    line((BLOCK_DIMENTION/3)*2,BLOCK_DIMENTION,((BLOCK_DIMENTION/3)*2)+1,(BLOCK_DIMENTION/3)*(float)random(1,3));
    line((BLOCK_DIMENTION/3)*3,BLOCK_DIMENTION,((BLOCK_DIMENTION/3)*3)+1,(BLOCK_DIMENTION/3)*(float)random(1,3));


  }else if (type == 7){

    line(BLOCK_DIMENTION/2,BLOCK_DIMENTION,BLOCK_DIMENTION/2,BLOCK_DIMENTION*(2.0/3));
    float r = random(5,BLOCK_DIMENTION/3);
    ellipse(BLOCK_DIMENTION/2,BLOCK_DIMENTION*(2.0/3),r,r);

  }else if (type == 8){
    fill(255,202,24);
    ellipse(+BLOCK_DIMENTION/2,+BLOCK_DIMENTION/2,BLOCK_DIMENTION,BLOCK_DIMENTION);

  }else if (type == 9){

    fill(196,255,14);
    ellipse(+BLOCK_DIMENTION/2,+BLOCK_DIMENTION/2,BLOCK_DIMENTION,BLOCK_DIMENTION);

  }else if (type == 10){

    fill(255,255,1,100);
    ellipse(+BLOCK_DIMENTION/2,+BLOCK_DIMENTION/2,BLOCK_DIMENTION*2*bounceFactor,BLOCK_DIMENTION*2*bounceFactor);
    fill(255,255,1);
    ellipse(+BLOCK_DIMENTION/2,+BLOCK_DIMENTION/2,BLOCK_DIMENTION,BLOCK_DIMENTION);



  }else if (type == 11){

    fill(255,1,255,100);
    ellipse(+BLOCK_DIMENTION/2,+BLOCK_DIMENTION/2,BLOCK_DIMENTION*2*bounceFactor,BLOCK_DIMENTION*2*bounceFactor);
    fill(255,1,255);
    ellipse(+BLOCK_DIMENTION/2,+BLOCK_DIMENTION/2,BLOCK_DIMENTION,BLOCK_DIMENTION);



  }

  popMatrix();

}

void die(){

  velx=0;
  deathParticles();
  pstate = PlayerState.SQUARE;

}

void drawPlayer(){

  pushMatrix();
  translate(width/2+BLOCK_DIMENTION/2,height/2+BLOCK_DIMENTION/2);
  rotate(radians(rot));
  fill(PLAYER_COLOR);
  if(pstate == PlayerState.SQUARE){
    rect(-BLOCK_DIMENTION/2,-BLOCK_DIMENTION/2,BLOCK_DIMENTION,BLOCK_DIMENTION);
  }else if (pstate == PlayerState.SAW){
    ellipse(0,0,BLOCK_DIMENTION,BLOCK_DIMENTION);
  }else if (pstate == PlayerState.PLANE){
    triangle(-BLOCK_DIMENTION/2,-BLOCK_DIMENTION/2,-BLOCK_DIMENTION/2, 0,0,-BLOCK_DIMENTION/4);
  }
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
        // 0  = nothing
        // 1  = block normal
        // 2  = spike
        // 3  = reverse gravity pad
        // 4  = jump pad
        // 5  = upside-down spikes
        // 6  = harmless grass
        // 7  = harmless bloopy thing
        // 8  = portal to saw
        // 9  = portal to plane
        // 10 = in-air jump blob
        // 11 = in-air reverse gravity
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
        }else if (dat.get(i,j) == color(255,1,0)){
          data[i][j] = 5;
        }else if (dat.get(i,j) == color(140,255,251)){
          data[i][j] = 6;
        }else if (dat.get(i,j) == color(0,168,243)){
          data[i][j] = 7;
        }else if (dat.get(i,j) ==color(255,202,24)){
          data[i][j] = 8;
        }else if (dat.get(i,j) == color(196,255,14)){
          data[i][j] = 9;
        }else if (dat.get(i,j) == color(255,255,1)){
          data[i][j] = 10;
        }else if (dat.get(i,j) == color(255,1,255)){
          data[i][j] = 11;
        }

      }

    }


  }

  void physics(){

    x+=velx;
    y+=vely;
    if(pstate == PlayerState.PLANE){
      planeParticles();
    }

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

    int indX = ((x)+width/2)/BLOCK_DIMENTION;
    int indY = ((y)+height/2)/BLOCK_DIMENTION;


    if ((indY>WORLD_DIMENTION-2)){
      if(velx!=0)die();
      return;
    }

    if(indX>WORLD_DIMENTION-2){
      if(velx!=0){
        worldNumber++;
        particles.clear();
        die();
      }
      return;
    }


    if(data[indX][indY+gravity] == 1){
      if(((gravity==1&&vely>0)||(gravity==-1&&vely<0))){
      vely=0;
      y = indY*BLOCK_DIMENTION - (height/2);
      inAir=false;
      rot = 0;
      rotvel = 0;

    }else{



    }
    if(velx!=0){
      moveParticles();
    }
    }else{
      vely+=gravity;
      inAir = true;
      if(pstate == PlayerState.SQUARE){
        rot += 10*gravity;
      }
    }

    if(inAir){

      if(pstate == PlayerState.PLANE){

        if(rot <360-45){
          rot = 360-45;
        }
        rot+= 1;

      }

      if(mousePressed){

        if(data[indX][indY] == 10){
          vely-=15*gravity;
        }if(data[indX][indY] == 11){
          vely=-vely;
          gravity = -gravity;
        }

      }

    }


    rolledOver(data[indX][indY]);



  }

  void d(){

    if(bounceFactor < 1){
      bounceFactor*=1.051;
    }else{
      bounceFactor*=.95;
    }

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

void rolledOver(int n){

     if(n == 1 &&velx!=0){

      die();
    }

    if(((n == 2)||(n==5))&&velx!=0){

      die();

    }

    if(n == 3&&velx!=0){

      gravity = -gravity;
      world.y+=gravity*BLOCK_DIMENTION;

    }

    if((n == 4)){

      vely-=gravity*50;

    }

    if(n == 8){
     if(pstate == PlayerState.SQUARE){
       pstate = PlayerState.SAW;
     } else{
       pstate = PlayerState.SQUARE;
     }
    }
    if(n == 9){
     if(pstate == PlayerState.SQUARE){
       pstate = PlayerState.PLANE;
     }else{
        pstate = PlayerState.SQUARE;
     }
    }

}
