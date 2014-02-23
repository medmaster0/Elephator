package com.example.elephator;

import android.content.res.Resources;
import android.graphics.Canvas;
import com.example.elephator.Egg;

public class Level {

	public Platform[] platforms;
	public int numPlats = 6;
	public int width, height; //width and height of the screen
	Egg egg = null;
	
	int upcounter;
	int downcounter;
	
	/**
	 * 
	 * @param width of screen
	 * @param height of screen
	 */
	public Level(Resources r, int width, int height){
		
		this.width = width;
		this.height = height;
		int y;
		
		/*Generate Platforms*/
		platforms = new Platform[numPlats ];
		for(int i = 0; i < numPlats ; i++ ){
			//calculate initial y pos of platforms*/
			y = (i+1)*(int)((float)(height) /(float)numPlats ); 
			platforms[i] = new Platform(r, 0, y, 20, (int)(width*0.7));
			/*Skew the platforms*/
			if(i%2 == 0) //if is even
			{
				platforms[i].x = (width - platforms[i].length); //move it all the way to the right
			}
			
		}
		
	}
	
	/**
	 * 
	 * @param width of screen
	 * @param height of screen
	 */
	public Level(Resources r, int width, int height, int numPlats){
		
		this.width = width;
		this.height = height;
		int y;
		this.numPlats = numPlats;
		
		/*Generate Platforms*/
		platforms = new Platform[numPlats ];
		for(int i = 0; i < numPlats ; i++ ){
			//calculate initial y pos of platforms*/
			y = (i+1)*(int)((float)(height) /(float)numPlats ); 
			platforms[i] = new Platform(r, 0, y, 20, (int)(width*0.7));
			/*Skew the platforms*/
			if(i%2 == 0) //if is even
			{
				platforms[i].x = (width - platforms[i].length); //move it all the way to the right
			}
			
		}
		
	}
	
	public void draw(Canvas canvas){
		if(egg!=null)egg.draw(canvas);
		for(Platform plat: platforms){
			plat.drawPlatform(canvas);
			plat.tickTock();
		}
		
	}
	
	/**
	 * Causes the entire level
	 * to shift a unit down
	 */
	public void shiftDown() {
		if(egg != null){
			++egg.y;
			if(egg.y>height)egg.y = 0;
		}
		for(Platform plat: platforms){
			plat.y = ++plat.y;
			if(plat.y > height){
				plat.y = 0;
				downcounter++;
			}
			
		}
	}
	
	/**
	 * Causes the entire level
	 * to shift a unit down
	 */
	public void shiftDown(int y) {
		if(egg != null){
			egg.y = egg.y + y;
			if(egg.y>height)egg.y = 0;
		}
		for(Platform plat: platforms){
			plat.y = y+plat.y;
			if(plat.y > height){
				plat.y = 0;
				downcounter++;
			}
			
		}
	}
	
	/**
	 * Causes the entire level
	 * to shift a unit up
	 */
	public void shiftUp(Resources r) {
		if(egg != null){
			--egg.y;
			if(egg.y<0)egg = null; //destroy egg if it goeas over the top
		}
		for(Platform plat: platforms){
			plat.y = --plat.y;
			if(plat.y < 0){
				upcounter++;
				plat.y = height;
				if(upcounter%5 == 4){
					if(egg==null)egg = new Egg(r,plat.x,plat.y-egg.r);
				}
			}
			
		}
		
	}
	
	/**
	 * Causes the entire level
	 * to shift a unit up
	 */
	public void shiftUp(int y, Resources r) {
		if(egg != null){
			egg.y = egg.y - y;
			if(egg.y<0)egg = null; //destroy egg if it goeas over the top
		}
		for(Platform plat: platforms){
			plat.y = plat.y-y;
			if(plat.y < 0){
				plat.y = height;
				upcounter++;
				if(upcounter%5 == 4){ 
					if(egg==null)egg = new Egg(r, plat.x, plat.y-egg.r);
				}
			}
		}
	}
}
