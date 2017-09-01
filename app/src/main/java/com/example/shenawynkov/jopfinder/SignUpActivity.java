package com.example.shenawynkov.jopfinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shenawynkov.jopfinder.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mNameEditText;
    private Button mSignUpBtn;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private EditText mPhoneEditText;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_signup);
// Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        mAuth=FirebaseAuth.getInstance();

        mReference = FirebaseDatabase.getInstance().getReference();
        mEmailEditText=(EditText)findViewById(R.id.email_sign_up);
        mPasswordEditText=(EditText)findViewById(R.id.password_sign_up);
        mNameEditText=(EditText)findViewById(R.id.name);
        mSignUpBtn=(Button)findViewById(R.id.sign_up_btn);
        mPhoneEditText=(EditText) findViewById(R.id.phone);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                type=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createAccount(mNameEditText.getText().toString(),
                        mEmailEditText.getText().toString(),
                        mPasswordEditText.getText().toString(),
                        mPhoneEditText.getText().toString(),type);
            }
        });

    }
    private void createAccount(final String name, final String email, String password, final String phone, final int type) {
        if (!(name.isEmpty()||email.isEmpty() || password.isEmpty() || phone.isEmpty())) {

            // [START create_user_with_email]
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                writeNewUser(user.getUid(),name,email,phone,type);
                                 updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        }
        else
        {
            Toast.makeText(SignUpActivity.this, "enter correct data",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void writeNewUser(String userId, String name, String email,String phone,int type) {
        User user = new User(name, email,phone,type);

        mReference.child("users").child(userId).setValue(user);
    }
    void updateUI(FirebaseUser user)
    {
        if(user!=null) {
            mReference = mReference.child("users").child(user.getUid());
            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user1 = dataSnapshot.getValue(User.class);
                    if (user1 != null) {
                        if (user1.type == 0) {
                            Intent intent = new Intent(SignUpActivity.this, NewJopActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(SignUpActivity.this, NavigationActivity.class);
                            startActivity(intent);

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

}
