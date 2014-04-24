package com.achoi007.yourcontacts.app;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Features:
 * Specifies layout of each list item via contact_list_item.xml
 * Dynamic name filter which detects name prefix as typed and filters contacts.
 */
public class UseListViewActivity extends Activity
        implements TextWatcher, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = UseListViewActivity.class.getSimpleName();
    private SimpleCursorAdapter mContactsAdpt;
    private static final int LOADER_ID = 1;
    private static final String NAME_PREFIX = "NamePrefix";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_list_view);

        mContactsAdpt = createContactAdapter();
        ((ListView) findViewById(R.id.contactList)).setAdapter(mContactsAdpt);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void reloadContacts(CharSequence namePrefix) {
        Bundle state = new Bundle();
        state.putString(NAME_PREFIX, namePrefix.toString());
        getLoaderManager().restartLoader(LOADER_ID, state, this);
    }

    private SimpleCursorAdapter createContactAdapter() {
        String[] fromFields = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
        };

        int[] toFields = {
                R.id.idTxt,
                R.id.nameTxt,
        };

        SimpleCursorAdapter adpt = new SimpleCursorAdapter(this,
                R.layout.contact_list_item, null,
                fromFields, toFields, 0);
        return adpt;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.use_list_view, menu);
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
    protected void onResume() {
        super.onResume();
        ((EditText) findViewById(R.id.nameFilterTxt)).addTextChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((EditText) findViewById(R.id.nameFilterTxt)).removeTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        Log.d(TAG, "seq=" + charSequence + ",i=" + i + ",i2=" + i2 + ",i3=" + i3);
        reloadContacts(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle state) {

        // If there is a non-empty name prefix, use filter URI, else use
        // regular uri
        Uri uri;

        String namePrefix = (state != null) ? state.getString(NAME_PREFIX) : null;
        if (namePrefix != null) {
            uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
                    Uri.encode(namePrefix));
        } else {
            uri = ContactsContract.Contacts.CONTENT_URI;
        }
        Log.d(TAG, "uri " + uri);

        // Starts query
        String[] projection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
        };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME;

        return new CursorLoader(this,
                uri, projection, selection, selectionArgs, sortOrder);
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
}
