int gravity = 1;
final float jumpRotVel = 1;
final int WORLD_DIMENTION = 500;
final int BLOCK_DIMENTION = 50;

int velx = 5;
int vely = 0;




void drawChunk(int type, int x, int y){

  pushMatrix();
  translate(-world.x+x*BLOCK_DIMENTION,-world.y+y*BLOCK_DIMENTION);
  stroke(255,255,255);
  fill(255,255,255,50);
  if(type==0){

  }else if (type == 1){
    rect(0,0,BLOCK_DIMENTION,BLOCK_DIMENTION);
  }

  popMatrix();

}

void drawPlayer(){

  rect(width/2,height/2+25,25,25);


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
    }

    int indX = (x+width/2)/BLOCK_DIMENTION;
    int indY = (y+height/2)/BLOCK_DIMENTION;

    if(data[indX][indY+1] == 1){
      vely=0;
      y = indY*BLOCK_DIMENTION - (height/2);
    }else{
      vely++;
    }


  }

  void d(){
    physics();


    drawPlayer();


    for(int i = 0 ; i < WORLD_DIMENTION;i++){

      for(int j = 0 ; j < WORLD_DIMENTION; j++){

        drawChunk(data[i][j],i,j);

      }
    }

  }


}
