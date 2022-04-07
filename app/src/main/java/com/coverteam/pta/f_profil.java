package com.coverteam.pta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class f_profil extends AppCompatActivity implements View.OnClickListener {

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    DatabaseReference reference;
    ProgressBar progressBar,progressBar0;
    TextView namaprofil,nipprofil,in_nama,in_nip,in_jabatan,in_sisa;
    ImageView fotouser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_profil);

        getUsernameLocal();

        findViewById(R.id.button_logout).setOnClickListener(this);
        findViewById(R.id.button_back).setOnClickListener(this);

        namaprofil = findViewById(R.id.namaprofil);
        nipprofil = findViewById(R.id.nipprofil);
        in_nama = findViewById(R.id.in_nama);
        in_nip = findViewById(R.id.in_nip);
        in_jabatan = findViewById(R.id.in_jabatan);
        in_sisa = findViewById(R.id.in_sisa);
        progressBar = findViewById(R.id.progressbar);
        progressBar0 = findViewById(R.id.progressbar0);
        fotouser = findViewById(R.id.fotouser);
        getInformationFromDB();
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_logout:
                goLogout();
                break;
            case R.id.button_back:
                Intent goback = new Intent(f_profil.this, d_menuUtama.class);
                startActivity(goback);
                break;
        }
    }

    private void getInformationFromDB() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("pegawai2").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namaprofil.setText(dataSnapshot.child("NAMA").getValue().toString());
                nipprofil.setText(dataSnapshot.child("NIP").getValue().toString());
                in_nama.setText(dataSnapshot.child("NAMA").getValue().toString());
                in_nip.setText(dataSnapshot.child("NIP").getValue().toString());
                in_jabatan.setText(dataSnapshot.child("JABATAN").getValue().toString());
                in_sisa.setText(dataSnapshot.child("SISA_CUTI").getValue().toString());
                Picasso.with(f_profil.this)
                        .load(dataSnapshot.child("FOTO").getValue().toString())
                        .into(fotouser, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar0.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                Toast.makeText(getApplicationContext(),"Gagal Memuat Foto",Toast.LENGTH_SHORT).show();
                            }
                        });
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void goLogout(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(username_key,null);
        editor.apply();
        progressBar.setVisibility(View.VISIBLE);
        Intent gologin = new Intent(f_profil.this, c_login.class);
        gologin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(gologin);
        finish();
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
