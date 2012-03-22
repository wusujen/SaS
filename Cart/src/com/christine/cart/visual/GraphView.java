package com.christine.cart.visual;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GraphView extends SurfaceView implements Runnable{
	
	Thread t = null;
	SurfaceHolder holder;
	boolean isOk = false;
	Canvas canvas;
	
	float _calorieRatio;
	float _calorieAdded;
	int _days;

	public GraphView(Context context) {
		super(context);
		holder = getHolder();
	}
	
	public GraphView(Context context, AttributeSet attrs){
		super(context, attrs);
		holder = getHolder();
	}
	
	public GraphView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		holder = getHolder();
	}

	public GraphView(Context context, float calorieRatio, float calorieAdded, int days){
		super(context);
		_calorieRatio = calorieRatio;
		_calorieAdded = calorieAdded;
		_days = days;
		holder = getHolder();
	}
	
	
	public void run() {
		// TODO Auto-generated method stub
		while(isOk){
			//drawing stuff goes here
			if(!holder.getSurface().isValid()){
				continue; 
			}
			
			canvas = holder.lockCanvas();
			canvas.drawARGB(255, 255, 255, 255);
			onDraw(canvas);
			holder.unlockCanvasAndPost(canvas);
		}
	}
	
	public void pause(){
		isOk = false;
		try{
			t.join();
		} catch ( InterruptedException e){
			e.printStackTrace();
		}
		
		t = null;	
	}
	
	public void resume(){
		isOk = true;
		t = new Thread(this);
		t.start();
	}
	
	@Override
	public void onDraw(Canvas c) {
		super.onDraw(c);
		
	    Paint blue = new Paint();
	    blue.setColor(Color.BLUE);
	    Paint black = new Paint();
	    black.setColor(Color.BLACK);
	    Paint grey = new Paint();
	    grey.setColor(Color.LTGRAY);
	    
	    int w = c.getWidth();
	    int h = c.getHeight();
	    
	    int base = h-80;
	    int topline = 60;
    	int diff = base-topline;
    	float interpolate = (float) diff/ (float) _days;
	    int baseHeight = Math.round(_calorieRatio*(float)diff);
	    int addHeight = Math.round(_calorieAdded*(float)diff);
	    
	    //to draw the lines:
	    for(int i=0;i<_days; i++){
	    	float b = topline + (i*interpolate);
	    	c.drawLine(20, b, w-20, b, grey);
	    }
	    
	    c.drawLine(20, base, w-20, base , black);
	    if(_calorieRatio != 0){ 
		    Rect baseRect = new Rect(40, base - baseHeight, 100, base);
		    c.drawRect(baseRect, blue);
		  } else {
			  Log.d("GraphView", "calorieratio is 0");
		  }
	    
	    if(_calorieAdded != 0){
	    	Rect addRect = new Rect(40, base - baseHeight , 100, base - baseHeight + addHeight );
	    	c.drawRect(addRect, grey);
	    }
	}
	
	public void setCalorieRatio(float calorieRatio){
		this._calorieRatio = calorieRatio;
	}
	
	public float getCalorieRatio(){
		return _calorieRatio;
	}
	
	public void setCalorieAdded(float calorieAdded){
		this._calorieAdded = calorieAdded;
	}
	
	public float getCalorieAdded(){
		return _calorieAdded;
	}
	
	public void setDays(int days){
		this._days = days;
	}

	

}
