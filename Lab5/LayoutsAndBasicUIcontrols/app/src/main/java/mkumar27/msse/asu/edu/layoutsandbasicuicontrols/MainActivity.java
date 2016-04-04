package mkumar27.msse.asu.edu.layoutsandbasicuicontrols;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (Spinner)findViewById(R.id.spinner1);
        try{
            WayPointCategory ac = (WayPointCategory) new WayPointCategory().execute(
                    new URL(this.getString(R.string.url_string)));

        }catch(Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"Exception Buttonclick: "+ex.getMessage());
        }

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
    public void waypoints(View v){
        try {
            String x = (String) spinner.getSelectedItem();
            Intent intent = new Intent(MainActivity.this, ViewWaypoints.class);
            intent.putExtra("SelectedCategoryName", x);
            startActivity(intent);
        }
        catch(Exception ex)
        {
            android.util.Log.d(this.getClass().getSimpleName(),"exception on button click "+
                    ex.getMessage());
        }
    }


    private class WayPointCategory extends AsyncTask<URL,Integer,String[]> {
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
            String val = "";
            String [] x={""} ;
            try{
                WaypointServerStub  wp = new WaypointServerStub(aUrl[0].toString());

                x =(String[]) wp.getCategoryNames();

            }
            catch(Exception ex){
                android.util.Log.d(this.getClass().getSimpleName(),"exception in remote call "+
                        ex.getMessage());
            }
            android.util.Log.d(this.getClass().getSimpleName(),"Completed jsonRPC call: result ");
            return x;
        }
        @Override
        protected void onPostExecute(String[] res) {
            try {
                android.util.Log.d(this.getClass().getSimpleName(), "In inPost execute on " +
                        (Looper.myLooper() == Looper.getMainLooper() ? "Main Thread" : "Async Thread"));
                android.util.Log.d(this.getClass().getSimpleName(), " result is: " + res);

                ArrayAdapter<String> adapter;
                List<String> list = new ArrayList<String>();

                for (String i : res) {
                    list.add(i);
                }
                adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
            catch(Exception e)
            {
                android.util.Log.d(this.getClass().getSimpleName(),"exception in on-postexecute "+
                        e.getMessage());
            }
        }
    }


}
