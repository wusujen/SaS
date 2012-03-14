package com.christine.cart.visual;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GraphView extends View {
	
	int _nH;
	int _days;

	public GraphView(Context context) {
		super(context);
	}
	
	public GraphView(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	public GraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
	
	public GraphView(Context context, int nH, int days){
		super(context);
		_nH = nH;
		_days = days;
	}
	
	@Override
	public void onDraw(Canvas canvas) {
	    super.onDraw(canvas);
	
		    Paint blue = new Paint();
		    blue.setColor(Color.BLUE);
		    Paint black = new Paint();
		    black.setColor(Color.BLACK);
		    Paint grey = new Paint();
		    grey.setColor(Color.LTGRAY);
		    
		    int w = canvas.getWidth();
		    int h = canvas.getHeight();
		    
		    int base = h-80;
		    int topline = 20;
	    	int diff = base-topline;
	    	Log.d("days in GV: ", String.valueOf(_days));
	    	int interpolate = diff/(_days + 1);
		    int baseHeight = (diff - (_nH/diff))/_days ;
		
		    for(int i=0;i<_days; i++){
		    	int b = base - (i*interpolate);
		    	canvas.drawLine(20, b, w-20, b, grey);
		    }
		    
		    canvas.drawLine(20, base, w-20, base , black);
		    if(_nH != 0){ 
		    	 Log.d("GraphView", "new calories is " + _nH);
				 Log.d("baseHeight ", "baseHeight: " + baseHeight);
			    Rect baseRect = new Rect(40, baseHeight, 100, base);
			    canvas.drawRect(baseRect, blue);
			  }
	}
	
	public void setnH(int nH){
		this._nH = nH;
	}
	
	public int getnH(){
		return _nH;
	}
	
	public void setDays(int days){
		this._days = days;
	}
	

}
