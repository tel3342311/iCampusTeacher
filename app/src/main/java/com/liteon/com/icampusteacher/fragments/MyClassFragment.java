package com.liteon.com.icampusteacher.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.liteon.com.icampusteacher.MainActivity;
import com.liteon.com.icampusteacher.R;
import com.liteon.com.icampusteacher.util.RecyclerItemClickListener;
import com.liteon.com.icampusteacher.util.StudentItem;
import com.liteon.com.icampusteacher.util.StudentItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private List<StudentItem> mDataSet;

    private static final int COLUMN_COUNT = 4;
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
        mLayoutManager = new GridLayoutManager(getContext(),COLUMN_COUNT);
        mRecycleView.setLayoutManager(mLayoutManager);
        mDataSet = new ArrayList<>();
        mAdapter = new StudentItemAdapter(mDataSet);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecycleView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), "OnClick", Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).changeFragment(StudentDetailFragment.newInstance("",""));
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getContext(), "OnLongClick", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDataSet.size() == 0) {
            mDataSet.addAll(getStudents());
            mAdapter.notifyDataSetChanged();
        }
        mRecycleView.invalidate();
    }

    private List<StudentItem> getStudents() {
        List<StudentItem> list = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            StudentItem item = new StudentItem();
            item.setNickname(random());
            item.setName(random());
            item.setGender("MALE");
            list.add(item);
        }
        return list;
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(8);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
