package com.example.smslf;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Activity_Map extends Activity {

	// Google Map
	private GoogleMap googleMap;
	MapView mapView;
	Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = this;
		double lat = Double.valueOf(getIntent().getExtras().getString("lat"));
		double lng = Double.valueOf(getIntent().getExtras().getString("lng"));

		try {
			setContentView(R.layout.activity_map);

			MapsInitializer.initialize(activity);
			mapView = (MapView) findViewById(R.id.map);
			mapView.onCreate(savedInstanceState);

			if (mapView != null) {
				googleMap = mapView.getMap();
				googleMap.getUiSettings().setMyLocationButtonEnabled(false);
				googleMap.setMyLocationEnabled(true);

				// create marker
				MarkerOptions marker = new MarkerOptions().position(
						new LatLng(lat, lng)).title(
						getIntent().getExtras().getString("time"));

				googleMap.addMarker(marker);

				// CameraUpdate cameraUpdate =
				// CameraUpdateFactory.newLatLngZoom(
				// new LatLng(43.1, -87.9), 10);
				//
				// googleMap.animateCamera(cameraUpdate);

				// Move the camera instantly to hamburg with a zoom of 15.
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						new LatLng(lat, lng), 15));

				// Zoom in, animating the camera.
				googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000,
						null);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onBackPressed() {
		mapView.onPause();
		finish();
	}

	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	public static String bytesToHex(byte[] bytes) {
		char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < hexChars.length; i += 2) {
			sb.append(hexChars[i]);
			sb.append(hexChars[i + 1]);
			if (i < hexChars.length - 2) {
				sb.append(':');
			}
		}
		return sb.toString();
	}
}
