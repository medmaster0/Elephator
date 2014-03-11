package com.example.elephator;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

public class Item {

	/*take care of animation*/
	private int bmpRows = 2;
	private int bmpCols = 1;
	private int currentFrame = 0; //collumn in sprite sheet
	private int currentDir = 0; //row in sprite sheet
	
	public int width; //width of individual sprite (frame)
	public int height; //height of individual sprite (frame)
	public int r = 48;
	public Bitmap bmp;
	public int prim, seco;
	public int oldp, olds; //keeps track of the creature's color's
	
	
	public Item(Bitmap bmp){
		this.bmp = bmp;
		this.oldp = Color.BLACK;
		this.olds = Color.WHITE;
		this.width = bmp.getWidth() / bmpCols;
		this.height = bmp.getHeight() / bmpRows;
		randomizeBmp();
	}
	
	public void draw(Canvas canvas, float x, float y, int currentDir){
		
		int srcX = currentFrame * width;
		int srcY = currentDir * height;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect((int)(x),(int)(y), 
				(int)(x + (1.5 * r)), (int)(y + (1.5 * r)));
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
	
}
