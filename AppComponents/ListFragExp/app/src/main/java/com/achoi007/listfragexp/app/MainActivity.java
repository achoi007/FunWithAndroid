package com.achoi007.listfragexp.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity
        extends Activity
        implements PersonListFragment.OnFragmentInteractionListener, IPersonRepositoryHolder {

    private IPersonRepository mPersonRep;

    public MainActivity() {
        mPersonRep = new FakePersonRepository(32);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    @Override
    public void onFragmentInteraction(Person p) {
        PersonDetailFragment detail = PersonDetailFragment.newInstance(p.id);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.detailFrag, detail)
                .commit();
    }

    @Override
    public IPersonRepository getPersonRepository() {
        return mPersonRep;
    }
}
