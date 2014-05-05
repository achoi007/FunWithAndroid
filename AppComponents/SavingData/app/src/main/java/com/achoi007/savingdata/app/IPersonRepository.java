package com.achoi007.savingdata.app;

import android.content.Context;

/**
 * Created by alang_000 on 5/4/2014.
 */
public interface IPersonRepository {

    void init(Context cntxt);

    void destroy();

    void save(Person person);

    Person get();
}
