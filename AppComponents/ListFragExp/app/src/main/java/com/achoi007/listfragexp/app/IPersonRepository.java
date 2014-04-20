package com.achoi007.listfragexp.app;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by achoi on 4/19/2014.
 */
public interface IPersonRepository {

    /**
     * Gets all persons
     * @return all persons
     */
    Collection<Person> getPersons();

    /**
     * Gets a person by unique id.
     * @param id
     * @return person found or null
     */
    Person getPersonById(long id);

    /**
     * Finds all persons matching optional first and optional last name
     * @param firstName
     * @param lastName
     * @return
     */
    Collection<Person> findPersonsByName(String firstName, String lastName);

    /**
     * Adds a new person to repository
     * @param person
     * @throws RuntimeException on unexpected error
     * @return new person id
     */
    long addPerson(Person person);

    /**
     * Updates person in repository.
     * @param person
     * @throws RuntimeException on unexpected error
     * @return true if successful or false if no person found.
     */
    boolean updatePerson(Person person);

    /**
     * Deletes person from repository
     * @param person
     * @throws RuntimeException on unexpected error
     * @return true if successful or false if no person found.
     */
    boolean deletePerson(Person person);

    /**
     * Registers change listener
     * @param listener
     */
    void addChangeListener(IPersonRepositoryChangeListener listener);

    /**
     * Unregisters change listener
     * @param listener
     */
    void removeChangeListener(IPersonRepositoryChangeListener listener);
}
