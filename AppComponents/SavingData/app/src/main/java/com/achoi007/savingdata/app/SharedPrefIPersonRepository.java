package com.achoi007.savingdata.app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by alang_000 on 5/4/2014.
 */
public class SharedPrefIPersonRepository implements IPersonRepository {

    private final String URL = "com.achoi007.savingdata.app.SharedPrefPersonRepository";
    private final String NAME = "name";
    private final String DOB = "dob";
    private final String LOCATION = "location";
    private SharedPreferences mPref;

    @Override
    public void init(Context cntxt) {
        if (mPref == null) {
            mPref = cntxt.getSharedPreferences(URL, cntxt.MODE_PRIVATE);
        }
    }

    @Override
    public void destroy() {
        mPref = null;
    }

    @Override
    public void save(Person person) {
        SharedPreferences.Editor edit = mPref.edit();
        edit.putString(NAME, person.name);
        edit.putLong(DOB, person.dob.getTime());
        edit.putString(LOCATION, person.location);
        edit.commit();
    }

    @Override
    public Person get() {
        Person person = new Person(mPref.getString(NAME, ""),
                mPref.getLong(DOB, 0),
                mPref.getString(LOCATION, ""));
        return person;
    }
}
