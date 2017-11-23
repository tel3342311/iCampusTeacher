package com.liteon.com.icampusteacher;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.liteon.com.icampusteacher.util.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UserResetPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = UserResetPasswordActivity.class.getName();
    private EditText mName;
    private TextView mTitleView;
    private TextView mDescView;
    private AppCompatButton mSend;
    private AppCompatButton mBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reset_password);
        findViews();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSend.setEnabled(false);
    }

    private void findViews() {
        mName = findViewById(R.id.login_name);
        mTitleView = findViewById(R.id.login_title);
        mDescView = findViewById(R.id.login_desc);
        mSend = findViewById(R.id.email_send);
        mBackToLogin = findViewById(R.id.back_to_login);
    }

    private void setListener() {
        mSend.setOnClickListener(this);
        mBackToLogin.setOnClickListener(this);
        mName.addTextChangedListener(mTextWatcher);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (validateInput()) {
                mSend.setEnabled(true);
            } else {
                mSend.setEnabled(false);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_send:
                new ResetTask().execute(null,null);

                break;
            case R.id.back_to_login:
                finish();
                break;
        }
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(mName.getText())) {
            return false;
        }
        //check if acount email is valid
        if (!Utils.isEmailValid(mName.getText())) {
            return false;
        }
        return true;
    }

//    private JSONResponse resetPassword() {
//        String strName = mName.getText().toString();
//        GuardianApiClient apiClient = new GuardianApiClient(this);
//        return apiClient.resetPassword(strName);
//    }


    class ResetTask extends AsyncTask<Void, Void, Boolean> {

        protected Boolean doInBackground(Void... params) {
            //check network
//            if (!isNetworkConnectionAvailable()) {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        showErrorDialog( getString(R.string.login_no_network));
//                    }
//                });
//                return false;
//            }
//            GuardianApiClient apiClient = new GuardianApiClient(UserResetPasswordActivity.this);
//            //check server
//            if (!isURLReachable(UserResetPasswordActivity.this, apiClient.getServerUri().toString())) {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        showErrorDialog(getString(R.string.login_error_no_server_connection));
//                    }
//                });
//                return false;
//            }
//            JSONResponse response = resetPassword();
//            if (response == null) {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        showErrorDialog(getString(R.string.forget_account_not_exist));
//                    }
//                });
//                return false;
//            }
//            if (response.getReturn().getResults() == null) {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        showErrorDialog(getString(R.string.login_error_email));
//                    }
//                });
//                return false;
//            }
//            return true;
            return true;
        }

        protected void onPostExecute(Boolean isSuccess) {
//            if (isSuccess) {
//                mTitleView.setText(getString(R.string.forget_reset_done));
//                mDescView.setText(getString(R.string.forget_check_mailbox_and_setup));
//                mSend.setVisibility(View.GONE);
//                mName.setVisibility(View.INVISIBLE);
//            }
        }
    }
}
