package com.doesnotscale.android.mysuperradandroidapp;

import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FOURSQUARE_URL = "https://api.foursquare.com/v2/venues/search?query=burritos&client_id=WG4AKOCYZ2W2FHJFQVH4W4CAX1W2YPF1IVSM4EZIIHBFA5E3&client_secret=Q2G14DW3NRAK2F2LVMINHF0S4PPW23O1354BTNUYOOWEUWIE&v=20140806&radius=3218&near=";

    private EditText mEditText;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.editText);

        mButton = (Button) findViewById(R.id.button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String zipcode;

                zipcode = mEditText.getText().toString();

                if (zipcode != null) {
                    new FetchBurritoTask().execute(zipcode);
                }
            }
        });

    }

    private class FetchBurritoTask extends AsyncTask<String, Void, ArrayList<BurritoPlace>> {

        @Override
        protected ArrayList<BurritoPlace> doInBackground(String... strings) {
            try {
                String zipcode = strings[0];

                URL url = new URL(FOURSQUARE_URL + zipcode);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                connection.disconnect();

                JSONObject response = new JSONObject(sb.toString());

                JSONArray venues = response.getJSONObject("response").getJSONArray("venues");

                ArrayList<BurritoPlace> burritoPlaces = new ArrayList<>();

                for (int i = 0; i < venues.length(); i++) {
                    JSONObject venue = venues.getJSONObject(i);

                    BurritoPlace burritoPlace = new BurritoPlace();

                    burritoPlace.setId(venue.getString("id"));
                    burritoPlace.setName(venue.getString("name"));

                    JSONObject location = venue.getJSONObject("location");

                    Location l = new Location("BurritoFinder");
                    l.setLatitude(location.getDouble("lat"));
                    l.setLongitude(location.getDouble("lng"));
                    burritoPlace.setLocation(l);

                    JSONArray formattedAddress = location.getJSONArray("formattedAddress");

                    String[] addressItems = new String[formattedAddress.length()];
                    for (int j = 0; j < formattedAddress.length(); j++) {
                        addressItems[j] = formattedAddress.getString(j);
                    }

                    burritoPlace.setAddress(TextUtils.join(" ", addressItems));

                    burritoPlaces.add(burritoPlace);
                }

                return burritoPlaces;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<BurritoPlace> burritoPlaces) {

            startActivity(MapsActivity.makeIntent(getApplicationContext(), burritoPlaces));
        }
    }
}
