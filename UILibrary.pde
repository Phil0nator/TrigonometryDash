//Library by Philo Kaulkin to create simple UI elements for processing

import java.time.Instant;
import java.util.ArrayList;


//Quick Colors:

public final color WHITE = color(255);
public final color BLACK = color(0);
public final color RED = color(255,0,0);
public final color GREEN = color(0,255,0);
public final color BLUE = color(0,0,255);
public final color PURPLE = color(255,0,255);
public final color YELLOW = color(255,255,0);
public final color GRAY = color(100,100,100);
public final color CYAN = color(0,255,255);


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
  protected color col = 0;
  protected color mouseOverColor = 0;
  protected color mouseDownColor = 0;
  protected PImage TEXTURE;
  protected String text = "";
  protected int state = 0;
  protected int textSize = 0;
  protected color textColor = 0;
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
  color col = color(200, 200, 200);
  color mouseOverColor = color(255, 255, 255);
  color mouseDownColor = color(255, 100, 200);
  String text = "Button";
  int textSize = 25;
  color textColor = color(100, 100, 100);
  boolean holding = false;
  boolean expand = true;
  PImage TEXTURE = null;
  boolean image = false;
  direction dir = null;

  public void setDir(direction newdir) {
    dir = newdir;
  }


  public void setColor(color incol) {
    col=incol;
  }
  public void mouseOverColor(color inmouseOverColor) {
    mouseOverColor = inmouseOverColor;
  }
  public void mouseDownColor( color inmouseDownColor) {
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
  public void text_color(color intextColor) {
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
    text(text, x+(diff/2), y+(h/1.5));
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
  color col = color(200, 200, 200);
  boolean initialized = false;
  color mouseOverColor = color(255, 255, 255);
  color mouseDownColor = color(255, 100, 200);
  String text = "";
  String hintText = "Enter text here...";
  int textSize = 25;
  color textColor = color(100, 100, 100);
  boolean active = false;
  boolean mouseOver = false;
  color textInactiveColor = color(50, 50, 50);
  boolean clicked = false;
  boolean keydown = false;
  long lastCursor = now();
  boolean cursor = false;


  public void setColor(color incol) {
    col=incol;
  }
  public void mouseOverColor(color inmouseOverColor) {
    mouseOverColor = inmouseOverColor;
  }
  public void mouseDownColor( color inmouseDownColor) {
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
  public void text_color(color intextColor) {
    textColor = intextColor;
  }
  public void text_inactiveColor(color in) {
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
        text(text+"|", x+10, y+textSize*1.5);
      } else {
        text(text, x+10, y+textSize*1.5);
      }
    } else {

      fill(textInactiveColor);
      if (text==""&&initialized==true) {
        text(hintText, x+10, y+textSize*1.5);
      } else {
        text(text, x+10, y+textSize*1.5);
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

  void setText(String intext) {
    text=intext;
  }

  void setSize(int insize) {
    textSize=insize;
  }

  void setColor(color inp) {
    col=inp;
  }

  void d() {
    if (show==false)return;

    fill(col);
    textSize(textSize);
    text(text, x, y);
    tick();
  }
  void tick() {
  }
  void update() {
    d();
  }
}


public class Page extends UiElement {
  boolean draggable = true;
  int clickedx;
  int clickedy;
  color col = color(100, 100, 100);
  color mouseOverColor=color(255, 255, 255);
  color mouseDownColor=color(200, 200, 255);
  public void setColor(color incol) {
    col=incol;
  }
  public void mouseOverColor(color inmouseOverColor) {
    mouseOverColor = inmouseOverColor;
  }
  public void mouseDownColor( color inmouseDownColor) {
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

  void add(UiElement element) {

    elements.remove(element);
    Elems.add(element);
  }

  void detatch(UiElement element) {
    elements.add(element);
    Elems.remove(element);
  }

  void destroy(UiElement element) {

    Elems.remove(element);
  }

  void d() {
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

  void tick() {
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
  color backColor = color(100, 100, 100);
  color frontColor = color(50, 255, 50);
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

  public void setBackColor(color in) {
    backColor=in;
  }
  public void setFrontColor(color in) {
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
