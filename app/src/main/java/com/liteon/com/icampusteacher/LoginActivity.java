package com.liteon.com.icampusteacher;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.liteon.com.icampusteacher.util.CustomDialog;

public class LoginActivity extends AppCompatActivity {

    private String TAG = LoginActivity.class.getName();
    private EditText mAccount;
    private EditText mPassword;
    private Button mLogin;
    private CustomDialog mErrorDialog;
    private boolean isValidInput;
    private String mAccountStr;
    private String mPasswordStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        setListener();
    }

    private void findViews() {

        mAccount = findViewById(R.id.login_account);
        mPassword = findViewById(R.id.login_password);
        mLogin = findViewById(R.id.ap_login);
    }

    private void setListener() {
        mAccount.addTextChangedListener(mAccountTextWatcher);
        mPassword.addTextChangedListener(mPasswordTextWatcher);
        mLogin.setOnClickListener((v) -> new LoginTask().execute());
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
            mPasswordStr = editable.toString();
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
            mAccountStr = editable.toString();
        }
    };

    class LoginTask extends AsyncTask<Void , String , Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            String account = mAccountStr;
            String password = mPasswordStr;
            Log.d(TAG, "Account: " + account + ", password : " + password);

            if (isValidInput) {
                //call API
            } else {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean) {

            } else {
                showErrorDialog();
            }
        }
    }

    private void showErrorDialog() {
        mErrorDialog = new CustomDialog();
        mErrorDialog.setTitle(getString(R.string.login_error_account_or_password));
        mErrorDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
        mErrorDialog.setBtnText(getString(R.string.login_ok));
        mErrorDialog.setBtnConfirm(v -> mErrorDialog.dismiss());
        mErrorDialog.show(getSupportFragmentManager(), "dialog_fragment");

    }
}
