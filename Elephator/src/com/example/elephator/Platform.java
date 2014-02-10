package com.example.elephator;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Platform {

	public int x,y; //posiiton on screen
	public int width;
	public int height;
	
	int thickness = 20;
	int length = 360;
	
	public Bitmap bmp;
	
	/*take care of animation*/
	private int bmpRows = 1;
	private int bmpCols = 2;
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
	
	
	//iterate the animation
	public void tickTock(){
		if(count > 30){
			currentFrame = (++currentFrame % bmpCols); //iterate to next frame
			count = 0;
		}else{
			count++;
		}
	}
	
}
