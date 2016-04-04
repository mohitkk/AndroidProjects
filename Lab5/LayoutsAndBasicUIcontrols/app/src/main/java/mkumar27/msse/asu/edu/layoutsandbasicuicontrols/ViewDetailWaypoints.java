package mkumar27.msse.asu.edu.layoutsandbasicuicontrols;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
public class ViewDetailWaypoints extends Activity {
    EditText et1 = (EditText)findViewById(R.id.editText2);
    EditText et2 = (EditText)findViewById(R.id.editText3);
    EditText et3 = (EditText)findViewById(R.id.editText4);
    EditText et4 = (EditText)findViewById(R.id.editText5);
    Intent intent = this.getIntent();
    int xx;
    String cat;
    String waypoint;
    Waypoint wpc;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewdetailwaypoints);
        cat = intent.getExtras().getString("SelectedCategoryName");
        waypoint = intent.getExtras().getString("SelectedWaypoint");
        xx=0;
        try{
            DetailWayPoint ac = (DetailWayPoint) new DetailWayPoint().execute(
                    new URL(this.getString(R.string.url_string)));

        }catch(Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),ex.getMessage());
        }
    }
    public void backtodrop(View v){
        Intent intent = new Intent(ViewDetailWaypoints.this,ViewWaypoints.class);
        startActivity(intent);
    }
    public void editway(View v){
        Intent intent = new Intent(ViewDetailWaypoints.this,EditWaypoint.class);
        xx=1;
        startActivity(intent);
    }
    private class DetailWayPoint extends AsyncTask<URL,Integer,Waypoint> {
        String retStr = "";

        @Override
        protected void onPreExecute(){
            android.util.Log.d(this.getClass().getSimpleName(),"in onPreExecute on "+
                    (Looper.myLooper()== Looper.getMainLooper()?"Main Thread":"Async Thread"));
        }
        @Override
        protected Waypoint doInBackground(URL... aUrl){
            android.util.Log.d(this.getClass().getSimpleName(),"in doInBackground on "+
                    (Looper.myLooper()==Looper.getMainLooper()?"Main Thread":"Async Thread"));
            if(xx==0) {
                Waypoint wpp = null;
                try {
                    WaypointServerStub wp = new WaypointServerStub(aUrl[0].toString());

                    wpp = wp.get(waypoint);


                } catch (Exception ex) {
                    android.util.Log.d(this.getClass().getSimpleName(), "exception in remote call " +
                            ex.getMessage());
                }
                android.util.Log.d(this.getClass().getSimpleName(), "Completed jsonRPC call: result ");

                return wpp;
            }
            else{
                boolean bol=false;
                Waypoint w=null;
                try {

                    WaypointServerStub wp = new WaypointServerStub(aUrl[0].toString());

                    bol = wp.remove(waypoint);


                } catch (Exception ex) {
                    android.util.Log.d(this.getClass().getSimpleName(), "exception in remote call " +
                            ex.getMessage());
                }
                android.util.Log.d(this.getClass().getSimpleName(), "Completed jsonRPC call: result ");
                if (bol = true)
                {
                    w.name="deleted";
                    return w;
                }
                else
                {
                    w.name="notdeleted";
                    return w;
                }
            }
        }
        @Override
        protected void onPostExecute(Waypoint res){
            if (res.name=="deleted" || res.name=="notdeleted")
            {
                android.util.Log.d(this.getClass().getSimpleName(), "In inPost execute on " +
                        (Looper.myLooper() == Looper.getMainLooper() ? "Main Thread" : "Async Thread"));
                android.util.Log.d(this.getClass().getSimpleName(), " result is: " + res.name);
            }
            else
            {
                android.util.Log.d(this.getClass().getSimpleName(), "In inPost execute on " +
                        (Looper.myLooper() == Looper.getMainLooper() ? "Main Thread" : "Async Thread"));
                android.util.Log.d(this.getClass().getSimpleName(), " result is: " + res);
                et1.setText((String) res.name);
                et2.setText((String) res.address);
                et3.setText(Double.toString(res.lat));
                et4.setText(Double.toString(res.lon));
            }


        }
    }
}
