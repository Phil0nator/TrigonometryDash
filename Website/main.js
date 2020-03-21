/////////////////////////
//@author: Philo Kaulkin - https://github.com/Phil0nator
//
//
//
//
////////////////////////
//dependcies:
import * as THREE from "/three/three.module.js";
import * as LOADER from "/three/GLTFLoader.js";
'use strict';
//constants:

var width = 1879;
var height = 939;
var world_dim = 1000;
var keys = [1024];
let world_object;
let world;
var blockDim = width/38.4;
var blocktypecount = 7;
var world_start_offset_y=980;
var world_start_offset_x=-blockDim;
let playerObject;
var gravity = 1;
var mousePressed = false;


var objects = [];
var scene = new THREE.Scene();
var fog = 100;
scene.fog = new THREE.Fog( 0xffffff, fog, fog + 1000 );
var Amblight = new THREE.AmbientLight( 0x404040 ); // soft white light
scene.add( Amblight );
var camera = new THREE.PerspectiveCamera( 65, width / height, 1, 10000 );
var renderer = new THREE.WebGLRenderer();
renderer.setPixelRatio(window.devicePixelRatio);
renderer.setSize( 1879, 939 );
const canvas = renderer.domElement;
//camera.aspect = canvas.clientWidth / canvas.clientHeight;
//camera.updateProjectionMatrix();

var light = new THREE.DirectionalLight( 0xffffff, 1 );
light.position.set( 0, 50, 0 );
scene.add(light);
var light2 = new THREE.DirectionalLight( 0xffffff, 1 );
light2.position.set( 50, 50, 50 );
scene.add(light2);
var light3 = new THREE.DirectionalLight(0xffAAff ,1);
light3.position.set(1,5,10);
scene.add(light3);
var helper = new THREE.DirectionalLightHelper( light3, 5 );
//scene.add( helper );


camera.position.y = 20;
camera.position.z=50;
camera.lookAt (new THREE.Vector3(0,0,0));

renderer.setSize( window.innerWidth, window.innerHeight );
document.body.appendChild( renderer.domElement );

var gridXZ = new THREE.GridHelper(100, 10,new THREE.Color(0xff0000), new THREE.Color(0xffffff));
gridXZ.position.set(0,10,0);
//scene.add(gridXZ);
//objects.push(gridXZ);


renderer.outputEncoding = THREE.sRGBEncoding;


var loader = new LOADER.GLTFLoader();


//standard geometries:
var sgs = new Array(0);
sgs.length=0;
sgs.push(null);
loader.load(
    // resource URL
    'assets/blocks/p.glb',
    // called when the resource is loaded
    function ( gltf ) {
        
        //sgs.push(gltf.scene);
        playerObject=gltf.scene;
        scene.add(gltf.scene);
        
        
        //playerObject.castShadow=true;
        
        
    },
    function ( xhr ) {

        console.log( ( xhr.loaded / xhr.total * 100 ) + '% loaded' );

    },
    // called when loading has errors
    function ( error ) {

        console.log( 'An error happened: '+error );

    }
);

for(var i = 1; i <= blocktypecount;i++){
    
    loader.load(
        // resource URL
        'assets/blocks/'+i+'.glb',
        // called when the resource is loaded
        function ( gltf ) {
            
            
            sgs.push(gltf.scene);
            
            //scene.add(gltf.scene);
        },
        function ( xhr ) {
    
            console.log( ( xhr.loaded / xhr.total * 100 ) + '% loaded' );
    
        },
        // called when loading has errors
        function ( error ) {
    
            console.log( 'An error happened: '+error );
    
        }
    );
    
}

//world data
let worlds = [0];
let imageLoad = false;
var loadImage = function (url) {
    var img = new Image();
    img.src = url
    img.onload = function () {
      var canvas = document.createElement('canvas');
      canvas.width=500;
      canvas.height=500; 
      canvas.getContext('2d').drawImage(img,0,0);
       
      worlds.push(canvas);
    }
  }
function preload(){
    loadImage(location.href+"/assets/worlds/w1.png");
    loadImage(location.href+"/assets/worlds/w2.png")
}
preload();

class Block{

    constructor(i,j,type){
        this.type=type;
        if(type==0){
            return;
        }
        this.object = sgs[type].clone(true);
        scene.add(this.object);
        this.x=i*2;
        this.y=j*2;
        this.z=0;
        this.object.position.x=world_start_offset_x+this.x;
        this.object.position.y=world_start_offset_y-this.y;
    }
    draw(velx,vely){
        if(this.type==0)return;
        //this.object.position.x=this.x;
        //this.object.position.y=this.y;
        //this.object.position.y;
        this.x+=velx;
        this.y+=vely;
        this.object.position.x+=velx;
        this.object.position.y+=vely;
        this.object.needsUpdate=true;

        
    }

    destroy(){
        scene.remove(this.object);
        delete this.object;
        delete this;
    }

}

