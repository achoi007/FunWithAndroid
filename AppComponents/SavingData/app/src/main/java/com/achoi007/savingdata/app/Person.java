package com.achoi007.savingdata.app;

import java.util.Date;

/**
 * Created by alang_000 on 5/4/2014.
 */
public class Person {
    String name;
    Date dob;
    String location;

    public Person() {
        name = location = "";
        dob = new Date();
    }

    public Person(String name, Date dob, String location) {
        this.name = name;
        this.dob = dob;
        this.location = location;
    }

    public Person(String name, long dob, String location) {
        this.name = name;
        this.dob = new Date(dob);
        this.location = location;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", dob=" + dob +
                ", location='" + location + '\'' +
                '}';
    }
}
