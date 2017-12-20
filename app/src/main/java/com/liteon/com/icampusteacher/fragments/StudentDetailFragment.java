package com.liteon.com.icampusteacher.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.MapView;
import com.liteon.com.icampusteacher.MainActivity;
import com.liteon.com.icampusteacher.R;
import com.liteon.com.icampusteacher.db.DBHelper;
import com.liteon.com.icampusteacher.util.HealthyItem;
import com.liteon.com.icampusteacher.util.HealthyItemAdapter;
import com.liteon.com.icampusteacher.util.JSONResponse;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentDetailFragment extends Fragment {

    private CardView mEnterToggle;
    private CardView mHealthyToggle;
    private CardView mPositionToggle;

    private View EnterSubView;
    private View HealthySubView;
    private View PositionSubView;

    private ImageView mEnterArrow;
    private ImageView mHealthyArrow;
    private ImageView mPositionArrow;
    private MapView mMapView;
    private TextView mStudentIdView;
    private RecyclerView mHealthyList;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private HealthyItemAdapter.ViewHolder.IHealthViewHolderClicks mOnItemClickListener;
    private static ArrayList<HealthyItem> myDataset = new ArrayList<>();
    private JSONResponse.Student mStudent;
    private DBHelper mDbHelper;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_STUDENT_NAME = "ARG_STUDENT_NAME";
    private static final String ARG_STUDENT_ID = "ARG_STUDENT_ID";

    private String mStudentName;
    private String mStudentId;
    private android.support.v7.widget.Toolbar mToolbar;
    private TextView mTitleView;
    public StudentDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentDetailFragment newInstance(String param1, String param2) {
        StudentDetailFragment fragment = new StudentDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STUDENT_NAME, param1);
        args.putString(ARG_STUDENT_ID, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStudentName = getArguments().getString(ARG_STUDENT_NAME);
            mStudentId = getArguments().getString(ARG_STUDENT_ID);

            if (!TextUtils.isEmpty(mStudentId)) {
                mDbHelper = DBHelper.getInstance(getActivity());
                mStudent = mDbHelper.getChildByStudentID(mDbHelper.getReadableDatabase(), mStudentId);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_student_detail, container, false);
        findViews(rootView);
        setupListener();
        initRecyclerView();
        return rootView;
    }

    private void findViews(View rootView) {
        mToolbar = getActivity().findViewById(R.id.toolbar);
        mTitleView = getActivity().findViewById(R.id.toolbar_title);
        mEnterToggle = rootView.findViewById(R.id.student_enter);
        mHealthyToggle = rootView.findViewById(R.id.student_healthy);
        mPositionToggle = rootView.findViewById(R.id.student_position);

        EnterSubView = mEnterToggle.findViewById(R.id.enter_item).findViewById(R.id.absent_report);
        HealthySubView = mHealthyList = mHealthyToggle.findViewById(R.id.healthy_item).findViewById(R.id.healthy_event_view);
        PositionSubView = mPositionToggle.findViewById(R.id.position_item).findViewById(R.id.map_frame);

        mMapView = mPositionToggle.findViewById(R.id.position_item).findViewById(R.id.map_view);

        mEnterArrow = mEnterToggle.findViewById(R.id.enter_item).findViewById(R.id.imageView2);
        mHealthyArrow = mHealthyToggle.findViewById(R.id.healthy_item).findViewById(R.id.imageView2);
        mPositionArrow = mPositionToggle.findViewById(R.id.position_item).findViewById(R.id.imageView2);

        mStudentIdView = mEnterToggle.findViewById(R.id.student_id);
    }

    private void setupListener() {
        mToolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        mToolbar.setNavigationOnClickListener(v -> {
            ((MainActivity)getActivity()).changeFragment(MyClassFragment.newInstance());
        });

        mEnterToggle.setOnClickListener(v -> {
            if (EnterSubView.getVisibility() == View.VISIBLE) {
                EnterSubView.setVisibility(View.GONE);
            } else {
                EnterSubView.setVisibility(View.VISIBLE);
            }

        });

        mHealthyToggle.setOnClickListener(v -> {
            if (HealthySubView.getVisibility() == View.VISIBLE) {
                HealthySubView.setVisibility(View.GONE);
            } else {
                HealthySubView.setVisibility(View.VISIBLE);
            }
        });

        mPositionToggle.setOnClickListener(v -> {
            if (PositionSubView.getVisibility() == View.VISIBLE) {
                PositionSubView.setVisibility(View.GONE);
            } else {
                PositionSubView.setVisibility(View.VISIBLE);
            }
        });

        mOnItemClickListener = type -> ((MainActivity)getActivity()).changeFragment(HealthMainFragment.newInstance(type.ordinal()));
    }

    private void initRecyclerView() {
        mHealthyList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mHealthyList.setLayoutManager(mLayoutManager);
        testData();
        mAdapter = new HealthyItemAdapter(myDataset, mOnItemClickListener);
        mHealthyList.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        EnterSubView.setVisibility(View.GONE);
        HealthySubView.setVisibility(View.GONE);
        PositionSubView.setVisibility(View.GONE);
        if (mStudent != null && !TextUtils.isEmpty(mStudent.getRoll_no())) {
            mStudentIdView.setText(mStudent.getRoll_no());
            mTitleView.setText(mStudent.getName());
        }
    }

    private void testData() {
        if (myDataset.size() == 0) {
            for (HealthyItem.TYPE type : HealthyItem.TYPE.values()) {
                HealthyItem item = new HealthyItem();
                item.setItemType(type);
                item.setValue(getTestValue(type, 0));
                myDataset.add(item);
            }
        }
    }

    private int getTestValue(HealthyItem.TYPE type, int idx) {
        if (idx == 0) {
            switch(type){

                case ACTIVITY:
                    return 85;
                case CALORIES_BURNED:
                    return 1060;
                case TOTAL_STEPS:
                    return 7600;
                case WALKING_TIME:
                    return 25;
                case RUNNING_TIME:
                    return 40;
                case CYCLING_TIME:
                    return 15;
                case HEART_RATE:
                    return 81;
                case SLEEP_TIME:
                    return 560;
            }
        } else {
            switch(type){

                case ACTIVITY:
                    return 76;
                case CALORIES_BURNED:
                    return 830;
                case TOTAL_STEPS:
                    return 6400;
                case WALKING_TIME:
                    return 23;
                case RUNNING_TIME:
                    return 25;
                case CYCLING_TIME:
                    return 15;
                case HEART_RATE:
                    return 85;
                case SLEEP_TIME:
                    return 530;
            }
        }
        return 0;
    }
}
