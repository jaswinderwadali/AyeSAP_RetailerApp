package com.retailerapp.ayesap;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class OutForDeliveryAdapter extends BaseExpandableListAdapter {

	private Context ctx;

	List<Parent> ParentView;
	HashMap<String, Child> childView;

	public OutForDeliveryAdapter(Context ctx, List<Parent> ParentView,
			HashMap<String, Child> ChildView) {
		this.ctx = ctx;

		this.ParentView = ParentView;
		this.childView = ChildView;
	}

	@Override
	public Object getChild(int parent, int child) {
		return childView.get(ParentView.get(parent).getOrderId());
	}

	@Override
	public long getChildId(int parent, int child) {
		// TODO Auto-generated method stub
		return child;
	}

	@Override
	public View getChildView(int parent, int child, boolean lastChild,
			View convertview, ViewGroup parentview) {

		Parent parentItems = ParentView.get(parent);
		Child childItems = childView.get(parentItems.getOrderId());

		if (convertview == null) {
			LayoutInflater inflator = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertview = inflator.inflate(R.layout.child_out_for_delivery,
					parentview, false);
		}

		TextView customerName = (TextView) convertview.findViewById(R.id.name);
		TextView addLine = (TextView) convertview.findViewById(R.id.addLine);
		// TextView addLine2 = (TextView)
		// convertview.findViewById(R.id.addLine2);
		// TextView addLine3 = (TextView)
		// convertview.findViewById(R.id.addLine3);

		customerName.setText(childItems.getCustomerName());
		addLine.setText(childItems.getCustomerAddress());
		// addLine2.setText(childItems.getAddLine2());
		// addLine3.setText(childItems.getAddLine3());

		return convertview;
	}

	@Override
	public int getChildrenCount(int child) {
		return 1;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return ParentView.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return ParentView.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int parent, boolean isExpanded, View convertview,
			ViewGroup parentview) {
		// TODO Auto-generated method stub
		Parent parentItems = (Parent) getGroup(parent);

		if (convertview == null) {
			LayoutInflater inflator = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertview = inflator.inflate(R.layout.parent_out_for_delivery,
					parentview, false);
		}
		TextView customerMob = (TextView) convertview
				.findViewById(R.id.customerMob);
		TextView orderAmt = (TextView) convertview.findViewById(R.id.amount);
		TextView orderId = (TextView) convertview.findViewById(R.id.orderId);
		TextView bookedAt = (TextView) convertview.findViewById(R.id.time);

		orderId.setText(parentItems.getOrderId());
		customerMob.setText(parentItems.getCustomerMobile());
		orderAmt.setText(parentItems.getOrderAmount());

		String bookNT = parentItems.getBookNowTime();

		if (bookNT != null && !bookNT.isEmpty()) {
			Log.d("OutForDelivery_bookNT", bookNT);
			String theTime = bookNT.substring(11, 16);
			Log.d("OutForDelivery_theTime", theTime);
			String theDate = "(" + bookNT.substring(8, 10) + "-"
					+ bookNT.substring(5, 7) + "-" + bookNT.substring(0, 4)
					+ ")";
			Log.d("OutForDelivery_theDate", theDate);
			String finalDtTime = theTime + " " + theDate;
			Log.d("OutForDelivery_finalDtTime", finalDtTime);
			bookedAt.setText(finalDtTime);
		}

		return convertview;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public void clear() {
		// TODO Auto-generated method stub
		ParentView.clear();
		childView.clear();
		notifyDataSetChanged();
	}

}
