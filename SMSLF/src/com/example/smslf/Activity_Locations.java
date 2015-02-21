package com.example.smslf;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.smslf.classes.ListAdapterLocation;
import com.example.smslf.classes.Toast_M;
import com.example.smslf.db.Location_M;
import com.example.smslf.db.Staff;

public class Activity_Locations extends Activity {

	static Activity activity;
	static ListView lv;
	static String id;
	ArrayList<Location_M> locations;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_locations);

		activity = this;
		id = getIntent().getExtras().getString("id");
		((TextView) findViewById(R.id.txtName)).setText(getIntent().getExtras()
				.getString("name"));

		ListAdapterLocation adapter = null;

		try {
			locations = Location_M.getStaffLocations(activity,
					Integer.valueOf(id));
			adapter = new ListAdapterLocation(activity, locations);
		} catch (Exception e) {
			Toast_M.show(e.getMessage(), activity);
			e.printStackTrace();
		}
		lv = (ListView) findViewById(R.id.lvLocations);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(activity, Activity_Map.class);
				intent.putExtra("lat", locations.get(position).lat);
				intent.putExtra("lng", locations.get(position).lon);
				intent.putExtra("time", locations.get(position).message_time);
				startActivity(intent);
			}
		});

		((Button) findViewById(R.id.btnRequestLocaion))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							Staff.requestLocation(activity, Integer.valueOf(id));

						} catch (Exception e) {
							Toast_M.show(e.getMessage(), activity);
							e.printStackTrace();
						}
					}
				});

	}

	public static void updateListView() throws Exception {
		if (lv == null)
			return;
		ListAdapterLocation adapter = new ListAdapterLocation(activity,
				Location_M.getStaffLocations(activity, Integer.valueOf(id)));

		lv.setAdapter(adapter);
	}

}
