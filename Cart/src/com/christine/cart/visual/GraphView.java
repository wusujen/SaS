package com.christine.cart.visual;

import java.util.ArrayList;
import java.util.HashMap;

import com.christine.cart.R;
import com.christine.cart.sqlite.Item;
import com.christine.cart.sqlite.PreviousHistory;
import com.christine.cart.sqlite.RecDailyValues;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Toast;

public class GraphView extends View implements View.OnTouchListener{

	public SurfaceHolder holder;
	public int _days;
	public int _topline;
	public int _base;
	public int _graphHeight;
	public int _cap;
	public ArrayList<Item> selectedItems;
	public ArrayList<Integer> selectedQuantities;

	private static HashMap<String, Float> needs;
	private static HashMap<String, Float> ratios;
	private static HashMap<String, Float> goals;
	
	private static int MODE;
	private final static int SELECT_NONE = 0;
	private final static int SELECT_SINGLE = 1;
	private final static int SELECT_COMPARE = 2;
	private final static String[] reduced = new String[]{
		"calories", "protein", "totalfats", "carbs", "fiber", 
		"sugar", "calcium", "iron", "magnesium", "potassium",
		"sodium", "zinc", "vitamin C", "vitamin D", "vitamin B6",
		"vitamin B12", "vitamin A", "vitamin E", "vitamin K", "saturated fat",
		"cholesterol"};
	
	private static int colorGreen;
	private static int colorOrange;
	private static int colorBlue;

	public GraphView(Context context) {
		super(context);

		selectedItems = new ArrayList<Item>();
		selectedQuantities = new ArrayList<Integer>();
		
		Resources res = getResources();
		colorGreen = res.getColor(R.color.theme_green);
		colorOrange = res.getColor(R.color.theme_orange);
		colorBlue = res.getColor(R.color.theme_lightblue);
	}

	public GraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		selectedItems = new ArrayList<Item>();
		selectedQuantities = new ArrayList<Integer>();
		
