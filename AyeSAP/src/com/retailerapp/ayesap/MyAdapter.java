package com.retailerapp.ayesap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;

	private String[] mNavTitles;
	private String Name;
	private int dp;
	public static OnButtonClickListener click;
	Context context;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		int Holderid;

		TextView textView;
		TextView nameView;
		ImageView DP;

		public ViewHolder(View itemView, int ViewType) {
			super(itemView);

			if (ViewType == TYPE_ITEM) {
				textView = (TextView) itemView.findViewById(R.id.rowText); // Creating
																			// TextView
																			// object
																			// with
																			// the
																			// id
																			// of
																			// textView
																			// from
																			// item_row.xml
				Holderid = 1; // setting holder id as 1 as the object being
								// populated are of type item row
			} else {

				nameView = (TextView) itemView.findViewById(R.id.name); // Creating
																		// Text
																		// View
																		// object
																		// from
																		// header.xml
																		// for
																		// name
				DP = (ImageView) itemView.findViewById(R.id.circleView);// Creating
																		// Image
																		// view
																		// object
																		// from
																		// header.xml
																		// for
																		// profile
																		// pic
				Holderid = 0; // Setting holder id = 0 as the object being
								// populated are of type header view
			}
		}

	}

	MyAdapter(Context context, String Titles[], String name, int DP) {
		mNavTitles = Titles;
		this.context = context;
		Name = name;
		dp = DP;
	}

	@Override
	public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
			int viewType) {

		if (viewType == TYPE_ITEM) {
			View v = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.row_item, parent, false); // Inflating the layout

			ViewHolder vhItem = new ViewHolder(v, viewType); // Creating
																// ViewHolder
																// and passing
																// the object of
																// type view

			return vhItem; // Returning the created object

			// inflate your layout and pass it to view holder

		} else if (viewType == TYPE_HEADER) {

			View v = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.header, parent, false); // Inflating the layout

			ViewHolder vhHeader = new ViewHolder(v, viewType); // Creating
																// ViewHolder
																// and passing
																// the object of
																// type view

			return vhHeader; // returning the object created

		}
		return null;
	}

	@Override
	public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
		if (holder.Holderid == 1) { // as the list view is going to be called
									// after the header view so we decrement the
									// position by 1 and pass it to the holder
									// while setting the text and image
			holder.textView.setText(mNavTitles[position - 1]); // Setting the
																// Text with the
																// array of our
																// Titles
			holder.textView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					click.onOptionClick(position - 1);
				}
			});
		} else {

			holder.DP.setImageResource(dp); // Similarly we set the resources
											// for header view
			holder.nameView.setText(Name);
		}
	}

	@Override
	public int getItemCount() {
		return mNavTitles.length + 1; // the number of items in the list will be
										// +1 the titles including the header
										// view.
	}

	// Witht the following method we check what type of view is being passed
	@Override
	public int getItemViewType(int position) {
		if (isPositionHeader(position))
			return TYPE_HEADER;

		return TYPE_ITEM;
	}

	private boolean isPositionHeader(int position) {
		return position == 0;
	}

}