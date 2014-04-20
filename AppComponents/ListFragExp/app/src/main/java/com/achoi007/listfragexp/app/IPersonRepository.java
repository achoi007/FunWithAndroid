package com.achoi007.listfragexp.app;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by alang_000 on 4/19/2014.
 */
public interface IPersonRepository {

    Collection<Person> getPersons();

    Person getPersonById(long id);

    Collection<Person> findPersonsByName(String firstName, String lastName);

    long addPerson(Person person);

    boolean updatePerson(Person person);

    boolean deletePerson(Person person);

    void addChangeListener(IPersonRepositoryChangeListener listener);

    void removeChangeListener(IPersonRepositoryChangeListener listener);
}
