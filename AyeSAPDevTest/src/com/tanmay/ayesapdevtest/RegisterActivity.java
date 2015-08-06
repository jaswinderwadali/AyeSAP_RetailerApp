package com.tanmay.ayesapdevtest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

public class RegisterActivity extends Activity {

	Context context;
	Button register;

	final String TAG = "Register Activity";

	String regexStr = "^[0-9]{10}$";
	String url = ConstantClass.baseUrl + "/retailer/requestForRegister";

	EditText nameEditText;
	EditText mobileEditText;
	EditText emailEditText;

	NoInternet noInternet;

	JSONObject jsonObj = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg);

		context = this;
		noInternet = new NoInternet();

		nameEditText = (EditText) findViewById(R.id.fullName);
		mobileEditText = (EditText) findViewById(R.id.mobileNumber);
		emailEditText = (EditText) findViewById(R.id.emailReg);

		register = (Button) findViewById(R.id.registerButton);
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String name = nameEditText.getText().toString();
				String email = emailEditText.getText().toString();
				String mobile = mobileEditText.getText().toString();

				nameEditText.setError(null);
				emailEditText.setError(null);
				mobileEditText.setError(null);

				if (name.isEmpty()) {
					nameEditText.setError("Cannot Be Left Blank");
					nameEditText.requestFocus();
				} else if (!isValidName(name)) {
					nameEditText.setError("Invalid Name");
					nameEditText.requestFocus();
				} else if (mobile.isEmpty()) {
					mobileEditText.setError("Cannot Be Left Blank");
					mobileEditText.requestFocus();
				} else if (!mobile.matches(regexStr)) {
					mobileEditText.setError("Invalid Number");
					mobileEditText.requestFocus();
				} else if (!email.isEmpty() && !isValidEmail(email)) {
					emailEditText.setError("Invalid Email");
					emailEditText.requestFocus();
				} else if (isValidName(name) && mobile.matches(regexStr)) {

					jsonObj = new JSONObject();
					try {
						jsonObj.put("name", name);
						jsonObj.put("email", email);
						jsonObj.put("mobile", mobile);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					noInternet.networkError(context);
					if (noInternet.isOnline(context)) {
						makeJsonObjReq(jsonObj);
					}
				}
			}
		});

	}

	private boolean isValidName(String name) {
		String NAME_PATTERN = "^[\\p{L} .'-]+$";
		Pattern pattern = Pattern.compile(NAME_PATTERN);
		Matcher matcher = pattern.matcher(name);
		return matcher.matches();
	}

	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	private void makeJsonObjReq(JSONObject jObject) {
		String tag_json_obj = "json_obj_req";

		final ProgressDialog pDialog = new ProgressDialog(this);
		pDialog.setMessage("Loading...");
		pDialog.show();
		Log.d("Register_JsonObjectSent", jObject.toString());

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST, url,
				jObject, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d("Register_Response", response.toString());
						pDialog.hide();
						Toast.makeText(
								context,
								"Your request has been registered. We will contact you shortly.",
								Toast.LENGTH_LONG).show();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						VolleyLog.d("VolleyError_Register",
								"Error: " + error.getMessage());
						pDialog.hide();

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

							case 401:
								jsonString = new String(response.data);
								jsonString = trimMessage(jsonString, "message");
								if (jsonString != null)
									displayMessage(jsonString);
								break;

							case 403:
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

}
