package com.example.elephator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	ElephantView elephantView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RelativeLayout layout = new RelativeLayout(this);
		
		elephantView = new ElephantView(this);
		layout.addView(elephantView);
		
		setContentView(layout);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	@Override
    protected void onPause() {
        super.onPause();
        if(elephantView.updateThread != null)elephantView.updateThread.setRunning(false);
        // Another activity is taking focus (this activity is about to be "paused").
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(elephantView.updateThread != null)elephantView.updateThread.setRunning(false);
        // The activity is no longer visible (it is now "stopped")
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onOptionEggdrop(MenuItem i){
		elephantView.updateThread.setRunning(false);
		Intent nextScreen = new Intent(this, EggdropActivity.class);
		startActivity(nextScreen);
	}

}
