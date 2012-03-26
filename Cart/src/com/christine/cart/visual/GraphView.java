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
		int graphHeight = base - topline;
		float interpolate = (float) graphHeight / (float) _days;
		
		
		// to draw the lines:
		for (int i = 0; i < _days; i++) {
			float b = topline + (i * interpolate);
			c.drawLine(20, b, w - 20, b, grey);
		} 
		c.drawLine(20, topline, w - 20, topline, blue);	//top line
		c.drawLine(20, base, w - 20, base, black);		//bottom line
		
		determineMode();
		
		if (calorieRatio!= 0 && MODE==SELECT_NONE) {
			
			drawCurrentCartContent(c, base, graphHeight);
			
		} else if(calorieRatio!=0 && MODE==SELECT_SINGLE){
			
			int baseHeight = Math.round(calorieRatio * (float) graphHeight);
			drawCurrentCartContent(c, base, graphHeight);

			drawSingleMode(c, base, graphHeight);
			
		} else if (MODE==SELECT_COMPARE){
			/*float baseCals = getAddedNutrition(0);
			float compareCals = getAddedNutrition(1);
			int baseHeight = Math.round(baseCals * (float) graphHeight);
			int cBaseHeight = Math.round(compareCals * (float) graphHeight);
			
			drawCompareMode(c, base, baseHeight, cBaseHeight);*/
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
		Paint blue = new Paint();
		blue.setColor(Color.BLUE);
		
		Paint blackText = new Paint();
		blackText.setColor(Color.BLACK);
		blackText.isAntiAlias();
		blackText.setTextSize(22);
		
		// WIDTH
		int startBar = 40;
		int endBar = 100;
		int halfBar = (endBar - startBar)/2;
		
		for(int i=0; i<order.length; i++){
			int spacing = 115*i;
			
			// TEXT
			String o = order[i];
			float len = blackText.measureText(o, 0, o.length());
			c.drawText(o, (startBar+spacing + (halfBar-(len/2))), base + 25, blackText);
			
			// BAR
			int barHeight = Math.round(ratios.get(o) * (float) graphHeight);
			
			Rect baseRect = new Rect((startBar+spacing), base - barHeight, (endBar+spacing), base);
			c.drawRect(baseRect, blue);
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
		Paint grey = new Paint();
		grey.setColor(Color.LTGRAY);
		
		ArrayList<Float> selectedNutrition = getAddedNutrition(0);
		
		// WIDTH
		int startBar = 40;
		int endBar = 100;
		
		for(int i=0; i<selectedNutrition.size(); i++){
			int spacing = 115*i;
			int barHeight = Math.round(ratios.get(order[i]) * (float) graphHeight);
			int addHeight = Math.round(selectedNutrition.get(i) * (float) graphHeight);
			
			Rect addRect = new Rect((startBar+spacing), base - barHeight, (endBar+spacing), base - barHeight
					+ addHeight);
			c.drawRect(addRect, grey);
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
