package com.example.elephator;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class UpdateThread extends Thread {

	private long time;
	private final int fps = 20;
	private boolean toRun = false;
	private UpdatableView upview;
	private SurfaceHolder surfaceHolder;
	
	public UpdateThread(UpdatableView upview){
		this.upview = upview;
		surfaceHolder = upview.getHolder();
	}
	
	public void setRunning(boolean run){
		toRun = run;
	}
	
	public void run(){
		Canvas c;
		while (toRun){
			long cTime = System.currentTimeMillis();
			if((cTime - time) <= (1000/fps)){
				c = null;
				try{
					c = surfaceHolder.lockCanvas(null);
					upview.updatePhysics();
					upview.onDraw(c);
				} finally {
					if(c != null){
						surfaceHolder.unlockCanvasAndPost(c);
					}
				}
				
			}
			time = cTime;
			
		}
		
		
		
	}
	
}
