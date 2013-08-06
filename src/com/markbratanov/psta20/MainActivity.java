package com.markbratanov.psta20;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

	ProgressDialog dialog;
	ArrayList<String> busLinesName = new ArrayList<String>();
	ArrayList<String> busLinesHref = new ArrayList<String>();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Typeface typeface = Typeface.createFromAsset(getAssets(),
				"fonts/Roboto-LightItalic.ttf");

		TextView h1GetStop = (TextView) findViewById(R.id.h1_enterstop);
		TextView h1ChooseRoute = (TextView) findViewById(R.id.TextView01);

		h1GetStop.setTypeface(typeface);
		h1ChooseRoute.setTypeface(typeface);

		if (isNetworkAvailable()) {
			new fetchData()
					.execute("http://ridepsta.net/bustime/wireless/html/home.jsp");

		} else {
			
			noInternet();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// The definition of our task class
	private class fetchData extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setProgress();
		}

		@Override
		protected String doInBackground(String... params) {

			Document doc = null;
			String url = params[0];
			
			try {
				doc = Jsoup.connect(url).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Elements links = doc.select("ul li a");

			for (Element link : links) {
				busLinesHref.add(link.attr("href"));
				busLinesName.add(link.text());
			}

			return "Done";

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// nothing
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			setSpinner();
			dismissProgress();
		}
	}
	
		
	public void setSpinner() {
		if (busLinesName.size() > 0) {
			Spinner spinnerCategory = (Spinner) findViewById(R.id.spinner1);
			ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(
					getApplicationContext(),
					android.R.layout.simple_spinner_item, busLinesName);
			spinnerCategory.setAdapter(categoriesAdapter);
		} else {
			Spinner spinnerCategory = (Spinner) findViewById(R.id.spinner1);
			spinnerCategory.setVisibility(View.GONE);
		}
	}

	public void setProgress() {
		dialog = ProgressDialog.show(MainActivity.this, "", "Loading...", true);
	}

	public void noInternet() {
		dialog = ProgressDialog.show(MainActivity.this, "",
				"No internet connection available", true);
	}

	public void dismissProgress() {
		dialog.dismiss();
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public class SpinnerActivity extends Activity implements OnItemSelectedListener {
	   
	    
	    public void onItemSelected(AdapterView<?> parent, View view, 
	            int pos, long id) {


	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	        // Another interface callback
	    }
	}
	
	/*
	 * public void setBusLines() {
	 * 
	 * busLines.add("CAT - CENTRAL AVENUE TROLLEY");
	 * busLines.add("PPS - PINELLAS PARK SHUTTLE");
	 * busLines.add("SBT - SUNCOAST BEACH TROLLEY");
	 * busLines.add("1 - ROUTE 1"); busLines.add("4 - ROUTE 4");
	 * busLines.add("5 - ROUTE 5"); busLines.add("7 - ROUTE 7");
	 * busLines.add("11 - ROUTE 11"); busLines.add("14 - ROUTE 14");
	 * busLines.add("15 - ROUTE 15"); busLines.add("18 - ROUTE 18");
	 * busLines.add("19 - ROUTE 19"); busLines.add("20 - ROUTE 20");
	 * busLines.add("23 - ROUTE 23"); busLines.add("30 - ROUTE 30");
	 * busLines.add("32 - ROUTE 32");
	 * busLines.add("35 - CENTRAL AVENUE TROLLEY");
	 * busLines.add("38 - ROUTE 38"); busLines.add("52 - ROUTE 52");
	 * busLines.add("58 - ROUTE 58"); busLines.add("59 - ROUTE 59");
	 * busLines.add("60 - ROUTE 60"); busLines.add("61 - ROUTE 61");
	 * busLines.add("62 - ROUTE 62"); busLines.add("66 - ROUTE 66");
	 * busLines.add("67 - ROUTE 67"); busLines.add("68 - ROUTE 68");
	 * busLines.add("73 - ROUTE 73"); busLines.add("74 - ROUTE 74");
	 * busLines.add("75 - ROUTE 75"); busLines.add("58 - ROUTE 58");
	 * busLines.add("76 - ROUTE 76"); busLines.add("78 - ROUTE 78");
	 * busLines.add("79 - ROUTE 79"); busLines.add("90 - ROUTE 90");
	 * busLines.add("97 - ROUTE 97"); busLines.add("98 - ROUTE_98");
	 * busLines.add("100X - ROUTE 100X"); busLines.add("300X - ROUTE 300X");
	 * busLines.add("811 - ELC");
	 * 
	 * }
	 */

	public ArrayList<String> getBusLines() {
		return busLinesName;
	}
	/*
	 * private boolean isFirstTime(){
	 * 
	 * SharedPreferences preferences = getPreferences(MODE_PRIVATE); boolean
	 * ranBefore = preferences.getBoolean("RanBefore", false); if (!ranBefore) {
	 * 
	 * SharedPreferences.Editor editor = preferences.edit();
	 * editor.putBoolean("RanBefore", true); editor.commit();
	 * 
	 * topLevelLayout.setVisibility(View.VISIBLE);
	 * topLevelLayout.setOnTouchListener(new View.OnTouchListener(){
	 * 
	 * @Override public boolean onTouch(View v, MotionEvent event) {
	 * topLevelLayout.setVisibility(View.INVISIBLE); return false; } });
	 * 
	 * }
	 * 
	 * return ranBefore;
	 * 
	 * }
	 */

}
