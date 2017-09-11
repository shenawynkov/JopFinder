package com.example.shenawynkov.jopfinder;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.example.shenawynkov.jopfinder.model.Job;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shenawynkov on 9/11/2017.
 */

public class MyWidgetRemoteViewsFactory  implements RemoteViewsService.RemoteViewsFactory {
    FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChildEventListener;
    private List<Job> mJobs=new ArrayList<>();
    private Context mContext;
    public MyWidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
        mDatabase= FirebaseDatabase.getInstance();
        mReference=mDatabase.getReference("job");
        mReference.keepSynced(true);
        mChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Job job=  dataSnapshot.getValue(Job.class);
                mJobs.add(job);
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

        mReference.orderByValue().limitToLast(3).addChildEventListener(mChildEventListener);
    }

    @Override
    public void onDataSetChanged() {



    }

    @Override
    public void onDestroy() {
        mJobs.clear();

    }

    @Override
    public int getCount() {
        return mJobs.size();
    }


    @Override
    public RemoteViews getViewAt(int i) {

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        views.setTextViewText(R.id.widget_item, mJobs.get(i).getTitle());

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
