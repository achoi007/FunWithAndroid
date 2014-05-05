package com.achoi007.savingdata.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class PersonActivity extends Activity {

    public final static String PERSON_REP_CLASSNAME = "person.rep.classname";
    private final static String TAG = PersonActivity.class.getSimpleName();
    private IPersonRepository mPersonRep;
    private TextView mNameTxt, mLocTxt;
    private DatePicker mDOBDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        mNameTxt = (TextView) findViewById(R.id.editName);
        mDOBDate = (DatePicker) findViewById(R.id.dateDOB);
        mLocTxt = (TextView) findViewById(R.id.editLocation);

        Intent i = getIntent();
        try {

            // Loads repository
            Log.d(TAG, "creating person repository");
            String personRepClass = i.getStringExtra(PERSON_REP_CLASSNAME);
            Class c = Class.forName(personRepClass);
            mPersonRep = (IPersonRepository) c.newInstance();

            Log.d(TAG, "init person repository");
            mPersonRep.init(this);

            // Fills GUI
            Log.d(TAG, "filling GUI with person");
            Person p = mPersonRep.get();
            mNameTxt.setText(p.name);
            Calendar cal = Calendar.getInstance();
            cal.setTime(p.dob);
            mDOBDate.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            mLocTxt.setText(p.location);
        }
        catch (Exception ex) {
            Log.e(TAG, "class name: " + i.getStringExtra(PERSON_REP_CLASSNAME));
            throw new ClassCastException("Unable to cast to person repository");
        }
    }

    public void onSave(View btn) {
        Person p = createPersonFromGui();
        mPersonRep.save(p);
        Toast.makeText(this, "Person saved", Toast.LENGTH_SHORT).show();
    }

    private Person createPersonFromGui() {
        String name = mNameTxt.getText().toString();
        Calendar cal = Calendar.getInstance();
        cal.set(mDOBDate.getYear(), mDOBDate.getMonth(), mDOBDate.getDayOfMonth());
        Date dob = cal.getTime();
        String location = mLocTxt.getText().toString();
        return new Person(name, dob, location);
    }

    @Override
    protected void onDestroy() {
        mPersonRep.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.person, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
