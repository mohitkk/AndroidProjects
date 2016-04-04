package mkumar27.bsse.asu.edu.lab4;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by Mohit on 08-Feb-15.
 */
public class CalculatorStub {
    JsonRpcRequestViaHttp server;
    public static int id =0;

    CalculatorStub(URL url){
        server = new JsonRpcRequestViaHttp(url);
    }

    public double add (double numerator, double denominator){
        double ret = 0.0;
        try{
            JSONArray ja = new JSONArray();
            ja.put(0,numerator).put(1,denominator);
            String callStr = this.packageCalcCall("add",ja);
            String retStr = server.call(callStr);
            JSONObject res = new JSONObject(retStr);
            ret  = res.optDouble("result");

        }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception in call: ADD "+ex.getMessage());
        }
        return ret;
    }
    public double subtract (double numerator, double denominator){
        double ret = 0.0;
        try{
            JSONArray ja = new JSONArray();
            ja.put(0,numerator).put(1,denominator);
            String callStr = this.packageCalcCall("subtract",ja);
            String retStr = server.call(callStr);
            JSONObject res = new JSONObject(retStr);
            ret  = res.optDouble("result");

        }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception in call: Substract"+ex.getMessage());
        }
        return ret;
    }
    public double multiply (double numerator, double denominator){
        double ret = 0.0;
        try{
            JSONArray ja = new JSONArray();
            ja.put(0,numerator).put(1,denominator);
            String callStr = this.packageCalcCall("multiply",ja);
            String retStr = server.call(callStr);
            JSONObject res = new JSONObject(retStr);
            ret  = res.optDouble("result");

        }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception in call: Multiply "+ex.getMessage());
        }
        return ret;
    }
    public double divide (double numerator, double denominator){
    double ret = 0.0;
    try{
        JSONArray ja = new JSONArray();
        ja.put(0,numerator).put(1,denominator);
        String callStr = this.packageCalcCall("divide",ja);
        String retStr = server.call(callStr);
        JSONObject res = new JSONObject(retStr);
        ret  = res.optDouble("result");

    }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception in call: "+ex.getMessage());
        }
        return ret;
    }
    private String packageCalcCall(String oper, JSONArray args){
        String ret = "";
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("jsonrpc","2.0");
            jsonObj.put("method",oper);
            jsonObj.put("id",++id);
            String almost = jsonObj.toString();
            String toInsert = null;
            if(args==null){
                toInsert = ",\"params\":[]";
            }
            else{
                toInsert = ",\"params\":"+args.toString();
            }
            String begin = almost.substring(0,almost.length()-1);
            String end = almost.substring(almost.length()-1);
            ret  = begin+toInsert+end;
            android.util.Log.d(this.getClass().getSimpleName(),"making call: "+ ret);
        }
        catch(Exception ex){

            android.util.Log.w(this.getClass().getSimpleName(),"exception packaging call: "+ex.getMessage());

        }
        return ret;
    }
}
