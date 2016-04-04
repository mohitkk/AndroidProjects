package mkumar27.bsse.asu.edu.lab_6;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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
 * @version February 21, 2015
 */
public class WayPointDetail extends Activity {
    private Button addButt, removeButt, modifyButt;
    private EditText nameET, latET, lonET,addET,catET;
    private TextView infoTV;
    private String selectedWaypoint;
    TextView msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.waypoint_detail);
        addButt = (Button)findViewById(R.id.addButt);
        removeButt = (Button)findViewById(R.id.removeButt);
        modifyButt = (Button)findViewById(R.id.modifyButt);
        latET = (EditText)findViewById(R.id.latitudeED);
        lonET = (EditText)findViewById(R.id.longitudeED);
        nameET = (EditText)findViewById(R.id.nameED);
        addET = (EditText)findViewById(R.id.addED);
        catET = (EditText)findViewById(R.id.catED);
        Bundle b = getIntent().getExtras();
        selectedWaypoint = b.getString(getString(R.string.selected));
        String [] sel = selectedWaypoint.split(" ");
        selectedWaypoint = sel[2];
        loadFields();
    }
    private void loadFields(){
        try{
            WayPointDb db = new WayPointDb((Context)this);
            db.copyDB();
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select * from waypoint where waypointid=? ;",
                    new String[]{selectedWaypoint});
            String category = "unknown";
            String add = "any@any.com";
            String lat = " ";
            String lon = " ";
            String name = " ";
            while (cur.moveToNext()){
                name = cur.getString(0);
                category = cur.getString(1);
                add = cur.getString(2);
                lat = cur.getString(3);
                lon = cur.getString(4);
            }
            nameET.setText(name);
            catET.setText(category);
            addET.setText(add);
            latET.setText(lat);
            lonET.setText(lon);
            cur.close();
            crsDB.close();
            db.close();
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception getting student info: "+
                    ex.getMessage());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_waypoint_info, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void addClicked(View v){
        android.util.Log.d(this.getClass().getSimpleName(),"add Clicked");
        Intent intent = new Intent(this, AddWaypoint.class);
        startActivity(intent);
        this.finish();
    }
    public void removeClicked(View v){
        try {
            android.util.Log.d(this.getClass().getSimpleName(), "remove Clicked");
            Intent intent = new Intent(this, MainActivity.class);
            WayPointDb db = new WayPointDb((Context) this);
            db.copyDB();
            SQLiteDatabase crsDB = db.openDB();
            crsDB.execSQL("delete from waypoint where waypoint.waypointid="+selectedWaypoint+";");
            crsDB.close();
            db.close();
            startActivity(intent);
            this.finish();
        }
        catch(Exception ex)
        {
            ex.getMessage();
        }
    }
    public void modifyClicked(View v){
        try {
            String name = nameET.getText().toString();
            String cate = catET.getText().toString();
            String addd = addET.getText().toString();
            String latt = latET.getText().toString();
            String lonn = lonET.getText().toString();
            android.util.Log.d(this.getClass().getSimpleName(), "modify Clicked");
            Intent intent = new Intent(this, MainActivity.class);
            WayPointDb db = new WayPointDb((Context) this);
            db.copyDB();
            SQLiteDatabase crsDB = db.openDB();
            crsDB.execSQL("update waypoint set name=\'"+name+"\',category=\'"+cate+"\',address=\'"+addd+"\',latitude="+latt+",longitude="+lonn+" where waypointid="+selectedWaypoint+";");
            crsDB.close();
            db.close();
            startActivity(intent);
            this.finish();
        }
        catch(Exception ex)
        {
            ex.getMessage();
        }
    }
    public void exportContent(View v){
        try {
            StringBuilder sb = new StringBuilder();
            WayPointDb db = new WayPointDb((Context) this);
            db.copyDB();
            SQLiteDatabase crsDB = db.openDB();
            Cursor cr = crsDB.rawQuery("select * from waypoint;",new String[]{});
            sb.append("[");
            while (cr.moveToNext())
            {
                sb.append("{");
                sb.append("\"name\":\""+cr.getString(0)+"\",");
                sb.append("\"category\":\""+cr.getString(1)+"\",");
                sb.append("\"address\":\""+cr.getString(2)+"\",");
                sb.append("\"latitude\":"+cr.getString(3)+",");
                sb.append("\"longitude\":"+cr.getString(4)+",");
                sb.append("\"waypointid\":"+cr.getString(5)+"");
                sb.append("},");
                System.out.println(cr.getString(0));
                System.out.println(cr.getString(1));
                System.out.println(cr.getString(2));
                System.out.println(cr.getString(3));
                System.out.println(cr.getString(4));
                System.out.println(cr.getString(5));
            }
            sb = sb.deleteCharAt(sb.length()-1);
            sb.append("]");
            System.out.println(sb);
            crsDB.close();
            db.close();
            System.out.print("------------------------");
            System.out.println(isExternalStorageWritable());
            InputStream ip;
            String x = sb.toString();
            ip = new ByteArrayInputStream(x.getBytes());
            String path = "/mnt/sdcard/waypoint.json/";
            File aFile = new File(path);
            if(!aFile.exists()){
               aFile.mkdirs();
            }
            String op=  "/mnt/sdcard/waypoint.json/123.json";
            OutputStream output = new FileOutputStream(op);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = ip.read(buffer))>0)
            {
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            ip.close();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        catch(Exception ex)
        {
            ex.getMessage();
            ex.printStackTrace();
        }
    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public void backClicked(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
