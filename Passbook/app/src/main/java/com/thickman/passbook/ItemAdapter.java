package com.thickman.passbook;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ItemAdapter extends ArrayAdapter<Item> {
private LayoutInflater mInflater;
    
	private List<Item> lItem;
    private int mViewResourceId;
     
    public ItemAdapter(Context ctx, int viewResourceId, List<Item> item) {
        super(ctx, viewResourceId);
        
        mInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lItem = item;
        
        mViewResourceId = viewResourceId;
    }

	public int getCount() {
    	return lItem.size();
    }
    
    public Item getItem(int position) {
    	return lItem.get(position);
    }
    
    public long getItemId(int position) {
    	return position;
    }
    
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);

        TextView txtKey = (TextView)convertView.findViewById(R.id.txtKey);
        txtKey.setText(getItem(position).getKey());

        TextView txtValue1 = (TextView)convertView.findViewById(R.id.txtValue1);
        txtValue1.setText(getItem(position).getValue1());

        TextView txtValue2 = (TextView)convertView.findViewById(R.id.txtValue2);
        txtValue2.setText(getItem(position).getValue2());

        return convertView;
    }
}