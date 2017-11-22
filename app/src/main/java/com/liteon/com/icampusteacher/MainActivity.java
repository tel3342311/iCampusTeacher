package com.liteon.com.icampusteacher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.liteon.com.icampusteacher.util.Def;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences(Def.SHARE_PREFERENCE, Context.MODE_PRIVATE);
        String token = sp.getString(Def.SP_LOGIN_TOKEN, "");
        if (sp.getInt(Def.SP_USER_TERM_READ, 0) == 0) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
        } else if (TextUtils.isEmpty(token)){
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
