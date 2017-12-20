package com.liteon.com.icampusteacher;

import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class UserInfoActivity extends AppCompatActivity {

    private boolean mIsEditMode;
    private Toolbar mToolbar;
    private ImageView mBack;
    private EditText mOriginPassword;
    private EditText mNewPassword;
    private EditText mConfirmNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        findViews();
        setupToolbar();
        setListener();
    }

    private void findViews() {
        mToolbar = findViewById(R.id.toolbar);
        mBack = findViewById(R.id.cancel);
        mOriginPassword = findViewById(R.id.login_password);
        mNewPassword = findViewById(R.id.login_new_password);
        mConfirmNewPassword = findViewById(R.id.login_new_password_confirm);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    private void setListener() {
        mBack.setOnClickListener( v -> onBackPressed());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.one_confirm_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {

        if (mIsEditMode) {
            menu.findItem(R.id.action_confirm).setVisible(true);
        } else {
            menu.findItem(R.id.action_confirm).setVisible(false);
        }
        return true;
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_confirm:
                updateAccount();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateAccount() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mToolbar.setTitle("");
    }
}
