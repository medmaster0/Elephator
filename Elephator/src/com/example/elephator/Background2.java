package com.example.elephator;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Background2 {
	
	public Bitmap bmp; 
	public int width, height;
	public int backY = 0;
	
	public Background2(Resources r, int width, int height){
		//So we can make a mutable (changeable) bitmap
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inMutable = true;
		opt.inScaled = false;
		bmp = BitmapFactory.decodeResource(r, R.drawable.sunsetblvd, opt);
		nearestNeighbor(bmp, width, height);
		
		this.width = width;
		this.height = height;
	}
	
	public void changeBack(Bitmap bmp){
		this.bmp = bmp;
		nearestNeighbor(bmp, width, height);
	}
	
	public void drawBackground(Canvas c){
		//We're going to draw two of the same bitmap on top of each other
		Rect src = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
		Rect dst = new Rect(0, backY, width, backY + height);
		Rect dst2 = new Rect(0, backY - height , width, backY);
		c.drawBitmap(bmp, src, dst, null);
		c.drawBitmap(bmp, src, dst2, null);
		
	}
	
	
	/*Scales the image with the nearest neighbor filter:
	 * Very pixelated block output
	 */
	public void nearestNeighbor(Bitmap img, int w, int h){
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
	    img.setPixels(out, 0, width, 0, 0, width, height);
	    
	 }

}
