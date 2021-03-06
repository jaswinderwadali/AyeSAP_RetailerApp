package com.tanmay.ayesapdevtest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

public class BookNowActivity extends Activity {

	Context context;

	TextView eta;
	EditText amount, amountTBC, custMobNum, custName, custAdd, custStreet,
			custArea, custCity, custPincode, noOfShip;
	Button bookNow;

	private SharedPreferences sharedPreferences;
	Editor editor;
	ProgressDialog pDialog;

	NoInternet noInternet;

	JSONObject jsonObj = null;
	JSONObject customerDetails = null;
	JSONObject address = null;
	JSONObject retailerDetails;

	String regexStr = "^[0-9]{10}$";
	String url = ConstantClass.baseUrl + "/order/bookOrder";
	public int min;
	int addFlag = 0;
	int repeatedFlag = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_now);

		context = this;
		noInternet = new NoInternet();

		sharedPreferences = getSharedPreferences("Reg", 0);
		editor = sharedPreferences.edit();

		eta = (TextView) findViewById(R.id.timeDigit);
		amount = (EditText) findViewById(R.id.amount);
		amountTBC = (EditText) findViewById(R.id.amountCollected);
		custMobNum = (EditText) findViewById(R.id.custMobNum);
		noOfShip = (EditText) findViewById(R.id.noOfShip);
		custName = (EditText) findViewById(R.id.custName);
		custAdd = (EditText) findViewById(R.id.custAddress);
		custStreet = (EditText) findViewById(R.id.street);
		custArea = (EditText) findViewById(R.id.area);
		custCity = (EditText) findViewById(R.id.city);
		custPincode = (EditText) findViewById(R.id.pincode);
		bookNow = (Button) findViewById(R.id.bookNowButton);

		min = sharedPreferences.getInt("leastETA", 0);
		eta.setText(min + " mins from now");

		amount.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				String a = amount.getText().toString();
				if (!hasFocus)
					amountTBC.setText(a, TextView.BufferType.EDITABLE);
			}
		});

		bookNow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final String amountText = amount.getText().toString();
				final String amountTBCText = amountTBC.getText().toString();
				final String custMobText = custMobNum.getText().toString();
				final String noOfShipText = noOfShip.getText().toString();
				Log.d("BookNow_NoOfShipments", noOfShipText);
				final String custNameText = custName.getText().toString();
				final String custAddText = custAdd.getText().toString();
				final String custStreetText = custStreet.getText().toString();
				final String custAreaText = custArea.getText().toString();
				final String custCityText = custCity.getText().toString();
				final String custPinText = custPincode.getText().toString();

				long lastOrder = sharedPreferences.getLong("lastOrder", 0);
				String cMobile = sharedPreferences.getString("cMobile", null);
				String lastOrderAmt = sharedPreferences.getString(
						"lastOrderAmt", null);
				long currTime = System.currentTimeMillis();
				long timeGap = currTime - lastOrder;
				Log.d("BookNow_TimeGAP", timeGap + "");

				if (timeGap < 300000 && cMobile.equals(custMobText)
						&& lastOrderAmt.equals(amountText)) {
					Log.d("BookNow_RepeatedFlag", repeatedFlag + "");
					repeatedFlag = 1;
					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setMessage("Order is same as previous one. Do you still want to continue?.");
					builder.setPositiveButton("Continue",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									editor.putLong("lastOrder", 0);
									editor.commit();
									repeatedFlag = 0;
									Log.d("BookNow_RepeatedFlag", repeatedFlag
											+ "");

									Log.d("BookNow_RepeatedFlag", repeatedFlag
											+ "");
									if (!custAddText.isEmpty()) {
										addFlag = addFlag + 1;
									}
									if (!custStreetText.isEmpty()) {
										addFlag = addFlag + 1;
									}
									if (!custAreaText.isEmpty()) {
										addFlag = addFlag + 1;
									}
									if (!custCityText.isEmpty()) {
										addFlag = addFlag + 1;
									}
									if (!custPinText.isEmpty()) {
										addFlag = addFlag + 1;
									}

									jsonObj = new JSONObject();
									customerDetails = new JSONObject();
									address = new JSONObject();

									if (amountText.isEmpty()) {
										amount.setError("Cannot Be Left Blank");
										amount.requestFocus();
									} else if (amountTBCText.isEmpty()) {
										amountTBC.setText("0");
									} else if (!amountTBCText.isEmpty()
											&& Integer.parseInt(amountText) < Integer
													.parseInt(amountTBCText)) {
										amountTBC
												.setError("Invalid COD Amount");
										amountTBC.requestFocus();
									} else if (noOfShipText.isEmpty()) {
										noOfShip.setError("Cannot Be Left Blank");
									} else if (custMobText.isEmpty()) {
										custMobNum
												.setError("Cannot Be Left Blank");
										custMobNum.requestFocus();
									} else if (!custMobText.matches(regexStr)) {
										custMobNum.setError("Invalid Number");
										custMobNum.requestFocus();
									} else if (!custNameText.isEmpty()
											&& !isValidName(custNameText)) {
										custName.setError("Invalid Name");
										custName.requestFocus();
									} else if (addFlag != 0 && addFlag != 5) {
										Toast.makeText(context,
												"Enter Complete Address",
												Toast.LENGTH_LONG).show();
										addFlag = 0;
									} else if (addFlag == 5
											&& custPinText.length() != 6) {
										custPincode
												.setError("Invalid Pin Code");
										custPincode.requestFocus();
									} else if (!amountText.isEmpty()
											&& custMobText.matches(regexStr)) {
										if (addFlag == 5) {
											try {
												address.put("address",
														custAddText);
												address.put("street",
														custStreetText);
												address.put("area",
														custAreaText);
												address.put("city",
														custCityText);
												address.put("pinCode",
														custPinText);
												customerDetails.put("address",
														address);
											} catch (JSONException e2) {
												// TODO Auto-generated catch
												// block
												e2.printStackTrace();
											}
											Log.d("BookNow_AddressJSONSent", ""
													+ address);
										}
										try {
											customerDetails.put("name",
													custNameText);
											customerDetails.put("mobile",
													custMobText);
										} catch (JSONException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										Log.d("BookNow_CustomerDetailsSent", ""
												+ customerDetails);
										try {
											String rDetails = sharedPreferences
													.getString(
															"retailerDetails",
															null);
											retailerDetails = new JSONObject(
													rDetails);
											retailerDetails.remove("email");
											retailerDetails
													.remove("registrationStatus");
											retailerDetails.remove("state");
											retailerDetails.remove("country");
											retailerDetails.remove("plainPass");
											retailerDetails.remove("landmark");
											retailerDetails.remove("id");
											retailerDetails.remove("OTPChanged");
											Log.d("BookNow_RetailerDetailsSent",
													retailerDetails.toString());
											int orderAmount = Integer
													.parseInt(amountText);
											// SimpleDateFormat sdf = new
											// SimpleDateFormat(
											// "yyyy-MM-dd'T'HH:mm:ssZ",
											// Locale.UK);
											// String formattedDate =
											// sdf.format(new Date());

											String resType = sharedPreferences
													.getString("resType", null);
											jsonObj.put("orderAmount",
													amountText);
											jsonObj.put("shipment",
													noOfShipText);
											jsonObj.put("paymentType", "COD");
											jsonObj.put("resourceType", resType);
											jsonObj.put("CODValue",
													amountTBCText);
											jsonObj.put("retailerDetails",
													retailerDetails);
											jsonObj.put("customerDetails",
													customerDetails);

											SimpleDateFormat sdf = new SimpleDateFormat(
													"yyyy-MM-dd'T'HH:mm:ssZ",
													Locale.UK);
											String formattedDate = sdf
													.format(new Date());
											jsonObj.put("bookNowTime",
													formattedDate);

											Log.d("BookNow_JSONObjectSENT", ""
													+ jsonObj);

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
					builder.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									Log.d("BookNow_RepeatedFlag", repeatedFlag
											+ "");
								}
							});
					builder.show();
				}

				if (repeatedFlag <= 0 || timeGap >= 300000) {
					Log.d("BookNow_RepeatedFlag", repeatedFlag + "");
					if (!custAddText.isEmpty()) {
						addFlag = addFlag + 1;
					}
					if (!custStreetText.isEmpty()) {
						addFlag = addFlag + 1;
					}
					if (!custAreaText.isEmpty()) {
						addFlag = addFlag + 1;
					}
					if (!custCityText.isEmpty()) {
						addFlag = addFlag + 1;
					}
					if (!custPinText.isEmpty()) {
						addFlag = addFlag + 1;
					}

					jsonObj = new JSONObject();
					customerDetails = new JSONObject();
					address = new JSONObject();

					if (amountText.isEmpty()) {
						amount.setError("Cannot Be Left Blank");
						amount.requestFocus();
					} else if (amountTBCText.isEmpty()) {
						amountTBC.setText("0");
					} else if (!amountTBCText.isEmpty()
							&& Integer.parseInt(amountText) < Integer
									.parseInt(amountTBCText)) {
						amountTBC.setError("Invalid COD Amount");
						amountTBC.requestFocus();
					} else if (noOfShipText.isEmpty()) {
						noOfShip.setError("Cannot Be Left Blank");
					} else if (custMobText.isEmpty()) {
						custMobNum.setError("Cannot Be Left Blank");
						custMobNum.requestFocus();
					} else if (!custMobText.matches(regexStr)) {
						custMobNum.setError("Invalid Number");
						custMobNum.requestFocus();
					} else if (!custNameText.isEmpty()
							&& !isValidName(custNameText)) {
						custName.setError("Invalid Name");
						custName.requestFocus();
					} else if (addFlag != 0 && addFlag != 5) {
						Toast.makeText(context, "Enter Complete Address",
								Toast.LENGTH_LONG).show();
						addFlag = 0;
					} else if (addFlag == 5 && custPinText.length() != 6) {
						custPincode.setError("Invalid Pin Code");
						custPincode.requestFocus();
					} else if (!amountText.isEmpty()
							&& custMobText.matches(regexStr)) {
						if (addFlag == 5) {
							try {
								address.put("address", custAddText);
								address.put("street", custStreetText);
								address.put("area", custAreaText);
								address.put("city", custCityText);
								address.put("pinCode", custPinText);
								customerDetails.put("address", address);
							} catch (JSONException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
							Log.d("BookNow_AddressJSONSent", "" + address);
						}
						try {
							customerDetails.put("name", custNameText);
							customerDetails.put("mobile", custMobText);
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Log.d("BookNow_CustomerDetailsSent", ""
								+ customerDetails);
						try {
							String rDetails = sharedPreferences.getString(
									"retailerDetails", null);
							retailerDetails = new JSONObject(rDetails);
							retailerDetails.remove("email");
							retailerDetails.remove("registrationStatus");
							retailerDetails.remove("state");
							retailerDetails.remove("country");
							retailerDetails.remove("plainPass");
							retailerDetails.remove("landmark");
							retailerDetails.remove("id");
							Log.d("BookNow_RetailerDetailsSent",
									retailerDetails.toString());
							int orderAmount = Integer.parseInt(amountText);
							// SimpleDateFormat sdf = new SimpleDateFormat(
							// "yyyy-MM-dd'T'HH:mm:ssZ", Locale.UK);
							// String formattedDate = sdf.format(new Date());

							String resType = sharedPreferences.getString(
									"resType", null);

							jsonObj.put("orderAmount", amountText);
							jsonObj.put("shipment", noOfShipText);
							jsonObj.put("paymentType", "COD");
							jsonObj.put("resourceType", resType);
							jsonObj.put("CODValue", amountTBCText);
							jsonObj.put("retailerDetails", retailerDetails);
							jsonObj.put("customerDetails", customerDetails);

							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd'T'HH:mm:ssZ", Locale.UK);
							String formattedDate = sdf.format(new Date());
							jsonObj.put("bookNowTime", formattedDate);

							Log.d("BookNow_JSONObjectSENT", "" + jsonObj);

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

			}
		});

	}

	private void makeJsonObjReq(final JSONObject jObject) {
		// String tag_json_obj = "json_obj_req";

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);
		pDialog.show();

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST, url,
				jObject, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d("BookNow_ResponseBookNow", response.toString());
						try {
							String message = response.getString("message");
							JSONObject details = response
									.getJSONObject("details");
							JSONObject order = details.getJSONObject("order");
							String retailerMobile = order
									.getString("retailerMobile");
							String customerMobile = order
									.getString("customerMobile");
							// String retailerId =
							// order.getString("retailerId");
							String orderAmount = order.getString("orderAmount");

							Toast.makeText(context, message, Toast.LENGTH_LONG)
									.show();

							long time = System.currentTimeMillis();
							repeatedFlag = 0;
							editor.putLong("lastOrder", time);
							editor.putString("rMobile", retailerMobile);
							editor.putString("cMobile", customerMobile);
							editor.putString("lastOrderAmt", orderAmount);
							editor.commit();

							Intent i = new Intent(BookNowActivity.this,
									DashboardActivity.class);
							startActivity(i);
							finish();

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						pDialog.dismiss();

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						VolleyLog.d("BookNow_Volley_Error",
								"Error: " + error.getMessage());
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
		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(0, 10,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		AppController.getInstance().addToRequestQueue(jsonObjReq);
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

	private boolean isValidName(String name) {
		String NAME_PATTERN = "^[\\p{L} .'-]+$";
		Pattern pattern = Pattern.compile(NAME_PATTERN);
		Matcher matcher = pattern.matcher(name);
		return matcher.matches();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (pDialog != null) {
			pDialog.dismiss();
		}
	}

}