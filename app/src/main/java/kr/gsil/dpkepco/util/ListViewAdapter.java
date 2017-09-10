package kr.gsil.dpkepco.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ListViewAdapter extends BaseAdapter {

	public abstract View setListGetView(int position, View convertView, ViewGroup parent);
	public abstract int setCount();
	
	@Override
	public int getCount() {
		return setCount();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return setListGetView(position, convertView, parent);
	}
}