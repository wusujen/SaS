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
import android.view.SurfaceView;

public class GraphView extends SurfaceView implements Runnable {

	private Thread t = null;
	public SurfaceHolder holder;
	private boolean isOk = false;
	private Canvas canvas;
	public int _days;
	public ArrayList<Item> selectedItems;
	public ArrayList<Integer> selectedQuantities;

	private static HashMap<String, Float> needs;
	private static HashMap<String, Float> ratios;
	
	private static float calorieRatio;
	private static float caloriesNeeded;
	
	private static int MODE;
	private final static int SELECT_NONE = 0;
	private final static int SELECT_SINGLE = 1;
	private final static int SELECT_COMPARE = 2;
	private final static String[] order = new String[] {
			"calories", "protein", "totalfats", "carbs", "fiber", 
			"sugar", "calcium", "iron", "magnesium", "potassium", "sodium", "zinc", "_vitamin C",
			"vitamin D", "vitamin B6", "vitamin B12", "vitamin A", "vitamin E", "vitamin K", "Saturated Fat", "Mononsaturated Fat", "Polyunsaturated Fat",
			"cholesterol" };

	public GraphView(Context context) {
		super(context);
		holder = getHolder();
		selectedItems = new ArrayList<Item>();
		selectedQuantities = new ArrayList<Integer>();
	}

	public GraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
		holder = getHolder();
		selectedItems = new ArrayList<Item>();
		selectedQuantities = new ArrayList<Integer>();
	}

	public GraphView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		holder = getHolder();
		selectedItems = new ArrayList<Item>();
		selectedQuantities = new ArrayList<Integer>();
	}

	public void run() {
		// TODO Auto-generated method stub
		while (isOk) {
			// drawing stuff goes here
			if (!holder.getSurface().isValid()) {
				continue;
			}

			canvas = holder.lockCanvas();
			canvas.drawARGB(255, 255, 255, 255);
			
			onDraw(canvas);
				
			holder.unlockCanvasAndPost(canvas);
		}
	}

	public void pause() {
		isOk = false;
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		t = null;
		
	}

	public void resume() {
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

		int base = h - 80;
		int topline = 60;
		int diff = base - topline;
		float interpolate = (float) diff / (float) _days;
		
		
		// to draw the lines:
		for (int i = 0; i < _days; i++) {
			float b = topline + (i * interpolate);
			c.drawLine(20, b, w - 20, b, grey);
		} 
		c.drawLine(20, topline, w - 20, topline, blue);	//top line
		c.drawLine(20, base, w - 20, base, black);		//bottom line
		
		determineMode();
		
		if (calorieRatio != 0 && MODE==SELECT_NONE) {
			int baseHeight = Math.round(calorieRatio * (float) diff);
			drawCurrentCartContent(c, base, baseHeight);
		} else if(calorieRatio!=0 && MODE==SELECT_SINGLE){
			int baseHeight = Math.round(calorieRatio * (float) diff);
			drawCurrentCartContent(c, base, baseHeight);
			
			float addedCals = getAddedContent(0);
			if(addedCals!=0){
					int addHeight = Math.round(addedCals * (float) diff);
					drawSingleMode(c, base, baseHeight, addHeight);
			} 
		} else if (MODE==SELECT_COMPARE){
			float baseCals = getAddedContent(0);
			float compareCals = getAddedContent(1);
			int baseHeight = Math.round(baseCals * (float) diff);
			int cBaseHeight = Math.round(compareCals * (float) diff);
			
			drawCompareMode(c, base, baseHeight, cBaseHeight);
		}
	}

	public void setDays(int days) {
		this._days = days;
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
	public void getRatios(PreviousHistory currentTotalCart, RecDailyValues currentRDV) {
		float currentCaloricContent = currentTotalCart.getCalories();
		float neededCaloricContent = currentRDV.getCalories();
		
		HashMap<String, Float> needs = new HashMap<String, Float>(order.length);
		HashMap<String, Float> ratios = new HashMap<String, Float>(order.length);
		
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
			
			Log.d("GraphView", "Need: " + n + "," + needs.get(n) + " || Ratio: " + ratios.get(n));
		}
	
		caloriesNeeded = (neededCaloricContent * (float) this._days);
		calorieRatio = (currentCaloricContent / caloriesNeeded);
	}
	
	/**
	 * Returns the height of the calories bar based on one item's index, taking into
	 * account the quantity.
	 * 
	 * @param index of the item in the cart
	 * @return
	 */
	public float getAddedContent(int index){
		float selectedCalories = selectedItems.get(index).getCalories();
		float addedContent = ((selectedCalories * (float) selectedQuantities.get(index)) / 
				caloriesNeeded );
		return addedContent;
	}

	private void drawCurrentCartContent(Canvas c, int base, int baseHeight) {
		Paint blue = new Paint();
		blue.setColor(Color.BLUE);
		
		Paint blackText = new Paint();
		blackText.setColor(Color.BLACK);
		blackText.isAntiAlias();
		blackText.setTextSize(22);

		Rect baseRect = new Rect(40, base - baseHeight, 100, base);
		c.drawRect(baseRect, blue);
		
		int halfBar = 30;
		
		String o = order[0];
		float len = blackText.measureText(o, 0, o.length());
		Log.d("GraphView", "len: " + len);
		c.drawText(o, 40 + (halfBar-(len/2)), base + 25, blackText);
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
	public void drawSingleMode(Canvas c, int base, int baseHeight,
			int addHeight) {
		// Bar Colors
		Paint grey = new Paint();
		grey.setColor(Color.LTGRAY);
		
		Rect addRect = new Rect(40, base - baseHeight, 100, base - baseHeight
				+ addHeight);
		c.drawRect(addRect, grey);
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
	public void drawCompareMode(Canvas c, int base, int baseHeight, int cBaseHeight){
		Paint orange = new Paint();
		orange.setColor(Color.YELLOW);
		Paint blue = new Paint();
		blue.setColor(Color.BLUE);

		Rect baseRect = new Rect(40, base - baseHeight, 100, base);
		Rect compareRect = new Rect(120, base - cBaseHeight, 180, base);
		
		c.drawRect(baseRect, blue);
		c.drawRect(compareRect, orange);
	}

}
