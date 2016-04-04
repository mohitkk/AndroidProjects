package mkumar27.bsse.asu.edu.lab7;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener  {
    EditText txt1,txt2,txt3,txt4;
    Spinner sp;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            txt1 = (EditText) findViewById(R.id.edtFN);
            txt2 = (EditText) findViewById(R.id.edtLN);
            txt3 = (EditText) findViewById(R.id.edtPH);
            txt4 = (EditText) findViewById(R.id.edtEID);
            sp = (Spinner) findViewById(R.id.spinner1);
            String[] displayNames = getContactsNames();
            System.out.println(displayNames);
            adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new ArrayList(Arrays.asList(displayNames)));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp.setAdapter(adapter);
            sp.setOnItemSelectedListener(this);
        }
        catch (Exception ex)
        {
            ex.getMessage();
            ex.printStackTrace();
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
    public void addClicked(View v){
        try {
            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, txt3.getText());
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, txt4.getText());
            intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
            intent.putExtra(ContactsContract.Intents.Insert.NAME, txt1.getText() + " " + txt2.getText());
            startActivity(intent);
        }
        catch (Exception ex)
        {
            ex.getMessage();
            ex.printStackTrace();
        }
    }
    private String[] getContactsNames(){
        ArrayList<String> ret = new ArrayList<String>();
        try{

        String wherename = ContactsContract.Data.MIMETYPE + "=?";
        String [] whereNameParams = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
        Cursor namecur = getContentResolver().query(ContactsContract.Data.CONTENT_URI,null,wherename,whereNameParams,ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
        while (namecur.moveToNext())
        {
            ret.add((String) namecur.getString(namecur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME)));
            System.out.println(namecur.getString(namecur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME)));
        }
        namecur.close();
        }
        catch (Exception ex)
        {
            ex.getMessage();
            ex.printStackTrace();
        }
        return (String[])ret.toArray(new String[]{});

    }

    private String getPhone(String aName){
        String ret = "noPhone";
        try {
            ContentResolver cr = getContentResolver();
            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, "DISPLAY_NAME = '" + aName + "'", null, null);
            if (cursor.moveToFirst()) {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
                while (phones.moveToNext()) {
                    String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    ret = number;
                    break;
                }
                phones.close();
            }
            cursor.close();
        }
        catch (Exception ex)
        {
            ex.getMessage();
            ex.printStackTrace();
        }
        return ret;
    }
    private String getMail(String aName){
        String ret = "noMail";
        try {
            ContentResolver cr = getContentResolver();
            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, "DISPLAY_NAME = '" + aName + "'", null, null);
            if (cursor.moveToFirst()) {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor mails = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactId, null, null);
                while (mails.moveToNext()) {
                    String mail = mails.getString(mails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    ret = mail;
                    break;
                }
                mails.close();
            }
            cursor.close();
        }
        catch (Exception ex)
        {
            ex.getMessage();
            ex.printStackTrace();
        }
        return ret;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            Object item = parent.getItemAtPosition(position);
            String phone = getPhone(item.toString());
            String mail  = getMail(item.toString());
            txt3.setText(phone);
            txt4.setText(mail);
            Scanner s = new Scanner(item.toString());
            txt1.setText((s.hasNext() ? s.next() : "aFirst"));
            txt2.setText((s.hasNext() ? s.next() : "aLast"));
        }
        catch (Exception ex)
        {
            ex.getMessage();
            ex.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        System.out.println("Nothing is selected.");
    }

}
