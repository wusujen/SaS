package com.christine.cart;

import java.util.ArrayList;
import java.util.List;

import com.christine.cart.sqlite.Account;
import com.christine.cart.sqlite.PreviousHistory;
import com.christine.cart.sqlite.RecDailyValues;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NutrientActivity extends Activity {

	TextView nutrientTitle;
	TextView nutrientDesc;
	TextView nutrientPercent;
	ListView nutrients;

	private static ActionBar actionBar;
	private static Account act;
	private static PreviousHistory pcart;
	private static RecDailyValues rdvTotals;
	private static Integer[] percent;
	private static Float[] pcartNums;
	private static Float[] rdvNums;

	private final static String[] reduced = new String[] { "calories",
			"protein", "total fats", "carbohydrates", "fiber", "sugar",
			"calcium", "iron", "magnesium", "potassium", "sodium", "zinc",
			"vitamin C", "vitamin D", "vitamin B6", "vitamin B12", "vitamin A",
			"vitamin E", "vitamin K", "saturated fats", "cholesterol" };

	private static String[] nutrientDescriptions;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);

		// ActionBar
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Learn About Nutrients");
		actionBar.setHomeAction(new backToDashboardAction());

		Intent dashboard = getIntent();
		act = dashboard.getParcelableExtra("account");
		pcart = dashboard.getParcelableExtra("pcart");
		rdvTotals = dashboard.getParcelableExtra("rdvTotals");

		nutrientTitle = (TextView) findViewById(R.id.tv_nutrient_title);
		nutrientDesc = (TextView) findViewById(R.id.tv_nutrient_desc);
		nutrientPercent = (TextView) findViewById(R.id.tv_nutrient_percent);
		nutrients = (ListView) findViewById(R.id.lv_nutrients);
		
		percent = new Integer[reduced.length];
		pcartNums = new Float[reduced.length];
		rdvNums = new Float[reduced.length];
		
		nutrientPercent.setVisibility(View.GONE);
		if (pcart != null && pcart.getCalories() != 0.0f && rdvTotals != null
				&& rdvTotals.getCalories() != 0.0f) {
			nutrientPercent.setVisibility(View.VISIBLE);
			pcartNums = pcart.getNutritionPropertiesNoMono();
			rdvNums = rdvTotals.getNutritionNeedsNoMono();
			Log.d("HistoryActivity", "pcartNums " + pcartNums[0]);
			Log.d("HistoryActivity", "rdvNums " + rdvNums[0]);

			// get the percentages of nutrients fulfilled
			for (int i = 0; i < pcartNums.length; i++) {
				if (rdvNums[i] != 0.0f) {
					percent[i] = Math.round((pcartNums[i] / (rdvNums[i]* (float) pcart.getDays()) * (float) 100));
				} else {
					percent[i] = 0;
				}
			}
		} else {
			nutrientPercent.setVisibility(View.GONE);
		}

		setupNutrientList();
	}

	/**
	 * Set up actionbar home
	 */
	private class backToDashboardAction implements Action {
		public int getDrawable() {
			return R.drawable.ab_home_large;
		}

		public void performAction(View view) {
			Intent startAgain = new Intent(NutrientActivity.this,
					DashboardActivity.class);
			startAgain.putExtra("account", act);
			startActivity(startAgain);
		}
	}

	/**
	 * Set up nutrient list
	 */
	private void setupNutrientList() {
		ArrayAdapter<String> nutrientList = new ArrayAdapter<String>(
				NutrientActivity.this, R.layout.history_list_item);
		for (int i = 0; i < reduced.length; i++) {
			nutrientList.add(reduced[i].toUpperCase());
		}
		nutrients.setAdapter(nutrientList);
		nutrients.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		nutrients.setItemChecked(0, true);
		
		setupNutritionDescriptions();
		showDescText(0);
		
		nutrients.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				showDescText(position);
			}
		});
	}
	
	/**
	 * Show description text
	 * 
	 * @see http://jtomlinson.blogspot.com/2010/03/textview-and-html.html
	 */
	public void showDescText(int position){
		String n = reduced[position];
		nutrientTitle.setText(n.toUpperCase());
		nutrientDesc.setText(nutrientDescriptions[position]);
		
		//create link
		String reference = "<br /><br /><font color='#aaaaaa'>Information procured from the <br /><a href='http://fnic.nal.usda.gov/nal_display/index.php?info_" +
				"center=4&tax_level=3&tax_subject=274&topic_id=1323&level3_id=5145'>USDA (United States Department of Agriculture)</font></a>";
		nutrientDesc.append(Html.fromHtml(reference));
		nutrientDesc.setMovementMethod(LinkMovementMethod.getInstance());
		
		if (percent != null) {
			nutrientPercent.setText("PREVIOUS CART HAD "
					+ percent[position] + "% FOR " + act.getDays()
					+ " DAYS.");
		} else {
			nutrientPercent.setVisibility(View.GONE);
		}
	}
	
	/**
	 * ALL DESCRIPTION TEXT
	 */
	private void setupNutritionDescriptions() {
		nutrientDescriptions = new String[reduced.length];
		nutrientDescriptions[0] = // => calories
		"A Calorie is the amount of heat needed to raise the temperature of a liter of water 1 degree. Sure, it was hard to understand when your science teacher explained it."
				+ " Relax. It is just a scientific way to measure energy. That said, what do you need to know about calories? Just a few things: Think about what you regularly "
				+ "eat, what your calorie needs are, and how to count calories. It takes approximately 3,500 calories below your calorie needs to lose a pound of body fat. It "
				+ "takes approximately 3,500 calories above your calorie needs to gain a pound.";

		nutrientDescriptions[1] = // => protein
		"Protein is found in the following foods: meats, poultry, and fish; legumes (dry beans and peas); tofu; eggs; nuts and seeds; milk and milk products;"
				+ "grains, some vegetables, and some fruits (provide only small amounts of protein relative to other sources). \n \n"
				+ "As we mentioned, most adults in the United States get more than enough protein to meet their needs. It's rare for someone "
				+ "who is healthy and eating a varied diet to not get enough protein. \n \nProteins are made up of amino acids. Think of amino acids "
				+ "as the building blocks. There are 20 different amino acids that join together to make all types of protein. Some of these amino acids"
				+ " can't be made by our bodies, so these are known as essential amino acids. It's essential that our diet provide these. \n\nIn the diet, protein"
				+ " sources are labeled according to how many of the essential amino acids they provide. A complete protein source is one that provides all of "
				+ "the essential amino acids. You may also hear these sources called high quality proteins. For example, meat, poultry, fish, milk, eggs, "
				+ "and cheese are considered complete protein sources.\n\nAn incomplete protein source is one that is low in one or more of the essential"
				+ " amino acids. Complementary proteins are two or more incomplete protein sources that together provide adequate amounts of all the essential "
				+ "amino acids.";

		nutrientDescriptions[2] = // => total fats
		"Dietary fats are found in both plant and animal foods. Fats supply calories and essential fatty acids, "
				+ "and help in the absorption of the fat-soluble vitamins A, D, E, and K. Fatty acids are categorized as being saturated, "
				+ "monounsaturated, or polyunsaturated. Fats contain a mixture of these different kinds of fatty acids. Trans "
				+ "fatty acids are unsaturated fatty acids. However, they are structurally different from the predominant "
				+ "unsaturated fatty acids that occur naturally in plant foods and have dissimilar health effects. ";

		nutrientDescriptions[3] = // => carbohydrates
		"Your body uses carbohydrates (carbs) to make glucose which is the fuel that gives you energy and helps keep everything going. Your body can use "
				+ "glucose immediately or store it in your liver and muscles for when it is needed. You can find carbohydrates in the following: fruits, vegetables,"
				+ "breads, cereals, other grains, milk and milk products and foods containing added sugars (e.g., cakes, cookies, and sugar-sweetened beverages). \n \n"
				+ "Healthier foods higher in carbohydrates include ones that provide dietary fiber and whole grains as well as those without added sugars. What about "
				+ "foods higher in carbohydrates such as sodas and candies that also contain added sugars? Those are the ones that add extra calories "
				+ "but not many nutrients to your diet. \n\nYour best approach is to follow a meal plan that gives you 45% to 65% of the calories as carbohydrates.";

		nutrientDescriptions[4] = // => fiber
		"You may have seen dietary fiber on the label listed as soluble fiber or insoluble fiber. Soluble fiber is found in the following: Oatmeal; Oat bran;"
				+ "Nuts and seeds; Most fruits (e.g., strawberries, blueberries, pears, and apples); Dry beans and peas. Insoluble fiber found in the following: "
				+ "Whole wheat bread; Barley; Brown rice; Couscous; Bulgur or whole grain cereals; Wheat bran; Seeds; Most vegetables; Fruits. \n\n"
				+ "Which type is best? Both! Each has important health benefits so eat a variety of these foods to get enough of both. You're "
				+ "also more likely to get other nutrients that you might miss if you just chose 1 or 2 high-fiber foods. It's recommended that you get 14 grams "
				+ "of dietary fiber for every 1,000 calories that you consume each day. If you need 2,000 calories each day, you should try to include 28 grams of dietary fiber.";

		nutrientDescriptions[5] = // => sugar
		"Simple carbohydrates include sugars found naturally in foods such as fruits, vegetables milk, and milk products. Simple carbohydrates also include sugars "
				+ "added during food processing and refining.4 What's the difference? In general, foods with added sugars have fewer nutrients than foods with "
				+ "naturally-occurring sugars. If you see any of these in the ingredient list, you know the food has added sugars."
				+ " The closer to the top of the list, the more of that sugar is in the food. ";

		nutrientDescriptions[6] = // => calcium
		"Calcium, the most abundant mineral in the body, is found in some foods, added to others, available as a dietary supplement, and present in some "
				+ "medicines (such as antacids). Calcium is required for vascular contraction and vasodilation, muscle function, nerve transmission, intracellular"
				+ "calcium is very tightly regulated and does not fluctuate with changes in dietary intakes; the body uses bone tissue as a reservoir for, and source "
				+ "of calcium, to maintain constant concentrations of calcium in blood, muscle, and intercellular fluids. \n\nThe remaining 99% of the body's calcium "
				+ "supply is stored in the bones and teeth where it supports their structure and function. Bone itself undergoes continuous remodeling, with constant"
				+ " resorption and deposition of calcium into new bone. The balance between bone resorption and deposition changes with age. Bone formation exceeds "
				+ "resorption in periods of growth in children and adolescents, whereas in early and middle adulthood both processes are relatively equal. "
				+ "In aging adults, particularly among postmenopausal women, bone breakdown exceeds formation, resulting in bone loss that increases the risk of "
				+ "osteoporosis over time. \n\nMilk, yogurt, and cheese are rich natural sources of calcium and are the major food contributors of this nutrient to "
				+ "people in the United States [1]. Nondairy sources include vegetables, such as Chinese cabbage, kale, and broccoli. Spinach provides calcium, but"
				+ " its bioavailability is poor. Most grains do not have high amounts of calcium unless they are fortified; however, they contribute calcium to the "
				+ "diet because they contain small amounts of calcium and people consume them frequently. Foods fortified with calcium include many fruit juices and "
				+ "drinks, tofu, and cereals.";

		nutrientDescriptions[7] = // => iron
		"Iron, one of the most abundant metals on Earth, is essential to most life forms and to normal human physiology. Iron is an integral part of many proteins"
				+ " and enzymes that maintain good health. In humans, iron is an essential component of proteins involved in oxygen transport. It is also essential for "
				+ "the regulation of cell growth and differentiation. A deficiency of iron limits oxygen delivery to cells, resulting in fatigue, poor work performance, "
				+ "and decreased immunity. On the other hand, excess amounts of iron can result in toxicity and even death. \n\nThere are two forms of dietary iron: "
				+ "heme and nonheme. Heme iron is derived from hemoglobin, the protein in red blood cells that delivers oxygen to cells. Heme iron is found in animal "
				+ "foods that originally contained hemoglobin, such as red meats, fish, and poultry. Iron in plant foods such as lentils and beans is arranged in a "
				+ "chemical structure called nonheme iron. This is the form of iron added to iron-enriched and iron-fortified foods. Heme iron is absorbed better "
				+ "than nonheme iron, but most dietary iron is nonheme iron. A variety of heme and nonheme sources of iron are listed in Tables 1 and 2.";

		nutrientDescriptions[8] = // => magnesium
		"Magnesium is the fourth most abundant mineral in the body and is essential to good health. Approximately 50% of total body magnesium is found in bone."
				+ " The other half is found predominantly inside cells of body tissues and organs. Only 1% of magnesium is found in blood, but the body works very hard "
				+ "to keep blood levels of magnesium constant. Magnesium is needed for more than 300 biochemical reactions in the body. It helps maintain normal muscle "
				+ "and nerve function, keeps heart rhythm steady, supports a healthy immune system, and keeps bones strong. Magnesium also helps regulate blood sugar "
				+ "levels, promotes normal blood pressure, and is known to be involved in energy metabolism and protein synthesis. There is an increased interest "
				+ "in the role of magnesium in preventing and managing disorders such as hypertension, cardiovascular disease, and diabetes. Dietary magnesium is absorbed "
				+ "in the small intestines. Magnesium is excreted through the kidneys. \n\nGreen vegetables such as spinach are good sources of magnesium because the "
				+ "center of the chlorophyll molecule (which gives green vegetables their color) contains magnesium. Some legumes (beans and peas), nuts and seeds, and "
				+ "whole, unrefined grains are also good sources of magnesium. Refined grains are generally low in magnesium. When white flour is refined and "
				+ "processed, the magnesium-rich germ and bran are removed. Bread made from whole grain wheat flour provides more magnesium than bread made from white "
				+ "refined flour. Tap water can be a source of magnesium, but the amount varies according to the water supply. Water that naturally contains more minerals "
				+ "is described as \'hard\'. \'Hard\' water contains more magnesium than \'soft\' water. Eating a wide variety of legumes, nuts, whole grains, and vegetables"
				+ " will help you meet your daily dietary need for magnesium.";

		nutrientDescriptions[9] = // => potassium
		"Potassium is a mineral involved in electrical and cellular body functions. In the body, potassium is classified as an electrolyte. Potassium is a very important"
				+ " mineral to the human body. It has various roles in metabolism and body functions and is essential for the proper function of all cells, tissues, and organs:"
				+ " It assists in the regulation of the acid-base balance. It assists in protein synthesis from amino acids and in carbohydrate metabolism."
				+ "It is necessary for the building of muscle and for normal body growth. It is essential for the normal electrical activity of the heart. \n\n"
				+ " Many foods contain potassium. All meats (red meat and chicken) and fish such as salmon, cod, flounder, and sardines are good sources of potassium."
				+ " Soy products and veggie burgers are also good sources of potassium. Vegetables including broccoli, peas, lima beans, tomatoes, potatoes "
				+ "(especially their skins), sweet potatoes, and winter squashes are all good sources of potassium. Fruits that contain significant sources of potassium "
				+ " include citrus fruits, cantaloupe, bananas, kiwi, prunes, and apricots. Dried apricots contain more potassium than fresh apricots. Milk and yogurt, "
				+ " as well as nuts, are also excellent sources of potassium. People on dialysis for kidney failure should avoid consuming too many of these potassium-rich "
				+ " foods. These people require specialized diets to avoid excess potassium in the blood.";

		nutrientDescriptions[10] = // => sodium
		"Sodium is an essential nutrient and is needed by the body in relatively small quantities, provided that "
				+ "substantial sweating does not occur. On average, the higher an individual\'s sodium intake, "
				+ "the higher the individual\'s blood pressure. \n \n"
				+ "Reduce sodium intake by: \n"
				+ "� Read the Nutrition Facts label for information on the sodium content of foods and purchase foods that are low in sodium. \n"
				+ "� Consume more fresh foods and fewer processed foods that are high in sodium. \n"
				+ "� Eat more home-prepared foods, where you have more control over sodium, and use little or no salt or saltcontaining seasonings"
				+ " when cooking or eating foods. \n� When eating at restaurants, ask that salt not be added"
				+ " to your food or order lower sodium options, if available. ";

		nutrientDescriptions[11] = // => zinc
		"Zinc is a nutrient that people need to stay healthy. Zinc is found in cells throughout the body. It helps the immune system fight"
				+ " off invading bacteria and viruses. The body also needs zinc to make proteins and DNA, the genetic material in all cells. During pregnancy,"
				+ " infancy, and childhood, the body needs zinc to grow and develop properly. Zinc also helps wounds heal and is important for proper senses of"
				+ "taste and smell. \n\nZinc is found in a wide variety of foods. You can get recommended amounts of zinc by eating a variety of foods including the "
				+ "following: Oysters, which are the best source of zinc; Red meat, poultry, seafood such as crab and lobsters, and fortified breakfast cereals,"
				+ " which are also good sources of zinc; Beans, nuts, whole grains, and dairy products, which provide some zinc.";

		nutrientDescriptions[12] = // => vitamin c
		"Vitamin C, also known as ascorbic acid, is a water-soluble nutrient found in some foods. In the body, it acts as an antioxidant, helping to "
				+ "protect cells from the damage caused by free radicals. Free radicals are compounds formed when our bodies convert the food we eat into energy."
				+ " People are also exposed to free radicals in the environment from cigarette smoke, air pollution, and ultraviolet light from the sun."
				+ " The body also needs vitamin C to make collagen, a protein required to help wounds heal. In addition, vitamin C improves the absorption of "
				+ "iron from plant-based foods and helps the immune system work properly to protect the body from disease."
				+ "\n\n Fruits and vegetables are the best sources of vitamin C. "
				+ "You can get recommended amounts of vitamin C by eating a variety of foods including the following: Citrus fruits (such as oranges and grapefruit) and their juices"
				+ ", as well as red and green pepper and kiwifruit, which have a lot of vitamin C. Other fruits and vegetables�such as broccoli, strawberries, cantaloupe,"
				+ " baked potatoes, and tomatoes�which also have vitamin C. Some foods and beverages that are fortified with vitamin C. To find out if vitamin C has been "
				+ "added to a food product, check the product labels. The vitamin C content of food may be reduced by prolonged storage and by cooking. Steaming or microwaving "
				+ "may lessen cooking losses. Fortunately, many of the best food sources of vitamin C, such as fruits and vegetables, are usually eaten raw.";

		nutrientDescriptions[13] = // => vitamin d
		"Vitamin D is a nutrient found in some foods that is needed for health and to maintain strong bones. It does so by helping the body absorb "
				+ "calcium (one of bone's main building blocks) from food and supplements. People who get too little vitamin D may develop soft, thin, and brittle bones,"
				+ " a condition known as rickets in children and osteomalacia in adults. \n\nVitamin D is important to the body in many other ways as well. "
				+ "Muscles need it to move, for example, nerves need it to carry messages between the brain and every body part, and the immune system needs "
				+ "vitamin D to fight off invading bacteria and viruses. Together with calcium, vitamin D also helps protect older adults from osteoporosis. Vitamin D "
				+ "is found in cells throughout the body. "
				+ "\n\nVery few foods naturally have vitamin D. Fortified foods provide most of the vitamin D in American diets."
				+ " Fatty fish such as salmon, tuna, and mackerel are among the best sources. Beef liver, cheese, and egg yolks provide small amounts."
				+ " Mushrooms provide some vitamin D. In some mushrooms that are newly available in stores, the vitamin D content is being boosted by exposing these mushrooms to ultraviolet light."
				+ " Almost all of the U.S. milk supply is fortified with 400 IU of vitamin D per quart. But foods made from milk, like cheese and ice cream, "
				+ " are usually not fortified. Vitamin D is added to many breakfast cereals and to some brands of orange juice, yogurt, margarine, and soy beverages;"
				+ " check the labels.";

		nutrientDescriptions[14] = // => vitamin b6
		"Vitamin B6 is a vitamin that is naturally present in many foods. The body needs vitamin B6 for more than 100 enzyme reactions involved in metabolism. "
				+ "Vitamin B6 is also involved in brain development during pregnancy and infancy as well as immune function."
				+ "\n\nVitamin B6 is found naturally in many foods and is added to other foods. You can get recommended amounts of vitamin B6 by eating a variety of foods,"
				+ " including the following: Poultry, fish, and organ meats, all rich in vitamin B6. Potatoes and other starchy vegetables, which are some of the major sources"
				+ " of vitamin B6 for Americans. Fruit (other than citrus), which are also among the major sources of vitamin B6 for Americans.";

		nutrientDescriptions[15] = // => vitamin b12
		"Vitamin B12 is a nutrient that helps keep the body's nerve and blood cells healthy and helps make DNA, the genetic material in all cells. "
				+ "Vitamin B12 also helps prevent a type of anemia called megaloblastic anemia that makes people tired and weak. Two steps are required for the "
				+ "body to absorb vitamin B12 from food. First, hydrochloric acid in the stomach separates vitamin B12 from the protein to which vitamin B12 "
				+ "is attached in food. After this, vitamin B12 combines with a protein made by the stomach called intrinsic factor and is absorbed by the body. "
				+ "Some people have pernicious anemia, a condition where they cannot make intrinsic factor. As a result, they have trouble absorbing vitamin B12 "
				+ "from all foods and dietary supplements. "
				+ "\n\n Vitamin B12 is found naturally in a wide variety of animal foods and is added to some fortified foods. "
				+ "Plant foods have no vitamin B12 unless they are fortified. You can get recommended amounts of vitamin B12 by eating a variety of foods including "
				+ "the following: Beef liver and clams, which are the best sources of vitamin B12. Fish, meat, poultry, eggs, milk, and other dairy products, "
				+ "which also contain vitamin B12. Some breakfast cereals, nutritional yeasts and other food products that are fortified with vitamin B12. ";

		nutrientDescriptions[16] = // => vitamin a
		"Vitamin A is a group of compounds that play an important role in vision, bone growth, reproduction, cell division, and cell differentiation "
				+ "(in which a cell becomes part of the brain, muscle, lungs, blood, or other specialized tissue.). Vitamin A helps regulate the immune system,"
				+ " which helps prevent or fight off infections by making white blood cells that destroy harmful bacteria and viruses. Vitamin A also may help "
				+ "lymphocytes (a type of white blood cell) fight infections more effectively. \n\nVitamin A promotes healthy surface linings of the eyes and the "
				+ "respiratory, urinary, and intestinal tracts [8]. When those linings break down, it becomes easier for bacteria to enter the body and cause infection."
				+ "Vitamin A also helps the skin and mucous membranes function as a barrier to bacteria and viruses. In general, there are two categories of vitamin A,"
				+ " depending on whether the food source is an animal or a plant. Vitamin A found in foods that come from animals is called preformed vitamin A. "
				+ "It is absorbed in the form of retinol, one of the most usable (active) forms of vitamin A. Sources include liver, whole milk, and some fortified food products."
				+ " Retinol can be made into retinal and retinoic acid (other active forms of vitamin A) in the body.";

		nutrientDescriptions[17] = // => vitamin e
		"Vitamin E is a fat-soluble nutrient found in many foods. In the body, it acts as an antioxidant, helping to protect cells from the damage caused by "
				+ "free radicals. Free radicals are compounds formed when our bodies convert the food we eat into energy. People are also exposed to free radicals in the "
				+ "environment from cigarette smoke, air pollution, and ultraviolet light from the sun. \n\n"
				+ "The body also needs vitamin E to boost its immune system so that it can fight off invading bacteria and viruses. It helps to widen blood vessels"
				+ " and keep blood from clotting within them. In addition, cells use vitamin E to interact with each other and to carry out many important functions. \n\n"
				+ "Vitamin E is found naturally in foods and is added to some fortified foods. You can get recommended amounts of vitamin E by eating a variety of foods"
				+ " including the following: Vegetable oils like wheat germ, sunflower, and safflower oils are among the best sources of vitamin E. "
				+ "Corn and soybean oils also provide some vitamin E. Nuts (such as peanuts, hazelnuts, and, especially, almonds) and seeds (like sunflower seeds) are also "
				+ "among the best sources of vitamin E. Green vegetables, such as spinach and broccoli, provide some vitamin E."
				+ "Food companies add vitamin E to some breakfast cereals, fruit juices, margarines and spreads, and other foods. ";

		nutrientDescriptions[18] = // => vitamin k
		" Vitamin K is known as the clotting vitamin, because without it blood would not clot. Some studies suggest that it helps maintain strong bones in the elderly."
				+ "The best way to get the daily requirement of vitamin K is by eating food sources. Vitamin K is found in the following foods: Green leafy vegetables, such as kale,"
				+ " spinach, turnip greens, collards, Swiss chard, mustard greens, parsley, romaine, and green leaf lettuce; Vegetables such as Brussels sprouts, broccoli, cauliflower,"
				+ " and cabbage; Fish, liver, meat, eggs, and cereals (contain smaller amounts); Vitamin K is also made by the bacteria that line the gastrointestinal tract.";

		nutrientDescriptions[19] = // => saturated fats
		"The body uses some saturated fatty acids for physiological and structural functions, but it makes "
				+ "more than enough to meet those needs. People therefore have no dietary requirement for saturated "
				+ "fatty acids. A strong body of evidence indicates that higher intake of most dietary saturated fatty "
				+ "acids is associated with higher levels of blood total cholesterol and low-density lipoprotein (LDL) "
				+ "cholesterol. Higher total and LDL cholesterol levels are risk factors for cardiovascular disease. \n \n"
				+ "Consuming less than 10 percent of calories from saturated fatty acids and replacing them with "
				+ "monounsaturated and/or polyunsaturated fatty acids is associated with low blood cholesterol levels, "
				+ "and therefore a lower risk of cardiovascular disease.Oils that are rich in monounsaturated fatty acids "
				+ "include canola, olive, and saflower oils. Oils that are good sources of polyunsaturated fatty acids "
				+ "include soybean, corn, and cottonseed oils. ";

		nutrientDescriptions[20] = // => cholesterol
		"Cholesterol is a fatty substance that's found in animal-based foods such as meats, poultry, egg yolks, and whole"
				+ " milks. Do you remember the other type of fat that is found in animal-based products? That's right � saturated fat."
				+ "The Recommendation The Dietary Guidelines for Americans 2005 recommend that individuals consume less than 300 milligrams"
				+ " (mg) of cholesterol each day. So, when you follow the tips to reduce your saturated fat intake, in most cases, you will "
				+ "be reducing your dietary cholesterol intake at the same time. For example, if you switch to low-fat and fat-free dairy products,"
				+ " you will reduce your intake of both saturated fat and cholesterol.";
	}

}
