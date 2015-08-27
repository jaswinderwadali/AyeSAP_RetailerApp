package com.tanmay.ayesapdevtest;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

public class ChangePasswordActivity extends AppCompatActivity {

	Context context;

	SharedPreferences sharedPreferences;
	Editor editor;

	EditText oldPass, newPass, reenterPass;
	Button changePass;
	private Toolbar toolbar;
	ProgressDialog pDialog;

	JSONObject jObject;

	NoInternet noInternet;

	String mobile, newPassword;
	String url = ConstantClass.baseUrl + "/retailer/changePassword";
	String url_logout = ConstantClass.baseUrl + "/retailer/logOut";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);

		context = this;
		noInternet = new NoInternet();

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		TextView title = (TextView) toolbar.findViewById(R.id.title);
		title.setText("Change Password");

		sharedPreferences = getApplicationContext().getSharedPreferences("Reg",
				0);
		editor = sharedPreferences.edit();

		jObject = new JSONObject();
		String retailerDetails, retailerId, oldPlainPass;
		JSONObject retDetails;
		try {
			retailerDetails = sharedPreferences.getString("retailerDetails",
					null);
			retDetails = new JSONObject(retailerDetails);
			retailerId = retDetails.getString("retailerId");
			oldPlainPass = retDetails.getString("plainPass");
			mobile = retDetails.getString("mobile");
			jObject.put("retailerId", retailerId);
			jObject.put("oldPlainPass", oldPlainPass);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		otpChanged = sharedPreferences.getString("OTPChanged", null);

		oldPass = (EditText) findViewById(R.id.oldPassword);
		newPass = (EditText) findViewById(R.id.newPassword);
		reenterPass = (EditText) findViewById(R.id.reenterPassword);
		changePass = (Button) findViewById(R.id.submitPassword);

		changePass.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String oldPassword = oldPass.getText().toString();
				newPassword = newPass.getText().toString();
				String reenterPassword = reenterPass.getText().toString();

				oldPass.setError(null);
				newPass.setError(null);
				reenterPass.setError(null);

				if (oldPassword.isEmpty()) {
					oldPass.setError("Cannot Be Left Blank");
					oldPass.requestFocus();
				} else if (!isValidPassword(oldPassword)) {
					oldPass.setError("Invalid Password");
					oldPass.requestFocus();
				} else if (newPassword.isEmpty()) {
					newPass.setError("Cannot Be Left Blank");
					newPass.requestFocus();
				} else if (!isValidPassword(newPassword)) {
					newPass.setError("Invalid Pasword");
					newPass.requestFocus();
				} else if (reenterPassword.isEmpty()) {
					reenterPass.setError("Cannot Be Left Blank");
					reenterPass.requestFocus();
				} else if (!reenterPassword.equals(newPassword)) {
					reenterPass.setError("Passwords Don't Match");
					reenterPass.requestFocus();
				} else if (newPassword.equals(reenterPassword)) {
					try {
						jObject.put("newPlainPass", newPassword);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					noInternet.networkError(context);
					if (noInternet.isOnline(context)) {

						makeJsonObjReq(jObject);
					}
				}
			}

		});
	}

	private void makeJsonObjReq(final JSONObject jObject) {
		Log.d("ChangePassword_Request", jObject.toString());

		String tag_json_obj = "json_obj_req";

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);
		pDialog.show();

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.PUT, url,
				jObject, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						pDialog.dismiss();
						Log.d("ChangePassword_Response", response.toString());
						try {
							String message = response.getString("message");
							Toast.makeText(context, message, Toast.LENGTH_LONG)
									.show();
							JSONObject jsonObj = new JSONObject();
							try {
								jsonObj.put("mobile", mobile);
								jsonObj.put("password", newPassword);
								editor.putString("credentials",
										jsonObj.toString());
								editor.commit();
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
//							if (otpChanged.equalsIgnoreCase("no")) {
//								makeLogoutReq();
//							} else {
								finish();
//							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						VolleyLog.d("VolleyError_ChangePassword", "Error: "
								+ error.getMessage());
						pDialog.dismiss();

						String jsonString = null;

						NetworkResponse response = error.networkResponse;
						if (response != null && response.data != null) {
							switch (response.statusCode) {
							case 400:
								jsonString = new String(response.data);
								jsonString = trimMessage(jsonString, "message");
								if (jsonString != null)
									displayMessage(jsonString);
								break;
							}

						}
					}
				});

		AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
	}

	public String trimMessage(String json, String key) {
		String trimmedString = null;

		try {
			JSONObject obj = new JSONObject(json);
			trimmedString = obj.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return trimmedString;
	}

	public void displayMessage(String toastString) {
		Toast.makeText(context, toastString, Toast.LENGTH_LONG).show();
	}

	private boolean isValidPassword(String pass) {
		if (pass.length() >= 6) {
			return true;
		}
		return false;
	}

	private void makeLogoutReq() {

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Logging Out...");
		pDialog.show();
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				url_logout, null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d("Dashboard_Logout_Response", response.toString());
						pDialog.dismiss();

						try {
							String message = response.getString("message");
							Toast.makeText(context, message, Toast.LENGTH_LONG)
									.show();
							editor.putBoolean("status", false);
							editor.commit();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Intent i = new Intent(context, MainActivity.class);
						startActivity(i);
						finish();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						VolleyLog.d("VolleyError_BookNow_Logout", "Error: "
								+ error.getMessage());
						pDialog.dismiss();

					}
				});

		AppController.getInstance().addToRequestQueue(jsonObjReq);
	}

}
