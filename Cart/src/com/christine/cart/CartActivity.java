package com.christine.cart;

import java.util.ArrayList;
import java.util.List;

import com.christine.cart.intentResult.IntentResult;
import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.AccountDatabaseHelper;
import com.christine.cart.sqlite.GroceryItem;
import com.christine.cart.sqlite.Item;
import com.christine.cart.sqlite.NutritionDatabaseHelper;
import com.christine.cart.sqlite.Person;
import com.christine.cart.sqlite.PreviousHistory;
import com.christine.cart.sqlite.RecDailyValues;
import com.christine.cart.visual.GraphLabelView;
import com.christine.cart.visual.GraphView;
import com.christine.cart.visual.NutritionAdvisor;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

public class CartActivity extends Activity {

	// UI Elements
	String results;
	Button viewItemList;
	Button checkout;
	Button test;
	Button searchItem;
	Button scanItem;
	Button handle;
	TextView added;
	TextView peopleDays;
	SlidingDrawer sd_itemlist;
	ListView sd_list;
	
	ActionBar actionBar;

	InputMethodManager manager;
	Context inputsContext;

	// for scan results
	private static String upcResults;
	private static String resultValue;
	private static String resultSize;
	private static String resultMessage;

	private static Intent passedIntent;

	// to start the graph view
	private static GraphView graph;
	private static GraphLabelView graphLabels;

	// information required to start the scan intent
	public static final String PACKAGE = "com.christine.cart";
	public static final String SCANNER = "com.google.zxing.client.android.SCAN";
	public static final String SCAN_FORMATS = "UPC_A,UPC_E,EAN_8,EAN_13,CODE_39,CODE_93,CODE_128";
	public static final String SCAN_MODE = "SCAN_MODE";
	public static final int REQUEST_CODE = 1;

	private static String currentUsername;
	private static Account act;
	private static AccountDatabaseHelper adb;
	private static NutritionDatabaseHelper ndb;
	private boolean onUPCResult = false;

	// information for graph settings
	private static int days;

	private static ArrayList<Item> selectedItems;
	private static ArrayList<Integer> quantities;
	private static List<GroceryItem> ccart;
	private static ArrayAdapter<String> ccartList;
	private static PreviousHistory pcart; // => previous cart totals
	private static RecDailyValues totalRDV;

