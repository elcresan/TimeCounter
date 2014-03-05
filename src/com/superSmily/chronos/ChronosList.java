package com.superSmily.chronos;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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
	ArrayAdapter<String> adapter;
	Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ctx = this;
        //Lista por defecto, más tarde usaremos datos de la app (SharedPreferences, DB, etc)
        String[] activities = {"Estudiar", "En clase", "Jugar", "Dormir"};
        listAct = new ArrayList<String>();
        
        for(int i=0; i < activities.length; ++i)
        	listAct.add(activities[i]);
        
        //Array simple, más tarde usaremos uno personalizado en el que aparezca la actividad y el tiempo
        adapter = new ArrayAdapter<String>(
        		ctx, android.R.layout.simple_list_item_1, listAct);
        setListAdapter(adapter);       
    }
    
    @Override
    public void  onListItemClick(ListView l, View v, int pos, long id){
    	//Lanzar actividad con el cronometro de esa actividad
    	Intent i = new Intent(ctx, ChronoActivity.class);
    	//Añadir los extras con la actividad elegida
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
    	//Añadir a los datos de la app (sharedPreferences, DB, etc.)
    	listAct.add(act);
    	adapter.notifyDataSetChanged();
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
    	case R.id.menu_settings:
    		//lanzar settings
    		addActivity("Settings");
    		break;
    	default:
    		break;
    	}   	
    	return true;
    }
}
