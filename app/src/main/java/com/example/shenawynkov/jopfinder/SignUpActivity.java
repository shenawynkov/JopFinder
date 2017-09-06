package com.example.shenawynkov.jopfinder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shenawynkov.jopfinder.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class SignUpActivity extends BaseActivity implements Validator.ValidationListener {
    private static final int READ_REQUEST_CODE = 42;
    @NotEmpty
    @Email
    private EditText mEmailEditText;
    @Password(min = 6)
    private EditText mPasswordEditText;
    @NotEmpty
    private EditText mNameEditText;
    private Button mSignUpBtn;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    @NotEmpty
    private EditText mPhoneEditText;
    private Button mAddCv;
    private int type;
      private  UploadTask mUploadTask;
      private FirebaseStorage mStorage;
     private StorageReference mStorageRef;
    private Validator mValidator;

     private Uri mCvLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);


        final Spinner spinner = (Spinner) findViewById(R.id.spinner_signup);
// Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        mAuth=FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef= mStorage.getReference();

        mReference = FirebaseDatabase.getInstance().getReference();
        mEmailEditText=(EditText)findViewById(R.id.email_sign_up);
        mPasswordEditText=(EditText)findViewById(R.id.password_sign_up);
        mNameEditText=(EditText)findViewById(R.id.name);
        mSignUpBtn=(Button)findViewById(R.id.sign_up_btn);
        mPhoneEditText=(EditText) findViewById(R.id.phone);
        mAddCv=(Button) findViewById(R.id.upload_file);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                type=i;
                if(type==1)
                {
                    mAddCv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog();
                mValidator.validate();


            }
        });
        mAddCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performFileSearch();
                showProgressDialog();
            }
        });

    }
    private void createAccount(final String name, final String email, String password, final String phone, final int type,final Uri cv) {

            // [START create_user_with_email]
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                writeNewUser(user.getUid(),name,email,phone,type,cv);
                                 updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }


                        }
                    });


    }
    private void writeNewUser(String userId, String name, String email,String phone,int type,Uri cv) {
        User user = new User(name, email,phone,type,cv);


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
                            Intent intent = new Intent(SignUpActivity.this, JopListActivity.class);
                            intent.putExtra(getString(R.string.user_extra),user1);
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
    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("application/pdf");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                uploadFile(uri);

            }
        }
    }
    void  uploadFile(Uri file)
    {

        StorageReference riversRef = mStorageRef.child("CVs/"+file.getLastPathSegment());
        mUploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        mUploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                mCvLink=downloadUrl;
            }
        });
        hideProgressDialog();
    }

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();
        if(type==1)
        {
            if(mCvLink!=null)
            {createAccount(mNameEditText.getText().toString(),
                    mEmailEditText.getText().toString(),
                    mPasswordEditText.getText().toString(),
                    mPhoneEditText.getText().toString(),type,mCvLink);

            }
            else
            {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(),"You must upload Your Cv",Toast.LENGTH_SHORT).show();

            }

        }
        else {
            createAccount(mNameEditText.getText().toString(),
                    mEmailEditText.getText().toString(),
                    mPasswordEditText.getText().toString(),
                    mPhoneEditText.getText().toString(), type,null);
        }
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
