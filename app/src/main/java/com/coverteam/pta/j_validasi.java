package com.coverteam.pta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class j_validasi extends AppCompatActivity implements View.OnClickListener{

    String idcuti,statuscuti,alasantolakstring,username;
    TextView in_nama,in_nip,in_jabatan,in_alasan,sisa_cuti,in_alamat,in_hp,in_tgl_mulai,in_tgl_selesai;
    TextView statuspegawai,statusatasan,statuspejabat,txalasantolak;
    LinearLayout laypegawaiacc,layatasan,layatasanacc,laypejabat,laypejabatacc,laypenolakan;
    EditText alasantolak;

    Button kirim;
    DatabaseReference pengajuancuti;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_j_validasi);

        Intent intent = getIntent();
        idcuti = intent.getStringExtra("cutiid");

        in_nama = findViewById(R.id.in_nama);
        in_nip = findViewById(R.id.in_nip);
        in_jabatan = findViewById(R.id.in_jabatan);
        in_alasan = findViewById(R.id.in_alasan);
        sisa_cuti = findViewById(R.id.sisa_cuti);
        in_alamat = findViewById(R.id.in_alamat);
        in_hp = findViewById(R.id.in_hp);
        in_tgl_mulai = findViewById(R.id.in_tgl_mulai);
        in_tgl_selesai = findViewById(R.id.in_tgl_selesai);
        statuspegawai = findViewById(R.id.txt_valid1);
        statusatasan = findViewById(R.id.txt_valid2);
        statuspejabat = findViewById(R.id.txt_valid3);
        laypegawaiacc = findViewById(R.id.laypegawaiacc);
        layatasan = findViewById(R.id.layatasan);
        layatasanacc = findViewById(R.id.layatasanacc);
        laypejabat = findViewById(R.id.laypejabat);
        laypejabatacc = findViewById(R.id.laypejabatacc);
        laypenolakan = findViewById(R.id.layalasantolak);
        alasantolak = findViewById(R.id.alasantolak);
        txalasantolak = findViewById(R.id.txalasantolak);
        kirim = findViewById(R.id.kirim);

        findViewById(R.id.terima1).setOnClickListener(this);
        findViewById(R.id.terima2).setOnClickListener(this);
        findViewById(R.id.terima3).setOnClickListener(this);
        findViewById(R.id.tolak1).setOnClickListener(this);
        findViewById(R.id.tolak2).setOnClickListener(this);
        findViewById(R.id.tolak3).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.kirim).setOnClickListener(this);

        getPengajuanCutiFromDB();
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.terima1:
                changeStatus("cekatasan");
                break;
            case R.id.tolak1:
                changeStatus("ditolakpegawai");
                break;
            case R.id.terima2:
                changeStatus("cekpejabat");
                break;
            case R.id.tolak2:
                changeStatus("ditolakatasan");
                break;
            case R.id.terima3:
                changeStatus("allok");
                break;
            case R.id.tolak3:
                changeStatus("ditolakpejabat");
                break;
            case R.id.back:
                Intent home = new Intent(j_validasi.this, d_menuUtama.class);
                startActivity(home);
                break;
            case R.id.kirim:
                kirimalasan();
                break;
        }
    }

    private void kirimalasan() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("Pengajuan_Cuti").child(idcuti);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    dataSnapshot.getRef().child("cutiTolakAlasan").setValue(alasantolak.getText().toString());
                    getPengajuanCutiFromDB();
                    Toast.makeText(getApplicationContext(), "Alasan Penolakan Cuti Telah Diperbarui!", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Terjadi Kesalahan 3", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPengajuanCutiFromDB() {
        pengajuancuti = FirebaseDatabase.getInstance().getReference()
                .child("Pengajuan_Cuti").child(idcuti);
        pengajuancuti.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    in_nama.setText(Objects.requireNonNull(dataSnapshot.child("cutiNama").getValue()).toString());
                    in_nip.setText(Objects.requireNonNull(dataSnapshot.child("cutiNIP").getValue()).toString());
                    username = Objects.requireNonNull(dataSnapshot.child("cutiUsername").getValue()).toString();
                    in_jabatan.setText(Objects.requireNonNull(dataSnapshot.child("cutiJabatan").getValue()).toString());
                    in_alamat.setText(Objects.requireNonNull(dataSnapshot.child("cutiAlamat").getValue()).toString());
                    in_alasan.setText(Objects.requireNonNull(dataSnapshot.child("cutiAlasan").getValue()).toString());
                    in_hp.setText(Objects.requireNonNull(dataSnapshot.child("cutiNoHP").getValue()).toString());
                    in_tgl_mulai.setText(Objects.requireNonNull(dataSnapshot.child("cutiTglMulai").getValue()).toString());
                    in_tgl_selesai.setText(Objects.requireNonNull(dataSnapshot.child("cutiTglSelesai").getValue()).toString());
                    alasantolakstring = dataSnapshot.child("cutiTolakAlasan").getValue().toString();
                    txalasantolak.setText(Objects.requireNonNull(dataSnapshot.child("cutiTolakAlasan").getValue()).toString());
                    statuscuti = dataSnapshot.child("cutiStatus").getValue().toString();
                    if (statuscuti.equals("ditolakpegawai")){
                        statuspegawai.setText("DATA DI TOLAK OLEH KEPEGAWAIAN");
                        laypegawaiacc.setVisibility(View.GONE);
                        laypenolakan.setVisibility(View.VISIBLE);
                        if (!alasantolakstring.equals("")){
                            alasantolak.setVisibility(View.GONE);
                            kirim.setVisibility(View.GONE);
                        }
                    }
                    else if (statuscuti.equals("cekatasan")){
                        statuspegawai.setText("DATA DI TERIMA OLEH KEPEGAWAIAN");
                        statuspegawai.setTextColor(Color.parseColor("#5ABD8C"));
                        laypegawaiacc.setVisibility(View.GONE);
                        layatasan.setVisibility(View.VISIBLE);
                    }
                    else if (statuscuti.equals("ditolakatasan")){
                        statuspegawai.setText("DATA DI TERIMA OLEH KEPEGAWAIAN");
                        statuspegawai.setTextColor(Color.parseColor("#5ABD8C"));
                        statusatasan.setText("ATASAN MENOLAK PENGAJUAN CUTI");
                        laypegawaiacc.setVisibility(View.GONE);
                        layatasan.setVisibility(View.VISIBLE);
                        layatasanacc.setVisibility(View.GONE);
                        laypenolakan.setVisibility(View.VISIBLE);
                        if (!alasantolakstring.equals("")){
                            alasantolak.setVisibility(View.GONE);
                            kirim.setVisibility(View.GONE);
                        }
                    }
                    else if (statuscuti.equals("cekpejabat")){
                        statuspegawai.setText("DATA DI TERIMA OLEH KEPEGAWAIAN");
                        statuspegawai.setTextColor(Color.parseColor("#5ABD8C"));
                        statusatasan.setText("PENGAJUAN CUTI DI TERIMA OLEH ATASAN");
                        statusatasan.setTextColor(Color.parseColor("#5ABD8C"));
                        laypegawaiacc.setVisibility(View.GONE);
                        layatasan.setVisibility(View.VISIBLE);
                        layatasanacc.setVisibility(View.GONE);
                        laypejabat.setVisibility(View.VISIBLE);
                    }
                    else if (statuscuti.equals("ditolakpejabat")){
                        statuspegawai.setText("DATA DI TERIMA OLEH KEPEGAWAIAN");
                        statuspegawai.setTextColor(Color.parseColor("#5ABD8C"));
                        statusatasan.setText("PENGAJUAN CUTI DI TERIMA OLEH ATASAN");
                        statusatasan.setTextColor(Color.parseColor("#5ABD8C"));
                        statuspejabat.setText("PEJABAT MENOLAK PENGAJUAN CUTI");
                        laypegawaiacc.setVisibility(View.GONE);
                        layatasan.setVisibility(View.VISIBLE);
                        layatasanacc.setVisibility(View.GONE);
                        laypejabat.setVisibility(View.VISIBLE);
                        laypejabatacc.setVisibility(View.GONE);
                        laypenolakan.setVisibility(View.VISIBLE);
                        if (!alasantolakstring.equals("")){
                            alasantolak.setVisibility(View.GONE);
                            kirim.setVisibility(View.GONE);
                        }
                    }
                    else if (statuscuti.equals("allok")){
                        statuspegawai.setText("DATA DI TERIMA OLEH KEPEGAWAIAN");
                        statuspegawai.setTextColor(Color.parseColor("#5ABD8C"));
                        statusatasan.setText("PENGAJUAN CUTI DI TERIMA OLEH ATASAN");
                        statusatasan.setTextColor(Color.parseColor("#5ABD8C"));
                        statuspejabat.setText("PENGAJUAN CUTI DI TERIMA OLEH PEJABAT");
                        statuspejabat.setTextColor(Color.parseColor("#5ABD8C"));
                        laypegawaiacc.setVisibility(View.GONE);
                        layatasan.setVisibility(View.VISIBLE);
                        layatasanacc.setVisibility(View.GONE);
                        laypejabat.setVisibility(View.VISIBLE);
                        laypejabatacc.setVisibility(View.GONE);
                    }
                    getInformationFromDB();
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

    private void getInformationFromDB() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("pegawai2").child(username);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    sisa_cuti.setText(Objects.requireNonNull(dataSnapshot.child("SISA_CUTI").getValue()).toString());
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Terjadi Kesalahan 2", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void changeStatus(final String status) {
        pengajuancuti.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                        dataSnapshot.getRef().child("cutiStatus").setValue(status);
                        getPengajuanCutiFromDB();
                        Toast.makeText(getApplicationContext(), "Status Cuti Telah Diperbarui!", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
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
