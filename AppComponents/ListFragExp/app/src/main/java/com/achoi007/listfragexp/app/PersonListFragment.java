package com.achoi007.listfragexp.app;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * interface.
 */
public class PersonListFragment
        extends Fragment
        implements AbsListView.OnItemClickListener, IPersonRepositoryChangeListener {

    private OnFragmentInteractionListener mListener;

    private static final String TAG = "achoi007.PersonFragment";

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ArrayAdapter<Person> mAdapter;

    private IPersonRepository mPersonRep;

    public static PersonListFragment newInstance(String personRepositoryClassName) {
        PersonListFragment fragment = new PersonListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PersonListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        if (getArguments() != null) {
        }

        // Gets list of persons.  Need to use LinkedList so that calling clear or addAll will
        // work.
        List<Person> persons = new LinkedList<Person>(mPersonRep.getPersons());
        mAdapter = new ArrayAdapter<Person>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1,
                persons);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            mPersonRep = ((IPersonRepositoryHolder) activity).getPersonRepository();
            mPersonRep.addChangeListener(this);
        }
        catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnFragmentInteractionListener and IPersonRepositoryHolder");
        }
    }

    @Override
    public void onDetach() {
        mPersonRep.removeChangeListener(this);
        mPersonRep = null;
        mListener = null;
        super.onDetach();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(mAdapter.getItem(position));
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public void onAdd(Person p) {
        updateDataSet();
    }

    @Override
    public void onUpdate(Person p) {
        updateDataSet();
    }

    @Override
    public void onDelete(Person p) {
        updateDataSet();
    }

    private void updateDataSet() {
        Log.d(TAG, "updating data set");
        mAdapter.clear();
        mAdapter.addAll(mPersonRep.getPersons());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Person p);
    }

}
