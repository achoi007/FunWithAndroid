package com.achoi007.listfragexp.app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonDetailFragment} interface
 * to handle interaction events.
 * Use the {@link PersonDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class PersonDetailFragment
        extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String PERSON_ID = "PersonID";

    private static final String TAG = "achoi007.PersonDetailFragment";
    private long mPersonID;
    private IPersonRepository mPersonRep;
    private SimpleDateFormat mDateFmt;
    private EditText mIDView, mLastNameView, mFirstNameView, mBirthdateView, mSexView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param personID Parameter 1.
     * @return A new instance of fragment PersonDetailFragment.
     */
    public static PersonDetailFragment newInstance(long personID) {
        PersonDetailFragment fragment = new PersonDetailFragment();
        Bundle args = new Bundle();
        args.putLong(PERSON_ID, personID);
        fragment.setArguments(args);
        return fragment;
    }

    public PersonDetailFragment() {
        mDateFmt = new SimpleDateFormat("MM-dd-yyyy");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPersonID = getArguments().getLong(PERSON_ID);
        }
        else {
            mPersonID = 1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_person_detail, container, false);

        // Fills with person details
        Log.d(TAG, "Getting person with id " + mPersonID);
        Person person = mPersonRep.getPersonById(mPersonID);
        if (person == null) {
            Log.d(TAG, "no person found matching id " + mPersonID);
            return v;
        }

        mIDView = (EditText) v.findViewById(R.id.idTxt);
        mIDView.setText(person.id + "");

        mLastNameView = (EditText) v.findViewById(R.id.lastNameTxt);
        mLastNameView.setText(person.lastName);

        mFirstNameView = (EditText) v.findViewById(R.id.firstNameTxt);
        mFirstNameView.setText(person.firstName);

        mBirthdateView = (EditText) v.findViewById(R.id.birthdateTxt);
        mBirthdateView.setText(mDateFmt.format(person.birthdate));

        mSexView = (EditText) v.findViewById(R.id.sexTxt);
        mSexView.setText("" + person.sex);

        // Sets up listeners for buttons.  Can't be done in layout.xml as
        // android:onClick because that expects Activity to have defined
        // onAdd(View), onUpdate(View), and onDelete(View)
        Button addBtn = (Button) v.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Person p = genPersonFromInput();
                if (p != null) {
                    long id = mPersonRep.addPerson(p);
                    // Updates with actual id of person just added.
                    mIDView.setText("" + id);
                }
            }
        });

        Button updateBtn = (Button) v.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Person p = genPersonFromInput();
                if (p != null) {
                    mPersonRep.updatePerson(p);
                }
            }
        });

        Button delBtn = (Button) v.findViewById(R.id.deleteBtn);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Person p = genPersonFromInput();
                if (p != null) {
                    if (mPersonRep.deletePerson(p)) {
                        // Clears out all fields if successful deletion.
                        mIDView.getEditableText().clear();
                        mFirstNameView.getEditableText().clear();
                        mLastNameView.getEditableText().clear();
                        mBirthdateView.getEditableText().clear();
                        mSexView.getEditableText().clear();
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mPersonRep = ((IPersonRepositoryHolder) activity).getPersonRepository();
        }
        catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement IPersonRepositoryHolder");
        }
    }

    @Override
    public void onDetach() {
        mPersonRep = null;
        super.onDetach();
    }

    /**
     * Generates a person object using inputs from GUI.
     * @return
     */
    private Person genPersonFromInput() {
        Person person = new Person();
        try {

            if (mIDView.getText().length() > 0) {
                person.id = Long.parseLong(mIDView.getText().toString());
            }

            person.lastName = mLastNameView.getText().toString();
            person.firstName = mFirstNameView.getText().toString();
            person.birthdate = mDateFmt.parse(mBirthdateView.getText().toString());
            person.sex = mSexView.getText().charAt(0);
            return person;
        }
        catch (Exception ex) {
            Log.e(TAG, "Unable to generate person due to exception: " + ex.getMessage());
            return null;
        }
    }
}
