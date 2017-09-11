package com.example.shenawynkov.jopfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shenawynkov.jopfinder.model.Job;
import com.example.shenawynkov.jopfinder.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.DecimalMax;
import com.mobsandgeeks.saripaar.annotation.DecimalMin;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

public class NewJopFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemSelectedListener,Validator.ValidationListener {


    @NotEmpty
    private EditText mEditTextTitle;
    @NotEmpty
    private EditText mEditTextMinSalary;
    @NotEmpty
    private EditText mEditTextMaxSalary;
    private Validator mValidator;
    @NotEmpty
    @Length(min = 50,max = 2000)
    private EditText mEditTextDesctiption;
    private Button mButton;

  private   String mcareer;
    private String mTitle;
    private int mMinSalary;

    private int mMaxSalary;

    private String mDescription;

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef ;
  private User mUser;
    private ChangePagerListner mPagerListner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_new_jop, container, false);


        mUser=(User)getActivity().getIntent().getSerializableExtra(getString(R.string.user_extra));

        mEditTextTitle=(EditText)v.findViewById(R.id.newjop_title);
        mEditTextMinSalary=(EditText)v.findViewById(R.id.newjop_min_salary);
        mEditTextMaxSalary=(EditText)v.findViewById(R.id.newjop_max_salary);
        mEditTextDesctiption=(EditText)v.findViewById(R.id.newjop_description);
        Spinner spinner = (Spinner) v.findViewById(R.id.newjop_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.career_array, R.layout.spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(this);
        mButton=(Button)v.findViewById(R.id.newjop_add_job);
        mValidator = new Validator(NewJopFragment.this);
        mValidator.setValidationListener(this);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mValidator.validate();
            }
        });


        return v;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mPagerListner = (ChangePagerListner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ChangePagerListner");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
     mcareer= (String) adapterView.getItemAtPosition(i);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mcareer= (String) adapterView.getItemAtPosition(0);
    }

    @Override
    public void onValidationSucceeded() {
        mTitle=mEditTextTitle.getText().toString();
        mMinSalary=Integer.parseInt(mEditTextMinSalary.getText().toString());
        mMaxSalary=Integer.parseInt(mEditTextMaxSalary.getText().toString());
        mDescription=mEditTextDesctiption.getText().toString();

        mDatabase= FirebaseDatabase.getInstance();


        DatabaseReference myRef = mDatabase.getReference("job");
        myRef.keepSynced(true);
        Job job=new Job(mTitle,mMinSalary,mMaxSalary,mcareer,mDescription,mUser.email);
        myRef.push().setValue(job);

//                Intent  intent=new Intent(getActivity(),EmpolyerJopListFragment.class);
//                startActivity(intent);
        mPagerListner.ChangePager();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }




    interface ChangePagerListner{
        void ChangePager();
    }
}
