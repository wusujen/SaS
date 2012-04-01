package com.christine.cart.visual;

import java.util.ArrayList;
import java.util.HashMap;

import com.christine.cart.sqlite.Item;
import com.christine.cart.sqlite.PreviousHistory;
import com.christine.cart.sqlite.RecDailyValues;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

public class GraphView extends View{

	public SurfaceHolder holder;
	public int _days;
	public int _topline;
	public int _base;
	public int _graphHeight;
	public ArrayList<Item> selectedItems;
	public ArrayList<Integer> selectedQuantities;

	private static HashMap<String, Float> needs;
	private static HashMap<String, Float> ratios;
	private static HashMap<String, Float> goals;
	
	private static int MODE;
	private final static int SELECT_NONE = 0;
	private final static int SELECT_SINGLE = 1;
	private final static int SELECT_COMPARE = 2;
	private final static String[] order = new String[] {
			"calories", "protein", "totalfats", "carbs", "fiber", 
			"sugar", "calcium", "iron", "magnesium", "potassium", "sodium", "zinc", "vitamin C",
			"vitamin D", "vitamin B6", "vitamin B12", "vitamin A", "vitamin E", "vitamin K",
			"Saturated Fat", "Mononsaturated Fat", "Polyunsaturated Fat", "cholesterol" };
	


	public GraphView(Context context) {
		super(context);

		selectedItems = new ArrayList<Item>();
		selectedQuantities = new ArrayList<Integer>();
	}

