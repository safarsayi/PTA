package com.coverteam.pta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coverteam.pta.adapter.AdapterCuti;
import com.coverteam.pta.model.DataCuti;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class i_list_riwayat_cuti extends AppCompatActivity{

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    ListView listcuti;
    private List<DataCuti> listItems;
    DatabaseReference reference;
    TextView nama, nip;
    ImageView foto;
    ProgressBar progressBar;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_list_riwayat_cuti);
        getUsernameLocal();
        listcuti = findViewById(R.id.listviewCuti);
        listItems = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Pengajuan_Cuti");
        nama = findViewById(R.id.tvnamaprofil);
        nip = findViewById(R.id.tvnip);
        back = findViewById(R.id.buttonbackhome);
        foto = findViewById(R.id.fotouser);

        progressBar = findViewById(R.id.progressbar0);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gomenuutama = new Intent(i_list_riwayat_cuti.this, d_menuUtama.class);
                startActivity(gomenuutama);
            }
        });

        findCuti("cutiUsername",username_key_new);
        getInformationFromDB();
        selectListView();
    }
    private void selectListView() {
        listcuti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataCuti posisidata = listItems.get(position);
                Intent detailcuti = new Intent(i_list_riwayat_cuti.this, g_pengajuan_terkirim.class);
                detailcuti.putExtra("cutiid", posisidata.getCutiID());
                startActivity(detailcuti);
            }
        });
    }

    private void findCuti(String id, String name) {
        Query searchQuery = reference.orderByChild(id).equalTo(name);
        searchQuery.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener(){
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            LinearLayout cuti404 = findViewById(R.id.cuti_not_found);
            listItems.clear();
            if (dataSnapshot.exists()){
                cuti404.setVisibility(View.GONE);
                listcuti.setVisibility(View.VISIBLE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DataCuti dataCuti = snapshot.getValue(DataCuti.class);
                    listItems.add(dataCuti);
                }
                AdapterCuti adapter = new AdapterCuti(i_list_riwayat_cuti.this,listItems);
                Collections.reverse(listItems); // ADD THIS LINE TO REVERSE ORDER!
                adapter.notifyDataSetChanged();
                listcuti.setAdapter(adapter);

            }
            else {
                listcuti.setVisibility(View.GONE);
                cuti404.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void getInformationFromDB() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("pegawai2").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    nama.setText(dataSnapshot.child("NAMA").getValue().toString());
                    nip.setText(dataSnapshot.child("NIP").getValue().toString());
                    Picasso.with(i_list_riwayat_cuti.this)
                            .load(dataSnapshot.child("FOTO").getValue().toString())
                            .into(foto, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    Toast.makeText(getApplicationContext(),"Gagal Memuat Foto",Toast.LENGTH_SHORT).show();
                                }
                            });
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

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
