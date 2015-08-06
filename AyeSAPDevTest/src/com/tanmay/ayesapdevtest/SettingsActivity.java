package com.tanmay.ayesapdevtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity implements
		OnButtonClickListener {

	Context context;

	private Toolbar toolbar;

	ListView theList;
	String[] buttons = { "Change Password" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		context = this;

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		TextView title = (TextView) toolbar.findViewById(R.id.title);
		title.setText("Settings");

		SettingsAdapter.click = SettingsActivity.this;

		theList = (ListView) findViewById(R.id.listview);
		theList.setAdapter(new SettingsAdapter(this, buttons));

	}

	@Override
	public void onOptionClick(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCardOptionClick(int position, boolean isCancel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSettingClick(int position) {
		// TODO Auto-generated method stub
		if (position == 0) {
			Intent i = new Intent(context, ChangePasswordActivity.class);
			startActivity(i);
		}

	}

}
