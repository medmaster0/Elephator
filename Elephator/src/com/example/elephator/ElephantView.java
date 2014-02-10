package com.example.elephator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.GestureDetector.OnGestureListener;
import android.view.SurfaceView;

public class ElephantView extends SurfaceView 
	implements SurfaceHolder.Callback, OnGestureListener{
	
	/*Used for detecting input*/
    GestureDetector gestureScanner;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	
	UpdateThread updateThread;
	
	Background back;
	Level level;
	Creature elleph;
	
	/*Take care of score*/
    int proScore = 0; //amount of product taken to crate
    int eggScore = 0; //amount of egg taken to crate
    Paint scorePaint;
	
	float followRes;
	
	private int width,height; //width and height of the screen
	
	@SuppressWarnings("deprecation")
	public ElephantView(Context context){
		super(context);
		getHolder().addCallback(this);
		
		 scorePaint = new Paint();
	     scorePaint.setColor(Color.GREEN);
	     scorePaint.setTextSize(14);
		
		gestureScanner = new GestureDetector(this);
	}

	
	@Override 
    public boolean onTouchEvent(MotionEvent e) {
    	
    	/*The Bully system :))))*/
		//llc.setxPos(llc.getxPos() + (e.getRawX() - llc.getxPos())/followRes);
		//llc.setyPos(llc.getyPos() + (e.getRawY() - llc.getyPos())/followRes);
    	if(e.getRawX() > width / 2.0){
    		elleph.vx = Math.abs(elleph.vx);
    	}else{
    		elleph.vx = -Math.abs(elleph.vx);
    	}
    	
    	
		return gestureScanner.onTouchEvent(e);
    }
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && 
                Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
			//From Right to Left
			elleph.randomizeBmp();
			elleph.vx = -5 + (int)(Math.random()*10); //random number between -5 and 5
			return true;
		}  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE &&
                Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
			//From Left to Right
			return true;
		}
  
		if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && 
               Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
			//From Bottom to Top
			return true;
		}  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && 
               Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
			//From Top to Bottom
			return true;
		}
		return false;
		
	}

	
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return true;
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
		followRes = (float) 20.0;
		
		/*Initialize*/
		back = new Background(getResources(), this.width, this.height);
		level = new Level(getResources(), this.width, this.height);
		elleph = new Creature(getResources());
		
		updateThread = new UpdateThread(this);
		updateThread.setRunning(true);
		updateThread.start();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	public void updatePhysics() {
		
		//check to see if elephants have landed on platform
		for(Platform plat: level.platforms){
			if(Collision.creaturePlatformCollision(elleph, plat)){
				elleph.y = plat.y - (float)(1.5*(elleph.r+1));
				elleph.vy = 0;
				elleph.isLanded = true;
			}else{
				elleph.isLanded = false;
			}
		}
		elleph.update();
		elleph.move(width, height);
		
		//check to see if elephant has gotten an egg
		if(level.egg != null){
			if(Collision.creatureProductCollision(elleph, level.egg)){
				eggScore = eggScore + 1;
				level.egg = null;
			}
		}

//		/*Set Camera*/
//		if(elleph.y > width / 2.0){
//			level.shiftUp();
//		}else{
//			level.shiftDown();
//		}
		/*The Bully system :))))*/
		//llc.setyPos(llc.getyPos() + (e.getRawY() - llc.getyPos())/followRes);
		level.shiftUp((int)((elleph.y - (width/2.0))/followRes), getResources());
		
	}
	
	protected void onDraw(Canvas c){
		
		back.drawBackground(c);
        scorePaint.setColor(Color.GREEN);
        proScore = level.upcounter;
        c.drawText(String.valueOf(proScore), 20, 20, scorePaint);
        scorePaint.setColor(Color.argb(255, 255, 175, 00));
        c.drawText(String.valueOf(eggScore), width - 20, 20, scorePaint);
		
		level.draw(c);
		//level.shiftUp();
		
		elleph.draw(c);
	}
	
	
	
	
	
	
}
