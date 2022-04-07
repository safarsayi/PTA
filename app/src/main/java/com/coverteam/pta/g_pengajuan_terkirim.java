package com.coverteam.pta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class g_pengajuan_terkirim extends AppCompatActivity implements View.OnClickListener{

    private static final int PERMISSION_STORAGE_CODE = 1000;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    String idcuti,statuscuti;

    DatabaseReference pengajuancuti;
    DatabaseReference reference;
    TextView txsisacuti,in_nama,in_nip,in_jabatan,in_alasan,in_alamat,in_nohp,in_tgl_mulai,in_tgl_selesai;
    TextView statuspegawai,statusatasan,statuspejabat,txalasantolak;
    LinearLayout layalasan;
    ProgressBar progressBar;
    Button download;

    String tolakalasan,downloadfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_pengajuan_terkirim);

        getUsernameLocal();
        Intent intent = getIntent();
        idcuti = intent.getStringExtra("cutiid");

        txsisacuti = findViewById(R.id.sisa_cuti);
        in_nama = findViewById(R.id.in_nama);
        in_nip = findViewById(R.id.in_nip);
        in_jabatan = findViewById(R.id.in_jabatan);
        in_alamat = findViewById(R.id.in_alamat);
        in_alasan = findViewById(R.id.in_alasan);
        in_nohp = findViewById(R.id.in_hp);
        in_tgl_mulai = findViewById(R.id.in_tgl_mulai);
        in_tgl_selesai = findViewById(R.id.in_tgl_selesai);
        statuspegawai = findViewById(R.id.statuspegawai);
        statusatasan = findViewById(R.id.statusatasan);
        statuspejabat = findViewById(R.id.statuspejabat);
        progressBar = findViewById(R.id.progressbar);
        txalasantolak = findViewById(R.id.txalasantolak);
        layalasan = findViewById(R.id.layalasantolak);
        download = findViewById(R.id.download);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.download).setOnClickListener(this);
        getInformationFromDB();
        getPengajuanCutiFromDB();
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent home = new Intent(g_pengajuan_terkirim.this, i_list_riwayat_cuti.class);
                startActivity(home);
                break;
            case R.id.download:
                downloadFile();
                break;
        }
    }

    private void downloadFile() {
        if (!downloadfile.equals("")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions,PERMISSION_STORAGE_CODE);
                }
                else {
                    startDownload();
                }
            }
            else {
                startDownload();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Surat Cuti Belum Dikirim Admin!", Toast.LENGTH_LONG).show();
        }

    }

    private void startDownload() {
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(downloadfile);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadfile));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Mendownload Surat Cuti ...");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis()+"."+fileExtension);

        DownloadManager manager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_STORAGE_CODE:
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startDownload();
                }
                else {
                    Toast.makeText(this, "Gagal Mendownload File, PERMISSION DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
                    in_jabatan.setText(Objects.requireNonNull(dataSnapshot.child("cutiJabatan").getValue()).toString());
                    in_alamat.setText(Objects.requireNonNull(dataSnapshot.child("cutiAlamat").getValue()).toString());
                    in_alasan.setText(Objects.requireNonNull(dataSnapshot.child("cutiAlasan").getValue()).toString());
                    in_nohp.setText(Objects.requireNonNull(dataSnapshot.child("cutiNoHP").getValue()).toString());
                    in_tgl_mulai.setText(Objects.requireNonNull(dataSnapshot.child("cutiTglMulai").getValue()).toString());
                    in_tgl_selesai.setText(Objects.requireNonNull(dataSnapshot.child("cutiTglSelesai").getValue()).toString());
                    tolakalasan = dataSnapshot.child("cutiTolakAlasan").getValue().toString();
                    statuscuti = dataSnapshot.child("cutiStatus").getValue().toString();
                    downloadfile = dataSnapshot.child("cutiSurat").getValue().toString();
                    if (statuscuti.equals("ditolakpegawai")){
                        statuspegawai.setText("DATA DI TOLAK OLEH KEPEGAWAIAN");
                        layalasan.setVisibility(View.VISIBLE);
                        if (tolakalasan.equals("")){
                            txalasantolak.setText("-");
                        }
                        else{
                            txalasantolak.setText(tolakalasan);
                        }
                    }
                    else if (statuscuti.equals("cekatasan")){
                        statuspegawai.setText("DATA DI TERIMA OLEH KEPEGAWAIAN");
                        statuspegawai.setTextColor(Color.parseColor("#5ABD8C"));
                    }
                    else if (statuscuti.equals("ditolakatasan")){
                        statuspegawai.setText("DATA DI TERIMA OLEH KEPEGAWAIAN");
                        statuspegawai.setTextColor(Color.parseColor("#5ABD8C"));
                        statusatasan.setText("ATASAN MENOLAK PENGAJUAN CUTI");
                        layalasan.setVisibility(View.VISIBLE);
                        if (tolakalasan.equals("")){
                            txalasantolak.setText("-");
                        }
                        else{
                            txalasantolak.setText(tolakalasan);
                        }
                    }
                    else if (statuscuti.equals("cekpejabat")){
                        statuspegawai.setText("DATA DI TERIMA OLEH KEPEGAWAIAN");
                        statuspegawai.setTextColor(Color.parseColor("#5ABD8C"));
                        statusatasan.setText("PENGAJUAN CUTI DI TERIMA OLEH ATASAN");
                        statusatasan.setTextColor(Color.parseColor("#5ABD8C"));
                    }
                    else if (statuscuti.equals("ditolakpejabat")){
                        statuspegawai.setText("DATA DI TERIMA OLEH KEPEGAWAIAN");
                        statuspegawai.setTextColor(Color.parseColor("#5ABD8C"));
                        statusatasan.setText("PENGAJUAN CUTI DI TERIMA OLEH ATASAN");
                        statusatasan.setTextColor(Color.parseColor("#5ABD8C"));
                        statuspejabat.setText("PEJABAT MENOLAK PENGAJUAN CUTI");
                        layalasan.setVisibility(View.VISIBLE);
                        if (tolakalasan.equals("")){
                            txalasantolak.setText("-");
                        }
                        else{
                            txalasantolak.setText(tolakalasan);
                        }
                    }
                    else if (statuscuti.equals("allok")){
                        statuspegawai.setText("DATA DI TERIMA OLEH KEPEGAWAIAN");
                        statuspegawai.setTextColor(Color.parseColor("#5ABD8C"));
                        statusatasan.setText("PENGAJUAN CUTI DI TERIMA OLEH ATASAN");
                        statusatasan.setTextColor(Color.parseColor("#5ABD8C"));
                        statuspejabat.setText("PENGAJUAN CUTI DI TERIMA OLEH PEJABAT");
                        statuspejabat.setTextColor(Color.parseColor("#5ABD8C"));
                        download.setVisibility(View.VISIBLE);
                    }
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

    private void getInformationFromDB() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("pegawai2").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    txsisacuti.setText(Objects.requireNonNull(dataSnapshot.child("SISA_CUTI").getValue()).toString());
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

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
