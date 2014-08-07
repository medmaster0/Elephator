package com.example.elephator;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;

@SuppressLint("NewApi")
public class Fish extends Creature {

	public Fish(Resources r){
		super(r);
		/*Always Add the opt so we get a mutable bitmap that wwe can scale*/
		BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        opt.inScaled = false;
		this.bmp = BitmapFactory.decodeResource(r, R.drawable.fish, opt);
		this.oldp = Color.BLACK;
		this.olds = Color.WHITE;
		this.bmpCols = 1;
		this.bmpRows = 2;
		this.currentFrame = 0;
		this.width = bmp.getWidth() / bmpCols;
		this.height = bmp.getHeight() / bmpRows;
		this.vx = -5 + (int)(Math.random()*10); //random number between -5 and 5
		randomizeBmp();
	}
	
	
}
