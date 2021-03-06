package com.christine.cart.visual;


import com.christine.cart.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GraphLabelView extends View{
	
	private int days;
	private static int colorGreen;
	
	public GraphLabelView(Context context) {
		super(context);
		Resources res = getResources();
		colorGreen = res.getColor(R.color.theme_green);
	}
	
	public GraphLabelView(Context context, AttributeSet attrs) {
		super(context, attrs);	
		Resources res = getResources();
		colorGreen = res.getColor(R.color.theme_green);
		
	}

	public GraphLabelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Resources res = getResources();
		colorGreen = res.getColor(R.color.theme_green);
	}
	
	@Override
	public void onDraw(Canvas c){
		super.onDraw(c);
		
		int xPos = 65;
		int h = getHeight();
		int base = h-90;
		int topline = 100;
		int graphHeight = base - topline;
		
		drawLabels(c, graphHeight, base, topline, xPos);
		
	}
	

	
	public void setDays(int days){
		this.days = days;
	}
	
	
	
	/**
	 * Draw day labels, taking into consideration the quantity of days
	 * by reducing the number of labels applied to the graph
	 * 
	 * @param c
	 * @param graphHeight
	 * @param base
	 * @param topline
	 * @param xPos
	 */
	
	public void drawLabels(Canvas c, int graphHeight, int base, int topline, int xPos){
		Paint textPaint = new Paint();
		textPaint .setColor(Color.GRAY);
		textPaint .isAntiAlias();
		textPaint .setTextAlign(Paint.Align.RIGHT);
		textPaint .setTextSize(16);
		textPaint .setAntiAlias(true);
		textPaint .setSubpixelText(true);
		
		for(int i=0; i<days; i++){
			if(days>5 && days<9){
				int mod = i%2;
				if(mod==0){
					int yPos = base - (Math.round((float) graphHeight / (float) days)*i); 
					if(i==1){
						c.drawText( i + " DAY", xPos, yPos+4, textPaint);
					} else {
						c.drawText( i + " DAYS", xPos, yPos+4, textPaint);
					}
				}
			} else {
				int yPos = base - (Math.round((float) graphHeight / (float) days)*i); 
				if(i==1){
					c.drawText( i + " DAY", xPos, yPos+4, textPaint);
				} else {
					c.drawText( i + " DAYS", xPos, yPos+4, textPaint);
				}
			}
			invalidate();
		}
		
		textPaint.setColor(colorGreen);
		textPaint.setTextSize(20);
		c.drawText("GOAL", xPos, topline+4, textPaint);
	}
	
	
}
