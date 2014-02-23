package com.example.elephator;

import java.util.ArrayList;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.GestureDetector.OnGestureListener;
import android.view.SurfaceView;

@SuppressLint("NewApi")
public class EggdropView extends SurfaceView
	implements UpdatableView, SurfaceHolder.Callback, OnGestureListener{
	
	/*Used for detecting input*/
    GestureDetector gestureScanner;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    long timer; //used to screen taps
    long oldtime = 0;
    
    UpdateThread updateThread;
    
    Background2 back;
    Level level;
    Creature elleph;
    Egg egg = null;
    ArrayList<Egg> launchedEggs;
    ArrayList<Egg> landedEggs;
    
    private int width,height; //width and height of the screen

	public EggdropView(Context context) {
		super(context);
		getHolder().addCallback(this);
		
		gestureScanner = new GestureDetector(this);
	}

	public boolean onTouchEvent(MotionEvent e) {
		timer = System.currentTimeMillis();
		if((timer-oldtime)<500)return gestureScanner.onTouchEvent(e);
		
		if(egg != null){
			launchedEggs.add(egg);
			if(launchedEggs.size()>5)launchedEggs.remove(0);
		}
		egg = new Egg(getResources());
		egg.x = elleph.x - elleph.r/2;
		egg.y = elleph.y + elleph.r/2;
		egg.vx = -5 + (int)(Math.random()*5);
		egg.vy = -5 + (int)(Math.random()*10);
		
		oldtime = timer;
		return gestureScanner.onTouchEvent(e);
	}
	
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Rect surfaceFrame = holder.getSurfaceFrame();
		width = surfaceFrame.width();
		height = surfaceFrame.height();;
		
		/*Initialize*/
		back = new Background2(getResources(), this.width, this.height);
		level = new Level(getResources(), this.width, this.height, 3);
		/*Make the platform really huge/cliff like*/
		BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        opt.inScaled = false;
        Bitmap bp = BitmapFactory.decodeResource(getResources(),R.drawable.cliff, opt);
		level.platforms[1] = new Platform(getResources(),level.platforms[1].x, 
				level.platforms[1].y,bp);
		level.platforms[1].width = bp.getWidth();
		level.platforms[1].bmpCols = 1;
		level.platforms[1].thickness = height - level.platforms[1].y; //stretch thickness to bottom of screen
		
		elleph = new Creature(getResources());
		elleph.x = level.platforms[0].x;
		elleph.y = level.platforms[0].y - elleph.r;
		elleph.setCurrentDir(0);
		
		launchedEggs = new ArrayList<Egg>();
		landedEggs = new ArrayList<Egg>();
		
		updateThread = new UpdateThread(this);
		updateThread.setRunning(true);
		updateThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		updateThread.setRunning(false);
		
	}
	
	public void onDraw(Canvas c){
		back.drawBackground(c);
		elleph.draw(c, 1);
		level.draw(c);
		if(egg!=null)egg.draw(c);
		for(Egg eggy : launchedEggs)eggy.draw(c);
		for(Egg eggy: landedEggs)eggy.draw(c);
	}
	
	public void updatePhysics(){
		elleph.update();
		
//		/*EGG STUFF BELOW*/	
//		for(Egg eggy : launchedEggs){
//			eggy.move(width, height);
//			if(Collision.eggPlatformCollision(eggy, level.platforms[1])){
//				eggy.y=level.platforms[1].y-eggy.r;
//				eggy.stopEgg();
//				landedEggs.add(eggy);
//				if(landedEggs.size()>10)landedEggs.remove(0);
//				HatchRun hatchrun = new HatchRun(eggy);
//				new Thread(hatchrun).start();
//			}
//
//		}
		Iterator<Egg> itr = launchedEggs.listIterator();
		if(!itr.hasNext())return;
		while(itr.hasNext()){
			Egg eggy = itr.next();
			eggy.move(width, height);
			if(Collision.eggPlatformCollision(eggy, level.platforms[1])){
				itr.remove();
				eggy.y = level.platforms[1].y-eggy.r;
				eggy.stopEgg();
				landedEggs.add(eggy);
				if(landedEggs.size()>20)landedEggs.remove(0);
				HatchRun hatchrun = new HatchRun(eggy);
				new Thread(hatchrun).start();
			}
			
		}
		
		
	}
	
	public class HatchRun implements Runnable{
		Egg eggy;
		public HatchRun(Egg egg){
			this.eggy = egg;
		}
		public void run(){
			eggy.hatchFast();
			return;
		}
		
	}
	

}
