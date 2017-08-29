package com.example.shenawynkov.jopfinder;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.shenawynkov.jopfinder.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Shenawynkov on 8/28/2017.
 */




public class SelectionFragment extends Fragment {
    private FirebaseAuth mFirebaseAuth;
    private final static int RC_SIGN_IN=1;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private Button mEmpolyerButton;
    private Button mSeekerButton;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseUser mUser;
    private int mType;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       View v= inflater.inflate(R.layout.fragment_selection,container,false);
        final Intent navIntent=new Intent(getActivity(),NavigationActivity.class);
        final Intent newJopIntent=new Intent(getActivity(),NewJopActivity.class);
        mEmpolyerButton=(Button)v.findViewById(R.id.employer);
        mSeekerButton=(Button)v.findViewById(R.id.JobSeeker);
        mSeekerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                int type = User.Empolyee;
                writeNewUser(mUser.getUid(),mUser.getDisplayName(),mUser.getEmail(),mUser.getPhoneNumber(),type);
                startActivity(navIntent);
            }
        });
        mEmpolyerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(newJopIntent);
            }
        });
return  v;
    }
    private void writeNewUser(String userId, String name, String email,String phone,int type) {
        User user = new User(name, email,phone,type);

        mReference.child("users").child(userId).setValue(user);
    }
}
