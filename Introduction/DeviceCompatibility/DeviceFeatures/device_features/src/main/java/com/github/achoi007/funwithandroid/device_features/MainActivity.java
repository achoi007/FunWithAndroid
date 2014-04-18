package com.github.achoi007.funwithandroid.device_features;

import android.app.ListActivity;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends ListActivity {

    private static final String TAG = "device_features";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(getDeviceFeatures());
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

    private ListAdapter getDeviceFeatures() {
        ArrayAdapter<String> featureAryAdpt =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getFeatures());
        return featureAryAdpt;
    }

    private String[] getFeatures() {

        // Gets the list of features.  Note that some names may be null
        FeatureInfo[] featureInfos = getPackageManager().getSystemAvailableFeatures();
        List<String> features = new ArrayList<String>(featureInfos.length);
        for (FeatureInfo featureInfo : featureInfos) {
            if (featureInfo.name != null) {
                features.add(featureInfo.name);
            }
        }

        // Sorts features by name and returns them.
        String[] featureArray = features.toArray(new String[features.size()]);
        Arrays.sort(featureArray);
        return featureArray;
    }

}
