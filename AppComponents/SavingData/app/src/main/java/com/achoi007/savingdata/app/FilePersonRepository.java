package com.achoi007.savingdata.app;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;

/**
 * Created by alang_000 on 5/4/2014.
 */
public class FilePersonRepository implements IPersonRepository {

    private final static String TAG = FilePersonRepository.class.getSimpleName();
    private final String NAME = "name";
    private final String DOB = "dob";
    private final String LOCATION = "location";
    private File mFile;
    private Context mCntxt;

    @Override
    public void init(Context cntxt) {
        mCntxt = cntxt;
        if (mFile == null) {
            try {
                mFile = new File(cntxt.getFilesDir(), "person.txt");
                Log.d(TAG, "created file object: " + mFile.getPath());

                if (!mFile.exists()) {
                    Log.d(TAG, "creating new file");
                    mFile.createNewFile();
                }
            } catch (Exception ex) {
                Log.e(TAG, "init: " + ex.getMessage());
                throw new RuntimeException("init", ex);
            }
        }
    }

    @Override
    public void destroy() {
        mFile = null;
        mCntxt = null;
    }

    @Override
    public void save(Person person) {
        try {

            Log.d(TAG, "creating json writer");
            FileOutputStream fos = mCntxt.openFileOutput(mFile.getName(), Context.MODE_PRIVATE);
            JsonWriter jw = new JsonWriter(new OutputStreamWriter(fos, "UTF-8"));

            Log.d(TAG, "writing json object");
            jw.beginObject();
            jw.name(NAME).value(person.name);
            jw.name(DOB).value(person.dob.getTime());
            jw.name(LOCATION).value(person.location);
            jw.endObject();
            jw.close();
        } catch (Exception ex) {
            Log.e(TAG, "save: " + ex.getMessage());
            throw new RuntimeException("Error saving json object", ex);
        }
    }

    @Override
    public Person get() {

        try {

            if (mFile.length() == 0) {
                Log.d(TAG, "returning empty person");
                return new Person();
            }

            Log.d(TAG, "creating json reader");
            FileInputStream fis = mCntxt.openFileInput(mFile.getName());
            JsonReader jr = new JsonReader(new InputStreamReader(fis, "UTF-8"));

            Log.d(TAG, "reading json object");
            Person p = new Person();
            jr.beginObject();
            while (jr.hasNext()) {
                String name = jr.nextName();
                if (name.equals(NAME)) {
                    p.name = jr.nextString();
                } else if (name.equals(DOB)) {
                    long tick = jr.nextLong();
                    p.dob = new Date(tick);
                } else if (name.equals(LOCATION)) {
                    p.location = jr.nextString();
                } else {
                    Log.e(TAG, "Ignoring unknown field name: " + name);
                }
            }
            jr.endObject();
            jr.close();
            return p;
        } catch (Exception ex) {
            Log.e(TAG, "get: " + ex.getMessage());
            return new Person();
            //throw new RuntimeException("Error reading json object", ex);
        }
    }
}
