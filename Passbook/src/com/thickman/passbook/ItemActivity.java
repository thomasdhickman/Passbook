package com.thickman.passbook;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ItemActivity extends Activity {

	private List<Item> allItems;
	private Database database;
	private ListView lvItems;
	private Item selectedItem;
	private int LOG_OUT = 2;
	private ArrayAdapter<Item> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content);

		database = new Database(this);
		database.open();

		allItems = database.getAllItems();
		adapter = new ItemAdapter(getApplicationContext(),
				R.layout.item_adapter, allItems);

		lvItems = (ListView) findViewById(R.id.lvItems);
		lvItems.setAdapter(adapter);
		lvItems.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectedItem = allItems.get(position);
				Intent i = new Intent(ItemActivity.this, AddItemActivity.class);
				i.putExtra(AddItemActivity.KEY, selectedItem.getKey());
				i.putExtra(AddItemActivity.VAL1, selectedItem.getValue1());
				i.putExtra(AddItemActivity.VAL2, selectedItem.getValue2());
				i.putExtra(AddItemActivity._ID, selectedItem.getId());
				startActivity(i);
				finish();
			}
		});
		lvItems.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				selectedItem = allItems.get(arg2);
				DialogFragment newFragment = new LongClickDialog();
				newFragment.show(getFragmentManager(), "Delete item?");
				return true;
			}
		});
	}

	@SuppressLint("ValidFragment")
	public class LongClickDialog extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			builder.setTitle(R.string.delete_item);
			builder.setMessage("Delete item?")
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Toast.makeText(
											getApplicationContext(),
											database.removeItem(selectedItem
													.getId()),
											Toast.LENGTH_LONG).show();
									allItems.remove(selectedItem);
									refresh();
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							});
			return builder.create();
		}
	}

	public void refresh() {
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			Intent i = new Intent(this, AddItemActivity.class);
			i.putExtra(AddItemActivity.KEY, "");
			i.putExtra(AddItemActivity.VAL1, "");
			i.putExtra(AddItemActivity.VAL2, "");
			i.putExtra(AddItemActivity._ID, 0);
			startActivity(i);
			finish();
			return true;
		case (R.id.action_about):
			Intent j = new Intent(this, AboutActivity.class);
			startActivity(j);
			return true;
		case (R.id.action_change_pin):
			Intent k = new Intent(this, ChangePinActivity.class);
			startActivity(k);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@SuppressLint("ValidFragment")
	public class LogOutDialog extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			builder.setTitle("Log off");
			builder.setMessage("Do you want to log off?")
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Toast.makeText(getApplicationContext(),
											"You are now logged off.",
											Toast.LENGTH_LONG).show();
									finish();
								}
							})
					.setNegativeButton(R.string.no,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							});
			return builder.create();
		}
	}

	@Override
	public void onBackPressed() {
		DialogFragment newFragment = new LogOutDialog();
		newFragment.show(getFragmentManager(), "Log out?");
	}
}
