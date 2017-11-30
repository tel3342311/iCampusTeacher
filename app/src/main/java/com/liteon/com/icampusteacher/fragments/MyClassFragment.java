package com.liteon.com.icampusteacher.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.liteon.com.icampusteacher.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyClassFragment extends Fragment {
    private final static String TAG = MyClassFragment.class.getSimpleName();
    private Toolbar mToolbar;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public MyClassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyClassFragment.
     */
    public static MyClassFragment newInstance() {
        MyClassFragment fragment = new MyClassFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_class, container, false);
        findViews(rootView);
        initRecycleView();
        return rootView;
    }

    private void findViews(View rootView) {

        mToolbar = rootView.findViewById(R.id.toolbar);
        mRecycleView = rootView.findViewById(R.id.student_grid);
    }

    private void initRecycleView() {

    }
}
