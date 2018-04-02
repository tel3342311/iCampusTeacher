package com.liteon.com.icampusteacher.fragments;


import android.animation.FloatEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.liteon.com.icampusteacher.App;
import com.liteon.com.icampusteacher.MainActivity;
import com.liteon.com.icampusteacher.R;
import com.liteon.com.icampusteacher.db.DBHelper;
import com.liteon.com.icampusteacher.util.Def;
import com.liteon.com.icampusteacher.util.GuardianApiClient;
import com.liteon.com.icampusteacher.util.HealthyItem;
import com.liteon.com.icampusteacher.util.HealthyItemAdapter;
import com.liteon.com.icampusteacher.util.JSONResponse;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    private GoogleMap mGoogleMap;
    private GradientDrawable mGradientDrawable;
    private static final int DURATION = 3000;
    private ValueAnimator mValueAnimator;
    private static LatLng mLastPosition = new LatLng(25.077877, 121.571141);
    private String mLastPositionUpdateTime;
    private Bitmap mBitmap;
    private FloatingActionButton mLocationOnMap;
    private boolean isAlerted;
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
    private TextView mUpdateTimeView;
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
        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);
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
        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);
            mMapView.setVisibility(View.INVISIBLE);
        }
        mLocationOnMap.setVisibility(View.INVISIBLE);
        initMapComponent();
        return rootView;
    }

    private void initMapComponent() {
        mMapView.getMapAsync(mOnMapReadyCallback);
    }
    private OnMapReadyCallback mOnMapReadyCallback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap map) {
            mGoogleMap = map;
            //mGoogleMap.setMaxZoomPreference(18);
            //mGoogleMap.setMinZoomPreference(12);
            new getCurrentLocation().execute("");
        }
    };

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

        mUpdateTimeView = mPositionToggle.findViewById(R.id.postion_update_date);
        mLocationOnMap = mPositionToggle.findViewById(R.id.map_location);

        //Hide these item
        EnterSubView.setVisibility(View.INVISIBLE);
        HealthySubView.setVisibility(View.GONE);
    }

    private void setupListener() {
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_navigate_before_white_24dp));
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
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null) {
            mMapView.onLowMemory();
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
    private void showRipples(LatLng latLng) {
        // Convert the drawable to bitmap
        Canvas canvas = new Canvas(mBitmap);
        mGradientDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        mGradientDrawable.draw(canvas);

        // Radius of the circle
        final int radius = 70;

        // Add the circle to the map
        final GroundOverlay circle = mGoogleMap.addGroundOverlay(new GroundOverlayOptions()
                .position(latLng, 2 * radius).image(BitmapDescriptorFactory.fromBitmap(mBitmap)));

        // Prep the animator
        PropertyValuesHolder radiusHolder = PropertyValuesHolder.ofFloat("radius", 0, radius);
        PropertyValuesHolder transparencyHolder = PropertyValuesHolder.ofFloat("transparency", 0, 1);
        mValueAnimator = new ValueAnimator();
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.setValues(radiusHolder, transparencyHolder);
        mValueAnimator.setDuration(DURATION);
        mValueAnimator.setEvaluator(new FloatEvaluator());
        mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedRadius = (float) valueAnimator.getAnimatedValue("radius");
                float animatedAlpha = (float) valueAnimator.getAnimatedValue("transparency");
                circle.setDimensions(animatedRadius * 2);
                circle.setTransparency(animatedAlpha);
            }
        });

        // start the animation
        mValueAnimator.start();
    }

    public void stopRipples() {
        mValueAnimator.end();
    }

    public void setAlert(LatLng position, String updateTime) {
        mLastPosition = position;
        mGoogleMap.clear();
        if (isDetached()) {
            return;
        }
        showRipples(mLastPosition);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mLastPosition, 16);
        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.addMarker(new MarkerOptions()
                .position(mLastPosition)
                .title("最後位置"));
        mLocationOnMap.setVisibility(View.VISIBLE);
    }

    class getCurrentLocation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            GuardianApiClient apiClient = GuardianApiClient.getInstance(App.getContext());
            JSONResponse response = apiClient.getStudentLocation(mStudent);
            if (response == null) {
                return null;
            }
            if (TextUtils.equals(Def.RET_SUCCESS_1, response.getReturn().getResponseSummary().getStatusCode())) {
                if (response.getReturn().getResults() == null) {
                    return "";
                }
                String lat = response.getReturn().getResults().getLatitude();
                String lnt = response.getReturn().getResults().getLongitude();
                if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lnt)) {
                    return "";
                }
                mLastPositionUpdateTime = response.getReturn().getResults().getEvent_occured_date();

                mLastPosition = new LatLng(Double.parseDouble(lat), Double.parseDouble(lnt));
                return mLastPositionUpdateTime;
            } else if (TextUtils.equals(Def.RET_ERR_02, response.getReturn().getResponseSummary().getStatusCode())) {
                return Def.RET_ERR_02;
            }
            return "";
        }

        protected void onPostExecute(String result) {
            mMapView.setVisibility(View.VISIBLE);
            if (TextUtils.equals(Def.RET_ERR_02, result)) {
                Toast.makeText(getActivity(), "Token provided is expired, need to re-login", Toast.LENGTH_LONG).show();
                return;
            }
            if (mGoogleMap != null) {
                mGoogleMap.addMarker(new MarkerOptions().position(mLastPosition).title("最後位置"));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mLastPosition, 16);
                mGoogleMap.moveCamera(cameraUpdate);
                if (isAlerted) {
                    setAlert(mLastPosition, "2017-07-10 週一 07:50");
                }
                if (TextUtils.isEmpty(result)) {
                    mUpdateTimeView.setText(R.string.unable_to_locate);
                } else {
                    SimpleDateFormat sdFormat = new SimpleDateFormat();
                    String format = "yyyy-MM-dd HH:mm:ss.S";
                    sdFormat.applyPattern(format);
                    Date date = Calendar.getInstance().getTime();
                    if (!TextUtils.isEmpty(mLastPositionUpdateTime)) {
                        try {
                            date = sdFormat.parse(mLastPositionUpdateTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd EE HH:mm");
                    String updateTime = sdf.format(date);
                    mUpdateTimeView.setText(updateTime);
                }
            }
        };
    }
}