	public GraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		selectedItems = new ArrayList<Item>();
		selectedQuantities = new ArrayList<Integer>();
	}

	public GraphView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		selectedItems = new ArrayList<Item>();
		selectedQuantities = new ArrayList<Integer>();
	}
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		heightMeasureSpec = 480;
		if( MODE == 0 || MODE == 1){
			widthMeasureSpec = 3800;
		} else if (MODE == 2){
			widthMeasureSpec = 4500; 
		}
		setMeasuredDimension( widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);
		Paint blueLine = new Paint();
		blueLine.setColor(Color.BLUE);
		blueLine.setStrokeWidth(2);
		
		Paint blackLine = new Paint();
		blackLine.setColor(Color.BLACK);
		
		Paint greyLine = new Paint();
		greyLine.setColor(Color.LTGRAY);

		int w = getWidth();
		int h = getHeight();

		_base = h - 90;
		_topline = 90;
		_graphHeight = _base - _topline;
		float interpolate = (float) _graphHeight / (float) _days;
		
		
		// to draw the lines:
		for (int i = 0; i < _days; i++) {
			float b = _topline + (i * interpolate);
			c.drawLine(20, b, w - 20, b, greyLine);
		} 
		c.drawLine(20, _topline, w - 20, _topline, blueLine);	//top line
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
		needs = new HashMap<String, Float>(order.length);
		ratios = new HashMap<String, Float>(order.length);
		goals = new HashMap<String, Float>(order.length);
		
		Float[] rdvTotals = currentRDV.getNutritionNeeds();
		Float[] cartTotals = currentTotalCart.getNutritionProperties();
		Float[] pcartTotals = pcart.getNutritionProperties();
		
		for(int i=0; i<order.length; i++){
			float need = rdvTotals[i] * (float) this._days;
			float ratio = cartTotals[i] / need;
			float goal = pcartTotals[i] / need;
			
			String n = order [i];
			
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
	}
	
	/**
	 * 
	 * @param totalCart
	 * @param totalRDV
	 */
	public void getRatiosWithoutPCart(PreviousHistory currentTotalCart, RecDailyValues currentRDV) {
		needs = new HashMap<String, Float>(order.length);
		ratios = new HashMap<String, Float>(order.length);
		
		Float[] rdvTotals = currentRDV.getNutritionNeeds();
		Float[] cartTotals = currentTotalCart.getNutritionProperties();
		
		for(int i=0; i<order.length; i++){
			float need = rdvTotals[i] * (float) this._days;
			float ratio = cartTotals[i] / need;
			
			String n = order [i];
			
			needs.put(n, need);
			if(need == 0.0f){
				ratios.put(n, 0.0f);
			} else {
				ratios.put(n, ratio);
			}
		}
	}
	
	/**
	 * Returns the height of the calories bar based on one item's index, taking into
	 * account the quantity.
	 * 
	 * @param index of the item in the cart
	 * @return
	 */
	public ArrayList<Float> getAddedNutrition(int index){
		Float[] nutrients = selectedItems.get(index).getNutritionProperties();
		
		ArrayList<Float> addedNutrition = new ArrayList<Float>();
		for(int i=0; i<nutrients.length; i++){
			float addedContent = ((nutrients[i] * (float) selectedQuantities.get(index)) / 
				needs.get(order[i]));
			addedNutrition.add(addedContent);
		}
		
		return addedNutrition;
	}

	private void drawCurrentCartContent(Canvas c, int base, int graphHeight) {
		
		Paint grey = new Paint();
		grey.setColor(Color.LTGRAY);
		
		Paint blackText = new Paint();
		blackText.setColor(Color.BLACK);
		blackText.isAntiAlias();
		blackText.setTextSize(22);
		
		// WIDTH
		int startBar = 40;
		int barWidth = 60;
		int endBar = startBar + barWidth;
		int halfBar = (endBar - startBar)/2;
		
		for(int i=0; i<order.length; i++){
			int spacing = 150*i;
			
			// TEXT
			String o = order[i];
			float len = blackText.measureText(o, 0, o.length());
			c.drawText(o, (startBar+spacing + (halfBar-(len/2))), base + 25, blackText);
			
			// BAR
			int barHeight = Math.round(ratios.get(o) * (float) graphHeight);
			
			Rect baseRect = new Rect((startBar+spacing), base - barHeight, (endBar+spacing), base);
			c.drawRect(baseRect, grey);
		}
		
	}
	
	/**
	 * Only if MODE is equal to SELECT_SINGLE
	 * Draws one bar with one grey selelection
	 * 
	 * @param c
	 * @param base
	 * @param baseHeight
	 * @param addHeight
	 */
	public void drawSingleMode(Canvas c, int base, int graphHeight) {
		
		// Bar Colors
		Paint blue = new Paint();
		blue.setColor(Color.BLUE);
		
		
		ArrayList<Float> selectedNutrition = getAddedNutrition(0);
		
		// WIDTH
		int startBar = 40;
		int endBar = 100;
		
		for(int i=0; i<selectedNutrition.size(); i++){
			int spacing = 150*i;
			int barHeight = Math.round(ratios.get(order[i]) * (float) graphHeight);
			int addHeight = Math.round(selectedNutrition.get(i) * (float) graphHeight);
			
			Rect addRect = new Rect((startBar+spacing), base - barHeight, (endBar+spacing), base - barHeight
					+ addHeight);
			c.drawRect(addRect, blue);
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
		
		Paint orange = new Paint();
		orange.setColor(Color.YELLOW);
		
		Paint blue = new Paint();
		blue.setColor(Color.BLUE);
		
		Paint blackText = new Paint();
		blackText.setColor(Color.BLACK);
		blackText.isAntiAlias();
		blackText.setTextSize(22);
		
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
			String o = order[i];
			float len = blackText.measureText(o, 0, o.length());
			c.drawText(o, (startBarB+spacing + (halfBar-(len/2))), base + 25, blackText);
			
			// BAR
			int barHeightB = Math.round(NutrientB.get(i) * (float) graphHeight);
			int barHeightO = Math.round(NutrientO.get(i) * (float) graphHeight);
			
			Rect baseRect = new Rect((startBarB+spacing), base - barHeightB, (endBarB+spacing), base);
			Rect compareRect = new Rect((startBarO+spacing), base - barHeightO, (endBarO+spacing), base);
			
			c.drawRect(baseRect, blue);
			c.drawRect(compareRect, orange);
		}
	}

	private void drawGoalLines(Canvas c, int graphHeight, int base, int topline){
		Paint incPaint = new Paint();
		incPaint.setColor(Color.rgb(10, 207, 72));
		incPaint.setStrokeWidth(2);
		
		Paint decPaint = new Paint();
		decPaint.setColor(Color.rgb(250, 94, 10));
		decPaint.setStrokeWidth(2);
		
		int startLineX = 40;
		int endLineX = 100;
		
		for(int i=0; i<order.length; i++){
			int spacing = 150*i;
			
			int yPos = base - Math.round(goals.get(order[i]) * (float) graphHeight); 
			
			if(yPos < topline) {
				c.drawLine(startLineX + spacing, yPos, endLineX + spacing, yPos, incPaint);
			} else if ( yPos > topline &  yPos < (base - 2)){
				c.drawLine(startLineX + spacing, yPos, endLineX + spacing, yPos, decPaint);
			}
			
		}
	}

}
