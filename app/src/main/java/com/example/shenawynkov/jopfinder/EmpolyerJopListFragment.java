package com.example.shenawynkov.jopfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shenawynkov.jopfinder.adapter.JobAdapter;
import com.example.shenawynkov.jopfinder.model.Job;
import com.example.shenawynkov.jopfinder.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class EmpolyerJopListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private JobAdapter mJobAdapter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mAuth;
    private User mUser;
    private List<Job> mJobList=new ArrayList();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.employer_jop_list, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.jobs_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mJobAdapter = new JobAdapter();
        mRecyclerView.setAdapter(mJobAdapter);
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("job");
        mReference.keepSynced(true);

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Job job = dataSnapshot.getValue(Job.class);
               Toast.makeText(getContext(),job.getEmployeer_mail()+" "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Toast.LENGTH_SHORT).show();
                if(job.getEmployeer_mail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) )
                {
                    mJobAdapter.addJob(job);

                    mJobList=mJobAdapter.getList();
                    addPref();

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mReference.addChildEventListener(mChildEventListener);

        return v;
    }


    void  addPref()
    {
        Gson gson = new Gson();
        String string = gson.toJson(mJobList);
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                "pref" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("key", string);


        editor.commit();
    }




}
