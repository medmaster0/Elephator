package com.example.elephator;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Egg extends Product {
	
	public Egg(Resources r){
		
		super(r);
		this.x = 1;
		this.y = 1;
		this.vy = 4; //it starts falling
		this.bmpRows = 1;
		this.bmpCols = 4;
		/*Always Add the opt so we get a mutable bitmap that wwe can scale*/
		BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        opt.inScaled = false;
		bmp = BitmapFactory.decodeResource(r, R.drawable.egg2, opt);
		this.oldp = Color.BLACK;
		this.olds = Color.WHITE;
		this.randomizeBmp();
		
		
		
	}

	public Egg(Resources r,float x, float y){
		
		super(r);
		this.x = x;
		this.y = y;
		this.vy = 4; //it starts falling
		this.bmpRows = 1;
		this.bmpCols = 4;
		/*Always Add the opt so we get a mutable bitmap that wwe can scale*/
		BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        opt.inScaled = false;
		bmp = BitmapFactory.decodeResource(r, R.drawable.egg2, opt);
		this.oldp = Color.BLACK;
		this.olds = Color.WHITE;
		this.randomizeBmp();
		
		
		
	}
	
	public void hatch(){
		while(this.currentFrame < 3){
			long start = System.currentTimeMillis();
			long now;
			while(true){
				now = System.currentTimeMillis();
				if((now - start) > 500){
					this.currentFrame++;
					
					break;
				}
			}
			
		}
	}
	
	/**
	 * Like hatch but takes half as much time to get to next frame
	 * 
	 */
	public void hatchFast(){
		while(this.currentFrame < 3){
			long start = System.currentTimeMillis();
			long now;
			while(true){
				now = System.currentTimeMillis();
				if((now - start) > 50){
					this.currentFrame++;
					
					break;
				}
			}
			
		}
	}
	
	public void eggDrop(){
		this.vy += this.gravity;
		this.y += this.vy;
		
	}
	
	public void stopEgg(){
		this.vy= 0;
		this.vx = 0;
		this.gravity = 0;
	}
	
	public void move(int width, int height){
		//gravity effects vy
		vy = vy + gravity; 
		
		//set limits for terminal velocity
		//if(vy > 10)vy = 10;
	    //if(vy < -10)vy = -10;
		
	    y = y + vy;
		x = x + vx;
		if((x+r)>width|(x<0)){
			vx = -vx; //bounce off walls
		}
		
	}
	
	public void hatchThreadFast(){
		new Thread(new Runnable() {
	        public void run() {
	        	hatchFast();
	        }
	    }).start();
	}
	
}
