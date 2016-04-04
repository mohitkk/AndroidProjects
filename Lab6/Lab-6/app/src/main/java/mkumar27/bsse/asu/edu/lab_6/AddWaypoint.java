package mkumar27.bsse.asu.edu.lab_6;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Copyright 2015 Mohit Kumar,
 * <p/>
 * TA and Instructor can download and execute this for evaluation purpose.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Purpose: activity to display information about a student
 *
 * @author Mohit Kumar mkumar27@asu.edu
 *         Software Engineering, Arizona State University Polytechnic
 * @version February 22, 2015
 */
public class AddWaypoint extends Activity {
    EditText latET,lonET,nameET,addET,catET;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_waypoint);
        latET = (EditText)findViewById(R.id.latitudeED1);
        lonET = (EditText)findViewById(R.id.longitudeED1);
        nameET = (EditText)findViewById(R.id.nameED1);
        addET = (EditText)findViewById(R.id.addED1);
        catET = (EditText)findViewById(R.id.catED1);
    }
    public void addClickedNew(View v){
        try {
            android.util.Log.d(this.getClass().getSimpleName(), "add Clicked");
            Intent intent = new Intent(this, MainActivity.class);
            String name = nameET.getText().toString();
            String category = catET.getText().toString();
            String address = addET.getText().toString();
            String lon = lonET.getText().toString();
            String lat = latET.getText().toString();
            WayPointDb db = new WayPointDb((Context) this);
            db.copyDB();
            SQLiteDatabase crsDB = db.openDB();
            Cursor cr = crsDB.rawQuery("select MAX(waypointid) from waypoint;",new String[]{});
            int id=0;
            while(cr.moveToNext()) {
                 id = new Integer(cr.getString(0)) + 1;
            }
            cr.close();
            crsDB.execSQL("insert into waypoint values(\'"+name+"\',\'"+category+"\',\'"+address+"\',"+lat+","+lon+","+id+");");
            Cursor crr = crsDB.rawQuery("select * from waypoint where waypoint.category = '"+"Hike"+"';",new String[]{});
            crr.close();
            crsDB.close();
            db.close();
            startActivity(intent);
            this.finish();
        }
        catch(Exception ex)
        {
            System.out.println("Add Waypoint exception " + ex.getMessage());
        }
    }
    public void backClicked(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
