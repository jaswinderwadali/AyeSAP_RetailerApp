package com.retailerapp.ayesap;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

public class ForgotPasswordActivity extends AppCompatActivity {

	Context context;
	Button changePass;

	String url = ConstantClass.baseUrl + "";

	Toolbar toolbar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);

		TextView title = (TextView) toolbar.findViewById(R.id.title);
		title.setText("Forgot Password");

	}

}
