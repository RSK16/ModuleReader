package com.reader.modulereader.function;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class MyEpListAdapter extends ArrayAdapter<Object>
{

	@SuppressWarnings("unchecked")
	public MyEpListAdapter(Context context, int resource,
			int textViewResourceId, @SuppressWarnings("rawtypes") List objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}
	
}
