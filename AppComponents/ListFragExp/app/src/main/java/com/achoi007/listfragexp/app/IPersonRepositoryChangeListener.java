package com.achoi007.listfragexp.app;

/**
 * Created by achoi on 4/20/2014.
 */
public interface IPersonRepositoryChangeListener {
    void onAdd(Person p);
    void onUpdate(Person p);
    void onDelete(Person p);
}
