package com.example.smslfc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationTracker implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	LocationClient mLocationClient;
	Location mCurrentLocation;
	LocationRequest mLocationRequest;
	LocationListener locationListener;
	Handler handler = new Handler();
	Handler dataConnectionHandler = new Handler();

	private static final int MILLISECONDS_PER_SECOND = 1000;
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
	private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND
			* FASTEST_INTERVAL_IN_SECONDS;

	Context context;

	public LocationTracker(Context context) {
		this.context = context;

	}

	public void getLocation() throws Exception {
		try {
			final int result = GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(context);
			if (result != ConnectionResult.SUCCESS) {
				Toast.makeText(
						context,
						"Google Play service is not available (status="
								+ result + ")", Toast.LENGTH_LONG).show();
				SMS.send(Info.getPhoneNumber(context),
						Coding.code("000000003000000003"));

			}
		} catch (Exception e) {
			throw new Exception(e);
		}

		mLocationClient = new LocationClient(context, this, this);
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

		mLocationClient.connect();

	}

	private final class MyLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			// Report to the UI that the location was updated

			// sendLocation(location);

			if (!mLocationClient.isConnected())
				return;
			if(mCurrentLocation == null){
				mLocationClient.removeLocationUpdates(locationListener);
				mLocationClient.disconnect();
				sendLocation(location);
				try {
					if (isDataConnected())
						setMobileDataEnabled(context, false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
			if (!(location.getLatitude() == mCurrentLocation.getLatitude() && location
					.getLongitude() == mCurrentLocation.getLongitude())) {

				
				mLocationClient.removeLocationUpdates(locationListener);
				mLocationClient.disconnect();
				sendLocation(location);
				try {
					if (isDataConnected())
						setMobileDataEnabled(context, false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Toast.makeText(context, "Connection Failed", Toast.LENGTH_LONG).show();

	}

	@Override
	public void onConnected(Bundle connectionHint) {

		final LocationManager manager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			SMS.send(Info.getPhoneNumber(context),
					Coding.code("000000000000000000"));
			return;
		}

		if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

			SMS.send(Info.getPhoneNumber(context),
					Coding.code("000000001000000001"));
			return;
		}

		locationListener = new MyLocationListener();
		mLocationClient.requestLocationUpdates(mLocationRequest,
				locationListener);
		mCurrentLocation = mLocationClient.getLastLocation();
		dataConnectionRecursivelycallHandler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mLocationClient.isConnected()) {
					mLocationClient.removeLocationUpdates(locationListener);
					mLocationClient.disconnect();

					SMS.send(Info.getPhoneNumber(context),
							Coding.code("000000002000000002"));// GPS Error
					try {
						if (isDataConnected())
							setMobileDataEnabled(context, false);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}, 5 * 60 * 1000);// 5 minutes

	}

	public void dataConnectionRecursivelycallHandler() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (mLocationClient.isConnected() && !isDataConnected()) {
					try {
						setMobileDataEnabled(context, true);
					} catch (Exception e) {
						String s = e.getMessage();
					}
				} else
					dataConnectionRecursivelycallHandler();

			}
		}, 1 * 60 * 1000);// 1 minute
	}

	private void sendLocation(Location location) {

		String lat = context.getString(R.string.lat_or_long,
				location.getLatitude());
		String lon = context.getString(R.string.lat_or_long,
				location.getLongitude());

		lat = lat.replace(".", "");
		lon = lon.replace(".", "");

		SMS.send(Info.getPhoneNumber(context), Coding.code(lat + lon));
	}

	@Override
	public void onDisconnected() {

	}

	private void setMobileDataEnabled(Context context, boolean enabled)
			throws Exception {
		final ConnectivityManager conman = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final Class conmanClass = Class.forName(conman.getClass().getName());
		final Field connectivityManagerField = conmanClass
				.getDeclaredField("mService");
		connectivityManagerField.setAccessible(true);
		final Object connectivityManager = connectivityManagerField.get(conman);
		final Class connectivityManagerClass = Class
				.forName(connectivityManager.getClass().getName());
		final Method setMobileDataEnabledMethod = connectivityManagerClass
				.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		setMobileDataEnabledMethod.setAccessible(true);

		setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
	}

	private boolean isDataConnected() {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) 
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) 
                for (int i = 0; i < info.length; i++) 
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
	}
}
