package mkumar27.bsse.asu.edu.lab4;

import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    Spinner spinner;
    EditText edittxt1,edittxt2,edittxt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        spinner = (Spinner)findViewById(R.id.spinner1);
        this.edittxt1 = (EditText)findViewById(R.id.edittxt1);
        this.edittxt2 = (EditText)findViewById(R.id.edittxt2);
        this.edittxt3 = (EditText)findViewById(R.id.edittxt3);

        ArrayAdapter<String> adapter;
        List<String> list = new ArrayList<String>();

        list.add("Add");
        list.add("Subtract");
        list.add("Multiply");
        list.add("Divide");

        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
    public void callMethod(View v){
        android.util.Log.w(this.getClass().getSimpleName(),"Button clicked: "+spinner.getSelectedItem());
        try{
            AsyncCalculate ac = (AsyncCalculate) new AsyncCalculate().execute(
                    new URL(this.getString(R.string.url_string)));

        }catch(Exception ex){
                android.util.Log.d(this.getClass().getSimpleName(),"Exception Buttonclick: "+ex.getMessage());
            }
        }


    private class AsyncCalculate extends AsyncTask <URL,Integer,Double> {
    String retStr = "";
    @Override
    protected void onPreExecute(){
        android.util.Log.d(this.getClass().getSimpleName(),"in onPreExecute on "+
                (Looper.myLooper()== Looper.getMainLooper()?"Main Thread":"Async Thread"));
    }
    @Override
    protected Double doInBackground(URL... aUrl){
        android.util.Log.d(this.getClass().getSimpleName(),"in doInBackground on "+
                (Looper.myLooper()==Looper.getMainLooper()?"Main Thread":"Async Thread"));
        double val = 0;
        try{
            CalculatorStub  calc = new CalculatorStub(aUrl[0]);
            String oper = (String)spinner.getSelectedItem();
            double left  =  new Double(edittxt1.getText().toString());
            double right  =  new Double(edittxt2.getText().toString());
            if ("Add".equals(oper)){
                val = calc.add(left,right);
            }else if ("Subtract".equals(oper)){
                val = calc.subtract(left, right);
            }else if ("Multiply".equals(oper)){
                val = calc.multiply(left, right);
            }else if ("Divide".equals(oper)){
                val = calc.divide(left, right);
            }

        }
        catch(Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"exception in remote call "+
                    ex.getMessage());
        }
        android.util.Log.d(this.getClass().getSimpleName(),"Completed jsonRPC call: result "+(new Double(val).toString()));

        return val;
    }
    @Override
    protected void onPostExecute(Double res){
        android.util.Log.d(this.getClass().getSimpleName(),"In inPost execute on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main Thread":"Async Thread"));
        android.util.Log.d(this.getClass().getSimpleName()," result is: "+res.toString());
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        edittxt3.setText(nf.format(res));
    }
}
}
