package com.coverteam.pta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class l_agenda extends AppCompatActivity {

    String id;
    DatabaseReference reference;
    ProgressBar progressBar;
    ImageView gambar,back;
    TextView judul,keterangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l_agenda);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        progressBar = findViewById(R.id.progressbar);
        gambar = findViewById(R.id.gambar);
        judul = findViewById(R.id.judul);
        keterangan = findViewById(R.id.keterangan);

        getInformationFromDB();

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getInformationFromDB() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("Agenda").child(id);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    judul.setText(dataSnapshot.child("judul").getValue().toString());
                    keterangan.setText(dataSnapshot.child("keterangan").getValue().toString());
                    Picasso.with(l_agenda.this)
                            .load(dataSnapshot.child("gambar").getValue().toString())
                            .into(gambar, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBar.setVisibility(View.GONE);
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
}
