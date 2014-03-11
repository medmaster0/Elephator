package com.example.elephator;

import java.util.Random;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FlyCre {
	
	//Usage Notes
	//Usually, you'll call move, update, and draw from outside view



	/*take care of animation*/
	private int bmpRows = 4;
	private int bmpCols = 2;
	private int currentFrame = 1; //collumn in sprite sheet
	private int currentDir = 0; //row in sprite sheet


	private int count = 0; //for animation speed (very hacky)
	/*general fields*/
	private int width; //width of individual sprite (frame)
	private int height; //height of individual sprite (frame)
	public float r = 48;
	public float x = 100;
	public float y = 100;
	public float oldy = 0;
	public float vx = 4;
	public float vy = 4;
	private float gravity = (float)0.2;
	private Bitmap bmp;
	private int prim, seco;
	private int oldp, olds; //keeps track of the creature's color's

	public FlyCre(Resources r){
		/*Always Add the opt so we get a mutable bitmap that wwe can scale*/
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inMutable = true;
		opt.inScaled = false;
		bmp = BitmapFactory.decodeResource(r, R.drawable.fallguy, opt);
		this.oldp = Color.BLACK;
		this.olds = Color.WHITE;
		this.width = bmp.getWidth() / bmpCols;
		this.height = bmp.getHeight() / bmpRows;
		this.vx = -5 + (int)(Math.random()*10); //random number between -5 and 5
		this.vy = -5 + (int)(Math.random()*10); //random number between -5 and 5
		this.x = 100 + (int)(Math.random()*100);
		this.y = 100 + (int)(Math.random()*100);
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
		if(count > 30){
			currentFrame = (++currentFrame % bmpCols); //iterate to next frame
			count = 0;
		}else{
			count++;
		}
		if(vx > 0){
			currentDir = 1;
		}else{
			currentDir = 0;
		}
	}

	public void draw(Canvas canvas){

		int srcX = currentFrame * width;
		int srcY = currentDir * height;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect((int)(x),(int)(y), 
				(int)(x + (2 * width)), (int)(y + (2 * height)));
		//the scalar coeffeiciet of width and height SCALE the sprite by that much
		canvas.drawBitmap(bmp, src, dst, null);
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

	public void move(int width, int height){

		x = x + vx;
		if((x+r)>width|(x<0)){
			vx = -vx; //bounce off walls
		}

	}




}

	
	
	

