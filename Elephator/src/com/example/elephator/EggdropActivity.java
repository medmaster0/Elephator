package com.example.elephator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EggdropActivity extends Activity {

	EggdropView eggdropView;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		eggdropView = new EggdropView(this);
		setContentView(eggdropView);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		//*****UNCOMMENT this when onBackClick is implemented
		//finish(); 
	}
	
	@Override
    protected void onPause() {
        super.onPause();
        if(eggdropView.updateThread != null)eggdropView.updateThread.setRunning(false);
        // Another activity is taking focus (this activity is about to be "paused").
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(eggdropView.updateThread != null)eggdropView.updateThread.setRunning(false);
        // The activity is no longer visible (it is now "stopped")
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onOptionEggdrop(MenuItem i){
		eggdropView.updateThread.setRunning(false);
		Intent nextScreen = new Intent(this, EggdropActivity.class);
		startActivity(nextScreen);
	}
	
}
