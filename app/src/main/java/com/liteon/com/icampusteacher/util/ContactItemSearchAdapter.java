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

    private final Comparator<SearchItem> mComparator;
    private WeakReference<ViewHolder.IHealthViewHolderClicks> mClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public View mRootView;
        public TextView mTitleTextView;
        public WeakReference<IHealthViewHolderClicks> mClicks;
        public SearchItem mItem;
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
            void onClick(SearchItem item);
        }
    }

    public ContactItemSearchAdapter(ViewHolder.IHealthViewHolderClicks ItemClickListener, Comparator<SearchItem> comparator) {
        mClickListener = new WeakReference<>(ItemClickListener);
        mComparator = comparator;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchItem item = mSortedList.get(position);
        holder.mTitleTextView.setText(item.getContent());
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

    private final SortedList<SearchItem> mSortedList = new SortedList<>(SearchItem.class, new SortedList.Callback<SearchItem>() {
        @Override
        public int compare(SearchItem a, SearchItem b) {
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
        public boolean areContentsTheSame(SearchItem oldItem, SearchItem newItem) {
            return oldItem.getContent().equals(newItem.getContent());
        }

        @Override
        public boolean areItemsTheSame(SearchItem item1, SearchItem item2) {
            return item1.getContent() == item2.getContent();
        }
    });

    public void add(SearchItem item) {
        mSortedList.add(item);
    }

    public void remove(SearchItem item) {
        mSortedList.remove(item);
    }

    public void add(List<SearchItem> item) {
        mSortedList.addAll(item);
    }

    public void remove(List<SearchItem> items) {
        mSortedList.beginBatchedUpdates();
        for (SearchItem item : items) {
            mSortedList.remove(item);
        }
        mSortedList.endBatchedUpdates();
    }

    public void replaceAll(List<SearchItem> items) {
        mSortedList.beginBatchedUpdates();
        for (int i = mSortedList.size() - 1; i >= 0; i--) {
            final SearchItem item = mSortedList.get(i);
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

