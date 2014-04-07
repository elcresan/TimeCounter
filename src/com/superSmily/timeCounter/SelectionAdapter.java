package com.superSmily.timeCounter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

//http://www.limecreativelabs.com/seleccion-multiple-listview-contextual-action-bar/
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
	
	/**
     * Deselect the item at @param position
     */
	public void removeSelection(int position){
		selection.remove(position);
		notifyDataSetChanged();
	}
	/**
     * Empty the array of selected items
     */
	
	public void clearSelection(){
		selection = new ArrayList<Integer>();
		notifyDataSetChanged();
	}
	/**
     * Get the array with selected items
     * @return Selection count
     */
	public 	ArrayList<Integer> getSelected(){
		return selection;
	}
    /**
     * Get number of selected items
     * @return Selection count
     */
    public int getSelectionCount() {
        return selection.size();
    }
    
    
    
}
