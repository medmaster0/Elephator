package com.example.elephator;

/*TODO
 * 
 * 
 * 
 * 
 */


import java.util.ArrayList;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Creature {
	/*take care of animation*/
	protected int bmpRows = 2;
	protected int bmpCols = 4;
	protected int currentFrame = 1; //collumn in sprite sheet
	private int currentDir = 0; //row in sprite sheet
	
	private int count = 0; //for animation speed (very hacky)
	/*general fields*/
	public int width; //width of individual sprite (frame)
	public int height; //height of individual sprite (frame)
	public float r = 48;  //should match height and width
	public float x = 100; //center
	public float y = 100; //center
	public float oldy = 0;
	public float vx = 4;
	public float vy = 4;
	public boolean isLanded = false;
	private float gravity = (float)0.4;
	protected Bitmap bmp;
	public int prim, seco;
	public int oldp, olds; //keeps track of the creature's color's
	
	public ArrayList<Item> items = new ArrayList<Item>();
	float offsetx; //take care of drawing flag right
	float offsety; //take care of drawing flag right
	 
	//Dimensions of outside view
	public int screenWidth = 540;
	public int screenHeight = 850;

	//This constructor was made for a 40x840 screen
	public Creature(Resources r){
		/*Always Add the opt so we get a mutable bitmap that wwe can scale*/
		BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        opt.inScaled = false;
		bmp = BitmapFactory.decodeResource(r, R.drawable.elephantbiped, opt);
		this.oldp = Color.BLACK;
		this.olds = Color.WHITE;
		this.width = bmp.getWidth() / bmpCols;
		this.height = bmp.getHeight() / bmpRows;
		this.vx = -5 + (int)(Math.random()*10); //random number between -5 and 5
		this.vy = -5 + (int)(Math.random()*10); //random number between -5 and 5
		this.x = 100 + (int)(Math.random()*100);
		this.y = 100 + (int)(Math.random()*100);
		
		this.offsetx = (float) (width * 0.75); //with 540x860 it equals 24
		this.offsety = height/2; //with 54x850 it equals 16
		
		randomizeBmp();
	}
	
	/**Constructor that will create creature aware of screen dimensions
	 * for size and movement
	 */
	public Creature(Resources r, int width, int height){
		/*Always Add the opt so we get a mutable bitmap that wwe can scale*/
		BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        opt.inScaled = false;
		bmp = BitmapFactory.decodeResource(r, R.drawable.elephantbiped, opt);
		this.oldp = Color.BLACK;
		this.olds = Color.WHITE;
		this.width = bmp.getWidth() / bmpCols;
		this.height = bmp.getHeight() / bmpRows;
		this.x = 100 + (int)(Math.random()*100);
		this.y = 100 + (int)(Math.random()*100);
		
		this.screenHeight = height;
		this.screenWidth = width;
		
		/*
		 * We calculated these ratios by using a 540x850 screen
		 * with a target of getting the same ratio with the values in 
		 * the field definitions (before constructor)
		 * 
		 */
		this.r = (float) (height/17.708); //magic ratio		
		//Again a magic ratio
		//Random number between negative ratio to positive ratio
		this.vy = (float) ((-screenHeight/170.0) + (Math.random()*((2.0 * screenHeight)/170.0))); 
		this.vx = (float) ((-screenWidth/108.0) + (Math.random()*((2.0 * screenWidth)/108.0))); 
		this.gravity = (float)(screenHeight/2125.0);
		
		this.offsetx = (float) (screenWidth/22.5);
		this.offsety = (float) (screenHeight/53.125);

		
		randomizeBmp();
		
	}

	public float getxPos() {
		return x;
	}

	public void setxPos(float xPos) {
		this.x = xPos;
	}

	public float getyPos() {
		return y;
	}

	public void setyPos(float yPos) {
		this.y = yPos;
	}

	//Update the animation of the creature
	public void update(){
		if(count > 15){
		currentFrame = (++currentFrame % bmpCols); //iterate to next frame
		//Take care of item animation
		if(items != null){
			for(Item i: items){
				i.currentFrame = (++i.currentFrame % i.bmpCols);
			}
		}
		
		count = 0;
		}else{
			count++;
		}
		if(vx > 0){
			currentDir = 0;
		}else{
			currentDir = 1;
		}
	}

	public void draw(Canvas canvas){
		
		/*draw items*/

		if(items != null){
			for(Item i: items){
				if(currentDir == 0){
					i.draw(canvas, this.x - offsetx, this.y - offsety, this.currentDir);
				}else{
					i.draw(canvas, this.x + offsetx, this.y - offsety, this.currentDir);
				}
			}
		}
		
		int srcX = currentFrame * width;
		int srcY = currentDir * height;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect((int)(x),(int)(y), 
				(int)(x + (1.5 * r)), (int)(y + (1.5 * r)));
				//the scalar coeffeiciet of width and height SCALE the sprite by that much
		canvas.drawBitmap(bmp, src, dst, null);

	}
	
	public void draw(Canvas canvas, float scale){
		
		int srcX = currentFrame * width;
		int srcY = currentDir * height;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect((int)(x),(int)(y), 
				(int)(x + (scale * r)), (int)(y + (scale * r)));
				//the scalar coeffeiciet of width and height SCALE the sprite by that much
		canvas.drawBitmap(bmp, src, dst, null);
		/*draw items*/
		if(items != null){
			for(Item i: items){
				i.draw(canvas, this.x, this.y, this.currentDir);
			}
		}
	}

	//todo: maybe add a class containing this function
	public void randomizeBmp(){
		
		/*Make some random colors*/
		Random rnd = new Random();
		prim = Color.argb(255, rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256));
		seco = Color.argb(255, rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256));
		
		/*We don't want the skin and dooRag to be the same*/
		while(prim == seco) {
			prim = Color.argb(255, rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256));
		}
		
		/*Setup pixel buffer*/
		int[] pixels = new int[this.bmp.getHeight()*this.bmp.getWidth()];
		this.bmp.getPixels(pixels, 0, this.bmp.getWidth(), 0, 0, this.bmp.getWidth(), this.bmp.getHeight());
			
		/*Go through each pixel and change accordingly*/
		for (int i=0; i<pixels.length; i++){
			if(pixels[i] == this.oldp){
				pixels[i] = prim;
			} else if (pixels[i] == this.olds){
				pixels[i] = seco;
			}
		    
		}
		this.bmp.setPixels(pixels, 0, this.bmp.getWidth(), 0, 0, this.bmp.getWidth(), 
				this.bmp.getHeight());
		this.olds = seco;
		this.oldp = prim;
	
	}
	
	//todo: maybe add a class containing this function
	public void setColors(int p, int s){
		prim = p;
		seco = s;
		
		/*Setup pixel buffer*/
		int[] pixels = new int[this.bmp.getHeight()*this.bmp.getWidth()];
		this.bmp.getPixels(pixels, 0, this.bmp.getWidth(), 0, 0, this.bmp.getWidth(), this.bmp.getHeight());
			
		/*Go through each pixel and change accordingly*/
		for (int i=0; i<pixels.length; i++){
			if(pixels[i] == this.oldp){
				pixels[i] = prim;
			} else if (pixels[i] == this.olds){
				pixels[i] = seco;
			}
		    
		}
		this.bmp.setPixels(pixels, 0, this.bmp.getWidth(), 0, 0, this.bmp.getWidth(), 
				this.bmp.getHeight());
		this.olds = seco;
		this.oldp = prim;
	
	}
	
	public Bitmap getBmp() {
		return bmp;
	}

	public void setBmp(Bitmap bmp) {
		this.bmp = bmp;
	}
	
	public int getCurrentDir() {
		return currentDir;
	}

	public void setCurrentDir(int currentDir) {
		this.currentDir = currentDir;
	}

	public float getR() {
		return r;
	}

	public void setR(float r) {
		this.r = r;
	}
	
	/**Scales the image with the nearest neighbor filter:
	 * Very pixelated block output
	 * 
	 * @param w :the width of new image in pixels
	 * @param h: the height of the new image in   pixels
	 */
	public static void nearestNeighbor(Bitmap img, int w, int h){
		/*Setup pixel buffer*/
		int[] pixels = new int[img.getHeight()*img.getWidth()];
		img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
		
		int[] out = new int[w * h];
		
		float x_ratio = (float)img.getWidth() / (float)w;
	    float y_ratio = (float)img.getHeight() / (float)h;
	    float px, py;
	    float i, j;
	    for ( i =0 ;i<h;i++) {
	      for ( j = 0;j<w;j++) {
	        px = (float)Math.floor(j * x_ratio);
	        py = (float)Math.floor(i * y_ratio);
	        out[(int)(i*w)+(int)j] = pixels[(int)(Math.floor((py*img.getWidth())+px))] ;
	      }
	    }
	    img.setPixels(out, 0, w, 0, 0, w, h);
	    
	 }
	
	public void move(int width, int height){
		//gravity effects vy
		if(isLanded == false) vy = vy + gravity; 
		
		//set limits for terminal velocity
		if(vy > 10)vy = 10;
	    if(vy < -20)vy = -20;
		
	    y = y + vy;
		x = x + vx;
		if((x+r)>width|(x<0)){
			vx = -vx; //bounce off walls
		}
		
	}
	
	public void giveItem(Bitmap bmp){
		items.add(new Item(bmp, screenWidth, screenHeight));
	}
	
	public void jump(){
		vy = -8;
	}
	
	public void randomizeVx(){
		//See second constructor thataccepts width and height for explantion of calculations
		this.vx = (float) ((-screenWidth/108.0) + (Math.random()*((2.0 * screenWidth)/108.0))); 
	}
	

}
