package com.example.shenawynkov.jopfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.shenawynkov.jopfinder.model.Job;
import com.example.shenawynkov.jopfinder.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewJopActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
private EditText mEditTextTitle;
    private EditText mEditTextMinSalary;
    private EditText mEditTextMaxSalary;

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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_jop);

        mUser=(User)getIntent().getSerializableExtra(getString(R.string.user_extra));

        mEditTextTitle=(EditText)findViewById(R.id.newjop_title);
        mEditTextMinSalary=(EditText)findViewById(R.id.newjop_min_salary);
        mEditTextMaxSalary=(EditText)findViewById(R.id.newjop_max_salary);
        mEditTextDesctiption=(EditText)findViewById(R.id.newjop_description);
        Spinner spinner = (Spinner) findViewById(R.id.newjop_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.career_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(this);
  mButton=(Button)findViewById(R.id.newjop_add_job);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTitle=mEditTextTitle.getText().toString();
                mMinSalary=Integer.parseInt(mEditTextMinSalary.getText().toString());
                mMaxSalary=Integer.parseInt(mEditTextMaxSalary.getText().toString());
                mDescription=mEditTextDesctiption.getText().toString();

                mDatabase= FirebaseDatabase.getInstance();

                DatabaseReference myRef = mDatabase.getReference("job");
                Job job=new Job(mTitle,mMinSalary,mMaxSalary,mcareer,mDescription,mUser.email);
                myRef.push().setValue(job);
                Intent  intent=new Intent(NewJopActivity.this,NavigationActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
     mcareer= (String) adapterView.getItemAtPosition(i);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mcareer= (String) adapterView.getItemAtPosition(0);
    }
}
