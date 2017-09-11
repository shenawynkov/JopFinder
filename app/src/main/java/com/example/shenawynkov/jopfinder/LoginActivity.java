package com.example.shenawynkov.jopfinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;


public class LoginActivity extends BaseActivity implements Validator.ValidationListener {

    @NotEmpty
    @Email
    private EditText mEmailEditText;
   // @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
   @Password(min = 6)
   private EditText mPasswordEditText;
    private Button mSignInBtn;
    private Button mSignUpBtn;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private Validator mValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);


        mAuth = FirebaseAuth.getInstance();
  mReference=FirebaseDatabase.getInstance().getReference();
        mEmailEditText=findViewById(R.id.email_sign_up);
        mPasswordEditText=findViewById(R.id.password);
        mSignInBtn=(Button)findViewById(R.id.sign_in_btn);
         mSignUpBtn=(Button)findViewById(R.id.sign_up);

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mValidator.validate();




            }
        });
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);

            }
        });
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            showProgressDialog();

            updateUI(currentUser);
        }
        else{
                hideProgressDialog();

        }


    }


    private void signIn(String email, String password) {

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                             FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            hideProgressDialog();
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
        // [END sign_in_with_email]
    }
    void updateUI(FirebaseUser user)
    {
        if(user!=null) {
            mReference = mReference.child("users").child(user.getUid());
            mReference.keepSynced(true);

            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user1 = dataSnapshot.getValue(User.class);
                    if (user1 != null) {
                        if (user1.type == 0) {
                            Intent intent = new Intent(LoginActivity.this, EmployerActivity.class);
                            intent.putExtra(getString(R.string.user_extra),user1);

                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(LoginActivity.this, JopListActivity.class);
                            intent.putExtra(getString(R.string.user_extra),user1);
                            startActivity(intent);

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    hideProgressDialog();

                }
            });
        }

    }






    @Override
    public void onValidationSucceeded() {
        signIn(mEmailEditText.getText().toString(),mPasswordEditText.getText().toString());

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        hideProgressDialog();
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

}
