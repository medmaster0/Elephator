package com.example.elephator;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Platform {

	public int x,y; //posiiton on screen
	public int width;  //width of bitmap frame
	public int height; //width of bitmap frame
	
	int thickness = 20;
	int length = 360;
	
	public Bitmap bmp;
	
	/*take care of animation*/
	private int bmpRows = 1;
	public int bmpCols = 2;
	public int currentFrame = 0; //collumn in sprite sheet
	private int currentDir = 0; //row in sprite sheet
	
	public int count; //for animation "clock"
	
	/**Constructor that creates a platform 
	 * Default length is 360
	 * Default thickness is 20
	 * DOES NOT SCALE ACROSS PLATFORMS!!!!
	 * 
	 * @param r //just the resources we've been passing along
	 * @param x //x position on screen
	 * @param y //y position on screen
	 */
	public Platform(Resources r, int x, int y){
		
		
		BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        opt.inScaled = false;
		bmp = BitmapFactory.decodeResource(r, R.drawable.platform2, opt);
		
		this.x = x;
		this.y = y;
		this.width = bmp.getWidth() / bmpCols;
		this.height = bmp.getHeight() / bmpRows;
		
	}
	
	/**Constructor that creates a platform 
	 * Default length is 360
	 * Default thickness is 20
	 * DOES NOT SCALE ACROSS PLATFORMS!!!!
	 * 
	 * @param r //just the resources we've been passing along
	 * @param x //x position on screen
	 * @param y //y position on screen
	 * @param bmp //the bmp of the platform
	 */
	public Platform(Resources r, int x, int y, Bitmap bmp){
		
		this.bmp = bmp;
		
		this.x = x;
		this.y = y;
		this.width = bmp.getWidth() / bmpCols;
		this.height = bmp.getHeight() / bmpRows;
		
	}
	
	/**Constructor that creates a platform 
	 *Allows you to specify length and thickess
	 * Good for Scaling :))))
	 * 
	 * @param r //just the resources we've been passing along
	 * @param x //x position on screen
	 * @param y //y position on screen
	 * @param width // width of the screen
	 * @param height // height of the screen 
	 */
	public Platform(Resources r, int x, int y, int thickness, int length){
		
		
		BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        opt.inScaled = false;
		bmp = BitmapFactory.decodeResource(r, R.drawable.platform2, opt);
		
		this.x = x;
		this.y = y;
		this.thickness = thickness; 
		this.length = length;
		this.width = bmp.getWidth() / bmpCols;
		this.height = bmp.getHeight() / bmpRows;
		
	}
	
	public void drawPlatform(Canvas canvas){
		
		int srcX = currentFrame * width;
		int srcY = currentDir * height;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect((int)(x),(int)(y), 
				(int)x + length, (int)y + thickness);
				//the scalar coeffeiciet of width and height SCALE the sprite by that much
		canvas.drawBitmap(bmp, src, dst, null);
		
	}
	
	public void changePlatform(Bitmap bmp, int numCols){
		this.bmp = bmp;
		this.bmpCols = numCols;
		this.width = bmp.getWidth() / bmpCols;
		this.height = bmp.getHeight() / bmpRows;
		
		//nearestNeighbor(this.bmp, this.length*bmpCols, this.thickness*bmpRows);
	}
	
	
	//iterate the animation
	public void tickTock(){
		if(count > 30){
			currentFrame = (++currentFrame % bmpCols); //iterate to next frame
			count = 0;
		}else{
			count++;
		}
	}
	
	
	public void flip(){
		Matrix m = new Matrix();
	    m.preScale(-1, 1);
	    Bitmap src= this.bmp;
	    Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
	    dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
	    this.bmp = dst;
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
	    img.setPixels(out, 0, w, 0, 0, w, h);
	    
	 }
}
