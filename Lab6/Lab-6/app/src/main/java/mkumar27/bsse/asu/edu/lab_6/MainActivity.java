package mkumar27.bsse.asu.edu.lab_6;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.io.File;


public class MainActivity extends ActionBarActivity {
    public ExpandableListView elview;
    public String selectedWaypoint;
    private ExpandableWaypointAdapter myListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        elview = (ExpandableListView) findViewById(R.id.lvExp);
        WayPointDb db = new WayPointDb(this);
        try {
            File ext = Environment.getExternalStorageDirectory();
            android.util.Log.w("in MainActivity onCreate", "external storage directory is: " +
                    ext.getAbsolutePath());
            File extPub = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            android.util.Log.w("in MainActivity onCreate", "pubic download directory is: " +
                    extPub.getAbsolutePath());
            File dataDir = Environment.getDataDirectory();
            android.util.Log.w("in MainActivity onCreate", "my data directory is: " +
                    dataDir.getAbsolutePath());
            android.util.Log.w("in MainActivity onCreate", "Context data dir is: " + this.getApplicationInfo().dataDir);
            android.util.Log.w("in MainActivity onCreate", "Context files dir is: " + this.getFilesDir());
            db.copyDB();
            SQLiteDatabase crsDB = db.openDB();
            //String[] args = {"Ser421 Fdns of Web","1"};
            //Cursor upDate = crsDB.rawQuery("UPDATE course set coursename=? WHERE courseid=?;",args);
            //upDate.moveToFirst();  // you must call move to first and close to complete the update.
            //upDate.close();
            Cursor c = crsDB.rawQuery("SELECT DISTINCT category FROM waypoint", null);
            String temp = "";
            while (c.moveToNext()) {
                String categoryName = c.getString(0);
                //int courseId = c.getInt(1);
                temp = temp + "\n Course:" + categoryName;
            }
            android.util.Log.w("onCreate read db", "found courses:" + temp);
            c.close();
            crsDB.close();
            db.close();
        } catch (java.sql.SQLException sqle) {
            android.util.Log.w("Caught SQLException:", sqle.getMessage());
        } catch (Exception ex) {
            android.util.Log.w("Caught Exception:", ex.getMessage());
        }
        myListAdapter = new ExpandableWaypointAdapter(this);
        elview.setAdapter(myListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startDialog(View v) {
        android.util.Log.w("ActivityMain", "clicked start Dialog");
        Intent intent = new Intent(MainActivity.this, WayPointDetail.class);
        startActivity(intent);
    }

}