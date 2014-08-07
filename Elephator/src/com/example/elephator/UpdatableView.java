package com.example.elephator;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public interface UpdatableView {
	
	void onDraw(Canvas c);
	void updatePhysics();
	SurfaceHolder getHolder();

}
