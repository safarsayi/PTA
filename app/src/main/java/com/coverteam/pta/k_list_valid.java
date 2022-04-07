package com.coverteam.pta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.coverteam.pta.adapter.AdapterCuti;
import com.coverteam.pta.model.DataCuti;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class k_list_valid extends AppCompatActivity {

    LinearLayout cuti404;

    ListView listviewcuti;
    DatabaseReference reference;
    List<DataCuti> cutiList;
    AdapterCuti adapterCuti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_k_list_valid);

        //item_one = findViewById(R.id.itemone);
        /*item_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goriwayat = new Intent(k_form_valid.this, j_validasi.class);
                startActivity(goriwayat);
            }
        });

         */
        listviewcuti = findViewById(R.id.listviewCuti);
        cuti404 = findViewById(R.id.cuti_not_found);
        reference = FirebaseDatabase.getInstance().getReference("Pengajuan_Cuti");
        cutiList = new ArrayList<>();
        adapterCuti = new AdapterCuti(k_list_valid.this,cutiList);
        listviewcuti.setAdapter(adapterCuti);

        listviewcuti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataCuti dataCuti = cutiList.get(position);
                Intent intent = new Intent(getApplicationContext(),j_validasi.class);
                intent.putExtra("cutiid",dataCuti.getCutiID());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cutiList.clear();
                for (DataSnapshot datacut: dataSnapshot.getChildren()){
                    DataCuti dataCuti = datacut.getValue(DataCuti.class);
                    cutiList.add(dataCuti);
                }
                AdapterCuti adapter = new AdapterCuti(k_list_valid.this,cutiList);
                Collections.reverse(cutiList);
                listviewcuti.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                cuti404.setVisibility(View.GONE);
                listviewcuti.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
