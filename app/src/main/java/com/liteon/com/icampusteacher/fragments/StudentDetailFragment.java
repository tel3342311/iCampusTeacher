package com.liteon.com.icampusteacher.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.MapView;
import com.liteon.com.icampusteacher.R;

import org.w3c.dom.Text;

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
    private RecyclerView mHealthyList;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_STUDENT_NAME = "ARG_STUDENT_NAME";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mStudentName;
    private String mParam2;


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
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStudentName = getArguments().getString(ARG_STUDENT_NAME);
            mParam2 = getArguments().getString(ARG_PARAM2);

            if (!TextUtils.isEmpty(mStudentName)) {

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_student_detail, container, false);
        findViews(rootView);
        return rootView;
    }

    private void findViews(View rootView) {
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

    }


}
