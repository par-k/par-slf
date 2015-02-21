package com.example.smslf.classes;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.example.smslf.Activity_Locations;
import com.example.smslf.R;
import com.example.smslf.db.DataBaseHandler;
import com.example.smslf.db.Location_M;

public class GetAddress extends AsyncTask<Void, Void, Void> {

	Location_M location;
	Context context;

	public GetAddress(Context context, Location_M location) {
		this.context = context;
		this.location = location;
	}

	@Override
	protected Void doInBackground(Void... params) {
   
		if (location.lat.equals("00.0000000")) {
			location.street = "";
			location.country = "";
			location.city = context.getResources()
					.getString(R.string.errGPSOff);
			return null;
		} else if (location.lat.equals("00.0000001")) {
			location.street = "";
			location.country = "";
			location.city = context.getResources().getString(
					R.string.errGoogleLocationService);
			return null;
		} else if (location.lat.equals("00.0000002")) {
			location.street = "";
			location.country = "";
			location.city = context.getResources().getString(
					R.string.errGPSUnable);
			return null;
		}
		else if (location.lat.equals("00.0000003")) {
			location.street = "";
			location.country = "";
			location.city = context.getResources().getString(
					R.string.errGPSUnable);
			return null;
		}
		location = getLocationFromGeocoder(location);

		if (location.city == null)
			try {
				location = setAddress(location, getLocationFormGoogle(location));
			} catch (JSONException e) {
			}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		DataBaseHandler db;
		try {
			if (location.city == null) {
				location.street = "";
				location.country = "";
				location.city = context.getResources().getString(
						R.string.errGoogleServiceDisabledDestination);
			}
			if (location.country == null) {
				location.country = "";
			}
			db = new DataBaseHandler(context);
			db.insertLocation(location);
			Activity_Locations.updateListView();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Location_M getLocationFromGeocoder(Location_M location) {
		Locale mLocale = new Locale("fa");
		Geocoder geocoder = new Geocoder(context, mLocale);
		try {
			List<Address> listAddresses = geocoder.getFromLocation(
					Double.valueOf(location.lat), Double.valueOf(location.lon),
					1);
			if (null != listAddresses && listAddresses.size() > 0) {
				location.street = listAddresses.get(0).getAddressLine(0);
				location.city = listAddresses.get(0).getAddressLine(1);
				location.country = listAddresses.get(0).getAddressLine(2);
			}
		} catch (IOException e) {

		}
		return location;
	}

	public JSONObject getLocationFormGoogle(Location_M location) {

		JSONObject jsonObject = null;
		HttpGet httpGet = new HttpGet(
				"http://maps.googleapis.com/maps/api/geocode/json?latlng="
						+ location.lat + "," + location.lon
						+ "&sensor=false&language=en");

		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (Exception e) {
			return jsonObject;
		}

		jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return jsonObject;
	}

	public Location_M setAddress(Location_M location, JSONObject jsonObject)
			throws JSONException {

		if (jsonObject == null)
			return location;
		JSONArray results = (JSONArray) jsonObject.get("results");
		JSONObject result = results.getJSONObject(0);
		//
		JSONArray addresses = (JSONArray) result.get("address_components");
		location.street = addresses.getJSONObject(0).getString("long_name");
		location.city = addresses.getJSONObject(2).getString("long_name");
		location.country = addresses.getJSONObject(5).getString("long_name");

		return location;

	}
}
