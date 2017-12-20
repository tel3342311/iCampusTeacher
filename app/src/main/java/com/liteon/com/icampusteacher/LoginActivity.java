package com.liteon.com.icampusteacher;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.liteon.com.icampusteacher.db.AccountTable;
import com.liteon.com.icampusteacher.db.DBHelper;
import com.liteon.com.icampusteacher.util.CustomDialog;
import com.liteon.com.icampusteacher.util.Def;
import com.liteon.com.icampusteacher.util.GuardianApiClient;
import com.liteon.com.icampusteacher.util.JSONResponse;
import com.liteon.com.icampusteacher.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;

public class LoginActivity extends AppCompatActivity {

    private String TAG = LoginActivity.class.getName();
    private EditText mAccount;
    private EditText mPassword;
    private Button mLogin;
    private CustomDialog mErrorDialog;
    private boolean mIsValidInput;
    private String mAccountStr;
    private String mPasswordStr;
    private TextView mforgetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAccountStr = mAccount.getText().toString();
        mPasswordStr = mPassword.getText().toString();
        if (checkInfoValid()) {
            mLogin.setEnabled(true);
        } else {
            mLogin.setEnabled(false);
        }
    }

    private void findViews() {

        mAccount = findViewById(R.id.login_account);
        mPassword = findViewById(R.id.login_password);
        mLogin = findViewById(R.id.ap_login);
        mforgetPassword = findViewById(R.id.forget_password);
    }

    private void setListener() {
        mAccount.addTextChangedListener(mAccountTextWatcher);
        mPassword.addTextChangedListener(mPasswordTextWatcher);
        mLogin.setOnClickListener(v -> new LoginTask().execute());
        mforgetPassword.setOnClickListener(v -> {

            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, UserResetPasswordActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    private TextWatcher mAccountTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            mAccountStr = editable.toString();
            if (checkInfoValid()) {
                mLogin.setEnabled(true);
            } else {
                mLogin.setEnabled(false);
            }
        }
    };

    private TextWatcher mPasswordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            mPasswordStr = editable.toString();
            if (checkInfoValid()) {
                mLogin.setEnabled(true);
            } else {
                mLogin.setEnabled(false);
            }
        }
    };

    class LoginTask extends AsyncTask<Void , String , Boolean> {
        private String mErrorMsg;
        @Override
        protected Boolean doInBackground(Void... voids) {
            String account = mAccountStr;
            String password = mPasswordStr;
            Log.d(TAG, "Account: " + account + ", password : " + password);

            if (!Utils.isNetworkConnectionAvailable()) {
                mErrorMsg = getString(R.string.login_error_no_server_connection);
                return false;
            }

            if (Utils.isEmailValid(account)) {
                //call API
                GuardianApiClient apiClient = GuardianApiClient.getInstance(LoginActivity.this);
                final JSONResponse  response = apiClient.login(account, password);
                if (response == null) {
                    return null;
                }
                if (TextUtils.equals(Def.RET_ERR_01, response.getReturn().getResponseSummary().getStatusCode())) {
                    runOnUiThread(() -> showLoginErrorDialog(getString(R.string.login_error_email), getString(android.R.string.ok)));
                    return null;
                }
                DBHelper helper = DBHelper.getInstance(LoginActivity.this);
                SharedPreferences sp = getApplicationContext().getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
                String token = response.getReturn().getResponseSummary().getSessionId();
                ContentValues cv = new ContentValues();
                cv.put(AccountTable.AccountEntry.COLUMN_NAME_USER_NAME, account);
                cv.put(AccountTable.AccountEntry.COLUMN_NAME_PASSWORD, password);
                cv.put(AccountTable.AccountEntry.COLUMN_NAME_TOKEN, token);
                if (!TextUtils.isEmpty(helper.getAccountToken(helper.getReadableDatabase(), account))) {
                    helper.updateAccountToken(helper.getWritableDatabase(), account, token);
                } else {
                    helper.insertAccount(helper.getWritableDatabase(), cv);
                }
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Def.SP_LOGIN_TOKEN, token);
                editor.commit();

                //get Child list
                JSONResponse response_childList = apiClient.getChildrenList();
                List<JSONResponse.Student> studentList = Arrays.asList(response_childList.getReturn().getResults().getStudents());
                if (studentList.size() > 0) {
                    //clear child list in db
                    helper.clearChildList(helper.getWritableDatabase());
                    //Save child list to db
                    helper.insertChildList(helper.getWritableDatabase(), studentList);
                }
                //get Reminder list
                JSONResponse reminders = apiClient.getReminders();
                List<JSONResponse.Contents> reminderList = Arrays.asList(reminders.getReturn().getResults().getReminders()[0].getContents());
                if (reminderList.size() > 0) {
                    helper.clearReminderList(helper.getWritableDatabase());
                    helper.insertReminderList(helper.getWritableDatabase(), reminderList);
                }
                return true;
            } else {
                mErrorMsg = getString(R.string.login_error_account_or_password);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isLoginSuccess) {

            if (isLoginSuccess) {
                finish();
            } else {
                //showErrorDialog(mErrorMsg);
                Utils.showErrorDialog(mErrorMsg);
            }
        }
    }

    private boolean checkInfoValid() {
        if (!TextUtils.isEmpty(mAccountStr) && !TextUtils.isEmpty(mPasswordStr)) {
            mIsValidInput = true;
        } else {
            mIsValidInput = false;
        }
        return mIsValidInput;
    }

    private void showLoginErrorDialog(String title, String btnText) {
        final CustomDialog dialog = new CustomDialog();
        dialog.setTitle(title);
        dialog.setIcon(R.drawable.ic_error_outline_black_24dp);
        dialog.setBtnText(btnText);
        dialog.setBtnConfirm(v -> dialog.dismiss());
        dialog.show(getSupportFragmentManager(), "dialog_fragment");
    }
}