	private static NutritionAdvisor advisor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart);

		// From the intent, read the number of days
		Intent passedIntent = getIntent();

		if (passedIntent != null) {
			// retrieve the account
			Account tempAccount = passedIntent.getParcelableExtra("account");

			if (tempAccount != null) {
				act = tempAccount;
				currentUsername = act.getName();
				int tdays = tempAccount.getDays();
				if (tdays != 0) {
					days = tdays;
				} else {
					// set days equal to 1 to prevent crashing
					days = 1;
				}
			} else {
				throw new RuntimeException(
						"CartActivity: account passed was null");
			}
		}
		
		//ActionBar
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Your Grocery Cart");
		actionBar.setHomeAction(new backToDashboardAction());
		actionBar.addAction(new toDaysAction());
		actionBar.addAction(new toPeopleAction());
		actionBar.addAction(new toCheckoutAction());

		// start the db
		adb = new AccountDatabaseHelper(this);
		ndb = new NutritionDatabaseHelper(this);

		// Set the number of days and start the graph view!
		graph = (GraphView) this.findViewById(R.id.graphview);
		graph.setDays(days);

		// Start the graph label view
		graphLabels = (GraphLabelView) this.findViewById(R.id.graphlabelview);

		// Start the nutrition advisor
		advisor = new NutritionAdvisor();
		
		added = (TextView) findViewById(R.id.tv_added);
		peopleDays = (TextView) findViewById(R.id.tv_cart_peopledays);
		
		//start the peopledays goal reminder
		int peopleNumber = adb.getPersonCountFor(currentUsername);
		if(days==1){
			peopleDays.setText("You're shopping for " + days + " day ");
		} else if(days>1) {
			peopleDays.setText("You're shopping for " + days + " days ");
		}
		if(peopleNumber==1){
			peopleDays.append("and " + peopleNumber + " person");
		} else if(peopleNumber>1) {
			peopleDays.append("and " + peopleNumber + " people.");
		}
		 
		// initiates the listview within the drawer
		sd_list = (ListView) findViewById(R.id.sd_list);
		sd_itemlist = (SlidingDrawer) findViewById(R.id.sd_itemlist);
		
		pcart = adb.getPreviousHistoryFor(currentUsername);
		adb.close();
		

		// Get the previous history for the user
		// if it exists, then pass it to Graph labels to draw goals
		if (pcart.getCalories() != 0.0f) {
			advisor.setPastCart(pcart);
		} else {
			Log.d("CartActivity",
					"There is no previous history yet for this cart. Username is: " + pcart.getUsername());
		}

		// Handles the PLU code
		searchItem = (Button) findViewById(R.id.btn_search);

		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputsContext = getApplicationContext();

		searchItem.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// opens up activity with a text entry and numpad
				Intent openSearchItemScreen = new Intent(CartActivity.this,
						InputSearchActivity.class);
				openSearchItemScreen.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				openSearchItemScreen.putExtra("account", act);
				startActivity(openSearchItemScreen);
			}
		}); // end searchItem

		// Handles the Barcode Scanning activity
		scanItem = (Button) findViewById(R.id.btn_scan);
		scanItem.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// start a scanner intent, using zxing library
				// refer back to PACKAGE settings
				Intent scanIntent = new Intent(SCANNER);
				scanIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				scanIntent.setPackage(PACKAGE);
				scanIntent.addCategory(Intent.CATEGORY_DEFAULT);
				scanIntent.putExtra("SCAN_FORMATS", SCAN_FORMATS);
				scanIntent.putExtra("SCAN_MODE", SCAN_MODE);
				try {
					startActivityForResult(scanIntent, REQUEST_CODE);
				} catch (ActivityNotFoundException e) {
					Toast eToast = Toast.makeText(CartActivity.this,
							"Activity Not Found!", Toast.LENGTH_LONG);
					eToast.setGravity(Gravity.TOP, 25, 400);
					eToast.show();
				}
			}
		});// end scanBarcode onClickListener
		

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		//get an updated version of the adb on resume each time
		adb = new AccountDatabaseHelper(this);
		ccart = adb.getAllGroceryItemsOf(currentUsername);
		int groceryCount = adb.getGroceryCountFor(currentUsername);
		added.setText(groceryCount + " ITEMS IN CART");
		adb.close();
		
		if (ccart != null) {
			setupItemDrawer();
		}
		
		passedIntent = getIntent();

		results = passedIntent.getStringExtra("results");
		int check = passedIntent.getIntExtra("check", 0);
		if (check == 1) {
			updateGraphWithSelected(results);
			// setup the listview if current cart is not null
		} 	
		
		PreviousHistory currentCart = getCartTotalsFor(currentUsername);
		totalRDV = getRDVTotalsFor(currentUsername);
		
		advisor.setCurrCart(currentCart);
		advisor.setRecDailyValues(totalRDV);
		advisor.setDays(days);
		
		if(pcart.getCalories()!=0.0f){
			graph.getRatiosWithPCart(currentCart, totalRDV, pcart);
		} else {
			graph.getRatiosWithoutPCart(currentCart, totalRDV);
		}
		graph.postInvalidate();

		graphLabels.setDays(days);
		graphLabels.postInvalidate();
		
		advisor.giveAdvice(this.getApplicationContext());

	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
	   return; //prevent back button from working
	}
	
	/**
	 * Set up actionbar home
	 */
	private class backToDashboardAction implements Action{
		public int getDrawable(){
			return R.drawable.ab_home_large;
		}
		
		public void performAction(View view){
			Intent startAgain = new Intent(CartActivity.this, DashboardActivity.class);
			startAgain.putExtra("account", act);
			startActivity(startAgain);
		}
	}
	
	/**
	 * ACTIONBAR DAYS
	 */
	private class toDaysAction implements Action{
		public int getDrawable(){
			return R.drawable.ab_date;
		}
		
		public void performAction(View view){
			Intent daysScreen = new Intent(CartActivity.this, SetupDaysActivity.class);
			daysScreen.putExtra("account", act);
			daysScreen.putExtra("cart", 1);
			startActivity(daysScreen);
		}
	}
	
	/**
	 * ACTIONBAR PEOPLE
	 */
	private class toPeopleAction implements Action{
		public int getDrawable(){
			return R.drawable.ab_people;
		}
		
		public void performAction(View view){
			Intent peopleScreen = new Intent(CartActivity.this, SetupPeopleActivity.class);
			peopleScreen.putExtra("account", act);
			startActivity(peopleScreen);
		}
	}
	
	/**
	 * ACTIONBAR CHECKOUT
	 */
	private class toCheckoutAction implements Action{
		public int getDrawable(){
			return R.drawable.ab_checkout_large;
		}
		
		public void performAction(View view){
			PreviousHistory cTotals = getCartTotalsFor(currentUsername);
			if(cTotals.getCalories() != 0) {
				showCheckoutDialog(cTotals);
			} else {
				Toast noItemsInCart = Toast.makeText(CartActivity.this, "You have no items in your cart yet!", Toast.LENGTH_SHORT);
				noItemsInCart.show();
			}
		}
	}
	

	/**
	 * ACTIVITY RESULTS FROM SEARCH AND SCAN
	 */
	public void onActivityResult(int requestCode, int resultCode,
			Intent scanIntent) {
		IntentResult scanResult = parseActivityResult(requestCode, resultCode,
				scanIntent);
		String contents = scanResult.getContents();

		if(resultCode == RESULT_OK){
			Intent startInputScanActivity = new Intent(CartActivity.this, InputScanActivity.class);
			startInputScanActivity.putExtra("contents", contents);
			startInputScanActivity.putExtra("account", act);
			startActivity(startInputScanActivity);
		} else {
			Toast toast = Toast.makeText(inputsContext, "Scan Failed",
					resultCode);
			toast.setGravity(Gravity.TOP, 25, 400);
			toast.show();
		}

	}

	/**
	 * ACTIVITY RESULT FROM SEARCH AND SCAN
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param intent
	 * @return
	 */
	public static IntentResult parseActivityResult(int requestCode,
			int resultCode, Intent intent) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

				return new IntentResult(contents, format);
			} else {
				// Handle cancel
				return new IntentResult(null, null);
			}
		}
		return new IntentResult(null, null);
	}// end IntentResult

	/**
	 * @param username
	 * @return Fetch all of the people for one user If null, then request that
	 *         user check preferences
	 * 
	 */
	public static String getAllPeopleDescFor(String username) {
		List<Person> p = adb.getAllPeopleFor(username);
		String allPeopleDesc = new String();
		if (p != null) {
			for (int i = 0; i < p.size(); i++) {
				Person temp = p.get(i);
				allPeopleDesc = allPeopleDesc + "\n" + temp.returnString();
			}
		} else {
			adb.close();
			return "No one has been added to Cart yet. Please check preferences.";
		}

		adb.close();
		return allPeopleDesc;
	}

	/**
	 * TOTAL RDR Get the total values of all of the RecDailyValues
	 * 
	 */
	public static RecDailyValues getRDVTotalsFor(String username) {
		List<Person> p = adb.getAllPeopleFor(username);
		List<RecDailyValues> rdvList = new ArrayList<RecDailyValues>();
		if (p != null) {
			for (int i = 0; i < p.size(); i++) {
				RecDailyValues tempRDV = new RecDailyValues(p.get(i));
				rdvList.add(tempRDV);
			}
		} else {
			adb.close();
			return null;
		}

		adb.close();
		RecDailyValues total = getTotalRDVOf(rdvList);
		return total;
	}

	/**
	 * TOTAL VALUES Add up all of the values of the current_cart items
	 * 
	 */
	public PreviousHistory getCartTotalsFor(String username) {
		NutritionDatabaseHelper ndb = new NutritionDatabaseHelper(this);
		AccountDatabaseHelper adb = new AccountDatabaseHelper(this);

		List<GroceryItem> allGItems = adb.getAllGroceryItemsOf(username);
		PreviousHistory cartTotals = new PreviousHistory();
		if (allGItems != null) {
			cartTotals.setId(-1);
			cartTotals.setUsername(username);

			for (int i = 0; i < allGItems.size(); i++) {
				// retrieve the grocery item from the array
				GroceryItem tempGrocery = allGItems.get(i);

				// get the Quantity of the item
				int quantity = tempGrocery.getQuantity();

				// locate the item in the nutrition database
				Item tempItem = ndb.getItem(tempGrocery.getItemName());

				// add the totals to the current cartTotal, remembering
				// to multiply by the quantity!
				cartTotals.setCalories(cartTotals.getCalories()
						+ (tempItem.getCalories() * quantity));
				cartTotals.setProtein(cartTotals.getProtein()
						+ (tempItem.getProtein() * quantity));
				cartTotals.setFat(cartTotals.getFat()
						+ (tempItem.getFat() * quantity));
				cartTotals.setCarbohydrate(cartTotals.getCarbohydrate()
						+ (tempItem.getCarbohydrate() * quantity));
				cartTotals.setFiber(cartTotals.getFiber()
						+ (tempItem.getFiber() * quantity));
				cartTotals.setSugar(cartTotals.getSugar()
						+ (tempItem.getSugar() * quantity));
				cartTotals.setCalcium(cartTotals.getCalcium()
						+ (tempItem.getCalcium() * quantity));
				cartTotals.setIron(cartTotals.getIron()
						+ (tempItem.getIron() * quantity));
				cartTotals.setMagnesium(cartTotals.getMagnesium()
						+ (tempItem.getMagnesium() * quantity));
				cartTotals.setPotassium(cartTotals.getPotassium()
						+ (tempItem.getPotassium() * quantity));
				cartTotals.setSodium(cartTotals.getSodium()
						+ (tempItem.getSodium() * quantity));
				cartTotals.setZinc(cartTotals.getZinc()
						+ (tempItem.getZinc() * quantity));
				cartTotals.setVitC(cartTotals.getVitC()
						+ (tempItem.getVitC() * quantity));
				cartTotals.setVitB6(cartTotals.getVitB6()
						+ (tempItem.getVitB6() * quantity));
				cartTotals.setVitB12(cartTotals.getVitB12()
						+ (tempItem.getVitB12() * quantity));
				cartTotals.setVitA(cartTotals.getVitA()
						+ (tempItem.getVitA() * quantity));
				cartTotals.setVitE(cartTotals.getVitE()
						+ (tempItem.getVitE() * quantity));
				cartTotals.setVitD(cartTotals.getVitD()
						+ (tempItem.getVitD() * quantity));
				cartTotals.setVitK(cartTotals.getVitK()
						+ (tempItem.getVitK() * quantity));
				cartTotals.setFatSat(cartTotals.getFatSat()
						+ (tempItem.getFatSat() * quantity));
				cartTotals.setFatMono(cartTotals.getFatMono()
						+ (tempItem.getFatMono() * quantity));
				cartTotals.setFatPoly(cartTotals.getFatPoly()
						+ (tempItem.getFatPoly() * quantity));
				cartTotals.setCholesterol(cartTotals.getCholesterol()
						+ (tempItem.getCholesterol() * quantity));
				cartTotals.setDays(-1);
			}

			// Log.d("Created: ", "Cart Total for : " + cartTotals.getUsername()
			// + "Total Calories: " + cartTotals.getCalories());
			adb.close();
			ndb.close();
			return cartTotals;
		}
		ndb.close();
		adb.close();
		PreviousHistory emptyHistory = new PreviousHistory();
		emptyHistory.setUsername(currentUsername);
		return emptyHistory;
	}

	/**
	 * @param username
	 * @return A string of all of the required values of one entity the user has
	 *         specified
	 */
	public static String getStringRDVTotalsFor(String username) {
		List<Person> p = adb.getAllPeopleFor(username);
		List<RecDailyValues> rdvList = new ArrayList<RecDailyValues>();
		if (p != null) {
			for (int i = 0; i < p.size(); i++) {
				RecDailyValues tempRDV = new RecDailyValues(p.get(i));
				rdvList.add(tempRDV);
			}
		} else {
			adb.close();
			return "No one has been added to Cart yet. Please check preferences.";
		}

		adb.close();
		RecDailyValues total = getTotalRDVOf(rdvList);
		return total.returnString();
	}

	/**
	 * @param rdvList
	 * @return RecDailyValue Contains all of the totaled values for the group of
	 *         people the user has set
	 */
	public static RecDailyValues getTotalRDVOf(List<RecDailyValues> rdvList) {

		RecDailyValues total = new RecDailyValues();

		for (int i = 0; i < rdvList.size(); i++) {
			// create a temporary RDV object
			RecDailyValues temp = rdvList.get(i);

			// set all of the properties based on the current and the sum of the
			total.setCalories(total.getCalories() + temp.getCalories());
			total.setProtein((total.getProtein() + temp.getProtein()));
			total.setFat((total.getFat() + temp.getFat()));
			total.setCarbohydrate((total.getCarbohydrate() + temp
					.getCarbohydrate()));
			total.setFiber((total.getFiber() + temp.getFiber()));
			total.setSugar((total.getSugar() + temp.getSugar()));
			total.setCalcium((total.getCalcium() + temp.getCalcium()));
			total.setIron((total.getIron() + temp.getIron()));
			total.setMagnesium((total.getMagnesium() + temp.getMagnesium()));
			total.setPotassium((total.getPotassium() + temp.getPotassium()));
			total.setSodium((total.getSodium() + temp.getSodium()));
			total.setZinc((total.getZinc() + temp.getZinc()));
			total.setVitC((total.getVitC() + temp.getVitC()));
			total.setVitB6((total.getVitB6() + temp.getVitB6()));
			total.setVitB12((total.getVitB12() + temp.getVitB12()));
			total.setVitA((total.getVitA() + temp.getVitA()));
			total.setVitE((total.getVitE() + temp.getVitE()));
			total.setVitD((total.getVitD() + temp.getVitD()));
			total.setVitK((total.getVitK() + temp.getVitK()));
			total.setFatSat((total.getFatSat() + temp.getFatSat()));
			total.setFatPoly((total.getFatPoly() + temp.getFatPoly()));
			total.setCholesterol((total.getCholesterol() + temp
					.getCholesterol()));
		}
		
		return total;
	}
	
	/**
	 * UPDATE GRAPH
	 * when results are returned
	 */
	void updateGraphWithSelected(String results){
		if (results.equals(null) || results.equals("e")) {
			Toast noMatch = Toast.makeText(inputsContext,
					"We couldn't find that item! Please try again.",
					Toast.LENGTH_LONG);
			noMatch.setGravity(Gravity.TOP | Gravity.LEFT, 0, 150);
			noMatch.show();
		} else {
			ndb = new NutritionDatabaseHelper(this);
	
			if (selectedItems != null || quantities != null) {
				selectedItems.clear();
				quantities.clear();
			} else {
				selectedItems = new ArrayList<Item>();
				quantities = new ArrayList<Integer>();
			}
	
			int position = -1;
			for (int i = 0; i < ccartList.getCount(); i++) {
				String tempItemName = ccartList.getItem(i);
				int pos = tempItemName.indexOf(" ");
				String compare = tempItemName.substring(pos + 1);
	
				if (compare.equals(results)) {
					position = i;
					break;
				} else {
					continue;
				}
			}
	
			Item selectedItem = ndb.getItem(results);
			ndb.close();
			
			selectedItems.add(selectedItem);
			String one = "<font color='#7EAD1A'>" + selectedItem.getItemName() +"</font> SELECTED";
			added.setText(Html.fromHtml(one));

	
			for (GroceryItem gItem : ccart) {
				if (gItem.getItemName().equals(results)) {
					quantities.add(gItem.getQuantity());
					Log.d("CartActivity: ", "Name: " + gItem.getItemName()
							+ quantities.get(0));
				}
			}
	
			Log.d("CartActivity", "Position: " + position);
			sd_list.setItemChecked(position, true);
	
			graph.passSelectedItems(selectedItems);
			graph.passSelectedQuantities(quantities);
			
			graph.postInvalidate();
	
			graphLabels.setDays(days);
			graphLabels.postInvalidate();
		}
	}
	
	/**
	 * Setup Sliding Drawer with List
	 */
	public void setupItemDrawer(){
		ccartList = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice);
		for (int i = 0; i < ccart.size(); i++) {
			GroceryItem temp = ccart.get(i);
			ccartList.add(String.valueOf(temp.getQuantity()) + " "
					+ temp.getItemName());
		}

		// set properties of the list view and populate the listview
		sd_list.setAdapter(ccartList);
		sd_list.setBackgroundColor(Color.WHITE);
		sd_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		sd_list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SparseBooleanArray listItems = new SparseBooleanArray();
				listItems.clear();
				listItems = sd_list.getCheckedItemPositions();
				
				adb = new AccountDatabaseHelper(CartActivity.this);
				int groceryCount = adb.getGroceryCountFor(currentUsername);
				adb.close();
				
				added.setText(groceryCount + " ITEMS IN CART");
				
				if (selectedItems != null || quantities != null) {
					selectedItems.clear();
					quantities.clear();
				} else {
					selectedItems = new ArrayList<Item>();
					quantities = new ArrayList<Integer>();
				}
				
				boolean isSelected = false;
				for (int i = 0; i < ccartList.getCount(); i++) {
					 isSelected = listItems.get(i);

					if (isSelected) {
						String tempItemName = ccartList.getItem(i);
						int pos = tempItemName.indexOf(" ");
						String selectedItemName = tempItemName
								.substring(pos + 1);
						
						Item selectedItem = ndb.getItem(selectedItemName);
						
						selectedItems.add(selectedItem);
						for (GroceryItem gItem : ccart) {
							if (gItem.getItemName()
									.equals(selectedItemName)) {
								quantities.add(gItem.getQuantity());
							}
						}	
					}
				}
				
				if(selectedItems.size()==1){
					String one = "<font color='#7EAD1A'>" + selectedItems.get(0).getItemName() +"</font> SELECTED";
					added.setText(Html.fromHtml(one));

				} else if(selectedItems.size()==2){
					String two = "COMPARE <font color='#7EAD1A'>" + selectedItems.get(0).getItemName() +"</font>"
							+ " VS <font color='#E57716'>" + selectedItems.get(1).getItemName() +"</font>";
					added.setText(Html.fromHtml(two));
					
				} else if (selectedItems.size() == 3) {
					String tempItemName = ccartList.getItem(position);
					int pos = tempItemName.indexOf(" ");
					String toggledItemName = tempItemName
							.substring(pos + 1);

					Item iToRemove = null;
					for (Item item : selectedItems) {
						if (item.getItemName().equals(toggledItemName)) {
							iToRemove = item;
						}
					}
					selectedItems.remove(iToRemove);
					Log.d("CartActivity",
							"Removed: " + iToRemove.getItemName());
					sd_list.setItemChecked(position, false);
				} 
				
				graph.passSelectedItems(selectedItems);
				graph.passSelectedQuantities(quantities);

			}
		});
	
		
		// for the sliding drawer in footer activity
		// populates controls the sliding drawer
		handle = (Button) findViewById(R.id.btn_handle);
		sd_itemlist.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {
				handle.setBackgroundDrawable(getResources().getDrawable(R.drawable.cart_tab_down));
			}
		});
		sd_itemlist.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			public void onDrawerClosed() {
				handle.setBackgroundDrawable(getResources().getDrawable(R.drawable.cart_tab));
			}
		});
	}
	/**
	 * CHECKOUT ALERT DIAOLOG
	 */
	private void showCheckoutDialog(final PreviousHistory cartTotals){
		Log.d("CartActivity", "Checkout Dialog");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Just to be sure, are you 100% done shopping? You won't be able to come back to this cart after you checkout!");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   Intent goCheckout = new Intent(CartActivity.this,
								SummaryActivity.class);
						goCheckout.putExtra("cartTotals", cartTotals); // pass the
																	// parceable!
						goCheckout.putExtra("account", act);
						goCheckout.putExtra("days", days);
						goCheckout.putExtra("rdv", totalRDV);
						goCheckout.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						startActivity(goCheckout);
		           }
		       });
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog endCart = builder.create();
		endCart.show();
	}

}