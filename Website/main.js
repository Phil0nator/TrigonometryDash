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


//constants:

var width = window.innerWidth;
var height = window.innerHeight;
var world_dim = 1000;
var keys = [1024];
let world_object;
let world;
var blockDim = width/38.4;
var blocktypecount = 7;
var world_start_offset_y=990;
var world_start_offset_x=-blockDim;
let playerObject;
var gravity = 1;

var objects = [];
var scene = new THREE.Scene();
var fog = 100;
scene.fog = new THREE.Fog( 0xffffff, fog, fog + 1000 );
var Amblight = new THREE.AmbientLight( 0x404040 ); // soft white light
scene.add( Amblight );
var camera = new THREE.PerspectiveCamera( 45, window.innerWidth / window.innerHeight, 1, 10000 );
var renderer = new THREE.WebGLRenderer();
renderer.setSize( window.innerWidth, window.innerHeight );


var light = new THREE.DirectionalLight( 0xffffff, 1 );
light.position.set( 0, 50, 0 );
scene.add(light);
var light2 = new THREE.DirectionalLight( 0xffffff, 1 );
light2.position.set( 50, 50, 50 );
scene.add(light2);
var helper = new THREE.DirectionalLightHelper( light2, 5 );
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
var sgs = [null];
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
    loadImage("/assets/worlds/w1.png");
    loadImage("/assets/worlds/w2.png")
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
        this.num=num;
        if(num==0)return;
        this.x=0;
        this.y=world_start_offset_y;
        var dat = worlds[num];
        this.width=dat.width;
        this.height=dat.height;
        this.data = [];
        this.objectData = [];
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
                this.objectData.push(new Block(i,j,this.data[i][j]));
            }
        }
        

        this.velx=-.1;
        this.vely=0;
        playerObject.position.y=7;
        
    }
    draw(){
        if(this.num==0)return;
        this.x+=this.velx;
        this.y+=this.vely;
        for(var i = 0 ; i < this.objectData.length;i++){
            
            this.objectData[i].draw(this.velx,this.vely);
        }
    }
    reset(){
        for(var i = 0 ; i < this.objectData.length;i++){
            
            this.objectData[i].object.position.x-=this.x;
            this.objectData[i].x-=this.x;

            this.objectData[i].object.position.y-=this.y;
            this.objectData[i].y-=this.y;

        }
    }
    physics(){
        if(this.num==0)return;
        var currentBlock = this.data[0][0];
    }

}
world = new World(0);


function keyDown(event){

    keys[event.keyCode] = true;

}

function keyUp(event){
    keys[event.keyCode]=false;
}

function keyboard(){
    if(keys[87]){
        world.y++;
    }   
    if(keys[83]){
        world.y--;
    }   
    if(keys[68]){
        
    }
    if(keys[65]){
        
    }
}
//<Maintain Aspect>
window.addEventListener( 'resize', onWindowResize, false );

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


//network ui
var prevINGAME = ingame;


var animate = function () {
    requestAnimationFrame( animate );
    keyboard();
    world.draw();
    world.physics();
    document.body.scrollTop = document.documentElement.scrollTop = 0;
    if(ingame){
        renderer.render( scene, camera );
        if(prevINGAME==false){
            world= new World(1);
        }
    }
    prevINGAME = ingame;
};



animate();
