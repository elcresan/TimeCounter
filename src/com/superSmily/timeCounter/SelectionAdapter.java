package com.superSmily.timeCounter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;


//Class made to handle selections and actionbar from ChronosList

public class SelectionAdapter extends ArrayAdapter<String> {

	private ArrayList<Integer> selection = new ArrayList<Integer>();
	
	public SelectionAdapter(Context ctx, int resource,
			int textViewResourceId, List<String> objects){
		super(ctx, resource, textViewResourceId, objects);
	}
	
	// New item selected and update the view
	public void setNewSelection(int position){
		selection.add(position);
		notifyDataSetChanged();
	}
	
	public 	ArrayList<Integer> getSelected(){
		return selection;
	}
}
