package com.liteon.com.icampusteacher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.liteon.com.icampusteacher.util.Def;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    private final static int RC_USER_TERM = 1000;
    private TextView mQuit;
    private TextView mConfirm;
    private TextView user_term_click;
    private CheckBox mRadioButtonImprove;
    private SharedPreferences mSharePreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        findViews();
        setListener();
        mSharePreference = getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
    }

    private void findViews() {
        mQuit = findViewById(R.id.option_quit);
        mConfirm = findViewById(R.id.option_agree);
        user_term_click = findViewById(R.id.user_term_click);
        mRadioButtonImprove = findViewById(R.id.user_improve_plan);
    }

    private void setListener() {
        mQuit.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        user_term_click.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean imporve = mSharePreference.getBoolean(Def.SP_IMPROVE_PLAN, false);
        mRadioButtonImprove.setChecked(imporve);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.option_quit) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        } else if (view.getId() == R.id.option_agree) {
            SharedPreferences.Editor editor = mSharePreference.edit();
            editor.putInt(Def.SP_USER_TERM_READ, 1);
            editor.commit();
            finish();
        } else if (view.getId() == R.id.user_term_click) {
            Intent intent = new Intent();
            intent.setClass(WelcomeActivity.this, UserTermActivity.class);
            startActivityForResult(intent, RC_USER_TERM);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RC_USER_TERM == requestCode) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
}
