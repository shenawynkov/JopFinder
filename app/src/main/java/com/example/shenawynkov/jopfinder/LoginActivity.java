package com.example.shenawynkov.jopfinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class LoginActivity extends AppCompatActivity {
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mSignInBtn;
    private Button mSignUpBtn;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
  mReference=FirebaseDatabase.getInstance().getReference();
        mEmailEditText=findViewById(R.id.email_sign_up);
        mPasswordEditText=findViewById(R.id.password);
        mSignInBtn=(Button)findViewById(R.id.sign_in_btn);
         mSignUpBtn=(Button)findViewById(R.id.sign_up);
        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(mEmailEditText.getText().toString(),mPasswordEditText.getText().toString());


            }
        });
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();


    }


    private void signIn(String email, String password) {


        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            final FirebaseUser user = mAuth.getCurrentUser();
                           mReference= mReference.child("users").child(user.getUid());
                            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user1= dataSnapshot.getValue(User.class);
                                   if(user1.type==0)
                                   {
                                       Intent  intent=new Intent(LoginActivity.this,NewJopActivity.class);
                                      startActivity(intent);
                                   }
                                    else
                                   {
                                      // Intent  intent=new Intent(LoginActivity.this,selection.class);

                                   }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });
        // [END sign_in_with_email]
    }

}
