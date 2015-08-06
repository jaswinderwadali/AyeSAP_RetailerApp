package com.retailerapp.ayesap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SettingsAdapter extends BaseAdapter {

	String[] settings;
	Context context;
	private static LayoutInflater inflater = null;
	public static OnButtonClickListener click;

	public SettingsAdapter(Context context, String[] buttons) {
		this.context = context;
		settings = buttons;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return settings.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class Holder {
		TextView tvSettings;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = new Holder();
		View rowView = inflater.inflate(R.layout.settings_rows, null);
		holder.tvSettings = (TextView) rowView.findViewById(R.id.button);
		holder.tvSettings.setText(settings[position]);

		holder.tvSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				click.onSettingClick(position);
			}
		});

		return rowView;
	}

}
