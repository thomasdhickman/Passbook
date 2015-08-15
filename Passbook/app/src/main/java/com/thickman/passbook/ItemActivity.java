package com.thickman.passbook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ItemActivity extends MasterActivity {

	private List<Item> allItems = new ArrayList<Item>();
	private Database database;
	private ListView lvItems;
	private Item selectedItem;
	private ArrayAdapter<Item> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content);

		database = new Database(this);
		database.open();

		allItems = database.getAllItems();

		Collections.sort(allItems, new Comparator<Item>() {
			public int compare(Item left, Item right) {
				return left.getKey().toLowerCase()
						.compareTo(right.getKey().toLowerCase());
			}
		});

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
				newFragment.show(getFragmentManager(), getResources()
						.getString(R.string.delete_item_ask));
				return true;
			}
		});

		FloatingActionButton fabButton = new FloatingActionButton.Builder(this)
				.withDrawable(
						getResources().getDrawable(
								R.drawable.ic_plus_white_24dp))
				.withButtonColor(getResources().getColor(R.color.blue))
				.withGravity(Gravity.BOTTOM | Gravity.RIGHT)
				.withMargins(0, 0, 12, 12).create();
		fabButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addItem();
			}
		});
	}

	@SuppressLint("ValidFragment")
	public class LongClickDialog extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			builder.setTitle(selectedItem.getKey());
			builder.setItems(R.array.longpressoptions,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								copyToClipboard(selectedItem.getValue1(),
										"Username");
								break;
							case 1:
								copyToClipboard(selectedItem.getValue2(),
										"Password");
								break;
							case 2:
								deleteItem();
								break;
							default:
								break;
							}
						}
					});
			return builder.create();
		}
	}

	public void copyToClipboard(String item, String itemName) {
		ClipboardManager clipboard = (ClipboardManager) this
				.getSystemService(Activity.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText(itemName, item);
		clipboard.setPrimaryClip(clip);
		Functions.makeAToast(this, itemName + " copied to clipboard.");
	}

	public void deleteItem() {
		Functions.makeAToast(getApplicationContext(),
				database.removeItem(selectedItem.getId()));
		allItems.remove(selectedItem);
		refresh();
	}

	public void refresh() {
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void addItem() {
		Intent i = new Intent(this, AddItemActivity.class);
		i.putExtra(AddItemActivity.KEY, "");
		i.putExtra(AddItemActivity.VAL1, "");
		i.putExtra(AddItemActivity.VAL2, "");
		i.putExtra(AddItemActivity._ID, 0);
		startActivity(i);
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int id = item.getItemId();
		if (id == R.id.action_add) {
			addItem();
			return true;
		} else if (id == R.id.action_about) {
			Intent j = new Intent(this, AboutActivity.class);
			startActivity(j);
			return true;
		} else if (id == R.id.action_change_pin) {
			Intent k = new Intent(this, ChangePinActivity.class);
			startActivity(k);
			finish();
			return true;
		} else if (id == R.id.action_change_security_question) {
			Intent n = new Intent(this, SecurityQuestionActivity.class);
			n.putExtra(SecurityQuestionActivity.ARG_ALLOW_BACK, true);
			startActivity(n);
			finish();
			return true;
		} else if (id == R.id.action_log_out) {
			onBackPressed();
			return true;
		} else if (id == R.id.action_import_export) {
			Intent l = new Intent(this, ImportExportActivity.class);
			startActivity(l);
			finish();
			return true;
		} else if (id == R.id.action_password_generator) {
			Intent m = new Intent(this, PasswordGeneratorActivity.class);
			startActivity(m);
			finish();
			return true;
		} else if (id == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressLint("ValidFragment")
	public class LogOutDialog extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			builder.setTitle(getResources().getString(R.string.log_out));
			builder.setMessage(getResources().getString(R.string.log_out_ask))
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Toast.makeText(
											getApplicationContext(),
											getResources().getString(
													R.string.log_out_confirm),
											Toast.LENGTH_LONG).show();

									Intent i = new Intent(ItemActivity.this,
											MainActivity.class);
									startActivity(i);
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
		newFragment.show(getFragmentManager(),
				getResources().getString(R.string.log_out_ask));
	}
}
