package com.example.smslf.classes;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.smslf.R;
import com.example.smslf.db.Location_M;

public class ListAdapterLocation extends ArrayAdapter<Location_M> {

	Activity activity;

	ArrayList<Location_M> locations = null;

	public ListAdapterLocation(Activity activity,
			ArrayList<Location_M> locations) {
		super(activity, R.layout.item_list_locations, locations);
		this.activity = activity;
		this.locations = locations;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			row = inflater.inflate(R.layout.item_list_locations, parent, false);

		}
		((TextView) row.findViewById(R.id.txt_city)).setText(locations
				.get(position).city);
		((TextView) row.findViewById(R.id.txt_street)).setText(locations
				.get(position).street);
		((TextView) row.findViewById(R.id.txt_date)).setText(locations
				.get(position).date_year
				+ "/"
				+ locations.get(position).date_month
				+ "/"
				+ locations.get(position).date_day);
		((TextView) row.findViewById(R.id.txt_time)).setText(locations
				.get(position).message_time);

		return row;
	}

}
