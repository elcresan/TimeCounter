package com.superSmily.timeCounter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;



public class ChronosList extends ListActivity {

	/*
	 *Se podría crear un array personalizado con un string y un numero(tiempo) 
	 *para rellenar y guardar la lista de actividades.
	 *Se puede usar el numero para ordenar la lista
	 *
	 *Guardar el tiempo en segundos y hacer transformaciones a minutos, horas.	
	*/
	
	ArrayList<String> listAct;
	private SelectionAdapter adapter;
	ArrayList<Activity> activities;
	Context ctx;
    ActionMode mActionMode;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        listAct = new ArrayList<String>();
        activities = new ArrayList<Activity>();
        activities = getActivities();
        for(int i=0; i < activities.size(); ++i){
         	listAct.add(activities.get(i).getName());
         }                 
        adapter = new SelectionAdapter(
        		ctx, android.R.layout.simple_list_item_1, android.R.id.text1, activities);
        setListAdapter(adapter);
        setupActionBar();

        // Estudiar hacerlo en segundo plano para no bloquear el hilo principal
        
    }
    
    @Override
    public void onResume(){
    	/*Necessary to refresh the list (counting or not) when user press back button instead
    	* of up button
    	*/
    	super.onResume();
    	adapter.clear();
    	activities = getActivities();
    	adapter.addAll(activities);
        adapter.notifyDataSetChanged();
    }
    
    private void setupActionBar(){
    	ListView lv = getListView();
        lv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);      
        lv.setMultiChoiceModeListener(new MultiChoiceModeListener(){
        	
			@Override
			public void onItemCheckedStateChanged(ActionMode mode,
					int position, long id, boolean checked) {
				// Set the item to selected or not at the adapter
				if(checked)
					adapter.setNewSelection(position);
				else
					adapter.removeSelection(position);
				mode.setTitle(adapter.getSelectionCount() + " activities selected");
				
			}
			
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				//Add the options of actionbar
				switch(item.getItemId()){
				
				case R.id.idborrar:
					// Remove the items
					removeActivities();
					Toast.makeText(ctx, adapter.getSelectionCount() + " activities removed",
							Toast.LENGTH_SHORT).show();
					//Toast.makeText(ctx, a, Toast.LENGTH_SHORT).show();
					adapter.clearSelection();
					
					mode.finish();
					return true;
				default:
					return false;
				}
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				
				// The actionBar is initialized
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.context_menu, menu);
				return true;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {			
				adapter.clearSelection();				
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}
        	
        });
    	
    	
    }

    private void removeActivities(){
    	ArrayList<Integer> selected = adapter.getSelected();
    	ArrayList<String> actNames = new ArrayList<String>();
    	ArrayList<Activity> acts = new ArrayList<Activity>();
    	
    	int size = selected.size();
    	for(int index = 0; index < size; ++index){
    		actNames.add(listAct.get(selected.get(index)));
    		acts.add(activities.get(selected.get(index)));
    	}
    	
    	for(int i = 0; i < size; ++i){
    		listAct.remove(actNames.get(i));
    		activities.remove(acts.get(i));
    	}
    	
		adapter.notifyDataSetChanged();    	
    	ActivityDAO dao = new ActivityDAO(ctx);
    	try{
    		dao.open();
    		dao.removeActivities(actNames);
    	}catch(Exception e){
    		//toast error removing activities from DB
    	}finally{
    		dao.close();
    	}
    }
    
    @Override
    public void  onListItemClick(ListView l, View v, int pos, long id){
    	//Lanzar actividad con el cronometro de esa actividad
    	Intent i = new Intent(ctx, ChronoActivity.class);
    	//Añadir los extras con la actividad elegida
    	i.putExtra("name", listAct.get(pos));
    	startActivity(i); 	
    }
    
    public void addActivity(String actName){
    	// Insert into database (SQLiteException)
    	if(listAct.contains(actName)){
			Toast.makeText(ctx,"That activity already exists. Use another name.",
					Toast.LENGTH_SHORT).show();
			return;
    	}
    		
    	ActivityDAO dao = new ActivityDAO(ctx);
    	Activity act = new Activity(actName);
    	try{
    		dao.open();
    		dao.addActivity(act);
    	}catch(Exception e){
    		//toast error adding activity at db, it wont be stored!
    	}finally{
    		dao.close();
    	} 	
    	listAct.add(actName);
    	activities.add(act);
    	adapter.notifyDataSetChanged();	
    }
    
    public void removeActivity(String actName){
    	ActivityDAO dao = new ActivityDAO(ctx);
    	try{
    		dao.open();
    		dao.removeActivity(actName);
    	}catch(Exception e){
    		Log.e("ChronosList", e.toString());
    	}finally{
    		dao.close();
    	}
    }
    
    public ArrayList<Activity> getActivities(){
    	ArrayList<Activity> acts = new ArrayList<Activity>();
    	ActivityDAO dao = new ActivityDAO(ctx);
    	try{
    		dao.open();
    		acts = dao.getActivities();
    	}catch(Exception e){
    		//Toast error reading getting your activities   		
    	}finally{
    		dao.close();
    	}
    	return acts;
    }
    
    public ArrayList<String> getActivitiesName(){
    	ArrayList<Activity> acts = getActivities();
    	ArrayList<String> actsName = new ArrayList<String>();
   		for(int i=0; i < acts.size(); ++i)
    		actsName.add(acts.get(i).getName());
    	return actsName;
    }
    
    public void dialogGetActivity(){
    	LayoutInflater li = LayoutInflater.from(ctx);
    	View layout = li.inflate(R.layout.dialogaddactivity,null);
    	final EditText et = (EditText) layout.findViewById(R.id.etDialogAdd);   	
    	AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
    	builder.setView(layout);
    	
    	builder.setTitle(R.string.list_dialog_add_title)
    	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
    		@Override
			public void onClick(DialogInterface dialog, int which) {
    			if(et.getText().length() > 0)
					addActivity(et.getText().toString());
				
				else{
					Toast toast = Toast.makeText(
							ctx, R.string.list_toast_name_empty, Toast.LENGTH_SHORT);	
					toast.show();
				}
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
    	AlertDialog alertD = builder.create();
    	alertD.show();
    	
    }
	
    

 //Menú de la activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chronos_list, menu);
        return true;
    }  
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	
    	case R.id.menu_add_activity:
    		//lanzar dialog		
    		dialogGetActivity();
    		break;
    	default:
    		break;
    	}   	
    	return true;
    }
}
