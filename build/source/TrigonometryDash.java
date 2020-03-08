import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

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

boolean keys[] = new boolean[1024];
PImage bg;
World world;
enum gameState{

  MAIN_MENU, GAME, PAUSE;

}

gameState State = gameState.MAIN_MENU;

public void setup(){

  
  setupUI();
  configureUI();

  bg = loadImage("world\\bg.png");


  world = new World("world\\w1.png");
  world.y = (BLOCK_DIMENTION*WORLD_DIMENTION) - (20*BLOCK_DIMENTION);

}
public void keyPressed(){

  keys[keyCode] = true;}

public void keyReleased(){

  keys[keyCode] = false;}

public void keyPress(){

  if(keys[87]){

    vely-=5;

  }




}

public void draw(){


  keyPress();

  if(State==gameState.GAME){

    image(bg,-world.x/100,0);
    world.d();
    fill(255,0,255);
    stroke(255,0,255);
    text(world.y,50,50);
  }

  if(State!=gameState.GAME){
    background(0);
    updateUI();
    UIConds();
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
final float jumpRotVel = 1;
final int WORLD_DIMENTION = 500;
final int BLOCK_DIMENTION = 50;

int velx = 5;
int vely = 0;




public void drawChunk(int type, int x, int y){

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

public void drawPlayer(){

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

  public void physics(){

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

  public void d(){
    physics();


    drawPlayer();


    for(int i = 0 ; i < WORLD_DIMENTION;i++){

      for(int j = 0 ; j < WORLD_DIMENTION; j++){

        drawChunk(data[i][j],i,j);

      }
    }

  }


}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "TrigonometryDash" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
