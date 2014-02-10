package com.example.elephator;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class UpdateThread extends Thread {

	private long time;
	private final int fps = 20;
	private boolean toRun = false;
	private ElephantView elephantView;
	private SurfaceHolder surfaceHolder;
	
	public UpdateThread(ElephantView rElephantView){
		elephantView = rElephantView;
		surfaceHolder = elephantView.getHolder();
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
					elephantView.updatePhysics();
					elephantView.onDraw(c);
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
