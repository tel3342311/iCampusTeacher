package com.liteon.com.icampusteacher.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liteon.com.icampusteacher.R;

import java.lang.ref.WeakReference;
import java.util.List;

public class HealthyItemAdapter extends RecyclerView.Adapter<HealthyItemAdapter.ViewHolder> {

    private List<HealthyItem> mDataset;
    private WeakReference<ViewHolder.IHealthViewHolderClicks> mClickListener;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements android.view.View.OnClickListener {
        // each data item is just a string in this case
        public View mRootView;
        public TextView mTitleTextView;
        public TextView mValueTextView;
        public ImageView mItemIcon;
        public HealthyItem.TYPE mType;
        public WeakReference<IHealthViewHolderClicks> mClicks;
        public ViewHolder(View v, IHealthViewHolderClicks itemClickListener) {
            super(v);
            mRootView = v;
            mClicks = new WeakReference<>(itemClickListener);
        }

        @Override
        public void onClick(View v) {
            if (mClicks.get() != null ) {
                mClicks.get().onClick(mType);
            }
        }

        public interface IHealthViewHolderClicks {
            void onClick(HealthyItem.TYPE type);
        }
    }

    public HealthyItemAdapter(List<HealthyItem> healthDataset, ViewHolder.IHealthViewHolderClicks ItemClickListener) {
        mDataset = healthDataset;
        mClickListener = new WeakReference<>(ItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HealthyItem item = mDataset.get(position);
        holder.mTitleTextView.setText(item.getTitle());
        holder.mValueTextView.setText(item.toString());
        holder.mItemIcon.setImageResource(item.getIconId());
        holder.mType = item.getItemType();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_healthy_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, mClickListener.get());
        vh.mTitleTextView = v.findViewById(R.id.title_text);
        vh.mValueTextView = v.findViewById(R.id.value_text);
        vh.mItemIcon = v.findViewById(R.id.healthy_icon);
        v.setOnClickListener(vh);
        return vh;
    }
}

