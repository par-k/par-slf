package com.example.smslf.classes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.example.smslf.Activity_Locations;
import com.example.smslf.R;
import com.example.smslf.db.DataBaseHandler;
import com.example.smslf.db.Location_M;

public class GetAddressAgain extends AsyncTask<Void, Void, Void> {

	Context context;
	Activity ac;
	ArrayList<Location_M> locations = new ArrayList<Location_M>();
	static int successfullCounter = 0;

	public GetAddressAgain(Activity ac) {
		this.context = (Context) ac;
		this.ac = ac;
	}

	@Override
	protected Void doInBackground(Void... params) {
		successfullCounter = 0;

		if (!isNetworkAvailable(context)) {
			ac.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					AlertDialog_M.show(context, context.getResources()
							.getString(R.string.alertNoInternetAccess));

				}
			});
			return null;
		}
		DataBaseHandler db = null;
		try {
			db = new DataBaseHandler(context);
			locations = db.selectLocationsMissed(context);
		} catch (Exception e) {
		}
		if (locations.size() == 0) {

			ac.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					AlertDialog_M.show(context, context.getResources()
							.getString(R.string.alertNoResult));
				}
			});

			return null;
		}

		ac.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast_M.show(
						context.getResources().getString(
								R.string.messageGettingAddressStarted), context);
			}
		});

		try {
			for (Location_M location : locations) {
				getAddress(location);
				
				if (!location.city.equals(context.getResources().getString(
						R.string.errGoogleServiceDisabledDestination))){
					successfullCounter++;
					db.updateLocationAddress(location);
				}
			}
		} catch (final Exception e) {

		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		if (locations.size() != 0)
			ac.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					AlertDialog_M.show(context, context.getResources()
							.getString(R.string.alertResultNumbers)
							+ " "
							+ successfullCounter + "\n");
				}
			});
		try {
			Activity_Locations.updateListView();
		} catch (final Exception e) {
			ac.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast_M.show(e.getMessage(), context);
				}
			});
		}
	}

	private void getAddress(Location_M location) throws Exception {
		getLocationFromGeocoder(location);
		if (location.street.equals(""))
			setAddress(location, getLocationFormGoogle(location));
	}

	private void getLocationFromGeocoder(Location_M location) {
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
		} catch (final IOException e) {

		}
	}

	private JSONObject getLocationFormGoogle(Location_M location) {

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
		} catch (final Exception e) {

		}

		jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return jsonObject;
	}

	private void setAddress(Location_M location, JSONObject jsonObject)
			throws JSONException {

		if (jsonObject == null)
			return;
		try {
			JSONArray results = (JSONArray) jsonObject.get("results");
			JSONObject result = results.getJSONObject(0);
			//
			JSONArray addresses = (JSONArray) result.get("address_components");
			location.street = addresses.getJSONObject(0).getString("long_name");
			location.city = addresses.getJSONObject(2).getString("long_name");
			location.country = addresses.getJSONObject(5)
					.getString("long_name");
		} catch (final Exception e) {

		}

	}

	public static boolean isNetworkAvailable(final Context context) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (activeNetworkInfo == null) {
				return false;
			}
			boolean resutl = connectivityManager.getActiveNetworkInfo()
					.isConnectedOrConnecting();
			return resutl;

		}

		catch (Exception ex) {

			return false;
		}

	}

}
