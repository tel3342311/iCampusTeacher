package com.liteon.com.icampusteacher.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.liteon.com.icampusteacher.MainActivity;
import com.liteon.com.icampusteacher.R;
import com.liteon.com.icampusteacher.db.DBHelper;
import com.liteon.com.icampusteacher.util.Def;
import com.liteon.com.icampusteacher.util.JSONResponse;
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
    private android.support.v7.widget.Toolbar mToolbar;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<JSONResponse.Student> mDataSet;
    private DBHelper mDbHelper;
    private static final int COLUMN_COUNT = 4;
    private DrawerLayout mDrawer;
    private TextView mTitleView;
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

        mDbHelper = DBHelper.getInstance(getActivity());
        //get child list
        mDataSet = mDbHelper.queryChildList(mDbHelper.getReadableDatabase());
        mTitleView.setText(getString(R.string.class_name));
        initRecycleView();
        setListener();
        return rootView;
    }

    private void findViews(View rootView) {

        mToolbar = rootView.findViewById(R.id.toolbar);
        mRecycleView = rootView.findViewById(R.id.student_grid);
        mToolbar = getActivity().findViewById(R.id.toolbar);
        mTitleView = getActivity().findViewById(R.id.toolbar_title);
        mDrawer = getActivity().findViewById(R.id.drawer_layout);
    }

    private void setListener() {
        mToolbar.setNavigationIcon(R.drawable.ic_dehaze_white_24dp);
        mToolbar.setNavigationOnClickListener(v -> mDrawer.openDrawer(Gravity.LEFT));
    }

    private void initRecycleView() {
        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(),COLUMN_COUNT);
        mRecycleView.setLayoutManager(mLayoutManager);
        mAdapter = new StudentItemAdapter(mDataSet);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecycleView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                JSONResponse.Student student = mDataSet.get(position);
                ((MainActivity)getActivity()).changeFragment(StudentDetailFragment.newInstance(student.getName(), student.getStudent_id()));
                SharedPreferences sp = getActivity().getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Def.SP_STUDENT_NAME, student.getName());
                editor.putString(Def.SP_STUDENT_ID, student.getStudent_id());
                editor.commit();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
