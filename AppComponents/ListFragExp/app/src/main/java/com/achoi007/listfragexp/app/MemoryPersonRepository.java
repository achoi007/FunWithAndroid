package com.achoi007.listfragexp.app;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by achoi on 4/19/2014.
 */
public class MemoryPersonRepository implements IPersonRepository {

    private Map<Long, Person> mPersons;
    private long nextPersonId = 1;
    private List<IPersonRepositoryChangeListener> mListeners = new LinkedList<IPersonRepositoryChangeListener>();

    public MemoryPersonRepository() {
        mPersons = new HashMap<Long, Person>();
    }

    @Override
    public Collection<Person> getPersons() {
        return mPersons.values();
    }

    @Override
    public Person getPersonById(long id) {
        return mPersons.get(id);
    }

    @Override
    public Collection<Person> findPersonsByName(String firstName, String lastName) {
        List<Person> persons = new LinkedList<Person>();
        for (Person person : getPersons()) {

            // Skips if first name is specified and doesn't match prefix
            if (firstName != null && !person.firstName.startsWith(firstName)) {
                continue;
            }

            // Skips if last name is specified and doesn't match prefix
            if (lastName != null && !person.lastName.startsWith(lastName)) {
                continue;
            }

            // Finds match
            persons.add(person);
        }

        return persons;
    }

    @Override
    public long addPerson(Person person) {
        person.id = nextPersonId++;
        mPersons.put(person.id, person);
        for (IPersonRepositoryChangeListener listener : mListeners) {
            listener.onAdd(person);
        }
        return person.id;
    }

    @Override
    public boolean updatePerson(Person person) {
        Person oldPerson = mPersons.get(person.id);
        if (oldPerson == null) {
            return false;
        }

        oldPerson.lastName = person.lastName;
        oldPerson.firstName = person.firstName;
        oldPerson.birthdate = person.birthdate;
        oldPerson.sex = person.sex;

        for (IPersonRepositoryChangeListener listener : mListeners) {
            listener.onUpdate(oldPerson);
        }
        return true;
    }

    @Override
    public boolean deletePerson(Person person) {
        Person p = mPersons.remove(person.id);
        if (p != null) {
            for (IPersonRepositoryChangeListener listener : mListeners) {
                listener.onDelete(p);
            }
        }
        return p != null;
    }

    @Override
    public void addChangeListener(IPersonRepositoryChangeListener listener) {
        mListeners.add(listener);
    }

    @Override
    public void removeChangeListener(IPersonRepositoryChangeListener listener) {
        mListeners.remove(listener);
    }
}
