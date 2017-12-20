package com.liteon.com.icampusteacher.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liteon.com.icampusteacher.MainActivity;
import com.liteon.com.icampusteacher.R;
import com.liteon.com.icampusteacher.util.HealthyItem.TYPE;
import com.liteon.com.icampusteacher.util.HealthyItemAdapter.ViewHolder.IHealthViewHolderClicks;

public class HealthMainFragment extends Fragment implements IHealthViewHolderClicks {

    private View mRootView;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private TextView mTitleView;
    private DrawerLayout mDrawerLayout;
    private static final int PAGE_COUNT = 8;
    private static final int HEALTHY_ACTIVITY = 0;
    private static final int HEALTHY_CALORIES = 1;
    private static final int HEALTHY_STEPS = 2;
    private static final int HEALTHY_WALKING = 3;
    private static final int HEALTHY_RUNNING = 4;
    private static final int HEALTHY_CYCLING = 5;
    private static final int HEALTHY_HEART_RATE = 6;
    private static final int HEALTHY_SLEEPING = 7;

    private static final String ARG_PAGE_IDX = "ARG_PAGE_IDX";
    private static int mPageIdx;

    public static HealthMainFragment newInstance(int pageIdx) {
        HealthMainFragment fragment = new HealthMainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_IDX, pageIdx);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPageIdx = getArguments().getInt(ARG_PAGE_IDX, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_healthy_main, container, false);
        findViews();
        setupViewPager();
        return mRootView;
    }

    private void findViews() {
        mViewPager = mRootView.findViewById(R.id.view_pager);
        mToolbar = getActivity().findViewById(R.id.toolbar);
        mTitleView = getActivity().findViewById(R.id.toolbar_title);
        mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);
    }

    private void setupViewPager() {
        mViewPager.setAdapter(buildAdapter());
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
    }

    private void setupTitleBar(int position) {
        //mToolbar.setTitle(mViewPager.getAdapter().getPageTitle(position));
        mTitleView.setText(mViewPager.getAdapter().getPageTitle(position));

        mToolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        mToolbar.setNavigationOnClickListener(v -> ((MainActivity)getActivity()).onBackPressed());
    }

    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            setupTitleBar(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }
    };

    private PagerAdapter buildAdapter() {
        return (new HealthPageAdapter(getActivity(), getChildFragmentManager()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewPager.setCurrentItem(mPageIdx);
    }

    class HealthPageAdapter extends FragmentPagerAdapter {


        Context mContext;

        public HealthPageAdapter(Context mCtx, FragmentManager fm) {
            super(fm);
            mContext = mCtx;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == HEALTHY_ACTIVITY) {
                return new DailyHealthFragment(TYPE.ACTIVITY);
            } else if (position == HEALTHY_CALORIES) {
                return new DailyHealthFragment(TYPE.CALORIES_BURNED);
            } else if (position == HEALTHY_STEPS) {
                return new DailyHealthFragment(TYPE.TOTAL_STEPS);
            } else if (position == HEALTHY_WALKING) {
                return new DailyHealthFragment(TYPE.WALKING_TIME);
            } else if (position == HEALTHY_RUNNING) {
                return new DailyHealthFragment(TYPE.RUNNING_TIME);
            } else if (position == HEALTHY_CYCLING) {
                return new DailyHealthFragment(TYPE.CYCLING_TIME);
            } else if (position == HEALTHY_HEART_RATE) {
                return new DailyHealthFragment(TYPE.HEART_RATE);
            } else if (position == HEALTHY_SLEEPING) {
                return new DailyHealthFragment(TYPE.SLEEP_TIME);
            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == HEALTHY_ACTIVITY) {
                return TYPE.ACTIVITY.getName();
            } else if (position == HEALTHY_CALORIES) {
                return TYPE.CALORIES_BURNED.getName();
            } else if (position == HEALTHY_STEPS) {
                return TYPE.TOTAL_STEPS.getName();
            } else if (position == HEALTHY_WALKING) {
                return TYPE.WALKING_TIME.getName();
            } else if (position == HEALTHY_RUNNING) {
                return TYPE.RUNNING_TIME.getName();
            } else if (position == HEALTHY_CYCLING) {
                return TYPE.CYCLING_TIME.getName();
            } else if (position == HEALTHY_HEART_RATE) {
                return TYPE.HEART_RATE.getName();
            } else if (position == HEALTHY_SLEEPING) {
                return TYPE.SLEEP_TIME.getName();
            }
            return super.getPageTitle(position);
        }
    }

    @Override
    public void onClick(TYPE type) {
        switch (type) {
            case ACTIVITY:
                mViewPager.setCurrentItem(HEALTHY_ACTIVITY, false);
                break;
            case CALORIES_BURNED:
                mViewPager.setCurrentItem(HEALTHY_CALORIES, false);
                break;
            case CYCLING_TIME:
                mViewPager.setCurrentItem(HEALTHY_CYCLING, false);
                break;
            case HEART_RATE:
                mViewPager.setCurrentItem(HEALTHY_HEART_RATE, false);
                break;
            case RUNNING_TIME:
                mViewPager.setCurrentItem(HEALTHY_RUNNING, false);
                break;
            case SLEEP_TIME:
                mViewPager.setCurrentItem(HEALTHY_SLEEPING, false);
                break;
            case TOTAL_STEPS:
                mViewPager.setCurrentItem(HEALTHY_STEPS, false);
                break;
            case WALKING_TIME:
                mViewPager.setCurrentItem(HEALTHY_WALKING, false);
                break;
            default:
                break;

        }
    }
}
