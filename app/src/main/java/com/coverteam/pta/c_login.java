package com.coverteam.pta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class c_login extends AppCompatActivity {
    //implements View.OnClickListener
    Button btn_login;
    EditText username,password;
    DatabaseReference reference;
    ProgressBar progressBar;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_login);

        btn_login = findViewById(R.id.button_login);
        username = findViewById(R.id.usernamelogin);
        password = findViewById(R.id.passwordlogin);
        progressBar = findViewById(R.id.progressbar);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInformationFromDB();
            }
        });

        //findViewById(R.id.button_login).setOnClickListener(this);
        //getInformationFromDB();
    }

    /*public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                getInformationFromDB();
                break;
        }
    }*/

    private void getInformationFromDB(){
        progressBar.setVisibility(View.VISIBLE);
        final String user = username.getText().toString().trim();
        final String pass = password.getText().toString().trim();
        if (validateInputs(user,pass)) {
            reference = FirebaseDatabase.getInstance().getReference()
                    .child("pegawai2").child(user);
            //reference = FirebaseDatabase.getInstance().getReference().child("pegawai2").child("16312182");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String passwordfirebase = dataSnapshot.child("PASS").getValue().toString();
                        if(pass.equals(passwordfirebase)){
                            //save user local
                            saveuserInformation();
                            Intent gomenuutama = new Intent(c_login.this, d_menuUtama.class);
                            gomenuutama.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(gomenuutama);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"Username/Password salah!",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            username.requestFocus();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Username/Password salah!",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        username.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    private boolean validateInputs(String user, String pass) {
        if (user.isEmpty()){
            username.setError("NIP Belum Diisi");
            username.requestFocus();
            return false;
        }
        if (pass.isEmpty()){
            password.setError("Password Belum Diisi");
            password.requestFocus();
            return false;
        }
        return true;
    }

    private void saveuserInformation() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(username_key,username.getText().toString());
        editor.apply();
    }
}
