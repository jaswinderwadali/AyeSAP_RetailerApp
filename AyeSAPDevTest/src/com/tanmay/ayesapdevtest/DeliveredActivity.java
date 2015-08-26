package com.tanmay.ayesapdevtest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class DeliveredActivity extends AppCompatActivity implements FetchData {

	Context context;
	LatestData latestData;

	private Toolbar toolbar;

	List<Parent> parents;
	HashMap<String, Child> children;

	ExpandableListView Exp_list;
	DeliveredAdapter adapter;

	ProgressDialog pDialog;
	NoInternet noInternet;

	TextView noOrder;

	private SwipeRefreshLayout swipeContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expandable_list);

		parents = new ArrayList<Parent>();
		children = new HashMap<String, Child>();
		context = this;
		noInternet = new NoInternet();

		swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		TextView title = (TextView) toolbar.findViewById(R.id.title);
		title.setText("Completed");

		latestData = new LatestData(context, DeliveredActivity.this);

		noOrder = (TextView) findViewById(R.id.noOrders);

		latestData.jsonReqLatestData();
		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Loading...");
		pDialog.show();

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
		Exp_list = (ExpandableListView) findViewById(R.id.expList);

		adapter = new DeliveredAdapter(context, LatestData.deliveredParent,
				LatestData.deliveredChild);
		Exp_list.setAdapter(adapter);
		
		if (LatestData.deliveredTCount == 0)
			noOrder.setVisibility(View.VISIBLE);
		if (LatestData.deliveredTCount != 0)
			noOrder.setVisibility(View.GONE);
		
		swipeContainer.setRefreshing(false);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	public void fetchTimelineAsync(int page) {

		// Remember to CLEAR OUT old items before appending in the new ones
		noInternet.networkError(context);
		if (noInternet.isOnline(context)) {
			adapter.clear();
			// ...the data has come back, add new items to your adapter...
			latestData.jsonReqLatestData();
			// Now we call setRefreshing(false) to signal refresh has finished
		} else {
			swipeContainer.setRefreshing(false);
		}
	}

}
