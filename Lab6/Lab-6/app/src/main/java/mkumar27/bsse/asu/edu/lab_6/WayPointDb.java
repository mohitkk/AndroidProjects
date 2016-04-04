package mkumar27.bsse.asu.edu.lab_6;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
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
public class WayPointDb extends SQLiteOpenHelper {
    private static final boolean debugon = false;
    private static final int DATABASE_VERSION = 3;
    private static String dbName = "waypointdb";
    private String dbPath;
    private SQLiteDatabase crsDB;
    private final Context context;
    public WayPointDb(Context context){
        super(context,dbName, null, DATABASE_VERSION);
        this.context = context;
        dbPath = context.getFilesDir().getPath()+"/";
        android.util.Log.d(this.getClass().getSimpleName(),"dbpath: "+dbPath);
    }
    public void createDB() throws IOException {
        this.getReadableDatabase();
        try {
            copyDB();
        } catch (IOException e) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "createDB Error copying database " + e.getMessage());
        }
    }

    private boolean checkDB(){    //does the database exist and is it initialized?
        SQLiteDatabase checkDB = null;
        boolean ret = false;
        try{
            String path = dbPath + dbName + ".db";
            debug("WaypointDB --> checkDB: path to db is", path);
            File aFile = new File(path);
            if(aFile.exists()){
                checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
                if (checkDB!=null) {
                    debug("WaypointDB --> checkDB","opened db at: "+checkDB.getPath());
                    Cursor tabChk = checkDB.rawQuery("SELECT name FROM sqlite_master where type='table' and name='waypoint';", null);
                    boolean crsTabExists = false;
                    if(tabChk == null){
                        debug("WayPointDB--> WayPointDB","check for Waypoint table result set is null");
                    }else{
                        tabChk.moveToNext();
                        debug("WaypointDB --> checkDB","check for Waypoint table result set is: " +
                                ((tabChk.isAfterLast() ? "empty" : (String) tabChk.getString(0))));
                        crsTabExists = !tabChk.isAfterLast();
                    }
                    if(crsTabExists){
                        Cursor c= checkDB.rawQuery("SELECT * FROM waypoint", null);
                        c.moveToFirst();
                        while(! c.isAfterLast()) {
                            String crsName = c.getString(0);
                            String crsid = c.getString(1);
                            debug("WaypointDB --> WaypointDB","Waypoint table has Waypoints: "+
                                    crsName+"\twaypointid: "+crsid);
                            c.moveToNext();
                        }
                        ret = true;
                    }
                }
            }
        }catch(SQLiteException e){
            android.util.Log.w("WaypointDB->checkDB",e.getMessage());
        }
        if(checkDB != null){
            checkDB.close();
        }
        return ret;
    }
    public void copyDB() throws IOException{
        try {

            if(!checkDB()){
                // only copy the database if it doesn't already exist in my database directory
                debug("WaypointDb --> copyDB", "checkDB returned false, starting copy");
                InputStream ip =  context.getResources().openRawResource(R.raw.waypointdb);
                // make sure the database path exists. if not, create it.
                File aFile = new File(dbPath);
                if(!aFile.exists()){
                    aFile.mkdirs();
                }
                String op=  dbPath  +  dbName +".db";

                OutputStream output = new FileOutputStream(op);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = ip.read(buffer))>0){
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                ip.close();
            }
            System.out.println("Pathhhhh "+dbPath+dbName+".db");
        } catch (IOException e) {
            android.util.Log.w("WaypointDB --> copyDB", "IOException: "+e.getMessage());
        }
    }
    public SQLiteDatabase openDB() throws SQLException {
        String myPath = dbPath + dbName + ".db";
        crsDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        debug("WaypointDB --> openDB", "opened db at path: " + crsDB.getPath());
        return crsDB;
    }
    @Override
    public synchronized void close() {
        if(crsDB != null)
            crsDB.close();
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    private void debug(String hdr, String msg){
        if(debugon){
            android.util.Log.d(hdr,msg);
        }
    }
}
