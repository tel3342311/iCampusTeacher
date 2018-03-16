package com.liteon.com.icampusteacher;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liteon.com.icampusteacher.db.DBHelper;
import com.liteon.com.icampusteacher.util.ClsUtils;
import com.liteon.com.icampusteacher.util.CustomDialog;
import com.liteon.com.icampusteacher.util.Def;
import com.liteon.com.icampusteacher.util.GuardianApiClient;
import com.liteon.com.icampusteacher.util.JSONResponse;
import com.liteon.com.icampusteacher.util.Utils;

public class UserInfoActivity extends AppCompatActivity {

    private boolean mIsEditMode;
    private boolean mIsEnableConfirm;
    private Toolbar mToolbar;
    private ImageView mBack;
    private EditText mAccount;
    private EditText mOriginPassword;
    private EditText mNewPassword;
    private EditText mConfirmNewPassword;
    private TextView mPasswordHint;
    private DBHelper mDbHelper;
    private String mToken;
    private JSONResponse.Parent mAccountItem;
    private View progressBarHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        findViews();
        setupToolbar();
        setListener();
        mDbHelper = DBHelper.getInstance(this);
    }

    private void findViews() {
        mToolbar = findViewById(R.id.toolbar);
        mBack = findViewById(R.id.cancel);
        mAccount = findViewById(R.id.login_account);
        mOriginPassword = findViewById(R.id.login_password);
        mNewPassword = findViewById(R.id.login_new_password);
        mConfirmNewPassword = findViewById(R.id.login_new_password_confirm);
        mPasswordHint = findViewById(R.id.password_hint);
        progressBarHolder = findViewById(R.id.progressBarHolder);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    private void setListener() {
        mBack.setOnClickListener( v -> onBackPressed());
        mOriginPassword.addTextChangedListener(mTextWatch);
        mNewPassword.addTextChangedListener(mTextWatch);
        mConfirmNewPassword.addTextChangedListener(mTextWatch);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_and_confirm_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {

        if (mIsEditMode) {
            menu.findItem(R.id.action_edit).setVisible(false);
            menu.findItem(R.id.action_confirm).setVisible(mIsEnableConfirm);
        } else {
            menu.findItem(R.id.action_edit).setVisible(true);
            menu.findItem(R.id.action_confirm).setVisible(false);
        }
        return true;
    };

    private boolean validateInput() {
        if (TextUtils.isEmpty(mOriginPassword.getText()) ||
                TextUtils.isEmpty(mConfirmNewPassword.getText()) ||
                TextUtils.isEmpty(mNewPassword.getText())) {
            return false;
        }

        if (!TextUtils.equals(mAccountItem.getPassword(), mOriginPassword.getText())) {
            mPasswordHint.setVisibility(View.VISIBLE);
            mPasswordHint.setText(R.string.origin_password_wrong);
            return false;
        } else {
            mPasswordHint.setVisibility(View.INVISIBLE);
            mPasswordHint.setText("");
        }
        //check if password is match constraint
        if (!ClsUtils.isValidPassword(mNewPassword.getText().toString())) {
            mPasswordHint.setVisibility(View.VISIBLE);
            mPasswordHint.setText(R.string.new_password_hint);
            return false;
        } else {
            mPasswordHint.setVisibility(View.INVISIBLE);
            mPasswordHint.setText("");
        }
        //check if password & password confirm is match
        if (!TextUtils.equals(mConfirmNewPassword.getText(), mNewPassword.getText())) {
            mPasswordHint.setVisibility(View.VISIBLE);
            mPasswordHint.setText(R.string.new_password_wrong);
            return false;
        } else {
            mPasswordHint.setVisibility(View.INVISIBLE);
            mPasswordHint.setText("");
        }
        //Enable to check constraint
        //ClsUtils.isValidPassword(mOriginPassword.getText().toString())

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_confirm:
                updateAccount();
                break;
            case R.id.action_edit:
                enterEditMode();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void enterEditMode() {
        mIsEditMode = true;
        mNewPassword.setVisibility(View.VISIBLE);
        mConfirmNewPassword.setVisibility(View.VISIBLE);
        mOriginPassword.setEnabled(true);
        mOriginPassword.setText("");
        mNewPassword.setText("");
        mConfirmNewPassword.setText("");
        mPasswordHint.setVisibility(View.VISIBLE);
        mPasswordHint.setText(R.string.password_hint);

        invalidateOptionsMenu();
    }

    private void updateAccount() {
        new UpdateInfoTask().execute(mConfirmNewPassword.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences(Def.SHARE_PREFERENCE, MODE_PRIVATE);
        mToken = sp.getString(Def.SP_LOGIN_TOKEN, "");
        mAccountItem = mDbHelper.getAccountByToken(mDbHelper.getReadableDatabase(), mToken);
        mToolbar.setTitle("");
        mAccount.setText(mAccountItem.getUsername());
        mOriginPassword.setText(mAccountItem.getPassword());
        mOriginPassword.setEnabled(false);
        mNewPassword.setVisibility(View.INVISIBLE);
        mConfirmNewPassword.setVisibility(View.INVISIBLE);
        mPasswordHint.setVisibility(View.INVISIBLE);
        mPasswordHint.setText("");
        mIsEditMode = mIsEnableConfirm = false;
        invalidateOptionsMenu();
    }

    private TextWatcher mTextWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (validateInput()) {
                mIsEnableConfirm = true;
            } else {
                mIsEnableConfirm = false;
            }
            invalidateOptionsMenu();
        }
    };

    class UpdateInfoTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {

            progressBarHolder.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Boolean doInBackground(String... args) {

            String password = args[0];

            GuardianApiClient apiClient = GuardianApiClient.getInstance(UserInfoActivity.this);
            apiClient.setToken(mToken);
            JSONResponse response = apiClient.updateParentDetail(password);
            if (response != null && TextUtils.equals(response.getReturn().getResponseSummary().getStatusCode(), Def.RET_SUCCESS_1) ) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressBarHolder.setVisibility(View.GONE);
            if (aBoolean.booleanValue()) {
                SharedPreferences sp = getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(Def.SP_LOGIN_TOKEN);
                editor.commit();
                finish();
            } else {
                Utils.showErrorDialog(getString(R.string.healthy_sync_failed));
            }
        }
    }


}
