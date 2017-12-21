package com.liteon.com.icampusteacher.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.liteon.com.icampusteacher.MainActivity;
import com.liteon.com.icampusteacher.R;
import com.liteon.com.icampusteacher.db.DBHelper;
import com.liteon.com.icampusteacher.util.Def;
import com.liteon.com.icampusteacher.util.GuardianApiClient;
import com.liteon.com.icampusteacher.util.JSONResponse;
import com.liteon.com.icampusteacher.util.RecyclerItemClickListener;
import com.liteon.com.icampusteacher.util.StudentItem;
import com.liteon.com.icampusteacher.util.StudentItemAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

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
    private View mSyncView;
    private Button mSyncBtn;
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
        mSyncView = rootView.findViewById(R.id.sync_view);
        mSyncBtn = mSyncView.findViewById(R.id.button_sync);
    }

    private void setListener() {
        mToolbar.setNavigationIcon(R.drawable.ic_dehaze_white_24dp);
        mToolbar.setNavigationOnClickListener(v -> mDrawer.openDrawer(Gravity.LEFT));
        mSyncBtn.setOnClickListener( v -> startSync());
    }

    public void startSync() {
        new UpdateStudentListTask().execute();
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
                SharedPreferences sp = getActivity().getSharedPreferences(Def.SHARE_PREFERENCE, MODE_PRIVATE);
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

    class UpdateStudentListTask extends AsyncTask<Void, Void, Boolean> {

        private String mErrorMsg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            final TextView title = mSyncView.findViewById(R.id.title);
            title.setText(R.string.alarm_syncing);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            SharedPreferences sp = getActivity().getSharedPreferences(Def.SHARE_PREFERENCE, MODE_PRIVATE);
            String token = sp.getString(Def.SP_LOGIN_TOKEN, "");
            GuardianApiClient apiClient = GuardianApiClient.getInstance(getActivity());
            apiClient.setToken(token);
            //get Child list
            JSONResponse response_childList = apiClient.getChildrenList();
            if (response_childList == null) {
                return false;
            }
            if (TextUtils.equals(response_childList.getReturn().getResponseSummary().getStatusCode(), Def.RET_SUCCESS_1 )) {
                List<JSONResponse.Student> studentList = Arrays.asList(response_childList.getReturn().getResults().getStudents());
                if (studentList.size() > 0) {
                    //clear child list in db
                    mDbHelper.clearChildList(mDbHelper.getWritableDatabase());
                    //Save child list to db
                    mDbHelper.insertChildList(mDbHelper.getWritableDatabase(), studentList);
                    List<JSONResponse.Student> list = mDbHelper.queryChildList(mDbHelper.getReadableDatabase());
                    mDataSet.clear();
                    mDataSet.addAll(list);
                }
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            final TextView title = mSyncView.findViewById(R.id.title);
            final Handler handler = new Handler();
            final Runnable hideSyncView = () -> mSyncView.setVisibility(View.GONE);
            Runnable runnable = () -> {
                if (aBoolean.booleanValue() == false) {
                    title.setText(R.string.healthy_sync_failed);
                } else {
                    title.setText(R.string.alarm_sync_complete);
                    mAdapter.notifyDataSetChanged();
                }
                handler.postDelayed(hideSyncView, 3000);
            };
            handler.postDelayed(runnable, 2000);
        }
    }
}