function color(r,g,b){
    return new Array(r,g,b,255);
}
function arraysEqual(a, b) {
    for (var i = 0; i < a.length; ++i) {
      if (a[i] !== b[i]) return false;
    }
    return true;
}


function die(){
    world.velx=0;
    
}

class World{

    constructor(num){/**
        *@param path number of world
        */
       console.log(sgs);
        this.num=num;
        if(num==0)return;
        this.x=0;
        this.y=world_start_offset_y;
        var dat = worlds[num];
        this.width=dat.width;
        this.height=dat.height;
        this.data = [];
        this.objectData = [];
        this.inAir=true;
        this.justBounced = false;
        this.idleTime = 0;
        for(var i = 0; i < dat.width;i++){
            this.data.push([]);
            for(var j = 0 ; j < dat.height;j++){
                this.data[i].push(0);
    
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
                // 
                //////////////////////////////
                
                var c = dat.getContext('2d').getImageData(i,j,1,1).data;

                if(arraysEqual(c,color(0,0,0))){
                    this.data[i][j] = 0;
                }else if (arraysEqual(c,color(255,255,255))){
                    this.data[i][j] = 1;
                }else if (arraysEqual(c , color(255,0,0))){
                    this.data[i][j]=2;
                
                }else if (arraysEqual(c , color(255,0,255))){
                    this.data[i][j]=3;
                
                }else if (arraysEqual(c , color(255,255,0))){
                    this.data[i][j] = 4;
                }else if (arraysEqual(c , color(255,1,0))){
                    this.data[i][j] = 5;
                
                }else if (arraysEqual(c , color(140,255,251))){
                    this.data[i][j] = 6;
                
                }else if (arraysEqual(c , color(0,168,243))){
                    this.data[i][j] = 7;
                }else{
                    this.data[i][j]=0;
                }
                /*
                }else if (arraysEqual(c ,color(255,202,24))){
                    this.data[i][j] = 8;
                }else if (arraysEqual(c , color(196,255,14))){
                    this.data[i][j] = 9;
                }else if (arraysEqual(c , color(255,255,1))){
                    this.data[i][j] = 10;
                }else if (arraysEqual(c , color(255,1,255))){
                    this.data[i][j] = 11;
                }
                */
                if(this.data[i][j]!=0)
                this.objectData.push(new Block(i,j,this.data[i][j]));
            }
        }
        

        this.velx=-.27;
        this.vely=0;
        playerObject.position.y=2.3;
        
    }

    empty(){

        for(var i = 0 ; i < this.objectData.length;i++){
            
            this.objectData[i].destroy();
        }

    }
    die(){

        if(this.velx!=0){
            deathParticles();
        }

        this.velx=0;
        this.vely=0;
        playerObject.visible=false;
        playerObject.needsUpdate=true;

        
        
    }

    draw(){
        if(this.num==0)return;
        this.x+=this.velx/2;
        this.y+=this.vely;
        for(var i = 0 ; i < this.objectData.length;i++){
            
            this.objectData[i].draw(this.velx,this.vely);
        }

        if(!this.inAir&&this.velx!=0){
            new Particle(0,0,1);
        }else if (this.velx==0){
            this.idleTime++;
            if(this.idleTime>120){
                clearParticles();
                this.idleTime=0;
            }
        }
    }
    reset(){
        
       
        scene.add(playerObject);

        playerObject.visible=true;
        var nw = new World(this.num);
        this.data=nw.data;
        this.empty();
        this.objectData = nw.objectData;
        this.velx=-.3;
        this.vely=0;
        this.x=0;
        this.y = world_start_offset_y;
        playerObject.needsUpdate=true;

    }
    nextLevel(){
        this.num++;
        this.reset();
    }

    setY(){
        if(this.num==0)return;

    }

    physics(){
        if(this.num==0)return;
        var i =Math.floor((-this.x+24))+1;
        var j = Math.floor((this.y-(playerObject.position.y+4.7)/2)/2+1);
        
        if(i > this.data.length){
            this.next();
        }
        if(j > this.data[0].length || j < 0){
            this.die();
        }

        
        var ct = this.data[i][j];
        var dt = this.data[i][j+(1*gravity)];
        var rt = this.data[i][j];

        if(ct == 2||ct==5){
            this.die();
        }

        else if (ct == 3 || dt == 3 || rt == 3){
            gravity = - gravity;
            this.data[i][j] = 0; //make sure gravity only changes once. Will get reset with rest of map
        }else if (ct == 4 || dt == 4 || rt == 4){
            this.vely = -1.4*gravity;
            this.justBounced=true;
        }


        if(dt!=1){
            this.vely+=.05 * gravity;
            this.inAir=true;
            this.justBounced=false;
            playerObject.rotateY(-.1);
            playerObject.rotateZ(-.1);
            playerObject.rotateX(.1);

        }else{
            if (!this.justBounced){
                this.vely=0;
            }
            if(this.inAir){
                
            }
            this.inAir=false;
            playerObject.lookAt(0,0,0);
            this.setY();
        }

        if(rt == 1){
            this.die();
        }
        
        

    }

}
world = new World(0);





