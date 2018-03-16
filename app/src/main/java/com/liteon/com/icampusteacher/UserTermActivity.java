package com.liteon.com.icampusteacher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.liteon.com.icampusteacher.util.Def;

public class UserTermActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mAgree;
    private TextView mQuit;
    private Toolbar mToolbar;
    private View mBottomView;
    private AppCompatCheckBox mRadioButton;
    private ImageView mCancel;
    private SharedPreferences mSharePreference;
    private TextView mAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_term);
        findViews();
        setupToolbar();
        setListener();
        mSharePreference = getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);

    }

    private void findViews() {
        mAgree = findViewById(R.id.option_agree);
        mQuit = findViewById(R.id.option_quit);
        mToolbar = findViewById(R.id.toolbar);
        mBottomView = findViewById(R.id.bottom_bar);
        mRadioButton = findViewById(R.id.user_improve_plan);
        mCancel = findViewById(R.id.cancel);
        mAppVersion = findViewById(R.id.app_ver_value);

    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        mCancel.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mToolbar.setTitle("");
        Intent intent = getIntent();
        boolean disableBottom = intent.getBooleanExtra(Def.EXTRA_DISABLE_USERTREM_BOTTOM, false);
        if (disableBottom == true) {
            mBottomView.setVisibility(View.INVISIBLE);
        } else {
            mBottomView.setVisibility(View.VISIBLE);
        }
        mRadioButton.setChecked(mSharePreference.getBoolean(Def.SP_IMPROVE_PLAN, false));
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            mAppVersion.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void setListener() {
        mAgree.setOnClickListener(this);
        mQuit.setOnClickListener(this);
        mRadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences sp = getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(Def.SP_IMPROVE_PLAN, isChecked);
            editor.commit();

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.option_agree:
                userTermRead();
                break;
            case R.id.option_quit:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }
    private void userTermRead() {
        SharedPreferences sp = getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Def.SP_USER_TERM_READ, 1);
        editor.commit();
        setResult(RESULT_OK);
        finish();
    }
}
