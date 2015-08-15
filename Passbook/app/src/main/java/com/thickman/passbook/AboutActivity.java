package com.thickman.passbook;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends MasterActivity {

	private String intent = "other";

	@Override
	public void onCreate(Bundle s) {
		super.onCreate(s);
		setContentView(R.layout.activity_about);

		try {
			Bundle extras = getIntent().getExtras();
			intent = extras.getString("intent", "other");
		} catch (Exception ex) {
			intent = "other";
		}
		Button button = (Button) findViewById(R.id.btnRate);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("market://details?id="
						+ getApplicationContext().getPackageName());
				Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
				try {
					startActivity(goToMarket);
				} catch (ActivityNotFoundException e) {
					startActivity(new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("http://play.google.com/store/apps/details?id="
									+ getApplicationContext().getPackageName())));
				}
			}
		});
		getActionBar().setTitle(getResources().getString(R.string.about_passbook));
	}

	@Override
	public void onBackPressed() {
		if (intent.equals("home")) {
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			finish();
		} else
			super.onBackPressed();
	}
}
