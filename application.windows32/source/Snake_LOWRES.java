import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Snake_LOWRES extends PApplet {

/* Nedenunder konstrueres en ArrayListe som indeholder info omkring
   x-koordinater og y-koordinater                                   */
ArrayList<Integer> x = new ArrayList<Integer>(), y = new ArrayList<Integer>();

ArrayList<Integer> obstaclex = new ArrayList<Integer>(), obstacley = new ArrayList<Integer>();

/* w = downscaled width og h = downscaled height mens bs = blocksize
   Alts\u00e5 er blocksizen ogs\u00e5 st\u00f8rrelsen p\u00e5 slangens hoved            */
int w = 30, h = 32, bs = 20, direction = 2, count = 0, level = 1, levelStore = 1, fruitx, fruity, fruitCount = 0, fruitCount2 = 0, fruitNeeded = 5, exitx = 41, exity = 41, keyCount = 0;

/* definere de forskellige retninger i en array 
   Trykker man f.eks. 'd' vil man rykke sig +1 
   ud af x-aksen og trykker man a rykker man sig -1 
   samme princip foreg\u00e5r for y. Dvs. at disse definere 
   de forskellige retninger                         */
int[] dx = {0,0,1,-1}, dy = {1,-1,0,0};
int gamestate = 0;
int pauseState = 2;
boolean gameover = false;
public void setup ()
{
  /* size(w*bs, h*bs); kan ikke godtages af processing da st\u00f8rrelsen
     af programmet ikke kan afh\u00e6nge af en variabel men skal v\u00e6re konstant
     derfor har jeg skrevet hvad 30*20 vil give som width og height */
  
  x.add(w/4);
  y.add(h/2);
  fruitx = (int)random(0,w);
  fruity = (int)random(3,h);
}
public void lvl(int level)
{
  switch(level)
  {
    case 1:
      for(int i = 0; i < 7; i++)
      {
        obstaclex.add((int)random(0,w));
        obstacley.add((int)random(3,h));
      }    
      break;
    case 2:
      for(int i = 0; i < 14; i++)
      {
        obstaclex.add((int)random(0,w));
        obstacley.add((int)random(3,h));
      }
      break;
    case 3:
      for(int i = 0; i < 21; i++)
      {
        obstaclex.add((int)random(0,w));
        obstacley.add((int)random(3,h));
      }
      break;
    default:;
  }
}
public void draw()
{
  background(0);
  for(int i = 0; i < x.size(); i++)
  {
    fill(0,255,0);
    rect(x.get(i)*bs, y.get(i)*bs, bs, bs);     
  }
  
  lvl(level);
  level = 0;
  
  for(int i = 0; i < obstaclex.size(); i++)
  {
    fill(255);
    rect(obstaclex.get(i)*bs,obstacley.get(i)*bs,bs,bs);
  }
  fill(255);
  rect(0,0,width,bs*2);
  fill(0);
  textSize(30);
  textAlign(CENTER);
  text("Level:           Fruits:        Needed:", width/2, height/25);
  text(levelStore,width/3.5f,height/25);
  text(fruitCount2,width/1.65f,height/25);
  text(fruitNeeded,width/1.05f,height/25);
  
  if(!gameover && levelStore != 4)
  {   
    fill(255,0,0);
    rect(fruitx*bs,fruity*bs,bs,bs);
    
    fill(0,0,255);
    rect(exitx*bs,exity*bs,bs,bs);
    
    if(key == ' ')
    {
      gamestate = 1;
    }
    else if(key == 'p')
    {
      gamestate = 0;
      pauseState = direction;
    }
    
    if(frameCount%5==0 && gamestate == 1)
    {
      x.add(0,x.get(0)+dx[direction]);
      y.add(0,y.get(0)+dy[direction]);
      
      /* linje 114-132 er "gamelogikken" om s\u00e5 at sige. Her beskrives de ting,
         som man d\u00f8r af i spillet. Du kan d\u00f8 af og ramme kanterne af banen, bide
         sig selv eller ramme en af de hvide forhindringer. g\u00f8r man en af disse
         ting blive boolean "gameover" lig med true */
      if(x.get(0) < 0 || y.get(0) < 2 || x.get(0) >= w || y.get(0) >= h) gameover = true;
      for(int i = 1; i < x.size(); i++)
      {
        if(x.get(0) == x.get(i) && y.get(0) == y.get(i)) gameover = true;
      }
      for(int i = 0; i < obstaclex.size(); i++)
      {
        if(x.get(0) == obstaclex.get(i) && y.get(0) == obstacley.get(i)) gameover = true;
        if(fruitx == obstaclex.get(i) && fruity == obstacley.get(i))
        {
          fruitx = (int)random(0,w);
          fruity = (int)random(2,h); 
        }
        if(exitx == obstaclex.get(i) && exity == obstacley.get(i))
        {
          exitx = (int)random(0,w);
          exity = (int)random(0,h);
        }
      }
      
      count++;
      
      if(x.get(0) == fruitx && y.get(0) == fruity && fruitCount != fruitNeeded-1)
      {
        fruitx = (int)random(0,w);
        fruity = (int)random(2,h);
        fruitCount++;
        fruitCount2++;
      }
      else if(x.get(0) == fruitx && y.get(0) == fruity && fruitCount == fruitNeeded-1)
      {
        fruitx = 50;
        fruity = 50;
        fruitCount++;
        fruitCount2++;
      }
      else if(x.get(0) == exitx && y.get(0) == exity)
      {        
        x.clear();
        y.clear();
        x.add(w/4);
        y.add(h/2);
        obstaclex.clear();
        obstacley.clear();
        fruitx = (int)random(0,w);
        fruity = (int)random(2,h);
        levelStore++;
        level = levelStore;
        fruitCount = 0;
        fruitCount2 = 0;
        exitx = 41;
        exity = 41;
        direction = 2;
        gamestate = 0;
        fruitNeeded *= 2;        
      }
      else if(count < 20)
      {
        x.remove(x.size()-1);
        y.remove(y.size()-1);
      }
      else
      {
        count = 0; 
      }
      
      if(fruitCount == fruitNeeded)
      {
        fill(0,0,255);
        exitx = (int)random(0,w);
        exity = (int)random(2,h);
        fruitx = 50;
        fruity = 50;
        fill(255,0,0);
        rect(fruitx*bs,fruity*bs,bs,bs);
        fruitCount = 0;
      }     
      keyCount = 0;
    }
    
    if(gamestate == 0)
    {
      fill(255);
      textSize(30);
      textAlign(CENTER);
      text("Game is paused press SPACE to continue",width/2,height/6);
    }    
  }
  else if(levelStore == 4)
  {
    x.clear();
    y.clear();
    fill(255);
    textSize(25);
    textAlign(CENTER);
    text("Congratz you won the game!",width/2,height/2);
    text("press SPACE to start new game", width/2, height/1.8f);
    if(keyPressed&&key==' ')
    {
      reset();
    }
  }
  else
  {
    fill(255);
    textSize(25);
    textAlign(CENTER);
    text("GAME OVER YOU LOST, press SPACE to start over",width/2,height/2);
    if(keyPressed&&key==' ')
    {
      reset();
    }
  }      
}

public void reset()
{
  x.clear();
  y.clear();
  x.add(w/4);
  y.add(h/2);
  gameover = false;
  gamestate = 0;
  direction = 2;
  fruitCount = 0;
  fruitCount2 = 0;
  fruitNeeded = 5;
  fruitx = (int)random(0,w);
  fruity = (int)random(2,h);
  exitx = 41;
  exity = 41;
  level = 1;
  levelStore = 1;
  obstaclex.clear();
  obstacley.clear();
}
public void keyPressed()
{
  // Bev\u00e6gelse
  /* Slangen starter mod h\u00f8jre og fra midten */
  int newdir = 3;
  /*   w
     a   d
       s    */
  if(key == CODED)
  {
    switch(keyCode)
    {
      case DOWN:
        if(keyCount < 1)
        {
          if(direction != 1)
          {
          newdir = 0;
          }
          else 
          {
            newdir = 1;
          }
          keyCount = 1;
        }
      break;
      case UP: 
        if(keyCount < 1)
        {
          if(direction != 0)
          {
            newdir = 1;
          }
          else 
          {
            newdir = 0;
          }
          keyCount = 1;
        }
      break;
      case RIGHT: 
        if(keyCount < 1)
        {  
          if(direction != 3)
          {
            newdir = 2;
          }
          else 
          {
            newdir = 3;
          }
          keyCount = 1;
        }
      break;
      case LEFT:
        if(keyCount < 1)
        {
          if(direction != 2)
          {
            newdir = 3;
          }
          else 
          {
            newdir = 2;
          }
          keyCount = 1;
        }
      break;
      default: newdir = -1;
    }
  }
  else
  { 
    switch(key)
    {
      case 's':
        if(keyCount < 1)
        {
          if(direction != 1)
          {
          newdir = 0;
          }
          else 
          {
            newdir = 1;
          }
          keyCount = 1;
        }
      break;
      case 'w': 
        if(keyCount < 1)
        {
          if(direction != 0)
          {
            newdir = 1;
          }
          else 
          {
            newdir = 0;
          }
          keyCount = 1;
        }
      break;
      case 'd': 
        if(keyCount < 1)
        {  
          if(direction != 3)
          {
            newdir = 2;
          }
          else 
          {
            newdir = 3;
          }
          keyCount = 1;
        }
      break;
      case 'a':
        if(keyCount < 1)
        {
          if(direction != 2)
          {
            newdir = 3;
          }
          else 
          {
            newdir = 2;
          }
          keyCount = 1;
        }
      break;
      default: newdir = -1;
    }
  }
  if(gamestate == 0)
  {
    direction = pauseState;
  }
  else if (newdir != -1) direction = newdir;      
}
  public void settings() {  size(600, 640); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Snake_LOWRES" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