		Resources res = getResources();
		colorGreen = res.getColor(R.color.theme_green);
		colorOrange = res.getColor(R.color.theme_orange);
		colorBlue = res.getColor(R.color.theme_lightblue);
	}

	public GraphView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		selectedItems = new ArrayList<Item>();
		selectedQuantities = new ArrayList<Integer>();
		
		Resources res = getResources();
		colorGreen = res.getColor(R.color.theme_green);
		colorOrange = res.getColor(R.color.theme_orange);
		colorBlue = res.getColor(R.color.theme_lightblue);
	}
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		heightMeasureSpec = MeasureSpec.getSize(heightMeasureSpec);
		widthMeasureSpec = 3800;
		setMeasuredDimension( widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);
		
		Paint greenLine = new Paint();
		greenLine.setColor(colorGreen);
		greenLine.setStrokeWidth(2);
		
		Paint blackLine = new Paint();
		blackLine.setColor(Color.BLACK);
		blackLine.setStrokeWidth(2);
		
		Paint greyLine = new Paint();
		greyLine.setColor(Color.LTGRAY);

		int w = getWidth();
		int h = getHeight();

		_base = h - 90;
		_topline = 100;
		_graphHeight = _base - _topline;
		_cap = _graphHeight + 10;
		float interpolate = (float) _graphHeight / (float) _days;
		
		
		// to draw the lines:
		for (int i = 0; i < _days; i++) {
			float b = _topline + (i * interpolate);
			c.drawLine(20, b, w - 20, b, greyLine);
		} 
		c.drawLine(20, _topline, w - 20, _topline, greenLine);	//top line
		c.drawLine(20, _base, w - 20, _base, blackLine);		//bottom line
		
		determineMode();
		
		if (ratios!=null && MODE==SELECT_NONE) {
			drawCurrentCartContent(c, _base, _graphHeight);
			if(goals!=null){
				drawGoalLines(c, _graphHeight, _base, _topline);
			}
			
		} else if(ratios!=null && MODE==SELECT_SINGLE){
			drawCurrentCartContent(c, _base, _graphHeight);
			drawSingleMode(c, _base, _graphHeight);
			if(goals!=null){
				drawGoalLines(c, _graphHeight, _base, _topline);
			}
			
		} else if (MODE==SELECT_COMPARE){
			drawCompareMode(c, _base, _graphHeight);
		}

	}
	
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		int[] location = new int[2];
		v.getLocationInWindow(location);
		
		Log.d("GraphView", "location x: " + location[0] + " location y: " + location[2]);
		return true;
	}
	
	public void setDays(int days) {
		this._days = days;
	}
	
	public int getDays() {
		return this._days;
	}
	
	public int getBase(){
		return this._base;
	}
	
	public int getTopline(){
		return this._topline;
	}
	
	public int getGraphHeight(){
		return this._graphHeight;
	}

	public void passSelectedItems(ArrayList<Item> selection) {
		this.selectedItems = selection;
	}

	public void passSelectedQuantities(ArrayList<Integer> quantities) {
		this.selectedQuantities = quantities;
	}

	public void determineMode() {
		if(selectedItems!=null){
			MODE = selectedItems.size();
			return;
		} 
		
		MODE = SELECT_NONE;
	}
		
	/**
	 * 
	 * @param totalCart
	 * @param totalRDV
	 */
	public void getRatiosWithPCart(PreviousHistory currentTotalCart, RecDailyValues currentRDV, PreviousHistory pcart) {
		needs = new HashMap<String, Float>(reduced.length);
		ratios = new HashMap<String, Float>(reduced.length);
		goals = new HashMap<String, Float>(reduced.length);
		
		Float[] rdvTotals = currentRDV.getNutritionNeedsNoMono();
		Float[] cartTotals = currentTotalCart.getNutritionPropertiesNoMono();
		Float[] pcartTotals = pcart.getNutritionPropertiesNoMono();
		float previousDays = pcart.getDays();
		
		for(int i=0; i<reduced.length; i++){
			float need = rdvTotals[i] * (float) this._days;
			float ratio = cartTotals[i] / need;
			float goal = pcartTotals[i] /(rdvTotals[i] * (float) previousDays);
			
			String n = reduced [i];
			
			needs.put(n, need);
			if(need == 0.0f){
				ratios.put(n, 0.0f);
			} else {
				ratios.put(n, ratio);
			}
			
			if(goal == 0.0f){
				goals.put(n, 0.0f);
			} else {
				goals.put(n, goal);
			}
			/*
			Log.d("GraphView", "Need: " + n + "," + needs.get(n) + " || Ratio: " + ratios.get(n) 
					+ "||  Goal " + goals.get(n));*/
		}
		
		needs.remove("polyunsaturated fat");
		needs.remove("monunsaturated fat");
		
		ratios.remove("polyunsaturated fat");
		ratios.remove("monunsaturated fat");
		
		goals.remove("polyunsaturated fat");
		goals.remove("monunsaturated fat");
		
	}
	
	/**
	 * 
	 * @param totalCart
	 * @param totalRDV
	 */
	public void getRatiosWithoutPCart(PreviousHistory currentTotalCart, RecDailyValues currentRDV) {
		needs = new HashMap<String, Float>(reduced.length);
		ratios = new HashMap<String, Float>(reduced.length);
		goals = null;
		
		Float[] rdvTotals = currentRDV.getNutritionNeedsNoMono();
		Float[] cartTotals = currentTotalCart.getNutritionPropertiesNoMono();
		
		for(int i=0; i<reduced.length; i++){
			float need = rdvTotals[i] * (float) this._days;
			float ratio = cartTotals[i] / need;
			
			String n = reduced[i];
			
			needs.put(n, need);
			if(need == 0.0f){
				ratios.put(n, 0.0f);
			} else {
				ratios.put(n, ratio);
			}
		}
		
		needs.remove("polyunsaturated fat");
		needs.remove("monunsaturated fat");
		
		ratios.remove("polyunsaturated fat");
		ratios.remove("monunsaturated fat");
	}
	
	/**
	 * Returns the height of the calories bar based on one item's index, taking into
	 * account the quantity.
	 * 
	 * @param index of the item in the cart
	 * @return
	 */
	public ArrayList<Float> getAddedNutrition(int index){
		Float[] nutrients = selectedItems.get(index).getNutritionPropertiesNoMono();
		
		ArrayList<Float> addedNutrition = new ArrayList<Float>();
		for(int i=0; i<nutrients.length; i++){
			float addedContent = ((nutrients[i] * (float) selectedQuantities.get(index)) / 
				needs.get(reduced[i]));
			addedNutrition.add(addedContent);
		}
		
		return addedNutrition;
	}

	private void drawCurrentCartContent(Canvas c, int base, int graphHeight) {
		
		Paint grey = new Paint();
		grey.setColor(Color.LTGRAY);
		
		Paint orange = new Paint();
		orange.setColor(colorOrange);
		
		Paint blackText = new Paint();
		blackText.setColor(Color.BLACK);
		blackText.isAntiAlias();
		blackText.setTextSize(18);
		blackText.setTextAlign(Paint.Align.CENTER);
		blackText.setAntiAlias(true);
		blackText.setSubpixelText(true);
		
		// WIDTH
		int startBar = 40;
		int barWidth = 60;
		int endBar = startBar + barWidth;
		int halfBar = (endBar - startBar)/2;
		
		for(int i=0; i<reduced.length; i++){
			int spacing = 180*i;
			
			// TEXT
			String o = reduced[i];
			float len = blackText.measureText(o.toUpperCase(), 0, o.length());
			c.drawText(o.toUpperCase(), (startBar + spacing + halfBar), base + 25, blackText);
			
			// BAR
			int barHeight = Math.round(ratios.get(o) * (float) graphHeight);

			//cap the heights of the bars 
			if(barHeight > this._graphHeight){
				barHeight = _cap;
			}

			Rect baseRect = new Rect((startBar + spacing), base - barHeight, (endBar+spacing), base);
			c.drawRect(baseRect, grey);

		}
		
	}
	
	/**
	 * Only if MODE is equal to SELECT_SINGLE
	 * Draws one bar with one grey selection
	 * 
	 * @param c
	 * @param base
	 * @param baseHeight
	 * @param addHeight
	 */
	public void drawSingleMode(Canvas c, int base, int graphHeight) {
		
		// Bar Colors
		Paint green = new Paint();
		green.setColor(colorGreen);
		
		Paint orange = new Paint();
		orange.setColor(colorOrange);
		
		ArrayList<Float> selectedNutrition = getAddedNutrition(0);
		
		// WIDTH
		int startBar = 40;
		int endBar = 100;
		
		for(int i=0; i<selectedNutrition.size(); i++){
			int spacing = 180*i;
			int barHeight = Math.round(ratios.get(reduced[i]) * (float) graphHeight);
			int addHeight = Math.round(selectedNutrition.get(i) * (float) graphHeight);
			
			//cap the heights of the bars 
			if(barHeight > this._graphHeight){
				barHeight = _cap;
				addHeight = _cap;
			}
			
			Rect addRect = new Rect((startBar+spacing), base - barHeight, (endBar+spacing), base - barHeight
					+ addHeight);
			c.drawRect(addRect, green);
		}
	}
	
	/**
	 * ONLY if mode is equal to SELECT_COMPARE
	 * Draws a blue bar to represent the first selected item
	 * and yellow bar to represent the second.
	 *  
	 * @param c
	 * @param base
	 * @param baseHeight
	 * @param cBaseHeight
	 */
	public void drawCompareMode(Canvas c, int base, int graphHeight){
		
		Paint blue = new Paint();
		blue.setColor(colorBlue);
		
		Paint green = new Paint();
		green.setColor(colorGreen);
		
		Paint blackText = new Paint();
		blackText.setColor(Color.BLACK);
		blackText.isAntiAlias();
		blackText.setTextSize(18);
		blackText.setAntiAlias(true);
		blackText.setSubpixelText(true);
		
		// WIDTH
		int startBarB = 40;
		int endBarB = startBarB + 60;
		int startBarO = endBarB + 20;
		int endBarO = startBarO + 60 ;
		int halfBar = (endBarO - startBarB)/2;	//TEXT
		
		// NUTRITION
		ArrayList<Float> NutrientB = getAddedNutrition(0);
		ArrayList<Float> NutrientO = getAddedNutrition(1);
		
		for(int i=0; i<NutrientB.size(); i++){
			int spacing = (endBarO + 20)*i;
			
			// TEXT
			String o = reduced[i];
			float len = blackText.measureText(o.toUpperCase(), 0,o.length());
			c.drawText(o.toUpperCase(), (startBarB + spacing + (halfBar-(len/2))), base + 25, blackText);
			
			// BAR
			int barHeightB = Math.round(NutrientB.get(i) * (float) graphHeight);
			int barHeightO = Math.round(NutrientO.get(i) * (float) graphHeight);
			
			//cap the heights of the bars 
			if(barHeightB > this._graphHeight){
				barHeightB = _cap;
			}
			if(barHeightO > this._graphHeight){
				barHeightO = _cap;
			}
			
			Rect baseRect = new Rect();
			Rect compareRect = new Rect();
			
			baseRect = new Rect((startBarB+spacing), base - barHeightB, (endBarB+spacing), base);
		
			compareRect = new Rect((startBarO+spacing), base - barHeightO, (endBarO+spacing), base);
			
			c.drawRect(baseRect, green);
			c.drawRect(compareRect, blue);
		}
	}
	
	/*
	 * Draw the lines that form the goals (not including labels)
	 */
	private void drawGoalLines(Canvas c, int graphHeight, int base, int topline){
		Paint incPaint = new Paint();
		incPaint.setColor(colorGreen);
		incPaint.setStrokeWidth(2);
		
		Paint decPaint = new Paint();
		decPaint.setColor(colorOrange);
		decPaint.setStrokeWidth(2);
		
		int startLineX = 40;
		int endLineX = 100;
		
		Bitmap incArrow = BitmapFactory.decodeResource(getResources(), R.drawable.cart_increase);
		Bitmap decArrow = BitmapFactory.decodeResource(getResources(), R.drawable.cart_decrease);
		int bitmapXSpacing = 4;
		int bitmapYA = 2;
		int bitmapYB = 18;
		int minHeight = base - _cap;
		
		for(int i=0; i<reduced.length; i++){
			int spacing = 180*i;
			
			int yPos = base - Math.round(goals.get(reduced[i]) * (float) graphHeight); 
			int barHeight = base - (Math.round(ratios.get(reduced[i]) * (float) graphHeight));
		
			
			//special case numbers for the bad stuff, always set to neg
			//sugar=>5 || sodium=>10 || saturated fat=>19 || cholesterol=>20
			if(i!=5 && i!=10 && i!=19 && i!=20){
				if(yPos < minHeight ) {
					//don't draw an arrow for these, because they are exceeding already
					yPos = minHeight;
					c.drawLine(startLineX + spacing, yPos, endLineX + spacing, yPos, incPaint);
				} else if ( yPos > minHeight && yPos < (base - 2)){
					//otherwise, draw an arrow and the line		
					c.drawLine(startLineX + spacing, yPos, endLineX + spacing, yPos, incPaint);
					if(yPos < barHeight) {
						c.drawBitmap(incArrow, startLineX + bitmapXSpacing +spacing, yPos - bitmapYB, null);
					} else {
						// draw the arrow on top of the bar to encourage increase only if the
						// bar is below the cap height
						c.drawBitmap(incArrow, startLineX + bitmapXSpacing + spacing, barHeight - bitmapYB, null);
					}
				}
			} else {
				if(yPos < minHeight ) {
					yPos = minHeight;
					c.drawLine(startLineX + spacing, yPos, endLineX + spacing, yPos, decPaint);
					c.drawBitmap(decArrow, startLineX + bitmapXSpacing +spacing, yPos + bitmapYA, null);
				}  else if ( yPos > minHeight && yPos < (base - 2)){
					//otherwise, draw an arrow and the line
					c.drawLine(startLineX + spacing, yPos, endLineX + spacing, yPos, decPaint);
					
					if(yPos < barHeight) {
						c.drawBitmap(decArrow, startLineX + bitmapXSpacing +spacing, yPos - bitmapYB, null);
					} else {
						// draw the arrow on top of the bar to encourage increase only if the
						// bar is below the cap height
						c.drawBitmap(decArrow, startLineX + bitmapXSpacing + spacing, barHeight - bitmapYB, null);
					}
				}  	
			}
		}
	}

	
	

}
