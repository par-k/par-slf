package com.example.smslf.classes;

import java.util.ArrayList;
import java.util.List;

import com.example.smslf.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MyPopup {

	Context context;
	private ListView lv;

	// ArrayList<String> sections_list;

	public MyPopup(Context context) {
		this.context = context;

	}

	public ListView get_listview() {
		return lv;
	}

	public PopupWindow popupWindow(ArrayList<String> sections_list) {
		PopupWindow popupWindow = new PopupWindow(context);

		lv = new ListView(context);
		PopupListAdapter adapter = new PopupListAdapter(context,
				R.layout.list_row_popup, sections_list);

		lv.setAdapter(adapter);

		popupWindow.setFocusable(true);  
		popupWindow.setWidth(200);
		popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindow.setContentView(lv);

		return popupWindow;
	}

	public class PopupListAdapter extends ArrayAdapter<String> {

		Context context;
		int layoutResourceId;
		List<String> data;

		public PopupListAdapter(Context context, int layoutResourceId,
				List<String> data) {
			super(context, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			ListHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);
				row.setBackgroundColor(Color.WHITE);
				holder = new ListHolder();
				holder.txtTitle = (TextView) row.findViewById(R.id.txtListView);
				holder.txtTitle.setTypeface(Typeface.createFromAsset(
						context.getAssets(), "kamran_b.ttf"));
				row.setTag(holder);
			} else {

				holder = (ListHolder) row.getTag();
			}
			holder.txtTitle.setText(data.get(position));
			return row;
		}

	}

	static class ListHolder {
		TextView txtTitle;
	}

}
