package com.liteon.com.icampusteacher.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liteon.com.icampusteacher.R;

import java.util.List;

public class StudentItemAdapter extends RecyclerView.Adapter<StudentItemAdapter.ViewHolder> {

    private List<JSONResponse.Student> mStudentList;

    public StudentItemAdapter(List<JSONResponse.Student> list) {
        mStudentList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_student_item, parent, false);
        int height = parent.getMeasuredHeight() / 4;
        int width = parent.getMeasuredWidth() / 4;

        v.setLayoutParams(new RecyclerView.LayoutParams(width, height));
        ViewHolder vh = new ViewHolder(v);
        vh.mStudentIcon = v.findViewById(R.id.student_icon);
        vh.mStudentName = v.findViewById(R.id.student_name);
        vh.mStudentId = v.findViewById(R.id.student_id);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        JSONResponse.Student item = mStudentList.get(position);
        holder.mStudentName.setText(item.getName());
        holder.mStudentId.setText(item.getRoll_no());
    }

    @Override
    public int getItemCount() {
        return mStudentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mRootView;
        public ImageView mStudentIcon;
        public TextView mStudentName;
        public TextView mStudentId;
        public ViewHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
        }
    }
}
