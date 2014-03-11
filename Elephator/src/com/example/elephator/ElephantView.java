package com.example.elephator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.elephator.EggdropView.HatchRun;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.GestureDetector.OnGestureListener;
import android.view.SurfaceView;

@SuppressLint("NewApi")
public class ElephantView extends SurfaceView 
	implements UpdatableView, SurfaceHolder.Callback, OnGestureListener{
	
	/*Used for detecting input*/
    GestureDetector gestureScanner;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	
	UpdateThread updateThread;
	File file;
	RandomAccessFile raf;
	long rafIndex = 0;
	
	long timer = 0;
	long oldtime = 0;
	
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

		file = new File(context.getFilesDir(), "eggList.out");
		try {
			raf = new RandomAccessFile(file, "rw");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		scorePaint = new Paint();
		scorePaint.setColor(Color.GREEN);
		scorePaint.setTextSize(14);

		gestureScanner = new GestureDetector(this);
	}

	
	@Override 
    public boolean onTouchEvent(MotionEvent e) {
    	
//		timer = System.currentTimeMillis();
//		if((timer-oldtime)<200)return gestureScanner.onTouchEvent(e);
//		oldtime = timer;
		
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
			elleph.jump();
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
		
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inMutable = true;  //allows us access to pixel array later
		opt.inScaled = false;
		Bitmap bp = null; //used as a holder for the following bitmap factories
		bp = BitmapFactory.decodeResource(getResources(), R.drawable.cap, opt);
		elleph.giveItem(bp);
		bp = BitmapFactory.decodeResource(getResources(), R.drawable.purplog,opt);
		int p = 0; //used as index in following
		for(Platform platform: level.platforms){
			platform.thickness = height/30;
			platform.changePlatform(bp, 1);
			p++;
			if(p%2!=0)platform.flip();
		}
				
		
		updateThread = new UpdateThread(this);
		updateThread.setRunning(true);
		updateThread.start();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		updateThread.setRunning(false);
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
				DataRun datarun = new DataRun(level.egg);
				new Thread(datarun).start();
				level.egg = null;
			}
		}
		
		//Check to see if elephant has gotten an product
		if(level.pro != null){
			if(Collision.creatureProductCollision(elleph, level.pro)){
				proScore = proScore + 1;
				DataRun datarun = new DataRun(level.pro);
				new Thread(datarun).start();
				level.pro = null;
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
		//level.shiftUp((int)((elleph.y - (width/2.0))/followRes), getResources());
		level.shiftUp((int)((elleph.y - (height/3.3))/followRes), getResources());
	}
	
	public void onDraw(Canvas c){
		
		back.drawBackground(c);
        scorePaint.setColor(Color.GREEN);
        c.drawText(String.valueOf(level.upcounter), 20, 20, scorePaint);
        c.drawText(String.valueOf(level.downcounter), 20, 40, scorePaint);
        scorePaint.setColor(Color.argb(255, 255, 175, 00));
        c.drawText(Integer.toString(eggScore), width - 20, 20, scorePaint);
        c.drawText(Integer.toString(proScore), width - 20, 40, scorePaint);
        
        
        /*Debugging Purposes*/
        if(level.egg!=null)c.drawText(String.valueOf(level.egg.prim), width- 150, 20, scorePaint);
        if(level.pro!=null)c.drawText(Double.toString(level.pro.width), width - 150, 40, scorePaint);
        
		//level.shiftUp();
		elleph.draw(c);
		level.draw(c);
	}
	
	public class DataRun implements Runnable{

		Product pro;
		public DataRun(Product pro){
			this.pro = pro;
		}
		
		@Override
		public void run() {
			postData(pro);
			//saveData(prim,seco);
		}
		
	}
	
	public void postData(Product pro) {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://elephator.tk:8888/");

	    try {
	        // Add your data
	        ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("prim", String.valueOf(pro.prim)));
	        nameValuePairs.add(new BasicNameValuePair("seco", String.valueOf(pro.seco)));
	        nameValuePairs.add(new BasicNameValuePair("iid", String.valueOf(pro.iid)));
	        
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Requestd
	        httpclient.execute(httppost);

	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	}
	
	public void saveData(int prim, int seco){
		try {
			raf.writeInt(prim); 
			raf.writeInt(seco);
			raf.seek(0 +8*rafIndex); //int takes 8 pointer offsets
			elleph.setColors(raf.readInt(), raf.readInt());
			rafIndex++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
}
