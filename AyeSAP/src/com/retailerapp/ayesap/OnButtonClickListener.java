package com.retailerapp.ayesap;

public interface OnButtonClickListener {

	public void onOptionClick(int position);

	public void onCardOptionClick(int position, boolean isCancel);

	public void onSettingClick(int position);

}
