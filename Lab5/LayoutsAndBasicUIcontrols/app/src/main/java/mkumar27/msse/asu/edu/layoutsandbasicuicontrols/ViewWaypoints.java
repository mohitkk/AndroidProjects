package mkumar27.msse.asu.edu.layoutsandbasicuicontrols;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2015 Mohit Kumar,
 * <p/>
 * Licensed under Apache License, Version 2.0(the "License");
 * TA and Instructor are authorised to build, run and evaluate the project.
 *
 * @author Mohit Kumar, mkumar27@asu.edu
 *         Software Engineering, Arizona State University
 * @version January ${Day}, 2015
 */
public class ViewWaypoints extends Activity {
    Spinner spinner;
    Intent intent = this.getIntent();
    String cat;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waypoints_drop);
         cat = intent.getExtras().getString("SelectedCategoryName");
        spinner = (Spinner)findViewById(R.id.spinner2);
        try{
            WayPoints ac = (WayPoints) new WayPoints().execute(
                    new URL(this.getString(R.string.url_string)));

        }catch(Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),ex.getMessage());
        }

    }
    public void back(View v){
        Intent intent = new Intent(ViewWaypoints.this,MainActivity.class);

        startActivity(intent);
    }
    public void viewpoint(View v){
        Intent intent = new Intent(ViewWaypoints.this,ViewDetailWaypoints.class);
        String x = (String)spinner.getSelectedItem();
        intent.putExtra("SelectedWaypoint",x);
        intent.putExtra("SelectedCategoryName",cat);  //cat is a variable name
        startActivity(intent);
    }
    public void addnewpoint(View v){
        Intent intent = new Intent(ViewWaypoints.this,AddNewWaypoint.class);
        startActivity(intent);
    }
    private class WayPoints extends AsyncTask<URL,Integer,String[]> {
        String retStr = "";

        @Override
        protected void onPreExecute(){
            android.util.Log.d(this.getClass().getSimpleName(),"in onPreExecute on "+
                    (Looper.myLooper()== Looper.getMainLooper()?"Main Thread":"Async Thread"));
        }
        @Override
        protected String[] doInBackground(URL... aUrl){
            android.util.Log.d(this.getClass().getSimpleName(),"in doInBackground on "+
                    (Looper.myLooper()==Looper.getMainLooper()?"Main Thread":"Async Thread"));

            String [] x={""} ;
            try{
                WaypointServerStub  wp = new WaypointServerStub(aUrl[0].toString());

                x = wp.getNamesInCategory(cat);


            }
            catch(Exception ex){
                android.util.Log.d(this.getClass().getSimpleName(),"exception in remote call "+
                        ex.getMessage());
            }
            android.util.Log.d(this.getClass().getSimpleName(),"Completed jsonRPC call: result ");

            return x;
        }
        @Override
        protected void onPostExecute(String[] res){
            android.util.Log.d(this.getClass().getSimpleName(),"In inPost execute on "+
                    (Looper.myLooper() == Looper.getMainLooper()?"Main Thread":"Async Thread"));
            android.util.Log.d(this.getClass().getSimpleName()," result is: "+res);

            ArrayAdapter<String> adapter;
            List<String> list = new ArrayList<String>();

            for (String i: res)
            {
                list.add(i);
            }
            adapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }
}
