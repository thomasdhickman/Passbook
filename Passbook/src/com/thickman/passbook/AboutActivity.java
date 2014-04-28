package com.thickman.passbook;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AboutActivity extends Activity {

	@Override
	public void onCreate(Bundle s) {
		super.onCreate(s);
		setContentView(R.layout.activity_about);
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case (android.R.id.home): {
			onBackPressed();
			finish();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
