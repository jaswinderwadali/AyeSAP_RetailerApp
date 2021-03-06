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

public class PickupNewActivity extends AppCompatActivity implements FetchData {

    Context context;

    private Toolbar toolbar;

    LatestData latestData;
    NoInternet noInternet;

    List<Parent> parents;
    HashMap<String, Child> children;

    ExpandableListView Exp_list;
    PickupNewAdapter adapter;

    TextView noOrder;

    ProgressDialog pDialog;

    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_list);
        noInternet = new NoInternet();

        parents = new ArrayList<Parent>();
        children = new HashMap<String, Child>();
        context = this;

        latestData = new LatestData(getApplicationContext(),
                PickupNewActivity.this);

        noOrder = (TextView) findViewById(R.id.noOrders);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        latestData.jsonReqLatestData();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setText("Out for Pickup");

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

        if (LatestData.pickupTCount == 0)
            noOrder.setVisibility(View.VISIBLE);
        else
            noOrder.setVisibility(View.GONE);


//		Exp_list.setFocusable(false);
        Exp_list.setGroupIndicator(null);

//Exp_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//@Override
//public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) (
// return true;
//}
//});
        adapter = new PickupNewAdapter(context, LatestData.outForPickupParent,
                LatestData.outForPickupChild);
        Exp_list.setAdapter(adapter);

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
