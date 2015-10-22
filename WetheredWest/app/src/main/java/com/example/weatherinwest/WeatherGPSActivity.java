package com.example.weatherinwest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherGPSActivity extends Activity {
	
	Button btnShowWeather;

	public final static String apiURL = "https://api.forecast.io/forecast/23db9d75fd6ebaefa7d585ba89f8e9a9/";
	//https://api.forecast.io/forecast/23db9d75fd6ebaefa7d585ba89f8e9a9/37.8267,-122.423

	// GPSTracker class
	GPSTracker gps;
	String resturl = "" ;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnShowWeather = (Button) findViewById(R.id.btnShowLocation);
		getWeatherNow(btnShowWeather );

	};
        
        // show location button click event
       // btnShowLocation.setOnClickListener(new View.OnClickListener() {
		//	public void onClick(View arg0) {
				// create class that instantiates both at creation time
				//and on refresh
				//we need :location coprdinates, append it to
				//weather url and invoke api async
				//on return should be able to refresh ui

					private class CallAPI extends AsyncTask<String, String, String> {


						@Override
						protected String doInBackground(String... params) {
							String urlString=params[0]; // URL to call
							String resultToDisplay = "";
							InputStream in = null;
                            String result  = "sunny" ;

							// HTTP Get
							try {
								URL url = new URL(urlString);
								HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
								in = new BufferedInputStream(urlConnection.getInputStream());
							} catch (Exception e ) {
								System.out.println(e.getMessage());
								return e.getMessage();
							}
                       //parsing
							byte[] contents = new byte[1024];

							int bytesRead=0;
							String strFileContents = "";
							try {
								while( (bytesRead = in.read(contents)) != -1){
                                    strFileContents+= new String(contents, 0, bytesRead);
                                }
							} catch (IOException e) {
								e.printStackTrace();
							}

							JSONParser parser = new JSONParser();
							try {

								Object obj = parser.parse(strFileContents);

								JSONObject jsonObject = (JSONObject) obj;
//
   //    Set mp =   jsonObject.keySet();
       System.out.println("Set picked;" + jsonObject.get("hourly"));
//
								jsonObject  = (JSONObject)jsonObject.get("hourly");

//
      //   mp =   jsonObject.keySet();
      //  System.out.println("hourly ;" + mp);
								result  = (String)jsonObject.get("summary");


							} catch (ParseException e) {
								e.printStackTrace();

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
  								return result;

						}

						protected void onPostExecute(String result) {
						//efresh screen with result

							TextView tv =   (TextView) findViewById(R.id.weathertext);
							ImageView iv = (ImageView) findViewById(R.id.weatherimage);
						tv.setText(result);
							if (result.indexOf("cloud")	 > 0) {

								iv.setImageResource(R.drawable.cloud);
							}
							else
							if (result.indexOf("rain")	 > 0) {

								iv.setImageResource(R.drawable.androidrain);
							}
							else
							if (result.indexOf("sun")	 > 0) {

								iv.setImageResource(R.drawable.androidsun);
							}
							else{iv.setImageResource(R.drawable.android); }

						}


						}

					 // end CallAPI
					// This is the method that is called when the refresh button is clicked
					public void getWeatherNow(View V) {
						gps = new GPSTracker(WeatherGPSActivity.this);

						// check if GPS enabled
						if(gps.canGetLocation()){

							double latitude = gps.getLatitude();
							double longitude = gps.getLongitude();
							resturl = apiURL +Double.toString(latitude) + ","+Double.toString(longitude);}
							new CallAPI().execute(resturl);
						}

					}


















































