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
public class EditWaypoint extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editwaypoints);
    }
    public void backtoview(View v){
        Intent intent = new Intent(EditWaypoint.this,ViewDetailWaypoints.class);
        startActivity(intent);
    }
}
