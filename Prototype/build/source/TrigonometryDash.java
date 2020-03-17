import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 
import java.time.Instant; 
import java.util.ArrayList; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class TrigonometryDash extends PApplet {





public final int WORLD_COUNT = 3;

boolean keys[] = new boolean[1024];
PImage bg;
World world;
int worldNumber = 2;
boolean loaded = false;

SoundFile music[] = new SoundFile[WORLD_COUNT];
SoundFile death;


enum gameState{

  MAIN_MENU, GAME, PAUSE, LOADING;

}

enum PlayerState{

  SQUARE, SAW, PLANE;

}
public void loadSongs(){

    death = new SoundFile(this, "sound\\d.mp3");
    music[1] = new SoundFile(this, "sound\\1.mp3");
    music[1].amp(.2f);
    music[2] = new SoundFile(this, "sound\\1.mp3");
    music[2].amp(.2f);
    loaded=true;
}

gameState State = gameState.LOADING;
PlayerState pstate = PlayerState.SQUARE;
public void setup(){

  
  
  setupUI();
  configureUI();
  bg = loadImage("world\\bg.png");
  world = new World("world\\w"+worldNumber+".png");
  world.y = (BLOCK_DIMENTION*WORLD_DIMENTION) - (20*BLOCK_DIMENTION);
  frameRate(45);

  thread("loadSongs");

}
public void keyPressed(){

  keys[keyCode] = true;}

public void keyReleased(){

  keys[keyCode] = false;}

public void keyPress(){


  boolean validKey = keys[87]||keys[32]||keys[38];

  if(validKey||mousePressed){

    inputPlayer();

  }




}

public void inputPlayer(){
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



public void draw(){

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
Button MM_P;

public void configureUI(){

  MM_P = new Button(width/3,height/2,width/3,height/10);
  MM_P.string("Play");
  MM_P.setColor(color(20,20,20));
  MM_P.mouseOverColor(color(40,40,40));
  MM_P.mouseDownColor(color(60,60,60));
  MM_P.init();


}

public void UIConds(){

  if(MM_P.clicked()){

    State = gameState.GAME;
    music[worldNumber].play();

  }


}
//Library by Philo Kaulkin to create simple UI elements for processing





//Quick Colors:

public final int WHITE = color(255);
public final int BLACK = color(0);
public final int RED = color(255,0,0);
public final int GREEN = color(0,255,0);
public final int BLUE = color(0,0,255);
public final int PURPLE = color(255,0,255);
public final int YELLOW = color(255,255,0);
public final int GRAY = color(100,100,100);
public final int CYAN = color(0,255,255);


enum direction{

  UP,DOWN,LEFT,RIGHT;

}

public void rotate(direction d){
  rotate(0);
}


ArrayList<UiElement> elements;
Entry activeEntry = new Entry(0, 0, 0, 0);

public long now() { //returns current time since epoch in milliseconds (just more shorthand than writing it all out)
  return Instant.now().toEpochMilli();
}


public abstract class UiElement {
  protected int x = 0;
  protected int y = 0;
  protected int w = 0;
  protected int h = 0;
  protected boolean Clicked = false;
  protected boolean holding = false;
  protected boolean expand = true;
  protected int col = 0;
  protected int mouseOverColor = 0;
  protected int mouseDownColor = 0;
  protected PImage TEXTURE;
  protected String text = "";
  protected int state = 0;
  protected int textSize = 0;
  protected int textColor = 0;
  protected long lastClicked = 0;
  protected boolean show = true;


  //only used in pages, helps with polymorphism
  ArrayList<UiElement> Elems = new ArrayList(1);



  public void moveToFront() {

    elements.remove(this);
    elements.add(this);
  }

  public void moveToBack() {

    elements.remove(this);
    elements.add(0, this);
  }



  protected void pressed() {
  }

  public void init() {
    elements.add(this);
  }

  public void hide() {
    show=false;
  }
  public void show() {
    show =true;
  }

  public void destroy() {
    elements.remove(this);
  }

  public void d() {
  }
  public void tick() {
  }
  public boolean clicked() {
    return false;
  }
}

public class Button extends UiElement {
  int col = color(200, 200, 200);
  int mouseOverColor = color(255, 255, 255);
  int mouseDownColor = color(255, 100, 200);
  String text = "Button";
  int textSize = 25;
  int textColor = color(100, 100, 100);
  boolean holding = false;
  boolean expand = true;
  PImage TEXTURE = null;
  boolean image = false;
  direction dir = null;

  public void setDir(direction newdir) {
    dir = newdir;
  }


  public void setColor(int incol) {
    col=incol;
  }
  public void mouseOverColor(int inmouseOverColor) {
    mouseOverColor = inmouseOverColor;
  }
  public void mouseDownColor( int inmouseDownColor) {
    mouseDownColor= inmouseDownColor;
  }
  public void string(String intext) {
    text = intext;
  }
  public void holdable(boolean inholding) {
    holding = inholding;
  }
  public void expander(boolean inexpand) {
    expand=inexpand;
  }
  public void text_size(int intextSize) {
    textSize = intextSize;
  }
  public void text_color(int intextColor) {
    textColor = intextColor;
  }
  public void setTexture(PImage texture) {
    TEXTURE  = texture;
    image = true;
    //TEXTURE.resize(w, h);
  }

  public Button(int inx, int iny, int inw, int inh) {//,, , ,  , color intextColor){
    x =inx;
    y = iny;
    w = inw;
    h = inh;
    state = 0;//0 = normal, 1 = mouseOver, 2 = mouseDown
  }

  public void d() {
    tick();
    if (show==false)return;
    pushMatrix();

    if (image==false) {
      if (state == 0) {
        fill(col);
      } else if (state == 1) {
        fill(mouseOverColor);
      } else {
        fill (mouseDownColor);
      }
      stroke(255);

      if (expand&&state==1) {
        rect(x-5, y-5, w+10, h+10);
      } else {
        rect(x, y, w, h);
      }
    } else {
      if (state == 0) {
        tint(col);
      } else if (state == 1) {
        tint(mouseOverColor);
      } else {
        tint (mouseDownColor);
      }
      if (dir!=null) {
        translate(x+w/2, y+h/2);
        rotate(dir);
        image(TEXTURE, -w/2, -h/2);
      } else {
        image(TEXTURE, x, y);
      }
    }

    fill(textColor);
    textSize(textSize);
    float textWidth = textWidth(text);
    float diff = w-textWidth;

    //text(text, (x+(w/2)-((text.length()*(textSize/4))/2)), y+(h/2)); // places the text in the middle of the button with some mathy maths
    text(text, x+(diff/2), y+(h/1.5f));
    popMatrix();
  }

  public void tick() {
    if (!mousePressed&&holding==false) {
      Clicked=false;
    }//prevents holding

    if (mouseX>x&&mouseX<(x+w)&&mouseY > y&&mouseY<(y+h)) {//if x in range && y in range
      if ((mousePressed&&Clicked==false)||(mousePressed&&holding==true)) {
        state=3;//mouse down
        Clicked = true;
      } else if (mousePressed&&Clicked==true) {
        state =2;//shows like mouse down but value is still false
      } else {
        state =1;//mouse over
      }
    } else {
      state = 0; // normal
    }
  }

  public boolean clicked() {
    if (show==false)return false;
    boolean out = state==3;
    return out;
  }
}


public class Entry extends UiElement {
  int col = color(200, 200, 200);
  boolean initialized = false;
  int mouseOverColor = color(255, 255, 255);
  int mouseDownColor = color(255, 100, 200);
  String text = "";
  String hintText = "Enter text here...";
  int textSize = 25;
  int textColor = color(100, 100, 100);
  boolean active = false;
  boolean mouseOver = false;
  int textInactiveColor = color(50, 50, 50);
  boolean clicked = false;
  boolean keydown = false;
  long lastCursor = now();
  boolean cursor = false;


  public void setColor(int incol) {
    col=incol;
  }
  public void mouseOverColor(int inmouseOverColor) {
    mouseOverColor = inmouseOverColor;
  }
  public void mouseDownColor( int inmouseDownColor) {
    mouseDownColor= inmouseDownColor;
  }
  public void hintText(String intext) {
    hintText = intext;
  }
  public void holdable(boolean inholding) {
    holding = inholding;
  }
  public void expander(boolean inexpand) {
    expand=inexpand;
  }
  public void text_size(int intextSize) {
    textSize = intextSize;
  }
  public void text_color(int intextColor) {
    textColor = intextColor;
  }
  public void text_inactiveColor(int in) {
    textInactiveColor = in;
  }


  public Entry(int inx, int iny, int inw, int inh) {
    x=inx;
    y=iny;
    w=inw;
    h=inh;

    initialized=true;
  }
  public void d() {
    if (show==false)return;

    tick();
    if (show==false)return;
    pushMatrix();
    if (mouseOver==false) {
      fill(col);
    } else if (mousePressed) {
      fill(mouseDownColor);
    } else {
      fill (mouseOverColor);
    }
    rect(x, y, w, h);
    textSize(textSize);
    if (active) {
      activeEntry=this;
      fill(textColor);

      if (cursor) {
        text(text+"|", x+10, y+textSize*1.5f);
      } else {
        text(text, x+10, y+textSize*1.5f);
      }
    } else {

      fill(textInactiveColor);
      if (text==""&&initialized==true) {
        text(hintText, x+10, y+textSize*1.5f);
      } else {
        text(text, x+10, y+textSize*1.5f);
      }
    }


    popMatrix();
  }
  public void tick() {
    if (mouseX>x&&mouseX<(x+w)&&mouseY > y&&mouseY<(y+h)) {
      mouseOver=true;
      if (mousePressed==true&&clicked==false) {
        active=!active;
        clicked = true;
      }
    } else {
      mouseOver = false;
      if (mousePressed==true&&clicked==false) {
        active=false;
      }
    }

    if (mousePressed==false) {
      clicked=false;
    }
    if (active) {
      if (now()-lastCursor>500) {
        cursor=!cursor;
        lastCursor = now();
      }
    }
  }


  public String get() {
    return text;
  }

  public void clear() {
    text="";
    active=false;
  }

  public void input(char key) {
    if (!active)return;
    text+=String.valueOf(key);
    if (typeable(keyCode)) {
    } else {
      if (key=='\n'||(keyCode>15&&keyCode<19)) {
        text=text.substring(0, text.length()-1);
      }

      if (key==8&&text.length()>1) {
        text=text.substring(0, text.length()-2);
      } else if (text.length()<=1&&key==8) {
        clear();
      }

      if (key == 32) {
        text+=" ";
      }
    }
  }
}

public class TextBox extends UiElement {

  TextBox(int inx, int iny, int intextSize, String intext) {
    x=inx;
    y=iny;
    text=intext;
    textSize=intextSize;
  }

  public void setText(String intext) {
    text=intext;
  }

  public void setSize(int insize) {
    textSize=insize;
  }

  public void setColor(int inp) {
    col=inp;
  }

  public void d() {
    if (show==false)return;

    fill(col);
    textSize(textSize);
    text(text, x, y);
    tick();
  }
  public void tick() {
  }
  public void update() {
    d();
  }
}


public class Page extends UiElement {
  boolean draggable = true;
  int clickedx;
  int clickedy;
  int col = color(100, 100, 100);
  int mouseOverColor=color(255, 255, 255);
  int mouseDownColor=color(200, 200, 255);
  public void setColor(int incol) {
    col=incol;
  }
  public void mouseOverColor(int inmouseOverColor) {
    mouseOverColor = inmouseOverColor;
  }
  public void mouseDownColor( int inmouseDownColor) {
    mouseDownColor= inmouseDownColor;
  }
  public void setDrag(boolean b) {
    draggable = b;
  }
  Page(int inx, int iny, int inw, int inh) {
    x=inx;
    y=iny;
    w=inw;
    h=inh;
  }

  public void add(UiElement element) {

    elements.remove(element);
    Elems.add(element);
  }

  public void detatch(UiElement element) {
    elements.add(element);
    Elems.remove(element);
  }

  public void destroy(UiElement element) {

    Elems.remove(element);
  }

  public void d() {
    if (show==false)return;

    fill(col);

    if (draggable==false) {
    } else if (state == 1) {
      fill(mouseOverColor);
    } else if (state == 2) {
      fill (mouseDownColor);
    }
    rect(x, y, w, h);

    for (int i = 0; i<Elems.size(); i++) {
      UiElement temp = Elems.get(i);
      temp.x+=x;
      temp.y+=y;
      temp.d();
      temp.x-=x;
      temp.y-=y;
    }

    tick();
  }

  public void tick() {
    if (draggable!=true) {
      return;
    }
    if (mouseX>x&&mouseX<(x+w)&&mouseY > y&&mouseY<(y+h)) {

      if (mousePressed==true) {
        if (state!=2) {
          clickedx = mouseX-x;
          clickedy=  mouseY-y;
        }
        state=2;
      } else {
        state = 1;
      }
    } else {
      if (state==2) {
        x = mouseX-clickedx;
        y=mouseY-clickedy;
      } else {
        state = 0;
      }
    }

    if (state==2&&draggable) {
      x = mouseX-clickedx;
      y=mouseY-clickedy;
    }
  }
}

public class ProgressBar extends UiElement {

  int max = 1;
  int current = 0;
  int backColor = color(100, 100, 100);
  int frontColor = color(50, 255, 50);
  int w = 500;
  int h = 100;

  public ProgressBar(int inx, int iny, int inw, int inh) {

    x=inx;
    y=iny;
    w=inw;
    h=inh;
  }

  public void setMax(int newMax) {
    max=newMax;
  }
  public void next() {
    current++;
    current = constrain(current, 0, max);
  }
  public void iterate(int amount) {
    current+=amount;
    current = constrain(current, 0, max);
  }
  public void finish() {
    current=max;
  }
  public void reset() {
    current = 0;
  }

  public void setBackColor(int in) {
    backColor=in;
  }
  public void setFrontColor(int in) {
    frontColor=in;
  }
  //save
  public void d() {
    if (show==false)return;
    float prog = ((float)((float)w/max)*(current));
    fill(backColor);
    rect(x, y, w, h);
    fill(frontColor);
    rect(x+2, y+2, prog-2, h-4);
    tick();
  }
  public void tick() {
  }
  public void update() {
    d();
  }

  public boolean complete() {
    return (current==max);
  }
}

private static boolean typeable(int c) {
  return
    (c > 47 && c < 58)   || // number keys
    c == 32 || c == 13   || // spacebar & return key(s) (if you want to allow carriage returns)
    (c > 64 && c < 91)   || // letter keys
    (c > 95 && c < 112)  || // numpad keys
    (c > 185 && c < 193) || // ;=,-./` (in order)
    (c > 218 && c < 223)||   // [\]' (in order)
    (c==189||c==186||c==190||c==188); //other specific things for processing
}



public void keyboardInput() {
  activeEntry.input(key);
}

public void setupUI() {

  activeEntry.initialized=false;
  elements = new ArrayList(1);
}
public void updateUI() {

  for (int i = 0; i<elements.size(); i++) {
    elements.get(i).d();
  }
}
//
int gravity = 1;
final float jumpRotVel = 60;
final int WORLD_DIMENTION = 500;
final int BLOCK_DIMENTION = 50;
final int PLAYER_COLOR = color(50,255,50,235);
boolean inAir = false;
int velx = 10;
int vely = 0;
float rot = 0;
float rotvel = 0;
float bounceFactor = .5f;
SoundFile currentSong;

public void drawChunk(int type, int x, int y){

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

    line(BLOCK_DIMENTION/2,BLOCK_DIMENTION,BLOCK_DIMENTION/2,BLOCK_DIMENTION*(2.0f/3));
    float r = random(5,BLOCK_DIMENTION/3);
    ellipse(BLOCK_DIMENTION/2,BLOCK_DIMENTION*(2.0f/3),r,r);

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

public void die(){

  velx=0;
  deathParticles();
  pstate = PlayerState.SQUARE;
  music[worldNumber].stop();
  death.play();

}

public void drawPlayer(){

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

  public void physics(){

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

  public void d(){

    if(bounceFactor < 1){
      bounceFactor*=1.051f;
    }else{
      bounceFactor*=.95f;
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

public void rolledOver(int n){

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
final int PARTICLE_DIM = 10;

ArrayList<Particle> particles = new ArrayList<Particle>();
ArrayList<Particle> deadParts = new ArrayList<Particle>();
public void moveParticles(){

  for(int i = 0; i < 5;i++){

    Particle p = new Particle(world.x+width/2+25+(int)random(-1,1),world.y+height/2+(25*(gravity+1))+(int)random(-1,1));
    p.velx = -12+random(-1,1);
    p.vely = 1+random(-1,1);
    p.life = 100;
    p.c = color(random(0,255),random(0,255),random(0,255));


  }


}

public void planeParticles(){

    Particle p = new Particle(world.x+width/2+25+(int)random(-1,1),world.y+height/2+(25*gravity)+(int)random(-1,1));
    p.velx = -12+random(-1,1);
    p.vely = 1+random(-1,1);
    p.life = 100;
    p.c = color(random(0,255),random(0,255),random(0,255));
  
}


public void deathParticles(){

  for(int i = 0 ; i < 150;i++){

    Particle p = new Particle(world.x+width/2+(int)random(-10,10),world.y+height/2+(int)random(-10,10));
    p.velx = 10+random(-5,5);
    p.vely = random(-50,5);
    p.life = 100;
    p.c = PLAYER_COLOR;

  }


}


class Particle{


  int x = 0;
  int y = 0;
  float velx = 0;
  float vely = 0;
  float rot = 0;
  float rotvel = radians(random(-5,5));
  int c = color(255,255,255,200);
  int life = 0;

  Particle(int x, int y){

    this.x=x;
    this.y=y;
    particles.add(this);
  }

  public void tick(){

    x+=velx;

    y+=vely;

    rot+=rotvel;
    life++;
    vely++;
    if (life>254){
      life = 255;
      deadParts.add(this);
    }

    c = color(red(c),green(c),blue(c),255-life);

  }

  public void d(){

    pushMatrix();
    translate(x-world.x,y-world.y);
    rotate(rot);
    noStroke();
    fill(c);
    rect(-PARTICLE_DIM/2,-PARTICLE_DIM/2,PARTICLE_DIM,PARTICLE_DIM);
    popMatrix();


  }


}
public void handleParticles(){

  for(Particle p : particles){

    p.tick();
    p.d();

  }
  for(Particle p : deadParts){

    particles.remove(p);

  }

  deadParts.clear();


}
  public void settings() {  size(1920,1080,P2D);  smooth(8); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "TrigonometryDash" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
