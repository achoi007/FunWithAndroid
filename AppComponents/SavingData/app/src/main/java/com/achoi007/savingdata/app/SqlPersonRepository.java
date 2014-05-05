package com.achoi007.savingdata.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;

/**
 * Created by alang_000 on 5/4/2014.
 */
public class SqlPersonRepository implements IPersonRepository {

    /**
     * data contract
     */
    public static final class PersonContract {
        public static final String TABLE_NAME = "person";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DOB = "dob";
        public static final String COLUMN_NAME_LOCATION = "location";
    };

    /**
     * Person db helper
     */
    public static final class PersonDBHelper extends SQLiteOpenHelper {

        public static final int DB_VERSION = 1;
        public static final String DB_NAME = "Person.db";
        private static final String TAG = PersonDBHelper.class.getSimpleName();

        public PersonDBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            StringBuffer sb = new StringBuffer();
            sb.append(" CREATE TABLE ");
            sb.append(PersonContract.TABLE_NAME);
            sb.append(" ( ");
            sb.append(PersonContract.COLUMN_NAME_ID);
            sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
            sb.append(PersonContract.COLUMN_NAME_NAME);
            sb.append(" TEXT,");
            sb.append(PersonContract.COLUMN_NAME_DOB);
            sb.append(" TEXT,");     // YYYY-MM-DD HH:MM:SS.SSS
            sb.append(PersonContract.COLUMN_NAME_LOCATION);
            sb.append(" TEXT");
            sb.append(" ) ");

            String sql = sb.toString();
            Log.d(TAG, "onCreate: " + sql);
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
            Log.d(TAG, "onUpgrade: drop table");
            db.execSQL("DROP TABLE IF EXISTS " + PersonContract.TABLE_NAME);
            onCreate(db);
        }
    }

    private PersonDBHelper mDBHelper;
    private SimpleDateFormat mDateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String TAG = SqlPersonRepository.class.getSimpleName();
    private Context mCntxt;

    @Override
    public void init(Context cntxt) {
        mCntxt = cntxt;
        mDBHelper = new PersonDBHelper(cntxt);
    }

    @Override
    public void destroy() {
        mDBHelper.close();
        mDBHelper = null;
        mCntxt = null;
    }

    @Override
    public void save(Person person) {

        // Gets handle to database
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        // Creates record to insert into database
        ContentValues values = new ContentValues();
        values.put(PersonContract.COLUMN_NAME_ID, (Integer) null);
        values.put(PersonContract.COLUMN_NAME_NAME, person.name);
        values.put(PersonContract.COLUMN_NAME_DOB, mDateFmt.format(person.dob));
        values.put(PersonContract.COLUMN_NAME_LOCATION, person.location);
        Log.d(TAG, "insert values: " + values);

        long rowId = db.insert(PersonContract.TABLE_NAME, null, values);
        Log.d(TAG, "finished inserting value with rowid = " + rowId);
        if (rowId == -1) {
            Toast.makeText(mCntxt, "error inserting row", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Person get() {

        // Gets handle to db
        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        // selects from database
        String[] projection = {
                PersonContract.COLUMN_NAME_NAME,
                PersonContract.COLUMN_NAME_DOB,
                PersonContract.COLUMN_NAME_LOCATION
        };
        Cursor c;
        c = db.query(PersonContract.TABLE_NAME, projection, null, null, null, null, null);

        // Returns dummy row if no row
        if (!c.moveToLast()) {
            Log.d(TAG, "get: returns empty person");
            return new Person();
        }

        // Returns last row
        try {
            Person p = new Person();
            p.name = c.getString(c.getColumnIndexOrThrow(PersonContract.COLUMN_NAME_NAME));
            p.location = c.getString(c.getColumnIndexOrThrow(PersonContract.COLUMN_NAME_LOCATION));
            String dateStr = c.getString(c.getColumnIndexOrThrow(PersonContract.COLUMN_NAME_DOB));
            p.dob = mDateFmt.parse(dateStr);
            return p;
        } catch (Exception ex) {
            Log.e(TAG, "get: " + ex.getMessage());
            throw new RuntimeException("get exception", ex);
        }
    }
}
