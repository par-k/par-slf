package com.example.smslf;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.smslf.classes.GetAddress;
import com.example.smslf.classes.GetAddressAgain;
import com.example.smslf.classes.ListAdapterStaffs;
import com.example.smslf.classes.MyPopup;
import com.example.smslf.classes.Toast_M;
import com.example.smslf.db.DataBaseHandler;
import com.example.smslf.db.Info;
import com.example.smslf.db.Location_M;
import com.example.smslf.db.Staff;

public class Activity_Staffs extends Activity implements OnClickListener {

	static Activity activity;
	static ListView lv;
	static ArrayList<Staff> staffs;
	PopupWindow popupWindow;
	ListView lv_popup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_staffs);

		activity = this;
		staffs = null;

		try {
			if (!Info.getActivation(activity)) {
				Intent intent = new Intent(getApplicationContext(),
						Activity_Entrance.class);
				startActivity(intent);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			staffs = Staff.getAllStaffs(activity);

		} catch (Exception e) {
			Toast_M.show(e.getMessage(), activity);
			e.printStackTrace();
		}

		lv = (ListView) findViewById(R.id.lvStaffs);
		ListAdapterStaffs adapter = new ListAdapterStaffs(activity, staffs);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(activity, Activity_Locations.class);
				intent.putExtra("id", String.valueOf(staffs.get(position).id));
				intent.putExtra("name",
						String.valueOf(staffs.get(position).name));
				startActivity(intent);
			}
		});

		((ImageView) findViewById(R.id.imgMore)).setOnClickListener(this);

		MyPopup pop = new MyPopup(this);
		popupWindow = pop.popupWindow(ArraytoArrayList(activity.getResources()
				.getStringArray(R.array.array_main_page_more)));
		lv_popup = pop.get_listview();
		lv_popup.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				popupWindow.dismiss();
				switch (position) {
				case 0:
					Intent intentAdd = new Intent(activity,
							Activity_AddStaff.class);
					startActivity(intentAdd);
					break;
				case 1:
					Intent intentRemove = new Intent(activity,
							Activity_DeleteStaff.class);
					startActivity(intentRemove);
					break;
				case 2:
					new GetAddressAgain(activity).execute();
					break;
				case 3:
					DataBaseHandler db;
					try {
						db = new DataBaseHandler(activity);
						db.BackupDatabase();
						//db.export();
					} catch (Exception e) {
						Toast_M.show(e.getMessage(), activity);
					}

					break;

				}

			}
		});

	}

	public static void refreshListView() throws Exception {
		ListAdapterStaffs adapter = new ListAdapterStaffs(activity,
				Staff.getAllStaffs(activity));
		staffs = Staff.getAllStaffs(activity);
		lv.setAdapter(adapter);
	}

	public ArrayList<String> ArraytoArrayList(String[] array) {
		ArrayList<String> arrayList = new ArrayList<String>();
		for (String string : array) {
			arrayList.add(string);
		}
		return arrayList;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.imgMore)
			popupWindow.showAsDropDown(v, -5, 0);

	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	public static void goToEntrance() {
		Intent intent = new Intent(activity, Activity_Entrance.class);
		activity.startActivity(intent);
	}

}
