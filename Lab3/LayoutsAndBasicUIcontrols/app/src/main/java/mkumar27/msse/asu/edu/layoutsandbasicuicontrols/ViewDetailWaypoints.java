package mkumar27.msse.asu.edu.layoutsandbasicuicontrols;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewdetailwaypoints);
    }
    public void backtodrop(View v){
        Intent intent = new Intent(ViewDetailWaypoints.this,ViewWaypoints.class);
        startActivity(intent);
    }
    public void editway(View v){
        Intent intent = new Intent(ViewDetailWaypoints.this,EditWaypoint.class);
        startActivity(intent);
    }
}
