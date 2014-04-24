package com.achoi007.yourcontacts.app;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;


public class UseListActivity extends ListActivity {

    private final String TAG = UseListActivity.class.getSimpleName();
    private SimpleCursorAdapter mContactsAdpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_use_list);

        // Sets up contact adapter
        String[] fromFields = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
        };

        int[] toFields = {
                android.R.id.text1, android.R.id.text2,
        };

        mContactsAdpt = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null,
                fromFields, toFields, 0);
        setListAdapter(mContactsAdpt);

        // Loads contacts
        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                String[] projection = {
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                };

                String selection = null;
                String[] selectionArgs = null;
                String sortOrder = ContactsContract.Contacts.DISPLAY_NAME;

                Log.d(TAG, "loading from " + ContactsContract.Contacts.CONTENT_URI);

                return new CursorLoader(UseListActivity.this, ContactsContract.Contacts.CONTENT_URI,
                        projection, selection, selectionArgs, sortOrder);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
                Log.d(TAG, "onLoadFinished");
                mContactsAdpt.swapCursor(cursor);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {
                Log.d(TAG, "onLoaderReset");
                mContactsAdpt.swapCursor(null);
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.use_list, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
