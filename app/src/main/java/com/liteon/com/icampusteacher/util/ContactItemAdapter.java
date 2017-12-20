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

public class ContactItemAdapter extends RecyclerView.Adapter<ContactItemAdapter.ViewHolder> {

    private List<ContactItem> mDataset;
    private WeakReference<ViewHolder.IHealthViewHolderClicks> mClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public View mRootView;
        public TextView mTitleTextView;
        public TextView mValueTextView;
        public WeakReference<IHealthViewHolderClicks> mClicks;
        public ContactItem mItem;
        public ViewHolder(View v, IHealthViewHolderClicks itemClickListener) {
            super(v);
            mRootView = v;
            mClicks = new WeakReference<>(itemClickListener);
        }

        @Override
        public void onClick(View v) {
            if (mClicks.get() != null ) {
                mClicks.get().onClick(mItem);
            }
        }

        public interface IHealthViewHolderClicks {
            void onClick(ContactItem item);
        }
    }

    public ContactItemAdapter(List<ContactItem> contactDataset, ViewHolder.IHealthViewHolderClicks ItemClickListener) {
        mDataset = contactDataset;
        mClickListener = new WeakReference<>(ItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactItem item = mDataset.get(position);
        holder.mTitleTextView.setText(item.getDate());
        holder.mValueTextView.setText(item.getContent());
        holder.mItem = item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_contact_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, mClickListener.get());
        vh.mTitleTextView = v.findViewById(R.id.title_date);
        vh.mValueTextView = v.findViewById(R.id.value_text);
        v.setOnClickListener(vh);
        return vh;
    }
}

