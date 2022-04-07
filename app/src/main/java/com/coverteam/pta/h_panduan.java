package com.coverteam.pta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.coverteam.pta.adapter.AdapterPanduan;
import com.coverteam.pta.listener.LFirebaseLoadDone;
import com.coverteam.pta.model.DataPanduan;
import com.coverteam.pta.transformer.DepthPageTransformer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class h_panduan extends AppCompatActivity implements LFirebaseLoadDone {

    ViewPager viewPager;
    AdapterPanduan adapterPanduan;
    ImageView back;

    DatabaseReference reference;
    LFirebaseLoadDone lFirebaseLoadDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_panduan);

        viewPager = findViewById(R.id.pagerpanduan);
        reference = FirebaseDatabase.getInstance().getReference("panduan");
        lFirebaseLoadDone = this;

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadTutor();
        viewPager.setPageTransformer(true,new DepthPageTransformer());
    }

    private void loadTutor() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            List<DataPanduan> dataPanduanList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot tutorSnapShot:dataSnapshot.getChildren())
                    dataPanduanList.add(tutorSnapShot.getValue(DataPanduan.class));
                lFirebaseLoadDone.onFirebaseLoadSuccess(dataPanduanList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                lFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
            }
        });
    }

    public void onFirebaseLoadSuccess(List<DataPanduan> dataPanduansList) {
        adapterPanduan = new AdapterPanduan(this,dataPanduansList);
        viewPager.setAdapter(adapterPanduan);
    }

    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
