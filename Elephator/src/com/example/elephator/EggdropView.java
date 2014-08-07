package com.example.elephator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

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
public class EggdropView extends SurfaceView
	implements UpdatableView, SurfaceHolder.Callback, OnGestureListener{
	
	/*Used for detecting input*/
    GestureDetector gestureScanner;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    long timer; //used to screen taps
    long oldtime = 0;
    boolean isEggRunning = false; //If we ar requesting an egg
    
    /*Used in HTTP Regex*/
    Pattern emptyPattern = Pattern.compile("BAD");
    Pattern colorPattern = Pattern.compile("([-+][0-9]*)([-+][0-9]*)iid([0-9]*)");
    Matcher m = null;
    
    UpdateThread updateThread;
    
    Paint stashPaint;
    String stashLow = null;
    
    Background2 back;
    Level level;
    Creature elleph;
    Fish fis = null;
    Egg egg = null;
    Product pro = null;
    ArrayList<Egg> launchedEggs;
    ArrayList<Egg> landedEggs;
    
    private int width,height; //width and height of the screen

	public EggdropView(Context context) {
		super(context);
		getHolder().addCallback(this);
		
		stashPaint = new Paint();
		stashPaint.setColor(Color.GREEN);
		stashPaint.setTextSize(14);
		
		gestureScanner = new GestureDetector(this);
	}

	public boolean onTouchEvent(MotionEvent e) {
		timer = System.currentTimeMillis();
		if((timer-oldtime)<500)return gestureScanner.onTouchEvent(e);
		
		//Proceed with Launching of Egg and New Egg
		if(isEggRunning == false){ //Preven multiple runs
				EggRun eggrun = new EggRun();
				new Thread(eggrun).start();
		}
		
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
		level.platforms[1].length = (int)((2.0/3.0)*width);
		
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
		if(pro!=null)pro.draw(c);
		if(fis!=null)fis.draw(c,1);
		try{
		for(Egg eggy : launchedEggs)eggy.draw(c);
		}catch(Exception e){
			//do nothing
		}
		
		for(Egg eggy: landedEggs)eggy.draw(c);
		
		//Print stash low if applicable
		if(stashLow != null)c.drawText(stashLow, 0, 60, stashPaint);
				
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
		//fish stuff
		if(fis!=null){
			fis.move(width, height);
			if(fis.y>height)fis=null;
		}
		//product stuff
		if(pro!=null){
			pro.move(width, height);
			if(pro.y > height)pro = null;
		}
		
		Iterator<Egg> itr = launchedEggs.listIterator();
		if(!itr.hasNext())return;
		try{
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
		catch(Exception e){
			return; //just return the routine. Updating physics isn't crucial?
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
	
	public class EggRun implements Runnable{

		@Override
		public void run() {
			int prim;
			int seco;
			int iid;
			isEggRunning = true; //set this true in case request takes long
			if(egg != null){
				launchedEggs.add(egg);
				if(launchedEggs.size()>5)launchedEggs.remove(0);
			}
			String colors = getData();
			/*Check to see if egg stash is empty*/
			m = emptyPattern.matcher(colors);
			if(m.matches()){
				stashLow = "Egg Stash is Empty\n Help Get More!!";
				return;
			}
			
			/*Otherwise, Create new egg*/
			m = colorPattern.matcher(colors);
			if(m.matches()){
				stashLow = null;
				prim = Integer.parseInt(m.group(1));
				seco = Integer.parseInt(m.group(2));
				iid = Integer.parseInt(m.group(3));
				if(iid == 1){
					egg = new Egg(getResources());
					egg.setColors(prim, seco);
					egg.x = elleph.x - elleph.r/2;
					egg.y = elleph.y + elleph.r/2;
					egg.vx = -5 + (int)(Math.random()*5);
					egg.vy = -5 + (int)(Math.random()*10);
				}
				if(iid == 0){
					pro = new Product(getResources());
					pro.setColors(prim, seco);
					pro.vy = -5 + (int)(Math.random()*10);
					pro.x = 0 + (int)(Math.random()*width);
					if(fis==null){
						fis=new Fish(getResources());
						fis.x = 0 + (int)(Math.random()*width);
						fis.y = height;
						fis.vy = (int)(-10) - (int)(Math.random()*10); //random number between -5 and 5
					}
				}
			}
			isEggRunning = false;
			
		}
		
	}
	
	public String getData(){
		// Create a new HttpClient and Get Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpGet httpget = new HttpGet("http://slymi.xen.prgmr.com:8888/get");
	    HttpResponse response;
	    String colors = "Empty";
	    try{
	    	response = httpclient.execute(httpget);
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
	    	colors = reader.readLine();
	    }catch (Exception e){}
	    System.out.println(colors);
		return colors;
	}
	

}
