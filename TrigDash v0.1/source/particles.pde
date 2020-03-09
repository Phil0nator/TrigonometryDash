final int PARTICLE_DIM = 10;

ArrayList<Particle> particles = new ArrayList<Particle>();
ArrayList<Particle> deadParts = new ArrayList<Particle>();
void moveParticles(){

  for(int i = 0; i < 5;i++){

    Particle p = new Particle(world.x+width/2+25+(int)random(-1,1),world.y+height/2+(25*(gravity+1))+(int)random(-1,1));
    p.velx = -12+random(-1,1);
    p.vely = 1+random(-1,1);
    p.life = 100;
    p.c = color(random(0,255),random(0,255),random(0,255));


  }


}

void deathParticles(){

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
  color c = color(255,255,255,200);
  int life = 0;

  Particle(int x, int y){

    this.x=x;
    this.y=y;
    particles.add(this);
  }

  void tick(){

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

  void d(){

    pushMatrix();
    translate(x-world.x,y-world.y);
    rotate(rot);
    noStroke();
    fill(c);
    rect(-PARTICLE_DIM/2,-PARTICLE_DIM/2,PARTICLE_DIM,PARTICLE_DIM);
    popMatrix();


  }


}
void handleParticles(){

  for(Particle p : particles){

    p.tick();
    p.d();

  }
  for(Particle p : deadParts){

    particles.remove(p);

  }

  deadParts.clear();


}
