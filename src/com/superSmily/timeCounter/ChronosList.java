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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ChronosList extends ListActivity {

	/*
	 *Se podr�a crear un array personalizado con un string y un numero(tiempo) 
	 *para rellenar y guardar la lista de actividades.
	 *Se puede usar el numero para ordenar la lista
	 *
	 *Guardar el tiempo en segundos y hacer transformaciones a minutos, horas.	
	*/
	ArrayList<String> listAct;
	private SelectionAdapter adapter;
	Context ctx;
    ActionMode mActionMode;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        // Estudiar hacerlo en segundo plano para no bloquear el hilo principal
        ArrayList<String> storedAct = getActivitiesName();  
        listAct = new ArrayList<String>();
        for(int i=0; i < storedAct.size(); ++i)
        	listAct.add(storedAct.get(i));
         
        //Array simple, m�s tarde usaremos uno personalizado en el que aparezca la actividad y el tiempo
        adapter = new SelectionAdapter(
        		ctx, android.R.layout.simple_list_item_1, android.R.id.text1, listAct);
        setListAdapter(adapter); 
        setupActionBar();
    }
    
    private void setupActionBar(){
    	ListView lv = getListView();
        lv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        
        lv.setMultiChoiceModeListener(new MultiChoiceModeListener(){
        	
			@Override
			public void onItemCheckedStateChanged(ActionMode mode,
					int position, long id, boolean checked) {
				if(checked)
					adapter.setNewSelection(position);
				else
					adapter.removeSelection(position);
				mode.setTitle(adapter.getCount() + "activities selected");
				
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				// TODO Auto-generated method stub
				
			}

        	
        });
    	
    	
    }
 
    @Override
    public void  onListItemClick(ListView l, View v, int pos, long id){
    	//Lanzar actividad con el cronometro de esa actividad
    	Intent i = new Intent(ctx, ChronoActivity.class);
    	//A�adir los extras con la actividad elegida
    	i.putExtra("name", listAct.get(pos));
    	startActivity(i); 	
    }
/*    
    public void onListItemLongClick(ListView l, View v, int pos, long id){
    	//Lanzar dialog para borrar
    	AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
    	builder.setTitle(R.string.list_dialog_delete_title)
    		.setMessage(R.string.list_dialog_delete_message)
    		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//Eliminar actividad
				}
			})
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
    	AlertDialog dialog = builder.create();
    	dialog.show();
    }
  */  
    
    public void addActivity(String act){
    	// Insert into database (SQLiteException)
    	ActivityDAO dao = new ActivityDAO(ctx);
    	try{
    		dao.open();
    		dao.addActivity(new Activity(act));
    	}catch(Exception e){
    		//toast error adding activity at db, it wont be stored!
    	}finally{
    		dao.close();
    	} 	
    	listAct.add(act);
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
    // getActivitiesName() and getActivities()
    
    public ArrayList<String> getActivitiesName(){
    	ArrayList<Activity> acts = new ArrayList<Activity>();
    	ArrayList<String> actsName = new ArrayList<String>();
    	ActivityDAO dao = new ActivityDAO(ctx);
    	try{
    		dao.open();
    		acts = dao.getActivities();
    		for(int i=0; i < acts.size(); ++i)
    			actsName.add(acts.get(i).getName());
    	}catch(Exception e){
    		//Toast error reading getting your activities
    		
    	}finally{
    		dao.close();
    	}
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
    

 //Men� de la activity
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
    	case R.id.menu_settings:
    		//lanzar settings
    		break;
    	default:
    		break;
    	}   	
    	return true;
    }
}
