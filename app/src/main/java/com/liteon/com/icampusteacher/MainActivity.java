package com.liteon.com.icampusteacher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liteon.com.icampusteacher.fragments.ContactMatterFragment;
import com.liteon.com.icampusteacher.fragments.MyClassFragment;
import com.liteon.com.icampusteacher.fragments.SearchFragment;
import com.liteon.com.icampusteacher.util.Def;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private BottomNavigationView mBottomView;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private TextView mClassName;
    private Button mLogoutButton;
    private boolean mIsFirstLaunch = true;
    private Fragment mCurrentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setListener();
    }

    private void findViews() {
        mToolbar = findViewById(R.id.toolbar);
        mBottomView = findViewById(R.id.bottom_navigation);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mLogoutButton = findViewById(R.id.drawer_button_logout);
        mNavigationView = findViewById(R.id.navigation);
        mClassName = mNavigationView.getHeaderView(0).findViewById(R.id.child_name);
    }

    private void setListener() {
        mBottomView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.action_myclass:
                    changeFragment(MyClassFragment.newInstance());
                    break;

                case R.id.action_contact:
                    changeFragment(ContactMatterFragment.newInstance());
                    break;
                case R.id.action_search:
                    changeFragment(SearchFragment.newInstance());
                    break;
            }
            return true;
        });

        //Drawer Listener
        mNavigationView.setNavigationItemSelectedListener(item -> {
            Intent intent = new Intent();
            switch (item.getItemId()) {

                case R.id.action_teacher_info:
                    intent.setClass(MainActivity.this, UserInfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.action_app_info:
                    intent.setClass(MainActivity.this, UserTermActivity.class);
                    startActivity(intent);
                    break;
            }
            return true;
        });

        //Log out
        mLogoutButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        //Drawer Listener
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
        String token = sp.getString(Def.SP_LOGIN_TOKEN, "AA");
        if (sp.getInt(Def.SP_USER_TERM_READ, 0) == 0) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
        } else if (TextUtils.isEmpty(token)){
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (mIsFirstLaunch) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoadingPageActivity.class);
            startActivity(intent);
            mIsFirstLaunch = false;
        }
        mClassName.setText(R.string.class_name);
    }

    public void changeFragment(Fragment frag) {
        mCurrentFragment = frag;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, frag);
        fragmentTransaction.commit();
    }
}
