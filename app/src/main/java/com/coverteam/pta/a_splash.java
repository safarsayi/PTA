package com.coverteam.pta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class a_splash extends AppCompatActivity {

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_splash);

        //seting 1 detk splash
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //pindah ke getstarted
                Intent gogetstarted = new Intent(a_splash.this, b_started.class);
                startActivity(gogetstarted);
                finish();
            }
        },2000);
*/
        getUsernameLocal();
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");

        if(username_key_new.isEmpty()){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent gogetstarted = new Intent(a_splash.this, b_started.class);
                    startActivity(gogetstarted);
                    finish();
                }
            },2000);
        }
        else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent gohome = new Intent(a_splash.this, d_menuUtama.class);
                    startActivity(gohome);
                    finish();
                }
            },2000);
        }
    }
}
