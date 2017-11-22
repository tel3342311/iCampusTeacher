package com.liteon.com.icampusteacher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.liteon.com.icampusteacher.util.Def;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mQuit;
    private TextView mConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        findViews();
        setListener();
    }

    private void findViews() {
        mQuit = findViewById(R.id.option_quit);
        mConfirm = findViewById(R.id.option_agree);
    }

    private void setListener() {
        mQuit.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.option_quit) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        } else if (view.getId() == R.id.option_agree) {
            SharedPreferences sp = getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(Def.SP_USER_TERM_READ, 1);
            editor.commit();
            finish();
        }
    }
}
