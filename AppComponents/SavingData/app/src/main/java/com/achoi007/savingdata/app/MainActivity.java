package com.achoi007.savingdata.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends Activity {

    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView saverList = (ListView) findViewById(R.id.listSaver);
        final String[] savers = getSaverClassNames();
        saverList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                savers));
        saverList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                try {
                    Intent i = new Intent(MainActivity.this, PersonActivity.class);
                    i.putExtra(PersonActivity.PERSON_REP_CLASSNAME, savers[pos]);
                    Log.d(TAG, "Starting person activity with classname " + savers[pos]);
                    startActivity(i);
                }
                catch (Exception ex) {

                }
            }
        });
    }

    private String[] getSaverClassNames() {
        String names[] = {
                SharedPrefIPersonRepository.class.getName(),
                FilePersonRepository.class.getName(),
                SqlPersonRepository.class.getName(),
        };

        return names;
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

}
