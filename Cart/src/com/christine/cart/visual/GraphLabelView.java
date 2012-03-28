package com.christine.cart.visual;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GraphLabelView extends View{
	
	private int days;
	private int graphHeight;
	
	public GraphLabelView(Context context) {
		super(context);
	}
	
	public GraphLabelView(Context context, AttributeSet attrs) {
		super(context, attrs);	
	}

	public GraphLabelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public void onDraw(Canvas c){
		super.onDraw(c);
		
		Paint textPaint = new Paint();
		textPaint .setColor(Color.GRAY);
		textPaint .isAntiAlias();
		textPaint .setTextAlign(Paint.Align.RIGHT);
		textPaint .setTextSize(18);
		
		int xPos = 75;
		int h = getHeight();
		int base = h-80;
		int topline = 60;
		int graphHeight = base - topline;
		
		for(int i=0; i<days; i++){
			if(days>10 && days<18){
				int mod = i%2;
				if(mod==0){
					int yPos = base - (Math.round((float) graphHeight / (float) days)*i); 
					c.drawText( i + " days", xPos, yPos+4, textPaint);
				}
			} else if(days>18){
				int mod = i%4;
				if(mod==0){
					int yPos = base - (Math.round((float) graphHeight / (float) days)*i); 
					c.drawText( i + " days", xPos, yPos+4, textPaint);
				}
			} else {
				int yPos = base - (Math.round((float) graphHeight / (float) days)*i); 
				c.drawText( i + " days", xPos, yPos+4, textPaint);
			}
			invalidate();
		}
		
		c.drawText("GOAL", xPos, topline+4, textPaint);
		
	}
	
	public Paint makeTextPaint(int textSize, int color){

		Paint textPaint = new Paint();
		textPaint .setColor(color);
		textPaint .isAntiAlias();
		textPaint .setTextAlign(Paint.Align.RIGHT);
		textPaint .setTextSize(textSize);
		
		return textPaint;
	}
	
	public void setDays(int days){
		this.days = days;
	}
	
	public void setGraphHeight(int gH){
		this.graphHeight = gH;
	}


}
