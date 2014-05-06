package com.achoi007.implicitintent;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {

    private static final String TAG = "com.achoi007.implicitintent.Main";
    private AutoCompleteTextView mAction, mCategory;
    private TextView mComponent, mData, mType;
    private Map<String, String> mActionMap, mCategoryMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAction = (AutoCompleteTextView) findViewById(R.id.actionTxt);
        mCategory = (AutoCompleteTextView) findViewById(R.id.categoryTxt);
        mComponent = (TextView) findViewById(R.id.componentTxt);
        mData = (TextView) findViewById(R.id.dataTxt);
        mType = (TextView) findViewById(R.id.typeTxt);

        // Sets list of available actions
        mActionMap = new HashMap<String, String>();
        String[] actions = getListOfNames("ACTION", mActionMap);
        setAdapterForView(mAction, actions);

        // Sets list of available categories
        mCategoryMap = new HashMap<String, String>();
        String[] categories = getListOfNames("CATEGORY", mCategoryMap);
        setAdapterForView(mCategory, categories);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    /**
     * Sets adapter for auto complete text view to given choices.
     *
     * @param textView
     * @param choices
     */
    private void setAdapterForView(AutoCompleteTextView textView, String[] choices) {
        ArrayAdapter<String> adpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, choices);
        textView.setAdapter(adpt);
    }

    /**
     * Gets an array of strings which are member variables of Intent matching given prefix_.  Also
     * populates name value map so that key = ACTION_|CATEGORY_|*, value is android.intent.*.*
     *
     * @param prefix
     * @return
     */
    private String[] getListOfNames(String prefix, Map<String, String> nameValueMap) {

        // Get list of names via reflection, matching all fields with given prefix
        Class intentClass = Intent.class;
        Field[] fields = intentClass.getFields();
        List<String> names = new LinkedList<String>();
        prefix = prefix + "_";
        Log.d(TAG, prefix);
        for (Field field : fields) {
            try {
                String name = field.getName();
                if (name.startsWith(prefix)) {

                    // From name (ACTION_* or CATEGORY_*) to actual android.intent.action.*
                    String value = field.get(null).toString();
                    Log.d(TAG, name + "=>" + value);
                    names.add(name);
                    nameValueMap.put(name, value);
                }
            } catch (Exception ex) {
                Log.e(TAG, "Ignoring " + field.getName() + " due to exception " + ex);
            }
        }

        // Returns a sorted array of names
        String[] nameArray = names.toArray(new String[names.size()]);
        Arrays.sort(nameArray);
        return nameArray;
    }

    public void onResolve(View btn) {

        // Creates implicit intent from user input
        Intent impIntent = new Intent();

        if (mComponent.getText().length() > 0) {
            impIntent.setClassName(this, mComponent.getText().toString());
        }

        if (mAction.getText().length() > 0) {
            String name = mAction.getText().toString();
            String value = mActionMap.get(name);
            if (value != null) {
                impIntent.setAction(value);
            }
        }

        if (mCategory.getText().length() > 0) {
            String name = mCategory.getText().toString();
            String value = mCategoryMap.get(name);
            if (value != null) {
                impIntent.addCategory(value);
            }
        }

        if (mData.getText().length() > 0) {
            try {
                impIntent.setData(Uri.parse(mData.getText().toString()));
            } catch (Exception ex) {
                Log.e(TAG, "Unable to parse data uri " + mData.getText());
                Toast.makeText(this, "Unable to parse data uri", Toast.LENGTH_LONG).show();
            }
        }

        if (mType.getText().length() > 0) {
            impIntent.setType(mType.getText().toString());
        }

        Log.d(TAG, impIntent.toString());

        // Resolves implicit intent
        ComponentName compName = impIntent.resolveActivity(getPackageManager());

        if (compName != null) {
            Intent chooser = Intent.createChooser(impIntent, "Please choose");
            startActivity(chooser);
        } else {
            Toast.makeText(this, "Resolution failed", Toast.LENGTH_LONG).show();
        }
    }
}
