package com.liteon.com.icampusteacher.util;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liteon.com.icampusteacher.R;

import java.lang.ref.WeakReference;
import java.util.Comparator;
import java.util.List;

public class ContactItemSearchAdapter extends RecyclerView.Adapter<ContactItemSearchAdapter.ViewHolder> {

    private final Comparator<JSONResponse.Contents> mComparator;
    private WeakReference<ViewHolder.IHealthViewHolderClicks> mClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public View mRootView;
        public TextView mTitleTextView;
        public WeakReference<IHealthViewHolderClicks> mClicks;
        public JSONResponse.Contents mItem;
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
            void onClick(JSONResponse.Contents item);
        }
    }

    public ContactItemSearchAdapter(ViewHolder.IHealthViewHolderClicks ItemClickListener, Comparator<JSONResponse.Contents> comparator) {
        mClickListener = new WeakReference<>(ItemClickListener);
        mComparator = comparator;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JSONResponse.Contents item = mSortedList.get(position);
        holder.mTitleTextView.setText(item.getComments());
        holder.mItem = item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_search_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, mClickListener.get());
        vh.mTitleTextView = v.findViewById(R.id.title_text);
        v.setOnClickListener(vh);
        return vh;
    }

    private final SortedList<JSONResponse.Contents> mSortedList = new SortedList<>(JSONResponse.Contents.class, new SortedList.Callback<JSONResponse.Contents>() {
        @Override
        public int compare(JSONResponse.Contents a, JSONResponse.Contents b) {
            return mComparator.compare(a, b);
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(JSONResponse.Contents oldItem, JSONResponse.Contents newItem) {
            return oldItem.getComments().equals(newItem.getComments());
        }

        @Override
        public boolean areItemsTheSame(JSONResponse.Contents item1, JSONResponse.Contents item2) {
            return item1.getReminder_id() == item2.getReminder_id();
        }
    });

    public void add(JSONResponse.Contents item) {
        mSortedList.add(item);
    }

    public void remove(JSONResponse.Contents item) {
        mSortedList.remove(item);
    }

    public void add(List<JSONResponse.Contents> item) {
        mSortedList.addAll(item);
    }

    public void remove(List<JSONResponse.Contents> items) {
        mSortedList.beginBatchedUpdates();
        for (JSONResponse.Contents item : items) {
            mSortedList.remove(item);
        }
        mSortedList.endBatchedUpdates();
    }

    public void replaceAll(List<JSONResponse.Contents> items) {
        mSortedList.beginBatchedUpdates();
        for (int i = mSortedList.size() - 1; i >= 0; i--) {
            final JSONResponse.Contents item = mSortedList.get(i);
            if (!items.contains(item)) {
                mSortedList.remove(item);
            }
        }
        mSortedList.addAll(items);
        mSortedList.endBatchedUpdates();
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }
}

