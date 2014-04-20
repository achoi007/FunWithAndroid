package com.achoi007.listfragexp.app;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by alang_000 on 4/19/2014.
 */
public class FakePersonRepository extends MemoryPersonRepository {

    public FakePersonRepository(int numPeople) {

        String[] firstNames = new String[]{
                "Adam", "Bob", "Charlie", "Dave", "Ed", "Fred", "George", "Henry",
                "Issac", "John", "Kerry", "Louis", "Mark", "Nick", "Paul", "Rich", "Steve",
                "Tom", "Victor", "Lily", "Cindy", "Erika"
        };

        String[] lastNames = new String[]{
                "Johnson", "Jackson", "Stevens", "Hall", "Bold", "Tallon", "Gallons",
                "Henricks", "Mart", "Nelson", "Wilson", "Choi", "Yau"
        };

        Random rnd = new Random();
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < numPeople; i++) {
            Person p = new Person();
            p.firstName = firstNames[rnd.nextInt(firstNames.length)];
            p.lastName = lastNames[rnd.nextInt(lastNames.length)];
            p.sex = rnd.nextBoolean() ? 'M' : 'F';
            cal.set(rnd.nextInt(50) + 1980, Calendar.JANUARY, rnd.nextInt(28));
            p.birthdate = cal.getTime();
            addPerson(p);
        }
    }

}
