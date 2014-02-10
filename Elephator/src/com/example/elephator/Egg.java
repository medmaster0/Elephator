package com.example.elephator;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Egg extends Product {
	
	public boolean isHatching = false;

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
	
	public void eggDrop(){
		this.vy += this.gravity;
		this.y += this.vy;
		
	}
	
}
