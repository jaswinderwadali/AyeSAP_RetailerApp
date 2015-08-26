package com.tanmay.ayesapdevtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class OrderSummaryActivity extends AppCompatActivity implements
		FetchData {

	Context context;

	private SwipeRefreshLayout swipeContainer;

	TextView todayPickup, todayOutForDel, todayDelivered, todayFailed, todayCancelled;
	TextView allPickup, allOutForDel, allDelivered, allFailed, allCancelled;

	int a, b, c, d, e, f, g, h, i, j;

	private Toolbar toolbar;

	LatestData latestData;
	NoInternet noInternet;
	ProgressDialog pDialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_summary);

		context = this;
		noInternet = new NoInternet();

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		TextView title = (TextView) toolbar.findViewById(R.id.title);
		title.setText("Order Summary");

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(true);
		pDialog.show();

		latestData = new LatestData(getApplicationContext(),
				OrderSummaryActivity.this);
		latestData.jsonReqLatestData();
		todayPickup = (TextView) findViewById(R.id.todayPickupNum);
		todayOutForDel = (TextView) findViewById(R.id.todayOutForDelNum);
		todayDelivered = (TextView) findViewById(R.id.todayDeliveredNum);
		todayFailed = (TextView) findViewById(R.id.todayFailedNum);
		todayCancelled = (TextView) findViewById(R.id.todayCancelledNum);

		allPickup = (TextView) findViewById(R.id.allPickupNum);
		allOutForDel = (TextView) findViewById(R.id.allOutForDelNum);
		allDelivered = (TextView) findViewById(R.id.allDeliveredNum);
		allFailed = (TextView) findViewById(R.id.allFailedNum);
		allCancelled = (TextView) findViewById(R.id.allCancelledNum);

		swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
		// Setup refresh listener which triggers new data loading
		swipeContainer.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Your code to refresh the list here.
				// Make sure you call swipeContainer.setRefreshing(false)
				// once the network request has completed successfully.
				fetchTimelineAsync(0);
			}
		});
		// Configure the refreshing colors
		swipeContainer.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

	}

	@Override
	public void displayData() {
		// TODO Auto-generated method stub
		pDialog.dismiss();

		a = LatestData.pickupCount;
		b = LatestData.deliveryCount;
		c = LatestData.deliveredCount;
		d = LatestData.pickupTCount;
		e = LatestData.deliveryTCount;
		f = LatestData.deliveredTCount;
		i = LatestData.failedCount;
		j = LatestData.failedTCount;
		g = LatestData.cancelledCount;
		h = LatestData.cancelledTCount;
		
		todayPickup.setText(a + " ");
		todayOutForDel.setText(b + " ");
		todayDelivered.setText(c + " ");
		todayFailed.setText(i + " ");
		todayCancelled.setText(g + " ");
		
		allPickup.setText(d + " ");
		allOutForDel.setText(e + " ");
		allDelivered.setText(f + " ");
		allFailed.setText(j + " ");
		allCancelled.setText(h + " ");
		swipeContainer.setRefreshing(false);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	public void fetchTimelineAsync(int page) {

		// ...the data has come back, add new items to your adapter...
		noInternet.networkError(context);
		if (noInternet.isOnline(context)) {
			latestData.jsonReqLatestData();
		}// Now we call setRefreshing(false) to signal refresh has finished
		else {
			swipeContainer.setRefreshing(false);
		}

	}

}
