package com.coverteam.pta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class d_menuUtama extends AppCompatActivity implements View.OnClickListener{

    LinearLayout lnr_validasi;
    TextView nama,nip,namahorizon1,namahorizon2,kethorizon1,kethorizon2;
    ProgressBar progressBar,progressbar2,progressbar3,progressBar0;
    ImageView gambarhorizon, gambarhorizon2,fotouser;

    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    String id1 = "",id2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_menu_utama);
        getUsernameLocal();

        findViewById(R.id.ikon_cuti).setOnClickListener(this);
        findViewById(R.id.ikon_profil).setOnClickListener(this);
        findViewById(R.id.ikon_panduan).setOnClickListener(this);
        findViewById(R.id.ikon_riwayat).setOnClickListener(this);
        findViewById(R.id.ikon_validasi).setOnClickListener(this);
        findViewById(R.id.agenda1).setOnClickListener(this);
        findViewById(R.id.agenda2).setOnClickListener(this);

        nama = findViewById(R.id.namamenu);
        nip = findViewById(R.id.nipmenu);
        progressBar = findViewById(R.id.progressbar);
        progressbar2 = findViewById(R.id.progressbar2);
        progressbar3 = findViewById(R.id.progressbar3);
        progressBar0 = findViewById(R.id.progressbar0);
        lnr_validasi = findViewById(R.id.ikon_validasi);
        namahorizon1 = findViewById(R.id.txhorizon);
        namahorizon2 = findViewById(R.id.txhorizon2);
        kethorizon1 = findViewById(R.id.txhorizonket);
        kethorizon2 = findViewById(R.id.txhorizonket2);
        gambarhorizon = findViewById(R.id.gambarhorizon);
        gambarhorizon2 = findViewById(R.id.gambarhorizon2);
        fotouser = findViewById(R.id.fotouserhome);

        if (username_key_new.equals("admin")){
            lnr_validasi.setVisibility(View.VISIBLE);
        }

        if (username_key_new.equals("16312182")){
            lnr_validasi.setVisibility(View.VISIBLE);
        }


        getInformationFromDB();
        getAgendaNew();
    }
    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ikon_cuti:
                Intent gocuti = new Intent(d_menuUtama.this, e_FormCuti.class);
                startActivity(gocuti);
                break;
            case R.id.ikon_profil:
                Intent goprofil = new Intent(d_menuUtama.this, f_profil.class);
                startActivity(goprofil);
                break;
            case R.id.ikon_panduan:
                Intent gopanduan = new Intent(d_menuUtama.this, h_panduan.class);
                startActivity(gopanduan);
                break;
            case R.id.ikon_riwayat:
                Intent goriwayat = new Intent(d_menuUtama.this, i_list_riwayat_cuti.class);
                startActivity(goriwayat);
                break;
            case R.id.ikon_validasi:
                Intent govalidasi = new Intent(d_menuUtama.this, k_list_valid.class);
                startActivity(govalidasi);
                break;
            case R.id.agenda1:
                if (id1.equals("")){
                    break;
                }
                else {
                    Intent itemone = new Intent(d_menuUtama.this, l_agenda.class);
                    itemone.putExtra("id",id1);
                    startActivity(itemone);
                    break;
                }
            case R.id.agenda2:
                if (id2.equals("")){
                    break;
                }
                else {
                    Intent itemtwo = new Intent(d_menuUtama.this, l_agenda.class);
                    itemtwo.putExtra("id",id2);
                    startActivity(itemtwo);
                    break;
                }
        }
    }

    private void getInformationFromDB() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("pegawai2").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    nama.setText(dataSnapshot.child("NAMA").getValue().toString());
                    nip.setText(dataSnapshot.child("NIP").getValue().toString());
                    Picasso.with(d_menuUtama.this)
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
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAgendaNew() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("Agenda");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    int index = (int) dataSnapshot.getChildrenCount();
                    int index2 = index-1;
                    int count = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        count++;
                        if (count == index) {
                            id1 = snapshot.child("id").getValue().toString();
                            namahorizon1.setText(snapshot.child("judul").getValue().toString());
                            kethorizon1.setText(snapshot.child("keterangan").getValue().toString());
                            Picasso.with(d_menuUtama.this)
                                    .load(snapshot.child("gambar").getValue().toString()).centerCrop().fit()
                                    .into(gambarhorizon, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            progressbar2.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }
                        if (count == index2) {
                            id2 = snapshot.child("id").getValue().toString();
                            namahorizon2.setText(snapshot.child("judul").getValue().toString());
                            kethorizon2.setText(snapshot.child("keterangan").getValue().toString());
                            Picasso.with(d_menuUtama.this)
                                    .load(snapshot.child("gambar").getValue().toString()).centerCrop().fit()
                                    .into(gambarhorizon2, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            progressbar3.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
