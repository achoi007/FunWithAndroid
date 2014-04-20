package com.achoi007.listfragexp.app;

import java.util.Date;

/**
 * Created by achoi on 4/19/2014.
 */
public class Person {
    public long id;
    public String lastName, firstName;
    public Date birthdate;
    public char sex;    // 'M' or 'F'

    @Override
    public String toString() {
        return id + " " + firstName + " " + lastName;
    }
}
