package com.coverteam.pta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.coverteam.pta.model.DataCuti;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class e_FormCuti extends AppCompatActivity implements View.OnClickListener{

    LinearLayout btn_back,ln_notice;
    Button btn_min,btn_plus,btn_lanjut;

    Integer nilaijum_hari = 1;
    Integer nilai_sisa_cuti;
    Integer jumhari;
    TextView txsisa_cuti, txjum_hari, notice_txt;
    ImageView foto;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    String username;
    DatabaseReference reference;
    TextView in_nama,in_nip,in_jabatan,in_sisa;
    ProgressBar progressBar,progressBar0;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    EditText in_mulai,in_selesai,in_alasan,in_alamat,in_noHP;
    String idcuti;
    String masuknama,masukjabatan,masuknip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e__form_cuti);

        getUsernameLocal();
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        in_nama = findViewById(R.id.in_nama);
        in_nip = findViewById(R.id.in_nip);
        in_jabatan = findViewById(R.id.in_jabatan);
        in_sisa = findViewById(R.id.in_sisa);
        progressBar = findViewById(R.id.progressbar);
        progressBar0 = findViewById(R.id.progressbar0);
        in_mulai = findViewById(R.id.in_mulai);
        in_selesai = findViewById(R.id.in_selesai);
        in_alasan = findViewById(R.id.in_alasan);
        in_alamat = findViewById(R.id.in_alamat);
        in_noHP = findViewById(R.id.in_NoHP);
        foto = findViewById(R.id.fotouser);

        findViewById(R.id.in_mulai).setOnClickListener(this);
        findViewById(R.id.in_selesai).setOnClickListener(this);
        findViewById(R.id.btnplus).setOnClickListener(this);
        findViewById(R.id.btnmin).setOnClickListener(this);
        findViewById(R.id.button_back).setOnClickListener(this);
        findViewById(R.id.button_lanjut).setOnClickListener(this);
        btn_min = findViewById(R.id.btnmin);
        btn_plus = findViewById(R.id.btnplus);
        btn_lanjut = findViewById(R.id.button_lanjut);
        ln_notice = findViewById(R.id.ln_notice);
        txjum_hari = findViewById(R.id.jum_cuti);
        btn_min.animate().alpha(0).setDuration(300).start();

        getInformationFromDB();
    }

    private void checkCutiUser() {
        if (nilai_sisa_cuti == 0){
            in_sisa.setTextColor(Color.parseColor("#D1206B"));
            ln_notice.setVisibility(View.VISIBLE);
            btn_plus.setVisibility(View.GONE);
            btn_lanjut.setVisibility(View.GONE);
        }
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.in_mulai:
                showDatePicker();
                break;
            case R.id.in_selesai:
                showDatePicker2();
                break;
            case R.id.btnplus:
                nilaijum_hari+=1;
                txjum_hari.setText(nilaijum_hari.toString());
                if (nilaijum_hari > 1){
                    btn_min.animate().alpha(1).setDuration(300).start();
                    btn_min.setEnabled(true);
                }
                if(nilaijum_hari>nilai_sisa_cuti){
                    in_sisa.setTextColor(Color.parseColor("#D1206B"));
                    ln_notice.setVisibility(View.VISIBLE);
                    btn_plus.setVisibility(View.GONE);
                    btn_lanjut.setVisibility(View.GONE);
                }
                break;
            case R.id.btnmin:
                nilaijum_hari-=1;
                txjum_hari.setText(nilaijum_hari.toString());
                if (nilaijum_hari < 2){
                    btn_min.animate().alpha(0).setDuration(300).start();
                    btn_min.setEnabled(false);
                }
                if(nilaijum_hari<=nilai_sisa_cuti){
                    in_sisa.setTextColor(Color.parseColor("#5ABD8C"));
                    ln_notice.setVisibility(View.GONE);
                    btn_plus.setVisibility(View.VISIBLE);
                    btn_lanjut.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.button_back:
                Intent gomenu = new Intent(e_FormCuti.this, d_menuUtama.class);
                startActivity(gomenu);
                break;
            case R.id.button_lanjut:
                saveCutiOnDB();
                break;
        }
    }

    private void saveCutiOnDB() {
        String alasancuti = in_alasan.getText().toString();
        String alamatcuti = in_alamat.getText().toString();
        String nohpcuti = in_noHP.getText().toString();
        String tglmulaicuti = in_mulai.getText().toString();
        String tglselesaicuti = in_selesai.getText().toString();
        String tolakalasan = "";
        String suratcuti = "";
        masuknama = in_nama.getText().toString();
        masuknip = in_nip.getText().toString();
        masukjabatan = in_jabatan.getText().toString();
        //masuksisa = Integer.valueOf(in_sisa.getText().toString());
        jumhari = Integer.valueOf(txjum_hari.getText().toString());
        String cutistatus = "cekpegawai";
        if(validateInputs(alasancuti,alamatcuti,nohpcuti,tglmulaicuti,tglselesaicuti)){
            progressBar.setVisibility(View.VISIBLE);
            reference = FirebaseDatabase.getInstance().getReference("Pengajuan_Cuti");
            idcuti = reference.push().getKey();
            DataCuti cuti = new DataCuti(idcuti,masuknama,masuknip,masukjabatan,alasancuti,alamatcuti,nohpcuti,tglmulaicuti,tglselesaicuti,cutistatus,tolakalasan,suratcuti,username,jumhari);
            reference.child(idcuti).setValue(cuti);
            saveCutiUser();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(e_FormCuti.this, g_pengajuan_terkirim.class);
                    intent.putExtra("cutiid",idcuti);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            },3000);

        }
    }

    private boolean validateInputs(String alasanct,String alamatct,String nohpct,String tglmulai,String tglselesai){
        if (alasanct.isEmpty()){
            in_alasan.setError("Alasan Cuti Belum Diisi");
            in_alasan.requestFocus();
            return false;
        }
        if (alamatct.isEmpty()){
            in_alamat.setError("Alamat Cuti Belum Diisi");
            in_alamat.requestFocus();
            return false;
        }
        if (nohpct.isEmpty()){
            in_noHP.setError("No HP Belum Diisi");
            in_noHP.requestFocus();
            return false;
        }
        if (tglmulai.isEmpty()){
            in_mulai.setError("Tanggal Mulai Cuti Belum Diisi");
            return false;
        }
        if (tglselesai.isEmpty()){
            in_selesai.setError("Tanggal Selesai Cuti Belum Diisi");
            return false;
        }
        return true;
    }

    private void saveCutiUser() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("pegawai2").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    dataSnapshot.getRef().child("SISA_CUTI").setValue(nilai_sisa_cuti-jumhari);
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Terjadi Kesalahan ngurang", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showDatePicker(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                in_mulai.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void showDatePicker2(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                in_selesai.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void getInformationFromDB() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("pegawai2").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                in_nama.setText(Objects.requireNonNull(dataSnapshot.child("NAMA").getValue()).toString());
                in_nip.setText(Objects.requireNonNull(dataSnapshot.child("NIP").getValue()).toString());
                in_jabatan.setText(Objects.requireNonNull(dataSnapshot.child("JABATAN").getValue()).toString());
                in_sisa.setText(Objects.requireNonNull(dataSnapshot.child("SISA_CUTI").getValue()).toString());
                username = dataSnapshot.child("USERNAME").getValue().toString();
                nilai_sisa_cuti = Integer.valueOf(in_sisa.getText().toString());
                checkCutiUser();
                Picasso.with(e_FormCuti.this)
                        .load(dataSnapshot.child("FOTO").getValue().toString())
                        .into(foto, new Callback() {
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
                Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
