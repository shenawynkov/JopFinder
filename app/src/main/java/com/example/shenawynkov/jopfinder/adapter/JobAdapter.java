package com.example.shenawynkov.jopfinder.adapter;

import android.os.AsyncTask;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shenawynkov.jopfinder.R;
import com.example.shenawynkov.jopfinder.mail.GMailSender;
import com.example.shenawynkov.jopfinder.model.Job;
import com.example.shenawynkov.jopfinder.model.User;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Shenawynkov on 8/24/2017.
 */

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    private List<Job> mJobList=new ArrayList();
    private User mUser;

    public JobAdapter()
    {
        mUser=null;
    }
   public JobAdapter(User user)
    {
        mUser=user;
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitle;
        public TextView mMinSalary;
        public TextView mMaxSalary;
        public TextView mCareer;
        public TextView mDescription;
        public Button mApply;
        public ViewHolder(View v) {
            super(v);
            mCareer=(TextView)v.findViewById(R.id.job_carear_level);
            mDescription=(TextView)v.findViewById(R.id.job_description);
            mMaxSalary=(TextView)v.findViewById(R.id.job_max_salary);
            mMinSalary=(TextView)v.findViewById(R.id.job_min_salary);
            mTitle=(TextView)v.findViewById(R.id.job_title);
               mApply=(Button)v.findViewById(R.id.apply);
        }
    }



    // Create new views (invoked by the layout manager)
    @Override
    public JobAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
           holder.mTitle.setText(mJobList.get(position).getTitle());

        holder.mMaxSalary.setText(mJobList.get(position).getSalary_max()+"");
        holder.mMinSalary.setText(mJobList.get(position).getSalary_min()+"");
        holder.mCareer.setText(mJobList.get(position).getCareer_level());
        holder.mDescription.setText(mJobList.get(position).getDescription());
        if(mUser!=null){
        holder.mApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("username", mUser.cv);
             new  MailAsyncTask().execute(new  String[]{holder.mTitle.getText().toString(),
                     "My name is : " +mUser.username+" \n"+
                      "And here my CV : \n"+       mUser.cv,
                     mUser.email,
                     mJobList.get(position).getEmployeer_mail()
             });

            }
        });}
        else
        {
            holder.mApply.setVisibility(View.GONE);
        }

            }





    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mJobList.size();
    }
    public void addJob(Job job)
    {
        mJobList.add(job);
        notifyDataSetChanged();
    }
    public class MailAsyncTask extends AsyncTask<String, Void, String>{




        @Override
        protected String doInBackground(String... strings) {
            try {
                GMailSender sender = new GMailSender("jopfinder4u@gmail.com", "jobpublished");
                sender.sendMail(strings[0],
                        strings[1],
                        strings[2],
                        strings[3]);
            } catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);
            }
            return null;
        }
    }
    public  List<Job>  getList()
    {
        return mJobList;
    }
}


