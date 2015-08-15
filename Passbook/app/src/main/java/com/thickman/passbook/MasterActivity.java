package com.thickman.passbook;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class MasterActivity extends Activity {

	@Override
	protected void onCreate(Bundle s) {
		super.onCreate(s);
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setBackgroundDrawable(getResources().getDrawable(R.color.blue));
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public void onRestart() {
		super.onRestart();
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		Log.d("Logout", "Logout due to inactivity");
		finish();
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(this, ItemActivity.class);
		startActivity(i);
		finish();
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
