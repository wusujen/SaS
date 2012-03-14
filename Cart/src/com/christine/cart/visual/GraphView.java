package com.christine.cart.visual;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class GraphView extends View {
	
	int _nH;

	public GraphView(Context context) {
		super(context);
	}
	
	public GraphView(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	public GraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
	
	public GraphView(Context context, int nH){
		super(context);
		_nH = nH;
	}
	
	@Override
	public void onDraw(Canvas canvas) {
	    super.onDraw(canvas);

	    Paint blue = new Paint();
	    blue.setColor(Color.BLUE);
	    Paint black = new Paint();
	    black.setColor(Color.BLACK);
	    
	    int w = canvas.getWidth();
	    int h = canvas.getHeight();
	    
	    //Rect baseRect = new Rect(w-40, h-(_nH+61),w-80, h-61);
	   // canvas.drawRect(baseRect, blue);
	    canvas.drawLine(20, h-80, w-20, h-80, black);
	    
	    
	}

}
