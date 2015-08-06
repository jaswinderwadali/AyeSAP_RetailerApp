package com.retailerapp.ayesap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NoInternet {

	public void networkError(Context ctx) {

		if (!isOnline(ctx)) {

			AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
			builder.setMessage("Please check your internet connection.");
			builder.setTitle("No Internet Connection");
			builder.setPositiveButton("Retry",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
						}
					});
			builder.show();
		}
	}

	public void networkErrorMain(final Context ctx) {

		if (!isOnline(ctx)) {

			AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
			builder.setMessage("Please check your internet connection.");
			builder.setTitle("No Internet Connection");
			builder.setPositiveButton("Retry",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							((Activity)ctx).finish();
						}
					});
			builder.show();
		}
	}

	public boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

}
