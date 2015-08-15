package com.thickman.passbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddItemActivity extends MasterActivity {

	private EditText etKey;
	private EditText etValue1;
	private EditText etValue2;
	private Button btnAdd;
	private Database database;
	private String sKey;
	private String sVal1;
	private String sVal2;
	private int _id;
	private boolean isEdit = false;

	public final static String KEY = "KEY";
	public final static String VAL1 = "VAL1";
	public final static String VAL2 = "VAL2";
	public final static String _ID = "_ID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_item);

		database = new Database(this);
		database.open();

		etKey = (EditText) findViewById(R.id.etKey);
		etValue1 = (EditText) findViewById(R.id.etValue1);
		etValue2 = (EditText) findViewById(R.id.etValue2);

		try {
			Bundle extras = getIntent().getExtras();
			sKey = extras.getString(KEY, "");
			sVal1 = extras.getString(VAL1, "");
			sVal2 = extras.getString(VAL2, "");
			_id = extras.getInt(_ID, 0);
		} catch (Exception e) {
			sKey = "";
			sVal1 = "";
			sVal2 = "";
			_id = 0;
		}

		if (!sKey.equals("")) {
			isEdit = true;
			etKey.setText(sKey);
			etValue1.setText(sVal1);
			etValue2.setText(sVal2);
		}

		etKey.requestFocus();

		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (etKey.getText().length() != 0) {
					if (etValue1.getText().length() != 0) {
						if (!isEdit) {
							Item item = new Item(etKey.getText().toString(),
									etValue1.getText().toString(), etValue2
											.getText().toString());
							Toast.makeText(getApplicationContext(),
									database.addItem(item), Toast.LENGTH_LONG)
									.show();
						} else {
							Item item = new Item(etKey.getText().toString(),
									etValue1.getText().toString(), etValue2
											.getText().toString());
							item.setId(_id);
							Toast.makeText(getApplicationContext(),
									database.updateItem(item),
									Toast.LENGTH_LONG).show();
						}

						Intent i = new Intent(AddItemActivity.this,
								ItemActivity.class);
						startActivity(i);
						finish();

					} else
						etValue1.setError(getResources().getString(
								R.string.required_field));
				} else
					etKey.setError(getResources().getString(
							R.string.required_field));
			}
		});
		getActionBar().setTitle(getResources().getString(R.string.add_item));
	}
}