let particles = new Array(0);
var numParticles = 0;
class Particle{

    constructor(x,y,type){
        this.x=x;
        this.y=y+.5;
        this.z=0;
        this.type=type;

        this.life = 0;
        this.maxLife;
        this.velx;
        this.vely;
        this.velz;
        this.object;
        this.mat;
        this.geo;
        this.index;
        this.active=true;

        if(type == 0){ //0=death
            this.velx=rand(-1,1);
            this.vely=rand(-1,1);
            this.velz=rand(-1,1);

            this.geo = new THREE.BoxGeometry( );
            this.mat = new THREE.MeshBasicMaterial( {color: 0xFF5555} );
            this.object = new THREE.Mesh(this.geo, this.mat);
            
            this.maxLife = 120;
        }else if (type==1){//1==normal running
            //this.velx=rand(-1,1);
            this.velx=world.velx+rand(-1,-.5);
            this.vely=rand(-1,0);
            this.velz=rand(-1,1);

            this.geo = new THREE.BoxGeometry( );
            this.mat = new THREE.MeshBasicMaterial( {color: 0xAAAAFF} );
            this.object = new THREE.Mesh(this.geo, this.mat);
            
            this.maxLife = 120;

        }


        this.object.position.x=this.x;
        this.object.position.y=this.y;
        this.object.position.z=this.z;
        this.object.needsUpdate=true;

        scene.add(this.object);
        this.index=particles.length;
        numParticles++;
        particles.push(this);

    }


    destroy(){
        scene.remove(this.object);
        this.object.geometry.dispose();
        this.object.material.dispose();
        renderer.renderLists.dispose();
        //delete this.object;
        //particles.pop();
        this.active=false;
        numParticles--;
    }

    draw(){
        
        if(!this.active){
            this.destroy();
            return 1;
        }
        this.x+=this.velx+world.velx;
        this.y+=this.vely+world.vely;
        this.z+=this.velz;

        
        this.vely-=.01;
        

        this.object.position.x=this.x;
        this.object.position.y=this.y;
        this.object.position.z=this.z;

        this.object.needsUpdate=true;
        this.life++;
        if(this.life>this.maxLife){
            this.destroy();
            return 0;
        }
        return 1;
    }


}
function clearParticles(){
    for(var i = 0 ; i < particles.length;i++){
        scene.remove(particles[i].object);

    }
    particles.length=0;
    numParticles=0;
}

function handleParticles(){
    for(var i = 0 ; i < particles.length;i++){
        if (particles[i].draw() ==0){
            i--;
        }
    }

    if(numParticles==0){
        //clearParticles();
        particles.length=0;
    }
}


function deathParticles(){

    for(var i = 0 ; i < 150;i++){
        new Particle(0,0,0);
    }

}










function keyDown(event){

    keys[event.keyCode] = true;

}

function keyUp(event){
    keys[event.keyCode]=false;
}

function jump(){
    if(!world.inAir){
        world.vely=-.7 * gravity;
    }
}

function mouseD(event){
    mousePressed=true;
}
function mouseU(event){
    mousePressed=false;
}



function keyboard(){
    if(keys[87]||keys[32]||keys[38]){
        jump();
    }   
}
function mouseInput(){
    if(mousePressed){
        jump();
    }
}

function orientationchangehandler(event){
    location.reload()
}


//<Maintain Aspect>
window.addEventListener( 'resize', onWindowResize, false );
window.addEventListener('orientationchange', orientationchangehandler, false);
canvas.onmousedown=mouseD;
canvas.onmouseup = mouseU;
canvas.ontouchstart=mouseD;
canvas.ontouchend=mouseU;

function onWindowResize(){

    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();

    renderer.setSize( window.innerWidth, window.innerHeight );
    width = window.innerWidth;
    height = window.innerHeight;
}
//</Maintain Aspect>
document.addEventListener('keydown', keyDown, false);
document.addEventListener('keyup', keyUp, false);
function disableselect(e) {return false}
document.onselectstart = function() {return false};
document.onmousedown = disableselect

//network ui
var prevINGAME = ingame;


var animate = function () {
    requestAnimationFrame( animate );
    keyboard();
    mouseInput();
    handleParticles();
    world.draw();
    world.physics();
    document.body.scrollTop = document.documentElement.scrollTop = 0;
    if(ingame){
        renderer.render( scene, camera );
        if(prevINGAME==false){
            prevINGAME=true;
            world= new World(1);
            canvas.style.cursor = "none";
        }

        if(world.velx==0&&numParticles==0){
            world.reset();
            clearParticles();
            gravity=1;
            particles.length=0;
            
        }

    }
    prevINGAME = ingame;
};



animate();
