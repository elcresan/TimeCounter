package com.superSmily.timeCounter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

//http://www.limecreativelabs.com/seleccion-multiple-listview-contextual-action-bar/
//Class made to handle selections and actionbar from ChronosList

public class SelectionAdapter extends ArrayAdapter<Activity> {

	private ArrayList<Integer> selection = new ArrayList<Integer>();
	private ArrayList<Activity> activities = new ArrayList<Activity>();
	private Context context;
	
	// Puede que haya que hacer otro array para saber si se esta el chrono en marcha y
	// cambiar el color del row
	
	public SelectionAdapter(Context ctx, int resource,
			int textViewResourceId, ArrayList<Activity> objects){
		
		super(ctx, resource, textViewResourceId, objects);
		context = ctx;
		activities = objects;
	}
	
	/**
     * Add the item at @param position as selected
     */
	// New item selected and update the view
	public void setNewSelection(int position){
		selection.add(position);
		notifyDataSetChanged();
	}
	
	/**
     * Deselect the item at @param position
     */
	public void removeSelection(int position){
		selection.remove(Integer.valueOf(position));
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
    
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
    	
    	//View v = super.getView(position, convertView, parent);
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //convertView = inflater.inflate(R.layout.activity_list_item, parent, false);
            convertView = inflater.inflate(R.layout.activity_list_item, null);
        }
    	
    	TextView name = (TextView) convertView.findViewById(R.id.id_item_list_name);
    	TextView time = (TextView) convertView.findViewById(R.id.id_item_list_time);
    	
    	String actN = activities.get(position).getName();
    	String actT = getTextFromLong(activities.get(position).getTimeRunning());
    	
    	name.setText(actN);
    	time.setText(actT);
    	
        convertView.setBackgroundColor(getContext().getResources().getColor(
                android.R.color.transparent)); // Default color
 
        if (selection.contains(position)) {
        	convertView.setBackgroundColor(getContext().getResources().getColor(
                    android.R.color.holo_green_light)); // color when selected
        } 
        return convertView;
    }
    public String timetoString(int time){
		if(time < 10)
			return "0"+String.valueOf(time);
		else
			return String.valueOf(time);
	}

	
	public String getTextFromLong(Long time){

		int seconds = time.intValue()/1000;
		int hour = seconds/3600;
		int minutes = (seconds/60)%60;
		seconds = seconds % 60;
		if(hour > 0)
			return timetoString(hour) + ":"
					+ timetoString(minutes) + ":" 
					+ timetoString(seconds);
		else
			return timetoString(minutes) + ":" 
					+ timetoString(seconds);
	}
    
}
